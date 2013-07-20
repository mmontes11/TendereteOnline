package es.udc.pa.pa013.practicapa.web.pages.user;

import java.util.List;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

import es.udc.pa.pa013.practicapa.model.product.Product;
import es.udc.pa.pa013.practicapa.model.productservice.ProductService;
import es.udc.pa.pa013.practicapa.model.util.Block;
import es.udc.pa.pa013.practicapa.web.services.AuthenticationPolicy;
import es.udc.pa.pa013.practicapa.web.services.AuthenticationPolicyType;
import es.udc.pa.pa013.practicapa.web.util.FindCategoriesConversor;
import es.udc.pa.pa013.practicapa.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public class FoundProducts {

	private final static int PRODUCTS_PER_PAGE = 10;

	private String keywords;
	private Long category;
	private int startIndex;
	private Block<Product> productBlock;
	private Product product;

	@Inject
	private ProductService productService;

	@Property
	@SessionState(create = false)
	private UserSession userSession;

	@InjectPage
	private NotFound notFound;

	@Property
	private String avaliableCategories = FindCategoriesConversor
			.convertCategories(productService.getCategories());

	@InjectComponent
	private Zone tableZone;

	@Inject
	private Request request;

	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;

	public Long getCategory() {
		return category;
	}

	public void setCategory(Long category) {
		this.category = category;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public List<Product> getProducts() {
		return productBlock.getList();
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Object[] getPreviousLinkContext() {
		if (startIndex - PRODUCTS_PER_PAGE >= 0) {
			return new Object[] { keywords, category,
					startIndex - PRODUCTS_PER_PAGE };
		} else {
			return null;
		}

	}

	public Object[] getNextLinkContext() {
		if (productBlock.getExistMore()) {
			return new Object[] { keywords, category,
					startIndex + PRODUCTS_PER_PAGE };
		} else {
			return null;
		}
	}

	Object[] onPassivate() {
		return new Object[] { keywords, category, startIndex };
	}

	void onActivate(String keywords, Long category, int startIndex) {
		processPage(keywords, category, startIndex);
	}

	void onNext(String keywords, Long category, int startIndex) {
		processPage(keywords, category, startIndex);
		renderAjax();
	}

	void onPrevious(String keywords, Long category, int startIndex) {
		processPage(keywords, category, startIndex);
		renderAjax();
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

	void onSuccessFromSearch() {
		processPage(keywords, category, 0);
		renderAjax();
	}
	
	private void renderAjax(){
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender("tableZone", tableZone);
		}
	}
	
	private void processPage(String keywords, Long category, int startIndex) {
		this.keywords = keywords;
		this.category = category;
		this.startIndex = startIndex;
		productBlock = productService.findProductsByKeywordsCategory(keywords,
				category, startIndex, PRODUCTS_PER_PAGE);
	}
}
