package es.udc.pa.pa013.practicapa.web.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.pa.pa013.practicapa.model.product.Product;
import es.udc.pa.pa013.practicapa.model.productservice.ProductService;
import es.udc.pa.pa013.practicapa.web.pages.user.FoundProducts;
import es.udc.pa.pa013.practicapa.web.util.FindCategoriesConversor;

public class Index {

	@Property
	private String keywords;

	@Property
	private Long category;

	@Inject
	private ProductService productService;

	@Property
	private String avaliableCategories = FindCategoriesConversor
			.convertCategories(productService.getCategories());

	@Property
	private Product activeProductCarousel;

	@Property
	private List<Product> restProductsCarousel;

	@Property
	private Product product;

	@InjectPage
	private FoundProducts foundProducts;
	
	private final int NUMBER_OF_PRODUCTS_IN_CAROUSEL = 10;

	Object onSuccess() {
		foundProducts.setKeywords(keywords);
		foundProducts.setCategory(category);
		return foundProducts;
	}
	
	Object onActivate (){
		avaliableCategories = FindCategoriesConversor.convertCategories(productService.getCategories());
		List<Product> prouductsCarousel;
		prouductsCarousel = productService.getTopN(NUMBER_OF_PRODUCTS_IN_CAROUSEL);
		activeProductCarousel = prouductsCarousel.get(0);
		restProductsCarousel = getRestOfCarousel(prouductsCarousel);
		return null;
	}
	

	private static List<Product> getRestOfCarousel(List<Product> listProducts) {
		ArrayList<Product> rest = new ArrayList<>();
		for (int i = 0; i < listProducts.size(); i++) {
			if (i == 0) {
				continue;
			}
			rest.add(listProducts.get(i));
		}
		return rest;
	}

}
