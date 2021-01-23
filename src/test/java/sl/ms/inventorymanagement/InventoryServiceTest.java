package sl.ms.inventorymanagement;

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
import sl.ms.inventorymanagement.repository.ProductRepo;
import sl.ms.inventorymanagement.service.InventoryService;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class InventoryServiceTest {

    private MockMvc mockMvc;

    @Autowired
    WebApplicationContext context;

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
    void addInventoryTest() {
        InventoryService mockInvent = Mockito.mock(InventoryService.class);
        Product product = new Product();
        product.setId(1);
        product.setName("Item1");
        product.setPrice(120.12);
        product.setQuantity(10);

        Mockito.doNothing().when(mockInvent).addInventory(Mockito.isA(Integer.class), Mockito.isA(Product.class));
        mockInvent.addInventory(1, product);

        Mockito.verify(mockInvent).addInventory(1, product);
    }

    @Test
    void addInventoryListTest() throws Exception {

        List<Product> list = new ArrayList<>();
        Product product = new Product();
        product.setId(1);
        product.setName("Item1");
        product.setPrice(120.12);
        product.setQuantity(10);


        Mockito.doNothing().when(invent).addInventoryList(ArgumentMatchers.anyList());
        invent.addInventoryList(list);


        Mockito.verify(invent).addInventoryList(list);
    }

    @Test
    void addInventoryFileTest() throws Exception {
        file = Paths.get("src", "test", "resources", "sample.csv").toFile();
        FileInputStream input = new FileInputStream(file);
        MockMultipartFile file1 = new MockMultipartFile("file", input);

        Mockito.doNothing().when(invent).addInventoryFile(file1);
        invent.addInventoryFile(file1);
        Mockito.verify(invent).addInventoryFile(file1);

    }

}
