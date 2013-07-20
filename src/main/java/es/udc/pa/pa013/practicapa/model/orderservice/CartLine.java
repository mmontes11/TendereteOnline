package es.udc.pa.pa013.practicapa.model.orderservice;

import java.util.ArrayList;
import java.util.List;

public class CartLine {

	private Long productId;
	private String name;
	private float price;
	private int amount;

	public CartLine(Long productId, String name, float price, int amount) {
		this.productId = productId;
		this.name = name;
		this.price = price;
		this.amount = amount;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public static List<Long> getProductIds (List<CartLine> cartLines) {
		ArrayList<Long> productIds = new ArrayList<Long>();
		for (CartLine cl : cartLines){
			productIds.add(cl.getProductId());
		}		
		return productIds;
	}

}