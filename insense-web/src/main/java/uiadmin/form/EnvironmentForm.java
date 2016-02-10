package uiadmin.form;

import com.cts.mint.uitesting.model.UiTestingSetupForm;


public class EnvironmentForm extends UiTestingSetupForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String  environmentName;
	private String  loginUrl;
	private Boolean secureSite;
	
	/**
	 * @return the environmentName
	 */
	public String getEnvironmentName() {
		return environmentName;
	}
	/**
	 * @param environmentName the environmentName to set
	 */
	public void setEnvironmentName(String environmentName) {
		this.environmentName = environmentName;
	}
	/**
	 * @return the loginUrl
	 */
	public String getLoginUrl() {
		return loginUrl;
	}
	/**
	 * @param loginUrl the loginUrl to set
	 */
	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}
	/**
	 * @return the secureSite
	 */
	public Boolean getSecureSite() {
		return secureSite;
	}
	/**
	 * @param secureSite the secureSite to set
	 */
	public void setSecureSite(Boolean secureSite) {
		this.secureSite = secureSite;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EnvironmentForm [environmentName=" + environmentName
				+ ", loginUrl=" + loginUrl + ", secureSite=" + secureSite+ "]";
	}

}
