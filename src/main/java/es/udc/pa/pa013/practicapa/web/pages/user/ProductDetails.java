package es.udc.pa.pa013.practicapa.web.pages.user;

import java.text.Format;
import java.text.NumberFormat;
import java.util.Locale;

import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.pa.pa013.practicapa.model.category.Category;
import es.udc.pa.pa013.practicapa.model.product.Product;
import es.udc.pa.pa013.practicapa.model.productservice.ProductService;
import es.udc.pa.pa013.practicapa.web.services.AuthenticationPolicy;
import es.udc.pa.pa013.practicapa.web.services.AuthenticationPolicyType;
import es.udc.pa.pa013.practicapa.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public class ProductDetails {
	
	@Property
	@SessionState(create = false)
	private UserSession userSession;
	
	@Property
	private Long productId;
	
	@Property
	private Category category;
	
	@Property
	private String name;
	
	@Property
	private float price;
	
	@Property
	private String imageURL;
	
	@Inject
	private ProductService productService;
	
	@InjectPage
	private NotFound notFound;
	
	@Inject
	private Locale locale;
	
	public Format getNumberFormat (){
		return NumberFormat.getInstance(locale);
	}
	
	@AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED_USERS)
	Object onActionFromAddCart(Long productId) {
		try {
			userSession.getCart().add(productId, 1);
		} catch (InstanceNotFoundException e) {
			notFound.setID(productId);
			return notFound;
		}
		return ViewCart.class;
	}
	
	Object[] onPasivate (){
		return new Object[] {productId};
	}
	
	Object onActivate (Long productId) {
		Product p = null;
		try {
			p = productService.findProductById(productId);
		} catch (InstanceNotFoundException e) {
			return NotFound.class;
		}
		this.productId = productId;
		this.category = p.getCategory();
		this.name = p.getName();
		this.price = p.getPrice();
		this.imageURL = p.getImageURL();
		
		return null;
	}
}
