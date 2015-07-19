package es.udc.pojo.minibank.web.pages;

import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;

public class FindAccounts {
	
    public enum AccountSearchType {
		ACC_ID, USR_ID
	}
	
	@Property
	private Long id;
	
	@Property
	private AccountSearchType accountSearchType;
	
	@InjectPage
	private AccountDetails accountDetails;
	
	@InjectPage
	private UserAccounts userAccounts;

	Object onSuccess() {
		
		if (accountSearchType==AccountSearchType.ACC_ID) {
			accountDetails.setAccountId(id);	
			return accountDetails;
		} else {
			userAccounts.setUserId(id);
			return userAccounts;
		}

	}

}
