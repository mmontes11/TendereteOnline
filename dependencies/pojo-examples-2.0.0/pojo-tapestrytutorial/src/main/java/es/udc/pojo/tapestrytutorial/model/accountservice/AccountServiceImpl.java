package es.udc.pojo.tapestrytutorial.model.accountservice;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;
import es.udc.pojo.tapestrytutorial.model.account.Account;

public class AccountServiceImpl {

	private static Map<Long, Account> accounts = Collections
			.synchronizedMap(new HashMap<Long, Account>());
	private static long lastAccountId = 1;

	public Account createAccount(Account account) {
		long accountId = getNextAccountId();
		account.setAccountId(accountId);
		accounts.put(accountId, account);
		return account;
	}

	public Account findAccount(long accountId) throws InstanceNotFoundException {
		Account account = accounts.get(accountId);
		if (account == null) {
			throw new InstanceNotFoundException(accountId, Account.class
					.getName());
		}
		return account;
	}

	private synchronized static long getNextAccountId() {
		return lastAccountId++;
	}
}
