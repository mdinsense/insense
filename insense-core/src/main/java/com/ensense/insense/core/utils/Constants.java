package com.ensense.insense.core.utils;

public class Constants {

	public static final String HAR_MAPPED_FILE_PATH = "Mapped";
	public static final String REPORTS_DIRECTORY_PATH = "Reports";
	public static final int PARENT_MENU_COUNT = 6;
	public static final int MAX_NO_OF_TIME_PAGE_TESTED = 2;
	public static final int MODULE_ALL = 0;
	public static final String BATCH_FILE_KEY = "mint.file.suit.directory";
	public static final String BATCH_FILE_NAME = "MINT_BATCH_ALL_SUITS";
	public static final String BATCH_FILE_HEADERS = "mint.batch.file.headers";
	public static final String BATCH_FILE_ENABLED_KEY = "mint.suit.batchfile.update.enable";

	public interface UiTesting {
		public static final String VIEW = "uitesting/UiTestingSetup";
		public static final String REDIRECT_VIEW = "redirect:/UiTestingSetup.ftl";
		public static final int UI_APPLICATION_SETUP = 1;
		public static final int UI_ENVIRONMENT_SETUP = 2;
		public static final int UI_INCLUDE_EXCLUDE_URL_SETUP = 3;
		public static final int UI_LOGIN_USER_SETUP = 4;
		public static final int UI_APPLICATION_CONFIG_SETUP = 5;
		public static final int UI_HTML_REPORTS_CONFIG_SETUP = 6;
		public static final int UI_MODULE_SETUP = 7;
		public static final int UI_ANALYTICS_EXCLUDE_SETUP = 8;
	}
	
	public interface UserAccess {
		public static final String VIEW = "usermanagement/ManageUserAccess";
		public static final String REDIRECT_VIEW = "redirect:/UserAccessSetup.ftl";
		public static final int MANAGE_USER_SETUP = 1;
		public static final int MANAGE_GROUP_SETUP = 2;
		public static final int MANAGE_MENU_SETUP = 3;
		public static final int MANAGE_ADDNEWMENU_SETUP = 4;
		public static final int MANAGE_ADDNEWFUNCTIONALITY_SETUP = 5;
	}

	public interface Functionality {
		public static final int SHOW_ALL_UI_SUITS = 4;
		public static final int SHOW_SET_ALIAS = 5;
		public static final int MISCELLANEOUSTOOL_CLEAR_CACHE = 6;
		public static final int MANAGE_SUITS = 8;
		public static final int UPDATE_JOB_STATUS = 9;
		public static final int MONITOR_JOB_STATUS = 10;
		public static final int ESB_PING_ADD = 14;
		public static final int MINTV4_SUIT = 15;
	}

	public interface MiscellaneousTools {
		public static final String HOME = "miscellaneous/MiscellaneousToolsHome";
		public static final String CLEAR_CACHE_URL = "miscellaneous/CacheJobExecution";
		public static final String CLEAR_CACHE_REDIRECT = "redirect:/ClearCacheHome.ftl";
		public static final String STATUS_NEW = "New";
		public static final String STATUS_RUNNING = "Running";
		public static final String STATUS_COMPLETED = "Completed";
		public static final String STATUS_FAILED = "Failed";
	}

	public static final int WEB_APPLICATION_TESTING = 1;

	public static final Integer COMPARE_WITH_PREVIOUS_SCHEDULE_EXECUTION = -1;

	public interface ParentMenu {
		public static final int CONFIGURE_TEST = 3;
	}

	public interface Menu {
		public static final int FUNCTIONAL = 8;
		public static final int ANALYTICS = 9;
		public static final int BROKEN_LINK_CHECK = 10;
		public static final int AKAMAI = 11;
		public static final int WEBSERVICE = 12;
		public static final int FINDTEXTIMAGE = 15;
	}

