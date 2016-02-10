package com.ensense.insense.services.common;

import java.util.List;

import com.cts.mint.common.entity.ApplicationGroupReference;
import com.cts.mint.common.entity.EnvironmentCategoryGroupXref;
import com.cts.mint.common.entity.Groups;
import com.cts.mint.common.entity.Users;
import com.cts.mint.common.model.ManageUsers;
import com.cts.mint.common.model.MenuAccess;


public interface UserService {
	public Users getMintUser(String userName);

	public String getGroupName(int groupId);

	public List<ManageUsers> getAllMintUsersWithGroup();

	public List<MenuAccess> reteiveMenusAccessForGroup(int groupId);
	
	public boolean addGroupAndUserDetails(Users user);
	
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
	
	public boolean addGroupMappingSetup(int groupId,
										List<ApplicationGroupReference> applicationGroupReferenceList,
										List<EnvironmentCategoryGroupXref> environmentGroupReferenceList);
	
	public List<Groups> getAllGroupsOrderByName();
	
	public List<Users> getUsersDetailsWithGroupId(int groupId);
}
