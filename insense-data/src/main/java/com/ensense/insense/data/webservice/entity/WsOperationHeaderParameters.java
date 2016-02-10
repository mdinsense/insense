package com.ensense.insense.data.webservice.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;



@Entity
@Table(name="WsOperationHeaderParameters")
public class WsOperationHeaderParameters implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "sequenceGen", strategy = "com.cts.mint.common.entity.SequenceTable")
	@GeneratedValue(generator = "sequenceGen")
	@Column(name="headerParameterId")
	private Integer headerParameterId;
	
	@Column(name="parameterId")
	private Integer parameterId;

	@Column(name="parameterName")
	private String parameterName;
	
	@Column(name="parameterValue")
	private String parameterValue;

	public Integer getHeaderParameterId() {
		return headerParameterId;
	}

	public void setHeaderParameterId(Integer headerParameterId) {
		this.headerParameterId = headerParameterId;
	}

	public Integer getParameterId() {
		return parameterId;
	}

	public void setParameterId(Integer parameterId) {
		this.parameterId = parameterId;
	}

	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	public String getParameterValue() {
		return parameterValue;
	}

	public void setParameterValue(String parameterValue) {
		this.parameterValue = parameterValue;
	}
	
	@Override
	public String toString() {
		return "WsOperationHeaderParameters [headerParameterId=" + headerParameterId + ", parameterId="
				+ parameterId + ", parameterName=" + parameterName + ", parameterValue="
				+ parameterValue + "]";
	}
	


}
