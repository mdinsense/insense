package uitesting;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cts.mint.common.entity.SuitGroupXref;
import com.cts.mint.common.entity.Users;
import com.cts.mint.common.model.ScheduleStatus;
import com.cts.mint.common.service.HomeService;
import com.cts.mint.common.utils.Constants;
import com.cts.mint.common.utils.UIFunctionalityType;
import com.cts.mint.common.utils.UserServiceUtils;
import com.cts.mint.uitesting.entity.ApplicationModuleXref;
import com.cts.mint.uitesting.entity.Environment;
import com.cts.mint.uitesting.entity.LoginUserDetails;
import com.cts.mint.uitesting.entity.Suit;
import com.cts.mint.uitesting.model.TestingForm;
import com.cts.mint.uitesting.service.ApplicationConfigService;
import com.cts.mint.uitesting.service.ApplicationModuleService;
import com.cts.mint.uitesting.service.ApplicationService;
import com.cts.mint.uitesting.service.EnvironmentService;
import com.cts.mint.uitesting.service.ExcludeURLService;
import com.cts.mint.uitesting.service.HtmlReportConfigService;
import com.cts.mint.uitesting.service.IncludeURLService;
import com.cts.mint.uitesting.service.LoginUserService;
import com.cts.mint.uitesting.service.UiTestingService;

@Controller
public class FunctionalityTestingController {

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
	private ApplicationService applicationService;

	@Autowired
	private EnvironmentService environmentService;

	@Autowired
	private IncludeURLService includeUrlService;

	@Autowired
	private ExcludeURLService excludeURLService;

	@Autowired
	private ApplicationConfigService applicationConfigService;

	@Autowired
	private ApplicationModuleService applicationModuleService;

	@Autowired
	private HtmlReportConfigService htmlReportConfigService;

	@Autowired
	private LoginUserService loginUserService;
	
	@Autowired
	private HomeService homeService;
	
	@Autowired
	private UiTestingService uiTestingService;

	private static Logger logger = Logger
			.getLogger(FunctionalityTestingController.class);


	/**
	 * gets the basic data for Functionality testing page display
	 * 
	 * @param model
	 */
	@RequestMapping(value = { "/FunctionalityTesting" }, method = RequestMethod.GET)
	public String functionalityTestingGet(Model model) {

		logger.debug("Entry: FunctionalityTesting");
		TestingForm testingForm = null;
		
		if (null != model.asMap()
				&& model.asMap().containsKey("testingForm")) {
			testingForm = (TestingForm) model.asMap().get(
					"testingForm");
		} 
		
		try {
			model.addAttribute("applications",
					applicationService.getAllApplicationDetails());
			if(testingForm != null) {
				if( testingForm.getApplicationId() > 0 ){
					model.addAttribute("environmentCategoryList",
							 environmentService.getEnvironmentCategory(testingForm.getApplicationId()));
					if(testingForm.getEnvironmentCategoryId() > 0 ){
						model.addAttribute("loginList",
								 loginUserService.getLoginUserDetails(testingForm.getApplicationId(), testingForm.getEnvironmentCategoryId()));
						model.addAttribute("environmentList", 
								environmentService.getEnvironmentsByCategory(testingForm.getEnvironmentCategoryId(), testingForm.getApplicationId()));
					}
				}
				
				if(testingForm.getEditOrViewMode().equals("ViewMode")){
					model.addAttribute("warning",
							"You don't have permission to modify");
				}
				
			}
				
			model.addAttribute("applicationModuleList",
				applicationModuleService.getApplicationModuleList());
			model.addAttribute("allModules", Constants.MODULE_ALL);
			model.addAttribute("browserTypeList",
					homeService.getBrowserTypes());
			
			model.addAttribute("testingForm", testingForm);
		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while loading Application home");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: FunctionalityTesting, uiTestingSetupForm->");
		
		return "uitesting/UiFunctionalityTesting";
	}
	
