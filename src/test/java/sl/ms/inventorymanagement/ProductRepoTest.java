package sl.ms.inventorymanagement;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import sl.ms.inventorymanagement.entity.Product;
import sl.ms.inventorymanagement.repository.ProductRepo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class ProductRepoTest {

    @Autowired
    ProductRepo productRepo;

    @BeforeEach
    public void saveProduct() throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        mapper.setDateFormat(df);


        File file = Paths.get("src", "test", "resources", "products_request.json").toFile();
        Product[] products = mapper.readValue(file, Product[].class);

        java.util.Arrays.stream(products).forEach(productRepo::save);

    }

    @AfterEach
    public void deleteAll() {
        productRepo.deleteAll();
    }

    @Test
    public void findById() {
        Assertions.assertNotNull(productRepo.findAll());

    }
}
