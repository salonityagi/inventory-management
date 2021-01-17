package sl.ms.inventorymanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import sl.ms.inventorymanagement.entity.Inventory;

public interface InventoryRepo extends JpaRepository<Inventory, Integer>{

}
