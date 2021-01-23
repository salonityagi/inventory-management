package sl.ms.inventorymanagement;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import sl.ms.inventorymanagement.entity.Product;
import sl.ms.inventorymanagement.entity.ProductDto;
import sl.ms.inventorymanagement.repository.ProductRepo;
import sl.ms.inventorymanagement.service.InventoryService;
import sl.ms.inventorymanagement.service.ProductService;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class ProductServiceTest {

    private MockMvc mockMvc;

    @Autowired
    WebApplicationContext context;

    @Autowired
    ProductService service;

    InventoryService invent;

    @MockBean
    ProductRepo productRepo;

    File file;

    @BeforeEach
    public void setup() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        file = Paths.get("src", "test", "resources", "sample.csv").toFile();

        invent = Mockito.mock(InventoryService.class);

    }



    @Test
    public void getProductsTest() throws Exception {
        ProductRepo mockRepo = Mockito.mock(ProductRepo.class);

        List<Product> expected = new ArrayList<>();
        Product mockorder = new Product();

        mockorder.setName("Item1");
        mockorder.setPrice(120.1);
        mockorder.setQuantity(10);
        mockorder.setId(1);

        expected.add(mockorder);

        Mockito.doReturn(expected).when(productRepo).findAll();

        List<Product> actual = service.getProducts();
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void findByProductId() {

        Product mockorder = new Product();

        mockorder.setName("Item1");
        mockorder.setPrice(120.1);
        mockorder.setQuantity(10);
        mockorder.setId(1);

        Mockito.doReturn(Optional.of(mockorder)).when(productRepo).findById(Mockito.anyInt());

        Optional<Object> result = Optional.of(service.findByProductId(1));
        Product p = (Product) result.get();
        assertNotNull(service.findByProductId(1));
        assertSame("Item1", p.getName());
    }

    @Test
    void updateProductTest() {
        service = Mockito.mock(ProductService.class);

        Product mockProduct = new Product();

        mockProduct.setName("Item1");
        mockProduct.setPrice(120.1);
        mockProduct.setQuantity(10);
        mockProduct.setId(1);

        Mockito.doReturn(Optional.of(mockProduct)).when(productRepo).findById(Mockito.anyInt());

        Mockito.doReturn(mockProduct).when(productRepo).save(mockProduct);

        Mockito.doNothing().when(service).updateProduct(Mockito.anyInt(), Mockito.isA(Product.class));
        service.updateProduct(1, mockProduct);

        Mockito.verify(service).updateProduct(1, mockProduct);

    }

    @Test
    public void deleteProductTest() {
        service = Mockito.mock(ProductService.class);

        ProductRepo mockRepo = Mockito.mock(ProductRepo.class);

        Product mockorder = new Product();

        mockorder.setName("Item1");
        mockorder.setPrice(120.1);
        mockorder.setQuantity(10);
        mockorder.setId(1);

        Mockito.doReturn(Optional.of(mockorder)).when(mockRepo).findById(Mockito.anyInt());
        mockorder.setQuantity(0);
        Mockito.doReturn(mockorder).when(mockRepo).save(mockorder);

        Mockito.doNothing().when(service).deleteProduct(Mockito.anyInt());
        service.deleteProduct(1);

        Mockito.verify(service).deleteProduct(1);

    }

    @Test
    public void specificProductsTest() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        mapper.setDateFormat(df);

        file = Paths.get("src", "test", "resources", "products_request.json").toFile();
        Product[] products = mapper.readValue(file, Product[].class);

        List<Product> distinctList = new ArrayList<>();
        List<Product> productList = List.of(products);

        List<ProductDto> dtoList = new ArrayList<>();
        distinctList = productList.stream().distinct().collect(Collectors.toList());

        distinctList.stream().forEach(a -> {
            ProductDto dto = new ProductDto();
            dto.setId(a.getId());
            dto.setName(a.getName());
            dtoList.add(dto);
        });


        Mockito.doReturn(productList).when(productRepo).findAll();

        List<ProductDto> expected = service.specificProducts();

        assertNotNull(dtoList);
        assertEquals(expected.size(), dtoList.size());

    }
}