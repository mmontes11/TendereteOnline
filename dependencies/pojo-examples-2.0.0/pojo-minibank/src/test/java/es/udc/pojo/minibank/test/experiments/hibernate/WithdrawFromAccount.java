package es.udc.pojo.minibank.test.experiments.hibernate;

import java.util.Calendar;

import org.hibernate.Transaction;

import es.udc.pojo.minibank.model.account.Account;
import es.udc.pojo.minibank.model.account.AccountDao;
import es.udc.pojo.minibank.model.account.AccountDaoHibernate;
import es.udc.pojo.minibank.model.accountoperation.AccountOperation;
import es.udc.pojo.minibank.model.accountoperation.AccountOperationDao;
import es.udc.pojo.minibank.model.accountoperation.AccountOperationDaoHibernate;
import es.udc.pojo.minibank.model.accountservice.InsufficientBalanceException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public class WithdrawFromAccount {

	private final static String USAGE_MESSAGE = "Usage: WithdrawFromAccount <accountId:long> <amount:double>";

	public static void main(String[] args) {

		long accountId = -1;
		double amount = -1;
		if (args.length == 2) {
			try {
				accountId = Long.parseLong(args[0]);
				amount = Double.parseDouble(args[1]);
			} catch (NumberFormatException e) {
				System.err.println("Error: " + USAGE_MESSAGE);
				System.exit(-1);
			}
		} else {
			System.err.println("Error: " + USAGE_MESSAGE);
			System.exit(-1);
		}

		AccountDaoHibernate accountDaoHibernate = new AccountDaoHibernate();
		accountDaoHibernate
				.setSessionFactory(HibernateUtil.getSessionFactory());
		AccountDao accountDao = accountDaoHibernate;

		AccountOperationDaoHibernate accountOperationDaoHibernate = new AccountOperationDaoHibernate();
		accountOperationDaoHibernate.setSessionFactory(HibernateUtil
				.getSessionFactory());
		AccountOperationDao accountOperationDao = accountOperationDaoHibernate;

		Transaction tx = HibernateUtil.getSessionFactory().getCurrentSession()
				.beginTransaction();

		try {
			/* Find account. */
			Account account = accountDao.find(accountId);

			/* Modify balance. */
			double currentBalance = account.getBalance();

			if (currentBalance < amount) {
				throw new InsufficientBalanceException(accountId,
						currentBalance, amount);
			}

			account.setBalance(currentBalance - amount);

			/* Register account operation. */
			accountOperationDao.save(new AccountOperation(account, Calendar
					.getInstance(), AccountOperation.Type.WITHDRAW, amount));

			tx.commit();

		} catch (RuntimeException|Error e) {
			e.printStackTrace();
			tx.rollback();
			throw e;
		} catch (InstanceNotFoundException|InsufficientBalanceException e) {
			e.printStackTrace();
			tx.commit();
		} finally {
			HibernateUtil.getSessionFactory().getCurrentSession().close();
		}
		HibernateUtil.shutdown();

	}

}
