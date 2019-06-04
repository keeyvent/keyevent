package demo.neuralrnn.controller;

import demo.neuralrnn.config.DataPrepareInterceptor;
import demo.neuralrnn.data.impl.FacadeGenerator;
import demo.neuralrnn.entity.*;
import demo.neuralrnn.repository.*;
import org.apache.log4j.Logger;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class IndexController {
    private static Logger logger = Logger.getLogger(IndexController.class);
    @Autowired
    private TradeRepository tradeRepository;
    @Autowired
    private BidRepository bidRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private FacadeGenerator facadeGenerator;
    @Autowired
    private DefaultRatingRepository defaultRatingRepository;

    private final Integer NODE_SIZE = 5;

//    private List<Trade> allTrades;
//    private List<Bid> allBids;
//    private Map<String, Double> defaults;
//    private Map<Client, List<Bid>> clientBids;

    @RequestMapping("/")
    public ModelAndView list(HttpSession session) {
        ModelAndView mv = new ModelAndView("index");

        return mv;
    }

    @RequestMapping(value = "/calWithNeuralNet")
    public List<ClientPredication> calWithNeuralNet(@RequestParam("cusip") String cusip, @RequestParam("price") Double price, HttpSession session) {
        Map<Client, MultiLayerPerceptron> perceptronMap = (Map<Client, MultiLayerPerceptron>) session.getServletContext().getAttribute(DataPrepareInterceptor.PERCEPTRON_KEY);
        logger.info("perceptronMap is index has size " + perceptronMap.size());
        logger.info("Create new trade with cusip " + cusip);
        Product product = productRepository.findByCusip(cusip.trim());
        Map<String, Double> defaults = defaultRatingRepository.findAll().stream().collect(Collectors.toMap(DefaultRating::getSpRating, DefaultRating::getSpRate));
        List<ClientPredication> predications = perceptronMap.entrySet().stream().map(en -> {
            Client client = en.getKey();
            NeuralNetwork neuralNet = en.getValue();
            Trade trade = createTrade(product, price);
            logger.info("product " + product + ", defaults " + defaults);
            Double defaultRate = defaults.get(trade.getProduct().getRatingValue());
            NormalizedInput input = new NormalizedInput(client, trade, defaultRate);
            neuralNet.setInput(input.toDoubleArray());
            neuralNet.calculate();
            double networkOutput = neuralNet.getOutput()[0];
            return new ClientPredication(client, networkOutput);
        }).sorted(Comparator.comparing(ClientPredication::getPercentage).reversed()).collect(Collectors.toList());


        predications.stream().forEach(pre -> {
            logger.info("----Predication for " + pre.getClientName() + " : " + pre.getPercentage());
        });

        return predications;
    }


    private Trade createTrade(Product product, Double price) {
        Trade trade = new Trade();
        trade.setProduct(product);
        trade.setCreateDate(new Date());
        trade.setTradeId(UUID.randomUUID().toString());
        trade.setPrice(price);

        return trade;
    }


}
