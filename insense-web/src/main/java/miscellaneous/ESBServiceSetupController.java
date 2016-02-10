package miscellaneous;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONObject;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cts.mint.common.entity.Users;
import com.cts.mint.common.utils.CommonUtils;
import com.cts.mint.common.utils.Constants;
import com.cts.mint.common.utils.ExecutionStatus;
import com.cts.mint.common.utils.Constants.ESBServiceTesting;
import com.cts.mint.common.utils.Constants.Functionality;
import com.cts.mint.common.utils.Constants.SCHEDULER;
import com.cts.mint.common.utils.UserServiceUtils;
import com.cts.mint.uitesting.service.EnvironmentService;
import com.cts.mint.util.BrowserDriverLoaderUtil;
import com.cts.mint.util.DateTimeUtil;
import com.cts.mint.util.DateUtil;
import com.cts.mint.util.EmailUtil;
import com.cts.mint.util.FileDirectoryUtil;
import com.cts.mint.webservice.entity.WSPingResults;
import com.cts.mint.webservice.entity.WSPingSchedule;
import com.cts.mint.webservice.entity.WebservicesPingTest;
import com.cts.mint.webservice.model.ServiceStatus;
import com.cts.mint.webservice.model.ServiceUrl;
import com.cts.mint.webservice.model.WebserviceSetupForm;
import com.cts.mint.webservice.service.WebserviceTestingService;
import com.cts.mint.webservice.util.ESBWebServiceCrawler;
import com.cts.mint.webservice.util.WebserviceUtil;
@Controller
public class ESBServiceSetupController {

	@Autowired
	// Must needed
	private ServletContext servletContext;

	@Autowired
	private HttpServletRequest context;

	@Autowired
	private MessageSource configProperties;

	@Autowired
	public HttpSession mysession;

	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private MessageSource esbpingUrlProperty;

	@Autowired
	private WebserviceTestingService webserviceTestingService;

	@Autowired
	private EnvironmentService environmentService;

	private static Logger logger = Logger
	.getLogger(ESBServiceSetupController.class);


