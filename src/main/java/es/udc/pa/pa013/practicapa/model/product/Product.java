package es.udc.pa.pa013.practicapa.model.product;

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
import javax.persistence.Version;

import org.hibernate.annotations.BatchSize;

import es.udc.pa.pa013.practicapa.model.category.Category;

@Entity
@Table(name = "Product")
@BatchSize(size = 10)
public class Product {

	private Long productId;
	private Category category;
	private String name;
	private float price;
	private String imageURL;
	private long numSells;
	private long version;

	public Product() {
	}

	public Product(Category category, String name, float price) {
		/**
		 * NOTE: "productId" *must* be left as "null" since its value is
		 * automatically generated.
		 */
		this.category = category;
		this.name = name;
		this.price = price;
	}
	
	public Product(Category category, String name, float price,String imageURL) {
		this(category,name,price);
		this.imageURL = imageURL;
	}
	
	@Column(name = "productId")
	@SequenceGenerator( // It only takes effect for databases that use
						// identifier
	name = "ProductIdGenerator", // generators (like Oracle)
	sequenceName = "ProductSeq")
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "ProductIdGenerator")
	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "categoryId")
	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "price")
	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}
	
	@Column(name = "imageURL")
	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	
	@Column(name = "numSells")	
	public long getNumSells() {
		return numSells;
	}

	public void setNumSells(long numSells) {
		this.numSells = numSells;
	}

	@Version
	@Column(name = "version")
	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "Product [productId=" + productId + ", category=" + category
				+ ", name=" + name + ", price=" + price + "]";
	}
}