	public interface WebserviceTestingAdmin {
		public static final String VIEW = "webservice/WebserviceSetup";
		public static final String REDIRECT_VIEW = "redirect:/WebserviceSetup.ftl";
		public static final int ADD_WEBSERVICE = 1;
	/*	public static final int CREATE_WS_TESTSUITE = 3;
		public static final int CREATE_DATASET = 2;*/
		public static final Integer RELOAD_WEBSERVICES = 2;
		public static final Integer ESB_PING_TEST = 5;
		public static final Integer ESB_PING_RESULTS = 6;
		public static final int SCHEDULE_WEBSERVICE = 8;
		public static final int WEBSERVICE_REPORTS = 7;
		public static final String WEBSERVICE_SUIT_COMPLETE_NOTIFICATION_MSG = "mint.webservice.suit.email.subject";
	}
	
	public interface WebserviceTest {
		public static final String VIEW = "webservice/WebserviceTestingSetup";
		public static final String REDIRECT_VIEW = "redirect:/WebserviceTestingSetup.ftl.ftl";
	
		public static final int CREATE_WS_TESTSUITE = 3;
		public static final int CREATE_DATASET = 2;
		
	}

	public interface ESBServiceTesting {
		public static final String VIEW = "miscellaneous/ESBServiceSetupHome";
		public static final String REDIRECT_VIEW = "redirect:/ESBServiceSetupHome.ftl";
		public static final Integer ESB_PING_TEST = 1;
		public static final Integer ESB_PING_RESULTS = 2;
	}

	public interface FileSuitSchedulerJob {
		public static final String TXT = ".txt";
		public static final String COPY_COMPLETE = "_copy_complete";
		public static final String SCHEDULED = "_Scheduled_";
		public static final String PROCESSING = "PROCESSING";
		public static final String COMPARE = "compare";
		public static final String ERROR = "ERROR";
		public static final String ERROR_LOG = "ERROR_LOG";
		public static final String FOR_SCHEDULE = "for_schedule";
		public static final String CONFIGKEY_DIRECTORY = "mint.file.suit.directory";
		public static final String CONFIGKEY_SENDER = "mint.email.sender";
		public static final String CONFIGKEY_SUBJECT = "mint.file.suit.email.subject";
		public static final String CONFIGKEY_HOST = "mint.email.smtp.host";
		public static final String CONFIGKEY_SCHEDULED = "mint.file.suit.email.scheduled";
		public static final String CONFIGKEY_NOACCESS = "mint.file.suit.email.noaccess";
		public static final String CONFIGKEY_NOTEXISTS = "mint.file.suit.email.notexists";
		public static final String CONFIGKEY_NOTSCHEDULED = "mint.file.suit.email.notscheduled";
		public static final String CONFIGKEY_INVALID_SUIT = "mint.file.suit.name.invalid";
		public static final String CONFIGKEY_INVALID_SCHEDULE = "mint.file.suit.schedule.invalid";
		public static final String CONFIGKEY_INVALID_BASELINE_SCHEDULE = "mint.file.suit.baseline.schedule.invalid";
		public static final String CONFIGKEY_INVALID_BASELINE_DATE = "mint.file.suit.baseline.invalid";
		public static final String CONFIGKEY_INVALID_USER = "mint.file.suit.invalid.user";
		public static final String CONFIGKEY_FILE_LOCKED = "mint.file.suit.email.filelocked";
	}

	public interface KEYS {
		public static final String DOT = ".";
		public static final String COMMA = ",";
		public static final String HASH = "#";
		public static final String TILDE = "~";
		public static final String BLANK = "";
		public static final String F_SLASH = "/";
		public static final String B_SLASH_DOUBLE = "\\";
		public static final String SEMICOLON = ";";
		public static final String EQUALS = "=";
		public static final String UNDERSCORE = "_";
	}

