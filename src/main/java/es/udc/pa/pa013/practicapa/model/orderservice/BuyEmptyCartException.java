package es.udc.pa.pa013.practicapa.model.orderservice;

@SuppressWarnings("serial")
public class BuyEmptyCartException extends Exception {

	public BuyEmptyCartException (){
		super("There are no products to buy in the cart");
	}
	
}
