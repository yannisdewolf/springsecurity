package be.dewolf.hofleverancier.ordertaking.integration;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "ORDER_LINES")
public class OrderLine implements Serializable {

    @Id
    @Column(name = "OLE_ID")
    @Basic
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID", referencedColumnName = "ORDER_ID", nullable = false)
    private Order order;

    public OrderLine() {
    }

    public String getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    private OrderLine(Builder builder) {
        id = builder.id;
        //order = builder.order;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    void setOrder(Order order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "OrderLine{" +
                "id='" + id + '\'' +
                ", order=" + order.getId() +
                '}';
    }

    public static final class Builder {
        private Order order;
        private String id;

        private Builder() {
            id = UUID.randomUUID().toString();
        }

        public Builder withOrder(Order val) {
            order = val;
            return this;
        }

        public OrderLine build() {
            return new OrderLine(this);
        }
    }
}
