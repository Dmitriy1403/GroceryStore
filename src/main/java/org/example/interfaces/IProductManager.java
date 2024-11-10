package org.example.interfaces;

import org.example.model.Product;

import java.util.List;

public interface IProductManager {
    void addProduct(Product product);
    void updateProduct(int productId, Product updatedProduct);
    void deleteProduct(int productId);
    List<Product> getAllProducts();
}
