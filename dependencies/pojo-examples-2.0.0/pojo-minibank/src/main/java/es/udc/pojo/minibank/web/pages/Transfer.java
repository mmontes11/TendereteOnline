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

public class Transfer {

	@Property
	private Long sourceAccountId;

	@Property
	private Long destinationAccountId;

	@Property
	private String amount;

	private double amountAsDouble;

	@Component
	private Form transferForm;

	@Component(id = "sourceAccountId")
	private TextField sourceAccountIdTextField;

	@Component(id = "destinationAccountId")
	private TextField destinationAccountIdTextField;
	
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

	void onValidateFromTransferForm() {

		if (!transferForm.isValid()) {
			return;
		}

		NumberFormat numberFormatter = NumberFormat.getInstance(locale);
		ParsePosition position = new ParsePosition(0);
		Number number = numberFormatter.parse(amount, position);
		
		if (position.getIndex() != amount.length()) {
			transferForm.recordError(amountTextField, messages.format(
					"error-incorrectNumberFormat", amount));
			return;
		} else {
			amountAsDouble = number.doubleValue();
		}

		try {
			accountService.transfer(sourceAccountId, destinationAccountId,
					amountAsDouble);
		} catch (InstanceNotFoundException e) {

			Long key = (Long) e.getKey();

			if (key.equals(sourceAccountId)) {
				transferForm.recordError(sourceAccountIdTextField, messages
						.format("error-accountNotFound", sourceAccountId));
			} else {
				transferForm.recordError(destinationAccountIdTextField,
						messages.format("error-accountNotFound",
								destinationAccountId));
			}

		} catch (InsufficientBalanceException e) {
			transferForm.recordError(messages.format(
					"error-insufficientBalance", NumberFormat.getInstance(
							locale).format(e.getCurrentBalance())));
		}
		

	}

	Object onSuccess() {
		return successfulOperation;
	}

}
