package uiadmin.form;


import com.ensense.insense.data.uitesting.model.UiTestingSetupForm;

public class EnvironmentLoginUserForm extends UiTestingSetupForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String elememntsArray;
	private String testLoginId;

	/**
	 * @return the elememntsArray
	 */
	public String getElememntsArray() {
		return elememntsArray;
	}

	/**
	 * @param elememntsArray
	 *            the elememntsArray to set
	 */
	public void setElememntsArray(String elememntsArray) {
		this.elememntsArray = elememntsArray;
	}

	

	/**
	 * @return the testLoginId
	 */
	public String getTestLoginId() {
		return testLoginId;
	}

	/**
	 * @param testLoginId
	 *            the testLoginId to set
	 */
	public void setTestLoginId(String testLoginId) {
		this.testLoginId = testLoginId;
	}

}
