package es.udc.pa.pa013.practicapa.model.category;

import java.util.List;

import es.udc.pojo.modelutil.dao.GenericDao;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public interface CategoryDao extends GenericDao<Category, Long> {

	public List<Category> getCategories();
	
	public Category findCategoryByName(String name) throws InstanceNotFoundException;
}
