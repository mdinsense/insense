package com.ensense.insense.data.common.dao.impl;

import com.ensense.insense.data.common.dao.MenuDAO;
import com.ensense.insense.data.common.entity.FunctionalityPermission;
import com.ensense.insense.data.common.entity.Menu;
import com.ensense.insense.data.common.entity.UserGroupMenuReference;
import com.ensense.insense.data.common.model.FunctionalityForGroup;
import com.ensense.insense.data.common.model.MenuAccess;
import com.ensense.insense.data.common.model.uiadmin.form.UserManagementForm;
import com.ensense.insense.data.miscellaneous.entity.MiscellaneousTool;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


@Service
public class MenuDAOImpl implements MenuDAO {
	private static Logger logger = Logger.getLogger(MenuDAOImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public int getMenuIdForAction(String menuAction) {
		logger.debug("Entry: getMenuIdForAction");
		Query query = null;

		int menuId = 0;
		try {
			query = sessionFactory.getCurrentSession().createQuery(
					" from Menu m " + " where menuAction =:menuAction");
			query.setParameter("menuAction", menuAction);

			@SuppressWarnings("unchecked")
			List<Menu> menuList = query.list();
			for (Menu menu : menuList) {
				menuId = menu.getMenuId();
			}
		} catch (Exception exp) {
			logger.error(exp);
		}
		logger.debug("Exit: getMenuIdForAction");
		return menuId;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean checkGroupHasAccessToMenu(int groupId, int menuId) {
		logger.debug("Entry: checkGroupHasAccessToMenu");
		Query query = null;
		boolean result = false;
		List<UserGroupMenuReference> userGroupMenuRef = new ArrayList<UserGroupMenuReference>();

		try {

			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from UserGroupMenuRefTable where menuId = :menuId and groupId = :groupId");
			query.setParameter("groupId", groupId);

			query.setParameter("menuId", menuId);

			userGroupMenuRef = query.list();

			if (userGroupMenuRef != null && userGroupMenuRef.size() > 0) {
				result = true;
			} else {
				result = false;
			}
		} catch (Exception exp) {
			logger.error("Exception in checkGroupHasAccessToMenu :" + exp);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(exp));
			result = false;
		}
		logger.debug("Exit: checkGroupHasAccessToMenu");
		return result;

	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<FunctionalityForGroup> getFunctionalityForGroup(int groupId) {
		logger.debug("Entry: getFunctionalityForGroup");
		List<FunctionalityForGroup> functionalityForGroupList = new ArrayList<FunctionalityForGroup>();
		Query query = null;
		try {
			query = sessionFactory.getCurrentSession().createQuery(
					"select fp.functionalityId, fp.functionalityName, fp.menuId, fgr.groupId "
					+ "from FunctionalityPermission fp, FunctionalityGroupRef fgr where fp.functionalityId = fgr.functionalityId and fgr.groupId = :groupId");
			query.setParameter("groupId",groupId);
			List<Object> result = query.list();
			for (Iterator<Object> i = result.iterator(); i.hasNext();) {  
				  Object[] values = (Object[]) i.next();
				  FunctionalityForGroup functionalityForGroup = new FunctionalityForGroup();
				  functionalityForGroup.setFunctionalityId(Integer.parseInt(values[0].toString()));
				  functionalityForGroup.setFunctionalityName(values[1].toString());
				  functionalityForGroup.setMenuId(Integer.parseInt(values[2].toString()));
				  functionalityForGroup.setGroupId(Integer.parseInt(values[3].toString()));
				  functionalityForGroupList.add(functionalityForGroup);
			}
		} catch (Exception exp) {
			logger.error("Exception in getFunctionalityForGroup :"+exp);
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(exp));
		}
		logger.debug("Exit: getFunctionalityForGroup");
		return functionalityForGroupList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean addNewMenu(UserManagementForm userManagementForm) {
		boolean insertStatus = false;
		Query query = null;
		List<Menu> menuList = new ArrayList<Menu>();
		try {
			query = sessionFactory.getCurrentSession().createQuery(
					"FROM Menu menu where parentMenuId =:parentMenuId ");
			query.setParameter("parentMenuId",userManagementForm.getParentMenuId());
			menuList = query.list();
			Menu menu = new Menu();
			menu.setMenuName(userManagementForm.getMenuName());
			menu.setMenuAction(userManagementForm.getActionUrl());
			menu.setMenuOrder(menuList.size()+1);
			menu.setDateCreated(new Date());
			menu.setDateModified(new Date());
			menu.setParentMenuId(userManagementForm.getParentMenuId());
			sessionFactory.getCurrentSession().save(menu);
			insertStatus = true;
		} catch (Exception exp) {
			insertStatus = false;
			logger.error("Exception in addNewMenu :" +exp);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(exp));
		}
		logger.info("Exit: addNewMenu");
		return insertStatus;
	}

	@Override
	public List<MenuAccess> reteiveMenusAccessForGroup(int groupId) {
		logger.info("Entry: reteiveMenusAccessForGroup");

		List<MenuAccess> menuAccessList = new ArrayList<MenuAccess>();
		Query query = null;
		try {
			query = sessionFactory.getCurrentSession().createSQLQuery(
			"select m.menuId, m.menuName, m.menuAction, m.parentMenuId, u.groupId "
			+ "from Menu m LEFT OUTER JOIN userGroupMenuReference u on m.menuId = u.menuId "
			+ "and u.groupId = :groupId order by m.menuOrder");
			
			query.setParameter("groupId", groupId);

			@SuppressWarnings("unchecked")
			List<Object> result = query.list();
            
			for (Iterator<Object> i = result.iterator(); i.hasNext();) {  
			    Object[] values = (Object[]) i.next();
			    MenuAccess menuAccess = new MenuAccess();
			    
			    menuAccess.setMenuId(Integer.parseInt(values[0].toString()));
			    menuAccess.setMenuName(values[1].toString());
			    
			    if ( null != values[2] ){
			    	menuAccess.setMenuAction(values[2].toString());
			    }else{
			    	menuAccess.setMenuAction("");
			    }
			    menuAccess.setParentMenuId(Integer.parseInt(values[3].toString()));
			    try {
			    	if ( null != values[4] && Integer.parseInt(values[4].toString()) > 0 ){
			    		menuAccess.setAccess(true);
			    	}else{
			    		menuAccess.setAccess(false);
			    	}
			    }catch(Exception e){
			    	menuAccess.setAccess(false);
			    }

			    menuAccessList.add(menuAccess);
			}
		} catch (Exception exp) {

			logger.error("Exception in reteiveMenusAccessForGroup :"+exp);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(exp));
		}
		logger.info("Exit: reteiveMenusAccessForGroup");
		return menuAccessList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FunctionalityPermission> getFunctionalityPermissionList() {
		logger.debug("Entry: getFunctionalities");
		Criteria criteria = null;
		List<FunctionalityPermission> functionalityPermissionList = new ArrayList<FunctionalityPermission>(); 
		try {
			
		criteria = sessionFactory.getCurrentSession().createCriteria(FunctionalityPermission.class);
		functionalityPermissionList = criteria.list();
		} catch (Exception e) {
			logger.error("Exception in getFunctionalityPermissionList :"+e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getFunctionalities");
		return functionalityPermissionList;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Boolean addFunctionality(FunctionalityPermission functionality) {
		logger.debug("Entry: addFunctionality");
		Boolean insertStatus = false;
		try {
			sessionFactory.getCurrentSession().save(functionality);
			insertStatus = true;
		} catch (Exception e) {
			logger.error("Exception in addFunctionality :"+e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
			
			insertStatus = false;
			
		}
		logger.debug("Exit: addFunctionality");
		return insertStatus;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public List<FunctionalityPermission> getFunctionality(int menuId){
		logger.debug("Entry: getFunctionality");
		List<FunctionalityPermission> functionalityList = new ArrayList<FunctionalityPermission>();
		Query query = null;
		try {
			query = sessionFactory.getCurrentSession().createQuery(
			"from FunctionalityPermission where menuId= :menuId ");
			query.setParameter("menuId", menuId);

			if (query != null && query.list() != null
					&& query.list().size() > 0) {
				functionalityList = (List<FunctionalityPermission>) query.list();
			}
		} catch (Exception e) {
			logger.error("Exception in getFunctionality :"+e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Entry: getFunctionality");
		return functionalityList;
	}
	
	@Override
	public List<Menu> getSubMenuList() {
		logger.info("Entry: getSubMenuList");
		Query query = null;
		try{
		 query = sessionFactory.getCurrentSession().createQuery(" from Menu m where " +
				"parentMenuId <> 0");
		}
		catch(Exception exp){
			logger.error(exp);}
		logger.info("Exit: getAllMenus");
		return query.list();
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MiscellaneousTool> getToolsList() {
		logger.info("Entry: getToolsList");
		List<MiscellaneousTool> toolsList = new ArrayList<MiscellaneousTool>();
		Query query = null;
		try{
		 query = sessionFactory.getCurrentSession().createQuery("from MiscellaneousTool order by toolId");
		 toolsList = query.list();
		}
		catch(Exception exp){
			logger.error(exp);}
		logger.info("Exit: getToolsList");
		return toolsList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<MiscellaneousTool> getToolsListForGroup(int userGroupId) {
		logger.info("Entry: getToolsListForGroup");
		List<MiscellaneousTool> toolsList = new ArrayList<MiscellaneousTool>();
		Query query = null;
		try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from MiscellaneousTool where toolId in (select toolId from FunctionalityPermission where functionalityId in (select functionalityId from FunctionalityGroupRef where groupId=:groupId))");
			query.setParameter("groupId", userGroupId);
			toolsList = query.list();
		} catch (Exception exp) {
			logger.error("Exception in getToolsListForGroup");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(exp));
		}
		logger.info("Exit: getToolsListForGroup");
		return toolsList;
	}
	
	@Override
	public List<Menu> getSubMenuList(int parentMenuId) {
		logger.info("Entry: getSubMenuList");
		Query query = null;
		List<Menu> menuList = new ArrayList<Menu>();
		try{
		
		 query = sessionFactory.getCurrentSession().createQuery(" from Menu m where " +
				"parentMenuId =:parentMenuId");
		 query.setParameter("parentMenuId", parentMenuId);
		 

		 menuList=  query.list();
		}
		catch(Exception exp){
			logger.error(exp);}
		logger.info("Exit: getAllMenus");
		return menuList;
		
	}
}
