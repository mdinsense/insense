package com.ensense.insense.data.common.model;

import java.io.Serializable;
import java.util.List;

public class UrlForm implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String formId;
	private String formName;
	private List<FormElements> formElements;
	
	
	public String getFormId() {
		return formId;
	}
	public void setFormId(String formId) {
		this.formId = formId;
	}
	public String getFormName() {
		return formName;
	}
	public void setFormName(String formName) {
		this.formName = formName;
	}
	
	public List<FormElements> getFormElements() {
		return formElements;
	}
	public void setFormElements(List<FormElements> formElements) {
		this.formElements = formElements;
	}
	
	@Override
	public String toString() {
		return "UrlForm [formId=" + formId + ", formName=" + formName
				+ ", formElements=" + formElements + "]";
	}
	
	@Override
	public int hashCode() {
		 return getFormId().hashCode() + getFormName().hashCode() + getFormElements().hashCode();
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof UrlForm)) {
        	return false;
        }
		UrlForm urlForm = (UrlForm) obj;
		if(urlForm.formId.equals(this.getFormId()) && urlForm.formName.equals(this.getFormName()) && urlForm.formElements.size() ==this.getFormElements().size() ) {
			List<FormElements> current = urlForm.formElements;
			
				if(this.getFormElements().containsAll(current)) {
					return true;
				} else {
					return false;
				}
			
		} else {
			return false;
		}
	
	}
	
	

}
