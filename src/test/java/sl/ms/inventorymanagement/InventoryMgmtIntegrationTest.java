package sl.ms.inventorymanagement;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
class InventoryMgmtIntegrationTest {

    @Autowired
    WebApplicationContext context;

    @Autowired
    ProductService productService;

    @Autowired
    InventoryService inventService;

    @Autowired
    ProductRepo productRepo;

    File file;

    private MockMvc mockMvc;

    HttpHeaders httpHeaders = new HttpHeaders();
    ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setup() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        mapper.setDateFormat(df);

        // file = Paths.get("src", "test", "resources", "orders.json").toFile();
        httpHeaders.add("Authorization",
                "Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MTAwNTI4ODEsInVzZXJfbmFtZSI6ImthbmRhbiIsImF1dGhvcml0aWVzIjpbIlJPTEVfQURNSU4iXSwianRpIjoiMTkxYjI3NTItODljOS00NjkzLWE2NDUtNjdlYzQ1YzA0YjI4IiwiY2xpZW50X2lkIjoiY2xpZW50aWQiLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXX0.arqBHp6W6ntNRR783DFwu4pLwyZ9raucNzNDsR5OsasqE5Ngdr-j64dkGlG8w84JGRJ4ahaeHvjrXqdMijykTXUhWRwyrtikIAXIUrv3Glshn03SCLuXuEpDKpI8mFaj1sDD6sUW9_1R-V02PEEqYdMsBqSOfzSwFcuKyFbpVR9LlqKSyppGAN7qISKPs8zzhLzd1DY94FDm2p4PKoGt29dPoYqOGjAJuboRf7Tk-X83z5Z5iH98XK3DKl_ShEcaxCQOoFXyKdugmbamUnF3-lnxGju_goQNr2TOwiNDXi93PKvWWz9COWlBVDUT_FGqqVtF2BoqT54vtic1SATutQ");
    }

    @AfterEach
    public void dropDB() {
        System.out.println("after each test*****************");
        productRepo.deleteAll();
    }

    @Test
    void addProductsTest() throws Exception {
        httpHeaders.add("Content-Type", "application/json");

        List<Product> list = new ArrayList<>();
        Product product = new Product();
        product.setId(1);
        product.setName("Item1");
        product.setPrice(120.12);
        product.setQuantity(10);

        list.add(product);
        byte[] iJosn = toJson(list);

        mockMvc.perform(MockMvcRequestBuilders.post("/products").content(iJosn).headers(httpHeaders)
                .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }


    @Test
    void getProductsTest() throws Exception {
        //Add products
        addProductsTest();

        //get products
        MvcResult result = mockMvc
                .perform(
                        MockMvcRequestBuilders.get("/products").headers(httpHeaders).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        Product[] list1 = mapper.readValue(result.getResponse().getContentAsString(), Product[].class);
        //Assert products
        assertEquals(1, list1[0].getId());

    }

    @Test
    void addInventoryTest() throws Exception {
        httpHeaders.add("Content-Type", "application/json");

        Product product = new Product();
        product.setId(1);
        product.setName("Item1");
        product.setPrice(120.12);
        product.setQuantity(10);

        byte[] iJosn = toJson(product);

        mockMvc.perform(MockMvcRequestBuilders.post("/products/1").content(iJosn).headers(httpHeaders)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void uploadInventoryFileTest() throws Exception {
        file = Paths.get("src", "test", "resources", "sample.csv").toFile();
        FileInputStream input = new FileInputStream(file);
        MockMultipartFile file1 = new MockMultipartFile("file", input);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/products/file").file(file1).headers(httpHeaders)
                .contentType(MediaType.MULTIPART_FORM_DATA).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void updateProductTest() throws Exception {
        addProductsTest();

        Product product = new Product();
        product.setId(1);
        product.setName("Item1");
        product.setPrice(220.12);
        product.setQuantity(20);

        byte[] iJosn = toJson(product);

        mockMvc.perform(MockMvcRequestBuilders.put("/products/1").content(iJosn).headers(httpHeaders)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.TEXT_PLAIN_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("product updated successfully"));

    }

    @Test
    void deleteProductTest() throws Exception {
        addProductsTest();

        mockMvc.perform(MockMvcRequestBuilders.delete("/products/1").headers(httpHeaders)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.TEXT_PLAIN_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("product deleted successfully"));

    }

    @Test
    void uploadSupportedProductsTest() throws Exception {
        file = Paths.get("src", "test", "resources", "supported_products.json").toFile();
        ProductDto[] productsDto = mapper.readValue(file, ProductDto[].class);

        addProductsTest();

        List<Product> list = new ArrayList<>();

        Product product = new Product();
        product.setId(2);
        product.setName("Item2");
        product.setPrice(130.12);
        product.setQuantity(20);

        list.add(product);
        byte[] iJosn = toJson(list);

        mockMvc.perform(MockMvcRequestBuilders.post("/products").content(iJosn).headers(httpHeaders)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.get("/products/supported").headers(httpHeaders)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        assertEquals(result.getResponse().getContentAsString(), mapper.writeValueAsString(productsDto));

    }

    private byte[] toJson(Object r) throws Exception {
        ObjectMapper map = new ObjectMapper();
        return map.writeValueAsString(r).getBytes();
    }
}
