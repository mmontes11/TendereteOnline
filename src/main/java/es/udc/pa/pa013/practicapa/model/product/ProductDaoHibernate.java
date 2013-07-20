package es.udc.pa.pa013.practicapa.model.product;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@Repository("productDao")
public class ProductDaoHibernate extends GenericDaoHibernate<Product, Long>
		implements ProductDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<Product> findProductsByKeyWordsCategory(String keywords,
			Long categoryId, int startIndex, int count) {
		String[] keywordsList = null;
		if (keywords != null ) {		
				keywordsList = keywords.split(" ");
		}
		
		String queryString = "SELECT p FROM Product p";
		if (categoryId != null) {
			queryString += " WHERE p.category.categoryId = :categoryId";
			if (keywordsList != null) {
				queryString += " AND LOWER(p.name) LIKE :keyword0";
			}
		} else {
			if (keywordsList != null) {
				queryString += " WHERE LOWER(p.name) LIKE :keyword0";
			}
		}
		if (keywordsList != null && keywordsList.length > 1) {
			for (int i = 1; i < keywordsList.length; i++) {
				queryString += " AND LOWER(p.name) LIKE :keyword"
						+ String.valueOf(i);
			}
		}
		queryString += " ORDER BY p.productId";

		Query query = getSession().createQuery(queryString);
		if (categoryId != null) {
			query = query.setParameter("categoryId", categoryId);
		}
		if (keywordsList != null && keywordsList.length > 0) {
			for (int i = 0; i < keywordsList.length; i++) {
				query = query.setParameter("keyword" + String.valueOf(i), "%"
						+ keywordsList[i].toLowerCase() + "%");
			}
		}
		query = query.setFirstResult(startIndex);
		query = query.setMaxResults(count);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Product> findProducts(List<Long> productIds) {
		
		String queryString = "SELECT p FROM Product p WHERE ";
		for (int i = 0; i<productIds.size() - 1 ;i++){
			queryString += "productId = :productId"+String.valueOf(i)+" OR ";
		}
		queryString += "productId = :productId"+String .valueOf(productIds.size() - 1)+" ";
		queryString += "ORDER BY productId";
		
		Query query = getSession().createQuery(queryString);
		int i = 0;
		for (Long productId : productIds){
			query.setParameter("productId"+String.valueOf(i), productId);
			i++;
		}		
		return query.list();
	}

	@Override
	public Product findProductByName(String name) throws InstanceNotFoundException{
		String queryString = "SELECT p FROM Product p WHERE p.name = :name";
		Query query = getSession().createQuery(queryString).setParameter("name", name);
		Product product = (Product) query.uniqueResult();
		if (product == null){
			throw new InstanceNotFoundException(name, Product.class.getName());
		}else{
			return product;
		}
	}

	@Override
	public void incrementSell(Long productId, int amount) {
		String queryString = "UPDATE Product SET numSells = numSells + "+String.valueOf(amount)+" WHERE productId = :productId";
		Query query = getSession().createQuery(queryString).setParameter("productId",productId);
		query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Product> getTopN(int N) {
		String queryString = "SELECT p FROM Product p ORDER BY numSells DESC";
		Query query = getSession().createQuery(queryString).setMaxResults(N);
		return query.list();
	}
	
	

}
