package org.example;

import org.example.model.Customer;
import org.example.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PurchaseFileSaverTest {

    private PurchaseFileSaver purchaseFileSaver;
    private Customer mockCustomer;
    private Product mockProduct;
    private final String filePath = "purchases.txt";

    @BeforeEach
    void setUp() throws IOException {
        // Создаем новый объект PurchaseFileSaver
        purchaseFileSaver = new PurchaseFileSaver();

        // Мокаем объекты Customer и Product
        mockCustomer = mock(Customer.class);
        mockProduct = mock(Product.class);

        // Настраиваем моки
        when(mockCustomer.getName()).thenReturn("Иван Иванов");
        when(mockProduct.getName()).thenReturn("Холодильник");

        // Очищаем файл перед каждым тестом
        Files.deleteIfExists(Path.of(filePath));
    }

    @Test
    void testSavePurchaseToFile() throws IOException {
        // Проверяем, что данные корректно записываются в файл
        int quantity = 2;
        purchaseFileSaver.savePurchaseToFile(mockCustomer, mockProduct, quantity);

        // Считываем записанное значение
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine();

            // Получаем текущую дату для сравнения с записью
            String expectedDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

            // Проверяем, что в файле присутствует запись о покупке с нужными значениями
            assertTrue(line.contains("Дата: " + expectedDate));
            assertTrue(line.contains("Покупатель: Иван Иванов"));
            assertTrue(line.contains("Продукт: Холодильник"));
            assertTrue(line.contains("Количество: " + quantity));
        }
    }

    @Test
    void testIOExceptionHandling() throws IOException {
        // Проверка обработки IOException
        try {
            // Создаем временный файл с правами "только для чтения", чтобы вызвать IOException
            Path filePath = Path.of("purchases.txt");
            Files.createFile(filePath);
            Files.setPosixFilePermissions(filePath, Set.of(PosixFilePermission.OWNER_READ));

            // Выполняем тест, ожидая, что IOException будет обработано
            purchaseFileSaver.savePurchaseToFile(mockCustomer, mockProduct, 1);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Удаляем временный файл после теста
            Files.deleteIfExists(Path.of(filePath));
        }
    }
}





