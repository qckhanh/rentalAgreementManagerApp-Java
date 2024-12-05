package org.rmit.database;

import org.hibernate.Session;
import org.rmit.entity.Renter;

import java.util.List;

public class RenterDAO implements DAOInterface<Renter> {
    private final Session session;

    public RenterDAO() {
        session = HibernateUtil.getSessionFactory().openSession();
    }

    @Override
    public boolean add(Renter renter) {
        return false;
    }

    @Override
    public boolean update(Renter renter) {
        return false;
    }

    @Override
    public boolean delete(Renter renter) {
        return false;
    }

    @Override
    public Renter get(int id) {
        return null;
    }

    @Override
    public List<Renter> getAll() {
        return null;
    }

    @Override
    public void close() {
        HibernateUtil.shutdown(session);
    }
}
