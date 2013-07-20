package es.udc.pa.pa013.practicapa.test.model.orderservice;

import static es.udc.pa.pa013.practicapa.model.util.GlobalNames.SPRING_CONFIG_FILE;
import static es.udc.pa.pa013.practicapa.test.util.GlobalNames.SPRING_CONFIG_TEST_FILE;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import es.udc.pa.pa013.practicapa.model.category.Category;
import es.udc.pa.pa013.practicapa.model.category.CategoryDao;
import es.udc.pa.pa013.practicapa.model.order.Order;
import es.udc.pa.pa013.practicapa.model.order.OrderDao;
import es.udc.pa.pa013.practicapa.model.orderline.OrderLine;
import es.udc.pa.pa013.practicapa.model.orderline.OrderLineDao;
import es.udc.pa.pa013.practicapa.model.orderservice.BuyEmptyCartException;
import es.udc.pa.pa013.practicapa.model.orderservice.Cart;
import es.udc.pa.pa013.practicapa.model.orderservice.CartLineAmountException;
import es.udc.pa.pa013.practicapa.model.orderservice.OrderService;
import es.udc.pa.pa013.practicapa.model.product.Product;
import es.udc.pa.pa013.practicapa.model.product.ProductDao;
import es.udc.pa.pa013.practicapa.model.productservice.ProductService;
import es.udc.pa.pa013.practicapa.model.userprofile.UserProfile;
import es.udc.pa.pa013.practicapa.model.userservice.UserProfileDetails;
import es.udc.pa.pa013.practicapa.model.userservice.UserService;
import es.udc.pa.pa013.practicapa.model.util.Block;
import es.udc.pojo.modelutil.exceptions.DuplicateInstanceException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { SPRING_CONFIG_FILE, SPRING_CONFIG_TEST_FILE })
@Transactional
public class OrderServiceTest {
	
	private final long NON_EXISTING_ORDER_ID = -1L;
	
	private final long NON_EXISTING_USER_PROFILE_ID = -1L;

	private final long NON_EXISTING_PRODUCT_ID = -1L;

	@Autowired
	private OrderService orderService;

	@Autowired
	private ProductService productService;

	@Autowired
	private OrderDao orderDao;

	@Autowired
	private OrderLineDao orderLineDao;

	@Autowired
	private UserService userService;

	@Autowired
	private ProductDao productDao;

	@Autowired
	private CategoryDao categoryDao;

	private UserProfile createUserProfile(String login, String password)
			throws DuplicateInstanceException {
		UserProfile up = null;
		up = userService.registerUser(login, password, new UserProfileDetails(
				"Chuck", "Norris", "chuck.norris@gmail.com", "5th avenue,NY",
				15, "3A", 10000));
		return up;
	}

	private Order createOrder(UserProfile who, Calendar when, String version) {
		Category c = new Category("Category" + version);
		categoryDao.save(c);
		Product p1 = new Product(c, "Nexus" + version, 299.00F);
		Product p2 = new Product(c, "Galaxy" + version, 599.00F);
		productDao.save(p1);
		productDao.save(p2);
		Order o = new Order(who, when);
		orderDao.save(o);
		OrderLine ol1 = new OrderLine(p1, o, 2, p1.getPrice());
		OrderLine ol2 = new OrderLine(p2, o, 1, p2.getPrice());
		orderLineDao.save(ol1);
		orderLineDao.save(ol2);
		List<OrderLine> lines = new ArrayList<OrderLine>();
		lines.add(ol1);
		lines.add(ol2);
		o.setOrderLines(lines);
		return o;
	}

	private Cart createCart() throws InstanceNotFoundException {
		Cart cart = new Cart(productService);
		Category c = new Category("Category");
		categoryDao.save(c);
		Product p1 = new Product(c, "Product1", 20.0F);
		productDao.save(p1);
		cart.add(p1.getProductId(), 2);
		Product p2 = new Product(c, "Product2", 40.0F);
		productDao.save(p2);
		cart.add(p2.getProductId(), 1);
		return cart;
	}
	
	@Test
	public void testFindOrderById () {
		Order o = null;
		UserProfile up = null;
		try {
			up = createUserProfile("user", "password");
			o = createOrder(up , Calendar.getInstance(), "");
		} catch (DuplicateInstanceException e) {
			fail();
		}
		orderDao.save(o);
		try {
			assertEquals(o,orderService.findOrderById(o.getOrderId() , up.getUserProfileId()));
		} catch (InstanceNotFoundException e) {
			fail();
		}		
	}
	
