package com.ensense.insense.data.uitesting.entity;

import com.ensense.insense.data.common.utils.JsonReaderWriter;
import com.ensense.insense.data.common.utils.MintFileUtils;
import com.ensense.insense.data.uitesting.model.ModuleType;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ApplicationModuleXref")
public class ApplicationModuleXref implements Serializable {
	private static Logger logger = Logger
			.getLogger(ApplicationModuleXref.class);
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "sequenceGen", strategy = "com.cts.mint.common.entity.SequenceTable")
	@GeneratedValue(generator = "sequenceGen")
	@Column(name = "applicationModuleXrefId")
	private int applicationModuleXrefId;

	@Column(name = "moduleTypeId")
	private int moduleTypeId;

	@Column(name = "moduleName")
	private String moduleName;

	@Column(name = "environmentId")
	private int environmentId;

	@Column(name = "applicationId")
	private int applicationId;

	@Column(name = "includeUrlPattern")
	private String includeUrlPattern;

	@Column(name = "testUrlPattern")
	private String testUrlPattern;

	@Column(name = "startUrl")
	private String startUrl;

	@Column(name = "staticUrls")
	private String staticUrls;

	@Column(name = "staticUrlCount")
	private int staticUrlCount;

	/*
	 * @OneToOne(fetch = FetchType.EAGER)
	 * 
	 * @NotFound(action = NotFoundAction.IGNORE)
	 * 
	 * @JoinColumn(name = "applicationId", insertable = false, updatable =
	 * false, nullable = true, unique = false) private Application application;
	 * 
	 * @OneToOne(fetch = FetchType.EAGER)
	 * 
	 * @NotFound(action = NotFoundAction.IGNORE)
	 * 
	 * @JoinColumn(name = "environmentId", insertable = false, updatable =
	 * false, nullable = true, unique = false) private Environment environment;
	 */

	@OneToOne(fetch = FetchType.EAGER)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "moduleTypeId", insertable = false, updatable = false, nullable = true, unique = false)
	private ModuleType moduleType;
	
	
	@OneToMany(fetch = FetchType.EAGER)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "applicationModuleXrefId", insertable = false, updatable = false, nullable = true, unique = false)
	private List<TransactionTestCase> transactionTestCaseList;

	/*
	 * @OneToOne(fetch = FetchType.EAGER)
	 * 
	 * @NotFound(action = NotFoundAction.IGNORE)
	 * 
	 * @JoinColumns( {
	 * 
	 * @JoinColumn(name = "environmentId", referencedColumnName =
	 * "environmentId", insertable = false, updatable = false, nullable = true,
	 * unique = false),
	 * 
	 * @JoinColumn(name = "applicationId", referencedColumnName =
	 * "applicationId", insertable = false, updatable = false, nullable = true,
	 * unique = false) }) private AppEnvEnvironmentCategoryXref
	 * appEnvEnvironmentCategoryXref;
	 */

	public int getApplicationModuleXrefId() {
		return applicationModuleXrefId;
	}

	public void setApplicationModuleXrefId(int applicationModuleXrefId) {
		this.applicationModuleXrefId = applicationModuleXrefId;
	}

	public int getModuleTypeId() {
		return moduleTypeId;
	}

	public void setModuleTypeId(int moduleTypeId) {
		this.moduleTypeId = moduleTypeId;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public int getEnvironmentId() {
		return environmentId;
	}

	public void setEnvironmentId(int environmentId) {
		this.environmentId = environmentId;
	}

	public int getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(int applicationId) {
		this.applicationId = applicationId;
	}

	public String getIncludeUrlPattern() {
		return includeUrlPattern;
	}

	public void setIncludeUrlPattern(String includeUrlPattern) {
		this.includeUrlPattern = includeUrlPattern;
	}

	public String getTestUrlPattern() {
		return testUrlPattern;
	}

	public void setTestUrlPattern(String testUrlPattern) {
		this.testUrlPattern = testUrlPattern;
	}

	public String getStartUrl() {
		return startUrl;
	}

	public void setStartUrl(String startUrl) {
		this.startUrl = startUrl;
	}

	public String getStaticUrls() {
		return staticUrls;
	}

	public void setStaticUrls(String staticUrls) {
		this.staticUrls = staticUrls;
	}

	public int getStaticUrlCount() {
		return staticUrlCount;
	}

	public void setStaticUrlCount(int staticUrlCount) {
		this.staticUrlCount = staticUrlCount;
	}

	public ModuleType getModuleType() {
		return moduleType;
	}

	public void setModuleType(ModuleType moduleType) {
		this.moduleType = moduleType;
	}

	public void setStaticUrlJsonData(ArrayList<String> filecontent,
			String filePath) {
		JsonReaderWriter<ArrayList<String>> jsonReaderWriter = new JsonReaderWriter<ArrayList<String>>();
		try {
			jsonReaderWriter.writeJsonObjectToFile(filecontent, filePath);
			this.staticUrls = filePath;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<String> getStaticUrlJsonData() {
		List<String> staticUrlList = new ArrayList<String>();
		try {
			staticUrlList = getStaticUrlJsonData(this.staticUrls);
		} catch (Exception e) {

		}

		return staticUrlList;
	}

	public List<String> getStaticUrlJsonData(String filePath) {
		JsonReaderWriter<ArrayList<String>> jsonReaderWriter = new JsonReaderWriter<ArrayList<String>>();
		List<String> staticUrlList = new ArrayList<String>();
		try {
			if ( MintFileUtils.isFileExists(filePath) ) {
				staticUrlList = jsonReaderWriter.readJsonObjectFromFile(
						new ArrayList<String>(), filePath);
			}
		} catch (Exception e) {
			logger.error("Exception while reading Static Url JSON file.");
			logger.error("stack Trace :" + ExceptionUtils.getStackTrace(e));
		}

		return staticUrlList;
	}

	public List<TransactionTestCase> getTransactionTestCaseList() {
		return transactionTestCaseList;
	}

	public void setTransactionTestCaseList(
			List<TransactionTestCase> transactionTestCaseList) {
		this.transactionTestCaseList = transactionTestCaseList;
	}
	
	public String getTestCaseDirectory() {
		String filePath = null;
		try {
			if (this.getModuleTypeId() == ModuleType.TRANSACTION) {
				if (this.getTransactionTestCaseList() != null
						&& this.getTransactionTestCaseList().size() > 0) {
					filePath = this.getTransactionTestCaseList().get(0)
							.getTestCasePath();
					filePath = StringUtils.substringBeforeLast(filePath, "\\");
				}
			}
		} catch (Exception e) {
			logger.error("Exception while reading Static Url JSON file.");
			logger.error("stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		return filePath;
	}
}
