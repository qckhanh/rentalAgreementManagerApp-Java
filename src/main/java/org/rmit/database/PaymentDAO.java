package org.rmit.database;

import org.rmit.entity.Payment;

import java.util.List;

public class PaymentDAO implements DAOInterface<Payment> {
    @Override
    public boolean add(Payment payment) {
        return false;
    }

    @Override
    public boolean update(Payment payment) {
        return false;
    }

    @Override
    public boolean delete(Payment payment) {
        return false;
    }

    @Override
    public Payment get(int id) {
        return null;
    }

    @Override
    public List<Payment> getAll() {
        return List.of();
    }

    @Override
    public void close() {

    }
}
