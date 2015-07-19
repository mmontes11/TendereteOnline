package es.udc.pojo.tapestrytutorial.web.pages;

import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;

import es.udc.pojo.tapestrytutorial.model.account.Account;
import es.udc.pojo.tapestrytutorial.model.accountservice.AccountServiceImpl;


public class CreateAccount {

	@Property
	private Long userId;
	
	@Property
	private Double balance;
	
	@InjectPage
	private AccountCreated accountCreated;
	
	Object onSuccess() {
		
		Account account = new Account(userId, balance);
		
		new AccountServiceImpl().createAccount(account);
		accountCreated.setAccountId(account.getAccountId());
		
		return accountCreated;
		
	}

}