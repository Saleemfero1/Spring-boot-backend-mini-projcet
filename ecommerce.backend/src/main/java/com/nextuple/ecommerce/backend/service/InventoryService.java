package com.nextuple.ecommerce.backend.service;

import com.nextuple.ecommerce.backend.model.Orders;
import com.nextuple.ecommerce.backend.model.Inventory;
import com.nextuple.ecommerce.backend.repository.InventoryRepository;
import com.nextuple.ecommerce.backend.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
@Component
@Service
public class InventoryService {
    @Autowired
    private static InventoryRepository inventoryRepository;

    @Autowired
    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }


    @Autowired
    private OrdersRepository ordersRepository;

    public int add(int a,int b){
      return a+b;
    }

    //Create Product
    public List<String> createProduct(List<Inventory> newProducts){
        List<Inventory> orders = new ArrayList<Inventory>();
        List<String> messages  =new ArrayList<String>();
        for (int i = 0; i < newProducts.size(); i++) {
            if (validateProduct(newProducts.get(i))){
                orders.add(newProducts.get(i));
                messages.add("Product " + newProducts.get(i).getProductCode() + " Added");
            } else {
                messages.add("Product " + newProducts.get(i).getProductCode() + " Already Exist");
            }
        }
        boolean addPurchase = orders.isEmpty();
        if (!addPurchase) {
            ordersRepository.save(new Orders(orders, "Purchase"));
        }
        return messages;
    }

    //To Validate a Product
    public boolean validateProduct(Inventory product){
        List<Inventory> itemsFromDB = inventoryRepository.findAll();
        for (Inventory item:itemsFromDB) {
            if ((product.getProductCode()).equals(item.getProductCode())){
                return false;
            }
        }
        inventoryRepository.save(product);
        return true;
    }


    //Read Operation
    public List<Inventory> inventoryDetails(){
        return inventoryRepository.findAll();
    }

    public List<String> deleteProduct(List<Inventory> productCodes){
        List<String> messages = new ArrayList<String>();
        for (Inventory i:productCodes){
            Inventory j = getProductByCode(i.getProductCode());
            // Check if product is not available
            if (j == null){
                messages.add("Product " + i.getProductCode() + " Dose not Exist!");
            } else {
                inventoryRepository.delete(j);
                messages.add("Product " + i.getProductCode() + " Deleted");
            }
        }
        return messages;
    }

    public void deleteAll(){
        inventoryRepository.deleteAll();
    }

    public void deleteAllTransaction(){ordersRepository.deleteAll();}

    public List<String> saleOrder(List<Inventory> products){
        List<Inventory> orders = new ArrayList<Inventory>();
        List<String> messages  =new ArrayList<String>();
        boolean purchase = true;
        // Scan the list of products requested
        for (Inventory order:products) {
            Inventory i = getProductByCode(order.getProductCode());
            if (i == null){
                messages.add("Product " + order.getProductCode() + " Dose not Exist!");
                purchase = false;
            } else if (i.getProductQuantity() == 0){
                messages.add(order.getProductCode() + " is out of stock.");
                purchase = false;
            } else if (order.getProductQuantity() > i.getProductQuantity()){
                messages.add(("Available quantity for " + order.getProductCode() + " product is " + i.getProductQuantity()));
                purchase = false;
            }
        }

        if (!purchase) {
            return messages;
        }

        if (purchase) {
            for (Inventory order : products) {
                Inventory i = getProductByCode(order.getProductCode());

                // Updating the quantity in the DataBase
                i.setProductQuantity(i.getProductQuantity() - order.getProductQuantity());
                inventoryRepository.save(i);

                messages.add("Product " + order.getProductCode() + " purchased. Cost: ₹" + (order.getProductQuantity() * i.getProductPrice()));

                // Add to orders by inventory class [This is for Transactions Record]
                orders.add(new Inventory(
                        i.getId(),
                        i.getProductName(),
                        i.getProductCategory(),
                        (order.getProductQuantity() * i.getProductPrice()),
                        (order.getProductQuantity()),
                        order.getProductCode()
                ));
            }
        }

        boolean addPurchase = orders.isEmpty();
        if (!addPurchase) {
            ordersRepository.save(new Orders(orders, "Sale"));
        }
        return messages;
    }

    public List<String> addStock(List<Inventory> productDetails){
        List<String> messages = new ArrayList<String>();
        for (Inventory i:productDetails){
            Inventory j = getProductByCode(i.getProductCode());

            // Check if product is not available
            if (j == null){
                messages.add("Product " + i.getProductCode() + " Dose not Exist!");
            } else {
                // Updating the quantity in the DB
                j.setProductQuantity(j.getProductQuantity() + i.getProductQuantity());
                inventoryRepository.save(j);
                messages.add("Product " + i.getProductCode() + " with " + i.getProductQuantity() + "qty added to stock");
            }
        }
        return messages;
    }

    public List<String> updateProduct(List<Inventory> productDetails){
        List<String> messages = new ArrayList<String>();
        for (Inventory i:productDetails){
            Inventory j = getProductByCode(i.getProductCode());

            // Check if product is not available
            if (j == null){
                messages.add("Product " + i.getProductCode() + " Dose not Exist!");
            } else {
                // Updating the quantity in the DB
                j.setProductName(i.getProductName());
                j.setProductCategory(i.getProductCategory());
                j.setProductPrice(i.getProductPrice());
                inventoryRepository.save(j);
                messages.add("Product " + i.getProductCode() + " Updated.");
            }
        }
        return messages;
    }

    //Order Transactions
    public List<String> transactions(){
        List<String> transactions = new ArrayList<String>();
        transactions.add("Total: " + ordersRepository.count());
        transactions.add("Purchase: " + ordersRepository.findByOrderType("Purchase").size());
        transactions.add("Sale: " + ordersRepository.findByOrderType("Sale").size());
        return transactions;
    }

    //Purchase Order Transaction
    public List<Orders> purchaseOrders(){
        return ordersRepository.findByOrderType("Purchase");
    }

    //Sale Order Transaction
    public List<Orders> saleOrders(){
        return ordersRepository.findByOrderType("Sale");
    }

    //Find Product By Code
    public Inventory getProductByCode(String productCode){
        return inventoryRepository.findByProductCode(productCode);
    }
}
