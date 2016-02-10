package webservice;

import java.io.File;
import java.io.StringWriter;
import java.sql.Clob;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cts.mint.common.entity.Users;
import com.cts.mint.common.service.HomeService;
import com.cts.mint.common.utils.Constants.WebserviceTest;
import com.cts.mint.common.utils.Constants.WebserviceTestingAdmin;
import com.cts.mint.common.utils.UserServiceUtils;
import com.cts.mint.uitesting.entity.Environment;
import com.cts.mint.uitesting.service.EnvironmentService;
import com.cts.mint.util.FileDirectoryUtil;
import com.cts.mint.webservice.entity.WSReports;
import com.cts.mint.webservice.entity.WSResults;
import com.cts.mint.webservice.entity.WebserviceOperations;
import com.cts.mint.webservice.entity.WebserviceSuite;
import com.cts.mint.webservice.entity.Webservices;
import com.cts.mint.webservice.entity.WsEndpointDetails;
import com.cts.mint.webservice.entity.WsOperationHeaderParameters;
import com.cts.mint.webservice.entity.WsOperationParameter;
import com.cts.mint.webservice.entity.WsOperationParameterValue;
import com.cts.mint.webservice.entity.WsOperationXmlParameter;
import com.cts.mint.webservice.entity.WsParameterAndSetId;
import com.cts.mint.webservice.model.SuiteObj;
import com.cts.mint.webservice.model.WSBaselineDate;
import com.cts.mint.webservice.model.WSReportsData;
import com.cts.mint.webservice.model.WSSuiteDetails;
import com.cts.mint.webservice.model.WSSuiteParams;
import com.cts.mint.webservice.model.WebServiceReports;
import com.cts.mint.webservice.model.WebserviceSetupForm;
import com.cts.mint.webservice.model.WsDataset;
import com.cts.mint.webservice.model.WsSuite;
import com.cts.mint.webservice.service.WebserviceTestingService;
import com.cts.mint.webservice.util.RestServiceUtil;
import com.cts.mint.webservice.util.WebserviceUtil;
import com.eviware.soapui.support.types.StringToStringMap;
import com.predic8.wsdl.Definitions;
import com.predic8.wsdl.Operation;
import com.predic8.wsdl.PortType;
import com.predic8.wsdl.WSDLParser;
@Controller
public class WebserviceSetupController {

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

	@Autowired
	HomeService homeService;
	
	private static Logger logger = Logger
	.getLogger(WebserviceSetupController.class);


	@RequestMapping(value = { "/WebserviceSetup" }, method = RequestMethod.GET)
	public String WebserviceSetupGet(Model model) {

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
				model.addAttribute("servicesList",
						webserviceTestingService.getAllWebservices());
				
				model.addAttribute("environmentCategoryList",environmentService.
						getEnvironmentCategoryGroupRefByGroupId(UserServiceUtils
								.getCurrentUserGroupIdFromRequest(context)));
				
				model.addAttribute("endPoint","");
				
				List<String> servicesToBeAdded = WebserviceUtil.
				getServicesToConfigure(configProperties, webserviceTestingService);


				if (servicesToBeAdded == null || servicesToBeAdded.isEmpty()) {
					model.addAttribute("result", "No New Services are available.");
					
				} 
					model.addAttribute("servicesToBeAdded", servicesToBeAdded);
			
					model.addAttribute("servicePaginationCount",
							getServicePaginateCount(servicesToBeAdded));
				break;
			/*case 2:
				model.addAttribute("servicesList",
						webserviceTestingService.getAllWebservices());
				
				model.addAttribute("environmentCategoryList",environmentService.
						getEnvironmentCategoryGroupRefByGroupId(UserServiceUtils
								.getCurrentUserGroupIdFromRequest(context)));
				
				break;
			
			case 3:
				//context.getSession().setAttribute("serviceMap",null);
				model.addAttribute("servicesList", webserviceTestingService.getAllWebservices());
				
				model.addAttribute("environmentCategoryList",environmentService.
						getEnvironmentCategoryGroupRefByGroupId(UserServiceUtils
								.getCurrentUserGroupIdFromRequest(context)));
				model.addAttribute("wsSuiteName", webserviceSetupForm.getWsSuiteName());
				model.addAttribute("environmentId", webserviceSetupForm.getEnvironmentId());
				model.addAttribute("visibleToOtherUsers", "true");
				if(webserviceSetupForm.getXmlInput() != null) {
					model.addAttribute("xmlInput", webserviceSetupForm.getXmlInput());
				}
				else{
					model.addAttribute("xmlInput", "false");
				}
				
				if(webserviceSetupForm.getRawInput() != null) {
					model.addAttribute("rawInput", webserviceSetupForm.getRawInput());
				}
				else{
					model.addAttribute("rawInput", "true");
				}
				break;*/
			case 2:
				model.addAttribute("wservicesList", webserviceTestingService
						.getAllWebservices());
				break;
			

			case 7:
				model.addAttribute("environmentCategoryList",environmentService.
						getEnvironmentCategoryGroupRefByGroupId(UserServiceUtils
								.getCurrentUserGroupIdFromRequest(context)));
				break;
			case 8:
				model.addAttribute("environmentCategoryList",environmentService.
						getEnvironmentCategoryGroupRefByGroupId(UserServiceUtils
								.getCurrentUserGroupIdFromRequest(context)));
				break;
			}
		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured in WebserviceSetupGet");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		model.addAttribute("webserviceSetupForm", webserviceSetupForm);

