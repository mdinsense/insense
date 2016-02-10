package uiadmin.form;

import com.cts.mint.uitesting.model.UiTestingSetupForm;

public class IncludeExcludeUrlForm extends UiTestingSetupForm{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String application;
	private String environment;
	private String includeUrl;
	private String excludeUrl;
	
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
	public String getIncludeUrl() {
		return includeUrl;
	}
	public void setIncludeUrl(String includeUrl) {
		this.includeUrl = includeUrl;
	}
	public String getExcludeUrl() {
		return excludeUrl;
	}
	public void setExcludeUrl(String excludeUrl) {
		this.excludeUrl = excludeUrl;
	}

	
}
