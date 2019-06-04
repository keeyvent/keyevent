package demo.neuralrnn.rule;

import demo.neuralrnn.entity.Trade;

import java.util.Arrays;

public class RatingRangeRule extends AbstractBidRule {
    private String[] ratings = null;

    public RatingRangeRule(String...  ratings) {
        this.ratings = ratings;
    }

    @Override
    protected Boolean applyLogic(Trade trade) {
        String group = trade.getProduct().getRatingGroup();
        return Arrays.stream(ratings).anyMatch(rate -> group.equalsIgnoreCase(rate));
    }
}
