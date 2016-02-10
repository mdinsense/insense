package com.ensense.insense.services.common.impl;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cts.mint.common.dao.MenuDAO;
import com.cts.mint.common.dao.UserDAO;
import com.cts.mint.common.entity.ApplicationGroupReference;
import com.cts.mint.common.entity.EnvironmentCategoryGroupXref;
import com.cts.mint.common.entity.Groups;
import com.cts.mint.common.entity.Users;
import com.cts.mint.common.model.ManageUsers;
import com.cts.mint.common.model.MenuAccess;
import com.cts.mint.common.service.UserService;
import com.cts.mint.uitesting.dao.ApplicationDAO;
import com.cts.mint.uitesting.dao.EnvironmentDAO;


@Service
public class UserServiceImpl implements UserService {
	private static Logger logger = Logger.getLogger(UserServiceImpl.class);
	@Autowired
	UserDAO userDao;
	
	@Autowired
	MenuDAO menuDao;
	
	@Autowired
	ApplicationDAO applicationDAO;
	
	@Autowired
	EnvironmentDAO environmentDAO;
	
	@Override
	@Transactional
	public Users getMintUser(String userName) {
		//logger.debug("Entry and Exit :getMintUser");
		return userDao.getMintUser(userName);
	}

	@Override
	@Transactional
	public String getGroupName(int groupId) {
		logger.debug("Entry and Exit :getGroupName");
		return userDao.getGroupName(groupId);
	}

	@Override
	@Transactional
	public List<ManageUsers> getAllMintUsersWithGroup() {
		logger.debug("Entry and Exit :getAllMintUsersWithGroup");
		return userDao.getAllMintUsersWithGroup();
	}

	@Override
	@Transactional
	public List<MenuAccess> reteiveMenusAccessForGroup(int groupId) {
		logger.debug("Entry and Exit :reteiveMenusAccessForGroup");
		return menuDao.reteiveMenusAccessForGroup(groupId);
	}
	
	/*public boolean addGroupAndUserDetails(Users user){
		logger.debug("Entry and Exit :addGroupAndUserDetails");
		return userDao.addGroupAndUserDetails(user);
	}*/
	@Override
	@Transactional
	public boolean addGroupAndUserDetails(Users user){
		logger.debug("Entry: service:addGroupAndUserDetails");
		boolean result = false;
		try {
			//Adding Group Details first
			userDao.addGroupDetails(user.getGroup());
			
			//Adding user Details with help of groupId
			if(user.getGroup().getGroupId() > 0){
				user.setGroupId(user.getGroup().getGroupId());
				userDao.addUserDetails(user);
				result = true;
			}
		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while Saving GroupMappingSetup details");
			logger.error("Stack Trace :"
					+ ExceptionUtils.getStackTrace(e));
			return false;
		}
		logger.debug("Exit: service:addGroupAndUserDetails");
		return result;
	}
	
	@Override
	@Transactional
	public boolean isGroupIdExists(int groupId){
		logger.debug("Entry and Exit :isGroupIdExists");
		return userDao.isGroupIdExists(groupId);
	}
	
	@Override
	@Transactional
	public Groups getGroup(int groupId){
		logger.debug("Entry and Exit :getGroup");
		return userDao.getGroup(groupId);
	}
	
	@Override
	@Transactional
	public boolean addUserDetails(Users user) throws Exception{
		logger.debug("Entry and Exit :addUserDetails");
		return userDao.addUserDetails(user);
	}
	
	@Override
	@Transactional
	public boolean addGroupDetails(Groups group) throws Exception{
		logger.debug("Entry and Exit :addGroupDetails");
		return userDao.addGroupDetails(group);
	}
	
	@Override
	@Transactional
	public boolean isUserNameExists(String userName){
		logger.debug("Entry and Exit :isGroupIdExists");
		return userDao.isUserNameExists(userName);
	}
	
	@Override
	@Transactional
	public List<Users> getAllUsersDetails(){
		logger.debug("Entry and Exit :getAllUsersDetails");
		return userDao.getAllUsersDetails();
	}
	
	@Override
	@Transactional
	public List<Groups> getAllGroupsDetails(){
		logger.debug("Entry and Exit :getAllGroupsDetails");
		return userDao.getAllGroupsDetails();
	}
	
	@Override
	@Transactional
	public Users getUser(int userId){
		//logger.debug("Entry and Exit :getUser");
		return userDao.getUser(userId);	
	}
	
	@Override
	@Transactional
	public boolean deleteUserModule(Users user){
		logger.debug("Entry and Exit :deleteUserModule");
		return userDao.deleteUserModule(user);
	}

	@Override
	@Transactional
	public Users getMintUserById(int userId) {
		logger.debug("Entry and Exit :reteiveMenusAccessForGroup");
		return userDao.getMintUserById(userId);
	}
	
	@Override
	@Transactional
	public boolean addGroupMappingSetup(int groupId,
			List<ApplicationGroupReference> applicationGroupReferenceList,
			List<EnvironmentCategoryGroupXref> environmentGroupReferenceList){
		logger.debug("Entry: service:addGroupMappingSetup");
		boolean result = false;
		try {
			
			applicationDAO.deleteApplicationGroupReference(groupId);
			environmentDAO.deleteEnvironmentGroupReference(groupId);
			
			for (ApplicationGroupReference applicationReference : applicationGroupReferenceList) {
				applicationReference.setGroupId(groupId);
				applicationDAO.saveApplicationGroupReference(applicationReference);
			}
			
			for (EnvironmentCategoryGroupXref environmentReference : environmentGroupReferenceList) {
				environmentReference.setGroupId(groupId);
				environmentDAO.saveEnvironmentGroupReference(environmentReference);
			}
			result = true;
		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while Saving GroupMappingSetup details");
			logger.error("Stack Trace :"
					+ ExceptionUtils.getStackTrace(e));
			return false;
		}
		logger.debug("Exit: service:addGroupMappingSetup");
		return result;
		
	}
	
	@Override
	@Transactional
	public List<Groups> getAllGroupsOrderByName(){
		logger.debug("Entry and Exit :getAllGroupsOrderByName");
		return userDao.getAllGroupsOrderByName();
	}
	
	@Override
	@Transactional
	public List<Users> getUsersDetailsWithGroupId(int groupId){
		logger.debug("Entry and Exit :getUsersDetailsWithGroupId");
		return userDao.getUsersDetailsWithGroupId(groupId);
	}
	
}
