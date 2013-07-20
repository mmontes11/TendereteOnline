package es.udc.pa.pa013.practicapa.model.orderservice;

@SuppressWarnings("serial")
public class CartLineAmountException extends Exception {

	public CartLineAmountException(int amount) {
		super("Amount should be positive (" + amount + ")");
	}

}
