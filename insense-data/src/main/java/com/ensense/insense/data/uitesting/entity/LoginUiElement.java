package com.ensense.insense.data.uitesting.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * The persistent class for the LoginUiElement database table.
 * 
 */
@Entity
@Table(name = "LoginUiElement")
public class LoginUiElement implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "sequenceGen", strategy = "com.cts.mint.common.entity.SequenceTable")
	@GeneratedValue(generator = "sequenceGen")
	@Column(name = "loginUiElementId")
	private Integer loginUiElementId;

	@Column(name = "loginUserDetailId", nullable = false)
	private Integer loginUserDetailId;
	
	@Column(name = "elementIdentifier")
	private String elementIdentifier;

	@Column(name = "uiElementName", nullable = false)
	private String uiElementName;

	@Column(name = "uiElementValue", nullable = false)
	private String uiElementValue;

	@Column(name = "secured")
	private byte secured;

	/*@ManyToOne
	@JoinColumn(name = "loginUserDetailId", insertable = false, updatable = false, nullable = true, unique = false)
	private LoginUserDetails loginUserDetails;
	*/
	
	/**
	 * @return the loginUiElementId
	 */
	public Integer getLoginUiElementId() {
		return loginUiElementId;
	}

	/**
	 * @param loginUiElementId
	 *            the loginUiElementId to set
	 */
	public void setLoginUiElementId(Integer loginUiElementId) {
		this.loginUiElementId = loginUiElementId;
	}

	/**
	 * @return the loginUserDetailId
	 */
	public Integer getLoginUserDetailId() {
		return loginUserDetailId;
	}

	/**
	 * @param loginUserDetailId
	 *            the loginUserDetailId to set
	 */
	public void setLoginUserDetailId(Integer loginUserDetailId) {
		this.loginUserDetailId = loginUserDetailId;
	}

	/**
	 * @return the elementIdentifier
	 */
	public String getElementIdentifier() {
		return elementIdentifier;
	}

	/**
	 * @param elementIdentifier
	 *            the elementIdentifier to set
	 */
	public void setElementIdentifier(String elementIdentifier) {
		this.elementIdentifier = elementIdentifier;
	}

	public String getUiElementName() {
		return uiElementName;
	}

	public void setUiElementName(String uiElementName) {
		this.uiElementName = uiElementName;
	}

	/**
	 * @return the uiElementValue
	 */
	public String getUiElementValue() {
		return uiElementValue;
	}

	/**
	 * @param uiElementValue
	 *            the uiElementValue to set
	 */
	public void setUiElementValue(String uiElementValue) {
		this.uiElementValue = uiElementValue;
	}

	/**
	 * @return the secured
	 */
	public byte getSecured() {
		return secured;
	}

	/**
	 * @param secured
	 *            the secured to set
	 */
	public void setSecured(byte secured) {
		this.secured = secured;
	}

	@Override
	public String toString() {
		return "LoginUiElement [loginUiElementId=" + loginUiElementId
				+ ", loginUserDetailId=" + loginUserDetailId
				+ ", elementIdentifier=" + elementIdentifier + ", uiElementName="
				+ uiElementName + ", uiElementValue=" + uiElementValue
				+ ", secured=" + secured + "]";
	}

}
