package com.ensense.insense.data.guardian.entity;

import java.io.Serializable;

public class Monitor  implements Serializable{

	/**
	 * 
	 */
	
	private String signatureId;
	 private String application;  
	 private String environment;  
	 private String signature;  
	 private String tier;
	 private String date;
	 
	 public Monitor(String signatureId,String application,String environment,String signature,String tier,String date){
		 this.signatureId=signatureId;
		 this.application=application; 
		 this.environment=environment; 
		 this.signature=signature; 
		 this.tier=tier; 
		 this.date=date; 
		 
	 }
	public String getSignatureId() {
		return signatureId;
	}
	public void setSignatureId(String signatureId) {
		this.signatureId = signatureId;
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
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getTier() {
		return tier;
	}
	public void setTier(String tier) {
		this.tier = tier;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	
}
