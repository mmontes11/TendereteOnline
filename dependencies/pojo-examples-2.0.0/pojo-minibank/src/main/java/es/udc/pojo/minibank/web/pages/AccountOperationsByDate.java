package es.udc.pojo.minibank.web.pages;

import java.text.DateFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.pojo.minibank.model.accountoperation.AccountOperation;
import es.udc.pojo.minibank.model.accountservice.AccountService;
import es.udc.pojo.minibank.web.util.AccountOperationGridDataSource;

public class AccountOperationsByDate {

	private final static int ROWS_PER_PAGE = 10;

	private Long accountId;
	private Date startDate;
	private Date endDate;
	private AccountOperationGridDataSource accountOperationGridDataSource;
	private AccountOperation accountOperation;

	@Inject
	private Locale locale;

	@Inject
	private AccountService accountService;

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public AccountOperationGridDataSource getAccountOperationGridDataSource() {
		return accountOperationGridDataSource;
	}

	public AccountOperation getAccountOperation() {
		return accountOperation;
	}

	public void setAccountOperation(AccountOperation accountOperation) {
		this.accountOperation = accountOperation;
	}

	public DateFormat getDateFormat() {
		return DateFormat.getDateInstance(DateFormat.SHORT, locale);
	}

	public Format getNumberFormat() {
		return NumberFormat.getInstance(locale);
	}

	public int getRowsPerPage() {
		return ROWS_PER_PAGE;
	}

	void onActivate(Long accountId, String startDate, String endDate)
		throws ParseException {

		this.accountId = accountId;
		this.startDate = stringToDate(startDate);
		this.endDate = stringToDate(endDate);

		Calendar startDateAsCalendar = Calendar.getInstance();
		Calendar endDateAsCalendar = Calendar.getInstance();
		startDateAsCalendar.setTime(this.startDate);
		endDateAsCalendar.setTime(this.endDate);
		endDateAsCalendar.set(Calendar.HOUR, 11);
		endDateAsCalendar.set(Calendar.MINUTE, 59);
		endDateAsCalendar.set(Calendar.SECOND, 59);
		endDateAsCalendar.set(Calendar.AM_PM, Calendar.PM);
		endDateAsCalendar.set(Calendar.MILLISECOND, 999);

		accountOperationGridDataSource = new AccountOperationGridDataSource(
			accountService, accountId, startDateAsCalendar, endDateAsCalendar);

	}

	Object[] onPassivate() {
		return new Object[] {accountId, dateToString(startDate),
			dateToString(endDate)};
	}

	private Date stringToDate(String date) throws ParseException {
		return getDateFormatter().parse(date);
	}


	private String dateToString(Date date) {
		return getDateFormatter().format(date);
	}

	/**
	 * It helps to generate pretty REST-like URLs.
	 */
	private DateFormat getDateFormatter() {

		SimpleDateFormat dateFormat =
            (SimpleDateFormat) DateFormat.getDateInstance();
        dateFormat.applyPattern("dd-MM-yyyy");

        return dateFormat;

	}

}
