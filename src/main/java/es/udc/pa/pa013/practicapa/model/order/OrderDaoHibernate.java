package es.udc.pa.pa013.practicapa.model.order;

import java.util.List;

import org.springframework.stereotype.Repository;

import es.udc.pa.pa013.practicapa.model.orderline.OrderLine;
import es.udc.pojo.modelutil.dao.GenericDaoHibernate;

@Repository("orderDao")
public class OrderDaoHibernate extends GenericDaoHibernate<Order, Long>
		implements OrderDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<Order> findOrdersByUser(Long userProfileId, int startIndex,
			int count) {
		String queryString = "SELECT o FROM Order o WHERE o.userProfile.userProfileId = :userProfileId ORDER BY o.date DESC";
		return getSession().createQuery(queryString)
				.setParameter("userProfileId", userProfileId)
				.setFirstResult(startIndex).setMaxResults(count).list();

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<OrderLine> getOrderLines(Long orderId) {
		String queryString = "SELECT ol FROM OrderLine ol WHERE ol.order.orderId = :orderId";
		return getSession().createQuery(queryString)
				.setParameter("orderId", orderId).list();
	}

}
