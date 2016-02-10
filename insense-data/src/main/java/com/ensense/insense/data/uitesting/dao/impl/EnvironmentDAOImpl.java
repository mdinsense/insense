package com.ensense.insense.data.uitesting.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cts.mint.common.entity.EnvironmentCategoryGroupXref;
import com.cts.mint.uitesting.dao.EnvironmentDAO;
import com.cts.mint.uitesting.entity.Application;
import com.cts.mint.uitesting.entity.EnvEnvironmentCategoryXref;
import com.cts.mint.uitesting.entity.Environment;
import com.cts.mint.uitesting.entity.EnvironmentCategory;
import com.cts.mint.uitesting.entity.AppEnvEnvironmentCategoryXref;
import com.cts.mint.uitesting.entity.EnvironmentLoginScript;
import com.cts.mint.uitesting.model.ApplicationEnvironmentCategory;
import com.cts.mint.uitesting.model.UiTestingSetupForm;

@Service
public class EnvironmentDAOImpl implements EnvironmentDAO {
	private static Logger logger = Logger.getLogger(EnvironmentDAOImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<EnvironmentCategoryGroupXref> getEnvironmentListForGroup(
			int groupId) {
		logger.debug("Entry: getEnvironmentListForGroup, groupId->" + groupId);
		List<EnvironmentCategoryGroupXref> environmentGroupReferenceList = new ArrayList<EnvironmentCategoryGroupXref>();
		Query query = null;
		try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from EnvironmentCategoryGroupXref where groupId = :groupId");
			query.setParameter("groupId", groupId);

			environmentGroupReferenceList = query.list();
		} catch (Exception e) {
			logger.error("Exception in addApplication :" + e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getEnvironmentListForGroup");

		return environmentGroupReferenceList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Environment> getAllEnvironmentDetails() {
		logger.debug("Entry: getAllEnvironments");
		Query query = null;
		List<Environment> environmentList = new ArrayList<Environment>();
		try {
			query = sessionFactory.getCurrentSession().createQuery(
					"from Environment order by environmentId");
			environmentList = query.list();
		}

		catch (Exception e) {
			logger.error("Exception in getAllApplicationDetails :" + e);
			logger.error("Stack Trace :"
					+ ExceptionUtils.getRootCauseMessage(e));
		}
		logger.debug("Exit: getAllEnvironments");

		return environmentList;
	}

	@Override
	public boolean addEnvironment(Environment environment) {
		logger.debug("Entry: addEnvironment, environment->" + environment);
		boolean result = true;
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(environment);
		} catch (Exception e) {
			result = false;
			logger.error("Exception in addEnvironment :" + e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: addEnvironment");
		return result;
	}

	@Override
	public boolean addEnvironmentLoginScript(
			EnvironmentLoginScript environmentLoginScript) {
		logger.debug("Entry: addEnvironmentLoginScript");
		boolean result = true;
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(
					environmentLoginScript);
		} catch (Exception e) {
			result = false;
			logger.error("Exception in addEnvironmentLoginScript.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: addEnvironmentLoginScript");
		return result;
	}

	@Override
	public boolean deleteEnvironmentLoginScript(int environmentId) {
		logger.debug("Entry: deleteEnvironmentLoginScript");
		boolean result = true;
		try {
			Query query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"delete from EnvironmentLoginScript where environmentId=:environmentId");
			query.setParameter("environmentId", environmentId);
			query.executeUpdate();
		} catch (Exception e) {
			result = false;
			logger.error("Exception in deleteEnvironmentLoginScript :" + e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: deleteEnvironmentLoginScript");
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public EnvironmentLoginScript getEnvironmentLoginScript(int environmentId) {
		logger.debug("Entry: getEnvironmentLoginScript");
		List<EnvironmentLoginScript> environmentLoginScriptList = new ArrayList<EnvironmentLoginScript>();
		EnvironmentLoginScript environmentLoginScript = new EnvironmentLoginScript();
		try {
			Query query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from EnvironmentLoginScript where environmentId=:environmentId");
			query.setParameter("environmentId", environmentId);
			environmentLoginScriptList = query.list();
			if (environmentLoginScriptList != null
					&& environmentLoginScriptList.size() > 0) {
				environmentLoginScript = environmentLoginScriptList.get(0);
			}
		} catch (Exception e) {
			logger.error("Exception in getEnvironmentLoginScript :" + e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getEnvironmentLoginScript");
		return environmentLoginScript;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EnvironmentLoginScript> getEnvironmentScriptListDetails() {
		logger.debug("Entry: getEnvironmentScriptListDetails");
		Query query = null;
		List<EnvironmentLoginScript> environmentLoginScriptList = new ArrayList<EnvironmentLoginScript>();
		try {
			query = sessionFactory.getCurrentSession().createQuery(
					"from EnvironmentLoginScript order by environmentId");
			environmentLoginScriptList = query.list();
		} catch (Exception e) {
			logger.error("Exception in getEnvironmentScriptListDetails :" + e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getEnvironmentScriptListDetails");
		return environmentLoginScriptList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public EnvironmentCategory getEnvironmentCategoryForEnvironment(
			int environmentId) {
		logger.debug("Entry: getEnvironment");
		Query query = null;
		List<EnvironmentCategory> environmentCategoryList = new ArrayList<EnvironmentCategory>();
		EnvironmentCategory environmentCategory = new EnvironmentCategory();
		try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"Select ec from EnvironmentCategory ec, AppEnvEnvironmentCategoryXref ecx  where ecx.environmentId = :environmentId and ec.environmentCategoryId = ecx.environmentCategoryId");
			query.setParameter("environmentId", environmentId);
			environmentCategoryList = query.list();
			if (environmentCategoryList != null
					&& environmentCategoryList.size() > 0) {
				environmentCategory = environmentCategoryList.get(0);
			}
		} catch (Exception e) {
			logger.error("Exception in getEnvironment :" + e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getEnvironment");
		return environmentCategory;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean isEnvironmentExists(String environmentName) {
		logger.debug("Entry: isEnvironmentExists");
		Query query = null;
		List<Environment> environmentList = new ArrayList<Environment>();
		boolean isEnvironmentExists = false;
		try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from Environment where environmentName = :environmentName");
			query.setParameter("environmentName", environmentName);
			environmentList = query.list();
			if (environmentList != null && environmentList.size() > 0) {
				isEnvironmentExists = true;
			}

		} catch (Exception e) {
			logger.error("Exception in isEnvironmentExists :" + e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: isEnvironmentExists");
		return isEnvironmentExists;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Environment> getMappedEnvironmentList() {
		logger.debug("Entry: geMappedEnvironmentList");
		Query query = null;

		List<Environment> environmentList = new ArrayList<Environment>();
		try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"select environment.environmentId,environment.environmentName, environment.isPublicSite "
									+ "from Environment environment, AppEnvEnvironmentCategoryXref envRef where envRef.environmentId = environment.environmentId");
			environmentList = query.list();

		} catch (Exception e) {
			logger.error("Exception in geMappedEnvironmentList :" + e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: geMappedEnvironmentList");

		return environmentList;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean deleteEnvironment(UiTestingSetupForm uiTestingSetupForm) {
		logger.debug("Entry: deleteEnvironment");
		boolean isDeleted = true;
		Query query;
		try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"delete from ExcludeUrl where environmentId =:environmentId and applicationId = :applicationId");
			query.setParameter("environmentId",
					uiTestingSetupForm.getEnvironmentId());
			query.setParameter("applicationId",
					uiTestingSetupForm.getApplicationId());
			query.executeUpdate();

			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"delete from IncludeUrl where environmentId =:environmentId and applicationId = :applicationId");
			query.setParameter("environmentId",
					uiTestingSetupForm.getEnvironmentId());
			query.setParameter("applicationId",
					uiTestingSetupForm.getApplicationId());
			query.executeUpdate();

			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"delete from LoginUiElement where loginUserDetailId in ("
									+ "select loginUserDetailId from LoginUserDetails where environmentId =:environmentId and applicationId = :applicationId)");
			query.setParameter("environmentId",
					uiTestingSetupForm.getEnvironmentId());
			query.setParameter("applicationId",
					uiTestingSetupForm.getApplicationId());
			query.executeUpdate();

			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"delete from LoginUserDetails where environmentId =:environmentId and applicationId =:applicationId");
			query.setParameter("environmentId",
					uiTestingSetupForm.getEnvironmentId());
			query.setParameter("applicationId",
					uiTestingSetupForm.getApplicationId());
			query.executeUpdate();

			/*query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"delete from EnvironmentCategoryGroupXref where environmentId =:environmentId");
			query.setParameter("environmentId",
					uiTestingSetupForm.getEnvironmentId());
			query.executeUpdate();*/

			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"delete from ApplicationConfig where environmentId =:environmentId and applicationId =:applicationId");
			query.setParameter("environmentId",
					uiTestingSetupForm.getEnvironmentId());
			query.setParameter("applicationId",
					uiTestingSetupForm.getApplicationId());
			query.executeUpdate();

			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"delete from AppEnvEnvironmentCategoryXref where environmentId =:environmentId and applicationId =:applicationId");
			query.setParameter("environmentId",
					uiTestingSetupForm.getEnvironmentId());
			query.setParameter("applicationId",
					uiTestingSetupForm.getApplicationId());
			query.executeUpdate();

			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"delete from EnvEnvironmentCategoryXref where environmentId =:environmentId");
			query.setParameter("environmentId",
					uiTestingSetupForm.getEnvironmentId());
			query.executeUpdate();
			
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"delete from EnvironmentLoginScript where environmentId =:environmentId");
			query.setParameter("environmentId",
					uiTestingSetupForm.getEnvironmentId());
			query.executeUpdate();
			
			query = sessionFactory
					.getCurrentSession()
					.createSQLQuery(
							"delete from Environment where environmentId =:environmentId and environmentId not in "
									+ "(select environmentId from AppEnvEnvironmentCategoryXref)");
			query.setParameter("environmentId",
					uiTestingSetupForm.getEnvironmentId());
			query.executeUpdate();

			isDeleted = true;

		} catch (Exception e) {
			isDeleted = false;
			logger.error("Exception in deleteEnvironment :" + e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: deleteEnvironment");
		return isDeleted;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EnvironmentCategory> getEnvironmentCategoryList() {
		logger.debug("Entry: getEnvironmentCategoryList");
		Query query = null;

		List<EnvironmentCategory> environmentCategoryList = new ArrayList<EnvironmentCategory>();
		try {
			query = sessionFactory.getCurrentSession().createQuery(
					"from EnvironmentCategory order by environmentCategoryId");
			environmentCategoryList = query.list();

		} catch (Exception e) {
			logger.error("Exception in getEnvironmentCategoryList :" + e);
			logger.error("Stack Trace :"
					+ ExceptionUtils.getRootCauseMessage(e));
		}
		logger.debug("Exit: getEnvironmentCategoryList");

		return environmentCategoryList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EnvironmentCategory> getEnvironmentCategoryListWithoutAll() {
		logger.debug("Entry: getEnvironmentCategoryListWithoutAll");
		Query query = null;

		List<EnvironmentCategory> environmentCategoryList = new ArrayList<EnvironmentCategory>();
		try {
			query = sessionFactory.getCurrentSession().createQuery(
					" from EnvironmentCategory "
							+ " where environmentCategoryName <> :ALL "
							+ " order by environmentCategoryId");
			query.setParameter("ALL", "ALL");
			environmentCategoryList = query.list();

		} catch (Exception e) {
			logger.error("Exception in getEnvironmentCategoryListWithoutAll :"
					+ e);
			logger.error("Stack Trace :"
					+ ExceptionUtils.getRootCauseMessage(e));
		}
		logger.debug("Exit: getEnvironmentCategoryListWithoutAll");

		return environmentCategoryList;
	}

	@Override
	public boolean addEnvironmentCategoryXref(
			AppEnvEnvironmentCategoryXref appEnvEnvironmentCategoryXref) {
		logger.debug("Entry: addEnvironmentCategoryXref, environmentCategoryXref->"
				+ appEnvEnvironmentCategoryXref);
		boolean result = true;
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(
					appEnvEnvironmentCategoryXref);
		} catch (Exception e) {
			result = false;
			logger.error("Exception in addEnvironmentCategoryXref :" + e);
			logger.error("Stack Trace :"
					+ ExceptionUtils.getRootCauseMessage(e));
		}
		logger.debug("Exit: addEnvironmentCategoryXref");
		return result;
	}

	@SuppressWarnings("unchecked")
	public EnvironmentCategory getEnvironmentCategoryName(int environmentId) {
		logger.debug("Entry: getEnvironmentCategoryName");
		Query query = null;
		List<EnvironmentCategory> environmentCategoryList = new ArrayList<EnvironmentCategory>();
		EnvironmentCategory environmentCategory = new EnvironmentCategory();

		try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"select ec from EnvironmentCategory ec, AppEnvEnvironmentCategoryXref ecx where ecx.environmentId = :environmentId "
									+ "and ec.environmentCategoryId = ecx.environmentCategoryId");

			query.setParameter("environmentId", environmentId);
			environmentCategoryList = query.list();
			if (environmentCategoryList != null
					&& environmentCategoryList.size() > 0) {
				environmentCategory = environmentCategoryList.get(0);
			}
		} catch (Exception exception) {
			logger.error("Exception in getEnvironmentListForApplicationId :"
					+ exception);
		}
		logger.debug("Exit: getEnvironmentCategoryName");
		return environmentCategory;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Environment> getEnvironmentListForApplicationId(
			int applicationId) {

		Query query = null;
		List<Environment> environmentList = new ArrayList<Environment>();

		try {
			query = sessionFactory.getCurrentSession().createQuery(
					" select env from Environment as env, AppEnvEnvironmentCategoryXref x "
							+ " where env.environmentId = x.environmentId "
							+ " and x.applicationId = :applicationId ");
			query.setParameter("applicationId", applicationId);

			environmentList = query.list();

		} catch (Exception exception) {
			logger.error("Exception in getEnvironmentListForApplicationId :"
					+ exception);
		}
		return environmentList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Environment> getEnvironmentsByCategory(
			int environmentCategoryId, int applicationId) {
		logger.debug("Entry: getEnvironmentsByCategory");
		Query query = null;
		List<Environment> environmentList = new ArrayList<Environment>();
		try {

			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							" from Environment where environmentId in ( "
									+ " select x.environmentId from AppEnvEnvironmentCategoryXref as x "
									+ " where x.environmentCategoryId =:environmentCategoryId "
									+ " and x.applicationId =:applicationId )");

			query.setParameter("environmentCategoryId", environmentCategoryId);
			query.setParameter("applicationId", applicationId);
			environmentList.addAll(query.list());
		} catch (Exception e) {
			logger.error("Exception in getEnvironmentsByCategory :" + e);
			logger.error("Stack Trace :"
					+ ExceptionUtils.getRootCauseMessage(e));
		}
		logger.debug("Exit: getEnvironmentsByCategory");
		return environmentList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Environment> getSecureEnvironmentsByCategory(
			int environmentCategoryId, int applicationId) {
		logger.debug("Entry: getEnvironmentCategoryName");
		Query query = null;
		List<Environment> environment = new ArrayList<Environment>();
		try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							" from Environment  where secureSite = :isSecureSite and environmentId in ( "
									+ " select x.environmentId from AppEnvEnvironmentCategoryXref as x "
									+ " where x.applicationId =:applicationId "
									+ " and x.environmentCategoryId =:environmentCategoryId)");
			query.setParameter("isSecureSite", true);
			query.setParameter("applicationId", applicationId);
			query.setParameter("environmentCategoryId", environmentCategoryId);
			environment.addAll(query.list());
		} catch (Exception e) {
			logger.error("Exception in getEnvironmentsByCategory :" + e);
			logger.error("Stack Trace :"
					+ ExceptionUtils.getRootCauseMessage(e));
		}
		logger.debug("Exit: getEnvironmentCategoryName");
		return environment;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EnvironmentCategory> getEnvironmentCategory(int applicationId) {
		logger.debug("Entry: getEnvironmentCategory");
		Query query = null;
		List<AppEnvEnvironmentCategoryXref> environmentCategoryXrefXrefList = new ArrayList<AppEnvEnvironmentCategoryXref>();

		List<EnvironmentCategory> environmentCategoryList = new ArrayList<EnvironmentCategory>();
		EnvironmentCategory environmentCategory = new EnvironmentCategory();
		try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from AppEnvEnvironmentCategoryXref where applicationId =:applicationId");

			query.setParameter("applicationId", applicationId);

			environmentCategoryXrefXrefList = query.list();
			for (AppEnvEnvironmentCategoryXref appEnvEnvironmentCategoryXref : environmentCategoryXrefXrefList) {
				environmentCategory = getEnvironmentCategoryName(appEnvEnvironmentCategoryXref
						.getEnvironmentId());
				environmentCategoryList.add(environmentCategory);
			}

		} catch (Exception e) {
			logger.error("Exception in getEnvironmentsByCategory :" + e);
			logger.error("Stack Trace :"
					+ ExceptionUtils.getRootCauseMessage(e));
		}
		logger.debug("Exit: getEnvironmentCategory");
		return environmentCategoryList;
	}

	@Override
	public boolean isMachesWithExistingEnvironmentAndEnvrionmentCategory(
			int applicationId, int environmentId, int environmentCategoryId) {
		logger.debug("Entry: isEnvironmentCategoryAlreadyExistsForApplication, environmentId->"
				+ environmentId
				+ ", environmentCategoryId->"
				+ environmentCategoryId);
		Query query = null;
		boolean exists = true;

		try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from AppEnvEnvironmentCategoryXref ecx "
									+ "where (ecx.environmentId = :environmentId and ecx.environmentCategoryId != :environmentCategoryId) OR "
									+ "(ecx.environmentId != :environmentId and ecx.environmentCategoryId = :environmentCategoryId)");
			query.setParameter("environmentId", environmentId);
			query.setParameter("environmentCategoryId", environmentCategoryId);

			logger.debug(" query.list() :" + query.list());
			if (query.list().size() > 0) {

				exists = false;
			}
		} catch (Exception e) {
			logger.error("Exception in isEnvironmentCategoryAlreadyExistsForApplication.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: isEnvironmentCategoryAlreadyExistsForApplication");
		return exists;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Environment getEnvironment(int environmentId) {
		logger.debug("Entry: getEnvironment");
		Query query = null;
		List<Environment> environmentList = new ArrayList<Environment>();

		Environment environment = new Environment();
		try {
			query = sessionFactory.getCurrentSession().createQuery(
					"from Environment where environmentId = :environmentId");

			query.setParameter("environmentId", environmentId);

			environmentList = query.list();

			if (null != environmentList && environmentList.size() > 0) {
				environment = environmentList.get(0);
			}

		} catch (Exception e) {
			logger.error("Exception in getEnvironment :" + e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getEnvironment");
		return environment;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean saveEnvironmentGroupReference(
			EnvironmentCategoryGroupXref environmentCategoryGroupXref)
			throws Exception {
		logger.debug("Exit: saveEnvironmentGroupReference");
		boolean saved = false;
		Query query = null;
		List<EnvironmentCategoryGroupXref> environmentCategoryList = new ArrayList<EnvironmentCategoryGroupXref>();
		try{
			
		query = sessionFactory.getCurrentSession()
			.createQuery("from EnvironmentCategoryGroupXref where groupId=:groupId " +
					"and environmentCategoryId =:environmentCategoryId");
		query.setParameter("groupId", environmentCategoryGroupXref.getGroupId());
		query.setParameter("environmentCategoryId", environmentCategoryGroupXref.getEnvironmentCategoryId());
		environmentCategoryList = query.list(); 
			if(environmentCategoryList.size() <= 0){
				sessionFactory.getCurrentSession().save(environmentCategoryGroupXref);
				sessionFactory.getCurrentSession().flush();
				sessionFactory.getCurrentSession().clear();
			}	
		logger.info("Exit: Record already exist");
		saved = true;
		}  catch (Exception e) {
			saved = false;
			logger.error("Exception in saveEnvironmentGroupReference :" + e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: saveEnvironmentGroupReference");
		return saved;
	}

	@Override
	public boolean deleteEnvironmentGroupReference(int groupId) {
		logger.debug("Entry: deleteEnvironmentGroupReference");
		boolean result = true;
		try {
			Query query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"delete from EnvironmentCategoryGroupXref where groupId=:groupId");
			query.setParameter("groupId", groupId);
			query.executeUpdate();
		} catch (Exception e) {
			result = false;
			logger.error("Exception in deleteEnvironmentGroupReference :" + e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: deleteEnvironmentGroupReference");
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public EnvironmentCategory getEnvironmentCategoryForGroup(
			int environmentCategoryId) {
		logger.debug("Entry: getEnvironmentCategoryForGroup");
		Query query = null;
		List<EnvironmentCategory> environmentCategoryList = new ArrayList<EnvironmentCategory>();
		EnvironmentCategory environmentCategory = new EnvironmentCategory();
		try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"Select ec from EnvironmentCategory ec  where ec.environmentCategoryId = :environmentCategoryId");
			/*
			 * Select ec.* from EnvironmentCategory ec,
			 * AppEnvEnvironmentCategoryXref ecx,environmentgroupreference gg
			 * where ecx.environmentId = 2 and ecx.environmentId =
			 * gg.environmentId and ec.environmentCategoryId =
			 * ecx.environmentCategoryId and gg.groupId = 3
			 */

			query.setParameter("environmentCategoryId", environmentCategoryId);
			environmentCategoryList = query.list();
			if (environmentCategoryList != null
					&& environmentCategoryList.size() > 0) {
				environmentCategory = environmentCategoryList.get(0);
			}
		} catch (Exception e) {
			logger.error("Exception in getEnvironmentCategoryForGroup :" + e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getEnvironmentCategoryForGroup");
		return environmentCategory;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Environment getEnvironmentForApplicationCategory(int applicationId,
			int environmentcategoryId) {
		logger.debug("Entry: getEnvironmentForApplicationCategory");
		Query query = null;
		List<Environment> environmentList = new ArrayList<Environment>();

		Environment environment = new Environment();
		try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from Environment where environmentId in (select environmentId from AppEnvEnvironmentCategoryXref where applicationId=:applicationId and environmentcategoryId=:environmentcategoryId)");

			query.setParameter("applicationId", applicationId);
			query.setParameter("environmentcategoryId", environmentcategoryId);

			environmentList = query.list();

			if (null != environmentList && environmentList.size() > 0) {
				environment = environmentList.get(0);
			}

		} catch (Exception e) {
			logger.error("Exception in getEnvironmentForApplicationCategory :"
					+ e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getEnvironmentForApplicationCategory");
		return environment;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EnvironmentCategoryGroupXref> getEnvironmentCategoryGroupRef(
			int applicationId, int groupId) {
		logger.debug("Entry: getEnvironmentCategoryGroupRef");
		Query query = null;
		List<EnvironmentCategoryGroupXref> environmentCategoryList = new ArrayList<EnvironmentCategoryGroupXref>();

		try {
			/*query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"select ecx from EnvironmentCategoryGroupXref ecx where environmentCategoryId in (select environmentCategoryId from AppEnvEnvironmentCategoryXref "
									+ "where applicationId =:applicationId and environmentCategoryId = 1) and groupId =:groupId");
			
			query.setParameter("applicationId", applicationId);
			query.setParameter("groupId", groupId);
			environmentCategoryList = query.list();*/

			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"select ecx from EnvironmentCategoryGroupXref ecx where groupId =:groupId and "
									+ "environmentCategoryId in (select environmentCategoryId from AppEnvEnvironmentCategoryXref "
									+ "where applicationId =:applicationId)");

			query.setParameter("applicationId", applicationId);
			query.setParameter("groupId", groupId);
			environmentCategoryList.addAll(query.list());

		} catch (Exception e) {
			logger.error("Exception in getEnvironmentCategoryGroupRef :" + e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getEnvironmentCategory");
		return environmentCategoryList;
	}

	@Override
	public List<EnvironmentCategoryGroupXref> getEnvironmentCategoryGroupRefByGroupId(
			int groupId) {
		logger.debug("Entry: getEnvironmentCategoryGroupRefByGroupId");
		Query query = null;
		List<EnvironmentCategoryGroupXref> environmentCategoryList = new ArrayList<EnvironmentCategoryGroupXref>();
		try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from EnvironmentCategoryGroupXref where groupId =:groupId order by environmentCategoryId");
			query.setParameter("groupId", groupId);
			environmentCategoryList = query.list();

		} catch (Exception e) {
			logger.error("Exception in getEnvironmentsByCategory :" + e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getEnvironmentCategoryGroupRefByGroupId");
		return environmentCategoryList;
	}

	@Override
	public boolean addEnvEnvironmentCategoryXref(
			EnvEnvironmentCategoryXref envEnvironmentCategoryXref) {
		logger.debug("Entry: addEnvEnvironmentCategoryXref");
		boolean status = true;
		try {
			sessionFactory.getCurrentSession().save(envEnvironmentCategoryXref);
			sessionFactory.getCurrentSession().flush();
			sessionFactory.getCurrentSession().clear();
		} catch (Exception e) {
			status = false;
			logger.error("Exception in addEnvEnvironmentCategoryXref :" + e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: addEnvEnvironmentCategoryXref");
		return status;
	}
	
	@SuppressWarnings("unchecked")
	public EnvironmentCategory getEnvironmentCategoryById(int environmentCategoryId) {
		logger.debug("Entry: getEnvironmentCategoryName");
		Query query = null;
		List<EnvironmentCategory> environmentCategoryList = new ArrayList<EnvironmentCategory>();
		EnvironmentCategory environmentCategory = new EnvironmentCategory();

		try {
			query = sessionFactory.getCurrentSession().createQuery("from EnvironmentCategory where " +
							"environmentCategoryId =:environmentCategoryId");

			query.setParameter("environmentCategoryId", environmentCategoryId);
			environmentCategoryList = query.list();
			if (environmentCategoryList != null
					&& environmentCategoryList.size() > 0) {
				environmentCategory = environmentCategoryList.get(0);
			}
		} catch (Exception exception) {
			logger.error("Exception in getEnvironmentListForApplicationId :"
					+ exception);
		}
		logger.debug("Exit: getEnvironmentCategoryName");
		return environmentCategory;
	}
	
}
