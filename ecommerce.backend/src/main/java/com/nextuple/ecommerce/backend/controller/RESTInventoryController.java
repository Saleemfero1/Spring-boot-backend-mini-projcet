package com.nextuple.ecommerce.backend.controller;
import com.nextuple.ecommerce.backend.model.Inventory;
import com.nextuple.ecommerce.backend.model.Orders;
import com.nextuple.ecommerce.backend.service.InventoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@Slf4j
public class RESTInventoryController {
    @Autowired
    private InventoryService inventoryService;

    @PostMapping("/api/orders/purchaseOrder")
    public ResponseEntity<List<String>> purchaseOrder(@RequestBody List<Inventory> newProducts) {
        List<String> messages = inventoryService.createProduct(newProducts);
        if(messages.isEmpty()){
            return new ResponseEntity<>(messages, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(messages, HttpStatus.CREATED) ;
    }

    @PostMapping("/api/orders/saleOrder")
    public ResponseEntity<List<String>> saleOrder(@RequestBody List<Inventory> order) {
        List<String> messages = inventoryService.saleOrder(order);
        if(messages.isEmpty()){
            return new ResponseEntity<>(messages, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(messages,HttpStatus.CREATED);
    }

    @GetMapping("/api/inventoryDetails")
    public ResponseEntity<List<Inventory>> inventoryDetails() {
        List<Inventory> inventoryList = inventoryService.inventoryDetails();
        if(inventoryList.isEmpty())
            return new ResponseEntity<>(inventoryList,HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(inventoryList,HttpStatus.OK);
    }


    @DeleteMapping("/api/orders/deleteProduct")
    public ResponseEntity<List<String>> deleteProduct(@RequestBody List<Inventory> productCodes) {
        List<String> messages = inventoryService.deleteProduct(productCodes);
        if (messages.isEmpty())
            return new ResponseEntity<>(messages,HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(messages,HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/api/deleteAll")
    public ResponseEntity<String> deleteAll() {
        inventoryService.deleteAll();
        return new ResponseEntity<>("All the items from your Inventory is deleted.",HttpStatus.NO_CONTENT);
    }

    @PostMapping("/api/addStock")
    public ResponseEntity<List<String>> addStock(@RequestBody List<Inventory> productDetails) {
        List<String> messages = inventoryService.addStock(productDetails);
        if (messages.isEmpty())
            return new ResponseEntity<>(messages,HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(messages,HttpStatus.OK);
    }

    @PostMapping("/api/updateProducts")
    public ResponseEntity<List<String>> updateProduct(@RequestBody List<Inventory> productDetails) {
        List<String> messages = inventoryService.updateProduct(productDetails);
        if (messages.isEmpty())
            return new ResponseEntity<>(messages,HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(messages,HttpStatus.OK);
    }

    @GetMapping("/api/orders/transactions")
    public ResponseEntity<List<String>> transactions() {
        List<String> results = inventoryService.transactions();
        if (results.isEmpty())
            return new ResponseEntity<>(results,HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(results,HttpStatus.OK);
    }

    @GetMapping("/api/orders/transactions/purchase")
    public ResponseEntity<List<Orders>> purchaseTransaction() {
        List<Orders> results = inventoryService.purchaseOrders();
        if (results.isEmpty())
            return new ResponseEntity<>(results,HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(results,HttpStatus.OK);
    }

    @GetMapping("/api/orders/transactions/sale")
    public ResponseEntity<List<Orders>> saleTransaction() {
        List<Orders> results = inventoryService.saleOrders();
        if (results.isEmpty())
            return new ResponseEntity<>(results,HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(results,HttpStatus.OK);
    }

    @DeleteMapping("/api/Transactions/deleteAll")
    public ResponseEntity<String>deleteAllTransaction(){
        inventoryService.deleteAllTransaction();
        return new ResponseEntity<>("All orders from the transaction has deleted",HttpStatus.NO_CONTENT);
    }
}