		logger.debug("Exit: WebserviceSetupGet, webserviceSetupForm->"
				+ webserviceSetupForm);
		return WebserviceTestingAdmin.VIEW;
	}

	
	@RequestMapping(value = { "/WebserviceTestingSetup" }, method = RequestMethod.GET)
	public String WebserviceTestingSetupGet(Model model) {

		logger.debug("Entry: WebserviceTestingSetup, WebserviceSetupForm->"
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
				model.addAttribute("servicesList",
						webserviceTestingService.getAllWebservices());
				
				model.addAttribute("environmentCategoryList",environmentService.
						getEnvironmentCategoryGroupRefByGroupId(UserServiceUtils
								.getCurrentUserGroupIdFromRequest(context)));
				
				break;
			
			case 2:
				//context.getSession().setAttribute("serviceMap",null);
				model.addAttribute("servicesList", webserviceTestingService.getAllWebservices());
				
				model.addAttribute("environmentCategoryList",environmentService.
						getEnvironmentCategoryGroupRefByGroupId(UserServiceUtils
								.getCurrentUserGroupIdFromRequest(context)));
				model.addAttribute("wsSuiteName", webserviceSetupForm.getWsSuiteName());
				model.addAttribute("environmentId", webserviceSetupForm.getEnvironmentId());
				model.addAttribute("visibleToOtherUsers", "true");
				if(webserviceSetupForm.getXmlInput() != null) {
					model.addAttribute("xmlInput", webserviceSetupForm.getXmlInput());
				}
				else{
					model.addAttribute("xmlInput", "false");
				}
				
				if(webserviceSetupForm.getRawInput() != null) {
					model.addAttribute("rawInput", webserviceSetupForm.getRawInput());
				}
				else{
					model.addAttribute("rawInput", "true");
				}
				break;
			
			
			}
		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured in WebserviceSetupGet");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		model.addAttribute("webserviceSetupForm", webserviceSetupForm);

		logger.debug("Exit: WebserviceTestingSetup, webserviceSetupForm->"
				+ webserviceSetupForm);
		return WebserviceTest.VIEW;
	}

	
	
	@RequestMapping(value = { "/WebserviceSetup" }, method = RequestMethod.POST)
	public String WebserviceSetup(
			@ModelAttribute WebserviceSetupForm webserviceSetupForm,
			Model model, RedirectAttributes redirAttr) {
		logger.debug("Entry: uiTestingSetup, uiTestingSetupForm->"
				+ webserviceSetupForm);
		try {

			switch (webserviceSetupForm.getSetupTabNumber()) {
			case 1:
				redirAttr.addFlashAttribute("servicesList",
						webserviceTestingService.getAllWebservices());
				
				redirAttr.addFlashAttribute("environmentCategoryList",environmentService.
						getEnvironmentCategoryGroupRefByGroupId(UserServiceUtils
								.getCurrentUserGroupIdFromRequest(context)));
				
				redirAttr.addFlashAttribute("endPoint","");
				
				List<String> servicesToBeAdded = WebserviceUtil.
				getServicesToConfigure(configProperties, webserviceTestingService);


				if (servicesToBeAdded == null || servicesToBeAdded.isEmpty()) {
					redirAttr.addFlashAttribute("result", "No New Services are available.");
				} 
					redirAttr.addFlashAttribute("servicesToBeAdded", servicesToBeAdded);
					redirAttr.addFlashAttribute("servicePaginationCount",
							getServicePaginateCount(servicesToBeAdded));
			
				break;
			/*case 2:
				redirAttr.addFlashAttribute("servicesList",
						webserviceTestingService.getAllWebservices());
				
				redirAttr.addFlashAttribute("environmentCategoryList",environmentService.
						getEnvironmentCategoryGroupRefByGroupId(UserServiceUtils
								.getCurrentUserGroupIdFromRequest(context)));
				
				break;
			
			case 3:
				//context.getSession().setAttribute("serviceMap",null);
				redirAttr.addFlashAttribute("servicesList", webserviceTestingService.getAllWebservices());
				
				redirAttr.addFlashAttribute("environmentCategoryList",environmentService.
						getEnvironmentCategoryGroupRefByGroupId(UserServiceUtils
								.getCurrentUserGroupIdFromRequest(context)));
				redirAttr.addFlashAttribute("wsSuiteName", webserviceSetupForm.getWsSuiteName());
				redirAttr.addFlashAttribute("visibleToOtherUsers", webserviceSetupForm.getVisibleToOtherUsers());
				redirAttr.addFlashAttribute("xmlInput", webserviceSetupForm.getXmlInput());
				redirAttr.addFlashAttribute("rawInput", webserviceSetupForm.getRawInput());
				redirAttr.addFlashAttribute("environmentId", webserviceSetupForm.getEnvironmentId());
				if(webserviceSetupForm.getXmlInput() != null) {
					model.addAttribute("xmlInput", webserviceSetupForm.getXmlInput());
				}
				else{
					model.addAttribute("xmlInput", "false");
				}
				
				if(webserviceSetupForm.getRawInput() != null) {
					model.addAttribute("rawInput", webserviceSetupForm.getRawInput());
				}
				else{
					model.addAttribute("rawInput", "true");
				}
				break;*/
			
			case 2:
				redirAttr.addFlashAttribute("wservicesList", webserviceTestingService
						.getAllWebservices());
				break;
	/*		case 5:
				redirAttr.addFlashAttribute("webservicesPingTestList",
						webserviceTestingService.getWebservicesPingTestList());
				redirAttr.addFlashAttribute("emailAddr", Constants.DEFAULT_EMAIL_ADDR);
				redirAttr.addFlashAttribute("environmentCategoryList",environmentService.
						getEnvironmentCategoryGroupRefByGroupId(UserServiceUtils
								.getCurrentUserGroupIdFromRequest(context)));		
				break;*/
			case 7:
				redirAttr.addFlashAttribute("environmentCategoryList",environmentService.
						getEnvironmentCategoryGroupRefByGroupId(UserServiceUtils
								.getCurrentUserGroupIdFromRequest(context)));
				break;
				
			case 8:
				redirAttr.addFlashAttribute("environmentCategoryList",environmentService.
						getEnvironmentCategoryGroupRefByGroupId(UserServiceUtils
								.getCurrentUserGroupIdFromRequest(context)));
				break;
			}
			
		} catch (Exception e) {
			logger.error("Exception in WebserviceSetup.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}

		logger.debug("Exit: uiTestingSetup");
		redirAttr.addFlashAttribute("webserviceSetupForm", webserviceSetupForm);
		return WebserviceTestingAdmin.REDIRECT_VIEW;
	}
	
	@RequestMapping(value = { "/WebserviceTestingSetup" }, method = RequestMethod.POST)
	public String WebserviceTestingSetup(
			@ModelAttribute WebserviceSetupForm webserviceSetupForm,
			Model model, RedirectAttributes redirAttr) {
		logger.debug("Entry: WebserviceTestingSetup, uiTestingSetupForm->"
				+ webserviceSetupForm);
		try {

			switch (webserviceSetupForm.getSetupTabNumber()) {
			
			case 1:
				redirAttr.addFlashAttribute("servicesList",
						webserviceTestingService.getAllWebservices());
				
				redirAttr.addFlashAttribute("environmentCategoryList",environmentService.
						getEnvironmentCategoryGroupRefByGroupId(UserServiceUtils
								.getCurrentUserGroupIdFromRequest(context)));
				
				break;
			
			case 2:
				//context.getSession().setAttribute("serviceMap",null);
				redirAttr.addFlashAttribute("servicesList", webserviceTestingService.getAllWebservices());
				
				redirAttr.addFlashAttribute("environmentCategoryList",environmentService.
						getEnvironmentCategoryGroupRefByGroupId(UserServiceUtils
								.getCurrentUserGroupIdFromRequest(context)));
				redirAttr.addFlashAttribute("wsSuiteName", webserviceSetupForm.getWsSuiteName());
				redirAttr.addFlashAttribute("visibleToOtherUsers", webserviceSetupForm.getVisibleToOtherUsers());
				/*redirAttr.addFlashAttribute("xmlInput", webserviceSetupForm.getXmlInput());
				redirAttr.addFlashAttribute("rawInput", webserviceSetupForm.getRawInput());*/
				redirAttr.addFlashAttribute("environmentId", webserviceSetupForm.getEnvironmentId());
				if(webserviceSetupForm.getXmlInput() != null) {
					model.addAttribute("xmlInput", webserviceSetupForm.getXmlInput());
				}
				else{
					model.addAttribute("xmlInput", "false");
				}
				
				if(webserviceSetupForm.getRawInput() != null) {
					model.addAttribute("rawInput", webserviceSetupForm.getRawInput());
				}
				else{
					model.addAttribute("rawInput", "true");
				}
				break;
			
			}
			
		} catch (Exception e) {
			logger.error("Exception in WebserviceSetup.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}

		logger.debug("Exit: uiTestingSetup");
		redirAttr.addFlashAttribute("webserviceSetupForm", webserviceSetupForm);
		return WebserviceTest.REDIRECT_VIEW;
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


	@RequestMapping(value = "/AddWebservices", method = RequestMethod.POST)
	public String addWebservices(RedirectAttributes redirAttr,
			Model model, @ModelAttribute WebserviceSetupForm webserviceSetupForm) {

		logger.debug("Entry: AddWebservices, webserviceSetupForm->"
				+ webserviceSetupForm);
		Boolean result = false;
		int wsdlflag = 0;
		int wadlflag = 0;
		
		try {
			
			webserviceSetupForm
			.setSetupTabNumber(WebserviceTestingAdmin.ADD_WEBSERVICE);
			
			//Adding Soap services
			Map<String, String> soapServicesMap =  WebserviceUtil.getSoapServices(configProperties);
			Iterator<Map.Entry<String, String>> entries = soapServicesMap.entrySet().iterator();
			while (entries.hasNext()) {
				Map.Entry<String, String> entry = entries.next();
				if (context.getParameter(entry.getValue()) != null) {
					result = WebserviceUtil.checkWSDL(entry.getValue(),entry.getKey(), configProperties, 
							webserviceTestingService);
					if (result) {
						wsdlflag = 1;
					} else {
						wsdlflag = 2;
					}
				}
			}
			
			
			// Adding Rest services
			Map<String, String> restServicesMap =  WebserviceUtil.getRestServices(configProperties);
			Iterator<Map.Entry<String, String>> iterator = restServicesMap.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, String> entry = iterator.next();
				if (context.getParameter(entry.getValue()) != null) {
					result = WebserviceUtil.checkWADL(entry.getValue(),entry.getKey(),configProperties, webserviceTestingService);
					if (result) {
						wsdlflag = 1;
					} else {
						wsdlflag = 2;
					}
				}
			}
			

			if (wsdlflag == 1 || wsdlflag == 0 && wadlflag == 1 || wadlflag == 0) {
				redirAttr.addFlashAttribute("Success",
						"All tables are successfully updated with data from WSDL/WADL for the selected service(s).");
			} else {
				redirAttr.addFlashAttribute("Success",
						"Issue while updating the tables with new data. Please try again. ");
			}
			redirAttr.addFlashAttribute("webserviceSetupForm",
					webserviceSetupForm);
			
		} catch(Exception e) {
			logger.error("Exception in AddWebservices.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
			redirAttr.addFlashAttribute("error",
					"Unexpected error occurred while loading the webservices.");
		}
		return WebserviceTestingAdmin.REDIRECT_VIEW;
	}
	
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/GetOperationsByService", method = RequestMethod.POST)
	public @ResponseBody
	String getOperationsByService(
			@RequestParam(value = "serviceId") int serviceId) {
		logger.debug("Entry: GetOperationsByService ");

		JSONObject obj = new JSONObject();
		StringWriter out = new StringWriter();
	
		if (serviceId > 0) {
			try {

				Webservices webservice = new Webservices();
				webservice.setServiceId(serviceId);

				List<WebserviceOperations> operationsList = null;
				operationsList = webserviceTestingService.getOperationsByService(webservice);
				for ( WebserviceOperations operations:operationsList ) {
					if(operations.getMethodType()==null){
						operations.setMethodType("");
					}
					obj.put(operations.getOperationName() + ":"+ operations.getMethodType(),
							operations.getOperationId());
				}
				obj.writeJSONString(out);
			} catch (Exception e) {
			logger.error("Exception in GetOperationsByService");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: GetSecureEnvironmentDropdown");
		}
		return out.toString();
	
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/GetEndpoint", method = RequestMethod.POST)
	public @ResponseBody
	String getEndpoint(
			@RequestParam(value = "serviceId") int serviceId,
			@RequestParam(value = "environmentId") int environmentId) {
		logger.debug("Entry: GetEndpoint ");

		JSONObject obj = new JSONObject();
		StringWriter out = new StringWriter();
		try {
			Webservices testServices = new Webservices();
			testServices.setServiceId(serviceId);
			String serviceName = webserviceTestingService.getServicefromServiceId(
					serviceId).getServiceName();
			
			String endPoint = webserviceTestingService.getEndPoint(
					environmentId, serviceName).getEndpoint();
			if (endPoint != null) {
				obj.put("endPoint", endPoint);

			} else {
				obj.put("endPoint", "");
			}
			obj.writeJSONString(out);
		} catch (Exception e) {
			logger.error("Exception in GetEndpoint");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		
		logger.debug("Exit: GetEndpoint");
		return out.toString();
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/GetSelectedOperations", method = RequestMethod.POST)
	public @ResponseBody
	String getSelectedOperations(
			@RequestParam(value = "serviceId") int serviceId,
			@RequestParam(value = "environmentId") int environmentId,
			@RequestParam(value = "operationId") int operationId) {
		logger.debug("Entry: GetSelectedOperations ");

		JSONObject obj = new JSONObject();
		StringWriter out = new StringWriter();
		try {
			List<WebserviceOperations> operationsList = null;
			operationsList = webserviceTestingService.getOperationsFromServiceEnv(
					serviceId, environmentId);
			for (WebserviceOperations opr : operationsList) {
				WebserviceOperations webserviceOperation = new WebserviceOperations();
				webserviceOperation.setOperationId(operationId);
				String operationName = webserviceTestingService.getOperationfromOperationId(webserviceOperation)
						.getOperationName();
				if (opr.getOperationName().equals(operationName)) {
					obj.put("isOprExist", true);
				}
			}
			obj.writeJSONString(out);
		} catch (Exception e) {
			logger.error("Exception in GetSelectedOperations");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		
		logger.debug("Exit: GetSelectedOperations");
		return out.toString();
	}
	
	
	@RequestMapping(value = "/ConfigureWebservice", method = RequestMethod.POST)
	public String configureWebservice(RedirectAttributes redirAttr,
			Model model,@ModelAttribute WebserviceSetupForm webserviceSetupForm) {

		logger.debug("Entry: ConfigureWebservice, webserviceSetupForm->"
				+ webserviceSetupForm);
		
		Boolean insertStatus = false;
		Integer insertedOperationId = 0;
		Boolean deleted = false;
		
		try {
			webserviceSetupForm
			.setSetupTabNumber(WebserviceTestingAdmin.ADD_WEBSERVICE);
			int serviceId = webserviceSetupForm.getServiceId();
			int environmentId = webserviceSetupForm.getEnvironmentId();
			String endPoint = webserviceSetupForm.getEndPoint();
			
			redirAttr.addFlashAttribute("serviceId", serviceId);
			redirAttr.addFlashAttribute("environmentId", environmentId);
			redirAttr.addFlashAttribute("endPoint", endPoint);
			
			// Inserting endpoint url into DB
			WsEndpointDetails wsEndpointDetails = new WsEndpointDetails();
			
			if (endPoint.length() > 0 && endPoint.charAt(endPoint.length()-1)=='/') {
				endPoint = endPoint.substring(0, endPoint.length()-1);
			}

			wsEndpointDetails.setEndpoint(endPoint);
			wsEndpointDetails.setServiceId(serviceId);
			wsEndpointDetails.setEnvironmentId(environmentId);
			insertStatus = webserviceTestingService.insertEndpointDetails(wsEndpointDetails);
			
			
			Webservices webservice = new Webservices();
			webservice.setServiceId(serviceId);
			List<WebserviceOperations> operationList = 
				webserviceTestingService.getOperationsByService(webservice);
			
			List<Integer> operationsRemoved = new ArrayList<Integer>();
			List<String> operationsIncluded = new ArrayList<String>();		
			
			
			for (WebserviceOperations operation : operationList) {
				if (operation.getMethodType() == null){
					operation.setMethodType("");
				}
				String operationName = operation.getOperationName();
				if (context.getParameter(operationName + ":" + operation.getMethodType()) == null) {
					operationsRemoved.add(operation.getOperationId());
				}
				if (context.getParameter(operationName + ":" + operation.getMethodType()) != null) {
					if (context.getParameter(operation.getOperationId() + "methodType") == null) {
						operation.setMethodType("GET");
					}else {
						operation.setMethodType(context.getParameter(operation.getOperationId() + "methodType"));	
					}
					if (operation.getContentType() == null
							|| operation.getContentType().isEmpty()) {
						operation.setContentType("-");
					}
					operationsIncluded.add(operation.getOperationId() + ":"
							+ operation.getMethodType() + ":"
							+ operation.getContentType());
				}
			}
			
			//Insert operations with Environment Id
			for (String operation : operationsIncluded) {
				String[] values = operation.split(":"); //methodType and ContentType
				int operationId = Integer.parseInt(values[0]);
				logger.info("Checked operation id: " + operation);
				if (values.length > 1) {
					insertedOperationId = webserviceTestingService.insertOperationWithEnvId(operationId, serviceId,
									environmentId, values[1], values[2]);
				} else {
					insertedOperationId = webserviceTestingService.insertOperationWithEnvId(operationId, serviceId,
									environmentId, "GET", values[2]);
				}
			}
			
			
			for (Integer operationId : operationsRemoved) {
				logger.info("Unchecked operation id: " + operationId);
				Integer reqdOprId = webserviceTestingService.getOperationIdFromNameAndEnvId(
						operationId, serviceId, environmentId);
				if (reqdOprId > 0) {
					deleted = webserviceTestingService.deleteOperations(reqdOprId,
							environmentId);
				}
			}
			
			if (insertedOperationId > 0 || deleted) {
				redirAttr.addFlashAttribute("Success",
						"Operation(s) for selected service and environment updated successfully. ");
			} else {
				redirAttr.addFlashAttribute("Success",
						"Operation(s) for selected service and environment already present in DB.");
			}

			
			redirAttr.addFlashAttribute("webserviceSetupForm",
					webserviceSetupForm);
			
		} catch(Exception e) {
			logger.error("Exception in ConfigureWebservice.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		return WebserviceTestingAdmin.REDIRECT_VIEW;
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/GetOperationsFromServiceEnv", method = RequestMethod.POST)
	public @ResponseBody
	String getOperationsFromServiceEnv(
			@RequestParam(value = "serviceId") int serviceId,
			@RequestParam(value = "environmentId") int environmentId) {
		
		logger.debug("Entry: getOperationsFromServiceEnv");
		JSONObject obj = new JSONObject();
		StringWriter out = new StringWriter();

			try {
				Webservices webservice = new Webservices();
				webservice.setServiceId(serviceId);
				List<WebserviceOperations> operationList = null;
				operationList = webserviceTestingService.getOperationsFromServiceEnv(
						serviceId, environmentId);
				for ( WebserviceOperations operations:operationList ) {
					if(operations.getMethodType() == null){
						operations.setMethodType("");
					} 
					if(operations.getMethodType().isEmpty()) {
						obj.put(operations.getOperationId(), operations.getOperationName());
					} else {
						obj.put(operations.getOperationId(), operations.getMethodType()+" " +operations.getOperationName());
					}
					
				}
				obj.writeJSONString(out);

			} catch (Exception e) {
				logger.error("Exception in getOperationsFromServiceEnv.");
				logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
			}
		logger.debug("Exit: getOperationsFromServiceEnv");
		return out.toString();
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/GetTestParamSets", method = RequestMethod.POST)
	public @ResponseBody
	String getTestParamSets(
			@RequestParam(value = "serviceId") int serviceId,
			@RequestParam(value = "environmentId") int environmentId,
			@RequestParam(value = "operationId") int operationId) {
		
		logger.debug("Entry: GetTestParamSets");
		JSONObject obj = new JSONObject();
		StringWriter out = new StringWriter();
		int reqdOprId = 0;
			try {
				Map<String, Map<String, String>> parameterSetMap = 
					new LinkedHashMap<String, Map<String, String>>();
				
				reqdOprId = webserviceTestingService.getOperationIdOfNullEnv(serviceId, operationId);
				parameterSetMap = webserviceTestingService.getParameterSets(reqdOprId, environmentId);
				Iterator paramSetMapIterator = parameterSetMap.keySet().iterator();
				int i = 1;
				while (paramSetMapIterator.hasNext()) {
					String paramSet = (String) paramSetMapIterator.next();
					Map<String, String> requestValuesMap = parameterSetMap
							.get(paramSet);
					obj.put(paramSet, requestValuesMap);
				}
				obj.writeJSONString(out);

			} catch (Exception e) {
				logger.error("Exception in GetTestParamSets.");
				logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
			}
		logger.debug("Exit: GetTestParamSets");
		return out.toString();
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/GetTestSoapParamSets", method = RequestMethod.POST)
	public @ResponseBody
	String getTestSoapParamSets(
			@RequestParam(value = "serviceId") int serviceId,
			@RequestParam(value = "environmentId") int environmentId,
			@RequestParam(value = "operationId") int operationId) {
		
		logger.debug("Entry: GetTestSoapParamSets");
		JSONObject obj = new JSONObject();
		StringWriter out = new StringWriter();
		int reqdOprId = 0;
			try {
				Map<String, Map<String, String>> parameterSetMap = 
					new LinkedHashMap<String, Map<String, String>>();
				
				reqdOprId = webserviceTestingService.getOperationIdOfNullEnv(serviceId, operationId);
				/*parameterSetMap = webserviceTestingService.getTestSoapParameterSets(reqdOprId, environmentId);
				Iterator paramSetMapIterator = parameterSetMap.keySet().iterator();
				int i = 1;
				while (paramSetMapIterator.hasNext()) {
					String paramSet = (String) paramSetMapIterator.next();
					Map<String, String> requestValuesMap = parameterSetMap
							.get(paramSet);
					
					obj.put(paramSet, requestValuesMap);
				}*/
				
				List<WsDataset> wsDatasets = webserviceTestingService.getTestSoapParameterSets(reqdOprId, environmentId);
				for (WsDataset wsDataset : wsDatasets) {
					obj.put(wsDataset.getDataSet(), wsDataset.getParameterValuesMap());
				}
				obj.writeJSONString(out);

			} catch (Exception e) {
				logger.error("Exception in GetTestParamSets.");
				logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
			}
		logger.debug("Exit: GetTestSoapParamSets");
		return out.toString();
	}

	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/GetServiceByEnvId", method = RequestMethod.POST)
	public @ResponseBody
	String getServiceByEnvId(@RequestParam(value = "environmentId") int environmentId) {
		
		logger.debug("Entry: getOperationsFromServiceEnv");
		JSONObject obj = new JSONObject();
		StringWriter out = new StringWriter();

			try {
				List<WsEndpointDetails> wsEndpointDetailsList = webserviceTestingService
				.getServiceByEnvId(environmentId);
				
				for ( WsEndpointDetails wsEndpointDetail:wsEndpointDetailsList ) {
					obj.put(wsEndpointDetail.getServiceId(), wsEndpointDetail.getWebservices().getServiceName());
				}
				obj.writeJSONString(out);

			} catch (Exception e) {
				logger.error("Exception in getOperationsFromServiceEnv.");
				logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
			}
		logger.debug("Exit: getOperationsFromServiceEnv");
		return out.toString();
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/GetServiceType", method = RequestMethod.POST)
	public @ResponseBody
	String getServiceType(@RequestParam(value = "serviceId") int serviceId) {
		logger.debug("Entry: getOperationsFromServiceEnv");
		JSONObject obj = new JSONObject();
		StringWriter out = new StringWriter();
			try {
				Webservices webservice =  webserviceTestingService.getServicefromServiceId(serviceId);
				String result = webservice.getServiceType();
				obj.put("serviceType",result);
				obj.writeJSONString(out);
			} catch (Exception e) {
				logger.error("Exception in getOperationsFromServiceEnv.");
				logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
			}
		logger.debug("Exit: getOperationsFromServiceEnv");
		return out.toString();
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/GetReqParameterList", method = RequestMethod.POST)
	public @ResponseBody
	String getReqParameterList(@RequestParam(value = "serviceId") int serviceId,
			@RequestParam(value = "operationId") int operationId) {
		logger.debug("Entry: GetReqParameterList");
		Integer reqdOprId = 0;
		JSONObject obj = new JSONObject();
		StringWriter out = new StringWriter();
			try {
				WsOperationParameter wsOperationParameterList = new WsOperationParameter();
				wsOperationParameterList.setServiceId(serviceId);
				reqdOprId = webserviceTestingService.getOperationIdOfNullEnv(serviceId, operationId);
				wsOperationParameterList.setOperationId(reqdOprId);
				List<WsOperationParameter> requestParamsList = webserviceTestingService
						.getRequestParamNames(wsOperationParameterList);
				for ( WsOperationParameter wsOperationParam:requestParamsList ) {
					if(wsOperationParam.getParamType() == null){
						wsOperationParam.setParamType("");
					}
					if(wsOperationParam.getParamType().equalsIgnoreCase("x")){
						  String clobStr = wsOperationParam.getSampleXML().getSubString(1, (int) wsOperationParam.getSampleXML().length());

						obj.put(wsOperationParam.getParameterName(),wsOperationParam.getParamType() +"^"+StringEscapeUtils.escapeXml(clobStr));
					} else {
					obj.put(wsOperationParam.getParameterName(),wsOperationParam.getParamType());
					}
				}
				obj.writeJSONString(out);
			} catch (Exception e) {
				logger.error("Exception in getOperationsFromServiceEnv.");
				logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
			}
			
		logger.debug("Exit: GetReqParameterList");
		return out.toString();
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/GetXMLParameter", method = RequestMethod.POST)
	public @ResponseBody
	String getXMLParameter(@RequestParam(value = "serviceId") int serviceId,
			@RequestParam(value = "operationId") int operationId) {
		logger.debug("Entry: GetReqParameterList");
		Integer reqdOprId = 0;
		JSONObject obj = new JSONObject();
		StringWriter out = new StringWriter();
		WSDLParser parser = new WSDLParser();
		String requestXml = "";
			try {
				
				Webservices webservices = webserviceTestingService.getServicefromServiceId(serviceId);
				
				WebserviceOperations webserviceOperations = new WebserviceOperations();
				webserviceOperations.setOperationId(operationId);
				webserviceOperations = webserviceTestingService.getOperationfromOperationId(webserviceOperations);
				
				WsOperationParameter wsOperationParameter = new WsOperationParameter();
				reqdOprId = webserviceTestingService.getOperationIdOfNullEnv(serviceId, operationId);

				
				wsOperationParameter.setOperationId(reqdOprId);
				wsOperationParameter.setServiceId(serviceId);

				List<WsOperationParameter> requestParamsList = webserviceTestingService
				.getRequestParamNames(wsOperationParameter);
		
				List<String> requestLabelsList = new ArrayList<String>();
				for (WsOperationParameter reqParam : requestParamsList) {
					requestLabelsList.add(reqParam.getParameterName());
				}
				
				Map<String, String> requestValuesMap = new LinkedHashMap<String, String>();
			
				for (int i = 0; i < requestLabelsList.size(); i++) {
					String parameter = requestLabelsList.get(i);
					requestValuesMap
							.put(parameter, "???");
				}
								
				File f = new File(webservices.getServiceFilePath());
				
				Definitions defs = parser.parse(webservices.getServiceFilePath());
				for (PortType pt : defs.getPortTypes()) {
					logger.info(pt.getName());
					for (Operation op : pt.getOperations()) {
						logger.info(" -" + op.getName());
						String operName = op.getName();
						if (op.getName().equalsIgnoreCase(webserviceOperations.getOperationName())) {
							
								/*for (Part part : op.getInput().getMessage().getParts()) {

									logger.info("request template: \n"
											+ part.getElement().getRequestTemplate());
									requestXml = part.getElement()
											.getRequestTemplate();
								}*/
							
							requestXml = WebserviceUtil.createSOAPreq(webservices.getServiceFilePath(),
									webservices.getServiceName(), operName, requestValuesMap);
								break;
						}
					}
				}
				obj.put("requestXml",requestXml);
				obj.writeJSONString(out);
			} catch (Exception e) {
				logger.error("Exception in getOperationsFromServiceEnv.");
				logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
			}
			
		logger.debug("Exit: GetReqParameterList");
		return out.toString();
	}
	
	@RequestMapping(value = "/SaveRawInputs", method = RequestMethod.POST)
	public String saveRawInputs(RedirectAttributes redirAttr,
			Model model, @ModelAttribute WebserviceSetupForm webserviceSetupForm) {

		logger.debug("Entry: SaveRawInputs, webserviceSetupForm->"
				+ webserviceSetupForm);
		Boolean result = false;
		boolean insStatus = false;
		
		try {
			webserviceSetupForm.setSetupTabNumber(WebserviceTest.CREATE_DATASET);
			
			Integer reqdOprId = 0;
			
			Webservices webservice = webserviceTestingService.getServicefromServiceId(
					webserviceSetupForm.getServiceId());
			
			WsOperationParameter wsOperationParameter = new WsOperationParameter();
			reqdOprId = webserviceTestingService.getOperationIdOfNullEnv(
					webserviceSetupForm.getServiceId(), webserviceSetupForm.getOperationId());
			wsOperationParameter.setOperationId(reqdOprId);
			wsOperationParameter.setServiceId(webserviceSetupForm.getServiceId());

			List<WsOperationParameter> requestParamsList = webserviceTestingService
			.getRequestParamNames(wsOperationParameter);
			List<String> requestLabelsList = new ArrayList<String>();
			
			for (WsOperationParameter reqParam : requestParamsList) {
				requestLabelsList.add(reqParam.getParameterName());
			}
			
			// Adding Custom parameters - Start
			Map<String, String> requestValuesMap = new LinkedHashMap<String, String>();
			//StringToStringMap customHeaders = new StringToStringMap();
			
			if(webserviceSetupForm.getElementsArray().length() > 0) {
				for (String str2 : webserviceSetupForm.getElementsArray().split(",")) {
					//customHeaders.put(str2.split("~")[0], str2.split("~")[1]);
					if(!str2.split("~")[0].equalsIgnoreCase("undefined")){
					requestValuesMap.put(str2.split("~")[0], str2.split("~")[1]);
					WsOperationParameter wsOperationParam = new WsOperationParameter();
					wsOperationParam.setServiceId(webserviceSetupForm.getServiceId());
					wsOperationParam.setOperationId(reqdOprId);
					wsOperationParam.setParameterName(str2.split("~")[0]);
					wsOperationParam.setParamType("C");
					webserviceSetupForm.setDatasetName(webserviceSetupForm.getDatasetName());
					String sampleXML = "";
					result = webserviceTestingService.
					insertRequestParameterName(wsOperationParam, sampleXML);
					}
				}
			}
			
			// Adding Custom parameters - end
			
			logger.info("User inputs are: ");
			for (int i = 0; i < requestLabelsList.size(); i++) {
				String parameter = requestLabelsList.get(i);
				requestValuesMap.put(parameter, context.getParameter(parameter));
			}
			Integer maxId = webserviceTestingService.getMaxTestParameterSetId();
			WsParameterAndSetId wsParameterAndSetId = new WsParameterAndSetId();
			wsParameterAndSetId.setParameterSetId(maxId);
			if(webserviceSetupForm.getReqInputType().equalsIgnoreCase("rawInput")){
				webserviceSetupForm.setRawInput("true");
				webserviceSetupForm.setXmlInput("false");
			} else {
				webserviceSetupForm.setRawInput("false");
				webserviceSetupForm.setXmlInput("true");
			}
			redirAttr.addFlashAttribute("webserviceSetupForm",webserviceSetupForm);
			insStatus = webserviceTestingService.addTestOperationData(
					wsOperationParameter, requestValuesMap,
					wsParameterAndSetId, webserviceSetupForm.getEnvironmentId(), webserviceSetupForm.getOperationId(), webserviceSetupForm.getDatasetName());
			if (insStatus) {
				redirAttr.addFlashAttribute("Success",
						"The test case for selected operation has been saved successfully.");
			} 
			
		} catch (DataIntegrityViolationException e) {
			logger.error("Exception in SaveRawInputs.");
			redirAttr.addFlashAttribute("error",e.getMessage());
		} 
		catch(Exception e) {
			logger.error("Exception in SaveRawInputs.");
			redirAttr.addFlashAttribute("Success",
			"Some issue while inserting the request parameter values. Please try again.");
		}
		return WebserviceTest.REDIRECT_VIEW;
	}
	
	
	@RequestMapping(value = "/SaveXmlInputs", method = RequestMethod.POST)
	public String saveXmlInputs(RedirectAttributes redirAttr,
			Model model, @ModelAttribute WebserviceSetupForm webserviceSetupForm) {

		logger.debug("Entry: SaveXmlInputs, webserviceSetupForm->"
				+ webserviceSetupForm);
		Boolean result = false;
		boolean insStatus = false;
		WsOperationXmlParameter wsOperationXmlParameter = new WsOperationXmlParameter();
		
		try {
			webserviceSetupForm.setSetupTabNumber(WebserviceTest.CREATE_DATASET);
			int reqdOprId = webserviceTestingService.getOperationIdOfNullEnv(webserviceSetupForm.getServiceId(), 
					webserviceSetupForm.getOperationId());
			wsOperationXmlParameter  = webserviceTestingService.submitParameters(
					webserviceSetupForm.getRequestXML(),
					webserviceSetupForm.getEnvironmentId(), reqdOprId, webserviceSetupForm.getServiceId(), webserviceSetupForm.getDatasetName());
			result = true;
			if(webserviceSetupForm.getReqInputType().equalsIgnoreCase("rawInput")){
				webserviceSetupForm.setRawInput("true");
				webserviceSetupForm.setXmlInput("false");
			} else {
				webserviceSetupForm.setRawInput("false");
				webserviceSetupForm.setXmlInput("true");
			}
			redirAttr.addFlashAttribute("webserviceSetupForm", webserviceSetupForm);
			if(webserviceSetupForm.getElementsArray().length() > 0) {
				for (String str2 : webserviceSetupForm.getElementsArray().split(",")) {
								
					WsOperationHeaderParameters wsOperationHeaderParameters = new WsOperationHeaderParameters();
					wsOperationHeaderParameters.setParameterId(wsOperationXmlParameter.getParameterId());
					wsOperationHeaderParameters.setParameterName(str2.split("~")[0]);
					wsOperationHeaderParameters.setParameterValue(str2.split("~")[1]);
					result = webserviceTestingService
					.insertRequestParameterHeaders(wsOperationHeaderParameters);
				}
			}
			
			if (result) {
				redirAttr.addFlashAttribute("Success",	"The test case for selected operation has been saved successfully.");
			} else {
				redirAttr.addFlashAttribute("Success", "Some issue while inserting the request parameter values. Please try again.");
			}
			
		
			
		} catch (DataIntegrityViolationException es){
			redirAttr.addFlashAttribute("error",
					"Duplicate Dataset Name. Please try with different Dataset name.");
		}
		catch(Exception e) {
			logger.error("Exception in SaveXmlInputs.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		return WebserviceTest.REDIRECT_VIEW;
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/WSTestingOperationDataSet", method = RequestMethod.POST)
	public @ResponseBody
	String doTestOperationDataSet( @RequestParam(value = "serviceId") int serviceId,
			 @RequestParam(value = "operationId") int operationId,
			 @RequestParam(value = "paramSetId") int paramSetId,
			 @RequestParam(value = "environmentId") int environmentId) {
		
		logger.debug("Entry: WSTestingOperationDataSet");
		Integer reqdOprId = 0;// operation id where environment id is null
		String responseXml = "";
		StringEntity stringEntity = null;
		try {
			
			Webservices webservice = new Webservices();
			webservice.setServiceId(serviceId);
			WebserviceOperations webserviceOperations = new WebserviceOperations();
			webserviceOperations.setOperationId(operationId);
			webserviceOperations = webserviceTestingService
			.getOperationfromOperationId(webserviceOperations);
			
			
			String operationName = webserviceOperations.getOperationName();
			String methodType = webserviceOperations.getMethodType();
			String contentType = webserviceOperations.getContentType(); 
			webservice = webserviceTestingService.getServicefromServiceId(serviceId);
			String serviceName = webservice.getServiceName();
			
			WsOperationParameter wsOperationParameter = new WsOperationParameter();
			reqdOprId = webserviceTestingService.getOperationIdOfNullEnv(serviceId, operationId);
			wsOperationParameter.setOperationId(reqdOprId);
			wsOperationParameter.setServiceId(serviceId);
			List<WsOperationParameter> requestParamsList = webserviceTestingService
			.getRequestParamNamesForDataset(wsOperationParameter);
			
			
			Map<String, String> requestValuesMap = new LinkedHashMap<String, String>();
			StringToStringMap customHeaders = new StringToStringMap();
			for(WsOperationParameter wsOprParam: requestParamsList) {
				WsParameterAndSetId wsParameterAndSetId =  new WsParameterAndSetId();
				wsParameterAndSetId.setParameterId(wsOprParam.getParameterId());
				wsParameterAndSetId.setParameterSetId(paramSetId);
				WsOperationParameterValue wsOperationParameterValue = webserviceTestingService
						.getRequestParameterValue(wsParameterAndSetId);
				
				if (wsOperationParameterValue != null) {
					Clob clob = wsOperationParameterValue.getParameterValue();
					String paramValue = "";
					if (clob != null) {
						paramValue = clob.getSubString(1, (int) clob.length());
					}
					if (wsOprParam.getParamType() != null && wsOprParam.getParamType()
									.equalsIgnoreCase("C")) {
						customHeaders.put(wsOprParam.getParameterName(), paramValue);
					} else {
						requestValuesMap.put(
								wsOprParam.getParameterName(), paramValue);
					}
				}
			} 

			Integer maxId = webserviceTestingService.getMaxTestParameterSetId();
			WsParameterAndSetId testParameterAndSetId = new WsParameterAndSetId();
			testParameterAndSetId.setParameterSetId(maxId);

			WebserviceUtil webserviceUtil = new WebserviceUtil();

			String filepath = FileDirectoryUtil.getWSDLFilePath(configProperties);
			
			String serviceType = webservice.getServiceType();
			
			String endPoint = webserviceTestingService.getEndPoint(environmentId, serviceName)
				.getEndpoint();
			
			if (!serviceType.equalsIgnoreCase("wadl")) {
				String WSDLFilePath = filepath + File.separator + serviceName + ".wsdl";

				String requestxml = webserviceUtil.createSOAPreq(WSDLFilePath,
						serviceName, operationName, requestValuesMap);

				logger.info("requestxml before submit :" + requestxml);
				logger.info("WSDLFilePath :"+WSDLFilePath);
				responseXml = webserviceUtil.executeWebServiceOperation(
						WSDLFilePath, requestxml, serviceName, operationName,
						endPoint, environmentId, customHeaders);
				logger.info("responseXml************: " + responseXml);
			} else {
				filepath = FileDirectoryUtil.getWADLFilePath(configProperties);
				String WADLFilePath = filepath + "\\"+ serviceName + ".wadl";
				Map<String, String> requestValuesMapRest = new LinkedHashMap<String, String>();
				HttpParams queryParams = new BasicHttpParams();
				String newPath = "";
				logger.info("User inputs are: ");
				for(WsOperationParameter testOperationParam: requestParamsList) {
					testParameterAndSetId.setParameterId(testOperationParam.getParameterId());
					testParameterAndSetId.setParameterSetId(paramSetId);
					String parameter = testOperationParam.getParameterName();

					Clob clob = (Clob) webserviceTestingService.getRequestParameterValue(
							testParameterAndSetId).getParameterValue();
					String value = "";
					if (clob != null) {
						value = clob.getSubString(1,
								(int) clob.length());
					}
					
					if(parameter.equalsIgnoreCase("reqXML")) {
						stringEntity = new StringEntity(
								value);
					}else {
						newPath = operationName.replace("{" + parameter
								+ "}", value);
						if (newPath.equalsIgnoreCase(operationName)) {
							requestValuesMapRest.put(parameter,
									value);
							queryParams.setParameter(parameter,
									value);
						} else {
							operationName = newPath;
						}
					}
				}
				
			
			
			// get http headers
			WsOperationParameter testHeaders = new WsOperationParameter();
			testHeaders.setServiceId(serviceId);
			reqdOprId = webserviceTestingService.getOperationIdOfNullEnv(serviceId, operationId);
			testHeaders.setOperationId(reqdOprId);
			List<WsOperationParameter> headers = webserviceTestingService
			.getRequestHeaders(testHeaders);
			
			String requestPath= "";
			if (!operationName.startsWith("/")) {
				requestPath = endPoint + "/" + operationName;
			}
			else {
				requestPath = endPoint  + operationName;	
			}
			responseXml = RestServiceUtil.getRestServiceResponse(
					requestPath, methodType, queryParams, contentType,
					stringEntity, headers, environmentId, customHeaders);
			}
			
			
				
		} catch(Exception e) {
			logger.error("Exception in WSTestingOperationDataSet.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: WSTestingOperationDataSet");
		return responseXml;
	}
	
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/WSTestingSoapOperationDataSet", method = RequestMethod.POST)
	public @ResponseBody
	String doTestSoapOperationDataSet(@RequestParam(value = "serviceId") int serviceId,
			@RequestParam(value = "operationId") int operationId,
			@RequestParam(value = "paramSetId") int paramSetId,
			@RequestParam(value = "environmentId") int environmentId) {
		
		logger.debug("Entry: WSTestingSoapOperationDataSet");

		boolean insStatus = false;
		Integer reqdOprId = 0;// operation id where environment id is null
	
		String requestxml ="", responseXml = "";
		try {
			Webservices webservice = new Webservices();
			webservice.setServiceId(serviceId);
			WebserviceOperations webserviceOperation = new WebserviceOperations();
			webserviceOperation.setOperationId(operationId);
			webservice = webserviceTestingService.getServicefromServiceId(serviceId);
			List<WebserviceOperations> operationsList = null;
			operationsList = webserviceTestingService.getOperationsFromServiceEnv(
					serviceId, environmentId);

			// get operation name from service ID
			webserviceOperation = webserviceTestingService
			.getOperationfromOperationId(webserviceOperation);
			String operationName = webserviceOperation.getOperationName();
			String methodType = webserviceOperation.getMethodType();
			String contentType = webserviceOperation.getContentType(); 
			// get service name from operation ID
			String serviceName = webservice.getServiceName();
			reqdOprId = webserviceTestingService.getOperationIdOfNullEnv(serviceId, operationId);
			WebserviceUtil webserviceUtil = new WebserviceUtil();
			String filepath = FileDirectoryUtil.getWSDLFilePath(configProperties);
			String serviceType =webservice.getServiceType();
			String endPoint = webserviceTestingService.getEndPoint(environmentId, serviceName).getEndpoint();
			StringToStringMap customHeaders = new StringToStringMap();
			requestxml = webserviceTestingService.getTestSoapParameters(paramSetId).getParameterValue();
			
			List<WsOperationHeaderParameters> headers = 
				webserviceTestingService.getTestSoapParameterHeaders(paramSetId);
			for(WsOperationHeaderParameters headerParameters : headers){
				customHeaders.put(headerParameters.getParameterName(), headerParameters.getParameterValue());
			}
			
			if (!serviceType.equalsIgnoreCase("wadl")) {
				String WSDLFilePath = filepath + File.separator + serviceName + ".wsdl";
				responseXml = webserviceUtil.executeWebServiceOperation(
						WSDLFilePath, requestxml, serviceName, operationName,
						endPoint, environmentId, customHeaders);
				logger.info("responseXml************: " + responseXml);
			} 
		} catch (Exception e) {
			logger.error("Exception in WSTestingOperationDataSet.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.info("Exit: WSTestingSoapOperationDataSet");
		return responseXml;

	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/WSTestingOperation", method = RequestMethod.POST)
	public @ResponseBody
	String doTestOperation( @ModelAttribute("model") ModelMap model,
			WebserviceSetupForm webserviceSetupForm ) {

		logger.debug("Entry: WSTestingOperationDataSet");

		int serviceId = webserviceSetupForm.getServiceId();
		int operationId = webserviceSetupForm.getOperationId();
		int environmentId = webserviceSetupForm.getEnvironmentId();
		
		Integer reqdOprId = 0;// operation id where environment id is null
		String responseXml = "";
		StringEntity stringEntity = null;
		long lStartTime, lEndTime ;
		boolean insStatus = false;
		lStartTime = System.currentTimeMillis();
		try {
			Webservices webservice = new Webservices();
			webservice.setServiceId(serviceId);
			WebserviceOperations webserviceOperation = new WebserviceOperations();
			webserviceOperation.setOperationId(operationId);
			
			// Populating custom http headers
			StringToStringMap customHeaders = new StringToStringMap();
			if(webserviceSetupForm.getElementsArray().length() > 0) {
				for (String str2 : webserviceSetupForm.getElementsArray().split(",")) {
					customHeaders.put(str2.split("~")[0], str2.split("~")[1]);
				}
			}
			List<WebserviceOperations> operationsList = null;
			operationsList = webserviceTestingService.getOperationsFromServiceEnv(
					serviceId, environmentId);
			
			webserviceOperation = webserviceTestingService
			.getOperationfromOperationId(webserviceOperation);
			String operationName = webserviceOperation.getOperationName();
			String methodType = webserviceOperation.getMethodType();
			String contentType = webserviceOperation.getContentType();
			
			webservice = webserviceTestingService.getServicefromServiceId(serviceId);
			String serviceName = webservice.getServiceName();
			

			WsOperationParameter wsOperationParameter = new WsOperationParameter();
			reqdOprId = webserviceTestingService.getOperationIdOfNullEnv(serviceId, operationId);

			
			wsOperationParameter.setOperationId(reqdOprId);
			wsOperationParameter.setServiceId(serviceId);

			List<WsOperationParameter> requestParamsList = webserviceTestingService
			.getRequestParamNames(wsOperationParameter);
	
			List<String> requestLabelsList = new ArrayList<String>();
			for (WsOperationParameter reqParam : requestParamsList) {
				requestLabelsList.add(reqParam.getParameterName());
			}
			
			Map<String, String> requestValuesMap = new LinkedHashMap<String, String>();
			logger.info("User inputs are: ");
			for (int i = 0; i < requestLabelsList.size(); i++) {
				String parameter = requestLabelsList.get(i);
				logger.info("Parameter Name:" + parameter);
				logger.info("Parameter Value:" + context.getParameter(parameter));
				requestValuesMap
						.put(parameter, context.getParameter(parameter));
			}
			
			Integer maxId = webserviceTestingService.getMaxTestParameterSetId();
			WsParameterAndSetId wsParameterAndSetId = new WsParameterAndSetId();
			wsParameterAndSetId.setParameterSetId(maxId);
			

			String filepath = FileDirectoryUtil.getWSDLFilePath(configProperties);
			
			String serviceType = webservice.getServiceType();
			String endPoint = webserviceTestingService.getEndPoint(environmentId, serviceName).getEndpoint();
			
			if (!serviceType.equalsIgnoreCase("wadl")) {
				String WSDLFilePath = filepath + serviceName + ".wsdl";

				String requestxml = WebserviceUtil.createSOAPreq(WSDLFilePath,
						serviceName, operationName, requestValuesMap);
				responseXml = WebserviceUtil.executeWebServiceOperation(
						WSDLFilePath, requestxml, serviceName, operationName,
						endPoint, environmentId, customHeaders);
				logger.info("responseXml************: " + responseXml);
			} else {
				
					String WADLFilePath = filepath + "\\"+ serviceName + ".wadl";
					Map<String, String> requestValuesMapRest = new LinkedHashMap<String, String>();
					HttpParams queryParams = new BasicHttpParams();
					String newPath = "";
					logger.info("User inputs are: ");
					for (int i = 0; i < requestLabelsList.size(); i++) {
						String parameter = requestLabelsList.get(i);
						logger.info("Parameter Name:" + parameter);
						logger.info("Parameter Value:"
								+ context.getParameter(parameter));
						if (parameter.equalsIgnoreCase("reqXML")) {
							stringEntity = new StringEntity(
									context.getParameter(parameter));
						} else {
							newPath = operationName.replace("{" + parameter
									+ "}", context.getParameter(parameter));
							if (newPath.equalsIgnoreCase(operationName)) {
								requestValuesMapRest.put(parameter,
										context.getParameter(parameter));
								queryParams.setParameter(parameter,
										context.getParameter(parameter));
							} else {
								operationName = newPath;
							}
						}
					}
					// get http headers
					WsOperationParameter wsOperationXmlParam = new WsOperationParameter();
					wsOperationXmlParam.setServiceId(serviceId);
					reqdOprId = webserviceTestingService.getOperationIdOfNullEnv(serviceId, operationId);
					wsOperationXmlParam.setOperationId(reqdOprId);
					List<WsOperationParameter> headers = webserviceTestingService
							.getRequestHeaders(wsOperationXmlParam);
					String requestPath= "";
					if (!operationName.startsWith("/") )
					requestPath = endPoint + "/" + operationName;
					else 
						requestPath = endPoint  + operationName;	
					responseXml = RestServiceUtil.getRestServiceResponse(
							requestPath, methodType, queryParams, contentType,
							stringEntity, headers, environmentId, customHeaders);
				}
		
		}
		catch (Exception e) {
			logger.error("Exception in doTestOperation.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.info("Exit: doTestOperation");
		return responseXml;

	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/WSSoapTestingOperation", method = RequestMethod.POST)
	public @ResponseBody
	String doTestSoapOperation( @ModelAttribute("model") ModelMap model,
			WebserviceSetupForm webserviceSetupForm ) {
		logger.debug("Entry: doTestSoapOperation");
		String responseXml = "";
		try {
			int serviceId = webserviceSetupForm.getServiceId();
			int operationId = webserviceSetupForm.getOperationId();
			Integer reqdOprId = 0;// operation id where environment id is null
			int environmentId = webserviceSetupForm.getEnvironmentId();
			String requestxml = webserviceSetupForm.getRequestXML();
			
			Webservices webservice = new Webservices();
			webservice = webserviceTestingService.getServicefromServiceId(
					serviceId);
			WebserviceOperations webserviceOperation = new WebserviceOperations();
			webserviceOperation.setOperationId(operationId);
			StringEntity stringEntity = null;
			
			
			// Populating custom http headers
			StringToStringMap customHeaders = new StringToStringMap();
			if(webserviceSetupForm.getElementsArray().length() > 0) {
				for (String str2 : webserviceSetupForm.getElementsArray().split(",")) {
					customHeaders.put(str2.split("~")[0], str2.split("~")[1]);
				}
			}
			
			List<WebserviceOperations> operationsList = null;
			operationsList = webserviceTestingService.getOperationsFromServiceEnv(
					serviceId, environmentId);
			
			webserviceOperation = webserviceTestingService
			.getOperationfromOperationId(webserviceOperation);
			
			String operationName = webserviceOperation.getOperationName();
			String methodType = webserviceOperation.getMethodType();
			String contentType = webserviceOperation.getContentType();
		
			
			WsOperationXmlParameter testOperationParameter = new WsOperationXmlParameter();
			reqdOprId = webserviceTestingService.getOperationIdOfNullEnv(serviceId, operationId);

			logger.info("reqdOprId: " + reqdOprId);
			testOperationParameter.setOperationId(reqdOprId);
			testOperationParameter.setServiceId(serviceId);
			
			Integer maxId = webserviceTestingService.getMaxTestParameterSetId();
			WsParameterAndSetId testParameterAndSetId = new WsParameterAndSetId();
			testParameterAndSetId.setParameterSetId(maxId);

			String filepath = FileDirectoryUtil.getWSDLFilePath(configProperties);
			
			String serviceType = webservice.getServiceType();
			String endPoint = webserviceTestingService.getEndPoint(
					environmentId, webservice.getServiceName()).getEndpoint();
			logger.info("serviceType :"+serviceType);
			if (!serviceType.equalsIgnoreCase("wadl")) {
				String WSDLFilePath = filepath + File.separator + webservice.getServiceName() + ".wsdl";
				logger.info("requestxml before submit :" + requestxml);
				logger.info("WSDLFilePath :"+WSDLFilePath);
				responseXml = WebserviceUtil.executeWebServiceOperation(
						WSDLFilePath, requestxml, webservice.getServiceName(), operationName,
						endPoint, environmentId, customHeaders);
				logger.info("responseXml************: " + responseXml);
			} 
		} catch (Exception e) {
			logger.error("Exception in doTestSoapOperation.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: doTestSoapOperation");
		return responseXml;
	}
	
	@RequestMapping(value = "/WSAddAnotherTestCase", method = RequestMethod.POST)
	public String showSelectedServiceAndOperation(RedirectAttributes redirAttr,
			Model model, @ModelAttribute WebserviceSetupForm webserviceSetupForm) {
		logger.debug("Entry: showSelectedServiceAndOperation");
		
		int reqdOprId = 0;		
		try {
			webserviceSetupForm.setSetupTabNumber(WebserviceTest.CREATE_WS_TESTSUITE);
			List<String> chosenOperationsList = new ArrayList<String>();
			Map<String, Map<String, String>> chosenServiceMap = new LinkedHashMap<String, Map<String, String>>();
			Map<Integer, Map<Integer,Map<String,List<Integer>>>> serviceOprSetIDMap = new LinkedHashMap<Integer, Map<Integer, Map<String,List<Integer>>>>();
			redirAttr.addFlashAttribute("addAnother", "Selected Service-Operation(s)");
			if (webserviceSetupForm.getOperationId() > 0) {
				Webservices webservice = new Webservices();
				webservice = webserviceTestingService.getServicefromServiceId(webserviceSetupForm.getServiceId());
				String serviceName = webservice.getServiceName();
				
				WebserviceOperations webserviceOperations = new WebserviceOperations();
				webserviceOperations.setOperationId(webserviceSetupForm.getOperationId());
				String operationName = webserviceTestingService
						.getOperationfromOperationId(webserviceOperations).getOperationName();
				Map<String, String> oprMap = new LinkedHashMap<String, String>();
				Map<Integer, Map<String,List<Integer>>> oprSetIDMap = new LinkedHashMap<Integer, Map<String,List<Integer>>>();
				reqdOprId = webserviceTestingService.getOperationIdOfNullEnv(webserviceSetupForm.getServiceId(),
						webserviceSetupForm.getOperationId());
				int executionCount=webserviceSetupForm.getParams().length;
				String environmentName = environmentService
						.getEnvironmentCategoryById(webserviceSetupForm.getEnvironmentId()).getEnvironmentCategoryName();
				
				String countAndEnv = executionCount + ":" + environmentName;
				oprMap.put(operationName, countAndEnv);
				
				

				
				WSSuiteParams wsSuiteParams = null;
				List<WSSuiteParams> listWSSuiteParams = new ArrayList<WSSuiteParams>();
				if(webserviceSetupForm.getEditOrViewMode() == null || webserviceSetupForm.getEditOrViewMode().isEmpty()) {
					context.getSession().removeAttribute("serviceMap");
					
				} else {
					redirAttr.addFlashAttribute("editOrViewMode", webserviceSetupForm.getEditOrViewMode());
				}
				if (context.getSession().getAttribute("serviceMap") == null) {
					
					logger.info("inside * no session yet: ");
					context.getSession().removeAttribute("listWebserviceSetupForm");
					//chosenServiceMap.put(serviceName, oprMap);
					
					for (Map.Entry<String, String> entry : oprMap.entrySet()) {
						String oprName = entry.getKey();
						String envName  = entry.getValue().split(":")[1];
						Integer count = webserviceSetupForm.getParams().length;
						for(int i=0;i<count;i++){
							wsSuiteParams = new WSSuiteParams();
							wsSuiteParams.setServiceName(serviceName);
							wsSuiteParams.setOperationName(oprName);
							wsSuiteParams.setEnvironmentName(envName);
							WsDataset wsDatasets = webserviceTestingService.getParameterValues(webserviceSetupForm.getEnvironmentId(), 
									 		Integer.parseInt(webserviceSetupForm.getParams()[i]), reqdOprId);
							wsSuiteParams.setWsDataset(wsDatasets);
							listWSSuiteParams.add(wsSuiteParams);
						}
						
						
					}

					context.getSession().setAttribute("serviceMap",
							listWSSuiteParams);
					redirAttr.addFlashAttribute("serviceMapUI", context.getSession()
							.getAttribute("serviceMap"));
				} else {
					logger.info("inside * session already present: ");
					List<WSSuiteParams> wsSuiteParamsList  =  (List<WSSuiteParams>) context.getSession().getAttribute("serviceMap");
					
					for (Map.Entry<String, String> entry : oprMap.entrySet()) {
						String oprName = entry.getKey();
						String envName  = entry.getValue().split(":")[1];
						Integer count = webserviceSetupForm.getParams().length;
						for(int i=0;i<count;i++){
							wsSuiteParams = new WSSuiteParams();
							wsSuiteParams.setServiceName(serviceName);
							wsSuiteParams.setOperationName(oprName);
							wsSuiteParams.setEnvironmentName(envName);
							WsDataset dataset = webserviceTestingService.getParameterValues(webserviceSetupForm.getEnvironmentId(), 
									 		Integer.parseInt(webserviceSetupForm.getParams()[i]), reqdOprId);
							wsSuiteParams.setWsDataset(dataset);
							WSSuiteParams tempWsSuiteParams = wsSuiteParams;
							if(isParamAvailable(wsSuiteParamsList, tempWsSuiteParams)){
								int retval=wsSuiteParamsList.indexOf(tempWsSuiteParams);
								wsSuiteParamsList.remove(retval);
								//wsSuiteParamsList.remove(tempWsSuiteParams);
								//wsSuiteParamsList.removeAll(Arrays.asList(tempWsSuiteParams));
							}
							wsSuiteParamsList.add(wsSuiteParams);
							
						}
						
						
					}
					
					context.getSession().setAttribute("serviceMap", wsSuiteParamsList);
					redirAttr.addFlashAttribute("serviceMapUI", context.getSession()
							.getAttribute("serviceMap"));
					redirAttr.addFlashAttribute("environmentId", webserviceSetupForm.getEnvironmentId());
				}
				
				redirAttr.addFlashAttribute("chosenOperationsList", chosenOperationsList);
				redirAttr.addFlashAttribute("webserviceSetupForm",webserviceSetupForm);
				redirAttr.addFlashAttribute("wsSuiteName", webserviceSetupForm.getWsSuiteName());
				redirAttr.addFlashAttribute("xmlInput", webserviceSetupForm.getXmlInput());
				redirAttr.addFlashAttribute("rawInput", webserviceSetupForm.getRawInput());
				redirAttr.addFlashAttribute("environmentId", webserviceSetupForm.getEnvironmentId());
				redirAttr.addFlashAttribute("wsSuiteId", webserviceSetupForm.getWsSuiteId());
				List<WebserviceSetupForm> listWebserviceSetupForm = new ArrayList<WebserviceSetupForm>();
				listWebserviceSetupForm = (List<WebserviceSetupForm>)
				context.getSession().getAttribute("listWebserviceSetupForm");
				
				/*if(webserviceSetupForm.getWsSuiteId() != null){
					if(listWebserviceSetupForm!= null){
						listWebserviceSetupForm.addAll(homeService.getSavedWsSuitesParams(webserviceSetupForm.getWsSuiteId()));
					} else {
						listWebserviceSetupForm=homeService.getSavedWsSuitesParams(webserviceSetupForm.getWsSuiteId());
					}
				}*/
				
				if(listWebserviceSetupForm == null) {
					listWebserviceSetupForm = new ArrayList<WebserviceSetupForm>();
				}
				listWebserviceSetupForm.add(webserviceSetupForm);
				context.getSession().setAttribute("listWebserviceSetupForm", listWebserviceSetupForm);
			}
		
		} catch (Exception e) {
			logger.error("Exception in showSelectedServiceAndOperation.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		
		logger.debug("Exit: showSelectedServiceAndOperation");
		return WebserviceTest.REDIRECT_VIEW;
		
	}
	
	public boolean isParamAvailable(List<WSSuiteParams> paramList,WSSuiteParams wSuiteParams ) {
		
		for (WSSuiteParams param : paramList) {
		    if(param.getServiceName().equals(wSuiteParams.getServiceName()) && param.getOperationName().equals(wSuiteParams.getOperationName())
		    		&& param.getWsDataset().equals(wSuiteParams.getWsDataset())) {
		       return true;
		    }
		}
		return false;
	}
	
	
	@RequestMapping(value = "/WSAddAnotherTestCaseV2", method = RequestMethod.POST)
	public String showSelectedServiceAndOperationV2(RedirectAttributes redirAttr,
			Model model, @ModelAttribute WebserviceSetupForm webserviceSetupForm) {
		logger.debug("Entry: showSelectedServiceAndOperationV2");
		
		int reqdOprId = 0;		
		try {
			
			webserviceSetupForm.setSetupTabNumber(WebserviceTest.CREATE_WS_TESTSUITE);
			redirAttr.addFlashAttribute("addAnother", "Selected Service-Operation(s)");
			List<String> chosenOperationsList = new ArrayList<String>();
			Map<String, Map<String, String>> chosenServiceMap = new LinkedHashMap<String, Map<String, String>>();
			Map<Integer, Map<Integer, Map<String,List<Integer>>>> serviceOprSetIDMap = new LinkedHashMap<Integer, Map<Integer, Map<String,List<Integer>>>>();
			model.addAttribute("addAnother", "Selected Service-Operation(s)");
			Map<String, List<Integer>> chosenSetIDMap = new HashMap<String, List<Integer>>();
			int serviceId = webserviceSetupForm.getServiceId();
			int operationId = webserviceSetupForm.getOperationId();
			int environmentId = webserviceSetupForm.getEnvironmentId();
			
			if (operationId > 0) {
			Webservices webservice = webserviceTestingService.getServicefromServiceId(serviceId);
			
			WebserviceOperations webserviceOperations = new WebserviceOperations();
			webserviceOperations.setOperationId(operationId);
			String serviceName = webservice.getServiceName();
			String operationName = webserviceTestingService
					.getOperationfromOperationId(webserviceOperations).getOperationName();
			redirAttr.addFlashAttribute("chosenServiceName", serviceName);
			List<String> operationsList = new ArrayList<String>();
			Map<String, String> oprMap = new LinkedHashMap<String, String>();

			Map<Integer, Map<String,List<Integer>>> oprSetIDMap = new LinkedHashMap<Integer, Map<String,List<Integer>>>();
			List<Integer> chosenSetIDList = new ArrayList<Integer>();
			reqdOprId = webserviceTestingService.getOperationIdOfNullEnv(serviceId, operationId);
			/*Map<String, Map<String, String>> parameterSetMap = new LinkedHashMap<String, Map<String, String>>();
			parameterSetMap = webserviceTestingService.getTestSoapParameterSets(
					reqdOprId, environmentId);*/
			
			List<WsDataset> wsDatasets = webserviceTestingService.getTestSoapParameterSets(reqdOprId, environmentId);
			wsDatasets = webserviceTestingService.getTestSoapParameterSets(
					reqdOprId, environmentId);
			logger.info("parameterSetMap size: " + wsDatasets.size());

			
			int i = 1;
			int executionCount =0;
			
			
			
			
			for (WsDataset wsDataset: wsDatasets)  {
				String paramSetId = wsDataset.getDataSet();
				Map<String, String> requestValuesMap = wsDataset.getParameterValuesMap();
				executionCount=webserviceSetupForm.getParams().length;
			}
			
			
			
			/*	while (paramSetMapIterator.hasNext()) {

				String paramSetId = (String) paramSetMapIterator.next();
				Map<String, String> requestValuesMap = parameterSetMap
						.get(paramSetId);
			
				Iterator requestValuesMapIterator = requestValuesMap
						.keySet().iterator();

				executionCount=webserviceSetupForm.getParams().length;
				
				for(String paramSet:webserviceSetupForm.getParams()){
						chosenSetIDList.add(Integer.parseInt(paramSet));
				}
			}*/
			chosenSetIDMap.put(webserviceSetupForm.getReqInputType(), chosenSetIDList);
			Environment environment = new Environment();
			environment.setEnvironmentId(environmentId);
			String environmentName = environmentService
			.getEnvironmentCategoryById(webserviceSetupForm.getEnvironmentId()).getEnvironmentCategoryName();
			
			String countAndEnv = executionCount + ":" + environmentName;
			oprMap.put(operationName, countAndEnv);
			oprSetIDMap.put(operationId, chosenSetIDMap);
			operationsList.add(operationName + ":" + executionCount + " ");
			
			
			
			List<WSSuiteParams> listWSSuiteParams = new ArrayList<WSSuiteParams>();
			WSSuiteParams wsSuiteParams = null;
			if (context.getSession().getAttribute("serviceMap") == null) {
				logger.info("inside * no session yet: ");
				
				for (Map.Entry<String, String> entry : oprMap.entrySet()) {
					String oprName = entry.getKey();
					String envName  = entry.getValue().split(":")[1];
					Integer count = webserviceSetupForm.getParams().length;
					for(int j=0;j<count;j++){
						wsSuiteParams = new WSSuiteParams();
						wsSuiteParams.setServiceName(serviceName);
						wsSuiteParams.setOperationName(oprName);
						wsSuiteParams.setEnvironmentName(envName);
						WsDataset wsDataset = webserviceTestingService.getXmlParameterValues(webserviceSetupForm.getEnvironmentId(), 
								 		Integer.parseInt(webserviceSetupForm.getParams()[j]), reqdOprId);
						wsSuiteParams.setWsDataset(wsDataset);
						listWSSuiteParams.add(wsSuiteParams);
					}
				}
				context.getSession().setAttribute("serviceMap",
						listWSSuiteParams);
				redirAttr.addFlashAttribute("serviceMapUI", context.getSession()
						.getAttribute("serviceMap"));
			} else {
				
				
				List<WSSuiteParams> wsSuiteParamsList  =  (List<WSSuiteParams>) context.getSession().getAttribute("serviceMap");
				for (Map.Entry<String, String> entry : oprMap.entrySet()) {
					String oprName = entry.getKey();
					String envName  = entry.getValue().split(":")[1];
					Integer count = webserviceSetupForm.getParams().length;
					for(int j=0;j<count;j++){
						wsSuiteParams = new WSSuiteParams();
						wsSuiteParams.setServiceName(serviceName);
						wsSuiteParams.setOperationName(oprName);
						wsSuiteParams.setEnvironmentName(envName);
						WsDataset wsDataset = webserviceTestingService.getXmlParameterValues(webserviceSetupForm.getEnvironmentId(), 
								 		Integer.parseInt(webserviceSetupForm.getParams()[j]), reqdOprId);
						wsSuiteParams.setWsDataset(wsDataset);
						WSSuiteParams tempWsSuiteParams = wsSuiteParams;
						if(isParamAvailable(wsSuiteParamsList, tempWsSuiteParams)){
							int retval=wsSuiteParamsList.indexOf(tempWsSuiteParams);
							wsSuiteParamsList.remove(retval);
							//wsSuiteParamsList.remove(tempWsSuiteParams);
							//wsSuiteParamsList.removeAll(Arrays.asList(tempWsSuiteParams));
						}
						wsSuiteParamsList.add(wsSuiteParams);
					}
					
					
				}
				
			
				
				
				context.getSession().setAttribute("serviceMap", wsSuiteParamsList);
				redirAttr.addFlashAttribute("serviceMapUI", context.getSession()
						.getAttribute("serviceMap"));
				redirAttr.addFlashAttribute("environmentId", webserviceSetupForm.getEnvironmentId());
			}
			
			}
			redirAttr.addFlashAttribute("chosenOperationsList", chosenOperationsList);
		
			redirAttr.addFlashAttribute("wsSuiteName", webserviceSetupForm.getWsSuiteName());
			redirAttr.addFlashAttribute("xmlInput", "true");
			redirAttr.addFlashAttribute("rawInput", "false");
			webserviceSetupForm.setXmlInput("true");
			webserviceSetupForm.setRawInput("false");
			redirAttr.addFlashAttribute("environmentId", webserviceSetupForm.getEnvironmentId());
			List<WebserviceSetupForm> listWebserviceSetupForm = (List<WebserviceSetupForm>)
			context.getSession().getAttribute("listWebserviceSetupForm");
			if(listWebserviceSetupForm == null) {
				listWebserviceSetupForm = new ArrayList<WebserviceSetupForm>();
			}
			listWebserviceSetupForm.add(webserviceSetupForm);
			context.getSession().setAttribute("listWebserviceSetupForm", listWebserviceSetupForm);
			redirAttr.addFlashAttribute("webserviceSetupForm",webserviceSetupForm);
			
		} catch (Exception e) {
			logger.error("Exception in showSelectedServiceAndOperationV2.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		
		logger.debug("Exit: showSelectedServiceAndOperationV2");
		return WebserviceTestingAdmin.REDIRECT_VIEW;
		
	}
	
	@RequestMapping(value = "/clearSavedDataset", method = RequestMethod.POST)
	public @ResponseBody
	void clearSavedDataset() {
		context.getSession().setAttribute("serviceMap", null);
	}
	
	@RequestMapping(value = "/isDatasetAvailable", method = RequestMethod.POST)
	public @ResponseBody
	String isDatasetAvailable() {
		String result = "";
		
		try{
			if (context.getSession().getAttribute("serviceMap") == null) {
				result = "No";
			} else {
				result = "Yes";
			}
		}catch(Exception e){
			logger.error("Exception in isDatasetAvailable.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		return result;
	}

	@RequestMapping(value = "/wSSubmitTestCases", method = RequestMethod.POST)
	public String submitWSTestCases(RedirectAttributes redirAttr,
			Model model,@Valid @ModelAttribute WebserviceSetupForm webserviceSetupForm, BindingResult results) {
		logger.debug("Entry: wSSubmitTestCases");
		try {
			if(results.hasErrors()) {
				
			}
			redirAttr.addFlashAttribute("submitSelected", "inside submittestcase");
			webserviceSetupForm.setSetupTabNumber(WebserviceTest.CREATE_WS_TESTSUITE);
			@SuppressWarnings("unchecked")
			Map<Integer, Map<Integer, Map<String,List<Integer>>>> sessionmap = (Map<Integer, Map<Integer, Map<String,List<Integer>>>>) context
					.getSession().getAttribute("sessionSetIdMap");
			int environmentId = webserviceSetupForm.getEnvironmentId();
			String testSuiteName = webserviceSetupForm.getWsSuiteName();
			String visibleToOtherUsers = webserviceSetupForm.getVisibleToOtherUsers();
			WebserviceSuite webserviceSuite = new WebserviceSuite();
			if(webserviceSetupForm.getWsSuiteId() != null){
				webserviceSuite.setWebserviceSuiteId(webserviceSetupForm.getWsSuiteId());
			}
			webserviceSuite.setWebserviceSuiteName(testSuiteName);
			webserviceSuite.setEnvironmentId(environmentId);
			
			Users currentUser = (Users) context.getSession().getAttribute("currentMintUser");
			webserviceSuite.setUserId(currentUser.getUserId());
			/*if (visibleToOtherUsers.equalsIgnoreCase("Yes")) {
				webserviceSuite.setVisibleToOtherUsers(true);
			} else {
				webserviceSuite.setVisibleToOtherUsers(false);
			}*/
			webserviceSuite.setPrivateSuit(webserviceSetupForm.isPrivateSuit());
			List<WebserviceSetupForm> listWebserviceSetupForm = (List<WebserviceSetupForm>)
			context.getSession().getAttribute("listWebserviceSetupForm");
			Boolean insertStatus = webserviceTestingService.insertTestWSSuite(
					webserviceSuite, listWebserviceSetupForm);
			context.getSession().removeAttribute("listWebserviceSetupForm");
			if (insertStatus) {
				if (context.getSession().getAttribute("serviceMap") != null) {
					context.getSession().setAttribute("serviceMap", null);
				}
				redirAttr.addFlashAttribute("Success",
						"The test case for selected operation has been saved successfully.");
			} else {
				redirAttr.addFlashAttribute("Success",
						"Some issue occurred while adding Test Suite Name. Please try again.");
			}
			
			redirAttr.addFlashAttribute("webserviceSetupForm",webserviceSetupForm );
		} catch (Exception e) {
			logger.error("Exception in showSelectedServiceAndOperationV2.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		context.getSession().setAttribute("listWebserviceSetupForm", null);
		logger.debug("Exit: submitWSTestCases");
		return WebserviceTestingAdmin.REDIRECT_VIEW;
		
	}
	
	@RequestMapping(value = "/ReloadWebservices", method = RequestMethod.POST)
	public String updateSelectedServices(RedirectAttributes redirAttr,
			Model model, @ModelAttribute WebserviceSetupForm webserviceSetupForm) {
		logger.debug("Entry: ReloadWebservices");
		try {
			webserviceSetupForm.setSetupTabNumber(WebserviceTestingAdmin.RELOAD_WEBSERVICES);
			List<Webservices> webservicesList = webserviceTestingService
			.getAllWebservices();
			redirAttr.addFlashAttribute("wservicesList", webservicesList);
			for (Webservices webservice : webservicesList) {
				String serviceName = webservice.getServiceName();
				if (context.getParameter(serviceName) != null
						&& context.getParameter(serviceName).length() > 0) {
					
					// delete the existing data for the chosen service
					webservice =  webserviceTestingService.getServicefromServiceName(serviceName);
					Integer serviceId = webservice.getServiceId();
					String serviceType = webservice.getServiceType();
					String servicePath = webservice.getServiceFilePath();
					Boolean deleted = webserviceTestingService
							.deleteServiceDetails(serviceId);
					
					if (deleted) {
						Boolean result = false;
						if(!serviceType.equalsIgnoreCase("wadl")){
							result =WebserviceUtil.checkWSDL(serviceName, servicePath, configProperties, webserviceTestingService);
						} else {
							result =WebserviceUtil.checkWADL(serviceName, servicePath, configProperties, webserviceTestingService);
						}
						logger.info("service: " + serviceName + " result: "
								+ result);
						if (result) {
							redirAttr.addFlashAttribute("success", "Previous data deleted and all tables are successfully updated with data from WSDL for the selected services");
							redirAttr.addFlashAttribute("wservicesList",
									webserviceTestingService
									.getAllWebservices());
						} else {
							redirAttr.addFlashAttribute(	"success","Previous data deleted but issue while updating the tables with new data. Please try again. ");
						}
					} else {
						logger.info("Some issue in deleting the Data. Please try Again.");
						redirAttr.addFlashAttribute("deleted", "Some issue in deleting the Data. Please try Again.");
					}
				
					
				}
			}
	
			redirAttr.addFlashAttribute("webserviceSetupForm",webserviceSetupForm );
		} catch (Exception e) {
			logger.error("Exception in showSelectedServiceAndOperationV2.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		
		logger.debug("Exit: ReloadWebservices");
		return WebserviceTestingAdmin.REDIRECT_VIEW;
		
	}
	
/*	@SuppressWarnings("unchecked")
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
	}*/
	
	
	/*@RequestMapping(value = "/SaveESBPingTestDetails", method = RequestMethod.POST)
	public String saveESBPingTestDetails(RedirectAttributes redirAttr,
			Model model, @ModelAttribute WebserviceSetupForm webserviceSetupForm) {
	
		logger.info("Entry: SaveESBPingTestDetails");
		
		List<WebservicesPingTest> serviceList = null;
		List<WebservicesPingTest> pingServiceList = null;
		List<WebservicesPingTest> duplicateService = null;
		String userid = "";
		String rootPath = "";
		String fileName = "";
		boolean success = false;
		boolean result = false;

		try {
			webserviceSetupForm.setSetupTabNumber(WebserviceTesting.ESB_PING_TEST);
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
		return WebserviceTesting.REDIRECT_VIEW;
	}*/
	
	/*@RequestMapping(value = "/SubmitESBPingTestDetails", method = RequestMethod.POST)
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
			webserviceSetupForm.setSetupTabNumber(WebserviceTesting.ESB_PING_TEST);
			
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

			
			
			String emailaddr = context.getParameter("emailAddrTxt");
			boolean sendEmail = false;
			if(webserviceSetupForm.getSendEmail() == null) {
				webserviceSetupForm.setSendEmail("false");
			}
			if(webserviceSetupForm.getSendEmail().equalsIgnoreCase("true")) {
				 sendEmail = true;
			} 
			
				wSPingSchedule.setSendEmail(sendEmail);
				if(sendEmail) {
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
				}
			if (webserviceSetupForm.getScheduleType().equals("Scheduled")) {
				String dateval = webserviceSetupForm.getSchDateTime();
				Date t = (Date) formatter.parse(dateval);
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

				if (wSPingSchedule.isSendEmail()
						&& null != wSPingSchedule.getEmailIds()
						&& wSPingSchedule.getEmailIds().length() > 0) {
					String scheduleType = "OnDemand";
					
   			  	EmailUtil emailUtil = WebserviceUtil.configureEmailForESBPingTesting(
   			  			wSPingSchedule, serviceUrlList,
							scheduleType, webserviceSetupForm.getEmailTriggerOption(),
							configProperties);
					emailUtil.sendEmail();
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
		return WebserviceTesting.REDIRECT_VIEW;
	}*/
	
/*	@RequestMapping(value = "/ESBPingReports", method = RequestMethod.POST)
	public String esbPingReports(RedirectAttributes redirAttr,
			Model model, @ModelAttribute WebserviceSetupForm webserviceSetupForm) {
		logger.info("Entry: ESBPingReports");
		List<WSPingResults> listWSPingResults = null;
		try {
			webserviceSetupForm.setSetupTabNumber(WebserviceTesting.ESB_PING_RESULTS);
			String environmentId = webserviceSetupForm.getEnvironment();
			String resultsId = webserviceSetupForm.getResultDate();
			listWSPingResults = webserviceTestingService.getESBPingServiceDetails(
					environmentId, resultsId);
			List<ServiceStatus> serviceStatusList = WebserviceUtil.getPingResults(listWSPingResults, messageSource, configProperties);
			serviceStatusList = WebserviceUtil.removeEscapeCharFromXML(serviceStatusList);
			redirAttr.addFlashAttribute("serviceStatusList", serviceStatusList);
			redirAttr.addFlashAttribute("webserviceSetupForm", webserviceSetupForm);
		} catch (Exception e) {
			logger.error("Exception in ESBPingReports.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.info("Exit: ESBPingResult");
		return WebserviceTesting.REDIRECT_VIEW;
	}*/
	
/*	
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
	*/
	
	@RequestMapping(value = "/GetWSTestSuites", method = RequestMethod.POST)
	public @ResponseBody
	String getWSTestSuites( @RequestParam(value = "environmentId") int environmentId) {
		logger.debug("Entry: getWSTestSuites");
		Users currentUser = (Users) context.getSession().getAttribute("currentMintUser");
		JSONObject obj = new JSONObject();
		StringWriter out = new StringWriter();
		List<WebserviceSuite> webserviceSuiteList = null;
			try {
				webserviceSuiteList = webserviceTestingService.getWSTestSuites(
						currentUser.getUserId(), environmentId);
				for ( WebserviceSuite webserviceSuite: webserviceSuiteList ) {
					obj.put(webserviceSuite.getWebserviceSuiteId(), webserviceSuite.getWebserviceSuiteName());
				}
				//
				webserviceSuiteList = webserviceTestingService.getAllWsTestSuites(environmentId);
				for ( WebserviceSuite webserviceSuite: webserviceSuiteList ) {
					obj.put(webserviceSuite.getWebserviceSuiteId(), webserviceSuite.getWebserviceSuiteName());
				}
				obj.writeJSONString(out);
			} catch (Exception e) {
				logger.error("Exception in getESBPingDates.");
				logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
			}
		logger.debug("Exit: getWSTestSuites");
		return out.toString();
	}

	@RequestMapping(value = "/GetWSReportDatesFromSuiteId", method = RequestMethod.POST)
	public @ResponseBody
	List<WebServiceReports> getWSReportDatesFromSuiteId( @RequestParam(value = "wsSuiteId") int wsSuiteId) {
		logger.debug("Entry: getWSTestSuites");
		List<WSReports> wsReportsList = null;
		List<WebServiceReports> webServiceReportsList = new ArrayList<WebServiceReports>();
			try {
				wsReportsList = webserviceTestingService
						.getWSReportDatesFromSuiteId(wsSuiteId);
				
				for ( WSReports wsReports : wsReportsList){
					WebServiceReports webServiceReports = new WebServiceReports();
					
					if ( null != wsReports.getGeneratedDateTime() && wsReports.getGeneratedDateTime().toString().length() > 0 ){
						webServiceReports.setWsScheduleId(wsReports.getWsScheduleId());
						webServiceReports.setGeneratedTime(wsReports.getGeneratedDateTime().toString());
						webServiceReportsList.add(webServiceReports);
					}
				}
			} catch (Exception e) {
				logger.error("Exception in getWSReportDatesFromSuiteId.");
				logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
			}
			
		logger.debug("Entry: getWSTestSuites, webServiceReportsList->"+webServiceReportsList);
		return webServiceReportsList;
	}

	@RequestMapping(value = "/WSReportsRetrieve", method = RequestMethod.POST)
	public String wsReportsRetrieve(RedirectAttributes redirAttr,
			Model model, @ModelAttribute WebserviceSetupForm webserviceSetupForm) {
		logger.info("Entry: wsReportsRetrieve, webserviceSetupForm->"+webserviceSetupForm);
		List<WSReportsData> wsReportsDataList = null;
		try {
		
			int webserviceSuiteId = webserviceSetupForm.getWsSuiteId();// SuiteIds
			int environmentId = webserviceSetupForm.getEnvironmentId();
			webserviceSetupForm.setSetupTabNumber(WebserviceTestingAdmin.WEBSERVICE_REPORTS);
			
		
			redirAttr.addFlashAttribute("webserviceSuiteId", webserviceSuiteId);
			redirAttr.addFlashAttribute("environmentId", environmentId);
			redirAttr.addFlashAttribute("wsReportId", webserviceSetupForm.getWsReportsId());
			wsReportsDataList = webserviceTestingService.getWSReports(
					webserviceSuiteId, webserviceSetupForm.getWsScheduleId());
			redirAttr.addFlashAttribute("wsReportsDataList", wsReportsDataList);
			redirAttr.addFlashAttribute("webserviceSetupForm", webserviceSetupForm);
			
		} catch (Exception e) {
			logger.error("Exception in wsReportsRetrieve.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.info("Exit: wsReportsRetrieve");
		return WebserviceTestingAdmin.REDIRECT_VIEW;
	}
	
	@RequestMapping(value = "/GetSuiteDetails", method = RequestMethod.POST)
	public @ResponseBody
	String getSuiteDetails( @RequestParam(value = "wsSuiteId") int wsSuiteId) {
		logger.debug("Entry: getSuiteDetails");
		Users currentUser = (Users) context.getSession().getAttribute("currentMintUser");
		JSONObject obj = new JSONObject();
		StringWriter out = new StringWriter();
		List<WebserviceSuite> webserviceSuiteList = null;
			try {
				List<WSSuiteDetails> wsSuiteDetailsList = new ArrayList<WSSuiteDetails>();
				wsSuiteDetailsList = webserviceTestingService
						.getWSSuiteDetails(wsSuiteId);
				List<String> oprList = new ArrayList<String>();
				Map<String, String> oprServiceMap = new LinkedHashMap<String, String>();
				int k = 1;
				for (WSSuiteDetails row : wsSuiteDetailsList) {

					oprList.add(row.getOperationName());
					oprServiceMap.put(k+":"+row.getOperationName(),
							row.getServiceName() + ":"+ row.getDatasetName());
					k++;
				}
				HashSet oprHashSet = new HashSet();
				oprHashSet.addAll(oprList);
				oprList.clear();
				oprList.addAll(oprHashSet);
				
				Map<Integer, String> oprMap = new LinkedHashMap<Integer, String>();
				Map<String, Map<Integer, String>> dataMap = new LinkedHashMap<String, Map<Integer, String>>();
				List<Integer> dataSetList = new ArrayList<Integer>();
				int m = 1;
				for (WSSuiteDetails row :wsSuiteDetailsList) {
					dataSetList.add(row.getParamSetId());
					oprMap.put(row.getParamSetId(), m+":"+row.getOperationName());
					
					oprList.add(row.getOperationName());
					oprServiceMap.put(m+":"+row.getOperationName(),
							row.getServiceName() + ":"+ row.getDatasetName());
					m++;
				}
				HashSet dataSetHashSet = new HashSet();
				dataSetHashSet.addAll(dataSetList);
				dataSetList.clear();
				dataSetList.addAll(dataSetHashSet);
				
				Map<Integer, List<Map<String, String>>> dataSetMap = new LinkedHashMap<Integer, List<Map<String, String>>>();
				for (Integer dataSetID : dataSetList) {
					List<Map<String, String>> dataSetIdList = new ArrayList<Map<String, String>>();
					Map<String, String> setMap = new LinkedHashMap<String, String>();
					for (WSSuiteDetails row : wsSuiteDetailsList) {
						if (row.getParamSetId() == dataSetID) {
							setMap.put(row.getParameterName(),
									row.getParameterValue());
							dataSetIdList.add(setMap);

						}
					}
					dataSetMap.put(dataSetID, dataSetIdList);
				}
				Iterator itr = oprMap.keySet().iterator();
				int i = 1;
				int count =1;
				while (itr.hasNext()) {
					Integer dataSetId = (Integer) itr.next();
					List<Map<String, String>> dataSetIdList1 = dataSetMap.get(dataSetId);
					String oprName = oprMap.get(dataSetId);
					String serviceName = oprServiceMap.get(oprName).split(":")[0];
					String datasetName = oprServiceMap.get(oprName).split(":")[1];
					String opr = oprName.split(":")[1];
					for (Map<String, String> row : dataSetIdList1) {
						obj.put(count+":"+opr+":"+serviceName +":"+datasetName, row);
					}
					//obj.put(oprName+":"+serviceName,dataSetMap );
					count++;
				}
				obj.writeJSONString(out);
			} catch (Exception e) {
				logger.error("Exception in getSuiteDetails.");
				logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
			}
		logger.debug("Exit: getSuiteDetails");
		return out.toString();
	}
	
	//@RequestMapping(value = "/WSSubmitTestsuite", method = RequestMethod.POST)
	/*
	public @ResponseBody
	String wsSubmitTestsuite( @RequestParam(value = "suiteObj") SuiteObj suiteObj) {
		
		logger.info("Entry: wsSubmitTestsuite");
		WSSchedule wsSchedule = new WSSchedule();
		Boolean result = false;
		String emailaddress="";
		int insertId = 0;
		Integer webserviceSuiteId = 0;
		List<Integer> serviceIdList = new ArrayList<Integer>();
		List<Boolean> endPointCheckList = new ArrayList<Boolean>();
		int envId = 0;
		Boolean endPointAvailable = false;
		try {
			if (suiteObj.getWebserviceSetupForm().getRecurrence() != null
					&& suiteObj.getWebserviceSetupForm().getRecurrence().equals("on")) {
				wsSchedule.setReocurrence(true);
			} else {
				wsSchedule.setReocurrence(false);
			}
			Users currentUser = (Users) context.getSession().getAttribute("currentMintUser");
			wsSchedule.setUserId(currentUser.getUserName());
			wsSchedule.setStartDate(new Date());
			if (suiteObj.getWebserviceSetupForm().getWsSuiteId() != null
					&& suiteObj.getWebserviceSetupForm().getWsSuiteId() > 0) {
				wsSchedule.setWebserviceSuiteId(suiteObj.getWebserviceSetupForm().getWsSuiteId());


				webserviceSuiteId = wsSchedule.getWebserviceSuiteId();
				
				List<WebserviceSuiteService> webserviceSuiteServicelist = webserviceTestingService.
				getWebserviceSuiteService(webserviceSuiteId);
				for ( WebserviceSuiteService wsSuiteService :webserviceSuiteServicelist){
					serviceIdList.add(wsSuiteService.getServiceId());
				}
				envId = webserviceTestingService.getEnvironment(suiteObj.getWebserviceSetupForm()	.getWsSuiteId());
	
				Map<Integer, Boolean> endPointCheckMap = new LinkedHashMap<Integer, Boolean>();

				for (Integer serviceId : serviceIdList) {
					endPointAvailable = webserviceTestingService.getEndPointForService(
							serviceId, envId);
					endPointCheckList.add(endPointAvailable);
					endPointCheckMap.put(serviceId, endPointAvailable);
				}
			}
			
			DateFormat formatter = new SimpleDateFormat("HH:mm");
			if (suiteObj.getWebserviceSetupForm().getScheduleType().equals("Scheduled")) {
				String dateval = DateUtil.convertToString(new Date(), DateUtil.WS_DATE_PICKER_FORMAT) + " "+ suiteObj.getWebserviceSetupForm().getSchDateTime();
				Date t = (Date) formatter.parse(dateval);
				Time timeValue = new Time(formatter.parse(dateval).getTime());
				wsSchedule.setStartTime(DateUtil.convertToDate(dateval, DateUtil.UI_DATE_PICKER_FORMAT));
				wsSchedule.setScheduled(true);
			} else {
				//String stringTime = formatter.format(new Date());
				String stringTime = DateUtil.convertToString(new Date(), DateUtil.WS_DATE_PICKER_FORMAT) + " "+ formatter.format(new Date());
				//Date t = (Date) formatter.parse(stringTime);
				//Time timeValue = new Time(formatter.parse(stringTime).getTime());
				wsSchedule.setStartTime(DateUtil.convertToDate(stringTime, DateUtil.UI_DATE_PICKER_FORMAT));
				wsSchedule.setScheduled(false);
			}

			if ( null != suiteObj.getWebserviceSetupForm().getEmailRecepients() && suiteObj.getWebserviceSetupForm().getEmailRecepients().trim().length() > 0 ){
				wsSchedule.setEmailIds(currentUser.getEmailId() + "," + suiteObj.getWebserviceSetupForm().getEmailRecepients());
			}else{
				wsSchedule.setEmailIds(currentUser.getEmailId());
			}
			
			if (!endPointCheckList.contains(false)) {
				

				insertId = webserviceTestingService
						.insertWSSchedule(wsSchedule); 
				if (insertId > 0) {
					result = true;
				}

				if (suiteObj.getWebserviceSetupForm().getWsResultsId() != null
						&& suiteObj.getWebserviceSetupForm().getWsResultsId() > 0) {
					WSBaseline wsBaseline = new WSBaseline();
					Date now = new Date();
					wsBaseline.setCreatedDateTime(now);
					int wsBaselineScheduleId = suiteObj.getWebserviceSetupForm()
							.getWsResultsId();
					wsBaseline.setWsBaselineScheduleId(wsBaselineScheduleId);
				
					wsBaseline.setWsScheduleId(insertId);
					result = result
							&& webserviceTestingService.wsSaveBaseline(wsBaseline);
				}

				if (result) {
					redirAttr.addFlashAttribute("Success",
							"Web Service Test Suite Schedule Details are Saved Successfully.");
				} else if (!result) {
					redirAttr.addFlashAttribute("error", "Sorry! Not able to save Web Service Test Suite Schedule Details.");
				}
			}
			else {
				redirAttr.addFlashAttribute(
						"error", "Sorry! End Point is not available for some service(s) in the selected Test Suite. "
								+ "Please enter the endpoint details in the Web Service Add details page");
			}
			suiteObj.getWebserviceSetupForm().setSetupTabNumber(WebserviceTesting.SCHEDULE_WEBSERVICE);
			
		} catch (Exception e) {
			logger.error("Exception in wsReportsRetrieve.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.info("Exit: wsSubmitTestsuite");
		return WebserviceTesting.REDIRECT_VIEW;
	}
	*/
	@RequestMapping(value = "/GetWSBaselineDates", method = RequestMethod.POST)
	public @ResponseBody
	List<WSBaselineDate> getWSBaselineDates( @RequestParam(value = "wsSuiteId") int wsSuiteId) {
		logger.debug("Entry: GetWSBaselineDates");
		List<WSBaselineDate> baselineDatesList = new ArrayList<WSBaselineDate>();
		WSBaselineDate wsBaselineDate = null;
			try {
				List<WSResults> wsResultsList = new ArrayList<WSResults>();
				wsResultsList = webserviceTestingService
						.getWSBaselineDates(wsSuiteId);

					for (WSResults testResult : wsResultsList) {
						wsBaselineDate = new WSBaselineDate();
						
						if ( null != testResult.getCreatedDateTime() && testResult.getCreatedDateTime().toString().length() > 0 ){
							wsBaselineDate.setDateTime(testResult.getCreatedDateTime().toString());
							wsBaselineDate.setWsScheduleId(testResult.getWsScheduleId());
							baselineDatesList.add(wsBaselineDate);
						}
					}
		
			} catch (Exception e) {
				logger.error("Exception in getESBPingDates.");
				logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
			}
		logger.debug("Exit: getSuiteDetails");
		return baselineDatesList;
	}
	
	private int getServicePaginateCount(List<String> webservicesList) {
		int listsize = 0;
		int paginateSize = 0;
		if (null == webservicesList || webservicesList.size() < 1) {
			return 0;
		}

		for (String webservice : webservicesList) {
				listsize++;
		}
		paginateSize = (listsize / 2) + (listsize % 2);
		return paginateSize;
	}
	

	/*@RequestMapping(value = "/submitWsSuite", method = RequestMethod.POST)
	public String submitWsSuite(RedirectAttributes redirAttr,
			Model model,@Valid @ModelAttribute WebserviceSetupForm webserviceSetupForm, BindingResult results) {
		logger.debug("Entry: submitWsSuite");
		try {
			if(results.hasErrors()) {
				
			}
			redirAttr.addFlashAttribute("submitSelected", "inside submittestcase");
			webserviceSetupForm.setSetupTabNumber(WebserviceTesting.CREATE_WS_TESTSUITE);
			@SuppressWarnings("unchecked")
			Map<Integer, Map<Integer, Map<String,List<Integer>>>> sessionmap = (Map<Integer, Map<Integer, Map<String,List<Integer>>>>) context
					.getSession().getAttribute("sessionSetIdMap");
			int environmentId = webserviceSetupForm.getEnvironmentId();
			String testSuiteName = webserviceSetupForm.getWsSuiteName();
			String visibleToOtherUsers = webserviceSetupForm.getVisibleToOtherUsers();
			WebserviceSuite webserviceSuite = new WebserviceSuite();
			if(webserviceSetupForm.getWsSuiteId() != null){
				webserviceSuite.setWebserviceSuiteId(webserviceSetupForm.getWsSuiteId());
			}
			webserviceSuite.setWebserviceSuiteName(testSuiteName);
			webserviceSuite.setEnvironmentId(environmentId);
			
			Users currentUser = (Users) context.getSession().getAttribute("currentMintUser");
			webserviceSuite.setUserId(currentUser.getUserId());
			if (visibleToOtherUsers.equalsIgnoreCase("Yes")) {
				webserviceSuite.setVisibleToOtherUsers(true);
			} else {
				webserviceSuite.setVisibleToOtherUsers(false);
			}
			webserviceSuite.setPrivateSuit(webserviceSetupForm.isPrivateSuit());
			
			WebserviceSetupForm wsSetupForm = new WebserviceSetupForm();
			Integer count = webserviceSetupForm.getParametersets().length;
			for(int i=0;i<count;i++){
				wsSetupForm.setServiceId(webserviceSetupForm.getServiceId());
				wsSetupForm.setOperationId(webserviceSetupForm.getOperationId());
				wsSetupForm.setReqInputType(webserviceSetupForm.getReqInputType());
			}
			List<WebserviceSetupForm> listWebserviceSetupForm = (List<WebserviceSetupForm>)
			context.getSession().getAttribute("listWebserviceSetupForm");
			Boolean insertStatus = webserviceTestingService.insertTestWSSuite(
					webserviceSuite, listWebserviceSetupForm);
			context.getSession().removeAttribute("listWebserviceSetupForm");
			if (insertStatus) {
				if (context.getSession().getAttribute("serviceMap") != null) {
					context.getSession().setAttribute("serviceMap", null);
				}
				redirAttr.addFlashAttribute("Success",
						"The test case for selected operation has been saved successfully.");
			} else {
				redirAttr.addFlashAttribute("Success",
						"Some issue occurred while adding Test Suite Name. Please try again.");
			}
			
			redirAttr.addFlashAttribute("webserviceSetupForm",webserviceSetupForm );
		} catch (Exception e) {
			logger.error("Exception in showSelectedServiceAndOperationV2.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		context.getSession().setAttribute("listWebserviceSetupForm", null);
		logger.debug("Exit: submitWsSuite");
		return WebserviceTesting.REDIRECT_VIEW;
		
	}*/
	
	
	
	@RequestMapping(value = "/submitWsSuite", method = RequestMethod.POST )
	public @ResponseBody
	String submitWsSuite(@RequestBody SuiteObj suiteObj) {
		logger.debug("Entry: submitWsSuite");
		String result="";
		WebserviceSuite webserviceSuite = new WebserviceSuite();
		try {
			if(suiteObj.getWsSuiteId() > 0){
				webserviceSuite.setWebserviceSuiteId(suiteObj.getWsSuiteId());
			}
			webserviceSuite.setWebserviceSuiteName(suiteObj.getWsSuiteName());
			webserviceSuite.setEnvironmentId(suiteObj.getEnvironmentId());
			
			Users currentUser = (Users) context.getSession().getAttribute("currentMintUser");
			webserviceSuite.setUserId(currentUser.getUserId());
			
			if(suiteObj.getPrivateSuit().equalsIgnoreCase("true")) {
				webserviceSuite.setPrivateSuit(true);
			} else {
				webserviceSuite.setPrivateSuit(false);
			}
			WebserviceSetupForm wsSetupForm = null;
			Integer count = suiteObj.getWsSuiteArray().length;
			WsSuite[] wsSuiteArray = suiteObj.getWsSuiteArray();
			List<WebserviceSetupForm> listWebserviceSetupForm = new ArrayList<WebserviceSetupForm>();
			
			
			for(WsSuite wsSuite : wsSuiteArray){
				wsSetupForm = new WebserviceSetupForm();
				wsSetupForm.setServiceId(wsSuite.getServiceId());
				wsSetupForm.setOperationId(wsSuite.getOperationId());
				wsSetupForm.setEnvironmentId(wsSuite.getEnvironmentId());
				wsSetupForm.setParamSetId(((Integer)wsSuite.getParamset()).toString());
				wsSetupForm.setReqInputType(suiteObj.getReqInputType());
				listWebserviceSetupForm.add(wsSetupForm);
			}
		
			Boolean insertStatus = webserviceTestingService.insertTestWSSuite(
					webserviceSuite, listWebserviceSetupForm);
			if(insertStatus) {
				result = "true";
			} else {
				result = "false";
			}
             
		} catch (Exception e) {
			logger.error("Exception in submitWsSuite.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}

		logger.debug("Exit: submitWsSuite");
		return result;
		
	}
	
}
