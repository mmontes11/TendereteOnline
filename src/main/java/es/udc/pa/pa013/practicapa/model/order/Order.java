package es.udc.pa.pa013.practicapa.model.order;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Immutable;

import es.udc.pa.pa013.practicapa.model.orderline.OrderLine;
import es.udc.pa.pa013.practicapa.model.userprofile.UserProfile;

@Entity
@Immutable
@Table(name = "Ord")
public class Order {

	private Long orderId;
	private UserProfile userProfile;
	private List<OrderLine> orderLines = new ArrayList<OrderLine>();
	private Calendar date;

	public Order() {
	}

	public Order(UserProfile userProfile, Calendar date) {
		/**
		 * NOTE: "orderId" *must* be left as "null" since its value is
		 * automatically generated.
		 */
		this.userProfile = userProfile;
		this.date = date;
	}

	public Order(UserProfile userProfile, List<OrderLine> orderLines,
			Calendar date) {
		this(userProfile, date);
		this.orderLines = orderLines;
	}

	@Column(name = "orderId")
	@SequenceGenerator( // It only takes effect for databases that use
						// identifier
	name = "OrderIdGenerator", // generators (like Oracle)
	sequenceName = "OrderSeq")
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "OrderIdGenerator")
	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "userProfileId")
	public UserProfile getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}

	@OneToMany(mappedBy = "order")
	public List<OrderLine> getOrderLines() {
		return orderLines;
	}

	public void setOrderLines(List<OrderLine> orderLines) {
		this.orderLines = orderLines;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date")
	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}

	@Transient
	public float getPrice() {
		float price = 0;
		for (OrderLine o : orderLines) {
			price += o.getAmount() * o.getPrice();
		}
		return price;
	}

	@Override
	public String toString() {
		return "Order [orderId=" + orderId + ", userProfile=" + userProfile
				+ ", orderLines=" + orderLines + ", date=" + date + "]";
	}

}
