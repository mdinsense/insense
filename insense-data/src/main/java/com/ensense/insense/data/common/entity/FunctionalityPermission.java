package com.ensense.insense.data.common.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "FunctionalityPermission")
public class FunctionalityPermission implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "sequenceGen", strategy = "com.cts.mint.common.entity.SequenceTable") 
	@GeneratedValue(generator = "sequenceGen")
	@Column(name="functionalityId")
	private int functionalityId;
	
	@Column(name="functionalityName")
	private String functionalityName;
	
	@Column(name = "menuId")
	private int menuId;
	
	@Column(name = "toolId")
	private Integer toolId;
	
	public int getFunctionalityId() {
		return functionalityId;
	}

	public void setFunctionalityId(int functionalityId) {
		this.functionalityId = functionalityId;
	}

	public String getFunctionalityName() {
		return functionalityName;
	}

	public void setFunctionalityName(String functionalityName) {
		this.functionalityName = functionalityName;
	}

	public int getMenuId() {
		return menuId;
	}

	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}

	public Integer getToolId() {
		return toolId;
	}

	public void setToolId(Integer toolId) {
		this.toolId = toolId;
	}

	@Override
	public String toString() {
		return "FunctionalityPermission [functionalityId=" + functionalityId
				+ ", functionalityName=" + functionalityName + ", menuId="
				+ menuId + ", toolId=" + toolId + "]";
	}
}
