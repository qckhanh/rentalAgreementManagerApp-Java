package Manager;

import Database.Database;
import Model.*;
import UIHelper.UserInterfaceManager;
import UIHelper.DateCreator;
import UIHelper.InputValidator;

import java.util.*;

import static Model.RentalApplication.mergeSort;
import static UIHelper.InputValidator.getValidInput;
import static UIHelper.InputValidator.isValidOption;

public class PaymentManager implements MangerInterface {
    public static final Comparator<Payment> BY_ID = Comparator.comparingInt(Payment::getPaymentId);
    public static final Comparator<Payment> BY_METHOD = Comparator.comparing(Payment::getPaymentMethod);
    public static final Comparator<Payment> BY_DATE = Comparator.comparing(Payment::getDate);
    public static final Comparator<Payment> BY_AMOUNT = Comparator.comparingDouble(Payment::getAmount);

    private MangerInterface manager;
    private final Database db;
    private final List<RentalAgreement> agreements;
    private final List<Payment> payments;
    private final List<String> tableHeader = List.of(
            "Payment ID", "Agreement ID", "Property", "Renter", "Amount", "Date", "Method"
    );


    public PaymentManager(Database db) {
        this.db = db;
        agreements = (List<RentalAgreement>)db.getAll(RentalAgreement.class);
        payments = (List<Payment>)db.getAll(Payment.class);
    }

    private void setManagerType(String type){
        if(type.equals("owner")) manager = new OwnerManager(db);
        if(type.equals("property")) manager = new PropertyManager(db);
        else if(type.equals("renter")) manager = new RenterManager(db);
        else if(type.equals("host")) manager = new HostManager(db);
        else if(type.equals("agreement")) manager = new RentalAgreementManager(db);
        else if(type.equals("payment")) manager = new PaymentManager(db);
        else throw new IllegalArgumentException("Invalid manager type");
    }

    @Override
    public boolean add() {
        if(agreements.isEmpty()){
            UserInterfaceManager.errorMessage("No agreement found");
            return false;
        }

        this.setManagerType("agreement");
        UserInterfaceManager.printTable(
                "ALL AGREEMENTS",
                ((RentalAgreementManager)manager).getTableHeader(),
                manager.convertToTable(agreements)
        );
        this.setManagerType("payment");
        int index = (int) InputValidator.getValidInput(
                Integer.class,
                "Enter agreement index to add payment (0 to exit): ",
                input->InputValidator.isValidOption(input, 0, agreements.size())
        );
        if(index == 0) return false;
        RentalAgreement agreement = agreements.get(index - 1);

        if(agreement.getStatus().toString().equals("COMPLETED")){
            UserInterfaceManager.errorMessage("This agreement is already completed");
            return false;
        }
        Renter mainRenter = agreement.getMainTenant();
        Property property = agreement.getProperty();
        double amount = -1, requiredAmount = agreement.getRentingFee();

        while (true){
            amount = (double) InputValidator.getValidInput(
                    Double.class, "Enter payment amount: ",
                    InputValidator::isValidPrice
            );
            if(amount == requiredAmount) break;
            else UserInterfaceManager.errorMessage("Amount is not equal to required amount");
        }

        String paymentMethod = (String) InputValidator.getValidInput(
                String.class,
                "Enter payment method (cash/card): ",
                input->InputValidator.isValidType(input, Arrays.asList("CASH", "CARD"))
        );

        Date datepay = (Date) InputValidator.getValidInput(
                Date.class,
                "Enter payment date (dd/MM/yyyy): ",
                InputValidator::isValidDateFormat
        );

        if(!InputValidator.confirm("Do you want to add this payment? ")) return false;

        Payment newPayment = new Payment();
        newPayment.setAgreement(agreement);
        newPayment.setMainRenter(mainRenter);
        newPayment.setProperty(property);
        newPayment.setAmount(amount);
        newPayment.setPaymentMethod(PaymentMethod.valueOf(paymentMethod.toUpperCase()));
        newPayment.setDate(datepay);

        mainRenter.addPayment(newPayment);      // payment & renter
        agreement.getPayments().add(newPayment);

        boolean isDone = db.add(newPayment);
        if(isDone) UserInterfaceManager.successMessage("Payment added successfully");
        UserInterfaceManager.pressAnyKeyToContinue();
        return false;
    }

