package sl.ms.inventorymanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import sl.ms.inventorymanagement.entity.Product;

public interface ProductRepo extends JpaRepository<Product, Integer>{

}
