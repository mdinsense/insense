package com.ensense.insense.services.usermanagement;

import java.util.List;

import com.cts.mint.common.entity.FunctionalityGroupRef;
import com.cts.mint.common.entity.Groups;
import com.cts.mint.common.entity.UserGroupMenuReference;

public interface UserManagementService{
	public List<Groups> getGroups();

	public boolean saveUserMenuAccess(int groupId, Integer menuId);

	public boolean deleteExistingUserMenuAccess(int groupId);

	public List<UserGroupMenuReference> getUserMenuAccessDetails(int groupId);

	public boolean saveFuncMenuAccess(int groupId, Integer functionalityId);
	
	public List<FunctionalityGroupRef> getFunctionalityGroupRef(int groupId);

	public boolean deleteExistingFunctionalityAccess(int groupId);
}
