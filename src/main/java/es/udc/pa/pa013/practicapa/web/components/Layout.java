package es.udc.pa.pa013.practicapa.web.components;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Cookies;

import com.trsvax.bootstrap.annotations.Exclude;

import es.udc.pa.pa013.practicapa.web.pages.Index;
import es.udc.pa.pa013.practicapa.web.services.AuthenticationPolicy;
import es.udc.pa.pa013.practicapa.web.services.AuthenticationPolicyType;
import es.udc.pa.pa013.practicapa.web.util.CookiesManager;
import es.udc.pa.pa013.practicapa.web.util.UserSession;

@Exclude(stylesheet={"core"})  //If you do not want Tapestry CSS
@Import(stylesheet={
        "context:/css/bootstrap.css",
        "context:/css/bootstrap-responsive.css"
        },
library={
        "context:/js/bootstrap.js"
        }
)
public class Layout {
    
	@Property
    @Parameter(required = false, defaultPrefix = "message")
    private String menuExplanation;

    @Property
    @Parameter(required = true, defaultPrefix = "message")
    private String pageTitle;

    @Property
    @SessionState(create=false)
    private UserSession userSession;

    @Inject
    private Cookies cookies;
    

    @Inject
    private Block user, noSession; 
    
    
    public int getTotalItems() {
		return userSession.getCart().getTotalItems();
	}
    
    public int getAdmin(){
    	if (userSession.getFirstName().equals("admin")){
    		return 1;
    	} else {
    		return 0;
    	}
    }
	
    public Object getTypeUser (){
    	if (userSession == null){
    		return noSession;
    	}
    	return user;

    }
    

    @AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED_USERS)
   	Object onActionFromLogout() {
        userSession = null;
        CookiesManager.removeCookies(cookies);
        return Index.class;
	}
}