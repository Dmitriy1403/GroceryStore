package org.example.model;

import java.util.Objects;

public class Product {
    private int id;
    private String name;
    private double price;
    private int quantity; // Новое поле для количества

    public Product(int id, String name, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    // Проверка, есть ли товар в наличии
    public boolean isAvailable(int requestedQuantity) {
        return quantity >= requestedQuantity;
    }

    // Метод для уменьшения количества после покупки
    public void reduceQuantity(int amount) {
        if (isAvailable(amount)) {
            this.quantity -= amount;
        }
    }

    // Getters и Setters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id &&
                Double.compare(product.price, price) == 0 &&
                quantity == product.quantity &&
                Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, quantity);
    }
}