	@RequestMapping(value = { "/FunctionalityTesting" }, method = RequestMethod.POST)
	public String functionalityTestingForm(
			@ModelAttribute TestingForm testingForm, Model model,
			RedirectAttributes redirAttr) {
		logger.debug("Entry: FunctionalityTesting, functionalityTesting->"
				+ testingForm);
		try {

			redirAttr.addFlashAttribute("applications",
					applicationService.getAllApplicationDetails());
			if(testingForm != null) {
				if( testingForm.getApplicationId() > 0 ){
					redirAttr.addFlashAttribute("environmentCategoryList",
							 environmentService.getEnvironmentCategory(testingForm.getApplicationId()));
					if(testingForm.getEnvironmentCategoryId() > 0 ){
						redirAttr.addFlashAttribute("loginList",
								 loginUserService.getLoginUserDetails(testingForm.getApplicationId(), testingForm.getEnvironmentCategoryId()));
					}
				}
				
			}
			redirAttr.addFlashAttribute("applicationModuleList",
				applicationModuleService.getApplicationModuleList());
			redirAttr.addFlashAttribute("allModules", Constants.MODULE_ALL);
			redirAttr.addFlashAttribute("browserTypeList",
					homeService.getBrowserTypes());
			
		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while loading Application home");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: FunctionalityTesting, uiTestingSetupForm->");
		redirAttr.addFlashAttribute("testingForm", testingForm);
		return "redirect:/FunctionalityTesting.ftl";
	}		
			

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/GetApplicationModules", method = RequestMethod.POST)
	public @ResponseBody
	List<ApplicationModuleXref> GetApplicationModules(
			@RequestParam(value = "applicationId") int applicationId,@RequestParam(value = "categoryId") int categoryId) {
		logger.debug("Entry: GetApplicationModules ");

		List<ApplicationModuleXref> applicationModuleXrefList = new ArrayList<ApplicationModuleXref>();
		List<ApplicationModuleXref> applicationModuleXrefListForALL = new ArrayList<ApplicationModuleXref>();
		
		try {
			if (applicationId > 0 && categoryId > 0) {
				applicationModuleXrefList = applicationModuleService.getModuleListForApplicationAndCategory(applicationId, categoryId);
				if (applicationModuleXrefList != null) {
					for (ApplicationModuleXref applicationModuleXref : applicationModuleXrefList) {
						if(applicationModuleService.confirmModuleIdForALL(applicationModuleXref)){
							applicationModuleXref.setApplicationModuleXrefId(Constants.MODULE_ALL);
						}
						applicationModuleXrefListForALL.add(applicationModuleXref);
					} 
				}
			}	
		} catch (Exception exp) {
			logger.error("Exception occured during GetApplicationModules execution.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(exp));
		}
		logger.debug("Exit: GetApplicationModules");
		return applicationModuleXrefListForALL;
	}
	
/*	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/GetApplicationModules", method = RequestMethod.POST)
	public @ResponseBody
	String GetApplicationModules(
			@RequestParam(value = "applicationId") int applicationId,@RequestParam(value = "categoryId") int categoryId) {
		logger.debug("Entry: GetApplicationModules ");

		List<ApplicationModuleXref> applicationModuleXrefList = new ArrayList<ApplicationModuleXref>();
		JSONObject obj = new JSONObject();
		StringWriter out = new StringWriter();

		try {
			//add All modules as default.
			//obj.put(Constants.MODULE_ALL, "All");
			if (applicationId > 0 && categoryId > 0) {
				applicationModuleXrefList = applicationModuleService.getModuleListForApplicationAndCategory(applicationId, categoryId);
				if (applicationModuleXrefList != null) {
					for (ApplicationModuleXref applicationModuleXref : applicationModuleXrefList) {
						obj.put(applicationModuleXref.getApplicationModuleXrefId(),
								applicationModuleXref.getModuleName());
					}
				} else {
					obj.put("", "");
				}

				obj.writeJSONString(out);
			}

		} catch (Exception exp) {
			logger.error("Exception occured during GetApplicationModules execution.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(exp));
		}
		logger.debug("Exit: GetApplicationModules");
		return out.toString();
	}*/
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/GetLoginUserDetails", method = RequestMethod.POST)
	public @ResponseBody
	String getLoginUserDetailsFuncationalityTesting(
			@RequestParam(value = "applicationId") int applicationId,@RequestParam(value = "categoryId") int categoryId) {
		logger.debug("Entry: GetLoginUserDetails ");

		List<LoginUserDetails> loginUserDetailsList = new ArrayList<LoginUserDetails>();
		JSONObject obj = new JSONObject();
		StringWriter out = new StringWriter();

		try {
			if (applicationId > 0 && categoryId > 0) {
				loginUserDetailsList = loginUserService.getLoginUserDetails(applicationId, categoryId);
				for(LoginUserDetails loginUserDetails : loginUserDetailsList) {
					if(loginUserDetails != null) {
					obj.put(loginUserDetails.getLoginUserDetailId(),loginUserDetails.getLoginId());
					} else {
						obj.put("", "");
					}
				}
				obj.writeJSONString(out);
			}

		} catch (Exception exp) {
			logger.error("Exception :Unexpected error occured while loading login user details");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(exp));
		}
		logger.debug("Exit: GetLoginUserDetails");
		return out.toString();
	}
	
