package es.udc.pojo.minibank.web.pages;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

public class FindAccountOperationsByDate {
	
	@Property
	private Long accountId;
	
	@Property
	private String startDate;
	
	@Property
	private String endDate;
	
	private Date startDateAsDate;
	private Date endDateAsDate;
	
	@Component
	private Form findAccountOperationsByDateForm;
	
	@Component(id="startDate")
	private TextField startDateField;
	
	@Component(id="endDate")
	private TextField endDateField;
	
	@Inject
	private Messages messages;
	
	@Inject
	private Locale locale;
	
	@InjectPage
	private AccountOperationsByDate accountOperationsByDate;
	
	void onValidateFromFindAccountOperationsByDateForm() {
		
		if (!findAccountOperationsByDateForm.isValid()) {
			return;
		}
		
		startDateAsDate = validateDate(startDateField, startDate);
		endDateAsDate = validateDate(endDateField, endDate);
		
	}
	
	Object onSuccess() {
		
		accountOperationsByDate.setAccountId(accountId);
		accountOperationsByDate.setStartDate(startDateAsDate);
		accountOperationsByDate.setEndDate(endDateAsDate);
		
		return accountOperationsByDate;
	}
	
	void onActivate() {
		startDate = dateToString(Calendar.getInstance().getTime());
		endDate = startDate;
	}
	
	private Date validateDate(TextField textField, String dateAsString) {
		
		ParsePosition position = new ParsePosition(0);
		Date date = DateFormat.getDateInstance(DateFormat.SHORT, locale).
			parse(dateAsString, position);
		
		if (position.getIndex() != dateAsString.length()) {
			findAccountOperationsByDateForm.recordError(textField,
				messages.format("error-incorrectDateFormat", dateAsString));
		}

		return date;
		
	}
	
	private String dateToString(Date date) {
		return DateFormat.getDateInstance(DateFormat.SHORT, locale).
			format(date);
	}

}
