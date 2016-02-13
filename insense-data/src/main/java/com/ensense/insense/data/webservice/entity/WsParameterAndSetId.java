package com.ensense.insense.data.webservice.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class WsParameterAndSetId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "parameterSetId")
	private Integer parameterSetId;

	@Column(name = "parameterId")
	private Integer parameterId;

	

	public Integer getParameterSetId() {
		return parameterSetId;
	}



	public void setParameterSetId(Integer parameterSetId) {
		this.parameterSetId = parameterSetId;
	}



	public Integer getParameterId() {
		return parameterId;
	}



	public void setParameterId(Integer parameterId) {
		this.parameterId = parameterId;
	}



	@Override
	public String toString() {
		return "TestParameterAndSetId [testParameterSetId="
				+ parameterSetId + ", testParameterId=" + parameterId
				+ "]";
	}

}
