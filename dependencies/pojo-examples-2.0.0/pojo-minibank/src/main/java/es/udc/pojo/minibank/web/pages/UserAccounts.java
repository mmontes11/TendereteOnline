package es.udc.pojo.minibank.web.pages;

import java.text.Format;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.pojo.minibank.model.account.Account;
import es.udc.pojo.minibank.model.accountservice.AccountBlock;
import es.udc.pojo.minibank.model.accountservice.AccountService;

public class UserAccounts {
	
	private final static int ACCOUNTS_PER_PAGE = 10;

	private Long userId;
	private int startIndex = 0;
	private AccountBlock accountBlock;
	private Account account;

	@Inject
	private AccountService accountService;
	
	@Inject
	private Locale locale;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public List<Account> getAccounts() {
		return accountBlock.getAccounts();
	}
	
	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}
	
	public Format getFormat() {
		return NumberFormat.getInstance(locale);
	}
	
	public Object[] getPreviousLinkContext() {
		
		if (startIndex-ACCOUNTS_PER_PAGE >= 0) {
			return new Object[] {userId, startIndex-ACCOUNTS_PER_PAGE};
		} else {
			return null;
		}
		
	}
	
	public Object[] getNextLinkContext() {
		
		if (accountBlock.getExistMoreAccounts()) {
			return new Object[] {userId, startIndex+ACCOUNTS_PER_PAGE};
		} else {
			return null;
		}
		
	}
	
	Object[] onPassivate() {
		return new Object[] {userId, startIndex};
	}
	
	void onActivate(Long userId, int startIndex) {
		this.userId = userId;
		this.startIndex = startIndex;
		accountBlock = accountService.findAccountsByUserId(userId,
			startIndex, ACCOUNTS_PER_PAGE);
	}

}
