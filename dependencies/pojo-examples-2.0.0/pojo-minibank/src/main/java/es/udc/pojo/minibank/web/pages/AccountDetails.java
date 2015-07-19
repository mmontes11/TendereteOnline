package es.udc.pojo.minibank.web.pages;

import java.text.Format;
import java.text.NumberFormat;
import java.util.Locale;

import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.pojo.minibank.model.account.Account;
import es.udc.pojo.minibank.model.accountservice.AccountService;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public class AccountDetails {

	private Long accountId;
	private Account account;
	
	@Inject
	private AccountService accountService;
		
	@Inject
	private Locale locale;

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
	
	public Account getAccount() {
		return account;
	}

	public Format getFormat() {
		return NumberFormat.getInstance(locale);
	}
	
	void onActivate(Long accountId) {
				
		this.accountId = accountId;
		
		try {
			account = accountService.findAccount(accountId);
		} catch (InstanceNotFoundException e) {
		}
		
	}
	
	Long onPassivate() {
		return accountId;
	}

}
