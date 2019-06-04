package demo.neuralrnn.rule;

import demo.neuralrnn.entity.Trade;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractBidRule implements Rule<Boolean, Trade> {
    protected List<Rule<Boolean, Trade>> ands = new ArrayList<>();
    protected List<Rule<Boolean, Trade>> ors = new ArrayList<>();

    @Override
    public Boolean apply(Trade trade) {
        Boolean orFlag = ors.parallelStream().anyMatch(rule -> rule.apply(trade))||applyLogic(trade);
        Boolean andFlag = CollectionUtils.isEmpty(ands) ? Boolean.TRUE : ands.parallelStream().allMatch(rule -> rule.apply(trade));
        return orFlag && andFlag;
    }

    protected abstract Boolean applyLogic(Trade trade);

    @Override
    public void and(Rule<Boolean, Trade> rule) {
        ands.add(rule);
    }

    @Override
    public void or(Rule<Boolean, Trade> rule) {
        ors.add(rule);
    }
}
