package es.udc.pa.pa013.practicapa.web.util;

import es.udc.pa.pa013.practicapa.model.orderservice.Cart;
import es.udc.pa.pa013.practicapa.model.productservice.ProductService;


public class UserSession {

	private Long userProfileId;
	private String firstName;
	private ProductService productService;
	private Cart cart;

	public Long getUserProfileId() {
		return userProfileId;
	}

	public void setUserProfileId(Long userProfileId) {
		this.userProfileId = userProfileId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public ProductService getProductService() {
		return productService;
	}

	public void setProductService(ProductService productService) {
		this.productService = productService;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}
}
