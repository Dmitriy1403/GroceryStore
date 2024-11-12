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
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
        // Arrange
        // Ensure that all mocks return the correct values
        when(inputValidator.getValidUniqueProductName(anyString(), anyList())).thenReturn("Orange");
        when(inputValidator.getValidPositivePrice(anyString())).thenReturn(10.6); // Ensure price is positive and valid
        when(inputValidator.getValidIntInput(anyString())).thenReturn(10); // Ensure quantity is valid

        // Act
        shop.addProduct();

        // Assert
        // Verify that the productManager's addProduct method was called with the expected product attributes
        verify(productManager, times(1)).addProduct(argThat(product ->
                "Orange".equals(product.getName()) &&
                        product.getPrice() == 10.6 &&
                        product.getQuantity() == 10
        ));
    }

    @Test
    void testUpdateProduct() {
        // Setup
        Product existingProduct = new Product(1, "Apple", 2.0, 20);
        when(productManager.getAllProducts()).thenReturn(Collections.singletonList(existingProduct));

        // Настройка возвращаемых значений от inputValidator
        when(inputValidator.getValidExistingProductId("Введите ID продукта для обновления: ", productManager.getAllProducts())).thenReturn(1);
        when(inputValidator.getValidTextInput("Введите новое название продукта: ")).thenReturn("Banana");
        when(inputValidator.getValidPositivePrice("Введите новую цену продукта: ")).thenReturn(2.50); // Корректная цена
        when(inputValidator.getValidPositiveQuantity("Введите новое количество продукта: ")).thenReturn(28);

        // Act
        shop.updateProduct();

        // Проверяем, что updateProduct вызван с корректными аргументами
        verify(productManager, times(1)).updateProduct(eq(1), argThat(product ->
                "Banana".equals(product.getName()) &&
                        product.getPrice() == 2.50 &&
                        product.getQuantity() == 28
        ));

    }

    @Test
    void testDeleteProduct() {

        Product product = new Product(1, "Test Product", 10.0,5); // Example product with ID 1
        List<Product> productList = Arrays.asList(product);

        // Mock getAllProducts to return a list with the product having ID 1
        when(productManager.getAllProducts()).thenReturn(productList);
        when(inputValidator.getValidIntInput(anyString())).thenReturn(1);

        // Act
        shop.deleteProduct();

        // Verify
        verify(productManager, times(1)).deleteProduct(1);
    }

    @Test
    void testAddCustomer() {
        when(inputValidator.getValidTextInput("Введите имя покупателя: ")).thenReturn("John");
        when(inputValidator.getValidPositivePrice("Введите баланс покупателя")).thenReturn(500.0); // Adjusted method

        // Act
        shop.addCustomer();

        // Verify behavior
        verify(customerManager, times(1)).addCustomer(argThat(customer ->
                "John".equals(customer.getName()) &&
                        customer.getBalance().compareTo(BigDecimal.valueOf(500.0)) == 0
        ));
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
        when(inputValidator.getValidPositivePrice(anyString())).thenReturn(10.0);  // Correct price
        when(inputValidator.getValidPositiveQuantity(anyString())).thenReturn(-5);  // Negative quantity

        // Act
        shop.addProduct();

        // Verify: ensure the product is not added due to negative quantity
        verify(productManager, never()).addProduct(any(Product.class));
    }

    @Test
    void testUpdateProductWithInvalidData() {
        // Setup
        Product existingProduct = new Product(1, "Apple", 2.0, 20);
        when(productManager.getAllProducts()).thenReturn(Collections.singletonList(existingProduct));
        when(inputValidator.getValidIntInput("Введите ID продукта для обновления: ")).thenReturn(1);
        when(inputValidator.getValidTextInput("Введите новое название продукта: ")).thenReturn("Banana");
        when(inputValidator.getValidPositivePrice("Введите новую цену продукта: ")).thenReturn(-1.0); // Invalid price
        when(inputValidator.getValidIntInput("Введите новое количество продукта: ")).thenReturn(25);

        // Act
        shop.updateProduct();

        // Verify: ensure the product is not updated due to invalid price
        verify(productManager, never()).updateProduct(eq(1), any(Product.class));
    }

}   // @Test
    //void testDeleteNonExistentProduct() {
     //   // Setup
     //   when(inputValidator.getValidIntInput(anyString())).thenReturn(999);