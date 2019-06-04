package demo.neuralrnn.repository;

import demo.neuralrnn.config.NeuralConfig;
import demo.neuralrnn.entity.Client;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes={NeuralConfig.class})
@EnableAutoConfiguration
@EnableJpaRepositories(basePackages = "demo.neuralrnn.repository")
public class ClientRepositoryTest {

    @Autowired
    private ClientRepository repository;
    @Test
    public void testFindAll(){
        List<Client> data = repository.findAll();

        assertNotNull(data);
        assertTrue(data.size() > 0);
        assertNotNull(data.get(0));
    }
}
