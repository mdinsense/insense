package com.ensense.insense.data.uitesting.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "ExcludeLinkType")
public class ExcludeLinkType {

	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "sequenceGen", strategy = "com.cts.mint.common.entity.SequenceTable")
	@GeneratedValue(generator = "sequenceGen")
	@Column(name="excludeLinktypeId")
	private int excludeLinktypeId;
	
	@Column(name = "excludeLinktype", nullable = false)
	private String excludeLinktype;

	public int getExcludeLinktypeId() {
		return excludeLinktypeId;
	}

	public void setExcludeLinktypeId(int excludeLinktypeId) {
		this.excludeLinktypeId = excludeLinktypeId;
	}

	public String getExcludeLinktype() {
		return excludeLinktype;
	}

	public void setExcludeLinktype(String excludeLinktype) {
		this.excludeLinktype = excludeLinktype;
	}

	@Override
	public String toString() {
		return "ExcludeLinkType [excludeLinktypeId=" + excludeLinktypeId
				+ ", excludeLinktype=" + excludeLinktype + "]";
	}
	
}
