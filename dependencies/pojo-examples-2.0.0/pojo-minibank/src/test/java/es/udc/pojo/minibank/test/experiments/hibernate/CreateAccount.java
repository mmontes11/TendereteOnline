package es.udc.pojo.minibank.test.experiments.hibernate;

import org.hibernate.Session;
import org.hibernate.Transaction;

import es.udc.pojo.minibank.model.account.Account;


public class CreateAccount {

	public static void main(String[] args) {

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();

			Account account = new Account(1, 200);

			session.saveOrUpdate(account);

			System.out.println("AccountId: " + account.getAccountId());
			System.out.println("UserId: " + account.getUserId());
			System.out.println("Balance: " + account.getBalance());
			tx.commit();
			
			System.out.println("Account with accountId '" + account.getAccountId()
					+ "' has been created");

		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			session.close();
		}

		HibernateUtil.getSessionFactory().close();

	}

}
