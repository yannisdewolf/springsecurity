package be.dewolf.hofleverancier.ordertaking;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = OrdertakingApplication.class)
@ActiveProfiles("integrationtest")
public class OrdertakingApplicationTests {

	@Value("${local.server.port}")
	protected int port;

	@Autowired
	private RestTemplate restTemplate;

	@Test
	public void contextLoads() {
	}

	@Test
	public void testGetUserOrdersWithUserCredentials_succeeds() {
		String url = "http://localhost:" + port + "/user/orders";
		ResponseEntity<OrderList> exchange = testRestTemplate("usertestconfig", "password").exchange(url, HttpMethod.GET, null, OrderList.class);
		System.out.println(exchange);
		Assert.assertEquals(HttpStatus.OK, exchange.getStatusCode());
		Assert.assertEquals(2, exchange.getBody().getOrders().size());
	}

	@Test
	public void testGetUserOrdersWithAdminCredentials_failsWithUnauthorized() {
		String url = "http://localhost:" + port + "/user/orders";
		ResponseEntity<OrderList> exchange = testRestTemplate("admin", "adminpassword").exchange(url, HttpMethod.GET, null, OrderList.class);
		System.out.println(exchange);
		Assert.assertEquals(HttpStatus.UNAUTHORIZED, exchange.getStatusCode());
	}

	@Test
	public void testGetUserOrdersWithoutCredentials_failsWithUnauthorized() {
		String url = "http://localhost:" + port + "/user/orders";
		ResponseEntity<Object> exchange = testRestTemplate(null, null).exchange(url, HttpMethod.GET, null, Object.class);
		System.out.println(exchange);
		Assert.assertEquals(HttpStatus.UNAUTHORIZED, exchange.getStatusCode());
	}

	@Test
	public void testGetAdminOrdersWithAdminCredentials_succeeds() {
		String url = "http://localhost:" + port + "/admin/orders";
		ResponseEntity<OrderList> exchange = testRestTemplate("adminuser", "adminpassword").exchange(url, HttpMethod.GET, null, OrderList.class);
		System.out.println(exchange);
		Assert.assertEquals(HttpStatus.OK, exchange.getStatusCode());
		Assert.assertEquals(1, exchange.getBody().getOrders().size());
	}

	public TestRestTemplate testRestTemplate(String username, String password) {
		return new TestRestTemplate(username, password);
	}

}
