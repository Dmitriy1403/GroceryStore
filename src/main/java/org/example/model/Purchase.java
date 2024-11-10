package org.example.model;

import java.time.LocalDateTime;
import java.util.List;

public class Purchase {
    private int customerId;
    private List<Product> products;
    private double totalAmount;
    private LocalDateTime purchaseDate;

    public Purchase(int customerId, List<Product> products, double totalAmount) {
        this.customerId = customerId;
        this.products = products;
        this.totalAmount = totalAmount;
        this.purchaseDate = LocalDateTime.now();
    }

    public int getCustomerId() {
        return customerId;
    }



    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }


    // Getters and Setters
}
