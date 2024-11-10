package org.example.interfaces;

import org.example.model.Purchase;

public interface IPurchaseManager {
    void makePurchase(int customerId, int productId);
    void savePurchaseInfo(Purchase purchase);
}
