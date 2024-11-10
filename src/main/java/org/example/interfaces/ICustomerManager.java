package org.example.interfaces;

import org.example.model.Customer;

import java.util.List;

public interface ICustomerManager
{
    void addCustomer(Customer customer);
    void deleteCustomer(int customerId);
    List<Customer> getAllCustomers();
}
