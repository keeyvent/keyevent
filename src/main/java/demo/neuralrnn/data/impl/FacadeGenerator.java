package demo.neuralrnn.data.impl;

import demo.neuralrnn.data.DataGenerator;
import demo.neuralrnn.entity.Client;
import demo.neuralrnn.entity.Trade;
import demo.neuralrnn.repository.BidRepository;
import demo.neuralrnn.repository.ClientRepository;
import demo.neuralrnn.repository.ProductRepository;
import demo.neuralrnn.repository.TradeRepository;
import demo.neuralrnn.rule.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class FacadeGenerator implements DataGenerator<Integer, Object> {
    @Autowired
    private CusipGenerator cusipGenerator;
    @Autowired
    private TradeGenerator tradeGenerator;
    @Autowired
    private BidGenerator bidGenerator;
    @Autowired
    private ClientStrategyGenerator clientStrategyGenerator;
    @Autowired
    private TradeRepository tradeRepository;
    @Autowired
    private BidRepository bidRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ClientRepository clientRepository;

    @Override
    public Object generate(Integer size) {
        bidRepository.deleteAll();
        tradeRepository.deleteAll();
        productRepository.deleteAll();
        //
        Map<Client, Rule<Boolean, Trade>> map = clientStrategyGenerator.generate(ClientStrategyGenerator.BASIC_STRATEGY);

        cusipGenerator.generate(size);
        tradeGenerator.generate(map);
        bidGenerator.generate(map);

        return new Object();
    }
}
