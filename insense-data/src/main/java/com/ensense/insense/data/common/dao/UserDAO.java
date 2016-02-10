package com.ensense.insense.data.common.dao;

import java.util.List;
import java.util.Map;

import com.cts.mint.common.entity.ApplicationGroupReference;
import com.cts.mint.common.entity.EnvironmentCategoryGroupXref;
import com.cts.mint.common.entity.Groups;
import com.cts.mint.common.entity.Users;
import com.cts.mint.common.model.ManageUsers;


public interface UserDAO {
	public Users getMintUser(String userid);

	public String getGroupName(int groupId);

	public List<ManageUsers> getAllMintUsersWithGroup();
	
	//public boolean addGroupAndUserDetails(Users user);
	
	public boolean isGroupIdExists(int groupId);
	
	public Groups getGroup(int groupId);
	
	public boolean addUserDetails(Users user) throws Exception;
	
	public boolean addGroupDetails(Groups group) throws Exception;
	
	public boolean isUserNameExists(String userName);
	
	public List<Users> getAllUsersDetails();
	
	public List<Groups> getAllGroupsDetails();
	
	public Users getUser(int userId);
	
	public boolean deleteUserModule(Users user);

	public Users getMintUserById(int userId);
	
	public List<Groups> getAllGroupsOrderByName();
	
	public List<Users> getUsersDetailsWithGroupId(int groupId);

	public Map<Integer, Users> getMintUserMapById(String userIds);
	
}
