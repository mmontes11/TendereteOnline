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
import es.udc.pa.pa013.practicapa.web.pages.user.NotFound;
import es.udc.pa.pa013.practicapa.web.services.AuthenticationPolicy;
import es.udc.pa.pa013.practicapa.web.services.AuthenticationPolicyType;
import es.udc.pa.pa013.practicapa.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.DuplicateInstanceException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;


@AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED_USERS)
public class ModifyCategoryDetails {
	
	@SessionState(create=false)
    private UserSession userSession;
	
	@Inject
	private ProductService productService;
	
	@Property
	private String name;
	
	private Long categoryId;
	
	@Component(id = "name")
    private TextField nameField;
	
	@Component(id = "modifyCategoryForm")
	private Form modifyCategoryForm;
	
	@Inject
    private Messages messages;
	

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	
	Object onPrepareForRender() {
        Category category;
		try {
			category = productService.findCategoryById(categoryId);
		} catch (InstanceNotFoundException e) {
			return NotFound.class;
		}
        name = category.getName();
        return null;
    }

	Object[] onPassivate (){
		return new Object[] {categoryId};
	}
	
	Object onActivate(Long categoryId){
		if (!userSession.getFirstName().equals("admin")){
			return NotAuthorized.class;
		}
		this.categoryId = categoryId;
		return null;
	}
	
	Object onSuccess (){
		try {
			productService.modifyCategory(categoryId, name);
		} catch (InstanceNotFoundException e) {
			return NotFound.class;
		} catch (DuplicateInstanceException e){
			modifyCategoryForm.recordError(nameField,messages.get("duplicatedCategoryName"));
			return null;
		}
		return SuccessfulOperation.class;
	}
	
	
	

}
