package es.udc.pa.pa013.practicapa.web.pages.admin;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.pa.pa013.practicapa.model.category.Category;
import es.udc.pa.pa013.practicapa.model.product.Product;
import es.udc.pa.pa013.practicapa.model.productservice.ProductService;
import es.udc.pa.pa013.practicapa.web.services.AuthenticationPolicy;
import es.udc.pa.pa013.practicapa.web.services.AuthenticationPolicyType;
import es.udc.pa.pa013.practicapa.web.util.FindCategoriesConversor;
import es.udc.pa.pa013.practicapa.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.DuplicateInstanceException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;


@AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED_USERS)
public class NewProduct {
	
	@SessionState(create=false)
    private UserSession userSession;
	
	@Inject
	private ProductService productService;
	
	@Property
	private Long category;
	
	@Property
	private String name;
	
	@Property
	private float price;
	
	@Property
	private String imageURL;
	
	@Property
	private String avaliableCategories = FindCategoriesConversor
			.convertCategories(productService.getCategories());
	
	@Component
    private Form newProductForm;
	
	@Inject
    private Messages messages;
	
	@Component(id = "name")
    private TextField nameField;
	
	@Component(id = "price")
    private TextField priceField;
	
	@Component(id = "imageURL")
    private TextField imageURLField;
	
	void onValidateFromNewProductForm() {

		if (!newProductForm.isValid()) {
			return;
		}		
		try {
			new URL(imageURL);
		} catch (MalformedURLException e) {
			newProductForm.recordError(imageURLField, messages.get("incorrectURL"));
		}
	}
	
	Object onSuccess (){
		Category productCategory;
		try {
			productCategory = productService.getCategoryById(category);
		} catch (InstanceNotFoundException e) {
			productCategory = null;
		}
		Product newProduct = new Product(productCategory,name,price,imageURL);
		try {
			productService.addProduct(newProduct);
		} catch (DuplicateInstanceException e) {
			newProductForm.recordError(nameField, messages.get("duplicatedProductName"));		
		}	
		return SuccessfulOperation.class;
	}
	
	
	Object onActivate(){
		if (!userSession.getFirstName().equals("admin")){
			return NotAuthorized.class;
		}
		return null;
	}

}
