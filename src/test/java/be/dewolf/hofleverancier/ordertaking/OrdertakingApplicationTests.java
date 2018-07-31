package be.dewolf.hofleverancier.ordertaking;

import be.dewolf.hofleverancier.ordertaking.controller.OrderList;
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

	public static final String API_USER_ORDERS = "/api/user/orders";
	public static final String API_ADMIN_ORDERS = "/api/admin/orders";
	public static final String USERNAME_1 = "amke";
	public static final String USERNAME_2 = "yannis";
	@Value("${local.server.port}")
	protected int port;

	@Autowired
	private RestTemplate restTemplate;

	@Test
	public void contextLoads() {
	}

	@Test
	public void testGetUserOrdersWithUserCredentials_succeeds() {
		String url = "http://localhost:" + port + API_USER_ORDERS;
		ResponseEntity<OrderList> exchange = testRestTemplate(USERNAME_1, "password").exchange(url, HttpMethod.GET, null, OrderList.class);
		System.out.println(exchange);
		Assert.assertEquals(HttpStatus.OK, exchange.getStatusCode());
		Assert.assertEquals(2, exchange.getBody().getOrders().size());
	}

	@Test
	public void testGetUserOrdersWithAdminCredentials_failsWithForbidden() {
		String url = "http://localhost:" + port + API_USER_ORDERS;
		ResponseEntity<OrderList> exchange = testRestTemplate("adminuser", "adminpassword").exchange(url, HttpMethod.GET, null, OrderList.class);
		System.out.println(exchange);
		Assert.assertEquals(HttpStatus.FORBIDDEN, exchange.getStatusCode());
	}

	@Test
	public void testGetAdminOrdersWithUserCredentials_failsWithForbidden() {
		String url = "http://localhost:" + port + API_ADMIN_ORDERS;
		ResponseEntity<OrderList> exchange = testRestTemplate(USERNAME_1, "password").exchange(url, HttpMethod.GET, null, OrderList.class);
		System.out.println(exchange);
		Assert.assertEquals(HttpStatus.FORBIDDEN, exchange.getStatusCode());
	}

	@Test
	public void testGetUserOrdersWithoutCredentials_failsWithUnauthorized() {
		String url = "http://localhost:" + port + API_USER_ORDERS;
		ResponseEntity<String> exchange = testRestTemplate(null, null).exchange(url, HttpMethod.GET, null, String.class);
		System.out.println(exchange);
		Assert.assertEquals(HttpStatus.UNAUTHORIZED, exchange.getStatusCode());
	}

	@Test
	public void basicInfoWithoutLogin_succeeds() {
		String url = "http://localhost:" + port + "/basicinfo";
		ResponseEntity<String> exchange = testRestTemplate(null, null).exchange(url, HttpMethod.GET, null, String.class);
		System.out.println(exchange);
		Assert.assertEquals(HttpStatus.OK, exchange.getStatusCode());
	}

	@Test
	public void basicInfoUserLogin_succeeds() {
		String url = "http://localhost:" + port + "/basicinfo";
		ResponseEntity<String> exchange = testRestTemplate(USERNAME_1, "password").exchange(url, HttpMethod.GET, null, String.class);
		System.out.println(exchange);
		Assert.assertEquals(HttpStatus.OK, exchange.getStatusCode());
	}

	@Test
	public void basicInfoAdminLogin_succeeds() {
		String url = "http://localhost:" + port + "/basicinfo";
		ResponseEntity<String> exchange = testRestTemplate("adminuser", "adminpassword").exchange(url, HttpMethod.GET, null, String.class);
		System.out.println(exchange);
		Assert.assertEquals(HttpStatus.OK, exchange.getStatusCode());
	}

	@Test
	public void publicDisclaimerWithoutLogin_succeeds() {
		String url = "http://localhost:" + port + "/publicdisclaimer";
		ResponseEntity<String> exchange = testRestTemplate(null, null).exchange(url, HttpMethod.GET, null, String.class);
		System.out.println(exchange);
		Assert.assertEquals(HttpStatus.OK, exchange.getStatusCode());
	}

	@Test
	public void getUserOrdersGetsOnlyOrdersForLoggedInUser() {
		testForUser(USERNAME_1);
		testForUser(USERNAME_2);
	}

	private void testForUser(String username1) {
		String url = "http://localhost:" + port + API_USER_ORDERS;
		ResponseEntity<OrderList> exchange = testRestTemplate(username1, "password").exchange(url, HttpMethod.GET, null, OrderList.class);
		Assert.assertEquals(HttpStatus.OK, exchange.getStatusCode());
		exchange.getBody().getOrders().stream().allMatch(o -> o.getOrderer().equals(username1));
	}

	@Test
	public void testGetAdminOrdersWithAdminCredentials_succeeds() {
		String url = "http://localhost:" + port + API_ADMIN_ORDERS;
		ResponseEntity<OrderList> exchange = testRestTemplate("adminuser", "adminpassword").exchange(url, HttpMethod.GET, null, OrderList.class);
		System.out.println(exchange);
		Assert.assertEquals(HttpStatus.OK, exchange.getStatusCode());
		Assert.assertEquals(4, exchange.getBody().getOrders().size());
	}

	@Test
	public void wrongUsernameReturns401() {
		String url = "http://localhost:" + port + API_ADMIN_ORDERS;
		ResponseEntity<OrderList> exchange = testRestTemplate("adminuser_not_existing", "adminpassword").exchange(url, HttpMethod.GET, null, OrderList.class);
		System.out.println(exchange);
		Assert.assertEquals(HttpStatus.UNAUTHORIZED, exchange.getStatusCode());
	}

	@Test
	public void wrongPasswordReeturns401() {
		String url = "http://localhost:" + port + API_ADMIN_ORDERS;
		ResponseEntity<OrderList> exchange = testRestTemplate("adminuser", "adminpassword_wrong").exchange(url, HttpMethod.GET, null, OrderList.class);
		System.out.println(exchange);
		Assert.assertEquals(HttpStatus.UNAUTHORIZED, exchange.getStatusCode());
	}

	public TestRestTemplate testRestTemplate(String username, String password) {
		return new TestRestTemplate(username, password);
	}

}
