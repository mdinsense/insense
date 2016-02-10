package com.ensense.insense.data.transaction.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name="testcase_table")
public class TestCaseFileUpload implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="test_case_id")
	private int testCaseId;
	
	@Column(name="test_case_name")
	private String testcaseName;

	@Column(name="application_id")
	private int applicationId;
	
	@Column(name="environment_id")
	private int environmentId;
	
	@Temporal( TemporalType.DATE)
	@Column(name="date_created")
	private Date dateCreated;

    @Temporal( TemporalType.DATE)
	@Column(name="date_modified")
	private Date dateModified;

	@Temporal( TemporalType.DATE)
	@Column(name="time_created")
	private Date timeCreated;

    @Temporal( TemporalType.DATE)
	@Column(name="time_modified")
	private Date timeModified;
    
    @Column(name="source_folder_path")
	private String sourceFolderPath;
    
    @Column(name="source_file_name")
	private String sourceFileName;

    

	public int getTestCaseId() {
		return testCaseId;
	}

	public void setTestCaseId(int testCaseId) {
		this.testCaseId = testCaseId;
	}

	public int getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(int applicationId) {
		this.applicationId = applicationId;
	}

	public int getEnvironmentId() {
		return environmentId;
	}

	public void setEnvironmentId(int environmentId) {
		this.environmentId = environmentId;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getDateModified() {
		return dateModified;
	}

	public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}

	public Date getTimeCreated() {
		return timeCreated;
	}

	public void setTimeCreated(Date timeCreated) {
		this.timeCreated = timeCreated;
	}

	public Date getTimeModified() {
		return timeModified;
	}

	public void setTimeModified(Date timeModified) {
		this.timeModified = timeModified;
	}

	public String getSourceFolderPath() {
		return sourceFolderPath;
	}

	public void setSourceFolderPath(String sourceFolderPath) {
		this.sourceFolderPath = sourceFolderPath;
	}

	public String getSourceFileName() {
		return sourceFileName;
	}

	public void setSourceFileName(String sourceFileName) {
		this.sourceFileName = sourceFileName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getTestcaseName() {
		return testcaseName;
	}

	public void setTestcaseName(String testcaseName) {
		this.testcaseName = testcaseName;
	}

	@Override
	public String toString() {
		return "TestCaseFileUpload [testCaseId=" + testCaseId
				+ ", testcaseName=" + testcaseName + ", applicationId="
				+ applicationId + ", environmentId=" + environmentId
				+ ", dateCreated=" + dateCreated + ", dateModified="
				+ dateModified + ", timeCreated=" + timeCreated
				+ ", timeModified=" + timeModified + ", sourceFolderPath="
				+ sourceFolderPath + ", sourceFileName=" + sourceFileName + "]";
	}

	
}
