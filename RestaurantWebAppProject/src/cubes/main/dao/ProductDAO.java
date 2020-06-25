package cubes.main.dao;

import java.util.List;

import cubes.main.entity.Product;

public interface ProductDAO {
	
	List<Product> getProducts();
	
	public void saveProduct(Product product);
	
	public Product updateProduct(int id);
	
	public Product getProduct(int id);
	
	public void deleteProduct(int id);
	
	public List<Product> getProductListByCategory(int id);
	
	public List<Product> getProductListByTag(int id);
	
	public Product getProductListWithTag(int id);
	
	public List<Product> getProductList(int orderBy);
	
	
	
	public List<Product> related(int id, int catid);

}
