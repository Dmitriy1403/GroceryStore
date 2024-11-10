package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.example.model.Customer;
import org.example.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;



class InputValidatorTest {
    private InputValidator inputValidator;
    private List<Customer> customers;
    private List<Product> products;

    private Scanner createScannerWithInput(String input) {
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        return new Scanner(inputStream);
    }

    @BeforeEach
    void setUp() {

        customers = Arrays.asList(
                new Customer(1, "Анна", 1000.0),
                new Customer(2, "Иван", 500.0)
        );
        products = Arrays.asList(
                new Product(1, "Яблоки", 1.99, 50),
                new Product(2, "Хлеб", 0.99, 30)
        );
    }


    @Test
    void getValidExistingCustomerId_ValidId_ReturnsId() {
        inputValidator = new InputValidator(createScannerWithInput("1\n")); // Simulates user entering "1"
        int validId = 1;
        assertEquals(validId, inputValidator.getValidExistingCustomerId("Введите ID покупателя: ", customers));
    }

    @Test
    void getValidTextInput_ValidText_ReturnsInput() {
        inputValidator = new InputValidator(createScannerWithInput("Яблоко\n"));
        String result = inputValidator.getValidTextInput("Введите текст:");
        assertEquals("Яблоко", result);
    }




    @Test
    void getValidExistingCustomerId_InvalidId_ShowsErrorMessage() {
        inputValidator = new InputValidator(createScannerWithInput("10\n1\n")); // Simulates entering "10" then "1"

        int validId = 1;
        assertEquals(validId, inputValidator.getValidExistingCustomerId("Введите ID покупателя: ", customers));
    }

    @Test
    void getValidTextInput_InvalidText_ShowsErrorUntilValid() {
        inputValidator = new InputValidator(createScannerWithInput("123\n!@#\nТекст\n"));
        String result = inputValidator.getValidTextInput("Введите текст:");
        assertEquals("Текст", result);
    }


    @Test
    void getValidDoubleInput_ValidDouble_ReturnsDouble() {
        inputValidator = new InputValidator(createScannerWithInput("12,5\n"));
        double result = inputValidator.getValidDoubleInput("Введите число:");
        assertEquals(12.5, result, 0.01);
    }
    @Test
    void getValidDoubleInput_InvalidDouble_ShowsErrorUntilValid() {
        inputValidator = new InputValidator(createScannerWithInput("abc\n12,5\n"));
        double result = inputValidator.getValidDoubleInput("Введите число:");
        assertEquals(12.5, result, 0.01);
    }

    @Test
    void getValidIntInput_ValidInt_ReturnsInt() {
        inputValidator = new InputValidator(createScannerWithInput("42\n"));
        int result = inputValidator.getValidIntInput("Введите целое число:");
        assertEquals(42, result);
    }

    @Test
    void getValidIntInput_InvalidInt_ShowsErrorUntilValid() {
        inputValidator = new InputValidator(createScannerWithInput("abc\n42\n"));
        int result = inputValidator.getValidIntInput("Введите целое число:");
        assertEquals(42, result);
    }

    @Test
    void getValidUniqueProductName_ValidUniqueName_ReturnsName() {
        inputValidator = new InputValidator(createScannerWithInput("Апельсин\n"));
        String result = inputValidator.getValidUniqueProductName("Введите название продукта:", products);
        assertEquals("Апельсин", result);
    }

    @Test
    void getValidUniqueProductName_ExistingName_ShowsErrorUntilUnique() {
        inputValidator = new InputValidator(createScannerWithInput("Яблоки\nХлеб\nВиноград\n"));
        String result = inputValidator.getValidUniqueProductName("Введите название продукта:", products);
        assertEquals("Виноград", result);
    }

    @Test
    void getValidExistingCustomerId_InvalidId_ShowsErrorUntilValid() {
        inputValidator = new InputValidator(createScannerWithInput("10\n2\n"));
        int result = inputValidator.getValidExistingCustomerId("Введите ID покупателя:", customers);
        assertEquals(2, result);
    }

    @Test
    void getValidExistingProductId_ValidId_ReturnsId() {
        inputValidator = new InputValidator(createScannerWithInput("2\n"));
        int result = inputValidator.getValidExistingProductId("Введите ID продукта:", products);
        assertEquals(2, result);
    }

    @Test
    void getValidExistingProductId_InvalidId_ShowsErrorUntilValid() {
        inputValidator = new InputValidator(createScannerWithInput("10\n1\n"));
        int result = inputValidator.getValidExistingProductId("Введите ID продукта:", products);
        assertEquals(1, result);
    }



}