package com.nextuple.ecommerce.backend.model;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
@Document
public class Orders {
    @Id
    private String orderId;
    private List<Inventory> orderData;
    private String orderType;

    public Orders(List<Inventory> orderData, String orderType) {
        this.orderData = orderData;
        this.orderType = orderType;
    }

    public Orders() {
    }

    public List<Inventory> getOrderData() {
        return orderData;
    }

    public void setOrderData(List<Inventory> orderData) {
        this.orderData = orderData;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderId() {
        return orderId;
    }
}
