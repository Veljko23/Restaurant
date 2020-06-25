package cubes.main.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

@Entity
@Table
public class Tag {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column
	@Size(min = 3, max = 20, message = "min 3, max 20!")
	private String name;
	@ManyToMany
	@JoinTable(name = "Product_Tag", joinColumns = @JoinColumn(name = "id_tag"), inverseJoinColumns = @JoinColumn(name="id_product"))
	private List<Product> products;
	
	@Transient
	private long count;
	
	public Tag() {}
	
	public Tag(String name) {
		super();
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	@Override
	public String toString() {
		return "(" + id + ")" + " " + name;
	}
	
}
