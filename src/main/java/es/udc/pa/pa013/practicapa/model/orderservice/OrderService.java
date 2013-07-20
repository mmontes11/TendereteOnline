package es.udc.pa.pa013.practicapa.model.orderservice;

import es.udc.pa.pa013.practicapa.model.order.Order;
import es.udc.pa.pa013.practicapa.model.util.Block;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public interface OrderService {

	public Order findOrderById(Long orderId, Long userProfileId) throws InstanceNotFoundException;

	public Block<Order> findOrdersByUser(Long userProfileId, int startIndex,
			int count) throws InstanceNotFoundException;

	public Order buy(Cart cart, Long userProfileId)
			throws InstanceNotFoundException, BuyEmptyCartException;

}