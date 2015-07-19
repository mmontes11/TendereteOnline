package es.udc.pojo.minibank.model.account;

import java.util.List;

import es.udc.pojo.modelutil.dao.GenericDao;

public interface AccountDao extends GenericDao<Account, Long> {    
        
    /**
     * Returns a list of accounts pertaining to a given user. If the user has
     * no accounts, an empty list is returned.
     *
     * @param userId the user identifier
     * @param startIndex the index (starting from 0) of the first account to 
     *        return
     * @param count the maximum number of accounts to return
     * @return the list of accounts
     */
    public List<Account> findByUserId(Long userId, int startIndex,
    	int count);

}
