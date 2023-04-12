package com.nextuple.ecommerce.backend.service;

import com.nextuple.ecommerce.backend.controller.RESTInventoryController;
import com.nextuple.ecommerce.backend.model.Inventory;
import com.nextuple.ecommerce.backend.model.Orders;
import com.nextuple.ecommerce.backend.repository.InventoryRepository;
import com.nextuple.ecommerce.backend.repository.OrdersRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = {InventoryService.class})
@ExtendWith(SpringExtension.class)
class InventoryServiceTest {

    @Autowired
    InventoryService inventoryService;
    //Creating Mock Object
    @MockBean
    private InventoryRepository inventoryRepository;
    @MockBean
    private OrdersRepository ordersRepository;

    @Test
    void testCreateProduct() {
        assertTrue(this.inventoryService.createProduct(new ArrayList<>()).isEmpty());
    }

    @Test
    void testCreateProduct2() {
        when(this.ordersRepository.save(ArgumentMatchers.any())).thenReturn(new Orders(new ArrayList<>(), "Order Type"));

        when(this.inventoryRepository.save(ArgumentMatchers.any())).thenReturn(new Inventory("42", "Product Name", "Product Category", 10, 50, "Product Code"));

        when(this.inventoryRepository.findAll()).thenReturn(new ArrayList<>());

        ArrayList<Inventory> inventoryList = new ArrayList<>();
        inventoryList.add(new Inventory("42", "Product Name", "Product Category", 1, 1, "Product Code"));

        List<String> actualCreateProductResult = this.inventoryService.createProduct(inventoryList);
        assertEquals(1, actualCreateProductResult.size());
        assertEquals("Product Product Code Added", actualCreateProductResult.get(0));

        verify(this.ordersRepository).save(any());
        verify(this.inventoryRepository).save(any());
        verify(this.inventoryRepository).findAll();

    }

    @Test
    void testCreateProduct3() {
        when(this.ordersRepository.save(ArgumentMatchers.any())).thenReturn(new Orders(new ArrayList<>(), "Order Type"));

        ArrayList<Inventory> inventoryList = new ArrayList<>();
        inventoryList.add(new Inventory("42", "Purchase", "Purchase", 1, 1, "Purchase"));

        when(this.inventoryRepository.save(ArgumentMatchers.any())).thenReturn(new Inventory("42", "Product Name", "Product Category", 1, 1, "Product Code"));

        when(this.inventoryRepository.findAll()).thenReturn(inventoryList);

        ArrayList<Inventory> inventoryList1 = new ArrayList<>();
        inventoryList1.add(new Inventory("42", "Product Name", "Product Category", 1, 1, "Product Code"));
        List<String> actualCreateProductResult = this.inventoryService.createProduct(inventoryList1);
        assertEquals(1, actualCreateProductResult.size());
        assertEquals("Product Product Code Added", actualCreateProductResult.get(0));
        verify(this.ordersRepository).save(ArgumentMatchers.any());
        verify(this.inventoryRepository).save(ArgumentMatchers.any());
        verify(this.inventoryRepository).findAll();
    }

    @Test
    void testValidateProduct() {
        when(this.inventoryRepository.save(ArgumentMatchers.any())).thenReturn(new Inventory("42", "Product Name", "Product Category", 1, 1, "Product Code"));

        when(this.inventoryRepository.findAll()).thenReturn(new ArrayList<>());

        assertTrue(this.inventoryService.validateProduct(new Inventory("42", "Product Name", "Product Category", 1, 1, "Product Code")));
        verify(this.inventoryRepository).save(ArgumentMatchers.any());
        verify(this.inventoryRepository).findAll();
    }

    @Test
    void testValidateProduct2() {
        ArrayList<Inventory> inventoryList = new ArrayList<>();
        inventoryList.add(new Inventory("42", "Product Name", "Product Category", 1, 1, "Product Code"));
        when(this.inventoryRepository.save(ArgumentMatchers.any())).thenReturn(new Inventory("42", "Product Name", "Product Category", 1, 1, "Product Code"));

        when(this.inventoryRepository.findAll()).thenReturn(inventoryList);

        assertFalse(this.inventoryService.validateProduct(new Inventory("42", "Product Name", "Product Category", 1, 1, "Product Code")));
        verify(this.inventoryRepository).findAll();
    }

    @Test
    void testInventoryDetails() {
        ArrayList<Inventory> inventoryList = new ArrayList<>();
        when(this.inventoryRepository.findAll()).thenReturn(inventoryList);

        List<Inventory> actualInventoryDetailsResult = this.inventoryService.inventoryDetails();
        assertSame(inventoryList, actualInventoryDetailsResult);
        assertTrue(actualInventoryDetailsResult.isEmpty());
        verify(this.inventoryRepository).findAll();
    }

    @Test
    void testDeleteProduct() {
        assertTrue(this.inventoryService.deleteProduct(new ArrayList<>()).isEmpty());
    }

