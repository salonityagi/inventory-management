package sl.ms.inventorymanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EnableJpaRepositories
public class InventoryManagement {

	public static void main(String[] args) {
		SpringApplication.run(InventoryManagement.class);
	}
}