	public interface SCHEDULER {
		public static final char BY_WEEKLY = 'W';
		public static final char BY_DATE = 'D';
		public static final String WEEKLY_SCHEDULE = "recurrenceWeekly";
		public static final String DATE_SCHEDULE = "recurrenceDate";
		public static final String SCHEDULED_RUN = "Scheduled";
		public static final String FILE_SCHEDULER_ENABLE = "mint.scheduler.filescheduler.enabled";
		public static final String UI_PICKUP_SCHEDULER_ENABLE = "mint.scheduler.uischedule.pickup.enabled";
		public static final String UI_EXECUTION_SCHEDULER_ENABLE = "mint.scheduler.uischedule.execution.enabled";
		public static final String UI_REPORTS_SCHEDULER_ENABLE = "mint.scheduler.uischedule.reports.enabled";
		public static final String UI_COMPARE_SCHEDULER_ENABLE = "mint.scheduler.uischedule.compare.enabled";
		public static final String WEBSERVICE_EXECUTION_SCHEDULER_ENABLE = "mint.scheduler.webservice.execution.enabled";
		public static final String WEBSERVICE_SCHEDULER_ENABLE = "mint.scheduler.webservice.enabled";
		public static final String WEBSERVICE_COMPARE_SCHEDULER_ENABLE = "mint.scheduler.webservice.compare.enabled";
		public static final String WEBSERVICE_REPORTS_NOTIFICATION_MSG = "mint.scheduler.wsreports.email.message";
		public static final String WEBSERVICE_ESBPING_NOTIFICATION_MSG = "mint.scheduler.esbping.email.message";
		public static final String WEBSERVICE_REPORTS_NOTIFICATION_ERROR_MSG = "mint.scheduler.wsreports.error.email.message";
		public static final String WEBSERVICE_SUIT_SUBJECT = "mint.webservice.suit.email.subject";
		public static final String WEBSERVICE_ESBPING_SUBJECT = "mint.webservice.esbping.email.subject";
		public static final String WEBSERVICE_SUIT_FAILED_NOTIFICATION_MSG = "mint.webservice.suit.failed.email.subject";
		public static final String SCHEDULER_STARTED_NOTIFICATION_MSG = "mint.scheduler.started.email.message";
		public static final String SCHEDULER_COMPLETE_NOTIFICATION_MSG = "mint.scheduler.completed.email.message";
		public static final String SCHEDULER_FAILED_NOTIFICATION_MSG = "mint.scheduler.failed.email.message";
		public static final String SCHEDULER_REPORTS_NOTIFICATION_MSG = "mint.scheduler.reports.email.message";
		public static final String SCHEDULER_REPORTS_NOTIFICATION_ERROR_MSG = "mint.scheduler.reports.error.email.message";
		public static final String SUIT_NAME_PLACE_HOLDER = "${suitName}";
		public static final String APPLICATION_NAME_PLACE_HOLDER = "${applicationName}";
		public static final String ENVIRONMENT_NAME_PLACE_HOLDER = "${environmentName}";
		public static final String OPRA_RESET_SCHEDULER_ENABLE = "mint.scheduler.opra.reset.enabled";
		public static final String MVC_OPRA_RESET_SCHEDULER_ENABLE = "mint.scheduler.mvc.opra.reset.enabled";
		public static final String REPORTS_COMPLETE_NOTIFICATION_MSG = "mint.scheduler.reports.email.subject";
		public static final String REPORT_SUMMARY_PLACE_HOLDER = "${reportSummary}";
	}

	public static final String DEFAULT_EMAIL_ADDR = "DManni@tiaa-cref.org";

	public interface GUARDIAN {
		public static final String VIEW = "guardian/GuardianHome";
		public static final String REDIRECT_VIEW = "redirect:/GuardianHome.ftl";
		public static final int ADD_SIGNATURE = 1;
		public static final int SIGNATURE_MONITOR = 2;
		public static final int VIEW_REPORTS = 2;
	}

