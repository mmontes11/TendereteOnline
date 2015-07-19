package es.udc.pojo.minibank.model.account;

import java.util.List;

import org.springframework.stereotype.Repository;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;

@Repository("accountDao")
public class AccountDaoHibernate extends GenericDaoHibernate<Account, Long>
	implements AccountDao {

	@SuppressWarnings("unchecked")
    public List<Account> findByUserId(Long userId, int startIndex,
        int count) {

        return getSession().createQuery(
        	"SELECT a FROM Account a WHERE a.userId = :userId " +
        	"ORDER BY a.accountId").
         	setParameter("userId", userId).
           	setFirstResult(startIndex).
           	setMaxResults(count).list();

    }

}
