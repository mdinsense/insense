package com.ensense.insense.data.common;

import com.ensense.insense.data.common.entity.ApplicationGroupReference;
import com.ensense.insense.data.common.entity.EnvironmentCategoryGroupXref;
import com.ensense.insense.data.common.entity.Groups;
import com.ensense.insense.data.common.entity.Users;
import com.ensense.insense.data.common.model.ManageUsers;
import com.ensense.insense.data.common.model.MenuAccess;

import java.util.List;


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
