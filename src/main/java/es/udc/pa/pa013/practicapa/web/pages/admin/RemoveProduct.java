package es.udc.pa.pa013.practicapa.web.pages.admin;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.pa.pa013.practicapa.model.productservice.ProductService;
import es.udc.pa.pa013.practicapa.web.pages.user.NotFound;
import es.udc.pa.pa013.practicapa.web.services.AuthenticationPolicy;
import es.udc.pa.pa013.practicapa.web.services.AuthenticationPolicyType;
import es.udc.pa.pa013.practicapa.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;


@AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED_USERS)
public class RemoveProduct {
	
	@SessionState(create=false)
    private UserSession userSession;
	
	@Property
	private Long productId;
	
	@Inject
	private ProductService productService;
	
	@Component
    private Form removeProductForm;
	
	@Inject
    private Messages messages;
	
	@Component(id = "productId")
    private TextField productIdField;
	
	
	void onValidateFromRemoveProductForm(){
		if (!removeProductForm.isValid()) {
			return;
		}		
		try {
			productService.findProductById(productId);
		} catch (InstanceNotFoundException e) {
			removeProductForm.recordError(productIdField,messages.get("productNotFound"));
		}		
	}
	
	Object onSuccess (){
		try {
			productService.removeProductById(productId);
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
