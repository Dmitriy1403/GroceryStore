package org.example;
import java.util.Locale;

import org.example.input.ConsoleInput;
import org.example.interfaces.ICustomerManager;
import org.example.interfaces.IProductManager;
import org.example.interfaces.IPurchaseManager;
import org.example.interfaces.Input;
import org.example.manageClasses.CustomerManager;
import org.example.manageClasses.ProductManager;
import org.example.manageClasses.PurchaseManager;
import org.example.model.Customer;
import org.example.model.Product;
import org.example.model.Purchase;
import org.example.InputValidator;

import java.util.List;
import java.util.Scanner;

public class Shop {
    private IProductManager productManager;
    private ICustomerManager customerManager;
    private IPurchaseManager purchaseManager;

    private InputValidator inputValidator;
    private Input input;
    private PurchaseFileSaver purchaseFileSaver;

    public Shop() {
        this.productManager = new ProductManager();
        this.customerManager = new CustomerManager();
        this.purchaseManager = new PurchaseManager(productManager, customerManager);

        this.purchaseFileSaver = new PurchaseFileSaver();
        this.input = new ConsoleInput();
        this.inputValidator = new InputValidator(input);

        // Добавляем начальные данные
        initializeProducts();
        initializeCustomers();
    }



    // Метод для инициализации продуктов питания с количеством
    private void initializeProducts() {
        productManager.addProduct(new Product(1, "Яблоки", 1.99, 50));
        productManager.addProduct(new Product(2, "Хлеб", 0.99, 30));
        productManager.addProduct(new Product(3, "Молоко", 0.89, 20));
        productManager.addProduct(new Product(4, "Сыр", 3.49, 15));
        productManager.addProduct(new Product(5, "Яйца (десяток)", 2.99, 25));
        System.out.println("Продукты успешно инициализированы.");
    }

    // Метод для инициализации покупателей с балансом
    private void initializeCustomers() {
        customerManager.addCustomer(new Customer(1, "Анна", 1000.00));
        customerManager.addCustomer(new Customer(2, "Иван", 500.00));
        customerManager.addCustomer(new Customer(3, "Ольга", 750.00));
        customerManager.addCustomer(new Customer(4, "Дмитрий", 1200.00));
        System.out.println("Покупатели успешно инициализированы.");
    }

