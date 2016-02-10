package com.ensense.insense.data.guardian.entity;

import java.io.Serializable;

public class Signature implements Serializable{

	/**
	 * 
	 */
	 private String signatureId;
	 private String criticality;  
	 private String signature;  
	 private String solution;  
	 private String providedby;
	 private String date;
		  
	public Signature(String signatureId,String criticality,String signature,String solution,String providedby,String date){  
		this.signatureId=signatureId;
		this.criticality=criticality;
		this.signature=signature;
		this.solution=solution;  
		this.providedby=providedby;
		this.date=date;  
   }

	public String getSignatureId() {
		return signatureId;
	}

	public void setSignatureId(String signatureId) {
		this.signatureId = signatureId;
	}

	public String getCriticality() {
		return criticality;
	}

	public void setCriticality(String criticality) {
		this.criticality = criticality;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getSolution() {
		return solution;
	}

	public void setSolution(String solution) {
		this.solution = solution;
	}

	public String getProvidedby() {
		return providedby;
	}

	public void setProvidedby(String providedby) {
		this.providedby = providedby;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}  
	
	

}
