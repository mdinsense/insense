package com.ensense.insense.data.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MintMenu implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private MenuAccess menu;
	private List<MenuAccess> childMenus;
	
	public MenuAccess getMenu() {
		return menu;
	}
	public void setMenu(MenuAccess menu) {
		this.menu = menu;
	}
	public List<MenuAccess> getChildMenus() {
		if( null == childMenus ){
			childMenus = new ArrayList<MenuAccess>();
		}
		return childMenus;
	}
	public void setChildMenus(List<MenuAccess> childMenus) {
		this.childMenus = childMenus;
	}
	@Override
	public String toString() {
		return "MintMenu [menu=" + menu + ", childMenus=" + childMenus + "]";
	}
}
