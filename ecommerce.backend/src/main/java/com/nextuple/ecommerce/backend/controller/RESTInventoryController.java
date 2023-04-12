package com.nextuple.ecommerce.backend.controller;

import com.nextuple.ecommerce.backend.model.Inventory;
import com.nextuple.ecommerce.backend.model.Orders;
import com.nextuple.ecommerce.backend.repository.InventoryRepository;
import com.nextuple.ecommerce.backend.repository.OrdersRepository;
import com.nextuple.ecommerce.backend.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
//@Component
public class RESTInventoryController {
    @Autowired
    private InventoryService inventoryService;

    @PostMapping("/api/orders/purchaseOrder")
    public List<String> purchaseOrder(@RequestBody List<Inventory> newProducts) {
        List<String> messages = inventoryService.createProduct(newProducts);
        return messages;
    }

    @PostMapping("/api/orders/saleOrder")
    public List<String> saleOrder(@RequestBody List<Inventory> order) {
        List<String> messages = inventoryService.saleOrder(order);
        return messages;
    }

    @GetMapping("/api/inventoryDetails")
    public List<Inventory> inventoryDetails() {
        return inventoryService.inventoryDetails();
    }


    @PostMapping("/api/orders/deleteProduct")
    public List<String> deleteProduct(@RequestBody List<Inventory> productCodes) {
        List<String> messages = inventoryService.deleteProduct(productCodes);
        return messages;
    }

    @GetMapping("/api/deleteAll")
    public String deleteAll() {
        inventoryService.deleteAll();
        return "All the items from your Inventory is deleted.";
    }

    @PostMapping("/api/addStock")
    public List<String> addStock(@RequestBody List<Inventory> productDetails) {
        List<String> messages = inventoryService.addStock(productDetails);
        return messages;
    }

    @PostMapping("/api/updateProducts")
    public List<String> updateProduct(@RequestBody List<Inventory> productDetails) {
        List<String> messages = inventoryService.updateProduct(productDetails);
        return messages;
    }

    @GetMapping("/api/orders/transactions")
    public List<String> transactions() {
        List<String> results = inventoryService.transactions();
        return results;
    }

    @GetMapping("/api/orders/transactions/purchase")
    public List<Orders> purchaseTransaction() {
        List<Orders> results = inventoryService.purchaseOrders();
        return results;
    }

    @GetMapping("/api/orders/transactions/sale")
    public List<Orders> saleTransaction() {
        List<Orders> results = inventoryService.saleOrders();
        return results;
    }
}
