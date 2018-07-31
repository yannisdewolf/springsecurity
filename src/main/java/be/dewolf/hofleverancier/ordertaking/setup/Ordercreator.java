package be.dewolf.hofleverancier.ordertaking.setup;

import be.dewolf.hofleverancier.ordertaking.integration.Order;
import be.dewolf.hofleverancier.ordertaking.integration.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile({"integrationtest", "dev"})
public class Ordercreator implements CommandLineRunner {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public void run(String... args) throws Exception {
        if(orderRepository.findByOrderer("yannis").isEmpty()) {
            createOrder("yannis");
            createOrder("yannis");
        }
        if(orderRepository.findByOrderer("amke").isEmpty()) {
            createOrder("amke");
            createOrder("amke");
            createOrder("amke");
        }
    }

    private void createOrder(String orderer) {
        Order entity = Order.newBuilder().withOrderer(orderer).build();
        orderRepository.save(entity);
    }
}
