package org.rmit.database;

import org.rmit.entity.CommercialProperty;

import java.util.List;

public class CommercialPropertyDAO implements DAOInterface<CommercialProperty> {

    @Override
    public boolean add(CommercialProperty commercialProperty) {
        return false;
    }

    @Override
    public boolean update(CommercialProperty commercialProperty) {
        return false;
    }

    @Override
    public boolean delete(CommercialProperty commercialProperty) {
        return false;
    }

    @Override
    public CommercialProperty get(int id) {
        return null;
    }

    @Override
    public List<CommercialProperty> getAll() {
        return List.of();
    }

    @Override
    public void close() {

    }
}