    @Test
    void testdeleteProduct1() {
        List<Inventory> productCodes = new ArrayList<>();
        productCodes.add(new Inventory("42", "Product Name", "Product Category", 1, 1, "Product Code"));
        productCodes.add(new Inventory("43", "Product Name2", "Product Category2", 1, 1, "Product Code2"));

        when(inventoryRepository.findByProductCode(productCodes.get(0).getProductCode())).thenReturn(productCodes.get(0));
        when(inventoryRepository.findByProductCode(productCodes.get(1).getProductCode())).thenReturn(productCodes.get(1));

        List<String> messages = List.of();
        messages = inventoryService.deleteProduct(productCodes);

        verify(inventoryRepository, times(1)).findByProductCode("Product Code");
        verify(inventoryRepository, times(1)).findByProductCode("Product Code2");
        verify(inventoryRepository, times(1)).delete(productCodes.get(0));
        verify(inventoryRepository, times(1)).delete(productCodes.get(0));
    }

    @Test
    void testDeleteProduct3() {
        doNothing().when(this.inventoryRepository).delete(ArgumentMatchers.any());
        when(this.inventoryRepository.findByProductCode(any())).thenReturn(null);

        ArrayList<Inventory> inventoryList = new ArrayList<>();
        inventoryList.add(new Inventory("42", "Product Name", "Product Category", 1, 1, "Product Code"));

        List<String> actualDeleteProductResult = this.inventoryService.deleteProduct(inventoryList);
        assertEquals(1, actualDeleteProductResult.size());
        assertEquals("Product Product Code Dose not Exist!", actualDeleteProductResult.get(0));
        verify(this.inventoryRepository).findByProductCode(any());
    }

    @Test
    void testSaleOrder2() {
        when(this.ordersRepository.save(ArgumentMatchers.any())).thenReturn(new Orders(new ArrayList<>(), "Order Type"));
        when(this.inventoryRepository.save(ArgumentMatchers.any())).thenReturn(new Inventory("42", "Product Name", "Product Category", 1, 1, "Product Code"));
        when(this.inventoryRepository.findByProductCode(ArgumentMatchers.any())).thenReturn(new Inventory("42", "Product Name", "Product Category", 1, 1, "Product Code"));

        ArrayList<Inventory> inventoryList = new ArrayList<>();
        inventoryList.add(new Inventory("42", "Product Name", "Product Category", 1, 1, "Product Code"));
        List<String> actualSaleOrderResult = this.inventoryService.saleOrder(inventoryList);
        assertEquals(1, actualSaleOrderResult.size());
        assertEquals("Product Product Code purchased. Cost: ₹1", actualSaleOrderResult.get(0));
        verify(this.ordersRepository).save(any());
        verify(this.inventoryRepository, atLeast(1)).findByProductCode(ArgumentMatchers.any());
        verify(this.inventoryRepository).save(ArgumentMatchers.any());
    }

    @Test
    void testTransactions() {
        when(this.ordersRepository.findByOrderType(any())).thenReturn(new ArrayList<>());
        when(this.ordersRepository.count()).thenReturn(3L);

        List<String> actualTransactionsResult = this.inventoryService.transactions();
        assertEquals(3, actualTransactionsResult.size());
        assertEquals("Total: 3", actualTransactionsResult.get(0));
        assertEquals("Purchase: 0", actualTransactionsResult.get(1));
        assertEquals("Sale: 0", actualTransactionsResult.get(2));
        verify(this.ordersRepository, atLeast(1)).findByOrderType(any());
        verify(this.ordersRepository).count();
    }

    @Test
    void testPurchaseOrders() {
        ArrayList<Orders> ordersList = new ArrayList<>();
        when(this.ordersRepository.findByOrderType(any())).thenReturn(ordersList);
        List<Orders> actualPurchaseOrdersResult = this.inventoryService.purchaseOrders();
        assertSame(ordersList, actualPurchaseOrdersResult);
        assertTrue(actualPurchaseOrdersResult.isEmpty());
        verify(this.ordersRepository).findByOrderType(any());
    }


    @Test
    void testGetProductByCode() {
        Inventory inventory = new Inventory("42", "Product Name", "Product Category", 1, 1, "Product Code");
        when(this.inventoryRepository.findByProductCode(any())).thenReturn(inventory);
        assertSame(inventory, this.inventoryService.getProductByCode("Product Code"));
        verify(this.inventoryRepository).findByProductCode(any());
    }


    @Test
    void testUpdateProduct() {
        assertTrue(this.inventoryService.updateProduct(new ArrayList<>()).isEmpty());
        ArrayList<Inventory> productList = new ArrayList<>();
        productList.add(new Inventory("42", "Product Name", "Product Category", 1, 1, "Product Code"));

        List<String> actualResultOfUpdateProduct = this.inventoryService.updateProduct((productList));
        assertSame(1, actualResultOfUpdateProduct.size());
        assertEquals("Product Product Code Dose not Exist!", actualResultOfUpdateProduct.get(0));

    }

