package es.udc.pa.pa013.practicapa.model.orderservice;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.pa.pa013.practicapa.model.order.Order;
import es.udc.pa.pa013.practicapa.model.order.OrderDao;
import es.udc.pa.pa013.practicapa.model.orderline.OrderLine;
import es.udc.pa.pa013.practicapa.model.orderline.OrderLineDao;
import es.udc.pa.pa013.practicapa.model.product.Product;
import es.udc.pa.pa013.practicapa.model.product.ProductDao;
import es.udc.pa.pa013.practicapa.model.userprofile.UserProfileDao;
import es.udc.pa.pa013.practicapa.model.util.Block;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@Service("orderService")
@Transactional
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderDao orderDao;

	@Autowired
	private UserProfileDao userProfileDao;

	@Autowired
	private OrderLineDao orderLineDao;

	@Autowired
	private ProductDao productDao;

	@Override
	public Order findOrderById(Long orderId, Long userProfileId)
			throws InstanceNotFoundException {
		Order o = orderDao.find(orderId);
		if (o.getUserProfile().getUserProfileId() != userProfileId) {
			throw new InstanceNotFoundException(orderId, Order.class.getName());
		}
		return o;
	}

	@Override
	public Block<Order> findOrdersByUser(Long userProfileId, int startIndex,
			int count) throws InstanceNotFoundException {
		userProfileDao.find(userProfileId);
		/*
		 * Find count+1 products to determine if there exist more products above
		 * the specified range.
		 */
		List<Order> orders = orderDao.findOrdersByUser(userProfileId,
				startIndex, count + 1);

		boolean existMoreOrders = orders.size() == (count + 1);
		/*
		 * Remove the last product from the returned list if there exist more
		 * product above the specified range.
		 */
		if (existMoreOrders) {
			orders.remove(orders.size() - 1);
		}

		return new Block<Order>(orders, existMoreOrders);
	}

	@Override
	public Order buy(Cart cart, Long userProfileId)
			throws InstanceNotFoundException, BuyEmptyCartException {
		if (cart.getCartLines().isEmpty()) {
			throw new BuyEmptyCartException();
		}
		Order o = new Order(userProfileDao.find(userProfileId),
				Calendar.getInstance());
		orderDao.save(o);
		List<OrderLine> orderLines = new ArrayList<>();
		List<Product> products = productDao.findProducts(CartLine
				.getProductIds(cart.getCartLines()));
		if (products.size() != cart.getCartLines().size()) {
			throw new RuntimeException(
					"La lista de productos y lista de lineas de carrito no tiene la misma longitud");
		}
		Collections.sort(products,new SortProduct());
		Collections.sort(cart.getCartLines(),new SortCartLines());
		int amount;
		for (int i = 0; i < products.size(); i++) {
			amount = cart.getCartLines().get(i).getAmount();
			OrderLine ol = new OrderLine(products.get(i), o, amount, products.get(i).getPrice());
			orderLineDao.save(ol);
			orderLines.add(ol);
			productDao.incrementSell(products.get(i).getProductId(), amount);
		}
		Collections.sort(orderLines,new SortOrderLine());
		o.setOrderLines(orderLines);
		cart.clear();
		return o;
	}
	
	public class SortProduct implements Comparator<Product> {

		@Override
		public int compare(Product o1, Product o2) {
			return (int) (o1.getProductId() - o2.getProductId());
		}	
	}
	
	public class SortCartLines implements Comparator<CartLine> {

		@Override
		public int compare(CartLine o1, CartLine o2) {
			return (int) (o1.getProductId() - o2.getProductId());
		}
	}
	
	public class SortOrderLine implements Comparator<OrderLine>{

		@Override
		public int compare(OrderLine o1, OrderLine o2) {
			return (int) (o2.getOrderLineId() - o1.getOrderLineId());
		}
		
	}

}