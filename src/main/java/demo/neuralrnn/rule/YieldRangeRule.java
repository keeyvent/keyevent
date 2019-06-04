package demo.neuralrnn.rule;

import demo.neuralrnn.entity.Trade;

public class YieldRangeRule extends AbstractBidRule {
    private Double from;
    private Double to;

    public YieldRangeRule(Double from, Double to) {
        this.from = from;
        this.to = to;
    }

    @Override
    protected Boolean applyLogic(Trade trade) {
        if (null == from && null == to) {
            return true;
        }
        if (null == from) {
            return trade.getYield() <= to;
        }

        if (null == to) {
            return trade.getYield() >= from;
        }
        return trade.getYield() >= from && trade.getYield() <= to;
    }
}
