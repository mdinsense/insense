package com.ensense.insense.core.webservice.util;

public enum WebServiceSecurity {
	DEV(0, "dev", "esbsupport", "poi098", "PasswordDigest"),
	PROD(6, "prod", "FISSupport", "fispassword", "PasswordDigest"),
	PRODFIX(5, "prodfix", "FISSupport", "fispassword", "PasswordDigest"),
	PROD_FIX(34, "prodfix", "FISSupport", "fispassword", "PasswordDigest"),
	PROD_WEBSERVICE(65, "prod", "FISSupport", "fispassword", "PasswordDigest");
	
	private String envName;
	private String userId;
	private String passwd;
	private String passwdType;
	private Integer environmentId;
	
	private WebServiceSecurity(Integer environmentId, String envName, String userId, String passwd, String passwdType) {
		this.environmentId = environmentId;
		this.envName = envName;
		this.userId = userId;
		this.passwd = passwd;
		this.passwdType = passwdType;
	}

	public String getEnvName() {
		return envName;
	}

	public void setEnvName(String envName) {
		this.envName = envName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getPasswdType() {
		return passwdType;
	}

	public void setPasswdType(String passwdType) {
		this.passwdType = passwdType;
	}

	public Integer getEnvironmentId() {
		return environmentId;
	}

	public void setEnvironmentId(Integer environmentId) {
		this.environmentId = environmentId;
	}
}
