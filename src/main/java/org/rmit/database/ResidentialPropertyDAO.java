package org.rmit.database;

import org.rmit.entity.ResidentialProperty;

import java.util.List;

public class ResidentialPropertyDAO implements DAOInterface<ResidentialProperty> {

    @Override
    public boolean add(ResidentialProperty residentialProperty) {
        return false;
    }

    @Override
    public boolean update(ResidentialProperty residentialProperty) {
        return false;
    }

    @Override
    public boolean delete(ResidentialProperty residentialProperty) {
        return false;
    }

    @Override
    public ResidentialProperty get(int id) {
        return null;
    }

    @Override
    public List<ResidentialProperty> getAll() {
        return List.of();
    }

    @Override
    public void close() {

    }
}