	@Test (expected = InstanceNotFoundException.class)
	public void testFindNonExistingOrderById () throws InstanceNotFoundException {
		UserProfile up = null;
		try {
			up = createUserProfile("user", "password");
			createOrder(up , Calendar.getInstance(), "");
		} catch (DuplicateInstanceException e) {
			fail();
		}		
		orderService.findOrderById(NON_EXISTING_ORDER_ID, up.getUserProfileId());
	}
	
	@Test (expected = InstanceNotFoundException.class)
	public void testFindOrderOfOtherUser () throws InstanceNotFoundException {
		UserProfile up1 = null , up2 = null;
		Order o1 = null;
		try {
			up1 = createUserProfile("user1", "password1");
			up2 = createUserProfile("user2", "password2");
			o1 = createOrder(up1 , Calendar.getInstance(), "v1.0");
			createOrder(up2 , Calendar.getInstance(), "v2.0");
		} catch (DuplicateInstanceException e) {
			fail();
		}		
		orderService.findOrderById(o1.getOrderId(),up2.getUserProfileId());
	}

	@Test
	public void testFindExistingOrdersWithExistingUser() {
		UserProfile user = null;
		try {
			user = createUserProfile("User", "Password");
		} catch (DuplicateInstanceException e1) {
			fail();
		}
		Order todayOrder = createOrder(user, Calendar.getInstance(), "V1.0");
		List<Order> result = null;
		boolean userFound = true;
		try {
			result = orderService.findOrdersByUser(
					todayOrder.getUserProfile().getUserProfileId(), 0, 10)
					.getList();
		} catch (InstanceNotFoundException e) {
			userFound = false;
		}
		assertTrue(userFound);
		Collections.reverse(result);
		assertEquals(todayOrder, result.get(0));

		userFound = true;
		Calendar beforeDate = Calendar.getInstance();
		beforeDate.add(Calendar.DATE, -1);
		Order beforeOrder = createOrder(user, beforeDate, "V2.0");
		try {
			result = orderService.findOrdersByUser(
					todayOrder.getUserProfile().getUserProfileId(), 0, 10)
					.getList();
		} catch (InstanceNotFoundException e) {
			userFound = false;
		}
		assertTrue(userFound);
		assertTrue(beforeOrder.getDate().before(todayOrder.getDate()));
		Collections.reverse(result);
		assertEquals(todayOrder, result.get(1));
		assertTrue(todayOrder.getPrice() == 299.00F * 2 + 599.00F);
		assertEquals(beforeOrder, result.get(0));
	}
	
