package demo.neuralrnn.data.impl;

import demo.neuralrnn.data.DataGenerator;
import demo.neuralrnn.entity.Client;
import demo.neuralrnn.entity.Product;
import demo.neuralrnn.entity.Trade;
import demo.neuralrnn.repository.ProductRepository;
import demo.neuralrnn.repository.TradeRepository;
import demo.neuralrnn.rule.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class TradeGenerator implements DataGenerator<Map<Client, Rule<Boolean, Trade>>, List<Trade>> {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private TradeRepository tradeRepository;

    private Random random = new Random();

    public List<Trade> generate(Map<Client, Rule<Boolean, Trade>> map) {
        List<Trade> trades = productRepository.findAll().parallelStream().map(product -> createDummyTrade(product)).collect(Collectors.toList());
        return tradeRepository.saveAll(trades);
    }

    private Trade createDummyTrade(Product product) {
        Trade trade = new Trade();
        trade.setProduct(product);
        //TODO
        trade.setCreateDate(new Date());
        Random random = new Random();
        trade.setTradeId(UUID.randomUUID().toString());
        trade.setPrice(100 + random.nextDouble() * 2);

        return trade;
    }

}
