package demo.neuralrnn.controller;

import demo.neuralrnn.entity.Bid;
import demo.neuralrnn.entity.Product;
import demo.neuralrnn.entity.Trade;
import demo.neuralrnn.repository.BidRepository;
import demo.neuralrnn.repository.ClientRepository;
import demo.neuralrnn.repository.ProductRepository;
import demo.neuralrnn.repository.TradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class DataController {
    @Autowired
    private TradeRepository tradeRepository;
    @Autowired
    private BidRepository bidRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ProductRepository productRepository;

    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(DataController.class);


    @RequestMapping("/listCusips")
    public List<Product> listCusips(HttpSession session) {
        return productRepository.findAll();
    }

    @RequestMapping("/listTrades")
    public List<Trade> listTrades(@RequestParam("sort") Optional<String> sort, @RequestParam("order") Optional<String> order, HttpSession session) {
        List<Trade> trades = tradeRepository.findAll();
        if (order.isPresent() && order.get().equalsIgnoreCase("desc")) {
            return trades.parallelStream().sorted(Comparator.comparing(Trade::getYield).reversed()).collect(Collectors.toList());
        } else {
            return trades.parallelStream().sorted(Comparator.comparing(Trade::getYield)).collect(Collectors.toList());
        }

    }
//
//    private Comparator<Trade> getComparator(String field, boolean reverse) {
//        Comparator<Trade> comparator = Comparator.comparing(Trade::getYield);
//        if (field.equalsIgnoreCase("yield")) {
//            comparator = Comparator.comparing(Trade::getYield);
//        }
//        if (field.equalsIgnoreCase("cusipRating")) {
//            comparator = Comparator.comparing(Trade::getCusipRating);
//        }
//
//        if (null != comparator && reverse) {
//            comparator = comparator.reversed();
//        }
//
//        return comparator;
//    }

    @RequestMapping("/listBids")
    public List<Bid> listBids(HttpSession session) {
        return bidRepository.findAll();
    }

    @RequestMapping(value = "/findBids", method = RequestMethod.GET)
    public List<Bid> findBids(@RequestParam("tradeId") String tradeId, HttpSession session) {
        return bidRepository.findBidByTradeId(tradeId);
    }

    @RequestMapping(value = "/findCusip")
    public Product findByCusip(@RequestParam("cusip") String cusip, HttpSession session) {
        return productRepository.findByCusip(cusip);
    }
}