	@RequestMapping(value = "/SaveFunctionalityTestingSuit", method = RequestMethod.POST)
	public String saveFunctionalityTestingSuit(RedirectAttributes redirAttr,
			Model model, @ModelAttribute TestingForm testingForm) {

		logger.debug("Entry: SaveFunctionalityTestingSuit, functionalityTesting->"+ testingForm);
		Suit suit = new Suit();
		try {
			
			suit.setApplicationId(testingForm.getApplicationId());
			suit.setEnvironmentCategoryId(testingForm.getEnvironmentCategoryId());
			suit.setBrowserTypeId(testingForm.getBrowserType());
			suit.setSuitName(testingForm.getSuitName());
			suit.setLoginId(testingForm.getLoginId());
			suit.setTextCompare(testingForm.isTextCompare());
			suit.setScreenCompare(testingForm.isScreenCompare());
			suit.setHtmlCompare(testingForm.isHtmlCompare());
			suit.setModuleId(getAsString(testingForm.getModules()));
			suit.setSolutionTypeId(1);
			suit.setPrivateSuit(testingForm.isPrivateSuit());
			suit.setRegressionTesting(true);
			suit.setType("Functional");
			suit.setFunctionalityTypeId(UIFunctionalityType.FUNCTIONAL_TESTING.getFunctionalityTypeId());
			if(testingForm.getTransactionOrCrawlerType().equals("1")){
				suit.setTransactionTestcase(true);	
			}else{
				suit.setTransactionTestcase(false);
			}
			suit.setUrlLevel(testingForm.getUrlLevel());
			
			Users currentMintUser = (Users)context.getSession().getAttribute("currentMintUser");

			suit.setUserId(currentMintUser.getUserId());
			
			if (this.uiTestingService.saveFunctionalityTestingSuit(suit)) {
				this.saveSuitGroupXref(suit
						.getSuitId());
				redirAttr.addFlashAttribute("success",
						"Functionality Testing Suit saved successfully");
			} else {
				redirAttr.addFlashAttribute("error",
						"Error while saving Functionality Testing Suit");
			}

		} catch (DataIntegrityViolationException es){
			redirAttr
			.addFlashAttribute("error",
					"Duplicate Suit Name. Please try with different Suit name.");
		}catch (Exception exception) {
			logger.error("Exception :Unexpected error occured while Saving Functionality Testing Suit");
			logger.error("Stack Trace :"
					+ ExceptionUtils.getStackTrace(exception));
			redirAttr
					.addFlashAttribute("error",
							"Unexpected error occured while Saving Functionality Testing Suit");
		}

		logger.debug("Exit: SaveFunctionalityTestingSuit");
		return "redirect:/FunctionalityTesting.ftl";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/GetEnvironmentLoginUrl", method = RequestMethod.POST)
	public @ResponseBody
	String getEnvironmentLoginUrl(
			@RequestParam(value = "environmentId") int environmentId) {

		logger.debug("Entry: GetEnvironmentLoginUrl ");

		Environment environment = null;
		JSONObject obj = new JSONObject();
		StringWriter out = new StringWriter();
		try {
			if (environmentId > 0) {
				environment = environmentService.getEnvironment(environmentId);

				if (environment != null) {
					obj.put(environment.getEnvironmentId(),
							environment.getLoginOrHomeUrl());
				} else {
					obj.put("", "");
				}
			}
			obj.writeJSONString(out);

		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while GetEnvironmentLoginUrl");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));

		}
		logger.debug("Exit: GetEnvironmentLoginUrl");
		return out.toString();
	}
	
	@RequestMapping(value = "/UpdateFunctionalityTestingSuit", method = RequestMethod.POST)
	public String updateFunctionalityTestingSuit(RedirectAttributes redirAttr,
			Model model, @ModelAttribute TestingForm testingForm) {

		logger.debug("Entry: UpdateFunctionalityTestingSuit, functionalityTesting->"+ testingForm);
		Suit suit = new Suit();
		try {
			suit = homeService.getSavedSuits(testingForm.getSuitId());
			
			if(suit.getSuitId()>0){
				suit.setSuitId(testingForm.getSuitId());
				suit.setApplicationId(testingForm.getApplicationId());
				suit.setEnvironmentCategoryId(testingForm.getEnvironmentCategoryId());
				suit.setBrowserTypeId(testingForm.getBrowserType());
				suit.setSuitName(testingForm.getSuitName());
				suit.setLoginId(testingForm.getLoginId());
				suit.setTextCompare(testingForm.isTextCompare());
				suit.setScreenCompare(testingForm.isScreenCompare());
				suit.setHtmlCompare(testingForm.isHtmlCompare());
				suit.setModuleId(getAsString(testingForm.getModules()));
				suit.setSolutionTypeId(1);
				suit.setPrivateSuit(testingForm.isPrivateSuit());
				suit.setType("Functional");
				if(testingForm.getTransactionOrCrawlerType().equals("1")){
					suit.setTransactionTestcase(true);	
				}else{
					suit.setTransactionTestcase(false);
				}
				suit.setUrlLevel(testingForm.getUrlLevel());
			}
			
			Users currentMintUser = (Users)context.getSession().getAttribute("currentMintUser");
			suit.setUserId(currentMintUser.getUserId());
			
			if (this.uiTestingService.saveFunctionalityTestingSuit(suit)) {
				redirAttr.addFlashAttribute("success",
						"Functionality Testing Suit updated successfully");
			} else {
				redirAttr.addFlashAttribute("error",
						"Error while updating Functionality Testing Suit");
			}
			
			

		} catch (DataIntegrityViolationException es){
			redirAttr
			.addFlashAttribute("error",
					"Duplicate Suit Name. Please try with different Suit name.");
		}catch (Exception exception) {
			logger.error("Exception :Unexpected error occured while UpdateFunctionalityTestingSuit");
			logger.error("Stack Trace :"
					+ ExceptionUtils.getStackTrace(exception));
			redirAttr
					.addFlashAttribute("error",
							"Unexpected error occured while UpdateFunctionalityTestingSuit");
		}

		logger.debug("Exit: UpdateFunctionalityTestingSuit");
		return "redirect:/FunctionalityTesting.ftl";
	}

	private String getAsString(String[] modules) {
		String modulesString ="";
		if ( null == modules || modules.length == 0 ){
			return "0";
		}
		
		for ( String module: modules){
			modulesString += module + ",";
		}
		
		if ( modulesString.length() > 0 ){
			modulesString = modulesString.substring(0, modulesString.length() -1 );
		}
		return modulesString;
	}
	
	private boolean saveSuitGroupXref(int suitId)  throws Exception{
		boolean saved = false;
		SuitGroupXref suitGroup = new SuitGroupXref();
		suitGroup.setGroupId(UserServiceUtils
				.getCurrentUserGroupIdFromRequest(context));
		suitGroup.setSuitId(suitId);
		
		saved = uiTestingService
				.saveSuitGroupXref(suitGroup);
		return saved;
	}
	
}