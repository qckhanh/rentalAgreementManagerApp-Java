package org.rmit.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "renter")
public class Renter extends Person{
    private int numOfPayments;
    private int numOfRentals;


    public Renter() {
        super();
    }

    public Renter(String name, int age, int numOfPayments, int numOfRentals) {
        super(name, age);
        this.numOfPayments = numOfPayments;
        this.numOfRentals = numOfRentals;
    }

    public int getNumOfPayments() {
        return numOfPayments;
    }

}
