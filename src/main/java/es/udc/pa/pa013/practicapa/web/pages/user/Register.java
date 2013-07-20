package es.udc.pa.pa013.practicapa.web.pages.user;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.PasswordField;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.pa.pa013.practicapa.model.orderservice.Cart;
import es.udc.pa.pa013.practicapa.model.productservice.ProductService;
import es.udc.pa.pa013.practicapa.model.userprofile.UserProfile;
import es.udc.pa.pa013.practicapa.model.userservice.UserProfileDetails;
import es.udc.pa.pa013.practicapa.model.userservice.UserService;
import es.udc.pa.pa013.practicapa.web.pages.Index;
import es.udc.pa.pa013.practicapa.web.services.AuthenticationPolicy;
import es.udc.pa.pa013.practicapa.web.services.AuthenticationPolicyType;
import es.udc.pa.pa013.practicapa.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.DuplicateInstanceException;

@AuthenticationPolicy(AuthenticationPolicyType.NON_AUTHENTICATED_USERS)
public class Register {

    @Property
    private String loginName;

    @Property
    private String password;

    @Property
    private String retypePassword;

    @Property
    private String firstName;

    @Property
    private String lastName;

    @Property
    private String email;
    
    @Property
    private String street;
    
    @Property
    private int number;
    
    @Property
    private String door;
    
    @Property
    private int zipCode;
			
			

    @SessionState(create=false)
    private UserSession userSession;

    @Inject
    private UserService userService;

    @Component
    private Form registrationForm;

    @Component(id = "loginName")
    private TextField loginNameField;

    @Component(id = "password")
    private PasswordField passwordField;
    
    @Component(id = "zipCode")
    private TextField zipCodeField;

    @Inject
    private Messages messages;

    private Long userProfileId;
    
    @Inject
    private ProductService productService;

    void onValidateFromRegistrationForm() {

        if (!registrationForm.isValid()) {
            return;
        }

        if (zipCode < 1000 || zipCode >= 53000){
        	registrationForm.recordError(zipCodeField, messages
                    .get("error-nonSpanishZipCode"));
        }
        
        if (!password.equals(retypePassword)) {
            registrationForm.recordError(passwordField, messages
                    .get("error-passwordsDontMatch"));
        } else {
        	
            try {
                UserProfile userProfile = userService.registerUser(loginName, password,
                    new UserProfileDetails(firstName, lastName, email,street,number,door,zipCode));
                userProfileId = userProfile.getUserProfileId();
            } catch (DuplicateInstanceException e) {
                registrationForm.recordError(loginNameField, messages
                        .get("error-loginNameAlreadyExists"));
            }

        }

    }

    Object onSuccess() {

        userSession = new UserSession();
        userSession.setUserProfileId(userProfileId);
        userSession.setFirstName(firstName);
        userSession.setProductService(productService);
        userSession.setCart(new Cart(productService));
        
        return Index.class;

    }

}
