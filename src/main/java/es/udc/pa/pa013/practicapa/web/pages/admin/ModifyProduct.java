package es.udc.pa.pa013.practicapa.web.pages.admin;



import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;


import es.udc.pa.pa013.practicapa.model.productservice.ProductService;
import es.udc.pa.pa013.practicapa.web.services.AuthenticationPolicy;
import es.udc.pa.pa013.practicapa.web.services.AuthenticationPolicyType;
import es.udc.pa.pa013.practicapa.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED_USERS)
public class ModifyProduct {
	
	@SessionState(create=false)
    private UserSession userSession;
	
	@Property
	private Long productId;
	
	@Component
	private Form modifyProductForm;
	
	@Inject
    private Messages messages;
	
	@Component(id = "productId")
    private TextField productIdField;

	@Inject
	private ProductService productService;
	
	@InjectPage
	private ModifyProductDetails modifyProductDetails;
	
	void onValidateFromModifyProductForm() {

		if (!modifyProductForm.isValid()) {
			return;
		}
		try {
			productService.findProductById(productId);
		} catch (InstanceNotFoundException e) {
			modifyProductForm.recordError(productIdField,messages.get("productNotFound"));
		}
	}
	
	Object onSuccess () {
		modifyProductDetails.setProductId(productId);	
		return modifyProductDetails;
	}
	
	Object onActivate(){
		if (!userSession.getFirstName().equals("admin")){
			return NotAuthorized.class;
		}
		return null;
	}

}
