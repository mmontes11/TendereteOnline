package es.udc.pa.pa013.practicapa.model.productservice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.pa.pa013.practicapa.model.category.Category;
import es.udc.pa.pa013.practicapa.model.category.CategoryDao;
import es.udc.pa.pa013.practicapa.model.product.Product;
import es.udc.pa.pa013.practicapa.model.product.ProductDao;
import es.udc.pa.pa013.practicapa.model.util.Block;
import es.udc.pojo.modelutil.exceptions.DuplicateInstanceException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@Service("productService")
@Transactional
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductDao productDao;

	@Autowired
	private CategoryDao categoryDao;

	@Override
	public Product findProductById(Long productId)
			throws InstanceNotFoundException {
		return productDao.find(productId);
	}

	@Override
	public Block<Product> findProductsByKeywordsCategory(String keywords,
			Long categoryId, int startIndex, int count) {
		/*
		 * Find count+1 products to determine if there exist more products above
		 * the specified range.
		 */
		List<Product> products = productDao.findProductsByKeyWordsCategory(
				keywords, categoryId, startIndex, count + 1);

		boolean existMoreProducts = products.size() == (count + 1);

		/*
		 * Remove the last product from the returned list if there exist more
		 * product above the specified range.
		 */
		if (existMoreProducts) {
			products.remove(products.size() - 1);
		}

		return new Block<Product>(products, existMoreProducts);
	}
	
	@Override
	public void addProduct(Product p) throws DuplicateInstanceException {
		try {
			productDao.findProductByName(p.getName());
			throw new DuplicateInstanceException(p.getName(), Product.class.getName());
		} catch (InstanceNotFoundException e) {
			productDao.save(p);
		}	
	}
	
	@Override
	public void removeProductById(Long productId) throws InstanceNotFoundException {
		productDao.remove(productId);
	}
	
	@Override
	public void modifyProduct (Long productId,Category category,String name,float price,String imageURL) throws InstanceNotFoundException, DuplicateInstanceException {
		try {
			productDao.findProductByName(name);
			throw new DuplicateInstanceException(name, Product.class.getName());
		} catch (InstanceNotFoundException e) {
			Product oldProduct = productDao.find(productId);
			oldProduct.setCategory(category);
			oldProduct.setName(name);
			oldProduct.setPrice(price);
			oldProduct.setImageURL(imageURL);
		}		
	}
	
	@Override
	public List<Product> getTopN(int N) {
		return productDao.getTopN(N);
	}
	
	@Override
	public Category findCategoryById(Long categoryId)
			throws InstanceNotFoundException {
		return categoryDao.find(categoryId);
	}
	
	@Override
	public void addCategory(Category category) throws DuplicateInstanceException {
		try {
			categoryDao.findCategoryByName(category.getName());
			throw new DuplicateInstanceException(category.getName(), Category.class.getName());
		} catch (InstanceNotFoundException e) {
			categoryDao.save(category);
		}
	}
	
	
	@Override
	public void removeCategoryById(Long categoryId) throws InstanceNotFoundException {
		categoryDao.remove(categoryId);
	}
	
	@Override
	public void modifyCategory(Long categoryId, String name)
			throws DuplicateInstanceException, InstanceNotFoundException {	
		try {
			categoryDao.findCategoryByName(name);
			throw new DuplicateInstanceException(name, Category.class.getName());
		} catch (InstanceNotFoundException e) {
			Category oldCategory = categoryDao.find(categoryId);
			oldCategory.setName(name);
		}		
	}
	
	
	@Override
	public Category getCategoryById(Long categoryId) throws InstanceNotFoundException {
		return categoryDao.find(categoryId);
	}
	
	@Override
	public List<Category> getCategories() {
		return categoryDao.getCategories();
	}

	@Override
	public Category findCategoryByName(String name) throws InstanceNotFoundException {
		return categoryDao.findCategoryByName(name);
	}

}