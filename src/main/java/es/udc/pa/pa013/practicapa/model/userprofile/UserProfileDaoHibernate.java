package es.udc.pa.pa013.practicapa.model.userprofile;

import org.springframework.stereotype.Repository;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@Repository("userProfileDao")
public class UserProfileDaoHibernate extends
		GenericDaoHibernate<UserProfile, Long> implements UserProfileDao {

	public UserProfile findByLoginName(String loginName)
			throws InstanceNotFoundException {

		UserProfile userProfile = (UserProfile) getSession()
				.createQuery(
						"SELECT u FROM UserProfile u WHERE u.loginName = :loginName")
				.setParameter("loginName", loginName).uniqueResult();
		if (userProfile == null) {
			throw new InstanceNotFoundException(loginName,
					UserProfile.class.getName());
		} else {
			return userProfile;
		}

	}

}