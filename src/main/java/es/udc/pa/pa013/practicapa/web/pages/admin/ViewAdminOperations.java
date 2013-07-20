package es.udc.pa.pa013.practicapa.web.pages.admin;

import org.apache.tapestry5.annotations.SessionState;

import es.udc.pa.pa013.practicapa.web.services.AuthenticationPolicy;
import es.udc.pa.pa013.practicapa.web.services.AuthenticationPolicyType;
import es.udc.pa.pa013.practicapa.web.util.UserSession;


@AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED_USERS)
public class ViewAdminOperations {
	
	@SessionState(create=false)
    private UserSession userSession;
	
	Object onActivate(){
		if (!userSession.getFirstName().equals("admin")){
			return NotAuthorized.class;
		}
		return null;
	}

}
