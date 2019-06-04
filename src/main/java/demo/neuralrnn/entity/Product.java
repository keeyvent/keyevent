package demo.neuralrnn.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "Product")
public class Product {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "cpnrate")
    private Double cpnRate;
    private String cusip;
    @Column(name = "issshrtname")
    private String issShrtName;
    private String matD;
    @Column(name = "cpneffd")
    private String cpnEffD;
    @Column(name = "frstcpnd")
    private String frstCpnD;
    @Column(name = "prodtyp")
    private String prodTyp;
    @Column(name = "ratingagency")
    private String ratingAgency;
    @Column(name = "ratingvalue")
    private String ratingValue;
    @Column(name = "ratinggroup")
    private String ratingGroup;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getCpnRate() {
        return new BigDecimal(cpnRate).setScale(8, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public void setCpnRate(Double cpnRate) {
        this.cpnRate = cpnRate;
    }

    public String getCusip() {
        return cusip;
    }

    public void setCusip(String cusip) {
        this.cusip = cusip;
    }

    public String getIssShrtName() {
        return issShrtName;
    }

    public void setIssShrtName(String issShrtName) {
        this.issShrtName = issShrtName;
    }

    public String getMatD() {
        return matD;
    }

    public void setMatD(String matD) {
        this.matD = matD;
    }

    public String getCpnEffD() {
        return cpnEffD;
    }

    public void setCpnEffD(String cpnEffD) {
        this.cpnEffD = cpnEffD;
    }

    public String getFrstCpnD() {
        return frstCpnD;
    }

    public void setFrstCpnD(String frstCpnD) {
        this.frstCpnD = frstCpnD;
    }

    public String getProdTyp() {
        return prodTyp;
    }

    public void setProdTyp(String prodTyp) {
        this.prodTyp = prodTyp;
    }

    public String getRatingAgency() {
        return ratingAgency;
    }

    public void setRatingAgency(String ratingAgency) {
        this.ratingAgency = ratingAgency;
    }

    public String getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(String ratingValue) {
        this.ratingValue = ratingValue;
    }

    public String getRatingGroup() {
        return ratingGroup;
    }

    public void setRatingGroup(String ratingGroup) {
        this.ratingGroup = ratingGroup;
    }
}