	@RequestMapping(value = { "/ESBServiceSetupHome" }, method = RequestMethod.GET)
	public String eSBServiceSetupHomeGet(Model model) {

		logger.debug("Entry: WebserviceSetupGet, WebserviceSetupForm->"
				+ model.asMap().get("webserviceSetupForm"));

		WebserviceSetupForm webserviceSetupForm = null;
		
		if (null != model.asMap()
				&& model.asMap().containsKey("webserviceSetupForm")) {
			webserviceSetupForm = (WebserviceSetupForm) model.asMap().get(
			"webserviceSetupForm");
		} else {
			webserviceSetupForm = new WebserviceSetupForm();
			webserviceSetupForm.setSetupTabNumber(getUiSetupTabNumber(model
					.asMap()));
		}
		try {
			switch (webserviceSetupForm.getSetupTabNumber()) {
			case 1:
				model.addAttribute("webservicesPingTestList",
						webserviceTestingService.getWebservicesPingTestList());
				model.addAttribute("emailAddr", Constants.DEFAULT_EMAIL_ADDR);
				model.addAttribute("environmentCategoryList",environmentService.
						getEnvironmentCategoryGroupRefByGroupId(UserServiceUtils
								.getCurrentUserGroupIdFromRequest(context)));
				model.addAttribute("isAddServiceAccessAvailable",
						UserServiceUtils.isUserHasAccessToFunctionality(context, Functionality.ESB_PING_ADD));
				break;
			}
		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured in WebserviceSetupGet");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		model.addAttribute("webserviceSetupForm", webserviceSetupForm);

		logger.debug("Exit: WebserviceSetupGet, webserviceSetupForm->"
				+ webserviceSetupForm);
		return ESBServiceTesting.VIEW;
	}

	@RequestMapping(value = { "/ESBServiceSetupHome" }, method = RequestMethod.POST)
	public String eSBServiceSetupHome(
			@ModelAttribute WebserviceSetupForm webserviceSetupForm,
			Model model, RedirectAttributes redirAttr) {
		logger.debug("Entry: uiTestingSetup, uiTestingSetupForm->"
				+ webserviceSetupForm);
		try {

			switch (webserviceSetupForm.getSetupTabNumber()) {
			case 1:
				redirAttr.addFlashAttribute("webservicesPingTestList",
						webserviceTestingService.getWebservicesPingTestList());
				redirAttr.addFlashAttribute("emailAddr", Constants.DEFAULT_EMAIL_ADDR);
				redirAttr.addFlashAttribute("environmentCategoryList",environmentService.
						getEnvironmentCategoryGroupRefByGroupId(UserServiceUtils
								.getCurrentUserGroupIdFromRequest(context)));	
				redirAttr.addFlashAttribute("isAddServiceAccessAvailable",
						UserServiceUtils.isUserHasAccessToFunctionality(context, Functionality.ESB_PING_ADD));
				break;
			}
			
		} catch (Exception e) {
			logger.error("Exception in WebserviceSetup.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}

		logger.debug("Exit: uiTestingSetup");
		redirAttr.addFlashAttribute("webserviceSetupForm", webserviceSetupForm);
		return ESBServiceTesting.REDIRECT_VIEW;
	}

	private int getUiSetupTabNumber(Map<?, ?> map) {
		int setUpTabNumber = 1;

		if (map.containsKey("setupTab")) {
			WebserviceSetupForm webserviceSetupForm = (WebserviceSetupForm) map
			.get("setupTab");
			setUpTabNumber = webserviceSetupForm.getSetupTabNumber();
		}

		return setUpTabNumber;
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/GetServiceOfEsbPing", method = RequestMethod.POST)
	public @ResponseBody
	String getServiceOfEsbPing( @ModelAttribute("model") ModelMap model,
			WebserviceSetupForm webserviceSetupForm ) {
		logger.info("Entry: getServiceOfEsbPing");
		
		JSONObject obj = new JSONObject();
		StringWriter out = new StringWriter();
		List<WebservicesPingTest> webservicesPingTestList = null;
		try {
			webservicesPingTestList = webserviceTestingService.getWebservicesPingTestList();
			for (WebservicesPingTest webservicesPingTest : webservicesPingTestList) {
				obj.put(webservicesPingTest.getEsbServiceId(), webservicesPingTest.getServiceName());
			}
			obj.writeJSONString(out);

		} catch (Exception e) {
			logger.error("Exception in GetServiceOfEsbPing.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
	logger.info("Exit: getServiceOfEsbPing");
	return out.toString();
	}
	
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/SaveESBPingTestDetails", method = RequestMethod.POST)
	public String saveESBPingTestDetails(RedirectAttributes redirAttr,
			Model model, @ModelAttribute WebserviceSetupForm webserviceSetupForm) {
	
		logger.info("Entry: SaveESBPingTestDetails");
		
		List<WebservicesPingTest> serviceList = null;
		List<WebservicesPingTest> pingServiceList = null;
		List<WebservicesPingTest> duplicateService = null;
		String rootPath = "";
		String fileName = "";
		boolean success = false;

		try {
			webserviceSetupForm.setSetupTabNumber(ESBServiceTesting.ESB_PING_TEST);
			webserviceSetupForm.setScheduleType(null);
			String buttonFlag = webserviceSetupForm.getButtonType();
			serviceList = new ArrayList<WebservicesPingTest>();
			try {

				for (MultipartFile file : webserviceSetupForm.getFile()) {
					if (file.getSize() > 0) {
						byte[] bytes = file.getBytes();
						String name = file.getOriginalFilename();
						// Creating the directory to store file
						rootPath = configProperties.getMessage(
								"serviceNameUploadPath", null,
								Locale.getDefault());
						rootPath = FileDirectoryUtil
								.getAbsolutePath(rootPath, FileDirectoryUtil
										.getMintRootDirectory(configProperties));
						File dir = new File(rootPath);

						if (!dir.exists())
							dir.mkdirs();

						// Create the file on server
						fileName = dir.getAbsolutePath()
								+ File.separator
								+ DateUtil.convertToString(new Date(),
										"yyyy-MM-dd-mm-ss") + "_" + name;
						logger.info("fileName :" + fileName);

						File serverFile = new File(fileName);
						BufferedOutputStream stream = new BufferedOutputStream(
								new FileOutputStream(serverFile));
						stream.write(bytes);
						stream.close();
					}
				}

				FileInputStream fis = new FileInputStream(fileName);
				// Create Workbook instance for xlsx/xls file input stream
				Workbook workbook = null;
				if (fileName.toLowerCase().endsWith("xlsx")) {
					workbook = new XSSFWorkbook(fis);
				} else if (fileName.toLowerCase().endsWith("xls")) {
					workbook = new HSSFWorkbook(fis);
				}
				// Read excel file and store in to the esb_ping_table

				Sheet sheet = workbook.getSheetAt(0);
				Iterator rows = sheet.rowIterator();

				while (rows.hasNext()) {
					WebservicesPingTest pingTestUploadData = new WebservicesPingTest();
					Row row = (Row) rows.next();

					if (row.getCell(0) != null && !row.getCell(0).toString()
									.equals("Service Name")) {
						pingTestUploadData.setServiceName(row.getCell(0)
								.toString());
						serviceList.add(pingTestUploadData);
					}
				}

			} catch (Exception e) {
				logger.error("Exception in SaveESBPingTestDetails.");
				logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
			}
			pingServiceList = webserviceTestingService.getWebservicesPingTestList();

			boolean flag = true;

			if (pingServiceList.size() > 0
					&& buttonFlag.equalsIgnoreCase("uploadRefresh")) {
				success = webserviceTestingService.deletePingServices();

				for (WebservicesPingTest userData : serviceList) {
						duplicateService = webserviceTestingService
							.getDuplicateService(userData.getServiceName());
					if (duplicateService.size() > 0) {
						flag = false;
					} else {
						success = webserviceTestingService
								.insertExcelServiceData(userData);

					}
				}
			} else {
				if (!serviceList.isEmpty()) {
					for (WebservicesPingTest userData : serviceList) {
						duplicateService = webserviceTestingService
								.getDuplicateService(userData.getServiceName());
						logger.info("duplicateService size >> "
								+ duplicateService.size());

						if (duplicateService.size() > 0) {
							flag = false;
						} else {
							success = webserviceTestingService
									.insertExcelServiceData(userData);

						}
					}
				}
			}
			String emailAddr = configProperties.getMessage(
					"mint.webservice.emailrecepients", null,
					Locale.getDefault());
			redirAttr.addFlashAttribute("emailAddr", emailAddr);
			redirAttr.addFlashAttribute("webserviceSetupForm", webserviceSetupForm);
			if (flag && success) {
				redirAttr.addFlashAttribute("success", "All Excel Data imported successfully");
				pingServiceList = webserviceTestingService.getWebservicesPingTestList();
			} else if (!flag && success) {
				redirAttr.addFlashAttribute(
						"success", "Sorry! Duplicate Service Names are not allowed to upload. All the remaining Excel Data imported successfully");
			}
			redirAttr.addFlashAttribute("pingServiceList", pingServiceList);

		} catch (Exception e) {
			logger.error("Exception in SaveESBPingTestDetails.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.info("Exit: ESBPingTest");
		redirAttr.addFlashAttribute("webserviceSetupForm", webserviceSetupForm);
		return ESBServiceTesting.REDIRECT_VIEW;
	}
	
	@RequestMapping(value = "/SubmitESBPingTestDetails", method = RequestMethod.POST)
	public String submitESBPingTestDetails(RedirectAttributes redirAttr,
			Model model, @ModelAttribute WebserviceSetupForm webserviceSetupForm) {
		logger.info("Exit: SubmitESBPingTestDetails");
		
		WSPingSchedule wSPingSchedule = null;
		String userid = "";
		String recurrence = "";
		int insertId = 0;
		boolean result = false;
		List<ServiceUrl> serviceUrlList = new ArrayList<ServiceUrl>();
		try {
			webserviceSetupForm.setSetupTabNumber(ESBServiceTesting.ESB_PING_TEST);
			Users currentUser = (Users) context.getSession().getAttribute("currentMintUser");
			
			
			wSPingSchedule = new WSPingSchedule();
			recurrence = webserviceSetupForm.getRecurrence();
			if (recurrence != null && recurrence.equals("on")) {
				wSPingSchedule.setReocurrence(true);
			} else {
				wSPingSchedule.setReocurrence(false);
			}
			
			
			wSPingSchedule.setUserId(userid);
			wSPingSchedule.setStartDate(new Date());

			String serviceNames = "";

			for (String service : webserviceSetupForm.getServiceNames()) {
				serviceNames = serviceNames + service + ",";
				ServiceUrl serviceUrl = WebserviceUtil.getEsbPingServiceDetails(service,
						webserviceSetupForm, esbpingUrlProperty);
				serviceUrlList.add(serviceUrl);
			}
			
			wSPingSchedule.setEnvironment(webserviceSetupForm.getEnvironment());
			logger.info("serviceUrlList :" + serviceUrlList);

			if (serviceNames.length() > 0) {
				serviceNames = serviceNames.substring(0,
						serviceNames.length() - 1);
			}
			wSPingSchedule.setServiceNames(serviceNames);
			
			DateFormat formatter = new SimpleDateFormat("HH:mm");

			
			
			//String emailaddr = context.getParameter("emailAddrTxt");
//			boolean sendEmail = false;
//			if(webserviceSetupForm.getSendEmail() == null) {
//				webserviceSetupForm.setSendEmail("false");
//			}
//			if(webserviceSetupForm.getSendEmail().equalsIgnoreCase("true")) {
//				 sendEmail = true;
//			} 
			
//				wSPingSchedule.setSendEmail(sendEmail);
			
			
			if ( null != webserviceSetupForm.getEmailRecepients() && webserviceSetupForm.getEmailRecepients().trim().length() > 0 ){
				wSPingSchedule.setEmailIds(currentUser.getEmailId() + "," + webserviceSetupForm.getEmailRecepients());
			}else{
				wSPingSchedule.setEmailIds(currentUser.getEmailId());
			}
			
			
			/*	if(sendEmail) {
					if(emailaddr == null) {
						emailaddr = "";
					}
					wSPingSchedule.setEmailIds(emailaddr);
					for (String triggerOption : webserviceSetupForm.getEmailTriggerOption()){
						if (triggerOption.equals("completion")) {
							wSPingSchedule.setEmailOnlyOnCompletion(true);
						
						} 
						if (triggerOption.equals("failure")) {
							wSPingSchedule.setEmailOnlyOnFailure(true);
							
						}
					}
					wSPingSchedule.setSendEmail(sendEmail);
				}*/
			if (webserviceSetupForm.getScheduleType().equals("Scheduled")) {
				String dateval = webserviceSetupForm.getSchDateTime();
				Time timeValue = new Time(formatter.parse(dateval).getTime());
				wSPingSchedule.setStartTime(timeValue);
				wSPingSchedule.setScheduled(true);

				insertId = webserviceTestingService
						.insertWSPingSchedules(wSPingSchedule);
				if (insertId > 0) {
					result = true;
				}

				if (result) {
					model.addAttribute("succes",
							"Web Service Ping Test Schedule Details are Saved Successfully.");
				} else if (!result) {
					model.addAttribute("error",
							"Sorry! Not able to save Web Service Ping Test Schedule Details.");
				}

			} else {
				// Ondemand execute immediately.

				BrowserDriverLoaderUtil driverLoader = new BrowserDriverLoaderUtil();
				WebDriver driver = driverLoader.getFirefoxDriver();
				ESBWebServiceCrawler esbWebServiceCrawler = new ESBWebServiceCrawler(
						driver);
				for (ServiceUrl serviceUrl : serviceUrlList) {
					if (null != serviceUrl.getEsbPingUrl()
							&& serviceUrl.getEsbPingUrl().length() > 0) {
						String htmlSource = esbWebServiceCrawler
								.getHtmlSourceForEsbPingUrl(serviceUrl
										.getEsbPingUrl());
						logger.info(serviceUrl.getServiceName() + " : "
								+ htmlSource);
						serviceUrl.setHtmlSource(StringEscapeUtils
								.escapeXml(htmlSource));
						serviceUrl.setStatus(WebserviceUtil.esbPingServiceStatus(htmlSource));
						logger.info("serviceStatus :" + serviceUrl.isStatus());
					} else {
						serviceUrl.setServiceFound(false);
					}
				}

				logger.info("serviceUrl :" + serviceUrlList);
				driver.close();

				
					String scheduleType = "OnDemand";
					
					String emailSMTPHost = CommonUtils.getPropertyFromPropertyFile(configProperties, "mint.email.smtp.host");
					String emailFrom = CommonUtils.getPropertyFromPropertyFile(configProperties, "mint.email.sender");
					EmailUtil emailUtil = new EmailUtil(emailSMTPHost, emailFrom);
					
					List<String> recepientList = new ArrayList<String>();
					String[] emailAddr = wSPingSchedule.getEmailIds().split(",");
					recepientList = Arrays.asList(emailAddr);
					//recepientList.add(user.getEmailId());
					String messageContent = "";
					if ( null != recepientList && recepientList.size() > 0 ){
						String subject = CommonUtils.getPropertyFromPropertyFile(configProperties,
								SCHEDULER.WEBSERVICE_ESBPING_SUBJECT);
						subject = subject + " " + ExecutionStatus.COMPLETE.getStatus();
						messageContent = CommonUtils.getPropertyFromPropertyFile(
								configProperties,
								SCHEDULER.WEBSERVICE_ESBPING_NOTIFICATION_MSG);
						messageContent = WebserviceUtil.getEmailContentForESBPingTest(serviceUrlList);				
						emailUtil.sendEmail(recepientList, subject, messageContent);
					}
					
					
					
			
				redirAttr.addFlashAttribute("serviceUrlList", serviceUrlList);
				redirAttr.addFlashAttribute("webserviceSetupForm", webserviceSetupForm);
				
			}
			
		} catch (Exception e) {
			logger.error("Exception in saveESBPingTestDetails.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.info("Exit: SubmitESBPingTestDetails");
		redirAttr.addFlashAttribute("webserviceSetupForm", webserviceSetupForm);
		return ESBServiceTesting.REDIRECT_VIEW;
	}
	
	@RequestMapping(value = "/ESBPingReports", method = RequestMethod.POST)
	public String esbPingReports(RedirectAttributes redirAttr,
			Model model, @ModelAttribute WebserviceSetupForm webserviceSetupForm) {
		logger.info("Entry: ESBPingReports");
		List<WSPingResults> listWSPingResults = null;
		try {
			webserviceSetupForm.setSetupTabNumber(ESBServiceTesting.ESB_PING_RESULTS);
			String environmentId = webserviceSetupForm.getEnvironment();
			String resultsId = webserviceSetupForm.getResultDate();
			listWSPingResults = webserviceTestingService.getESBPingServiceDetails(
					environmentId, resultsId);
			List<ServiceStatus> serviceStatusList = WebserviceUtil.getPingResults(listWSPingResults, messageSource, configProperties);
			serviceStatusList = WebserviceUtil.removeEscapeCharFromXML(serviceStatusList);
			redirAttr.addFlashAttribute("serviceStatusList", serviceStatusList);
		} catch (Exception e) {
			logger.error("Exception in ESBPingReports.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.info("Exit: ESBPingResult");
		redirAttr.addFlashAttribute("webserviceSetupForm", webserviceSetupForm);
		return ESBServiceTesting.REDIRECT_VIEW;
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getESBPingDates", method = RequestMethod.POST)
	public @ResponseBody
	String getESBPingDates( @RequestParam(value = "environment") String environment) 
	{
		JSONObject obj = new JSONObject();
		StringWriter out = new StringWriter();
		List<WSPingResults> wsPingResultsList = null;
			try {
				wsPingResultsList = webserviceTestingService.getESBPingDates(environment);
				for ( WSPingResults wsPingResults: wsPingResultsList ) {
					obj.put(wsPingResults.getWsPingScheduleId(), DateTimeUtil
							.getDateFormatyyyyMMdd(wsPingResults
									.getStartDate()));
				}
				obj.writeJSONString(out);
			} catch (Exception e) {
				logger.error("Exception in getESBPingDates.");
				logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
			}
		logger.info("Exit: getESBPingDates");
		return out.toString();
	}
}
