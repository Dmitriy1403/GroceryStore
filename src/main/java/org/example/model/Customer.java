package org.example.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Customer {
    private int id;
    private String name;
    private BigDecimal balance;


    public Customer(int id, String name, double balance) {
        this.id = id;
        this.name = name;
        this.balance = new BigDecimal(balance).setScale(2, RoundingMode.HALF_UP);
    }

    public boolean hasSufficientBalance(double amount) {
        return balance.compareTo(new BigDecimal(amount)) >= 0;

    }




    public void deductBalance(double amount) {
        BigDecimal amountToDeduct = new BigDecimal(amount);
        this.balance = this.balance.subtract(amountToDeduct).setScale(2, RoundingMode.HALF_UP);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getBalance() {
        return balance;
    }



    // Getters and Setters
}
