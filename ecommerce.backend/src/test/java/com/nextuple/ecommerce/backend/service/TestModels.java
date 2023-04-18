package com.nextuple.ecommerce.backend.service;
import com.nextuple.ecommerce.backend.model.Inventory;
import com.nextuple.ecommerce.backend.model.Orders;
import com.nextuple.ecommerce.backend.repository.InventoryRepository;
import com.nextuple.ecommerce.backend.repository.OrdersRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

public class TestModels {
    @MockBean
    OrdersRepository ordersRepository;
    @MockBean
    InventoryRepository inventoryRepository;

    @Autowired
    Orders orders;
    @Autowired
    Inventory inventory;

    @Test
    void testOrder() {
        List<Inventory> ordersList = new ArrayList<>();
        ordersList.add(new Inventory("42", "Product Name", "Product Category", 1, 1, "Product Code"));
        ordersList.add(new Inventory("42", "Product Name", "Product Category", 1, 1, "Product Code1"));
        Orders ordersEntity = new Orders();
        ordersEntity.setOrderData(ordersList);
        ordersEntity.setOrderType("Sale");
        ordersEntity.setOrderId("1010");

        List<Inventory> actualOrderList = ordersEntity.getOrderData();
        String actualOrderType = ordersEntity.getOrderType();
        String actualOrderId = ordersEntity.getOrderId();

        assertEquals(ordersList, actualOrderList);
        assertEquals("Sale", actualOrderType);
        assertEquals("1010", actualOrderId);
    }

    @Test
    void testInventory() {
        Inventory inventoryObj = new Inventory();
        inventoryObj.setId("123");
        inventoryObj.setProductName("Product Name");
        inventoryObj.setProductCategory("Product Category");
        inventoryObj.setProductPrice(1);
        inventoryObj.setProductQuantity(1);
        inventoryObj.setProductCode("Product Code");

        assertEquals("123", inventoryObj.getId());
        assertEquals("Product Name", inventoryObj.getProductName());
        assertEquals("Product Category", inventoryObj.getProductCategory());
        assertEquals(1, inventoryObj.getProductPrice());
        assertEquals(1, inventoryObj.getProductQuantity());
        assertEquals("Product Code", inventoryObj.getProductCode());
    }

    @Test
    void testInventory1() {
        Inventory inventoryNull = new Inventory(null, null, null, 0, 0, null);
        assertEquals(inventoryNull, new Inventory());
    }
}