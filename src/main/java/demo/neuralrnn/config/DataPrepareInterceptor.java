package demo.neuralrnn.config;

import demo.neuralrnn.data.impl.FacadeGenerator;
import demo.neuralrnn.entity.*;
import demo.neuralrnn.repository.*;
import org.apache.log4j.Logger;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.util.TransferFunctionType;
import org.neuroph.util.random.RangeRandomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DataPrepareInterceptor implements HandlerInterceptor {
    private static Logger logger = Logger.getLogger(DataPrepareInterceptor.class);
    public final static String DATA_KEY = "DATA_KEY";
    public final static String PERCEPTRON_KEY = "PERCEPTRON_KEY";

    @Autowired
    private FacadeGenerator facadeGenerator;
    @Autowired
    private BidRepository bidRepository;
    @Autowired
    private TradeRepository tradeRepository;
    @Autowired
    private DefaultRatingRepository defaultRatingRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ProductRepository productRepository;


    private final Integer NODE_SIZE = 5;
    private final Integer DATA_SIZE = 5000;
    private final Map<Client, MultiLayerPerceptron> perceptronMap = new HashMap<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        ServletContext context = request.getSession().getServletContext();
        if (context.getAttribute(DATA_KEY) != null) {
            return true;
        }
        facadeGenerator.generate(DATA_SIZE);
        perceptronMap.clear();
        List<Trade> allTrades = tradeRepository.findAll();
        Map<String, Double> defaults = defaultRatingRepository.findAll().stream().collect(Collectors.toMap(DefaultRating::getSpRating, DefaultRating::getSpRate));
        List<Bid> allBids = bidRepository.findAll();
        Map<Client, List<Bid>> groupByClients = allBids.stream().collect(Collectors.groupingBy(bid -> bid.getClient()));

        groupByClients.keySet().stream().forEach(client -> {
            MultiLayerPerceptron network = getPerceptron(client, allTrades, groupByClients.get(client), defaults);
            perceptronMap.put(client, network);
        });

        context.setAttribute(DATA_KEY, new Object());
        context.setAttribute(PERCEPTRON_KEY, perceptronMap);

        return true;
    }


    //TODO bad performance
    private MultiLayerPerceptron getPerceptron(Client client, List<Trade> allTrades, List<Bid> clientBids, Map<String, Double> defaults) {
        List<NormalizedInput> all = allTrades.stream().map(trade -> trade2NormalizedInput(client, trade, defaults.get(trade.getProduct().getRatingValue()))).collect(Collectors.toList());
        List<NormalizedInput> bidTrades = clientBids.stream().map(bid -> trade2NormalizedInput(client, bid.getTrade(), defaults.get(bid.getTrade().getProduct().getRatingValue()))).collect(Collectors.toList());

        List<String> bidTradeIds = clientBids.parallelStream().map(bid -> bid.getTrade().getTradeId()).collect(Collectors.toList());
        List<NormalizedInput> dropTrades = allTrades.stream().filter(trade -> !bidTradeIds.contains(trade.getTradeId())).map(trade ->
                trade2NormalizedInput(client, trade, defaults.get(trade.getProduct().getRatingValue()))
        ).collect(Collectors.toList());

        logger.info("bid trades for client " + client.getName() + " are " + bidTrades.size());
        logger.info("dropped trades for client " + client.getName() + " are " + dropTrades.size());

        DataSet trainingSet = new DataSet(NODE_SIZE, 1);
        MultiLayerPerceptron myMlPerceptron = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, NODE_SIZE, 2 * NODE_SIZE + 1, 1);
//        myMlPerceptron.randomizeWeights(new WeightsRandomizer(new Random(138)));
//        myMlPerceptron.setLearningRule(new BackPropagation());
        myMlPerceptron.setLearningRule(new MomentumBackpropagation());
        // this.setLearningRule(new DynamicBackPropagation());

        myMlPerceptron.randomizeWeights(new RangeRandomizer(-0.7, 0.7));

        myMlPerceptron.getLearningRule().setLearningRate(0.01);

        LearningRule learningRule = myMlPerceptron.getLearningRule();
        learningRule.addListener((event) -> {
            BackPropagation bp = (BackPropagation) event.getSource();
            if (event.getEventType() != LearningEvent.Type.LEARNING_STOPPED)
                logger.info(bp.getCurrentIteration() + ". iteration : " + bp.getTotalNetworkError());
        });

        bidTrades.stream().forEach(data -> {
            trainingSet.addRow(new DataSetRow(data.toDoubleArray(), new double[]{1}));
        });
        dropTrades.stream().forEach(data -> {
            trainingSet.addRow(new DataSetRow(data.toDoubleArray(), new double[]{0}));
        });

        myMlPerceptron.learn(trainingSet);
        return myMlPerceptron;
    }

    private NormalizedInput trade2NormalizedInput(Client client, Trade trade, Double defaultRating) {
        return new NormalizedInput(client, trade, defaultRating);
    }
}