	@Test
	public void testFindBlockOrder (){
		UserProfile up = null;
		try {
			up = createUserProfile("user", "password");
		} catch (DuplicateInstanceException e) {
			fail();
		}		
		for (int i=0 ; i<11 ; i++){
			Order o = createOrder(up, Calendar.getInstance(), "V"+String.valueOf(i)+".0");
			orderDao.save(o);
		}
		Block<Order> blockOrder = null;
		try {
			blockOrder = orderService.findOrdersByUser(up.getUserProfileId(), 0, 10);
		} catch (InstanceNotFoundException e) {
			fail();
		} 
		assertEquals(blockOrder.getList().size(),10);
		assertTrue(blockOrder.getExistMore());
		try {
			blockOrder = orderService.findOrdersByUser(up.getUserProfileId(), 10, 10);
		} catch (InstanceNotFoundException e) {
			fail();
		}
		assertEquals(blockOrder.getList().size(),1);
		assertFalse(blockOrder.getExistMore());
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testFindOrderWithNonExistingUserProfileId()
			throws InstanceNotFoundException {
		orderService.findOrdersByUser(NON_EXISTING_USER_PROFILE_ID, 0, 10)
				.getList();
	}

	@Test
	public void testModifyCart() {
		Cart cart = null;
		try {
			cart = createCart();
		} catch (InstanceNotFoundException e1) {
			fail();
		}
		assertTrue(cart.getTotalPrice() == 80.0F);
		assertEquals(cart.getCartLines().size(), 2);
		boolean found = true, negativeAmountCartLine = false;
		try {
			cart.modify(cart.getCartLines().get(0).getProductId(), 0);
			assertEquals(cart.getCartLines().size(), 1);
			assertTrue(cart.getTotalPrice() == 40.0F);
		} catch (InstanceNotFoundException e) {
			found = false;
		} catch (CartLineAmountException e) {
			fail();
		}
		assertTrue(found);
		try {
			cart.modify(NON_EXISTING_PRODUCT_ID, 1);
		} catch (InstanceNotFoundException e) {
			found = false;
		} catch (CartLineAmountException e) {
			fail();
		}
		assertFalse(found);
		found = true;
		try {
			cart.modify(cart.getCartLines().get(0).getProductId(), -1);
		} catch (InstanceNotFoundException e) {
			found = false;
		} catch (CartLineAmountException e) {
			negativeAmountCartLine = true;
		}
		assertTrue(found);
		assertTrue(negativeAmountCartLine);
		try {
			cart.add(cart.getCartLines().get(0).getProductId(), 10);
		} catch (InstanceNotFoundException e) {
			fail();
		}
		assertEquals(cart.getCartLines().size(), 1);
		assertEquals(cart.getCartLines().get(0).getAmount(), 11);
		assertTrue(cart.getTotalPrice() == 440.0F);
		cart.clear();
		assertEquals(cart.getCartLines().size(), 0);
		assertTrue(cart.getTotalPrice() == 0.0F);
	}
	
	@Test
	public void testRemoveCart() {
		Cart cart = null;
		try {
			cart = createCart();
		} catch (InstanceNotFoundException e1) {
			fail();
		}
		assertTrue(cart.getTotalPrice() == 80.0F);
		assertEquals(cart.getCartLines().size(), 2);
		try {
			cart.removeLine(cart.getCartLines().get(1).getProductId());
			cart.removeLine(cart.getCartLines().get(0).getProductId());
		} catch (InstanceNotFoundException e) {
			fail();
		}
		assertTrue(cart.getCartLines().isEmpty());
		boolean found = true;
		try {
			cart.removeLine(NON_EXISTING_PRODUCT_ID);
		} catch (InstanceNotFoundException e) {
			found = false;
		}
		assertFalse(found);
	}

	@Test
	public void testBuyWithNonEmptyCart() {
		Cart cart = null;
		try {
			cart = createCart();
		} catch (InstanceNotFoundException e2) {
			fail();
		}
		UserProfile userProfile = null;
		try {
			userProfile = createUserProfile("User", "Password");
		} catch (DuplicateInstanceException e1) {
			fail();
		}
		Order order = null;
		try {
			order = orderService.buy(cart, userProfile.getUserProfileId());
		} catch (InstanceNotFoundException | BuyEmptyCartException e1) {
			fail();
		}
		assertTrue(cart.getCartLines().isEmpty());
		boolean found = true;
		List<OrderLine> orderLines = null;
		try {
			Order o = orderService
					.findOrdersByUser(userProfile.getUserProfileId(), 0, 10)
					.getList().get(0);
			assertEquals(order, o);
			orderLines = new ArrayList<OrderLine>(order.getOrderLines());
			Collections.sort(orderLines, new SortLines());
			assertEquals(orderLines.get(0).getProduct().getName(), "Product1");
			assertTrue(orderLines.get(0).getProduct().getPrice() == 20.00F);
			assertEquals(
					orderLines.get(0).getProduct().getCategory().getName(),
					"Category");
			assertEquals(orderLines.get(1).getProduct().getName(), "Product2");
			assertTrue(orderLines.get(1).getProduct().getPrice() == 40.00F);
			assertEquals(
					orderLines.get(1).getProduct().getCategory().getName(),
					"Category");
			assertEquals(orderLines.size(), 2);
		} catch (InstanceNotFoundException e) {
			found = false;
		}
		assertTrue(found);
	}
	
	@Test (expected = BuyEmptyCartException.class)
	public void testBuyWithEmptyCart () throws BuyEmptyCartException {
		try {
			orderService.buy(new Cart(productService), (createUserProfile("user", "password")).getUserProfileId());
		} catch (InstanceNotFoundException | DuplicateInstanceException e) {
			fail();
		}
	}
	
	
	@Test (expected = InstanceNotFoundException.class)
	public void testBuyWithNonExistingUser () throws InstanceNotFoundException{
		try {
			orderService.buy(createCart(), NON_EXISTING_USER_PROFILE_ID);
		} catch (BuyEmptyCartException e) {
			fail();
		}
	}
	
		
	private class SortLines implements Comparator<OrderLine> {

		@Override
		public int compare(OrderLine o1, OrderLine o2) {
			return (int) (o1.getOrderLineId() - o2.getOrderLineId());
		}

	}
}