	public static final String TEST_EXECUTION_UPDATE = "testExecutionUpdate";
	public static final String TEXT_COMPARE_END = "textCompareEnd";
	public static final String TEXT_COMPARE_START = "textCompareStart";
	public static final String HTML_COMPARE_END = "htmlCompareEnd";
	public static final String HTML_COMPARE_START = "htmlCompareStart";
	public static final String IMAGE_COMPARE_END = "imageCompareEnd";
	public static final String IMAGE_COMPARE_START = "imageCompareStart";
	public static final String EXECUTION_STATUS_UPDATE = "executionStatus";
	public static final String BROKEN_URL_STATUS_START = "brokenUrlStatusStart";
	public static final String TEXTORIMAGE_REPORT_STATUS_START = "textOrImageStatusStart";
	public static final String TEXTORIMAGE_REPORT_STATUS_END = "textOrImageStatusEnd";
	public static final String ANALYTICS_REPORT_STATUS_START = "analyticsReportStart";
	public static final String ANALYTICS_REPORT_STATUS_END = "analyticsReportEnd";
	public static final String BROKEN_URL_STATUS_END = "brokenUrlStatusEnd";
	public static final String COMPARE_NOT_APPLICABLE = "compareNotApplicable";
	public static final String MODULE_XREF = "moduleXref.obj";
	public static final String FEEDBACK_DET = "feedbackDetails.obj";
	public static final String MINTV4 = "MintV4";
	public static final int DEFAULT_WAIT_TIME = 3100;
	
	public interface OPRA {
		public static final String VIEW = "miscellaneous/OpraReset";
		public static final String REDIRECT_VIEW = "redirect:/OpraReset.ftl";
	}
	
	public interface QUICK_TEST {
		public static final String VIEW = "miscellaneous/QuickTestConfig";
		public static final String REDIRECT_VIEW = "redirect:/QuickTest.ftl";
		public static final int QUICK_TEST = 1;
		public static final int QUICK_TEST_CONFIG = 2;
	}

	public interface MVCOPRA {
		public static final String VIEW = "miscellaneous/OpraMVCReset";
		public static final String REDIRECT_VIEW = "redirect:/OpraMVCReset.ftl";
	}

	public interface USAGEREPORTS {
		public static final String VIEW = "common/UsageReports";
		public static final String REDIRECT_VIEW = "redirect:/UsageReports.ftl";
	}
	
	public interface ARCHIVEDATA {
		public static final String VIEW = "common/ArchiveData";
		public static final String REDIRECT_VIEW = "redirect:/ArchiveData.ftl";
	}

	public enum SolutionType {
		UI_TESTING(1, "UI Testing"),
		WEBSERVICE(2, "Web Service"), 
		MISCELANEOUS(3, "Miscellaneous Tools");

		private final int solutionTypeId;
		private final String solutionTypeName;

		SolutionType(int solutionTypeId, String solutionTypeName) {
			this.solutionTypeId = solutionTypeId;
			this.solutionTypeName = solutionTypeName;
		}

		public int getSolutionTypeId() {
			return this.solutionTypeId;
		}

		public String getSolutionTypeName() {
			return this.solutionTypeName;
		}

		public static SolutionType getSolutionType(int solutionTypeId) {
			for (SolutionType status : SolutionType.values()) {
				if (status.getSolutionTypeId() == solutionTypeId) {
					return status;
				}
			}
			// throw an IllegalArgumentException or return null
			throw new IllegalArgumentException(
					"The given number doesn't match any SolutionType.");
		}

		public static String getSolutionTypeName(int solutionTypeId) {
			for (SolutionType status : SolutionType.values()) {
				if (status.getSolutionTypeId() == solutionTypeId) {
					return status.solutionTypeName;
				}
			}
			// throw an IllegalArgumentException or return null
			throw new IllegalArgumentException(
					"The given number doesn't match any SolutionType.");
		}
	}
	
	public enum EnvironmentCategoryENUM {
		ALL(1, "ALL"),
		ST2(2, "ST2"),
		ST4(3, "ST4"),
		AT(4, "AT"),
		PROD_FIX(5, "PROD_FIX"),
		PROD(6, "PROD"),
		DEV_INT(7, "DEV_INT");


