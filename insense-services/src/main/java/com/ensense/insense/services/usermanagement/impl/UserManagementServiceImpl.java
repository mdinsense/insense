package com.ensense.insense.services.usermanagement.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cts.mint.common.entity.FunctionalityGroupRef;
import com.cts.mint.common.entity.Groups;
import com.cts.mint.common.entity.UserGroupMenuReference;
import com.cts.mint.usermanagement.dao.UserManagementDAO;
import com.cts.mint.usermanagement.service.UserManagementService;

@Service
public class UserManagementServiceImpl implements UserManagementService{
	private static Logger logger = Logger.getLogger(UserManagementServiceImpl.class);
	
	@Autowired
	UserManagementDAO userManagementDAO;
	
	@Override
	@Transactional
	public List<Groups> getGroups() {
		logger.debug("Entry and Exit :getGroups");
		return userManagementDAO.getGroups();
	}

	@Override
	@Transactional
	public boolean saveUserMenuAccess(int groupId, Integer menuId) {
		logger.debug("Entry and Exit :saveUserMenuAccess");
		return userManagementDAO.saveUserMenuAccess(groupId, menuId);
	}

	@Override
	@Transactional
	public boolean deleteExistingUserMenuAccess(int groupId) {
		logger.debug("Entry and Exit :deleteExistingUserMenuAccess");
		return userManagementDAO.deleteExistingUserMenuAccess(groupId);
	}
	
	@Override
	@Transactional
	public boolean deleteExistingFunctionalityAccess(int groupId) {
		logger.debug("Entry and Exit :deleteExistingFunctionalityAccess");
		return userManagementDAO.deleteExistingFunctionalityAccess(groupId);
	}

	@Override
	@Transactional
	public List<UserGroupMenuReference> getUserMenuAccessDetails(int groupId) {
		logger.debug("Entry and Exit :getUserMenuAccessDetails");
		return userManagementDAO.getUserMenuAccessDetails(groupId);
	}

	@Override
	@Transactional
	public boolean saveFuncMenuAccess(int groupId, Integer functionalityId) {
		logger.debug("Entry and Exit :saveFuncMenuAccess");
		return userManagementDAO.saveFuncMenuAccess(groupId, functionalityId );
	}

	@Override
	@Transactional
	public List<FunctionalityGroupRef> getFunctionalityGroupRef(int groupId) {
		logger.debug("Entry and Exit :getFunctionalityGroupRef");
		return userManagementDAO.getFunctionalityGroupRef(groupId);
	}

}
