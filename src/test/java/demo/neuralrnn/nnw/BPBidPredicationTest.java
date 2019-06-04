package demo.neuralrnn.nnw;

import demo.neuralrnn.config.NeuralConfig;
import demo.neuralrnn.entity.Client;
import demo.neuralrnn.entity.DefaultRating;
import demo.neuralrnn.entity.NormalizedInput;
import demo.neuralrnn.entity.Trade;
import demo.neuralrnn.repository.*;
import demo.neuralrnn.rule.RatingRangeRule;
import demo.neuralrnn.rule.Rule;
import demo.neuralrnn.rule.YieldRangeRule;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.util.TransferFunctionType;
import org.neuroph.util.random.WeightsRandomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {NeuralConfig.class})
@EnableAutoConfiguration
@EnableJpaRepositories(basePackages = "demo.neuralrnn.repository")
public class BPBidPredicationTest extends DataPrepareTest {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private TradeRepository tradeRepository;
    @Autowired
    private BidRepository bidRepository;
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private DefaultRatingRepository defaultRatingRepository;
    private Double trainingRatio = 0.7D;

//    @Before
//    public  void initWebServer() {
//        try {
//            new Server().runTool(new String[]{"-web", "-webAllowOthers","-webPort", "8090"});
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

    @Test
//    @Sql(scripts = {"classpath:db/dml/drop.sql"})
//    @Sql(scripts = {"classpath:db/ddl/bid.ddl.sql",
//            "classpath:db/ddl/client.ddl.sql",
//            "classpath:db/ddl/default_rating.ddl.sql",
//            "classpath:db/ddl/product.ddl.sql",
//            "classpath:db/ddl/trade.ddl.sql"
//    })
//    @Sql(scripts = {"classpath:db/data/client.data.sql"})
//    @Sql(scripts = {"classpath:db/data/default_rating.data.sql"})
//    @Sql(scripts = {"classpath:db/data/product.data.sql"})
    public void testBasicYieldRangeRule() {
        List<Trade> trades = tradeRepository.findAll();

        Map<String, Double> defaults = defaultRatingRepository.findAll().stream().collect(Collectors.toMap(DefaultRating::getSpRating, DefaultRating::getSpRate));
        Client client = clientRepository.findById(1000L).get();

        List<NormalizedInput> all = trades.stream().map(trade -> new NormalizedInput(client, trade, defaults.get(trade.getProduct().getRatingValue()))).collect(Collectors.toList());
        List<NormalizedInput> bidTrades = all.stream().filter(input -> null != bidRepository.findBidByTradeIdAndClientId(input.getTrade().getTradeId(), client.getId())).collect(Collectors.toList());
        List<NormalizedInput> dropTrades = all.stream().filter(input -> !bidTrades.contains(input)).collect(Collectors.toList());

        DataSet trainingSet = new DataSet(5, 1);
        MultiLayerPerceptron myMlPerceptron = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, 5, 9, 9, 1);
        myMlPerceptron.randomizeWeights(new WeightsRandomizer(new Random(123)));
        myMlPerceptron.setLearningRule(new BackPropagation());

        myMlPerceptron.getLearningRule().setLearningRate(0.01);
        // enable batch if using MomentumBackpropagation
//        if( myMlPerceptron.getLearningRule() instanceof MomentumBackpropagation )
//        	((MomentumBackpropagation)myMlPerceptron.getLearningRule()).setBatchMode(false);

        LearningRule learningRule = myMlPerceptron.getLearningRule();
        learningRule.addListener((event) -> {
            BackPropagation bp = (BackPropagation) event.getSource();
            if (event.getEventType() != LearningEvent.Type.LEARNING_STOPPED)
                System.out.println(bp.getCurrentIteration() + ". iteration : " + bp.getTotalNetworkError());
        });
//        split(bidTrades).getKey().stream().forEach(data -> {
////            trainingSet.addRow(new DataSetRow(data.toDoubleArray(), new double[]{1}));
////        });
////        split(dropTrades).getKey().stream().forEach(data -> {
////            trainingSet.addRow(new DataSetRow(data.toDoubleArray(), new double[]{0}));
////        });


        bidTrades.stream().forEach(data -> {
            trainingSet.addRow(new DataSetRow(data.toDoubleArray(), new double[]{1}));
        });
        dropTrades.stream().forEach(data -> {
            trainingSet.addRow(new DataSetRow(data.toDoubleArray(), new double[]{0}));
        });


        // learn the training set
        System.out.println("Training neural network...");
        myMlPerceptron.learn(trainingSet);

        // test perceptron
        System.out.println("Testing trained neural network");
        testNeuralNetwork(myMlPerceptron, trainingSet);
    }


    private Pair<List<NormalizedInput>, List<NormalizedInput>> split(List<NormalizedInput> inputs) {
        int sub = (int) (inputs.size() * trainingRatio);
        return Pair.of(inputs.subList(0, sub), inputs.subList(sub, inputs.size()));
    }

    public void testNeuralNetwork(NeuralNetwork neuralNet, DataSet testSet) {

        for (DataSetRow testSetRow : testSet.getRows()) {
            neuralNet.setInput(testSetRow.getInput());
            neuralNet.calculate();
            double[] networkOutput = neuralNet.getOutput();

            System.out.print("Input: " + Arrays.toString(testSetRow.getInput()));
            System.out.println(" Output: " + Arrays.toString(networkOutput));
        }
    }

    @Override
    protected Map<Client, Rule<Boolean, Trade>> getClientMap(ClientRepository clientRepository) {

        Client client = clientRepository.findById(1000L).get();
        assertNotNull(client);

        Rule greedy = new YieldRangeRule(0.10D, null);
        Rule rule = new RatingRangeRule("AAA", "AA", "A", "BBB", "BB", "B");
        rule.or(greedy);

        Map<Client, Rule<Boolean, Trade>> map = new HashMap<>();
        map.put(client, rule);
        return map;

    }
}
