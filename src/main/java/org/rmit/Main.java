package org.rmit;

import org.rmit.Helper.EntityGraphUtils;
import org.rmit.database.CommercialPropertyDAO;
import org.rmit.database.RentalAgreementDAO;

public class Main {
    public static void main(String[] args) {
        RentalAgreementApplication.main(args);


//        System.out.println(new CommercialPropertyDAO().getAll(EntityGraphUtils::SimpleCommercialPropertyFull).size());
//        RentalAgreementDAO rentalAgreementDAO = new RentalAgreementDAO();
//        System.out.println(rentalAgreementDAO.getAll(EntityGraphUtils::SimpleRentalAgreementFull).size());
    }
}
