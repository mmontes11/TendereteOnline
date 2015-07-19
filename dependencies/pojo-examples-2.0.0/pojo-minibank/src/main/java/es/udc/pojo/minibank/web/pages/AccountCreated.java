package es.udc.pojo.minibank.web.pages;

public class AccountCreated {

	private Long accountId;
	
	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	Long onPassivate() {
		return accountId;
	}
	
	void onActivate(Long accountId) {
		this.accountId = accountId;
	}
	
}
