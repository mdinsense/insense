package com.ensense.insense.data.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.cts.mint.common.entity.FunctionalityPermission;

public class MenuAccess implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int menuId;
	private String menuName;
	private String menuAction;
	private int parentMenuId;
	private boolean access;
	private List<FunctionalityPermission> functionality;

	public int getMenuId() {
		return menuId;
	}

	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getMenuAction() {
		return menuAction;
	}

	public void setMenuAction(String menuAction) {
		this.menuAction = menuAction;
	}

	public int getParentMenuId() {
		return parentMenuId;
	}

	public void setParentMenuId(int parentMenuId) {
		this.parentMenuId = parentMenuId;
	}

	public boolean isAccess() {
		return access;
	}

	public void setAccess(boolean access) {
		this.access = access;
	}

	public List<FunctionalityPermission> getFunctionality() {
		if (null == functionality) {
			this.functionality = new ArrayList<FunctionalityPermission>();
		}
		return functionality;
	}

	public void setFunctionality(
			List<FunctionalityPermission> functionality) {
		this.functionality = functionality;
	}

	public void addFunctionality(FunctionalityPermission functionality) {
		this.functionality.add(functionality);

	}

	@Override
	public String toString() {
		return "MenuAccess [menuId=" + menuId + ", menuName=" + menuName
				+ ", menuAction=" + menuAction + ", parentMenuId="
				+ parentMenuId + ", access=" + access + ", functionality="
				+ functionality + "]";
	}

}
