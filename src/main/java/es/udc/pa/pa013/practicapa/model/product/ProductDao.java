package es.udc.pa.pa013.practicapa.model.product;

import java.util.List;

import es.udc.pojo.modelutil.dao.GenericDao;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public interface ProductDao extends GenericDao<Product, Long> {

	/**
	 * Returns a list of products that match with the keywords
	 * 
	 * @param keywords
	 * @param startIndex
	 *            the index (starting from 0) of the first product to return
	 * @param count
	 *            the maximum number of products to return
	 * @return the list of products
	 */
	public List<Product> findProductsByKeyWordsCategory(String keywords,
			Long categoryId, int startIndex, int count);
	
	public List<Product> findProducts (List<Long> productIds);
	
	public Product findProductByName (String name) throws InstanceNotFoundException;
	
	public void incrementSell (Long productId,int amount);
	
	public List<Product> getTopN (int N);

}
