package demo.neuralrnn.entity;

import javax.persistence.*;
import java.math.BigDecimal;

@Table(name = "Bid")
@Entity
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bidid")
    private Long bidId;
    @ManyToOne(cascade = {CascadeType.DETACH})
    @JoinColumn(name = "product_id")

//    @Transient
    private Product product;
    //    @Transient
    @ManyToOne(cascade = {CascadeType.DETACH})
    @JoinColumn(name = "client_id")
    private Client client;
    @ManyToOne(cascade = {CascadeType.DETACH})
    @JoinColumn(name = "trade_id")
//    @Transient
    private Trade trade;
    @Column(name = "bidprice")
    private Double bidPrice;
    private Double amount;
    private Boolean succeed = false;

    public String getGfcId() {
        return client.getGfcId();
    }

    public String getClientName() {
        return client.getName();
    }

    public String getCusip() {
        return product.getCusip();
    }

    public Double getTradeYield() {
        return trade.getYield();
    }

    public String getTradeId() {
        return trade.getTradeId();
    }

    public Double getBidYield() {
        return product.getCpnRate() / bidPrice;
    }

    public Long getBidId() {
        return bidId;
    }

    public void setBidId(Long bidId) {
        this.bidId = bidId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Trade getTrade() {
        return trade;
    }

    public void setTrade(Trade trade) {
        this.trade = trade;
    }

    public Double getBidPrice() {
        return new BigDecimal(bidPrice).setScale(8, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public void setBidPrice(Double bidPrice) {
        this.bidPrice = bidPrice;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Boolean getSucceed() {
        return succeed;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setSucceed(Boolean succeed) {
        this.succeed = succeed;
    }
}
