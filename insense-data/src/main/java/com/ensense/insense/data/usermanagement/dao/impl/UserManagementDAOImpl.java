package com.ensense.insense.data.usermanagement.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.mint.common.entity.FunctionalityGroupRef;
import com.cts.mint.common.entity.Groups;
import com.cts.mint.common.entity.UserGroupMenuReference;
import com.cts.mint.usermanagement.dao.UserManagementDAO;

@Service
public class UserManagementDAOImpl implements UserManagementDAO{
	private static Logger logger = Logger.getLogger(UserManagementDAOImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Groups> getGroups() {
		logger.debug("Entry: getGroups");
		List<Groups> groupList = new ArrayList<Groups>();
		Query query = null;
		try {
			query = sessionFactory.getCurrentSession().createQuery(
					"from Groups order by groupName");

			groupList = query.list();
		} catch (Exception e) {
			logger.error("Exception in getGroups");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getGroups");
		return groupList;
	}

	@Override
	public boolean saveUserMenuAccess(int groupId, Integer menuId) {
		logger.debug("Entry: saveUserMenuAccess");
		boolean insertStatus = false;

		try {
			UserGroupMenuReference userGroupMenuReference = new UserGroupMenuReference();
			userGroupMenuReference.setGroupId(groupId);
			userGroupMenuReference.setMenuId(menuId);
			sessionFactory.getCurrentSession().save(userGroupMenuReference);
			insertStatus = true;
		} catch (Exception e) {
			logger.error("Exception in saveUserMenuAccess");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
			insertStatus = false;
		}
		logger.debug("Exit: saveUserMenuAccess");
		return insertStatus;
	}

	@Override
	public boolean deleteExistingUserMenuAccess(int groupId) {
		logger.debug("Entry: deleteExistingUserMenuAccess");
		
		boolean deleteStatus = false;
		try {
			Query delRoleMenuDetails = sessionFactory
			.getCurrentSession()
			.createQuery("delete from UserGroupMenuReference where groupId=:groupId");
			
			delRoleMenuDetails.setParameter("groupId", groupId);
			delRoleMenuDetails.executeUpdate();
			deleteStatus = true;
			
		} catch(Exception e) {
			logger.error("Exception in deleteExistingUserMenuAccess");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
			deleteStatus = false;
		}
		logger.debug("Exit: deleteExistingUserMenuAccess");
		return deleteStatus;
	}

	@Override
	public List<UserGroupMenuReference> getUserMenuAccessDetails(int groupId) {
		logger.debug("Entry: getUserMenuAccessDetails");
		
		List<UserGroupMenuReference> userGroupMenuReferenceList = null;
		Query query = null;
		try {
			query = sessionFactory.getCurrentSession().createQuery(
			"from UserGroupMenuReference where groupId= :groupId ");
			query.setParameter("groupId", groupId);
			userGroupMenuReferenceList = new ArrayList<UserGroupMenuReference>();
			userGroupMenuReferenceList = query.list();
			
		} catch (Exception e) {
			logger.error("Exception in getUserMenuAccessDetails");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getUserMenuAccessDetails");
		return userGroupMenuReferenceList;
	}

	@Override
	public boolean saveFuncMenuAccess(int groupId, Integer functionalityId) {
		logger.debug("Entry: saveFuncMenuAccess");
		boolean insertStatus = false;

		try {
			FunctionalityGroupRef functionalityGroupRef = new FunctionalityGroupRef();
			functionalityGroupRef.setGroupId(groupId);
			functionalityGroupRef.setFunctionalityId(functionalityId);
			sessionFactory.getCurrentSession().save(functionalityGroupRef);
			insertStatus = true;
		} catch (Exception e) {
			logger.error("Exception in saveFuncMenuAccess");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
			insertStatus = false;
		}
		logger.debug("Exit: saveFuncMenuAccess");
		return insertStatus;
	}
	
	@Override
	public List<FunctionalityGroupRef> getFunctionalityGroupRef(int groupId) {
		logger.debug("Entry: getFunctionalityGroupRef");
		
		List<FunctionalityGroupRef> functionalityGroupRefList = null;
		Query query = null;
		try {
			query = sessionFactory.getCurrentSession().createQuery(
			"from FunctionalityGroupRef where groupId= :groupId ");
			query.setParameter("groupId", groupId);
			functionalityGroupRefList = new ArrayList<FunctionalityGroupRef>();
			functionalityGroupRefList = query.list();
			
		} catch (Exception e) {
			logger.error("Exception in getFunctionalityGroupRef");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getFunctionalityGroupRef");
		return functionalityGroupRefList;
	}

	@Override
	public boolean deleteExistingFunctionalityAccess(int groupId) {
	logger.debug("Entry: deleteExistingFunctionalityAccess");
		boolean deleteStatus = false;
		try {
			Query delRoleMenuDetails = sessionFactory
			.getCurrentSession()
			.createQuery("delete from FunctionalityGroupRef where groupId=:groupId");
			delRoleMenuDetails.setParameter("groupId", groupId);
			delRoleMenuDetails.executeUpdate();
			deleteStatus = true;
			
		} catch(Exception e) {
			logger.error("Exception in deleteExistingFunctionalityAccess");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
			deleteStatus = false;
		}
		logger.debug("Exit: deleteExistingFunctionalityAccess");
		return deleteStatus;
	}
}
