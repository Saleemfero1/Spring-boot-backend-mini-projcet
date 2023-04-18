package com.nextuple.ecommerce.backend.controller;
import com.nextuple.ecommerce.backend.model.Inventory;
import com.nextuple.ecommerce.backend.model.Orders;
import com.nextuple.ecommerce.backend.service.InventoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RESTInventoryControllerTest {
    @InjectMocks
    private RESTInventoryController restInventoryController;

    @Mock
    private InventoryService inventoryService;

    //this test case is pending
    @Test
    void purchaseOrder() {
        List<Inventory> productList = new ArrayList<>();
        productList.add(new Inventory("42", "Product Name", "Product Category", 1, 1, "Product Code"));
        productList.add(new Inventory("43", "Product Name2", "Product Category", 1, 1, "Product Code2"));
        productList.add(new Inventory("45", "Product Name3", "Product Category", 1, 1, "Product Code3"));

        List<String> successMessages = Arrays.asList("Product 'product1' created successfully", "Product 'product2' created successfully", "Product 'product3' created successfully");
        when(inventoryService.createProduct(productList)).thenReturn(successMessages);

        List<String> messages = restInventoryController.purchaseOrder(productList).getBody();
        assertEquals(successMessages, messages);
    }

    void purchaseOrder1() {
        List<Inventory> productList = new ArrayList<>();
        assertTrue(restInventoryController.purchaseOrder(productList).getBody().isEmpty());
    }

    @Test
    void saleOrder() {
        List<Inventory> productList = new ArrayList<>();
        productList.add(new Inventory("42", "Product Name", "Product Category", 1, 1, "Product Code"));
        productList.add(new Inventory("43", "Product Name2", "Product Category", 1, 1, "Product Code2"));
        productList.add(new Inventory("45", "Product Name3", "Product Category", 1, 1, "Product Code3"));

        List<String> successMessages = Arrays.asList("Product Product Code purchased. Cost: ₹1", "Product Product Code purchased. Cost: ₹1", "Product Product Code purchased. Cost: ₹1");

        when(inventoryService.createProduct(productList)).thenReturn(successMessages);

        List<String> messages = restInventoryController.purchaseOrder(productList).getBody();
        assertEquals(successMessages, messages);
    }

    void saleOrder1() {
        List<Inventory> productList = new ArrayList<>();
        assertTrue(restInventoryController.saleOrder(productList).getBody().isEmpty());
    }

    @Test
    void inventoryDetails() {
        List<Inventory> productList = new ArrayList<>();
        productList.add(new Inventory("42", "Product Name", "Product Category", 1, 1, "Product Code"));
        productList.add(new Inventory("43", "Product Name2", "Product Category", 1, 1, "Product Code2"));
        productList.add(new Inventory("45", "Product Name3", "Product Category", 1, 1, "Product Code3"));

        when(inventoryService.inventoryDetails()).thenReturn(productList);
        List<Inventory> actualInventory = restInventoryController.inventoryDetails().getBody();
        assertEquals(productList, actualInventory);
    }

    @Test
    void deleteProduct() {
        List<Inventory> productList = new ArrayList<>();
        productList.add(new Inventory("42", "Product Name", "Product Category", 1, 1, "Product Code"));
        productList.add(new Inventory("43", "Product Name2", "Product Category", 1, 1, "Product Code2"));

        List<String> successMessages = Arrays.asList("Product Product Code Deleted", "Product Product Code2 Deleted");
        when(inventoryService.deleteProduct(productList)).thenReturn(successMessages);

        List<String> message = restInventoryController.deleteProduct(productList).getBody();
        assertSame(successMessages, message);
    }


    @Test
    void deleteAll() {
        List<Inventory> productList = new ArrayList<>();
        productList.add(new Inventory("42", "Product Name", "Product Category", 1, 1, "Product Code"));
        productList.add(new Inventory("43", "Product Name2", "Product Category", 1, 1, "Product Code2"));

        inventoryService.deleteAll();
        String message = restInventoryController.deleteAll().getBody();
        assertEquals("All the items from your Inventory is deleted.", message);

    }

    @Test
    void addStock() {
    }

    @Test
    void updateProduct() {
        List<Inventory> productList = new ArrayList<>();
        assertTrue(inventoryService.updateProduct(productList).isEmpty());
        productList.add(new Inventory("42", "Product Name", "Product Category", 1, 1, "Product Code"));
        productList.add(new Inventory("43", "Product Name2", "Product Category", 1, 1, "Product Code2"));
        List<String> updateMessage = Arrays.asList("Product Product Code Updated.", "Product Product Code2 Updated.");
        when(inventoryService.updateProduct(productList)).thenReturn(updateMessage);
        List<String> message = restInventoryController.updateProduct(productList).getBody();
        assertEquals(updateMessage, message);
    }

    @Test
    void transactions() {
        List<String> transactionsList = new ArrayList<String>();
        transactionsList.add("Total: " + 2);
        transactionsList.add("Purchase: " + 1);
        transactionsList.add("Sale: " + 1);

        when(inventoryService.transactions()).thenReturn(transactionsList);
        List<String> actualTransaction = restInventoryController.transactions().getBody();
        assertEquals(transactionsList, actualTransaction);
    }

    @Test
    void purchaseTransaction() {
        List<Inventory> productList = Arrays.asList(new Inventory("42", "Product Name", "Product Category", 1, 1, "Product Code"), new Inventory("42", "Product Name", "Product Category", 1, 1, "Product Code"));
        List<Orders> ordersList = List.of(new Orders(productList, "purchase"));
        when(inventoryService.purchaseOrders()).thenReturn(ordersList);
        List<Orders> actualOrdersList = restInventoryController.purchaseTransaction().getBody();
        assertSame("purchase", actualOrdersList.get(0).getOrderType());
    }

    @Test
    void saleTransaction() {
        List<Inventory> productList = Arrays.asList(new Inventory("42", "Product Name", "Product Category", 1, 1, "Product Code"), new Inventory("42", "Product Name", "Product Category", 1, 1, "Product Code"));
        List<Orders> ordersList = List.of(new Orders(productList, "sale"));
        when(inventoryService.saleOrders()).thenReturn(ordersList);
        List<Orders> actualOrdersList = restInventoryController.saleTransaction().getBody();
        assertSame("sale", actualOrdersList.get(0).getOrderType());
    }
}