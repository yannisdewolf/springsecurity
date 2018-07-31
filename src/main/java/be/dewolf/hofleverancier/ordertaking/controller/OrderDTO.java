package be.dewolf.hofleverancier.ordertaking.controller;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderDTO {

    private String orderer;
    private String id;


    @JsonCreator
    public OrderDTO(@JsonProperty("orderer") String orderer,
                    @JsonProperty("id") String id) {
        this.orderer = orderer;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getOrderer() {
        return orderer;
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
                "orderer='" + orderer + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
