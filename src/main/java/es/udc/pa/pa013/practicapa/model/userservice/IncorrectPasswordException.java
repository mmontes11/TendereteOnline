package es.udc.pa.pa013.practicapa.model.userservice;

@SuppressWarnings("serial")
public class IncorrectPasswordException extends Exception {

	private String loginName;

	public IncorrectPasswordException(String loginName) {
		super("Incorrect password exception => loginName = " + loginName);
		this.loginName = loginName;
	}

	public String getLoginName() {
		return loginName;
	}

}
