package es.udc.pa.pa013.practicapa.web.pages.admin;



import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;


import es.udc.pa.pa013.practicapa.model.productservice.ProductService;
import es.udc.pa.pa013.practicapa.web.services.AuthenticationPolicy;
import es.udc.pa.pa013.practicapa.web.services.AuthenticationPolicyType;
import es.udc.pa.pa013.practicapa.web.util.FindCategoriesConversor;
import es.udc.pa.pa013.practicapa.web.util.UserSession;

@AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED_USERS)
public class ModifyCategory {
	
	@SessionState(create=false)
    private UserSession userSession;
	
	@Property
	private Long category;
	
	@Inject
	private ProductService productService;
	
	@Property
	private String avaliableCategories = FindCategoriesConversor
			.convertCategories(productService.getCategories());
	
	@InjectPage
	private ModifyCategoryDetails modifyCategoryDetails;
	
	
	Object onSuccess (){
		modifyCategoryDetails.setCategoryId(category);
		return modifyCategoryDetails;
	}
	
	
	Object onActivate(){
		if (!userSession.getFirstName().equals("admin")){
			return NotAuthorized.class;
		}
		return null;
	}


}
