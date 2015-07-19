package es.udc.pojo.minibank.test.experiments.hibernate;

import org.hibernate.Session;
import org.hibernate.Transaction;

import es.udc.pojo.minibank.model.account.Account;

public class FindAccount {

	private final static String USAGE_MESSAGE = "Usage: FindAccount <accountId:long>";

	public static void main(String[] args) {

		long accountId = -1;
		if (args.length == 1) {
			try {
				accountId = Long.parseLong(args[0]);
			} catch (NumberFormatException e) {
				System.err.println("Error: " + USAGE_MESSAGE);
				System.exit(-1);
			}
		} else {
			System.err.println("Error: " + USAGE_MESSAGE);
			System.exit(-1);
		}

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();

			Account account = (Account) session.get(Account.class, accountId);

			if (account != null) {
				System.out.println("AccountId: " + account.getAccountId());
				System.out.println("UserId: " + account.getUserId());
				System.out.println("Balance: " + account.getBalance());
			} else {
				System.out.println("Account with accountId '" + accountId
						+ "' has not been found");
			}

			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			session.close();
		}
		HibernateUtil.shutdown();

	}

}
