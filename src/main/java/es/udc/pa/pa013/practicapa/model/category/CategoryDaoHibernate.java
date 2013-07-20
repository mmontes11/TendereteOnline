package es.udc.pa.pa013.practicapa.model.category;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@Repository("categoryDao")
public class CategoryDaoHibernate extends GenericDaoHibernate<Category, Long>
		implements CategoryDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<Category> getCategories() {
		return getSession().createQuery("SELECT c FROM Category c").list();
	}

	@Override
	public Category findCategoryByName(String name) throws InstanceNotFoundException {
		String queryString = "SELECT c FROM Category c WHERE c.name = :categoryName";
		Query query = getSession().createQuery(queryString).setParameter("categoryName", name);
		Category category = (Category) query.uniqueResult();
		if (category == null){
			throw new InstanceNotFoundException(name,Category.class.getName());
		} else {
			return category;
		}
	}
}
