package be.dewolf.hofleverancier.ordertaking.integration;



import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "ORDERS")
public class Order implements Serializable {

    @Id
    @Basic
    @Column(name = "ORDER_ID")
    private String id;
//    @Id
//    @GeneratedValue(generator = "UUID")
//    @GenericGenerator(
//            name = "UUID",
//            strategy = "org.hibernate.id.UUIDGenerator"
//    )
//    @Column(name = "ORDER_ID", updatable = false, nullable = false)
//    private UUID id;

    @Version
//    @Column(name = "VERSION")
    private Long version;

    @Column(name = "ORDERER")
    private String orderer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<OrderLine> orderLines = new ArrayList<>();

    public Order() {
    }

    public String getId() {
        return id;
    }

    public Long getVersion() {
        return version;
    }

    public void setOrderer(String orderer) {
        this.orderer = orderer;
    }

    public void addOrderLine(OrderLine orderLine) {
        orderLine.setOrder(this);
        if(this.orderLines == null)
            this.orderLines = new ArrayList<>();
        this.orderLines.add(orderLine);
    }

    private Order(Builder builder) {
        orderLines = builder.orderLines;
        orderLines.forEach(ol -> ol.setOrder(this));
        id = builder.id;
    }


    public List<OrderLine> getOrderLines() {
        return orderLines;
    }

    public String getOrderer() {
        return orderer;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", version=" + version +
                ", orderer='" + orderer + '\'' +
                ", orderLines=" + orderLines +
                '}';
    }

    public static final class Builder {
        private List<OrderLine> orderLines;
        private String id;

        private Builder() {
            id = UUID.randomUUID().toString();
        }

        public Builder withOrderLines(List<OrderLine> val) {
            orderLines = val;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
}
