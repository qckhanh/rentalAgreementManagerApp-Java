package Model;

import Database.*;
import Manager.*;
import UIHelper.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static UIHelper.InputValidator.isValidOption;

public class RentalApplication {
    private final int MAX_OPTION = 5;

    private final Database db;
    public RentalApplication() {
        db = Database.getInstance();
    }


    private MangerInterface manager;      //strategy pattern
    public void setManagerType(String type) {
        switch (type) {
            case "renter" -> manager = new RenterManager(db);
            case "property" -> manager = new PropertyManager(db);
            case "host" -> manager = new HostManager(db);
            case "owner" -> manager = new OwnerManager(db);
            case "agreement" -> manager = new RentalAgreementManager(db);
            case "payment" -> manager = new PaymentManager(db);
            default -> throw new IllegalArgumentException("Invalid manager type");
        }
    }

    private boolean option(int userOption){
        if(userOption == 0) return false;  // return false ==> stop the loop
        else if(userOption == 1) this.manager.add();
        else if(userOption == 2) this.manager.remove();
        else if(userOption == 3) this.manager.update();
        else if(userOption == 4) this.manager.displayAll();
        else if(userOption == 5) this.manager.makeReport();

        return true;  // always return true ==> continue the application
    }

    public void start(){
        MenuOptionsManager.decor();
        while(true){
            MenuOptionsManager.mainMenu();
            Integer opt = (Integer) InputValidator.getValidInput(Integer.class, "Enter your option: ",
                    input->isValidOption(input, 0, 6));
            if(opt == 0) break;
            else if(opt == 1) this.MenuOf("agreement");
            else if(opt == 2) this.MenuOf("property");
            else if(opt == 3) this.MenuOf("host");
            else if(opt == 4) this.MenuOf("renter");
            else if(opt == 5) this.MenuOf("owner");
            else if(opt == 6) this.MenuOf("payment");
        }
        db.saveData();
    }

    private void MenuOf(String type){
        if(type.equals("host")){
            hostMenu();
            return;
        }

        this.setManagerType(type);     // set the manager type
        while(true){
            switch (type) {      // select its corresponding menu
                case "renter" -> MenuOptionsManager.RenterManagerMenu();
                case "property" -> MenuOptionsManager.PropertyManagerMenu();
                case "owner" -> MenuOptionsManager.OwnerManagerMenu();
                case "agreement" -> MenuOptionsManager.AgreementManagerMenu();
                case "payment" -> MenuOptionsManager.PaymentManagerMenu();
                default -> throw new IllegalArgumentException("Invalid manager type");
            }
            int opt = (int) InputValidator.getValidInput(
                    Integer.class,
                    "Enter your option: ",
                    input->isValidOption(input, 0, MAX_OPTION)
            );
            if(!this.option(opt)) break;     // if this return false, break the loop
        }
    }

    private void hostMenu(){
        this.setManagerType("host");
        while(true){
            MenuOptionsManager.HostManagerMenu();
            int opt = (int) InputValidator.getValidInput(Integer.class, "Enter your option: ",
                    input->isValidOption(input, 0, MAX_OPTION + 2));

            if(opt <= MAX_OPTION){
                if (!this.option(opt)) break;
            }
            else if(opt == 6) ((HostManager)manager).addToOwner();
            else if(opt == 7) ((HostManager)manager).addToProperty();
        }
    }

    public static <T> List<T> mergeSort(List<T> array, int left, int right, Comparator<? super T> comparator) {
        if (left >= right) {
            return List.of(array.get(left));
        }
        int mid = (left + right) / 2;
        List<T> leftSorted = mergeSort(array, left, mid, comparator);
        List<T> rightSorted = mergeSort(array, mid + 1, right, comparator);
        return merge(leftSorted, rightSorted, comparator);
    }

    private static <T> List<T> merge(List<T> leftHand, List<T> rightHand, Comparator<? super T> comparator) {
        List<T> sorted = new ArrayList<>(leftHand.size() + rightHand.size());
        int i = 0, j = 0;

        while (i < leftHand.size() && j < rightHand.size()) {
            if (comparator.compare(leftHand.get(i), rightHand.get(j)) >= 0) {
                sorted.add(leftHand.get(i));
                i++;
            } else {
                sorted.add(rightHand.get(j));
                j++;
            }
        }

        while (i < leftHand.size()) {
            sorted.add(leftHand.get(i));
            i++;
        }

        while (j < rightHand.size()) {
            sorted.add(rightHand.get(j));
            j++;
        }
        return sorted;
    }

}
