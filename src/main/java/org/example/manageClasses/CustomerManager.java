package org.example.manageClasses;

import org.example.interfaces.ICustomerManager;
import org.example.model.Customer;

import java.util.ArrayList;
import java.util.List;

public class CustomerManager implements ICustomerManager {
    private List<Customer> customers = new ArrayList<>();

    @Override
    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    @Override
    public void deleteCustomer(int customerId) {
        customers.removeIf(customer -> customer.getId() == customerId);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customers;
    }
}
