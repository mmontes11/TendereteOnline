package es.udc.pojo.tapestrytutorial.web.pages;

import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;
import es.udc.pojo.tapestrytutorial.model.account.Account;
import es.udc.pojo.tapestrytutorial.model.accountservice.AccountServiceImpl;

public class AccountDetails {
	private Long accountId;
	private Account account;
	
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
	
	public Account getAccount() {
		return account;
	}

	void onActivate(Long accountId) {
				
		this.accountId = accountId;
		
		try {
			account = new AccountServiceImpl().findAccount(accountId);
		} catch (InstanceNotFoundException e) {
		}
		
	}
	
	Long onPassivate() {
		return accountId;
	}

}
