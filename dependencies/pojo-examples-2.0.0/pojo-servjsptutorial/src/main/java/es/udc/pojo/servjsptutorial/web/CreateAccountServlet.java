package es.udc.pojo.servjsptutorial.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.udc.pojo.servjsptutorial.model.account.Account;
import es.udc.pojo.servjsptutorial.model.accountservice.AccountServiceImpl;
import es.udc.pojo.servjsptutorial.web.util.PropertyValidator;
import es.udc.pojo.servjsptutorial.web.util.WebUtil;

@SuppressWarnings("serial")
public class CreateAccountServlet extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		Map<String, String> errors = new HashMap<String, String>();
		String userId = request.getParameter("userId");
		String balance = request.getParameter("balance");

		long userIdAsLong = PropertyValidator.validateLong(errors, "userId",
				userId, true, 0, Long.MAX_VALUE);
		
		double balanceAsDouble = PropertyValidator.validateDouble(errors, "balance", 
				balance, true, 0, Double.MAX_VALUE);

		if (!errors.isEmpty()) {
			request.setAttribute("errors", errors);
			WebUtil.forwardTo(request, response, "CreateAccountForm.jsp");
		} else {

			Account account = new Account(userIdAsLong, balanceAsDouble);

			/*
			 * Insert the Account in the database
			 */
			Account insertedAccount = new AccountServiceImpl()
					.createAccount(account);

			response.sendRedirect("ShowCreatedAccount.jsp?accountId="
					+ insertedAccount.getAccountId());

		}

	}

}
