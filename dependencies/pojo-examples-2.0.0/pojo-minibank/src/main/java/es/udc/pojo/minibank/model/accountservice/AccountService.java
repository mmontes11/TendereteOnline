package es.udc.pojo.minibank.model.accountservice;

import java.util.Calendar;
import java.util.List;

import es.udc.pojo.minibank.model.account.Account;
import es.udc.pojo.minibank.model.accountoperation.AccountOperation;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public interface AccountService {

    public Account createAccount(Account account);

    public Account findAccount(Long accountId) throws InstanceNotFoundException;

    public void addToAccount(Long accountId, double amount)
    	throws InstanceNotFoundException;

    public void withdrawFromAccount(Long accountId, double amount)
        throws InstanceNotFoundException, InsufficientBalanceException;

    /**
     * Returns a block of accounts pertaining to a given user. If the user has
     * no accounts, an empty list is returned.
     *
     * @param userId the user identifier
     * @param startIndex the index (starting from 0) of the first account to
     *        return
     * @param count the maximum number of accounts to return
     * @return the block of accounts
     */
    public AccountBlock findAccountsByUserId(Long userId,
        int startIndex, int count);

    public void removeAccount(Long accountId) throws InstanceNotFoundException;

    public void transfer(Long sourceAccountId,
        Long destinationAccountId, double amount)
        throws InstanceNotFoundException, InsufficientBalanceException;

    public int getNumberOfAccountOperations(Long accountId, Calendar startDate,
    	Calendar endDate) throws InstanceNotFoundException;

    /**
     * Returns a list of account operations performed on a given account
     * between two dates. If there are no operations, an empty list is
     * returned.
     *
     * @param accountId the account identifier
     * @param startDate the start date of the account operations to be returned
     *        (including this date)
     * @param endDate the end date of the account operations to be returned
     *        (including this date)
     * @param startIndex the index (starting from 0) of the first account
     *        operation to return
     * @param count the maximum number of account operations to return
     * @return the list of account operations
     * @throws InstanceNotFoundException if the account does not exist
     */
    public List<AccountOperation> findAccountOperationsByDate(
        Long accountId, Calendar startDate, Calendar endDate,
        int startIndex, int count) throws InstanceNotFoundException;

}
