package es.udc.pa.pa013.practicapa.web.util;

import java.util.List;

import es.udc.pa.pa013.practicapa.model.category.Category;

public class FindCategoriesConversor {
	
	public static String convertCategories (List<Category> listCategories){
		String stringCategories = "";
		for (int i = 0 ; i<listCategories.size() ; i++ ){
			stringCategories += listCategories.get(i).getCategoryId() + "=" + listCategories.get(i).getName();
			if (i != (listCategories.size() - 1) ){
				stringCategories += " , ";
			}
		}
		return stringCategories;
	}
}
