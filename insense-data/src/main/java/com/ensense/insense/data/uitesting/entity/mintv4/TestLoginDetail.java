package com.ensense.insense.data.uitesting.entity.mintv4;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

/**
 * The persistent class for the TestLoginDetail database table.
 * 
 */
@Entity
@Table(name = "TestLoginDetail", uniqueConstraints = {@UniqueConstraint(columnNames={"loginId"})})
public class TestLoginDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "sequenceGen", strategy = "com.cts.mint.common.entity.SequenceTable") 
	@GeneratedValue(generator = "sequenceGen")
	@Column(name = "loginDetailId")
	private Integer loginDetailId;
	
	@Column(name = "suitId")
	private int suitId;

	@Column(name = "loginId", nullable = false)
	private String loginId;
	
	@Column(name = "password", nullable = false)
	private String password;
	
	@Column(name = "rsaEnabled", nullable = false)
	private boolean rsaEnabled;
	
	@Column(name = "securityAnswer", nullable = true)
	private String securityAnswer;

	public Integer getLoginDetailId() {
		return loginDetailId;
	}

	public void setLoginDetailId(Integer loginDetailId) {
		this.loginDetailId = loginDetailId;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isRsaEnabled() {
		return rsaEnabled;
	}

	public void setRsaEnabled(boolean rsaEnabled) {
		this.rsaEnabled = rsaEnabled;
	}

	public String getSecurityAnswer() {
		return securityAnswer;
	}

	public void setSecurityAnswer(String securityAnswer) {
		this.securityAnswer = securityAnswer;
	}

	public int getSuitId() {
		return suitId;
	}

	public void setSuitId(int suitId) {
		this.suitId = suitId;
	}

	@Override
	public String toString() {
		return "TestLoginDetail [loginDetailId=" + loginDetailId + ", loginId="
				+ loginId + ", password=" + password + ", rsaEnabled="
				+ rsaEnabled + ", securityAnswer=" + securityAnswer + "]";
	}
}