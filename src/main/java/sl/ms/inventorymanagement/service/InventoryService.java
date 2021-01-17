package sl.ms.inventorymanagement.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import sl.ms.inventorymanagement.entity.Inventory;
import sl.ms.inventorymanagement.entity.Product;
import sl.ms.inventorymanagement.logs.InventoryLogger;
import sl.ms.inventorymanagement.repository.InventoryRepo;

@Service
public class InventoryService {
	
	@Autowired
	InventoryRepo inventRepo;

	public void addInventory(Integer productId,Product product) {
		InventoryLogger logger=new InventoryLogger();
		String startTime=String.valueOf(System.currentTimeMillis());
		
		String request="";
		ObjectMapper mapper=new ObjectMapper();
		Inventory inventory = new Inventory();
		Set<Product> products = new HashSet<>();
		products.clear();
		products.add(product);
		try {
			request=mapper.writeValueAsString(product);
		} catch (Exception e) {
			e.printStackTrace();
		}
		inventory.setId(productId);
		inventory.setProducts(products);
		inventory.setRequest(request);
		inventRepo.save(inventory);
		String endTime=String.valueOf(System.currentTimeMillis());
		
		logger.addInventoryLogs(startTime, endTime, product);
	}

	public void addInventoryList(List<Product> products) {
		InventoryLogger logger=new InventoryLogger();
		ObjectMapper mapper=new ObjectMapper();
		String startTime=String.valueOf(System.currentTimeMillis());
		List<Inventory> list = new ArrayList<>();
		products.forEach(a -> {
			Inventory inventory = new Inventory();
			Product product = new Product();
			String request="";
			
			inventory.setId(a.getId());
			product.setId(a.getId());
			product.setName(a.getName());
			product.setPrice(a.getPrice());
			product.setQuantity(a.getQuantity());
			Set<Product> set = new HashSet<Product>();
			set.add(product);
			inventory.setProducts(set);
			
			try {
				request=mapper.writeValueAsString(product);
			} catch (Exception e) {
				e.printStackTrace();
			}
			inventory.setRequest(request);
			
			list.add(inventory);
		});

		inventRepo.saveAll(list);
		String endTime=String.valueOf(System.currentTimeMillis());
		logger.addInventoryLogs(startTime, endTime, products);
	}

	public void addInventoryFile(MultipartFile file) {
		InventoryLogger logger=new InventoryLogger();
		String startTime=String.valueOf(System.currentTimeMillis());
		List<Inventory> list = new ArrayList<>();
		Resource resource = file.getResource();

		try {
			InputStream input = resource.getInputStream();

			BufferedReader readFile = new BufferedReader(new InputStreamReader(input));

			list = readFile.lines().skip(1).map(maptoclass).collect(Collectors.toList());
		} catch (Exception e) {
			e.printStackTrace();
		}
		inventRepo.saveAll(list);
		String endTime=String.valueOf(System.currentTimeMillis());
		logger.addInventoryLogs(startTime, endTime, list);
	}

	private Function<String, Inventory> maptoclass = (line) -> {
		ObjectMapper mapper=new ObjectMapper();
		Inventory inventory = new Inventory();
		Product cls = new Product();
		String request="";
		String[] items = line.split(",");

		Set<Product> products = new HashSet<Product>();
		products.clear();
		
		if (items[0] != null) {
			inventory.setId(Integer.parseInt(items[0]));
			cls.setId(Integer.parseInt(items[0]));
		}
		if (items[1] != null)
			cls.setName(items[1]);
		if (items[2] != null)
			cls.setPrice(Double.parseDouble(items[2]));
		if (items[3] != null)
			cls.setQuantity(Integer.parseInt(items[3]));

		products.add(cls);
		inventory.setProducts(products);
		try {
			request=mapper.writeValueAsString(cls);
		} catch (Exception e) {
			e.printStackTrace();
		}
		inventory.setRequest(request);
		return inventory;
	};
}
