package com.ensense.insense.data.uitesting.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.mint.uitesting.dao.LoginUserServiceDAO;
import com.cts.mint.uitesting.entity.LoginUiElement;
import com.cts.mint.uitesting.entity.LoginUserDetails;

@Service
public class LoginUserServiceDAOImpl implements LoginUserServiceDAO {

	private static Logger logger = Logger
			.getLogger(LoginUserServiceDAOImpl.class);

	@Autowired
	SessionFactory sessionFactory;

	
	@Override
	public boolean addLoginUserDetails(LoginUserDetails loginUserDetails) {
		logger.info("Entry: addLoginUserDetails, loginUserDetails->"+loginUserDetails);
		boolean added = false;
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(loginUserDetails);
			added = true;
		} catch (Exception e) {
			logger.error("Exception in addLoginUserDetails :"+e);
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
		}
		logger.info("Exit: addLoginUserDetails");
		return added;
	}

	public boolean addLoginUserUIDetails(LoginUiElement loginUiElement) {
		logger.info("Entry: addUILoginDetails, loginUiElement->"+loginUiElement);
		try {
			sessionFactory.getCurrentSession().persist(loginUiElement);
			// to get an id assigned to the entity since save operation is in loop
			sessionFactory.getCurrentSession().flush();
		} catch (Exception e) {
			logger.error("Exception in addLoginUserUIDetails :"+e);
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
		}
		logger.info("Exit: addUILoginDetails");
		return false;
	}

	@Override
	public boolean deleteLoginUserDetails(LoginUserDetails loginUserDetails) {
		logger.info("Entry: deleteLoginUserDetails");
		boolean isDeleted = false;
		Query query;
		try {
			LoginUiElement loginUiElement = new LoginUiElement();
			loginUiElement.setLoginUserDetailId(loginUserDetails.getLoginUserDetailId());
			query = sessionFactory.getCurrentSession().createQuery("delete from LoginUiElement where loginUserDetailId = :loginUserDetailId");
			query.setParameter("loginUserDetailId", loginUserDetails.getLoginUserDetailId());
			query.executeUpdate();
			query = sessionFactory.getCurrentSession().createQuery("delete from LoginUserDetails where loginUserDetailId = :loginUserDetailId");
			query.setParameter("loginUserDetailId", loginUserDetails.getLoginUserDetailId());
			query.executeUpdate();
			isDeleted = true;

		} catch (Exception e) {
			logger.debug(e);
		}
		logger.info("Exit: deleteLoginUserDetails");
		return isDeleted;
	}

	public boolean deleteLoginUserUIDetails(LoginUiElement loginUiElement) {
		logger.info("Entry: deleteLoginUserUIDetails");
		try {
			sessionFactory.getCurrentSession().delete(loginUiElement);
		} catch (Exception e) {
			logger.debug(e);
		}
		logger.info("Exit: deleteLoginUserUIDetails");
		return false;
	}

	/*
	 * Getting TestLoginUser's list from TestLoginUser table
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<LoginUserDetails> getLoginUserList() {
		logger.info("Entry: getLoginUserList");
		List<LoginUserDetails> loginUserDetailsList = new ArrayList<LoginUserDetails>();
		Query query = null;
		try {
			query = sessionFactory.getCurrentSession().createQuery(
					"from LoginUserDetails");
			loginUserDetailsList = query.list();
			logger.info("getLoginUserList, loginUserDetailsList :"+loginUserDetailsList);
			
		} catch (Exception e) {
			logger.error("Exception in getLoginUserList");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
		}
		logger.info("Exit: getLoginUserList");
		return loginUserDetailsList;
	}

	/*
	 * Getting TestLoginUser's list from TestLoginUser table
	 */
	@SuppressWarnings("unchecked")
	@Override
	public LoginUserDetails getLoginUserDetails(LoginUserDetails loginUserDetails) {
		logger.info("Entry: getLoginUserDetails " + loginUserDetails);
		List<LoginUserDetails> loginUserDetailsList = new ArrayList<LoginUserDetails>();
		LoginUserDetails loginUserDetail = new LoginUserDetails();
		Query query;

		try {

			if (loginUserDetails.getLoginUserDetailId()!=null && loginUserDetails.getLoginUserDetailId() > 0) {
				query = sessionFactory.getCurrentSession().createQuery(
						"from LoginUserDetails where loginUserDetailId= :loginUserDetailId");
				query.setParameter("loginUserDetailId",
						loginUserDetails.getLoginUserDetailId());
			} else {
				query = sessionFactory
						.getCurrentSession()
						.createQuery(
								"from LoginUserDetails where applicationId = :applicationId and environmentId = :environmentId");
				query.setParameter("applicationId",
						loginUserDetails.getApplicationId());
				query.setParameter("environmentId",
						loginUserDetails.getEnvironmentId());
			}
			
			loginUserDetailsList = query.list();
			logger.info("loginUserDetailsList :"+loginUserDetailsList);
			if ( loginUserDetailsList != null && loginUserDetailsList.size() > 0 ) {
				loginUserDetail = (LoginUserDetails) loginUserDetailsList.get(0);
			}
		} catch (Exception exp) {
			logger.error("Exception in getLoginUserDetails");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(exp));
		}
		logger.info("Exit: getLoginUserDetails");
		return loginUserDetail;

	}

	@Override
	public boolean deleteLoginUIElements(LoginUserDetails loginUserDetails) {
		logger.info("Entry: deleteLoginUIElements");
		boolean isDeleted = false;
		Query query;
		try {
			query = sessionFactory.getCurrentSession().createQuery("delete from LoginUiElement where loginUserDetailId = :loginUserDetailId");
			query.setParameter("loginUserDetailId", loginUserDetails.getLoginUserDetailId());
			query.executeUpdate();
			isDeleted = true;
		} catch (Exception exp) {
			logger.debug(exp);
		}
		logger.info("Exit: deleteLoginUIElements");
		return isDeleted;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean deleteLoginUserListdetails(LoginUserDetails loginUserDetails) {
		logger.info("Entry: deleteLoginUserListdetails");
		Query query = null;
		boolean isDeleted = false;
		
		try {
			query = sessionFactory.getCurrentSession().createQuery(
					"from LoginUserDetails where applicationId=:applicationId ");
			query.setParameter("applicationId", loginUserDetails.getApplicationId());
			
			List<LoginUserDetails> result = query.list();
			if(result!=null&& result.size()>0){
			  for(LoginUserDetails obj: result){
				LoginUserDetails loginUserDetailsObj = new LoginUserDetails();
				loginUserDetailsObj.setLoginUserDetailId(obj.getLoginUserDetailId());
				isDeleted = deleteLoginUserDetails(loginUserDetailsObj);
				logger.info("Specific: deleteLoginUserListdetails" +isDeleted);
			  }
			}  
			
		} catch (Exception exp) {
			logger.debug(exp);
		}
		logger.info("Exit: getLoginUserList--no argument");
		return isDeleted;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LoginUserDetails>  getLoginUserDetails(int applicationId,
			int environmentCategoryId) {
		logger.debug("Entry: getLoginUserDetails ");
		List<LoginUserDetails> loginUserDetailsList = new ArrayList<LoginUserDetails>();
		Query query;

		try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from LoginUserDetails where applicationId= :applicationId and environmentId in (select environmentId from AppEnvEnvironmentCategoryXref where environmentCategoryId=:environmentCategoryId)");
			query.setParameter("applicationId", applicationId);
			query.setParameter("environmentCategoryId", environmentCategoryId);
			loginUserDetailsList = query.list();
		} catch (Exception exp) {
			logger.error("Exception in getLoginUserDetails");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(exp));
		}
		logger.debug("Exit: getLoginUserDetails");
		return loginUserDetailsList;

	}
	
	@SuppressWarnings("unchecked")
	@Override
	public LoginUserDetails getLoginIdDetails(LoginUserDetails loginUserDetails){
		logger.info("Entry: getLoginIdDetails ");
		List<LoginUserDetails> loginUserDetailsList = new ArrayList<LoginUserDetails>();
		LoginUserDetails loginUserDetail = new LoginUserDetails();
		Query query;

		try {

			
				query = sessionFactory
						.getCurrentSession()
						.createQuery(
								"from LoginUserDetails where applicationId = :applicationId " +
								" and environmentId = :environmentId" +
								" and loginId = :loginId ");
				query.setParameter("applicationId",
						loginUserDetails.getApplicationId());
				query.setParameter("environmentId",
						loginUserDetails.getEnvironmentId());
				query.setParameter("loginId",
						loginUserDetails.getLoginId());
			
			
			loginUserDetailsList = query.list();
			logger.info("loginUserDetailsList :"+loginUserDetailsList);
			if ( loginUserDetailsList != null && loginUserDetailsList.size() > 0 ) {
				loginUserDetail = (LoginUserDetails) loginUserDetailsList.get(0);
			}
		} catch (Exception exp) {
			logger.error("Exception in getLoginIdDetails");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(exp));
		}
		logger.info("Exit: getLoginIdDetails");
		return loginUserDetail;
	}
}
