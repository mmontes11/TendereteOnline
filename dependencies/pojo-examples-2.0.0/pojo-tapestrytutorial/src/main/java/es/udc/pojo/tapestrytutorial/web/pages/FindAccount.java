package es.udc.pojo.tapestrytutorial.web.pages;

import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;

public class FindAccount {

	@Property
	private Long accountId;
	
	@InjectPage
	private AccountDetails accountDetails;
	
	Object onSuccess() {
		
		accountDetails.setAccountId(accountId);	
		return accountDetails;

	}

}
