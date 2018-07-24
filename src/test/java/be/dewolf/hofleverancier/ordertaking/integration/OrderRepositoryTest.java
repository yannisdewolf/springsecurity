package be.dewolf.hofleverancier.ordertaking.integration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("integrationtest")
@Transactional
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @PersistenceContext
    private EntityManager em;

    @Test
    public void testSave() {



        List<OrderLine> orderLines = new ArrayList<>();
        orderLines.add(OrderLine.newBuilder().build());

        Order orderToSave = Order.newBuilder()
                .withOrderLines(orderLines)
                .build();

        orderRepository.save(orderToSave);

        printAllOrders();

        getPersistedOrder(orderToSave)
                .addOrderLine(OrderLine.newBuilder().build());

        printAllOrders();

        getPersistedOrder(orderToSave)
                .setOrderer("yannis");

        printAllOrders();

//        orderLines.forEach(orderToSave::addOrderLine);
//
//        orderRepository.save(orderToSave);
    }

    private Order getPersistedOrder(Order orderToSave) {
        return orderRepository.findById(orderToSave.getId()).orElseThrow(IllegalArgumentException::new);
    }

    private void printAllOrders() {
        em.flush();
        em.clear();

        List<Order> all = orderRepository.findAll();
        System.out.println("=== found orders ===");
        all.forEach(ln -> {
            System.out.println("   * " + ln);
            ln.getOrderLines().forEach(orderLine -> {
                System.out.println("      * " + orderLine);
            });
        });
    }

}