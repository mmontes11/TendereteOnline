package es.udc.pa.pa013.practicapa.model.productservice;

import java.util.List;

import es.udc.pa.pa013.practicapa.model.category.Category;
import es.udc.pa.pa013.practicapa.model.product.Product;
import es.udc.pa.pa013.practicapa.model.util.Block;
import es.udc.pojo.modelutil.exceptions.DuplicateInstanceException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public interface ProductService {

	public Product findProductById(Long productId)
			throws InstanceNotFoundException;

	public Block<Product> findProductsByKeywordsCategory(String keywords,
			Long categoryId, int startIndex, int count);
	
	public void addProduct (Product p) throws DuplicateInstanceException;
	
	public void removeProductById (Long productId) throws InstanceNotFoundException;
	
	public void modifyProduct (Long productId,Category category,String name,float price,String imageURL) throws InstanceNotFoundException, DuplicateInstanceException;
	
	public List<Product> getTopN (int N);
	
	public Category findCategoryById (Long categoryId) throws InstanceNotFoundException;
	
	public void addCategory (Category category) throws DuplicateInstanceException;
	
	public void removeCategoryById (Long categoryId) throws InstanceNotFoundException;
	
	public void modifyCategory (Long categoryId,String name) throws InstanceNotFoundException,DuplicateInstanceException;
	
	public Category getCategoryById (Long categoryId) throws InstanceNotFoundException;

	public List<Category> getCategories();
	
	public Category findCategoryByName (String name) throws InstanceNotFoundException;
}