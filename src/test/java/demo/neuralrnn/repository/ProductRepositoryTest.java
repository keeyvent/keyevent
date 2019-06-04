package demo.neuralrnn.repository;

import demo.neuralrnn.config.NeuralConfig;
import demo.neuralrnn.entity.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {NeuralConfig.class})
@EnableAutoConfiguration
@EnableJpaRepositories(basePackages = "demo.neuralrnn.repositor")
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository repository;

    @Test
    @Sql(scripts = {"classpath:db/dml/drop.sql", "classpath:db/ddl/product.ddl.sql"})
    @Sql(scripts = {"classpath:db/data/product.data.sql"})
    public void testFindAll() {
        List<Product> products = repository.findAll();

        assertNotNull(products);
        assertTrue(products.size() > 0);
    }

    @Test
    @Sql(scripts = {"classpath:db/dml/drop.sql", "classpath:db/ddl/product.ddl.sql"})
    @Sql(scripts = {"classpath:db/data/product.data.sql"})
    public void testFindByCusip() {

        Product product = repository.findByCusip(repository.findAll().get(0).getCusip());
        assertNotNull(product);
    }
}
