package com.ensense.insense.data.common.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * The persistent class for the menu database table.
 * 
 */
@Entity
@Table(name = "Menu")
public class Menu implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "sequenceGen", strategy = "com.cts.mint.common.entity.SequenceTable") 
	@GeneratedValue(generator = "sequenceGen")
	@Column(name = "menuId")
	private int menuId;
	
	//private ArrayList<Menu> menuList;
	
	@Column(name = "menuName")
	private String menuName;

	@Column(name = "menuAction")
	private String menuAction;

	@Column(name = "menuOrder")
	private int menuOrder;

	@Column(name = "parentMenuId")
	private int parentMenuId;

	@Temporal(TemporalType.DATE)
	@Column(name = "dateCreated")
	private Date dateCreated;

	@Temporal(TemporalType.DATE)
	@Column(name = "dateModified")
	private Date dateModified;

	public Menu() {
	}

	public int getMenuId() {
		return this.menuId;
	}

	public void setMenuid(int menuId) {
		this.menuId = menuId;
	}

	public int getMenuOrder() {
		return this.menuOrder;
	}

	public void setMenuOrder(int menuOrder) {
		this.menuOrder = menuOrder;
	}

	public int getParentMenuId() {
		return this.parentMenuId;
	}

	public void setParentMenuId(int parentMenuId) {
		this.parentMenuId = parentMenuId;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public Date getDateCreated() {
		return this.dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getDateModified() {
		return this.dateModified;
	}

	public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}

	public String getMenuAction() {
		return this.menuAction;
	}

	public void setMenuAction(String menuAction) {
		this.menuAction = menuAction;
	}

	@Override
	public String toString() {
		return "Menu [menuId=" + menuId + ", menuName=" + menuName
				+ ", menuAction=" + menuAction + ", menuOrder=" + menuOrder
				+ ", parentMenuId=" + parentMenuId + ", dateCreated="
				+ dateCreated + ", dateModified=" + dateModified + "]";
	}

	
}