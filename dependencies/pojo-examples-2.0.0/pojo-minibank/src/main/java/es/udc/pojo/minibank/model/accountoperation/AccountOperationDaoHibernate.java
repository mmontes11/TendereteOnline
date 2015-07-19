package es.udc.pojo.minibank.model.accountoperation;

import java.util.Calendar;
import java.util.List;

import org.springframework.stereotype.Repository;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;

@Repository("accountOperationDao")
public class AccountOperationDaoHibernate
	extends GenericDaoHibernate<AccountOperation, Long>
	implements AccountOperationDao {

	@SuppressWarnings("unchecked")
	public List<AccountOperation> findByDate(Long accountId,
	    Calendar startDate, Calendar endDate, int startIndex, int count) {

        return getSession().createQuery(
                "SELECT o FROM AccountOperation o WHERE " +
                "o.account.accountId = :accountId AND " +
                "o.date >= :startDate AND o.date <= :endDate ORDER BY o.date").
                setParameter("accountId", accountId).
                setCalendar("startDate", startDate).
                setCalendar("endDate", endDate).
                setFirstResult(startIndex).
                setMaxResults(count).
                list();

	}

	public int getNumberOfOperations(Long accountId, Calendar startDate,
		Calendar endDate) {

		long numberOfOperations = (Long) getSession().createQuery(
                "SELECT COUNT(o) FROM AccountOperation o WHERE " +
                "o.account.accountId = :accountId AND " +
                "o.date >= :startDate AND o.date <= :endDate").
                setParameter("accountId", accountId).
                setCalendar("startDate", startDate).
                setCalendar("endDate", endDate).
                uniqueResult();

        return (int) numberOfOperations;

	}

    public void removeByAccountId(Long accountId) {
        getSession().createQuery("DELETE FROM AccountOperation o WHERE " +
            "o.account.accountId = :accountId").
            setParameter("accountId", accountId).executeUpdate();
    }

}