    public void start() {
        System.out.println("Добро пожаловать в интернет-магазин продуктов питания!");

        boolean running = true;
        while (running) {
            System.out.println("\nВыберите опцию:");
            System.out.println("1. Добавить продукт");
            System.out.println("2. Изменить продукт");
            System.out.println("3. Удалить продукт");
            System.out.println("4. Показать все продукты");
            System.out.println("5. Добавить покупателя");
            System.out.println("6. Удалить покупателя");
            System.out.println("7. Показать всех покупателей");
            System.out.println("8. Покупка продукта");
            System.out.println("9. Выход");

            int choice = input.nextInt();
            input.nextLine();  // Чтение новой строки после nextInt

            switch (choice) {
                case 1 -> addProduct();
                case 2 -> updateProduct();
                case 3 -> deleteProduct();
                case 4 -> showAllProducts();
                case 5 -> addCustomer();
                case 6 -> deleteCustomer();
                case 7 -> showAllCustomers();
                case 8 -> makePurchase();
                case 9 -> {
                    running = false;
                    System.out.println("Выход из программы.");
                }
                default -> System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }

    public void addProduct() {
        System.out.println("Список продуктов перед добавлением:");
        showAllProducts();


        String name = inputValidator.getValidUniqueProductName("Введите название продукта",productManager.getAllProducts());
        double price = inputValidator.getValidPositivePrice("ведите цену продукта");

        if (price <= 0) {
            System.out.println("Ошибка: цена не может быть отрицательной или нулевой. Продукт не добавлен.");
            return;
        }

        int quantity = inputValidator.getValidPositiveQuantity("Введите количество продукта: ");
        if (quantity<=0){
            System.out.println("Ошибка: количество не может быть отрицательным или нулевым. Продукт не добавлен.");
            return;
        }
        int id = productManager.getAllProducts().size() + 1;

        Product product = new Product(id, name, price, quantity);
        productManager.addProduct(product);
        System.out.println("Продукт добавлен успешно.");
        System.out.println("Список продуктов после  добавления:");
        showAllProducts();

    }

   public  void updateProduct() {
        System.out.println("Список продуктов перед изменением:");
        showAllProducts();

        int productId = inputValidator.getValidExistingProductId("Введите ID продукта для обновления: ",productManager.getAllProducts());

        String name = inputValidator.getValidTextInput("Введите новое название продукта: ");


        double price = inputValidator.getValidPositivePrice("Введите новую цену продукта: ");

       if (price <= 0) {
           System.out.println("Ошибка: цена не может быть отрицательной или нулевой. Продукт не изменен.");
           return;
       }

        int quantity = inputValidator.getValidPositiveQuantity("Введите новое количество продукта: ");

       if (price <= 0) {
           System.out.println("Ошибка: Количество не может быть отрицательной или нулевой. Продукт не изменен.");
           return;
       }


        Product updatedProduct = new Product(productId, name, price, quantity);
        productManager.updateProduct(productId, updatedProduct);
        System.out.println("Продукт обновлен.");
        System.out.println("Обновленный список продуктов");
        showAllProducts();
    }

   public void deleteProduct() {
        System.out.println("Список продуктов перед удалением:");
        showAllProducts();

        int productId = inputValidator.getValidIntInput("Введите ID продукта для удаления: ");
       boolean productExists = productManager.getAllProducts().stream()
               .anyMatch(product -> product.getId() == productId);

       if (!productExists) {
           System.out.println("Ошибка: продукт с таким ID не найден.");
           return; // Прерываем выполнение метода, если продукт не найден
       }
        productManager.deleteProduct(productId);
        System.out.println("Продукт удален.");
        System.out.println("Обновленный список продуктов");
        showAllProducts();
    }

    public void showAllProducts() {
        System.out.println("Список продуктов:");
        for (Product product : productManager.getAllProducts()) {
            System.out.println("- ID: " + product.getId() + ", Название: " + product.getName() +
                    ", Цена: " + product.getPrice() + ", Количество: " + product.getQuantity());
        }
    }

    public void addCustomer() {
        System.out.print("Список покупателей перед добавлением:");
        showAllCustomers();

        System.out.print("Введите имя покупателя: ");
        String name =  inputValidator.getValidTextInput("Введите имя покупателя: ");
        System.out.print("Введите баланс покупателя: ");
        double balance = inputValidator.getValidPositivePrice("Введите баланс покупателя");
        if (balance <= 0) {
            System.out.println("Ошибка: Баланс не может быть отрицательным или нулевым. Покупатель не добавлен");
            return;
        }

        int id = customerManager.getAllCustomers().size() + 1;

        Customer customer = new Customer(id, name, balance);
        customerManager.addCustomer(customer);
        System.out.println("Покупатель добавлен.");
        System.out.println("Обновленный список покупателей");
        showAllCustomers();
    }





    public void deleteCustomer() {
        System.out.print("Список покупателей перед удалением:");
        showAllCustomers();

        int customerId = inputValidator.getValidIntInput("Введите ID покупателя для удаления: ");
        customerManager.deleteCustomer(customerId);
        System.out.println("Покупатель удален.");
        System.out.println("Обновленный список покупателей");
        showAllCustomers();
    }

    private void showAllCustomers() {
        System.out.println("Список покупателей:");
        for (Customer customer : customerManager.getAllCustomers()) {
            System.out.println("- ID: " + customer.getId() + ", Имя: " + customer.getName() +
                    ", Баланс: " + customer.getBalance());
        }
    }

    public void makePurchase() {
        boolean continueShopping = true;

        while (continueShopping) {
            System.out.print("Список покупателей кооторые будут совершать покупку :");
            showAllCustomers();

            int customerId = inputValidator.getValidExistingCustomerId("Введите ID покупателя: ",customerManager.getAllCustomers());
            System.out.print("Список продуктов для продажи :");
            showAllProducts();

            int productId = inputValidator.getValidExistingProductId("Введите ID продукта: ",productManager.getAllProducts());

            int quantity = inputValidator.getValidIntInput("Введите количество для покупки: ");

            Customer customer = customerManager.getAllCustomers().stream()
                    .filter(c -> c.getId() == customerId)
                    .findFirst()
                    .orElse(null);

            Product product = productManager.getAllProducts().stream()
                    .filter(p -> p.getId() == productId)
                    .findFirst()
                    .orElse(null);

            if (customer != null && product != null) {
                if (product.getQuantity() >= quantity) {
                    double totalCost = product.getPrice() * quantity;
                    if (customer.hasSufficientBalance(totalCost)) {
                        customer.deductBalance(totalCost);
                        product.reduceQuantity(quantity);
                        Purchase purchase = new Purchase(customerId, List.of(product), totalCost);
                        purchaseManager.savePurchaseInfo(purchase);
                        purchaseFileSaver.savePurchaseToFile(customer, product, quantity);

                        System.out.println("Покупка успешно совершена. Остаток товара: " + product.getQuantity());
                        System.out.println("Баланс покупателя: " + customer.getName() + "=" + customer.getBalance().toString());
                        ;

                    } else {
                        System.out.println("Недостаточно средств у покупателя.");
                    }
                } else {
                    System.out.println("Недостаточное количество товара.");
                }
            } else {
                System.out.println("Неверный ID покупателя или продукта.");
            }
                String userChoice = inputValidator.getValidTextInput("Хотите продолжить покупки?(да/нет)").toLowerCase();
                if(userChoice !=null && userChoice.toLowerCase().equals("да")) {

                        continueShopping = true;

                    } else if (userChoice.toLowerCase().equals("нет")) {
                        continueShopping = false;

                    } else {

                        System.out.println("Неверный ввод. Попробуйте снова.");
                    }



        }
    }



}
