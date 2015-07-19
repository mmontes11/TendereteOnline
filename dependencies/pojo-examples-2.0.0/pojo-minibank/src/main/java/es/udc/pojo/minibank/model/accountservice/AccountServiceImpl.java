package es.udc.pojo.minibank.model.accountservice;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.pojo.minibank.model.account.Account;
import es.udc.pojo.minibank.model.account.AccountDao;
import es.udc.pojo.minibank.model.accountoperation.AccountOperation;
import es.udc.pojo.minibank.model.accountoperation.AccountOperationDao;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@Service("accountService")
@Transactional
public class AccountServiceImpl implements AccountService {

    @Autowired
	private AccountDao accountDao;

    @Autowired
	private AccountOperationDao accountOperationDao;

	public Account createAccount(Account account) {

		accountDao.save(account);

		return account;

	}

	@Transactional(readOnly = true)
	public Account findAccount(Long accountId) throws InstanceNotFoundException {
		return accountDao.find(accountId);
	}

	public void addToAccount(Long accountId, double amount)
			throws InstanceNotFoundException {

		/* Find account. */
		Account account = accountDao.find(accountId);

		/* Modify balance. */
		account.setBalance(account.getBalance() + amount);

		/* Register account operation. */
		accountOperationDao.save(new AccountOperation(account,
			Calendar.getInstance(), AccountOperation.Type.ADD, amount));

	}

	public void withdrawFromAccount(Long accountId, double amount)
			throws InstanceNotFoundException, InsufficientBalanceException {

		/* Find account. */
		Account account = accountDao.find(accountId);

		/* Modify balance. */
		double currentBalance = account.getBalance();

		if (currentBalance < amount) {
			throw new InsufficientBalanceException(accountId, currentBalance,
				amount);
		}

		account.setBalance(currentBalance - amount);

		/* Register account operation. */
		accountOperationDao.save(new AccountOperation(account,
			Calendar.getInstance(), AccountOperation.Type.WITHDRAW, amount));

	}

	@Transactional(readOnly = true)
	public AccountBlock findAccountsByUserId(Long userId, int startIndex,
		int count) {

		/*
		 * Find count+1 accounts to determine if there exist more accounts above
		 * the specified range.
		 */
		List<Account> accounts = accountDao.findByUserId(userId, startIndex,
				count + 1);

		boolean existMoreAccounts = accounts.size() == (count + 1);

		/*
		 * Remove the last account from the returned list if there exist more
		 * accounts above the specified range.
		 */
		if (existMoreAccounts) {
			accounts.remove(accounts.size() - 1);
		}

		/* Return AccountBlock. */
		return new AccountBlock(accounts, existMoreAccounts);

	}

	public void removeAccount(Long accountId) throws InstanceNotFoundException {
	    accountDao.remove(accountId);
	    accountOperationDao.removeByAccountId(accountId);
	}

	public void transfer(Long sourceAccountId, Long destinationAccountId,
		double amount) throws InstanceNotFoundException,
		InsufficientBalanceException {

		/* Find accounts. */
		Account sourceAccount = accountDao.find(sourceAccountId);
		Account destinationAccount = accountDao.find(destinationAccountId);

		/* Modify accounts. */
		double sourceCurrentBalance = sourceAccount.getBalance();

		if (sourceCurrentBalance < amount) {
			throw new InsufficientBalanceException(sourceAccountId,
					sourceCurrentBalance, amount);
		}

		sourceAccount.setBalance(sourceCurrentBalance - amount);
		destinationAccount.setBalance(destinationAccount.getBalance() + amount);

		/* Register account operations. */
		Calendar date = Calendar.getInstance();

		accountOperationDao.save(new AccountOperation(sourceAccount, date,
				AccountOperation.Type.WITHDRAW, amount));
		accountOperationDao.save(new AccountOperation(destinationAccount,
				date, AccountOperation.Type.ADD, amount));
	}

	@Transactional(readOnly = true)
	public int getNumberOfAccountOperations(Long accountId, Calendar startDate,
		Calendar endDate) throws InstanceNotFoundException {

		/* Check if account exists. */
		if (!accountDao.exists(accountId)) {
			throw new InstanceNotFoundException(accountId,
				Account.class.getName());
		}

		/* Return count. */
		return accountOperationDao.getNumberOfOperations(accountId, startDate,
			endDate);

	}

	@Transactional(readOnly = true)
	public List<AccountOperation> findAccountOperationsByDate(
		Long accountId, Calendar startDate, Calendar endDate, int startIndex,
		int count) throws InstanceNotFoundException {

		/* Check if account exists. */
		if (!accountDao.exists(accountId)) {
			throw new InstanceNotFoundException(accountId,
				Account.class.getName());
		}

		/* Return account operations */
		return accountOperationDao.findByDate(accountId, startDate, endDate,
				startIndex, count);

	}

}
