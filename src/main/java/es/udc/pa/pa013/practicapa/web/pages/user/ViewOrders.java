package es.udc.pa.pa013.practicapa.web.pages.user;

import java.text.DateFormat;
import java.text.Format;
import java.util.List;
import java.util.Locale;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

import es.udc.pa.pa013.practicapa.model.order.Order;
import es.udc.pa.pa013.practicapa.model.orderservice.OrderService;
import es.udc.pa.pa013.practicapa.model.util.Block;
import es.udc.pa.pa013.practicapa.web.services.AuthenticationPolicy;
import es.udc.pa.pa013.practicapa.web.services.AuthenticationPolicyType;
import es.udc.pa.pa013.practicapa.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED_USERS)
public class ViewOrders {

	
	private final static int ORDERS_PER_PAGE = 10;
	
	private int startIndex;
	
	private Block<Order> orderBlock;
	
	@Property
	private Order order;
		
	@SessionState(create=false)
    private UserSession userSession;
	
	@Inject
    private OrderService orderService;
	
	@InjectComponent
	private Zone tableZone;

	@Inject
	private Request request;
	
	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;
	
	@Inject
	private Locale locale;

	
	public Format getDateFormat (){
		return DateFormat.getDateInstance(DateFormat.SHORT, locale);
	}
		
	public List<Order> getOrders (){
		return orderBlock.getList();
	}
	
	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	
	public Object[] getPreviousLinkContext() {		
		if (startIndex-ORDERS_PER_PAGE >= 0) {
			return new Object[] {startIndex-ORDERS_PER_PAGE};
		} else {
			return null;
		}
		
	}
	
	public Object[] getNextLinkContext() {		
		if (orderBlock.getExistMore()) {
			return new Object[] {startIndex+ORDERS_PER_PAGE};
		} else {
			return null;
		}		
	}
	
	
	void onNext(int startIndex) throws InstanceNotFoundException {
		onActivate(startIndex);
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender("tableZone", tableZone);
		}
	}
	
	void onPrevious(int startIndex) throws InstanceNotFoundException {
		onActivate(startIndex);
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender("tableZone", tableZone);
		}
	}	
	
	Object[] onPassivate() {
		return new Object[] {startIndex};
	}
	
	void onActivate(int startIndex) throws InstanceNotFoundException {
		this.startIndex = startIndex;
		orderBlock = orderService.findOrdersByUser(userSession.getUserProfileId(), startIndex, ORDERS_PER_PAGE);
	}
}
