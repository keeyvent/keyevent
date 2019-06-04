package demo.neuralrnn.entity;

import java.math.BigDecimal;

public class NormalizedInput {
    private Client client;
    private Trade trade;
    private Double defaultRating;
    private Boolean bidSameCusip = false;
//    private Double clientPortfolio;

    public NormalizedInput(Client client, Trade trade, Double defaultRating) {
        this.client = client;
        this.trade = trade;
        this.defaultRating = (null == defaultRating) ? trade.getYield() : defaultRating;
    }

    public double[] toDoubleArray() {
//        return new double[]{getCusipRateGroup(), getTradeYield(), defaultYieldRatio()};
        return new double[]{isRateGroup("A"),isRateGroup("B"), isRateGroup("C"), getTradeYield(), defaultYieldRatio()};
    }
    public Double getTradeYield() {
        return new BigDecimal(trade.getYield()).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public Double isRateGroup(String group) {
        return trade.getProduct().getRatingGroup().equalsIgnoreCase(group)? 1.0D:0D;
    }

    public Double getCusipRateGroup() {
        switch (trade.getProduct().getRatingGroup()) {
            case "A":
                return 1.0D;
            case "B":
                return 0.7D;
            case "C":
                return 0D;
            default:
                return -1D;
        }
    }

    public Double defaultYieldRatio() {
//        return new BigDecimal(trade.getYield()/(defaultRating)).setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
        return trade.getYield() > defaultRating ? 1.0D : 0D;
    }

    public Double getBidSameCusip() {
        return bidSameCusip ? 1D : 0D;
    }

    public Double getClientRatingPortfolio() {
        return null;
    }

    public Double getClientYieldPortfolio() {
        return null;
    }

    public Double getClientPositionRatio() {
        return null;
    }



    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Trade getTrade() {
        return trade;
    }

    public void setTrade(Trade trade) {
        this.trade = trade;
    }

    public Double getDefaultRating() {
        return defaultRating;
    }

    public void setDefaultRating(Double defaultRating) {
        this.defaultRating = defaultRating;
    }

    public void setBidSameCusip(Boolean bidSameCusip) {
        this.bidSameCusip = bidSameCusip;
    }
}