    @Test
    void testUpdateProduct1() {
        ArrayList<Inventory> productList = new ArrayList<>();
        productList.add(new Inventory("42", "Product Name", "Product Category", 1, 1, "Product Code"));
        productList.add(new Inventory("43", "Product Name2", "Product Category2", 1, 1, "Product Code2"));

        when(inventoryService.getProductByCode(productList.get(0).getProductCode())).thenReturn(productList.get(0));
        when(inventoryService.getProductByCode(productList.get(1).getProductCode())).thenReturn(productList.get(1));

        List<String> actualResultOfUpdateProduct = this.inventoryService.updateProduct((productList));

        verify(this.inventoryRepository).save(productList.get(0));
    }

    @Test
    void testSaleOrders() {
        //given
        List<Inventory> inventoryList = List.of(new Inventory("42", "Product Name", "Product Category", 1, 1, "Product Code"));
        List<Orders> ordersList = List.of(new Orders(inventoryList, "Sale"));
        when(ordersRepository.findByOrderType("Sale")).thenReturn(ordersList);

        //when
        List<Orders> actualResultOfOrdersList = ordersRepository.findByOrderType("Sale");
        //then
        assertEquals(1, actualResultOfOrdersList.size());
        verify(this.ordersRepository).findByOrderType("Sale");

    }

    @Test
    void testDeleteAll() {
        inventoryRepository.save(new Inventory("42", "Product Name", "Product Category", 1, 1, "Product Code"));
        inventoryRepository.save(new Inventory("43", "Product Name2", "Product Category", 1, 1, "Product Code2"));

        inventoryService.deleteAll();
        int inventorySize = inventoryService.inventoryDetails().size();
        assertEquals(0, inventorySize);
    }

    @Test
    void purchaseOrders() {
        List<Orders> ordersList = new ArrayList<>();
        assertTrue(ordersRepository.findByOrderType("").isEmpty());
    }

    @Test
    void purchaseOrders1() {
        List<Inventory> productList = Arrays.asList(new Inventory("42", "Product Name", "Product Category", 1, 1, "Product Code"), new Inventory("42", "Product Name", "Product Category", 1, 1, "Product Code"));
        List<Orders> ordersList = List.of(new Orders(productList, "purchase"));
        when(ordersRepository.findByOrderType("purchase")).thenReturn(ordersList);
        when(inventoryService.purchaseOrders()).thenReturn(ordersList);
        List<Orders> actulaOrders = inventoryService.purchaseOrders();
        assertEquals("purchase", actulaOrders.get(0).getOrderType());
    }

    @Test
    void saleOrders() {
        List<Orders> ordersList = new ArrayList<>();
        assertTrue(ordersRepository.findByOrderType("").isEmpty());
    }

    @Test
    void saleOrders1() {
        List<Inventory> productList = Arrays.asList(new Inventory("42", "Product Name", "Product Category", 1, 1, "Product Code"), new Inventory("42", "Product Name", "Product Category", 1, 1, "Product Code"));
        List<Orders> ordersList = List.of(new Orders(productList, "Sale"));
        when(ordersRepository.findByOrderType("Sale")).thenReturn(ordersList);
        when(inventoryService.saleOrders()).thenReturn(ordersList);
        List<Orders> actulaOrders = inventoryService.saleOrders();
        assertSame("Sale", actulaOrders.get(0).getOrderType());
    }

    @Test
    void testAddStock() {
        List<Inventory> inventoryList = new ArrayList<>();
        inventoryList.add(new Inventory("11", "Product Name2", "Product Category", 1, 1, "Product Code2"));
        when(inventoryRepository.findByProductCode(inventoryList.get(0).getProductCode())).thenReturn(inventoryList.get(0));
        Inventory actualProduct = inventoryService.getProductByCode(inventoryList.get(0).getProductCode());
        inventoryRepository.save(actualProduct);
        List<String> updateMessage = List.of("Product Product Code2 with 2qty added to stock");
        List<String> actualUpdateMessage = inventoryService.addStock(inventoryList);
        assertEquals(updateMessage, actualUpdateMessage);
        assertSame(inventoryList.get(0), actualProduct);
    }

//    //Not Completed.(addStock)
//    @Test
//    void testAddStock(){
//    List<Inventory>inventoryList=new ArrayList<>();
//    inventoryList.add(new Inventory("11","Product Name2", "Product Category", 1, 1, "Product Code2"));
//    when(inventoryRepository.findByProductCode("Product Code2")).thenReturn(new Inventory("11","Product Name2", "Product Category", 1, 1, "Product Code2"));
//    when(inventoryService.addStock(inventoryList)).thenReturn(new ArrayList<String>());
//
//    List<String>messages = inventoryService.addStock(inventoryList);
//    assertEquals(1,messages.size());
//    }


}