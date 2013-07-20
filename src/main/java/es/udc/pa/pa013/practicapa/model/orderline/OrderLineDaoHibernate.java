package es.udc.pa.pa013.practicapa.model.orderline;

import org.springframework.stereotype.Repository;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;

@Repository("orderLineDao")
public class OrderLineDaoHibernate extends GenericDaoHibernate<OrderLine, Long>
		implements OrderLineDao {

}
