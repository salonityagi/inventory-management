package sl.ms.inventorymanagement.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name = "sl_inv")
public class Inventory {
	
	@Id
	
	@SequenceGenerator(name = "inventseq",sequenceName = "inventory_seq",initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "inventseq")
	@JsonIgnore
	private int inventoryId;
	private int Id;
	//@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date", insertable = false, updatable = false, columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Date date;
	@Lob
	private String request;
	
	@OneToMany(targetEntity = Product.class,cascade = CascadeType.ALL)
	@JoinColumn(name="PRODUCT_ID")
	private Set<Product> products;
	
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getRequest() {
		return request;
	}
	public void setRequest(String request) {
		this.request = request;
	}
	public Set<Product> getProducts() {
		return products;
	}
	public void setProducts(Set<Product> products) {
		this.products = products;
	}
	@JsonIgnore
	public int getInventoryId() {
		return inventoryId;
	}
	public void setInventoryId(int inventoryId) {
		this.inventoryId = inventoryId;
	}
}
