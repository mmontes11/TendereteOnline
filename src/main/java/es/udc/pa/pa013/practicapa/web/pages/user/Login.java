package es.udc.pa.pa013.practicapa.web.pages.user;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Cookies;

import es.udc.pa.pa013.practicapa.model.orderservice.Cart;
import es.udc.pa.pa013.practicapa.model.productservice.ProductService;
import es.udc.pa.pa013.practicapa.model.userprofile.UserProfile;
import es.udc.pa.pa013.practicapa.model.userservice.IncorrectPasswordException;
import es.udc.pa.pa013.practicapa.model.userservice.UserService;
import es.udc.pa.pa013.practicapa.web.pages.Index;
import es.udc.pa.pa013.practicapa.web.pages.admin.ViewAdminOperations;
import es.udc.pa.pa013.practicapa.web.services.AuthenticationPolicy;
import es.udc.pa.pa013.practicapa.web.services.AuthenticationPolicyType;
import es.udc.pa.pa013.practicapa.web.util.CookiesManager;
import es.udc.pa.pa013.practicapa.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@AuthenticationPolicy(AuthenticationPolicyType.NON_AUTHENTICATED_USERS)
public class Login {

    @Property
    private String loginName;

    @Property
    private String password;

    @Property
    private boolean rememberMyPassword;

    @SessionState(create=false)
    private UserSession userSession;

    @Inject
    private Cookies cookies;

    @Component
    private Form loginForm;

    @Inject
    private Messages messages;

    @Inject
    private UserService userService;

    private UserProfile userProfile = null;
    
    @Inject
    private ProductService productService;


    void onValidateFromLoginForm() {

        if (!loginForm.isValid()) {
            return;
        }

        try {
            userProfile = userService.login(loginName, password, false);
        } catch (InstanceNotFoundException e) {
            loginForm.recordError(messages.get("error-authenticationFailed"));
        } catch (IncorrectPasswordException e) {
            loginForm.recordError(messages.get("error-authenticationFailed"));
        }

    }

    Object onSuccess() {

    	userSession = new UserSession();
        userSession.setUserProfileId(userProfile.getUserProfileId());
        userSession.setFirstName(userProfile.getFirstName());
        userSession.setProductService(productService);
        userSession.setCart(new Cart(productService));
        
        if (rememberMyPassword) {
            CookiesManager.leaveCookies(cookies, loginName, userProfile
                    .getEncryptedPassword());
        }
        
        if (userSession.getFirstName().equals("admin")){
        	return ViewAdminOperations.class;
        } else {
        	return Index.class;
        }      
    }

}
