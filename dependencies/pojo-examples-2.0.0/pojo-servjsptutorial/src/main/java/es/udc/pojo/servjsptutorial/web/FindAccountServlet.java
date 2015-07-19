package es.udc.pojo.servjsptutorial.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;
import es.udc.pojo.servjsptutorial.model.account.Account;
import es.udc.pojo.servjsptutorial.model.accountservice.AccountServiceImpl;
import es.udc.pojo.servjsptutorial.web.util.PropertyValidator;
import es.udc.pojo.servjsptutorial.web.util.WebUtil;

@SuppressWarnings("serial")
public class FindAccountServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {

        Map<String,String> errors = new HashMap<String,String>();
        String accountId = request.getParameter("accountId");

		long accountIdAsLong = PropertyValidator.validateLong(errors, "accountId",
				accountId, true, 0, Long.MAX_VALUE);
                
        if (!errors.isEmpty()) {
        	request.setAttribute("errors", errors);
            WebUtil.forwardTo(request, response, "FindAccountForm.jsp");
        } else {
        	
            try {
            	Account account = 
            		new AccountServiceImpl().findAccount(accountIdAsLong);
            	request.setAttribute("account", account);
            } catch (InstanceNotFoundException e) {
            }
            WebUtil.forwardTo(request, response, "ShowAccount.jsp");
        }

    }

}