    @Override
    public boolean remove() {
        if(payments.isEmpty()){
            UserInterfaceManager.errorMessage("No payment found");
            return false;
        }
        UserInterfaceManager.printTable(
                "ALL PAYMENTS",
                tableHeader,
                convertToTable(payments)
        );

        int index = (int) InputValidator.getValidInput(Integer.class, "Enter payment index to remove (0 to exit): ",
                input->InputValidator.isValidOption(input, 0, payments.size()));
        if(index == 0) return false;
        Payment payment = payments.get(index - 1);
        payment.preview();
        if(!InputValidator.confirm("Do you want to delete? ")) return false;
        boolean isDeleted = db.delete(payment);
        if(isDeleted) UserInterfaceManager.successMessage("Payment removed successfully");
        else UserInterfaceManager.errorMessage("Failed to remove payment");
        return isDeleted;
    }

    @Override
    public boolean update() {
        UserInterfaceManager.errorMessage("Payments are not allowed to update");
        return false;
    }

    @Override
    public boolean displayAll() {
        if(payments.isEmpty()){
            UserInterfaceManager.errorMessage("No payment found");
            return false;
        }
        UserInterfaceManager.printTable("ALL PAYMENTS",
                tableHeader,
                this.convertToTable(payments)
        );
        int index = (int) InputValidator.getValidInput(Integer.class, "Enter payment index to view details (0 to exit): ",
                input->InputValidator.isValidOption(input, 0, payments.size())
        );
        if(index == 0) return false;
        Payment payment = payments.get(index - 1);
        payment.preview();
        UserInterfaceManager.pressAnyKeyToContinue();
        return true;
    }

    @Override
    public List<List<String>> convertToTable(List<?> objects) {
        List<List<String>> data = new ArrayList<>();
        for (Payment o: (List<Payment>)objects){
            List<String> tmp = new ArrayList<>();
            tmp.add(String.valueOf(o.getPaymentId()));
            tmp.add(String.valueOf(o.getAgreement().getAgreementId()));
            tmp.add(o.getProperty().getAddress());
            tmp.add(o.getMainRenter().getName());
            tmp.add(o.getAmount() + "");
            tmp.add(DateCreator.formatDate(o.getDate()));
            tmp.add(o.getPaymentMethod().toString());
            data.add(tmp);
        }
        return data;
    }

    @Override
    public void makeReport() {
        List<Payment> obj_cpy = new ArrayList<>(payments);
        if(obj_cpy.isEmpty()){
            UserInterfaceManager.errorMessage("No payments found");
            return;
        }
        UserInterfaceManager.printMenu("SORT PAYMENT'S INFORMATION BY: ",
                List.of("By ID",
                        "By price",
                        "By status",
                        "By Date",
                        "No filter"
                )
        );
        int opt = (int) getValidInput(Integer.class, "Select the filter(0 to default) : ", input->isValidOption(input, 0, 4));

        if(opt == 1) obj_cpy = mergeSort(obj_cpy, 0, obj_cpy.size() - 1, BY_ID);
        else if(opt == 2) obj_cpy = mergeSort(obj_cpy, 0, obj_cpy.size() - 1, BY_AMOUNT);
        else if(opt == 3) obj_cpy = mergeSort(obj_cpy, 0, obj_cpy.size() - 1, BY_METHOD);
        else if(opt == 4) obj_cpy = mergeSort(obj_cpy, 0, obj_cpy.size() - 1, BY_DATE);

        UserInterfaceManager.printTable("PAYMENT REPORT", tableHeader, convertToTable(obj_cpy));
        List<Payment> finalObj = obj_cpy;
        UserInterfaceManager.printToFile(MangerInterface.REPORT_PATH,
                out -> UserInterfaceManager.printTable(
                        "ALL PAYMENTS",
                        tableHeader,
                        convertToTable(finalObj)
                )
        );
//        AsciiTable.pressAnyKeyToContinue();
    }
}
