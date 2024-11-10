package org.example;

import org.example.model.Customer;
import org.example.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

public class PurchaseFileSaverTest {

    private PurchaseFileSaver purchaseFileSaver;
    private Customer customer;
    private Product product;

    @TempDir
    Path tempDir;  // Это временная директория для файлов, которая будет автоматически удалена после завершения теста.

    @BeforeEach
    void setUp() {
        // Создание объектов для теста
        purchaseFileSaver = new PurchaseFileSaver();
        customer = new Customer(1, "John", 500.0); // Примерный покупатель
        product = new Product(1, "Apple", 1.5, 20); // Примерный продукт
    }

    @Test
    void testSavePurchaseToFile() throws IOException {
        // Путь к файлу для записи информации о покупке
        File purchaseFile = tempDir.resolve("purchases.txt").toFile();

        // Выводим путь к файлу для отладки
        System.out.println("Путь к файлу: " + purchaseFile.getAbsolutePath());

        // Сохраняем покупку в файл
        purchaseFileSaver.savePurchaseToFile(customer, product, 2 );

        // Проверяем, что файл существует
        assertTrue(purchaseFile.exists(), "Файл не был создан");

        // Читаем содержимое файла и проверяем, что оно соответствует ожидаемому формату
        String content = new String(Files.readAllBytes(purchaseFile.toPath()));

        // Проверяем, что файл содержит ожидаемые данные
        assertTrue(content.contains("Дата:"));
        assertTrue(content.contains("Покупатель: John"));
        assertTrue(content.contains("Продукт: Apple"));
        assertTrue(content.contains("Количество: 2"));

        // Дополнительная проверка даты
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDate = dateFormat.format(new Date());
        assertTrue(content.contains(currentDate.substring(0, 10))); // Проверяем, что дата есть
    }





}