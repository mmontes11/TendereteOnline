package es.udc.pojo.minibank.web.pages;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.pojo.minibank.model.accountservice.AccountService;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public class RemoveAccount {

	@Property
	private Long accountId;

	@Component
	private Form removeAccountForm;

	@Component(id="accountId")
	private TextField accountIdTextField;

	@Inject
	private Messages messages;

	@Inject
	private AccountService accountService;

	void onValidateFromRemoveAccountForm() {

		if (!removeAccountForm.isValid()) {
			return;
		}

        try {
    		accountService.removeAccount(accountId);
        } catch (InstanceNotFoundException e) {
        	removeAccountForm.recordError(accountIdTextField,
            		messages.format("error-accountNotFound", accountId));
        }

	}

	Object onSuccess() {
		return SuccessfulOperation.class;
	}

}