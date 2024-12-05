package org.rmit.database;

import org.hibernate.Session;
import org.rmit.entity.Host;

import java.util.List;

public class HostDAO implements DAOInterface<Host> {
    private final Session session;

    public HostDAO() {
        session = HibernateUtil.getSessionFactory().openSession();
    }

    @Override
    public boolean add(Host host) {
        return false;
    }

    @Override
    public boolean update(Host host) {
        return false;
    }

    @Override
    public boolean delete(Host host) {
        return false;
    }

    @Override
    public Host get(int id) {
        return null;
    }

    @Override
    public List<Host> getAll() {
        return null;
    }

    @Override
    public void close() {
        HibernateUtil.shutdown(session);
    }
}
