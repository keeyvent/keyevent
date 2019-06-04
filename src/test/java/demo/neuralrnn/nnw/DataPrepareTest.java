package demo.neuralrnn.nnw;

import demo.neuralrnn.data.impl.BidGenerator;
import demo.neuralrnn.data.impl.CusipGenerator;
import demo.neuralrnn.data.impl.TradeGenerator;
import demo.neuralrnn.entity.Client;
import demo.neuralrnn.entity.Trade;
import demo.neuralrnn.repository.ClientRepository;
import demo.neuralrnn.rule.Rule;

import java.util.Map;

public abstract class DataPrepareTest extends AbstractContextAwareTest {
    private static Boolean initialized = false;

    @Override
    public void afterApplicationContext() {
        synchronized (initialized) {
            if (!initialized) {
                CusipGenerator cusipGenerator = (CusipGenerator) applicationContext.getBean("cusipGenerator");
                TradeGenerator tradeGenerator = (TradeGenerator) applicationContext.getBean("tradeGenerator");
                BidGenerator bidGenerator = (BidGenerator) applicationContext.getBean("bidGenerator");
                ClientRepository clientRepository = (ClientRepository)applicationContext.getBean("clientRepository");

                Map<Client, Rule<Boolean, Trade>> map = getClientMap(clientRepository);

                cusipGenerator.generate(1000);
                tradeGenerator.generate(map);
                bidGenerator.generate(map);
                initialized = true;
            }
        }

    }

    protected abstract Map<Client, Rule<Boolean, Trade>> getClientMap(ClientRepository clientRepository);
}
