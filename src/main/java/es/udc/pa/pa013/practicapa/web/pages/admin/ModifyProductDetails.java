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
import es.udc.pa.pa013.practicapa.web.pages.user.NotFound;
import es.udc.pa.pa013.practicapa.web.services.AuthenticationPolicy;
import es.udc.pa.pa013.practicapa.web.services.AuthenticationPolicyType;
import es.udc.pa.pa013.practicapa.web.util.FindCategoriesConversor;
import es.udc.pa.pa013.practicapa.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.DuplicateInstanceException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;


@AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED_USERS)
public class ModifyProductDetails {
	
	@SessionState(create=false)
    private UserSession userSession;
	
	@Inject
	private ProductService productService;
	
	private Long productId;
	
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
    private Form modifyProductForm;
	
	@Inject
    private Messages messages;
	
	@Component(id = "name")
    private TextField nameField;
	
	@Component(id = "price")
    private TextField priceField;
	
	@Component(id = "imageURL")
    private TextField imageURLField;
		
		
	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}
	
	void onPrepareForRender() throws InstanceNotFoundException {
        Product product = productService.findProductById(productId);
        
        category = product.getCategory().getCategoryId();
        name = product.getName();
        price = product.getPrice();
        imageURL = product.getImageURL();

    }
	
	void onValidateFromModifyProductForm (){
		if (!modifyProductForm.isValid()) {
			return;
		}		
		try {
			new URL(imageURL);
		} catch (MalformedURLException e) {
			modifyProductForm.recordError(imageURLField, messages.get("incorrectURL"));
		}
	}
	
	Object onSuccess (){
		Category productCategory;
		try {
			productCategory = productService.getCategoryById(category);
		} catch (InstanceNotFoundException e) {
			productCategory = null;
		}
		try {
			productService.modifyProduct(productId,productCategory,name,price,imageURL);
		} catch (InstanceNotFoundException e) {
			return NotFound.class;
		} catch (DuplicateInstanceException e) {
			modifyProductForm.recordError(nameField,messages.get("duplicatedProductName"));
			return null;
		} 		
		return SuccessfulOperation.class;
	}
	

	Object[] onPassivate (){
		return new Object[] {productId};
	}
	
	Object onActivate(Long productId){
		if (!userSession.getFirstName().equals("admin")){
			return NotAuthorized.class;
		}
		this.productId = productId;
		return null;
	}
	
	
	

}
