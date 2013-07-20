package es.udc.pa.pa013.practicapa.web.pages.user;

import java.util.ArrayList;
import java.util.List;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

import es.udc.pa.pa013.practicapa.model.orderservice.BuyEmptyCartException;
import es.udc.pa.pa013.practicapa.model.orderservice.CartLine;
import es.udc.pa.pa013.practicapa.model.orderservice.CartLineAmountException;
import es.udc.pa.pa013.practicapa.model.orderservice.OrderService;
import es.udc.pa.pa013.practicapa.web.services.AuthenticationPolicy;
import es.udc.pa.pa013.practicapa.web.services.AuthenticationPolicyType;
import es.udc.pa.pa013.practicapa.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED_USERS)
public class ViewCart {

	@SessionState(create = false)
	private UserSession userSession;

	@Property
	private CartLine cartLine;

	@Inject
	private OrderService orderService;

	@InjectPage
	private OrderDetails orderDetails;

	@InjectComponent
	private Zone tableZone;

	@InjectComponent
	private Zone priceZone;

	@Inject
	private Request request;

	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;

	void onAddOne(Long productId) throws InstanceNotFoundException {
		userSession.getCart().add(productId, 1);

		if (request.isXHR()) {
			ajaxResponseRenderer.addRender("tableZone", tableZone).addRender(
					"priceZone", priceZone);
		}
	}

	void onRemoveOne(Long productId) throws InstanceNotFoundException,
			CartLineAmountException {
		userSession.getCart().remove(productId, 1);

		if (request.isXHR()) {
			ajaxResponseRenderer.addRender("tableZone", tableZone).addRender(
					"priceZone", priceZone);
		}
	}

	void onRemoveLine(Long productId) throws InstanceNotFoundException {
		userSession.getCart().removeLine(productId);

		if (request.isXHR()) {
			ajaxResponseRenderer.addRender("tableZone", tableZone).addRender(
					"priceZone", priceZone);
		}
	}

	void onSuccess() {
		List<CartLine> toDelete = new ArrayList<>();
		for (CartLine cartLine : userSession.getCart().getCartLines()) {
			if (cartLine.getAmount() < 1) {
				toDelete.add(cartLine);
			}
		}
		userSession.getCart().getCartLines().removeAll(toDelete);

		if (request.isXHR()) {
			ajaxResponseRenderer.addRender("tableZone", tableZone).addRender(
					"priceZone", priceZone);
		}
	}

	Object onActionFromBuy() {
		List<CartLine> toDelete = new ArrayList<>();
		for (CartLine cartLine : userSession.getCart().getCartLines()) {
			if (cartLine.getAmount() < 1) {
				toDelete.add(cartLine);
			}
		}
		userSession.getCart().getCartLines().removeAll(toDelete);
		try {
			Long id = orderService.buy(userSession.getCart(),
					userSession.getUserProfileId()).getOrderId();
			orderDetails.setOrderId(id);
			return orderDetails;
		} catch (InstanceNotFoundException e) {
			return BuyNonExistingProduct.class;
		} catch (BuyEmptyCartException e) {
			return BuyEmptyCart.class;
		}
	}

	public List<CartLine> getCartLines() {
		return userSession.getCart().getCartLines();
	}

	public float getTotalPrice() {
		return userSession.getCart().getTotalPrice();
	}

}
