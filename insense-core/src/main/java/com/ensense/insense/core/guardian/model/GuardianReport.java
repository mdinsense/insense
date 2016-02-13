package com.ensense.insense.core.guardian.model;

import java.io.Serializable;

public class GuardianReport  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer reprotId;
	private String application;
	private String environment;
	private String channel;
	
	public Integer getReprotId() {
		return reprotId;
	}
	public void setReprotId(Integer reprotId) {
		this.reprotId = reprotId;
	}
	public String getApplication() {
		return application;
	}
	public void setApplication(String application) {
		this.application = application;
	}
	public String getEnvironment() {
		return environment;
	}
	public void setEnvironment(String environment) {
		this.environment = environment;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}

}
