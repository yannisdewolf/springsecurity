package be.dewolf.hofleverancier.ordertaking.controller;

import be.dewolf.hofleverancier.ordertaking.integration.Order;
import be.dewolf.hofleverancier.ordertaking.integration.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class OrderController {


    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/api/user/orders")
    @Secured("ROLE_USER")
    public OrderList ordersForUser(Authentication authentication) {
        List<Order> byOrderer = orderRepository.findByOrderer(authentication.getName());
        List<OrderDTO> collect = byOrderer.stream().map(this::toDTO).collect(Collectors.toList());

        return new OrderList(collect);
    }

    private OrderDTO toDTO(Order order) {
        return new OrderDTO(order.getOrderer(), order.getId());
    }

    private List<OrderDTO> getUserOrders(String username) {
        OrderDTO order = new OrderDTO(username, UUID.randomUUID().toString());
        OrderDTO order2 = new OrderDTO(username, UUID.randomUUID().toString());
        return Arrays.asList(order, order2);
    }


    @GetMapping("/api/admin/orders")
    @Secured("ROLE_ADMIN")
    public OrderList allOrders(Authentication authentication) {
        System.out.println("Authentication: " + authentication);

        List<OrderDTO> l = new ArrayList<>();
        l.addAll(getUserOrders("yannis"));
        l.addAll(getUserOrders("amke"));

        return new OrderList(l);
    }

}
