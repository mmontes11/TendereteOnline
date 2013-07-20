package es.udc.pa.pa013.practicapa.web.pages.admin;


import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.pa.pa013.practicapa.model.category.Category;
import es.udc.pa.pa013.practicapa.model.productservice.ProductService;
import es.udc.pa.pa013.practicapa.web.services.AuthenticationPolicy;
import es.udc.pa.pa013.practicapa.web.services.AuthenticationPolicyType;
import es.udc.pa.pa013.practicapa.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.DuplicateInstanceException;


@AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED_USERS)
public class NewCategory {
	
	@SessionState(create=false)
    private UserSession userSession;
	
	@Property
	private String name;
	
	@Inject
	private ProductService productService;

	@Component
    private Form newCategoryForm;
	
	@Inject
    private Messages messages;
	
	@Component(id = "name")
    private TextField nameField;
	
	Object onSuccess (){
		Category newCategory = new Category(name);
		try {
			productService.addCategory(newCategory);
		} catch (DuplicateInstanceException e) {
			newCategoryForm.recordError(nameField, messages.get("duplicatedCategoryName"));		
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
