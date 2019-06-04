package demo.neuralrnn.data.impl;

import demo.neuralrnn.data.DataGenerator;
import demo.neuralrnn.entity.Bid;
import demo.neuralrnn.entity.Client;
import demo.neuralrnn.entity.Trade;
import demo.neuralrnn.repository.BidRepository;
import demo.neuralrnn.repository.TradeRepository;
import demo.neuralrnn.rule.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Component
public class BidGenerator implements DataGenerator<Map<Client, Rule<Boolean, Trade>>, List<Bid>> {
    @Autowired
    private TradeRepository tradeRepository;
    @Autowired
    private BidRepository bidRepository;
    private Random random = new Random();

    public List<Bid> generate(Map<Client, Rule<Boolean, Trade>> map) {
        List<Trade> trades = tradeRepository.findAll();
        List<Bid> bids = trades.parallelStream().flatMap(trade -> createDummyBids(trade, map).stream()).collect(Collectors.toList());
        return bidRepository.saveAll(bids);
    }

    private List<Bid> createDummyBids(Trade trade, Map<Client, Rule<Boolean, Trade>> map) {
        List<Bid> bids = map.entrySet().parallelStream().map(en -> {
            Client client = en.getKey();
            Rule<Boolean, Trade> rule = en.getValue();
            if (rule.apply(trade)) {
                Bid bid = new Bid();
                bid.setBidPrice(trade.getPrice() - random.nextDouble());
                bid.setAmount(Math.pow(random.nextDouble() * 10000, 4) * 100000);
                bid.setProduct(trade.getProduct());
                bid.setClient(client);
                bid.setTrade(trade);
                return bid;
            }
            return null;
        }).filter(bid -> null != bid).collect(Collectors.toList());
        return bids;
    }

}
