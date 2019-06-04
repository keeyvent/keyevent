package demo.neuralrnn.repository;

import demo.neuralrnn.config.NeuralConfig;
import demo.neuralrnn.entity.Bid;
import demo.neuralrnn.entity.Client;
import demo.neuralrnn.entity.Product;
import demo.neuralrnn.entity.Trade;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {NeuralConfig.class})
@EnableAutoConfiguration
@EnableJpaRepositories(basePackages = "demo.neuralrnn.repository")
public class BidRepositoryTest {

    @Autowired
    private BidRepository bidRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private TradeRepository tradeRepository;

    @Test
    @Sql(scripts = {"classpath:db/dml/drop.sql"})
    @Sql(scripts = {"classpath:db/ddl/bid.ddl.sql",
            "classpath:db/ddl/client.ddl.sql",
            "classpath:db/ddl/default_rating.ddl.sql",
            "classpath:db/ddl/product.ddl.sql",
            "classpath:db/ddl/trade.ddl.sql"
    })
    @Sql(scripts = {"classpath:db/data/client.data.sql"})
    @Sql(scripts = {"classpath:db/data/default_rating.data.sql"})
    @Sql(scripts = {"classpath:db/data/product.data.sql"})
    public void testFindAll() {
        Product product = productRepository.findAll().get(0);
        Client client = clientRepository.findAll().get(0);

        Trade trade = new Trade();
        trade.setPrice(101D);
        trade.setCreateDate(new Date());
        trade.setProduct(product);
        trade.setTradeId(UUID.randomUUID().toString());
        tradeRepository.save(trade);

        Bid bid = new Bid();
        bid.setClient(client);
        bid.setProduct(product);
        bid.setTrade(tradeRepository.findAll().get(0));
        bid.setBidPrice(99D);
        bidRepository.save(bid);


        List<Bid> data = bidRepository.findAll();

        assertNotNull(data);
        assertTrue(data.size() > 0);
        assertNotNull(data.get(0).getProduct());

        List<Bid> bids4Trade = bidRepository.findBidByTradeId(trade.getTradeId());
        assertEquals(1, bids4Trade.size());

    }
}
