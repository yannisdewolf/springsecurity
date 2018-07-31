package be.dewolf.hofleverancier.ordertaking;

import be.dewolf.hofleverancier.ordertaking.integration.Order;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@RestController
public class OrdertakingApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrdertakingApplication.class, args);
	}

	@GetMapping("/api/user/orders")
	public OrderList getOrders() {
		Order order = new Order();
		order.setOrderer("yannis");

		Order order2 = new Order();
		order2.setOrderer("amke");
		return new OrderList(Arrays.asList(order, order2));
	}

	@GetMapping("/api/admin/orders")
	public OrderList ordersForAdmin() {
		Order order = new Order();
		order.setOrderer("admin");
		return new OrderList(Arrays.asList(order));
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}

class OrderList {

	private List<Order> orders;

	@JsonCreator
	public OrderList(@JsonProperty("orders") List<Order> orders) {
		this.orders = orders;
	}

	public List<Order> getOrders() {
		return orders;
	}

	@Override
	public String toString() {
		return "OrderList{" +
				"orders=" + orders +
				'}';
	}
}
