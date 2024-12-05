package org.rmit.database;

import org.rmit.entity.RentalAgreement;

import java.util.List;

public class RentalAgreementDAO implements DAOInterface<RentalAgreement>{
    @Override
    public boolean add(RentalAgreement rentalAgreement) {
        return false;
    }

    @Override
    public boolean update(RentalAgreement rentalAgreement) {
        return false;
    }

    @Override
    public boolean delete(RentalAgreement rentalAgreement) {
        return false;
    }

    @Override
    public RentalAgreement get(int id) {
        return null;
    }

    @Override
    public List<RentalAgreement> getAll() {
        return List.of();
    }

    @Override
    public void close() {

    }
}
