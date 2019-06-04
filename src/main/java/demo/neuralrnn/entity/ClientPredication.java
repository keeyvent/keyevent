package demo.neuralrnn.entity;

import java.math.BigDecimal;

public class ClientPredication {

    private Client client;

    private String name;

    private Double percentage;

    public ClientPredication() {

    }

    public ClientPredication(Client client, Double percentage) {
        this.client = client;
        this.percentage = percentage;
    }

    public String getGfcId() {
        return client.getGfcId();
    }

    public String getClientName() {
        return client.getName();
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPercentage() {
        return new BigDecimal(percentage).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }
}
