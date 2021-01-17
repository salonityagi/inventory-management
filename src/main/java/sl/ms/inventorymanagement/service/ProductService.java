package sl.ms.inventorymanagement.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sl.ms.inventorymanagement.entity.Product;
import sl.ms.inventorymanagement.entity.ProductDto;
import sl.ms.inventorymanagement.exception.ProductNotFound;
import sl.ms.inventorymanagement.logs.InventoryLogger;
import sl.ms.inventorymanagement.repository.ProductRepo;

@Service
public class ProductService {

	@Autowired
	ProductRepo productRepo;
	
	InventoryLogger logger=new InventoryLogger();

	public List<Product> getProducts() {
		return productRepo.findAll();
	}

	public Object findByProductId(int productId) {
		String startTime=String.valueOf(System.currentTimeMillis());
		Optional<Product> product = productRepo.findById(productId);
		if (product.isPresent() && product.get().getQuantity()>0) {
			Product prod=product.get();
			String endTime=String.valueOf(System.currentTimeMillis());
			
			logger.addInventoryLogs(startTime, endTime, prod);
			return prod;
		}
		else 
			throw new ProductNotFound();
	}

	public void updateProduct(int productId, Product product) {
		String startTime=String.valueOf(System.currentTimeMillis());
		Optional<Product> pro = productRepo.findById(productId);
		if (pro.isPresent()) {
			pro.get().setId(product.getId());
			pro.get().setName(product.getName());
			pro.get().setPrice(product.getPrice());
			pro.get().setQuantity(product.getQuantity());

			productRepo.save(pro.get());
			
			String endTime=String.valueOf(System.currentTimeMillis());
			
			logger.addInventoryLogs(startTime, endTime, product);
		}
	}

	public void deleteProduct(int productId) {
		Optional<Product> pro = productRepo.findById(productId);
		if (pro.isPresent()) {
			pro.get().setQuantity(0);
			productRepo.save(pro.get());
		}
	}

	public List<ProductDto> specificProducts() {
		String startTime=String.valueOf(System.currentTimeMillis());
		List<Product> list = productRepo.findAll();
		List<Product> distinctList = productRepo.findAll();
		List<ProductDto> list1 = new ArrayList<>();
		distinctList=list.stream().distinct().collect(Collectors.toList());
		
		distinctList.stream().forEach(a -> {
			ProductDto dto = new ProductDto();
			dto.setId(a.getId());
			dto.setName(a.getName());
			list1.add(dto);
		});

		String endTime=String.valueOf(System.currentTimeMillis());
		
		logger.addInventoryLogs(startTime, endTime, list1);
		return list1;
	}
}
