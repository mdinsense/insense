package com.ensense.insense.data.uitesting.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

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
