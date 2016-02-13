package com.ensense.insense.data.webservice.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "Webservices")
public class Webservices implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "sequenceGen", strategy = "com.cts.mint.common.entity.SequenceTable")
	@GeneratedValue(generator = "sequenceGen")
	@Column(name = "serviceId")
	private int serviceId;

	@Column(name = "serviceName")
	private String serviceName;

	@Column(name = "addedDate")
	private Date addedDate;

	@Column(name = "modifiedDate")
	private Date modifiedDate;

	@Column(name = "serviceType")
	private String serviceType;

	@Column(name = "serviceFilePath")
	private String serviceFilePath;

	public String getServiceFilePath() {
		return serviceFilePath;
	}

	public void setServiceFilePath(String serviceFilePath) {
		this.serviceFilePath = serviceFilePath;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public Webservices() {

	}

	public int getServiceId() {
		return serviceId;
	}

	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	
	public Date getAddedDate() {
		return addedDate;
	}

	public void setAddedDate(Date addedDate) {
		this.addedDate = addedDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	@Override
	public String toString() {
		return "Webservices [serviceId=" + serviceId + ", serviceName="
				+ serviceName + ", addedDate="
				+ addedDate + ", modifiedDate=" + modifiedDate + ", serviceType="
				+ serviceType + ", serviceFilePath=" + serviceFilePath + "]";
	}

}
