package demo.neuralrnn.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "Trade")
@Entity
public class Trade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(cascade = {CascadeType.DETACH})
    @JoinColumn(name = "product_id")
    private Product product;
    @Column(name = "tradeid")
    private String tradeId;
    private Double price;
    private Date createDate;

    public Double getYield() {
        return new BigDecimal(product.getCpnRate() / price).setScale(8, BigDecimal.ROUND_HALF_UP).doubleValue() ;
    }

    public String getCusipRating() {
        return product.getRatingValue();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public Double getCpnRate() {
        return product.getCpnRate();
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getCusip() {
        return product.getCusip();
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public Double getPrice() {
        return new BigDecimal(price).setScale(8, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
