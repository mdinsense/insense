package com.ensense.insense.data.miscellaneous.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="MiscellaneousTool")
public class MiscellaneousTool {
	@Id
	@GenericGenerator(name = "sequenceGen", strategy = "com.cts.mint.common.entity.SequenceTable")
	@GeneratedValue(generator = "sequenceGen")
	@Column(name = "toolId")
	private int toolId;
	
	@Column(name="toolName")
	private String toolName;
	
	@Column(name="toolDescription")
	private String toolDescription;
	
	@Column(name="toolActionUrl")
	private String toolActionUrl;
	
	@Transient
	private boolean hasAccess;

	public int getToolId() {
		return toolId;
	}

	public void setToolId(int toolId) {
		this.toolId = toolId;
	}

	public String getToolName() {
		return toolName;
	}

	public void setToolName(String toolName) {
		this.toolName = toolName;
	}

	public String getToolDescription() {
		return toolDescription;
	}

	public void setToolDescription(String toolDescription) {
		this.toolDescription = toolDescription;
	}

	public String getToolActionUrl() {
		return toolActionUrl;
	}

	public void setToolActionUrl(String toolActionUrl) {
		this.toolActionUrl = toolActionUrl;
	}

	public boolean isHasAccess() {
		return hasAccess;
	}

	public void setHasAccess(boolean hasAccess) {
		this.hasAccess = hasAccess;
	}

	@Override
	public String toString() {
		return "MiscellaneousTool [toolId=" + toolId + ", toolName=" + toolName
				+ ", toolDescription=" + toolDescription + ", toolActionUrl="
				+ toolActionUrl + "]";
	}
}
