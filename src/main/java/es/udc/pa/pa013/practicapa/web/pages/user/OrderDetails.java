package es.udc.pa.pa013.practicapa.web.pages.user;

import java.text.DateFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.pa.pa013.practicapa.model.order.Order;
import es.udc.pa.pa013.practicapa.model.orderline.OrderLine;
import es.udc.pa.pa013.practicapa.model.orderservice.OrderService;
import es.udc.pa.pa013.practicapa.model.userprofile.UserProfile;
import es.udc.pa.pa013.practicapa.web.services.AuthenticationPolicy;
import es.udc.pa.pa013.practicapa.web.services.AuthenticationPolicyType;
import es.udc.pa.pa013.practicapa.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED_USERS)
public class OrderDetails {
	
	private Long orderId;
	
	@Property
	private UserProfile userProfile;
	
	@Property
	private List<OrderLine> orderLines ;
	
	@Property 
	private OrderLine orderLine;
	
	@Property
	private Calendar date;
	
	@Property
	private Order order;
	
	
	@Inject
	private OrderService orderService;
	
	@SessionState(create=false)
    private UserSession userSession;
	
	@InjectPage
	private NotFound notFound;
	
	@Inject
	private Locale locale;
	
	public Format getNumberFormat (){
		return NumberFormat.getInstance(locale);
	}
	
	public Format getDateFormat (){
		return DateFormat.getDateInstance(DateFormat.SHORT, locale);
	}
	
	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	Object[] onPassivate() {
		return new Object[] {orderId};
	}

	Object onActivate(Long orderId) {
		this.orderId = orderId;
		try {
			order= orderService.findOrderById(orderId, userSession.getUserProfileId());
		} catch (InstanceNotFoundException e) {
			notFound.setID(orderId);
			return notFound;
		}
		this.userProfile = order.getUserProfile();
		this.orderLines = order.getOrderLines();
		this.date = order.getDate();
		return null;
	}
	

}
