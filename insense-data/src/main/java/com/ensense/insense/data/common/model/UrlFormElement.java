package com.ensense.insense.data.common.model;

import java.io.Serializable;
import java.util.List;

public class UrlFormElement implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String url;
	private List<UrlForm> urlForms;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public List<UrlForm> getUrlForms() {
		return urlForms;
	}
	public void setUrlForms(List<UrlForm> urlForms) {
		this.urlForms = urlForms;
	}
	@Override
	public String toString() {
		return "UrlFormElement [url=" + url + ", urlForms=" + urlForms + "]";
	}
	@Override
	public int hashCode() {
		 return getUrl().hashCode() + getUrlForms().hashCode();
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof UrlFormElement)) {
        	return false;
        }
		
		UrlFormElement urlFormElement = (UrlFormElement) obj;
		if(urlFormElement.url.equals(this.getUrl()) && urlFormElement.urlForms.size() ==this.getUrlForms().size() ) {
			if(urlFormElement.urlForms.containsAll(this.getUrlForms())) {
				return true;
			} else {
				return false;
			}
		}  else {
			return false;
		}
	}
}
