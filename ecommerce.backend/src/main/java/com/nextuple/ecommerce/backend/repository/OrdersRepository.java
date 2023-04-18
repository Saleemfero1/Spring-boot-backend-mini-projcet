package com.nextuple.ecommerce.backend.repository;
import com.nextuple.ecommerce.backend.model.Orders;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersRepository extends MongoRepository<Orders, String> {
    public List<Orders> findByOrderType(String orderType);
}
