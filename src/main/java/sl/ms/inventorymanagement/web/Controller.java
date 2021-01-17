package sl.ms.inventorymanagement.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import sl.ms.inventorymanagement.entity.Product;
import sl.ms.inventorymanagement.entity.ProductDto;
import sl.ms.inventorymanagement.logs.InventoryLogger;
import sl.ms.inventorymanagement.service.InventoryService;
import sl.ms.inventorymanagement.service.ProductService;

@RestController
@RequestMapping(path = "/products")
public class Controller {
	
	InventoryLogger logger=new InventoryLogger();

	@Autowired
	ProductService productService;

	@Autowired
	InventoryService inventService;

	@GetMapping
	public List<Product> getProducts() {
		return productService.getProducts();
	}

	@GetMapping(path = "/{product_id}")
	public ResponseEntity<Object> getSpecificProducts(@PathVariable(name = "product_id") int productId) {
		return new ResponseEntity<>(productService.findByProductId(productId),HttpStatus.OK);
	}

	@PostMapping(path = "/{product_id}",consumes = MediaType.APPLICATION_JSON_VALUE)
	public String addInventory(@PathVariable(name = "product_id") int productId,@RequestBody Product product) {
		inventService.addInventory(productId,product);
		return "Inventory Added Successfully";
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public String addProducts(@RequestBody List<Product> products) {
		inventService.addInventoryList(products);
		return "Inventory & Product Added Successfully";
	}

	@PostMapping(path = "/file")
	public String addInventoryFile(@RequestParam("file") MultipartFile file) {
		inventService.addInventoryFile(file);
		return "Inventory File & Product Added Successfully";
	}

	@PutMapping(path = "/{product_id}")
	public String updateProduct(@RequestBody Product product, @PathVariable(name = "product_id") int productId) {
		productService.updateProduct(productId, product);
		return "product updated successfully";
	}

	@DeleteMapping(path = "/{product_id}")
	public String deleteInventory(@PathVariable(name = "product_id") int productId) {
		productService.deleteProduct(productId);
		return "product deleted successfully";
	}

	@GetMapping(path = "/supported")
	public List<ProductDto> supportedProducts() {
		return productService.specificProducts();
	}

}
