package es.udc.pojo.minibank.web.util;

import java.util.Calendar;
import java.util.List;

import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.SortConstraint;

import es.udc.pojo.minibank.model.accountoperation.AccountOperation;
import es.udc.pojo.minibank.model.accountservice.AccountService;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public class AccountOperationGridDataSource implements GridDataSource {

	private AccountService accountService;
	private Long accountId;
	private Calendar startDate;
	private Calendar endDate;
	private List<AccountOperation> accountOperations;
	private int totalNumberOfAccountOperations;
	private int startIndex;
	private boolean accountNotFound;

	public AccountOperationGridDataSource(AccountService accountService,
		Long accountId, Calendar startDate, Calendar endDate) {

		this.accountService = accountService;
		this.accountId = accountId;
		this.startDate = startDate;
		this.endDate = endDate;

		try {
			totalNumberOfAccountOperations =
				accountService.getNumberOfAccountOperations(accountId,
					startDate, endDate);
		} catch (InstanceNotFoundException e) {
			accountNotFound = true;
		}

	}

    public int getAvailableRows() {
        return totalNumberOfAccountOperations;
    }

    public Class<AccountOperation> getRowType() {
        return AccountOperation.class;
    }

    public Object getRowValue(int index) {
        return accountOperations.get(index-this.startIndex);
    }

    public void prepare(int startIndex, int endIndex,
    	List<SortConstraint> sortConstraints) {

        try {

        	accountOperations = accountService.findAccountOperationsByDate(
        				accountId, startDate, endDate, startIndex,
        				endIndex-startIndex+1);
	        this.startIndex = startIndex;

		} catch (InstanceNotFoundException e) {
		}

    }

    public boolean getAccountNotFound() {
    	return accountNotFound;
    }

}
