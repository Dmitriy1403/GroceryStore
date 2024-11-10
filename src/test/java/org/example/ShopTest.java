package org.example;


import org.example.interfaces.ICustomerManager;
import org.example.interfaces.IProductManager;
import org.example.interfaces.IPurchaseManager;
import org.example.model.Customer;
import org.example.model.Product;
import org.example.model.Purchase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class ShopTest {
    @Mock
    private IProductManager productManager;
    @Mock
    private ICustomerManager customerManager;
    @Mock
    private IPurchaseManager purchaseManager;
    @Mock
    private InputValidator inputValidator;
    @Mock
    private PurchaseFileSaver purchaseFileSaver;

    @InjectMocks
    private Shop shop;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddProduct() {
        // Setup
        when(inputValidator.getValidUniqueProductName(anyString(), anyList())).thenReturn("Orange");
        when(inputValidator.getValidDoubleInput(anyString())).thenReturn(1.50);
        when(inputValidator.getValidIntInput(anyString())).thenReturn(10);

        // Act
        shop.addProduct();

        // Verify
        verify(productManager, times(1)).addProduct(argThat(product ->
                product.getName().equals("Orange") &&
                        product.getPrice() == 1.50 &&
                        product.getQuantity() == 10
        ));
    }

    @Test
    void testUpdateProduct() {
        // Setup
        Product existingProduct = new Product(1, "Apple", 2.0, 20);
        when(productManager.getAllProducts()).thenReturn(Collections.singletonList(existingProduct));

        // Stubbing the inputValidator to return correct values step-by-step
        when(inputValidator.getValidIntInput("Введите ID продукта для обновления: ")).thenReturn(1);  // Product ID
        when(inputValidator.getValidTextInput("Введите новое название продукта: ")).thenReturn("Banana"); // New name
        when(inputValidator.getValidDoubleInput("Введите новую цену продукта: ")).thenReturn(1.20); // New price
        when(inputValidator.getValidIntInput("Введите новое количество продукта: ")).thenReturn(25); // New quantity

        // Act
        shop.updateProduct();

        // Verify the correct product is being updated
        verify(productManager, times(1)).updateProduct(eq(1), argThat(product ->
                product.getName().equals("Banana") &&
                        product.getPrice() == 1.20 &&
                        product.getQuantity() == 25
        ));
    }

    @Test
    void testDeleteProduct() {
        // Setup
        when(inputValidator.getValidIntInput(anyString())).thenReturn(1);

        // Act
        shop.deleteProduct();

        // Verify
        verify(productManager, times(1)).deleteProduct(1);
    }

    @Test
    void testAddCustomer() {
        // Setup
        when(inputValidator.getValidTextInput("Введите имя покупателя: ")).thenReturn("John");
        when(inputValidator.getValidDoubleInput("Введите баланс покупателя: ")).thenReturn(500.0);

        // Act
        shop.addCustomer();

        // Verify: ensure the customer added has the correct name and balance
        verify(customerManager, times(1)).addCustomer(argThat(customer -> {
            // Print out customer details to debug
            System.out.println("Customer added: Name = " + customer.getName() + ", Balance = " + customer.getBalance());

            // Perform the actual assertions
            return "John".equals(customer.getName()) &&
                    customer.getBalance().compareTo(BigDecimal.valueOf(500.0)) == 0;
        }));
    }

    @Test
    void testDeleteCustomer() {
        // Setup
        when(inputValidator.getValidIntInput(anyString())).thenReturn(1);

        // Act
        shop.deleteCustomer();

        // Verify
        verify(customerManager, times(1)).deleteCustomer(1);
    }

    @Test
    void testMakePurchase_SufficientBalanceAndStock() {
        // Setup
        Customer customer = new Customer(1, "Anna", 1000.0);
        Product product = new Product(1, "Apple", 2.0, 20);

        when(inputValidator.getValidExistingCustomerId(anyString(), anyList())).thenReturn(1);
        when(inputValidator.getValidExistingProductId(anyString(), anyList())).thenReturn(1);
        when(inputValidator.getValidIntInput(anyString())).thenReturn(5);
        when(customerManager.getAllCustomers()).thenReturn(Collections.singletonList(customer));
        when(productManager.getAllProducts()).thenReturn(Collections.singletonList(product));
        when(inputValidator.getValidTextInput(anyString())).thenReturn("нет");

        // Act
        shop.makePurchase();

        // Verify
        verify(purchaseManager, times(1)).savePurchaseInfo(any(Purchase.class));
        verify(purchaseFileSaver, times(1)).savePurchaseToFile(eq(customer), eq(product), eq(5));
        assertEquals(new BigDecimal("990.00"), customer.getBalance());
        assertEquals(15, product.getQuantity());
    }

    @Test
    void testMakePurchase_InsufficientBalance() {
        // Setup
        Customer customer = new Customer(1, "Anna", 5.0);
        Product product = new Product(1, "Apple", 2.0, 20);

        when(inputValidator.getValidExistingCustomerId(anyString(), anyList())).thenReturn(1);
        when(inputValidator.getValidExistingProductId(anyString(), anyList())).thenReturn(1);
        when(inputValidator.getValidIntInput(anyString())).thenReturn(5);
        when(customerManager.getAllCustomers()).thenReturn(Collections.singletonList(customer));
        when(productManager.getAllProducts()).thenReturn(Collections.singletonList(product));
        when(inputValidator.getValidTextInput(anyString())).thenReturn("нет");

        // Act
        shop.makePurchase();

        // Verify
        verify(purchaseManager, never()).savePurchaseInfo(any(Purchase.class));
        verify(purchaseFileSaver, never()).savePurchaseToFile(any(), any(), anyInt());
    }

    @Test
    void testMakePurchase_InsufficientStock() {
        // Setup
        Customer customer = new Customer(1, "Anna", 1000.0);
        Product product = new Product(1, "Apple", 2.0, 2);

        when(inputValidator.getValidExistingCustomerId(anyString(), anyList())).thenReturn(1);
        when(inputValidator.getValidExistingProductId(anyString(), anyList())).thenReturn(1);
        when(inputValidator.getValidIntInput(anyString())).thenReturn(5);
        when(customerManager.getAllCustomers()).thenReturn(Collections.singletonList(customer));
        when(productManager.getAllProducts()).thenReturn(Collections.singletonList(product));
        when(inputValidator.getValidTextInput(anyString())).thenReturn("нет");

        // Act
        shop.makePurchase();

        // Verify
        verify(purchaseManager, never()).savePurchaseInfo(any(Purchase.class));
        verify(purchaseFileSaver, never()).savePurchaseToFile(any(), any(), anyInt());
    }



    @Test
    void testAddProductWithInvalidData() {
        // Setup
        when(inputValidator.getValidUniqueProductName(anyString(), anyList())).thenReturn("Orange");
        when(inputValidator.getValidPositivePrice(anyString())).thenReturn(-1.50);  // Invalid price
        when(inputValidator.getValidIntInput(anyString())).thenReturn(10);

        // Act
        shop.addProduct();

        // Verify: ensure the product is not added due to invalid price
        verify(productManager, never()).addProduct(any(Product.class));
    }

    @Test
    void testAddProductWithNegativeQuantity() {
        // Setup
        when(inputValidator.getValidUniqueProductName(anyString(), anyList())).thenReturn("Orange");
        when(inputValidator.getValidPositivePrice(anyString())).thenReturn(10.0);  // Корректная цена
        when(inputValidator.getValidPositiveQuantity(anyString())).thenReturn(-5);  // Неверное количество (отрицательное)

        // Act
        shop.addProduct();

        // Verify: убедитесь, что продукт не был добавлен из-за отрицательного количества
        verify(productManager, never()).addProduct(any(Product.class));
    }


    @Test
    void testUpdateProductWithInvalidData() {
        // Setup
        Product existingProduct = new Product(1, "Apple", 2.0, 20);
        when(productManager.getAllProducts()).thenReturn(Collections.singletonList(existingProduct));
        when(inputValidator.getValidIntInput("Введите ID продукта для обновления: ")).thenReturn(1);  // Product ID
        when(inputValidator.getValidTextInput("Введите новое название продукта: ")).thenReturn("Banana"); // New name
        when(inputValidator.getValidPositivePrice("Введите новую цену продукта: ")).thenReturn(-1.0); // Invalid price
        when(inputValidator.getValidIntInput("Введите новое количество продукта: ")).thenReturn(25); // New quantity

        // Act
        shop.updateProduct();

        // Verify: ensure the product is not updated due to invalid price
        verify(productManager, never()).updateProduct(eq(1), any(Product.class));
    }

    @Test
    void testDeleteNonExistentProduct() {
        // Setup
        when(inputValidator.getValidIntInput(anyString())).thenReturn(999);  // Non-existent product ID

        // Act
        shop.deleteProduct();

        // Verify: ensure no product deletion occurs
        verify(productManager, never()).deleteProduct(999);
    }
    @Test
    void testAddCustomerWithInvalidBalance() {
        // Setup
        when(inputValidator.getValidTextInput("Введите имя покупателя: ")).thenReturn("John");
        when(inputValidator.getValidPositivePrice("Введите баланс покупателя: ")).thenReturn(-500.0);  // Invalid balance

        // Act
        shop.addCustomer();

        // Verify: ensure the customer is not added due to invalid balance
        verify(customerManager, never()).addCustomer(any(Customer.class));
    }

    @Test
    void testMakePurchase_WithExactBalance() {
        // Setup
        Customer customer = new Customer(1, "Anna", 10.0);
        Product product = new Product(1, "Apple", 5.0, 10);

        // Mock the inputValidator methods
        when(inputValidator.getValidExistingCustomerId(anyString(), anyList())).thenReturn(1); // Customer ID 1
        when(inputValidator.getValidExistingProductId(anyString(), anyList())).thenReturn(1); // Product ID 1
        when(inputValidator.getValidIntInput(anyString())).thenReturn(2);  // Trying to buy 2 apples

        // Mock the managers to return the customer and product
        when(customerManager.getAllCustomers()).thenReturn(Collections.singletonList(customer));
        when(productManager.getAllProducts()).thenReturn(Collections.singletonList(product));

        // Act
        shop.makePurchase(); // This should execute the purchase logic

        // Verify the correct balance deduction and purchase save
        verify(purchaseManager, times(1)).savePurchaseInfo(any(Purchase.class));
        verify(purchaseFileSaver, times(1)).savePurchaseToFile(eq(customer), eq(product), eq(2));

        // Assert that the customer's balance has been correctly updated to 0.00
        assertEquals(new BigDecimal("0.00"), customer.getBalance());

        // Assert the product quantity has been reduced by 2
        assertEquals(8, product.getQuantity());
    }

//    @Test
//    void testMakePurchase_ProductQuantityDepletion() {
//        // Setup
//        Customer customer = new Customer(1, "Anna", 1000.0);
//        Product product = new Product(1, "Apple", 2.0, 2);  // Only 2 apples available
//
//        // Mock the inputValidator methods
//        when(inputValidator.getValidExistingCustomerId(anyString(), anyList())).thenReturn(1); // Customer ID 1
//        when(inputValidator.getValidExistingProductId(anyString(), anyList())).thenReturn(1); // Product ID 1
//        when(inputValidator.getValidIntInput(anyString())).thenReturn(2);  // Buying all available apples
//        when(inputValidator.getValidTextInput(anyString())).thenReturn("yes"); // Mock valid response for confirmation
//
//        // Mock the managers to return the customer and product
//        when(customerManager.getAllCustomers()).thenReturn(Collections.singletonList(customer));
//        when(productManager.getAllProducts()).thenReturn(Collections.singletonList(product));
//
//        // Act
//        shop.makePurchase(); // This should execute the purchase logic
//
//        // Verify: ensure product quantity is updated to 0 after the purchase
//        assertEquals(0, product.getQuantity());
//        // Optionally, verify the balance after the purchase if needed
//        assertEquals(996.0, customer.getBalance());
//    }





}

