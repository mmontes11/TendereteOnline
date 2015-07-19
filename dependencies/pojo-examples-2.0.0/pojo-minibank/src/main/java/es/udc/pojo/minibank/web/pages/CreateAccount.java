package es.udc.pojo.minibank.web.pages;

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.pojo.minibank.model.account.Account;
import es.udc.pojo.minibank.model.accountservice.AccountService;


public class CreateAccount {

	@Property
	private Long userId;
	
	@Property
	private String balance;
	
	private double balanceAsDouble;
	
	@Component
	private Form createAccountForm;
	
	@Component(id="balance")
	private TextField balanceTextField;
	
	@Inject
	private Locale locale;
	
	@Inject
	private Messages messages;
	
	@Inject
	private AccountService accountService;
	
	@InjectPage
	private AccountCreated accountCreated;
	
	void onValidateFromCreateAccountForm() {
		
		if (!createAccountForm.isValid()) {
			return;
		}
		
		NumberFormat numberFormatter = NumberFormat.getInstance(locale);
		ParsePosition position = new ParsePosition(0);
		Number number = 
			numberFormatter.parse(balance, position);
		
		if (position.getIndex() != balance.length()) {
			createAccountForm.recordError(balanceTextField, messages.format(
					"error-incorrectNumberFormat", balance));
		} else {
			balanceAsDouble = number.doubleValue();
		}

	}
		
	Object onSuccess() {
		
		Account account = new Account(userId, balanceAsDouble);
		
		accountService.createAccount(account);
		accountCreated.setAccountId(account.getAccountId());
		
		return accountCreated;
		
	}

}
