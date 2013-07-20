package es.udc.pa.pa013.practicapa.web.pages.admin;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.pa.pa013.practicapa.model.productservice.ProductService;
import es.udc.pa.pa013.practicapa.web.pages.user.NotFound;
import es.udc.pa.pa013.practicapa.web.services.AuthenticationPolicy;
import es.udc.pa.pa013.practicapa.web.services.AuthenticationPolicyType;
import es.udc.pa.pa013.practicapa.web.util.FindCategoriesConversor;
import es.udc.pa.pa013.practicapa.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;


@AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED_USERS)
public class RemoveCategory {
	
	@SessionState(create=false)
    private UserSession userSession;
	
	@Property
	private Long category;
	
	@Inject
	private ProductService productService;
	
	@Property
	private String avaliableCategories = FindCategoriesConversor
			.convertCategories(productService.getCategories());
	
	
	Object onSuccess (){
		try {
			productService.removeCategoryById(category);
		} catch (InstanceNotFoundException e) {
			return NotFound.class;
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
