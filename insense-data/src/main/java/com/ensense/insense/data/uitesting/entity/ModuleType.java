package com.ensense.insense.data.uitesting.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "ModuleType")
public class ModuleType implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GenericGenerator(name = "sequenceGen", strategy = "com.cts.mint.common.entity.SequenceTable")
	@GeneratedValue(generator = "sequenceGen")
	@Column(name = "moduleTypeId")
	private Integer moduleTypeId;

	@Column(name = "moduleType")
	private String moduleType;

	public Integer getModuleTypeId() {
		return moduleTypeId;
	}

	public void setModuleTypeId(Integer moduleTypeId) {
		this.moduleTypeId = moduleTypeId;
	}

	public String getModuleType() {
		return moduleType;
	}

	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}

	@Override
	public String toString() {
		return "ModuleType [moduleTypeId=" + moduleTypeId + ", moduleType="
				+ moduleType + "]";
	}
	
}
