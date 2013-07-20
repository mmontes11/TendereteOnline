package es.udc.pa.pa013.practicapa.model.orderservice;

import java.util.ArrayList;
import java.util.List;

import es.udc.pa.pa013.practicapa.model.product.Product;
import es.udc.pa.pa013.practicapa.model.productservice.ProductService;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public class Cart {

	private List<CartLine> cartLines;

	private ProductService productService;

	public Cart(ProductService productService) {
		this.cartLines = new ArrayList<CartLine>();
		this.productService = productService;
	}

	public void add(Long productId, int amount)
			throws InstanceNotFoundException {
		boolean found = false;
		Product newProduct = productService.findProductById(productId);
		CartLine cartLine = new CartLine(newProduct.getProductId(),
				newProduct.getName(), newProduct.getPrice(), amount);
		for (CartLine cl : cartLines) {
			if (cl.getProductId().equals(cartLine.getProductId())) {
				cl.setAmount(cl.getAmount() + cartLine.getAmount());
				found = true;
				break;
			}
		}
		if (!found) {
			cartLines.add(cartLine);
		}
	}
	
	public void remove (Long productId, int amount) throws InstanceNotFoundException, CartLineAmountException{
		modify(productId,getCartline(productId).getAmount() - amount);
	}
	

	public void modify(Long productId, int amount)
			throws InstanceNotFoundException, CartLineAmountException {
		boolean found = false;
		for (CartLine cl : cartLines) {
			if (cl.getProductId().equals(productId)) {
				if (amount > 0) {
					cl.setAmount(amount);
				} else {
					if (amount == 0) {
						cartLines.remove(cl);
					} else {
						throw new CartLineAmountException(amount);
					}
				}
				found = true;
				break;
			}
		}
		if (!found) {
			throw new InstanceNotFoundException(productId,
					Product.class.getName());
		}
	}

	public void removeLine(Long productId) throws InstanceNotFoundException {
		boolean found = false;
		for (CartLine cl : cartLines) {
			if (cl.getProductId().equals(productId)) {
				cartLines.remove(cl);
				found = true;
				break;
			}
		}
		if (!found) {
			throw new InstanceNotFoundException(productId,
					Product.class.getName());
		}
	}

	public void clear() {
		cartLines.clear();
	}

	public List<CartLine> getCartLines() {
		return cartLines;
	}

	public float getTotalPrice() {
		int amount = 0;
		for (CartLine cl : cartLines) {
			amount += (cl.getPrice() * cl.getAmount());
		}
		return amount;
	}
	
	public CartLine getCartline (Long productId) throws InstanceNotFoundException{
		for (CartLine cl : cartLines) {
			if (cl.getProductId().equals(productId)) return cl;
		}
		throw new InstanceNotFoundException(productId,Product.class.getName());
	}

	public ProductService getProductService() {
		return productService;
	}

	public void setProductService(ProductService productService) {
		this.productService = productService;
	}

	public int getTotalItems() {
		int items = 0;
		for (CartLine cl : cartLines) {
			items += cl.getAmount();
		}
		return items;
	}
	
}