		private final int environmentCategoryId;
		private final String environmentCategoryName;

		EnvironmentCategoryENUM(int environmentCategoryId, String environmentCategoryName) {
			this.environmentCategoryId = environmentCategoryId;
			this.environmentCategoryName = environmentCategoryName;
		}

		public int getEnvironmentCategoryId() {
			return environmentCategoryId;
		}


		public String getEnvironmentCategoryName() {
			return environmentCategoryName;
		}


		public static EnvironmentCategoryENUM getEnvironmentCategory(int environmentCategoryId) {
			for (EnvironmentCategoryENUM status : EnvironmentCategoryENUM.values()) {
				if (status.getEnvironmentCategoryId() == environmentCategoryId) {
					return status;
				}
			}
			// throw an IllegalArgumentException or return null
			throw new IllegalArgumentException(
					"The given number doesn't match any environment.");
		}

		public static String getEnvironmentName(int environmentCategoryId) {
			for (EnvironmentCategoryENUM status : EnvironmentCategoryENUM.values()) {
				if (status.getEnvironmentCategoryId() == environmentCategoryId) {
					return status.environmentCategoryName;
				}
			}
			// throw an IllegalArgumentException or return null
			throw new IllegalArgumentException(
					"The given number doesn't match any environment.");
		}
	}
	
	public enum Application {
		UD_OPRA(1, "UD OPRA"),
		MVC_OPRA(2, "Mvc Opra");

		private final int applicationId;
		private final String applicationName;

		Application(int applicationId, String applicationName) {
			this.applicationId = applicationId;
			this.applicationName = applicationName;
		}

		public int getApplicationId() {
			return this.applicationId;
		}

		public String getApplicationName() {
			return this.applicationName;
		}

		public static Application getApplication(int applicationId) {
			for (Application status : Application.values()) {
				if (status.getApplicationId() == applicationId) {
					return status;
				}
			}
			// throw an IllegalArgumentException or return null
			throw new IllegalArgumentException(
					"The given number doesn't match any Application.");
		}

		public static String getApplicationName(int applicationId) {
			for (Application status : Application.values()) {
				if (status.getApplicationId() == applicationId) {
					return status.getApplicationName();
				}
			}
			// throw an IllegalArgumentException or return null
			throw new IllegalArgumentException(
					"The given number doesn't match any Application.");
		}
	}
	
    public enum FormatType {
        TEXT, INTEGER, FLOAT, DATE, MONEY, PERCENTAGE
    }
    
    public interface UsageReportConstants {
    	public static final String ALL = "0"; 
    	public static final String ALL_ENVIRONMENT = "1";
    	public static final int DETAILED_REPORT = 2;
    	public static final int REPORT_CHART = 1;
    	public static final int FUNCTIONALITY = 3;
    	public static final int ENVIRONMENT = 2;
    	public static final int GROUP = 1;
     	public static final String FUNCTIONALITY_ID = "functionalityTypeId";
    	public static final String ENVIRONMENT_ID = "environmentId";
    	public static final String GROUP_ID = "groupId";
    }
    
    public interface ModuleType {
    	public static final int TRANSACTION = 3;
    }
    
    public interface FILE {
    	public static String CLASS = ".class";
    	public static String XLS = ".xls";
    	public static String XLSX = ".xlsx";
    	public static String TXT = ".txt";
    	public static String JAVA = ".java";
    	public static String HTML = ".html";
    	public static String ZIP = ".zip";
    }
    
	public enum ReportSelection {
		
		ANALYTICS_REPORT(1, "Analytics Report"),
		ANALYTICS_ERROR_REPORT(2, "Analytics ERROR Report"), 
		BROKEN_URL_REPORT(3, "Broken URL Report"),
		BROKEN_URL_WITH_RESOURCES_REPORT(4, "Broke URL with resources"),
		LOAD_TIME_ATTRIBUTES_REPROT(5, "LOAD TIME ATTRIBUTES"),
		REGRESSION_COMPARISON_REPORT(6, "COMPARISON REPORT");

