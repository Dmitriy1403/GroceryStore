package org.example.manageClasses;

import org.example.interfaces.IProductManager;
import org.example.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductManager implements IProductManager {

    private final List<Product> products = new ArrayList<>();

    @Override
    public void addProduct(Product product) {
        products.add(product);


    }
    @Override
    public void updateProduct(int productId, Product updatedProduct) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId() == productId) {
                products.set(i, updatedProduct);
                break;
            }
        }
    }

    @Override
    public void deleteProduct(int productId) {
        products.removeIf(product -> product.getId()==productId);

    }

    @Override
    public List<Product> getAllProducts() {
        return products;
    }
}
