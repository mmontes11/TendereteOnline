package es.udc.pa.pa013.practicapa.model.category;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;

@Entity
@Table(name = "Category")
@BatchSize(size = 10)
public class Category {

	private Long categoryId;
	private String name;

	public Category() {
	}

	public Category(String name) {
		/**
		 * NOTE: "categoryId" *must* be left as "null" since its value is
		 * automatically generated.
		 */
		this.name = name;
	}

	@Column(name = "categoryId")
	@SequenceGenerator( // It only takes effect for databases that use
						// identifier
	name = "CategoryIdGenerator", // generators (like Oracle)
	sequenceName = "CategorySeq")
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "CategoryIdGenerator")
	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Category [categoryId=" + categoryId + ", name=" + name + "]";
	}

}
