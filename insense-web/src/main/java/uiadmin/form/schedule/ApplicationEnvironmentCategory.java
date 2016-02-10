package uiadmin.form.schedule;

import com.cts.mint.uitesting.entity.Application;
import com.cts.mint.uitesting.entity.Environment;
import com.cts.mint.uitesting.entity.AppEnvEnvironmentCategoryXref;

public class ApplicationEnvironmentCategory {
	
	private Application application;
	private Environment environment;
	private AppEnvEnvironmentCategoryXref envcategoryXref;
	
	/**
	 * @return the application
	 */
	public Application getApplication() {
		return application;
	}
	/**
	 * @param application the application to set
	 */
	public void setApplication(Application application) {
		this.application = application;
	}
	/**
	 * @return the environment
	 */
	public Environment getEnvironment() {
		return environment;
	}
	/**
	 * @param environment the environment to set
	 */
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}
	/**
	 * @return the envcategoryXref
	 */
	public AppEnvEnvironmentCategoryXref getEnvcategoryXref() {
		return envcategoryXref;
	}
	/**
	 * @param envcategoryXref the envcategoryXref to set
	 */
	public void setEnvcategoryXref(AppEnvEnvironmentCategoryXref envcategoryXref) {
		this.envcategoryXref = envcategoryXref;
	}

	@Override
	public String toString() {
		return "ApplicationEnvironmentCategory [application=" + application
				+ ", environment=" + environment
				+ ", envcategoryXref=" + envcategoryXref + "]";
	}
	
}
