package com.nextuple.ecommerce.backend.repository;
import com.nextuple.ecommerce.backend.model.Inventory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends MongoRepository<Inventory, String> {
    public Inventory findByProductCode(String productCode);
}