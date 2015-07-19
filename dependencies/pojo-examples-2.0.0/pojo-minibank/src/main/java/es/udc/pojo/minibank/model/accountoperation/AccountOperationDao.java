package es.udc.pojo.minibank.model.accountoperation;

import java.util.Calendar;
import java.util.List;

import es.udc.pojo.modelutil.dao.GenericDao;

public interface AccountOperationDao
    extends GenericDao<AccountOperation, Long> {

	public int getNumberOfOperations(Long accountId, Calendar startDate,
		Calendar endDate);

    /**
     * Returns a list of account operations performed on a given account
     * between two dates. If there are no operations, an empty list is returned.
     *
     * @param accountId the account identifier
     * @param startDate the start date of the account operations to be returned
     *        (including this date)
     * @param endDate the end date of the account operations to be returned
     *        (including this date)
     * @param startIndex the index (starting from 0) of the first object to
     *        return
     * @param count the maximum number of account operations to return
     * @return the list of account operations
     */
    public List<AccountOperation> findByDate(Long accountId,
    	Calendar startDate, Calendar endDate, int startIndex, int count);

    public void removeByAccountId(Long accountId);

}
