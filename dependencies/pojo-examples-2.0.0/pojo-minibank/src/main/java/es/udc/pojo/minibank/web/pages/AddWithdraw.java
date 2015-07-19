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

import es.udc.pojo.minibank.model.accountservice.AccountService;
import es.udc.pojo.minibank.model.accountservice.InsufficientBalanceException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public class AddWithdraw {

	public enum OperationType {
		ADD_OP, WITH_OP
	}

	@Property
	private Long accountId;

	@Property
	private String amount;

	private double amountAsDouble;

	@Property
	private OperationType operationType;

	@Component
	private Form addWithdrawForm;

	@Component(id = "accountId")
	private TextField accountIdTextField;

	@Component(id = "amount")
	private TextField amountTextField;

	@Inject
	private Locale locale;

	@Inject
	private Messages messages;

	@Inject
	private AccountService accountService;

	@InjectPage
	private SuccessfulOperation successfulOperation;

	void onValidateFromAddWithdrawForm() {

		if (!addWithdrawForm.isValid()) {
			return;
		}

		NumberFormat numberFormatter = NumberFormat.getInstance(locale);
		ParsePosition position = new ParsePosition(0);
		Number number = numberFormatter.parse(amount, position);
		
		if (position.getIndex() != amount.length()) {
			addWithdrawForm.recordError(amountTextField, messages.format(
					"error-incorrectNumberFormat", amount));
			return;
		} else {
			amountAsDouble = number.doubleValue();
		}
		
		try {
			if (operationType == OperationType.ADD_OP) {
				accountService.addToAccount(accountId, amountAsDouble);
			} else {
				accountService.withdrawFromAccount(accountId, amountAsDouble);
			}
		} catch (InstanceNotFoundException e) {
			addWithdrawForm.recordError(accountIdTextField, messages.format(
					"error-accountNotFound", accountId));
		} catch (InsufficientBalanceException e) {
			addWithdrawForm.recordError(messages.format(
					"error-insufficientBalance", NumberFormat.getInstance(
							locale).format(e.getCurrentBalance())));
		}

	}

	Object onSuccess() {
		return successfulOperation;
	}

}
