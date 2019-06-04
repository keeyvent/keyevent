package demo.neuralrnn.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DEFAULT_RATING")
public class DefaultRating {
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "moody_rating")
    private String moodyRating;
    @Column(name = "moody_rate")
    private Double moodyRate;
    @Column(name = "sp_rating")
    private String spRating;
    @Column(name = "sp_rate")
    private Double spRate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMoodyRating() {
        return moodyRating;
    }

    public void setMoodyRating(String moodyRating) {
        this.moodyRating = moodyRating;
    }

    public Double getMoodyRate() {
        return moodyRate;
    }

    public void setMoodyRate(Double moodyRate) {
        this.moodyRate = moodyRate;
    }

    public String getSpRating() {
        return spRating;
    }

    public void setSpRating(String spRating) {
        this.spRating = spRating;
    }

    public Double getSpRate() {
        return spRate;
    }

    public void setSpRate(Double spRate) {
        this.spRate = spRate;
    }
}
