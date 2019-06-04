package demo.neuralrnn.data.impl;

import demo.neuralrnn.data.DataGenerator;
import demo.neuralrnn.entity.Client;
import demo.neuralrnn.entity.Trade;
import demo.neuralrnn.repository.ClientRepository;
import demo.neuralrnn.rule.RatingRangeRule;
import demo.neuralrnn.rule.Rule;
import demo.neuralrnn.rule.YieldRangeRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ClientStrategyGenerator implements DataGenerator<String, Map<Client, Rule<Boolean, Trade>>> {
    @Autowired
    private ClientRepository clientRepository;

    public final static String BASIC_STRATEGY ="BASIC_STRATEGY";

    @Override
    public Map<Client, Rule<Boolean, Trade>> generate(String strategy) {
        if (strategy.equalsIgnoreCase(BASIC_STRATEGY)) {
            return createBasicStrategy();
        }
        //TODO
        return createBasicStrategy();
    }

    public Map<Client, Rule<Boolean, Trade>> createBasicStrategy() {
        Map<Client, Rule<Boolean, Trade>> map = new HashMap<>();
        //
        map.put(clientRepository.findById(1000L).get(), new RatingRangeRule("AAA", "AA", "A"));
        //
        map.put(clientRepository.findById(1001L).get(), new RatingRangeRule("AAA", "AA", "A", "BBB", "BB", "B"));
        //
        Client playSafeWithALittleGreedy = clientRepository.findById(1002L).get();
        Rule greedy = new YieldRangeRule(0.05D, null);
        Rule rule = new RatingRangeRule("AAA", "AA", "A", "BBB", "BB", "B");
        rule.or(greedy);
        map.put(playSafeWithALittleGreedy, rule);
        //
        map.put(clientRepository.findById(1003L).get(), new YieldRangeRule(0.10D, null));

//        INSERT INTO CLIENT VALUES(1000,'0000853956','Miss PlaySafe');
//        INSERT INTO CLIENT VALUES(1001,'0008520689','Mr A&B');
//        INSERT INTO CLIENT VALUES(1002,'0026760077','Miss Greedy');
//        INSERT INTO CLIENT VALUES(1003,'0025530809','Mr Crazy');
        return map;
    }
}
