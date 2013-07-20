package es.udc.pa.pa013.practicapa.model.userprofile;

import es.udc.pojo.modelutil.dao.GenericDao;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public interface UserProfileDao extends GenericDao<UserProfile, Long> {

	/**
	 * Returns an UserProfile by login name (not user identifier)
	 * 
	 * @param loginName
	 *            the user identifier
	 * @return the UserProfile
	 */
	public UserProfile findByLoginName(String loginName)
			throws InstanceNotFoundException;
}
