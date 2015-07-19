package es.udc.pojo.minibank.test.experiments.hibernate;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import es.udc.pojo.minibank.model.account.Account;

public class ExistAccount {

	private final static String USAGE_MESSAGE = "Usage: ExistAccount <accountId:long>";

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

			if (session.createCriteria(Account.class).add(
					Restrictions.idEq(accountId)).setProjection(
					Projections.id()).uniqueResult() != null) {
				System.out.println("Account with accountId = '" + accountId
						+ "' exists");
			} else {
				System.out.println("Account with accountId = '" + accountId
						+ "' doest not exists");
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