		private final int reportSelectionId;
		private final String reportSelectionName;

		ReportSelection(int reportSelectionId, String reportSelectionName) {
			this.reportSelectionId = reportSelectionId;
			this.reportSelectionName = reportSelectionName;
		}

		public int getReportSelectionId() {
			return this.reportSelectionId;
		}

		public String getReportSelectionName() {
			return this.reportSelectionName;
		}

		public static ReportSelection getReportSelection(int reportSelectionId) {
			for (ReportSelection status : ReportSelection.values()) {
				if (status.getReportSelectionId() == reportSelectionId) {
					return status;
				}
			}
			// throw an IllegalArgumentException or return null
			throw new IllegalArgumentException(
					"The given number doesn't match any ReportSelection.");
		}

		public static String getReportSelectionName(int reportSelectionId) {
			for (ReportSelection status : ReportSelection.values()) {
				if (status.getReportSelectionId() == reportSelectionId) {
					return status.reportSelectionName;
				}
			}
			// throw an IllegalArgumentException or return null
			throw new IllegalArgumentException(
					"The given number doesn't match any ReportSelection.");
		}
	}
	
public enum ReportPattern {

		ANALYTICS_REPORT_PATTERN(1, "MINT_ANALYTICS_REPORT"),
		ANALYTICS_ERROR_REPORT_PATTERN(2, "MINT_ANALYTICS_ERROR_REPORT"), 
		BROKEN_URL_REPORT_PATTERN(3, "MINT_BROKEN_URL_REPORT"),
		BROKEN_URL_WITH_RESOURCES_REPORT_PATTERN(4, "MINT_BROKEN_URL_WITH_RESOURCES_REPORT"),
		LOAD_TIME_ATTRIBUTES_REPROT_PATTERN(5, "MINT_PAGE_LOAD_TIME_ATTRIBUTES_REPORT"),
		REGRESSION_COMPARISON_REPORT_PATTERN(6, "MINT_REGRESSION_COMPARISON_REPORT"),
		IMAGE_TEXT_COMPARISON_REPORT_PATTERN(7, "MINT_TEXT_IMAGE_REPORTS_DATA"),
		NORMALIZED_ZIP(8, "NormalizedImageTextdirectory.zip"),
		IMAGE_TEXT_COMPARISON_REPORT_BASELINE_PATTERN(9, "MINT_TEXT_IMAGE_REPORTS_BASELINE_DATA");
		
		private final int reportPatternId;
		private final String reportPatternName;

		ReportPattern(int reportPatternId, String reportPatternName) {
			this.reportPatternId = reportPatternId;
			this.reportPatternName = reportPatternName;
		}

		public int getReportPatternId() {
			return this.reportPatternId;
		}

		public String getReportPatternName() {
			return this.reportPatternName;
		}

		public static ReportPattern getReportPattern(int reportPatternId) {
			for (ReportPattern status : ReportPattern.values()) {
				if (status.getReportPatternId() == reportPatternId) {
					return status;
				}
			}
			// throw an IllegalArgumentException or return null
			throw new IllegalArgumentException(
					"The given number doesn't match any ReportPattern.");
		}

		public static String getReportPatternName(int reportPatternId) {
			for (ReportPattern status : ReportPattern.values()) {
				if (status.getReportPatternId() == reportPatternId) {
					return status.reportPatternName;
				}
			}
			// throw an IllegalArgumentException or return null
			throw new IllegalArgumentException(
					"The given number doesn't match any ReportPattern.");
		}
	}

	public interface BrokenReportSelection {
		public static final String BrokenLink = "BrokenLink";
		public static final String BrokenLinkResources = "BrokenLinkResources";
		public static final String LoadTimeAttribute = "LoadTimeAttributes";
		public static final String ALL_IMAGE_DIR = "\\AllImages\\";
	}
}
