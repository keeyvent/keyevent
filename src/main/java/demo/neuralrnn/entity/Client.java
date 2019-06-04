package demo.neuralrnn.entity;

import javax.persistence.*;

@Table(name = "Client")
@Entity
public class Client {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "gfc_id")
    private String gfcId;
    @Column(name = "name")
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGfcId() {
        return gfcId;
    }

    public void setGfcId(String gfcId) {
        this.gfcId = gfcId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
