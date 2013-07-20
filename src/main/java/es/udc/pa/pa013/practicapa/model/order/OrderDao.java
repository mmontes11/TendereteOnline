package es.udc.pa.pa013.practicapa.model.order;

import java.util.List;

import es.udc.pa.pa013.practicapa.model.orderline.OrderLine;
import es.udc.pojo.modelutil.dao.GenericDao;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public interface OrderDao extends GenericDao<Order, Long> {

	public List<Order> findOrdersByUser(Long userProfileId, int startIndex, int count)
			throws InstanceNotFoundException;

	public List<OrderLine> getOrderLines(Long orderId)
			throws InstanceNotFoundException;

}
