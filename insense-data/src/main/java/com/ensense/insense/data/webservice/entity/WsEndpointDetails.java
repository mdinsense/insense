package com.ensense.insense.data.webservice.entity;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

@Entity
@Table(name="WsEndpointDetails")
public class WsEndpointDetails {
	@Id
	@GenericGenerator(name = "sequenceGen", strategy = "com.cts.mint.common.entity.SequenceTable")
	@GeneratedValue(generator = "sequenceGen")
	@Column(name="wsEndpointDetailsId")
	private Integer wsEndpointDetailsId;

	@Column(name="serviceId")
	private int serviceId;
	
	@Column(name="environmentId")
	private int environmentId;
	
	@Column(name="endpoint")
	private String endpoint;

	@OneToOne(fetch = FetchType.EAGER)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "serviceId", insertable = false, updatable = false, nullable = true, unique = false)
	private Webservices webservices;
	
	public void setWsEndpointDetailsId(Integer wsEndpointDetailsId) {
		this.wsEndpointDetailsId = wsEndpointDetailsId;
	}

	public Integer getWsEndpointDetailsId() {
		return wsEndpointDetailsId;
	}
	public int getServiceId() {
		return serviceId;
	}

	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}

	public int getEnvironmentId() {
		return environmentId;
	}

	public void setEnvironmentId(int environmentId) {
		this.environmentId = environmentId;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	@Override
	public String toString() {
		return "WebServicesDetails [webserviceDetailsId=" + wsEndpointDetailsId
				+ ", serviceId=" + serviceId + ", environmentId="
				+ environmentId + ", endpoint=" + endpoint + "]";
	}

	public void setWebservices(Webservices webservices) {
		this.webservices = webservices;
	}

	public Webservices getWebservices() {
		return webservices;
	}

	

}
