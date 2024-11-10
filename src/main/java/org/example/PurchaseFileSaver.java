package org.example;
import org.example.model.Customer;
import org.example.model.Product;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PurchaseFileSaver {

    public void savePurchaseToFile(Customer customer, Product product, int quantity) {
        // Форматирование текущей даты
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDate = dateFormat.format(new Date());

        // Запись информации о покупке в файл
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("purchases.txt", true))) {
            String purchaseInfo = String.format("Дата: %s, Покупатель: %s, Продукт: %s, Количество: %d\n",
                    currentDate, customer.getName(), product.getName(), quantity);
            writer.write(purchaseInfo);
            System.out.println("Информация о покупке сохранена в файл.");
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении информации о покупке в файл: " + e.getMessage());
        }
    }

}
