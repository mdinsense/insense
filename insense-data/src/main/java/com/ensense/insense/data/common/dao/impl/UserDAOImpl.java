package com.ensense.insense.data.common.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cts.mint.common.dao.UserDAO;
import com.cts.mint.common.entity.Groups;
import com.cts.mint.common.entity.Users;
import com.cts.mint.common.model.ManageUsers;

@Service
public class UserDAOImpl implements UserDAO {
	private static Logger logger = Logger.getLogger(UserDAOImpl.class);
	@Autowired
	SessionFactory sessionFactory;
	
	@SuppressWarnings("unchecked")
	@Override
	public Users getMintUser(String userName) {
		//logger.debug("Entry: getMintUser");
		Query query = null;
		List<Users> mintUserList = new ArrayList<Users>();
		Users mintUser = new Users();

		try {
			query = this.sessionFactory.getCurrentSession().createQuery(
					"from  Users where userName = :userName");
		} catch (Exception e) {
			logger.error("Exception in getMintUser:" + e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		query.setString("userName", userName);

		mintUserList = query.list();

		if (null != mintUserList && mintUserList.size() > 0) {
			mintUser = mintUserList.get(0);
		}

		//logger.debug("Exit: getMintUser");
		return mintUser;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getGroupName(int groupId) {
		logger.debug("Entry: getGroupName");
		List<Groups> groupTableList = new ArrayList<Groups>();
		String groupName = "";
		try {

			Query query = sessionFactory.getCurrentSession().createQuery(
					"from Groups where groupId = :groupId");
			query.setParameter("groupId", groupId);
			groupTableList = query.list();

			if (null != groupTableList && groupTableList.size() > 0) {
				groupName = groupTableList.get(0).getGroupName();
			}
		} catch (Exception e) {
			logger.error("Exception in getGroupName:" + e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Entry: getGroupName");
		return groupName;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ManageUsers> getAllMintUsersWithGroup() {
		logger.debug("Entry: getAllMintUsersWithGroup");
		Query query = null;
		List<ManageUsers> manageUserList = new ArrayList<ManageUsers>();
		try {
			query = this.sessionFactory
					.getCurrentSession()
					.createQuery(
							"select u.userName, g.groupName from  Users u, Groups g where u.groupId = g.groupId");

			List<Object> result = query.list();

			for (Iterator<Object> i = result.iterator(); i.hasNext();) {
				Object[] values = (Object[]) i.next();
				ManageUsers manageUser = new ManageUsers();

				manageUser.setUserName(values[0].toString());
				manageUser.setGroupName(values[1].toString());

				manageUserList.add(manageUser);
			}

		} catch (HibernateException he) {
			logger.error("Exception in getAllMintUsersWithGroup :" + he);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(he));
		}
		logger.debug("Exit: getAllMintUsersWithGroup");
		return manageUserList;
	}

	/**
	 * checks the application record based on application name
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean isGroupIdExists(int groupId) {
		logger.debug("Entry: isGroupIdExists");

		boolean isGroupIdExists = false;
		List<Groups> groupList = new ArrayList<Groups>();
		String sqlQuery = "";

		try {
			sqlQuery = "from Groups where  groupId =:groupId";
			Query query = sessionFactory.getCurrentSession().createQuery(
					sqlQuery);
			query.setParameter("groupId", groupId);
			groupList = query.list();
			if (null != groupList && groupList.size() > 0) {
				isGroupIdExists = true;
			}

		} catch (Exception exception) {
			logger.error("Exception in getGroups :" + exception);
		}

		logger.debug("Exit: isGroupIdExists");
		return isGroupIdExists;
	}
	
	/**
	 * save groups details to Groups table
	 */
	/*@Override
	public boolean addGroupAndUserDetails(Users user){
		logger.debug("Entry: addGroupAndUserDetails");
		boolean added = false;

		try {
			sessionFactory.getCurrentSession().saveOrUpdate(user.getGroup());
			if(user.getGroup().getGroupId() > 0){
				user.setGroupId(user.getGroup().getGroupId());
				this.addUserDetailsWithGroup(user);
				added = true;
			}
		} catch (Exception e) {
			logger.error("Exception in addGroupAndUserDetails.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}

		logger.debug("Exit: addGroupAndUserDetails");
		return added;
	}
	
	public boolean addUserDetailsWithGroup(Users user) {
		logger.info("Entry: addUserDetailsWithGroup, user->"+user);
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(user);
		} catch (Exception e) {
			logger.error("Exception in addUserDetailsWithGroup :"+e);
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
		}
		logger.info("Exit: addUserDetailsWithGroup");
		return false;
	}*/
	
	/**
	 * get the group record based on group id
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Groups getGroup(int groupId) {
		//logger.debug("Entry: getGroup");
		Groups groupDetails = new Groups();
		List<Groups> groupsList = new ArrayList<Groups>();

		String sqlQuery = "";
		try {

			sqlQuery = "from Groups where groupId = :groupId";
			Query query = sessionFactory.getCurrentSession().createQuery(
					sqlQuery);
			query.setParameter("groupId", groupId);
			groupsList = query.list();
			if (null != groupsList && groupsList.size() > 0) {
				groupDetails = groupsList.get(0);
			}

		} catch (Exception e) {
			logger.error("Exception in getGroup :" + e);
		}

		//logger.debug("Exit: getGroup");
		return groupDetails;
	}
	
	/**
	 * save user details to Users table
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean addUserDetails(Users user) throws Exception{
		boolean result = false;
		logger.debug("Entry: addUserDetails");

			sessionFactory.getCurrentSession().saveOrUpdate(user);
			result = true;

		logger.debug("Exit: addUserDetails");
		return result;
	}
	
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean addGroupDetails(Groups group) throws Exception{
		boolean result = false;
		logger.debug("Entry: addGroupDetails");
		
			sessionFactory.getCurrentSession().saveOrUpdate(group);
			result = true;

		logger.debug("Exit: addGroupDetails");
		return result;
	}
	
	/**
	 * checks the user record based on user name
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean isUserNameExists(String userName){
		logger.debug("Entry: isUserNameExists");

		boolean isUserNameExists = false;
		List<Users> userList = new ArrayList<Users>();
		String sqlQuery = "";

		try {
			sqlQuery = "from Users where  userName =:userName";
			Query query = sessionFactory.getCurrentSession().createQuery(
					sqlQuery);
			query.setParameter("userName", userName);
			userList = query.list();
			if (null != userList && userList.size() > 0) {
				isUserNameExists = true;
			}

		} catch (Exception exception) {
			logger.error("Exception in getUsers :" + exception);
		}

		logger.debug("Exit: isUserNameExists");
		return isUserNameExists;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Users> getAllUsersDetails() {
		logger.debug("Entry: getAllUsersDetails");
		Query query = null;
		List<Users> userList = new ArrayList<Users>();
		try {
			query = sessionFactory.getCurrentSession().createQuery(
					"from Users order by userId");
			userList = query.list();
		}

		catch (Exception e) {
			logger.error("Exception in getAllUsersDetails :" + e);
			logger.error("Stack Trace :"
					+ ExceptionUtils.getRootCauseMessage(e));
		}
		logger.debug("Exit: getAllUsersDetails");

		return userList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Groups> getAllGroupsDetails() {
		logger.debug("Entry: getAllGroupsDetails");
		Query query = null;
		List<Groups> groupList = new ArrayList<Groups>();
		try {
			query = sessionFactory.getCurrentSession().createQuery(
					"from Groups order by groupId");
			groupList = query.list();
		}

		catch (Exception e) {
			logger.error("Exception in getAllGroupsDetails :" + e);
			logger.error("Stack Trace :"
					+ ExceptionUtils.getRootCauseMessage(e));
		}
		logger.debug("Exit: getAllGroupsDetails");

		return groupList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Groups> getAllGroupsOrderByName() {
		logger.debug("Entry: getAllGroupsDetails");
		Query query = null;
		List<Groups> groupList = new ArrayList<Groups>();
		try {
			query = sessionFactory.getCurrentSession().createQuery(
					"from Groups order by groupName");
			groupList = query.list();
		}

		catch (Exception e) {
			logger.error("Exception in getAllGroupsDetails :" + e);
			logger.error("Stack Trace :"
					+ ExceptionUtils.getRootCauseMessage(e));
		}
		logger.debug("Exit: getAllGroupsDetails");

		return groupList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Users getUser(int userId) {
		//logger.debug("Entry: getUser");
		Query query = null;
		List<Users> userList = new ArrayList<Users>();
		Users user = new Users();

		try {
			query = this.sessionFactory.getCurrentSession().createQuery(
					"from  Users where userId = :userId");
		} catch (Exception e) {
			logger.error("Exception in getUser:" + e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		query.setInteger("userId", userId);

		userList = query.list();

		if (null != userList && userList.size() > 0) {
			user = userList.get(0);
		}

		//logger.debug("Exit: getUser");
		return user;
	}
	
	@Override
	public boolean deleteUserModule(Users user) {
		logger.debug("Entry: deleteUserModule");
		boolean result = true;
		try {
			Query query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"delete from Users where userId=:userId");
			query.setParameter("userId", user.getUserId());
			query.executeUpdate();
		} catch (Exception e) {
			result = false;
			logger.error("Exception in deleteUserModule :" + e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: deleteUserModule");
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Users getMintUserById(int userId) {
		logger.debug("Entry: getMintUser");
		Query query = null;
		List<Users> mintUserList = new ArrayList<Users>();
		Users mintUser = new Users();

		try {
			query = this.sessionFactory.getCurrentSession().createQuery(
					"from  Users where userId = :userId");
		} catch (Exception e) {
			logger.error("Exception in getMintUser:" + e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		query.setParameter("userId", userId);

		mintUserList = query.list();

		if (null != mintUserList && mintUserList.size() > 0) {
			mintUser = mintUserList.get(0);
		}

		logger.debug("Exit: getMintUser");
		return mintUser;
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public Map<Integer, Users> getMintUserMapById(String userIds) {
		logger.debug("Entry: getMintUserMapById");
		Query query = null;
		List<Users> mintUserList = new ArrayList<Users>();
		Users mintUser = new Users();
		Map<Integer, Users> userMap = new HashMap<Integer, Users>();
		
		try {
			query = this.sessionFactory.getCurrentSession().createQuery(
					"from  Users where userId in ("+ userIds +")");
		} catch (Exception e) {
			logger.error("Exception in getMintUser:" + e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		//query.setParameter("userIdString", userIds);
		mintUserList = query.list();

		if (null != mintUserList && mintUserList.size() > 0) {
			for(Users user : mintUserList) {
				userMap.put(user.getUserId(), user);
			}
		}

		logger.debug("Exit: getMintUserMapById");
		return userMap;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Users> getUsersDetailsWithGroupId(int groupId){
		logger.debug("Entry: getUsersDetailsWithGroupId");
		Query query = null;
		List<Users> userList = new ArrayList<Users>();
		try {
			query = sessionFactory.getCurrentSession().createQuery(
					" from Users where groupId =:groupId " +
					" order by userId");
			
			query.setParameter("groupId", groupId);
			userList = query.list();
		}

		catch (Exception e) {
			logger.error("Exception in getUsersDetailsWithGroupId :" + e);
			logger.error("Stack Trace :"
					+ ExceptionUtils.getRootCauseMessage(e));
		}
		logger.debug("Exit: getUsersDetailsWithGroupId");

		return userList;
	}

}
