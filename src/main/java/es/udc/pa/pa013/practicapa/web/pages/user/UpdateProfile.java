package es.udc.pa.pa013.practicapa.web.pages.user;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.pa.pa013.practicapa.model.userprofile.UserProfile;
import es.udc.pa.pa013.practicapa.model.userservice.UserProfileDetails;
import es.udc.pa.pa013.practicapa.model.userservice.UserService;
import es.udc.pa.pa013.practicapa.web.pages.Index;
import es.udc.pa.pa013.practicapa.web.services.AuthenticationPolicy;
import es.udc.pa.pa013.practicapa.web.services.AuthenticationPolicyType;
import es.udc.pa.pa013.practicapa.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED_USERS)
public class UpdateProfile {

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
    
    @Component
    private Form updateProfileForm;
    
    @Component(id = "zipCode")
    private TextField zipCodeField;
    
    @Inject
    private Messages messages;

    @SessionState(create=false)
    private UserSession userSession;

    @Inject
    private UserService userService;
    
    @InjectPage
    private NotFound notFound;

    void onPrepareForRender() throws InstanceNotFoundException {

        UserProfile userProfile;

        userProfile = userService.findUserProfile(userSession
                .getUserProfileId());
        firstName = userProfile.getFirstName();
        lastName = userProfile.getLastName();
        email = userProfile.getEmail();
        street = userProfile.getStreet();
        number = userProfile.getNumber();
        door = userProfile.getDoor();
        zipCode = userProfile.getZipCode();

    }

    void onValidateFromUpdateProfileForm() {

        if (!updateProfileForm.isValid()) {
            return;
        }

        if (zipCode < 1000 || zipCode >= 53000){
        	updateProfileForm.recordError(zipCodeField, messages
                    .get("error-nonSpanishZipCode"));
        }
    }
    Object onSuccess() {
        try {
			userService.updateUserProfileDetails(
			        userSession.getUserProfileId(), new UserProfileDetails(firstName, lastName, email,street,number,door,zipCode));
		} catch (InstanceNotFoundException e) {
			notFound.setID(userSession.getUserProfileId());
			return notFound;
		}
        userSession.setFirstName(firstName);
        return Index.class;

    }

}