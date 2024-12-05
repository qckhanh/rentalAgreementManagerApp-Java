package org.rmit.database;

import org.rmit.entity.Owner;

import java.util.List;

public class OwnerDAO implements DAOInterface<Owner>{
    @Override
    public boolean add(Owner owner) {
        return false;
    }

    @Override
    public boolean update(Owner owner) {
        return false;
    }

    @Override
    public boolean delete(Owner owner) {
        return false;
    }

    @Override
    public Owner get(int id) {
        return null;
    }

    @Override
    public List<Owner> getAll() {
        return List.of();
    }

    @Override
    public void close() {

    }
}
