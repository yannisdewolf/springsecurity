package be.dewolf.hofleverancier.ordertaking.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
public class OrderController {

    @GetMapping("/api/user/orders")
    @Secured("ROLE_USER")
    public OrderList ordersForUser(Authentication authentication) {
        System.out.println("Authentication: " + authentication);
        List<OrderDTO> userOrders = getUserOrders(authentication.getName());
        return new OrderList(userOrders);
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
