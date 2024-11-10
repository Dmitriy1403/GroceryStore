package org.example.manageClasses;

import org.example.interfaces.ICustomerManager;
import org.example.interfaces.IProductManager;
import org.example.interfaces.IPurchaseManager;
import org.example.model.Customer;
import org.example.model.Product;
import org.example.model.Purchase;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class PurchaseManager implements IPurchaseManager {
    private IProductManager productManager;
    private ICustomerManager customerManager;

    public PurchaseManager(IProductManager productManager, ICustomerManager customerManager) {
        this.productManager = productManager;
        this.customerManager = customerManager;
    }

    @Override
    public void makePurchase(int customerId, int productId) {
        Customer customer = customerManager.getAllCustomers().stream()
                .filter(c -> c.getId() == customerId)
                .findFirst()
                .orElse(null);

        Product product = productManager.getAllProducts().stream()
                .filter(p -> p.getId() == productId)
                .findFirst()
                .orElse(null);

        if (customer != null && product != null && customer.hasSufficientBalance(product.getPrice())) {
            customer.deductBalance(product.getPrice());
            Purchase purchase = new Purchase(customerId, List.of(product), product.getPrice());
            savePurchaseInfo(purchase);
        } else {
            System.out.println("Недостаточно средств или товар не найден.");
        }
    }

    @Override
    public void savePurchaseInfo(Purchase purchase) {
        try (FileWriter writer = new FileWriter("purchases.txt", true)) {
            writer.write("Покупатель ID: " + purchase.getCustomerId() + "\n");
            writer.write("Дата покупки: " + purchase.getPurchaseDate() + "\n");
            writer.write("Список товаров:\n");
            for (Product product : purchase.getProducts()) {
                writer.write("- " + product.getName() + ": " + product.getPrice() + "\n");
            }
            writer.write("Сумма покупки: " + purchase.getTotalAmount() + "\n");
            writer.write("----\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
