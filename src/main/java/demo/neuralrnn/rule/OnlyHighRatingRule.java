package demo.neuralrnn.rule;

import demo.neuralrnn.entity.Product;
import demo.neuralrnn.entity.Trade;

public class OnlyHighRatingRule extends AbstractBidRule {
    @Override
    protected Boolean applyLogic(Trade trade) {
        Product product = trade.getProduct();
        if (isHighRating(product)) {
            return true;
        }
        return false;
    }

    private boolean isHighRating(Product product) {
        return product.getRatingValue().contains("B") || product.getRatingValue().contains("A");
    }
}
