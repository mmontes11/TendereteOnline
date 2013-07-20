package es.udc.pa.pa013.practicapa.model.orderline;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

import es.udc.pa.pa013.practicapa.model.order.Order;
import es.udc.pa.pa013.practicapa.model.product.Product;

@Entity
@Immutable
@Table(name = "OrderLine")
public class OrderLine {

	private Long orderLineId;
	private Product product;
	private Order order;
	private int amount;
	private float price;

	public OrderLine() {
	}

	public OrderLine(Product product, Order order, int amount, float price) {
		/**
		 * NOTE: "orderId" *must* be left as "null" since its value is
		 * automatically generated.
		 */
		this.product = product;
		this.order = order;
		this.amount = amount;
		this.price = price;
	}

	@Column(name = "orderLineId")
	@SequenceGenerator( // It only takes effect for databases that use
						// identifier
	name = "OrderLineIdGenerator", // generators (like Oracle)
	sequenceName = "OrderLineSeq")
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "OrderLineIdGenerator")
	public Long getOrderLineId() {
		return orderLineId;
	}

	public void setOrderLineId(Long orderLineId) {
		this.orderLineId = orderLineId;
	}

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "productId")
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "orderId")
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	@Column(name = "amount")
	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	@Column(name = "price")
	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "OrderLine [orderLineId=" + orderLineId + ", product=" + product
				+ ", order=" + order + ", amount=" + amount + ", price="
				+ price + "]";
	}

}
