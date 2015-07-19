package es.udc.pojo.minibank.test.experiments.hibernate;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import es.udc.pojo.minibank.model.account.Account;

public class FindAccountsByUserId {

	private final static String USAGE_MESSAGE = "Usage: FindAccountsByUserId <userId:long>";

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {

		long userId = -1;
		if (args.length == 1) {
			try {
				userId = Long.parseLong(args[0]);
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

			List<Account> accounts = session.createQuery(
					"SELECT a FROM Account a WHERE a.userId = :userId")
					.setParameter("userId", userId).setFirstResult(0)
					.setMaxResults(10).list();

			if (accounts.isEmpty()) {
				System.out.println("Accounts with userId '" + userId
						+ "' have not been found");
			} else {
				for (Account account : accounts) {
					System.out.println("AccountId: " + account.getAccountId());
					System.out.println("UserId: " + account.getUserId());
					System.out.println("Balance: " + account.getBalance());
				}
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
