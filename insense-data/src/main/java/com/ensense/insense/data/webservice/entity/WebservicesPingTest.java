package com.ensense.insense.data.webservice.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="WebservicesPingTest")
public class WebservicesPingTest implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GenericGenerator(name = "sequenceGen", strategy = "com.cts.mint.common.entity.SequenceTable")
	@GeneratedValue(generator = "sequenceGen")
	@Column(name="esbServiceId")
	private int esbServiceId;
	
	@Column(name="serviceName")
	private String serviceName;

	public int getEsbServiceId() {
		return esbServiceId;
	}
	public void setEsbServiceId(int esbServiceId) {
		this.esbServiceId = esbServiceId;
	}

	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	@Override
	public String toString() {
		return "WebservicesPingTest [esbServiceId=" + esbServiceId
				+ ", serviceName=" + serviceName + "]";
	}
}
