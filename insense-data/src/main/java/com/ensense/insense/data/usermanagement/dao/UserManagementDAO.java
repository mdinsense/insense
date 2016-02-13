package com.ensense.insense.data.usermanagement.dao;

import com.ensense.insense.data.common.entity.FunctionalityGroupRef;
import com.ensense.insense.data.common.entity.Groups;
import com.ensense.insense.data.common.entity.UserGroupMenuReference;

import java.util.List;

public interface UserManagementDAO {
	public List<Groups> getGroups();

	public boolean saveUserMenuAccess(int groupId, Integer menuId);

	public boolean deleteExistingUserMenuAccess(int groupId);

	public List<UserGroupMenuReference> getUserMenuAccessDetails(int groupId);

	public boolean saveFuncMenuAccess(int groupId, Integer functionalityId);

	public List<FunctionalityGroupRef> getFunctionalityGroupRef(int groupId);

	public boolean deleteExistingFunctionalityAccess(int groupId);
}
