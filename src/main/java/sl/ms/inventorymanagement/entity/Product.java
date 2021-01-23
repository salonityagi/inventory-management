package sl.ms.inventorymanagement.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name="sl_product")
public class Product {
	
	@Id
	@SequenceGenerator(name = "productseq",sequenceName = "product_seq",initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "productseq")
	@JsonIgnore
	private int prodId;
	private int Id;
	private String name;
	private Double price;
	private int quantity;
	
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	@JsonIgnore
	public int getProdId() {
		return prodId;
	}
	public void setProdId(int prodId) {
		this.prodId = prodId;
	}

	public boolean equals(Product obj) {
		return this.Id==(obj.getId());
	}

	@Override
	public String toString() {
		return "Product{" +
				"Id=" + Id +
				'}';
	}
}
