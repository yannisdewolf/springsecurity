package be.dewolf.hofleverancier.ordertaking;

import be.dewolf.hofleverancier.ordertaking.controller.OrderDTO;
import be.dewolf.hofleverancier.ordertaking.controller.OrderList;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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
		Assert.assertEquals(3, exchange.getBody().getOrders().size());
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
		testForUser(USERNAME_1, 3);
		testForUser(USERNAME_2, 2);
	}

	private void testForUser(String username1, int expectedOrdersFoundFromDummyData) {
		String url = "http://localhost:" + port + API_USER_ORDERS;
		ResponseEntity<OrderList> exchange = testRestTemplate(username1, "password").exchange(url, HttpMethod.GET, null, OrderList.class);
		Assert.assertEquals(HttpStatus.OK, exchange.getStatusCode());
		List<OrderDTO> orders = exchange.getBody().getOrders();
		Assert.assertTrue(orders.stream().allMatch(o -> o.getOrderer().equals(username1)));
		Assert.assertEquals(expectedOrdersFoundFromDummyData, orders.size());

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

	@Test
	public void afterLoginNoCredentialsRequired() {
		//login with correct credentials

		String url = "http://localhost:" + port + API_USER_ORDERS;


		String xsrfToken = getUserOrdersWithSuccessOutcome(url, null, responseAfterLogin -> {
			List<OrderDTO> orders = responseAfterLogin.getBody().getOrders();
			Assert.assertTrue(orders.stream().allMatch(o -> o.getOrderer().equals(USERNAME_1)));
			Assert.assertEquals(3, orders.size());
		}, testRestTemplate(USERNAME_1, "password"));

		printSeparator();

		HttpEntity<String> sessionIdRequestEntity = buildHeadersWithSessionId(xsrfToken);


		//get data after logging in
		getUserOrdersWithSuccessOutcome(url, sessionIdRequestEntity, responseAfterLogin -> {
			List<OrderDTO> ordersWithoutLoggingIn = responseAfterLogin.getBody().getOrders();
			Assert.assertTrue(ordersWithoutLoggingIn.stream().allMatch(o -> o.getOrderer().equals(USERNAME_1)));
			Assert.assertEquals(3, ordersWithoutLoggingIn.size());
		}, new TestRestTemplate());

		printSeparator();

		String logoutURL = "http://localhost:" + port + "/logout";
		ResponseEntity<Object> logoutResponse = new RestTemplate().exchange(logoutURL, HttpMethod.POST, sessionIdRequestEntity, Object.class);
		Assert.assertEquals(HttpStatus.OK, logoutResponse.getStatusCode());

		printSeparator();

		//get data after logout --> not possible
		ResponseEntity<OrderList> responseWithoutLoggingIn = new RestTemplate().exchange(url, HttpMethod.GET, sessionIdRequestEntity, OrderList.class);
		Assert.assertEquals(HttpStatus.UNAUTHORIZED, responseWithoutLoggingIn.getStatusCode());
	}

	private void printSeparator() {
		System.out.println("======================================");
		System.out.println("============ new call to server ======");
		System.out.println("======================================");
	}



	private HttpEntity<String> buildHeadersWithSessionId(String sessionId) {
		HttpHeaders requestHeaders = new HttpHeaders();
		//CsrfToken csrfToken = csrfTokenRepository.generateToken(null);
		requestHeaders.add("Cookie", "X-XSRF-TOKEN=" + sessionId);
		//requestHeaders.add("X-XSRF-TOKEN", );
		return new HttpEntity<>(requestHeaders);
	}

	private String getUserOrdersWithSuccessOutcome(String url, HttpEntity<String> stringHttpEntity, Consumer<ResponseEntity<OrderList>> extraChecks, TestRestTemplate restTemplate) {
		ResponseEntity<OrderList> responseWithoutLoggingIn = restTemplate.exchange(url, HttpMethod.GET, stringHttpEntity, OrderList.class);
		Assert.assertEquals(HttpStatus.OK, responseWithoutLoggingIn.getStatusCode());
		extraChecks.accept(responseWithoutLoggingIn);
		HttpHeaders headers = responseWithoutLoggingIn.getHeaders();
		//headers:
		//X-CSRF-HEADER: X-XSRF-TOKEN
		//X-CSRF-PARAM: _csrf
		//X-CSRF-TOKEN <UUID token>
		return headers.get("X-CSRF-TOKEN").get(0);
	}

	public TestRestTemplate testRestTemplate(String username, String password) {
		return new TestRestTemplate(username, password);
	}

}
