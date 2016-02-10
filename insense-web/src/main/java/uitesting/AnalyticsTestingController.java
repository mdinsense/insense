package uitesting;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cts.mint.common.entity.SuitGroupXref;
import com.cts.mint.common.entity.Users;
import com.cts.mint.common.service.HomeService;
import com.cts.mint.common.utils.Constants;
import com.cts.mint.common.utils.UIFunctionalityType;
import com.cts.mint.common.utils.UserServiceUtils;
import com.cts.mint.uitesting.entity.Suit;
import com.cts.mint.uitesting.model.FunctionalityTestingForm;
import com.cts.mint.uitesting.model.RegressionTestExecutionForm;
import com.cts.mint.uitesting.model.TestingForm;
import com.cts.mint.uitesting.service.ApplicationModuleService;
import com.cts.mint.uitesting.service.ApplicationService;
import com.cts.mint.uitesting.service.EnvironmentService;
import com.cts.mint.uitesting.service.LoginUserService;
import com.cts.mint.uitesting.service.UiTestingService;

@Controller
public class AnalyticsTestingController {

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
	private ApplicationModuleService applicationModuleService;

	@Autowired
	private HomeService homeService;
	
	@Autowired
	private LoginUserService loginUserService;
	
	@Autowired
	private EnvironmentService environmentService;
	
	@Autowired
	private UiTestingService uiTestingService;

	private static Logger logger = Logger
			.getLogger(AnalyticsTestingController.class);


	/**
	 * gets the basic data for Analytics testing page display
	 * 
	 * @param model
	 */
	@RequestMapping(value = { "/AnalyticsTesting" }, method = RequestMethod.GET)
	public String analyticsTestingGet(Model model) {

		logger.debug("Entry: AnalyticsTesting");
		TestingForm testingForm = null;
		
		if (null != model.asMap()
				&& model.asMap().containsKey("testingForm")) {
			testingForm = (TestingForm) model.asMap().get(
					"testingForm");
		} 
		
		try {
			model.addAttribute("applications",
					applicationService.getAllApplicationDetails());
			model.addAttribute("waitTime", Constants.DEFAULT_WAIT_TIME);
			if(testingForm != null) {
				if( testingForm.getApplicationId() > 0 ){
					model.addAttribute("environmentCategoryList",
							 environmentService.getEnvironmentCategory(testingForm.getApplicationId()));
					if(testingForm.getEnvironmentCategoryId() > 0 ){
						model.addAttribute("loginList",
								 loginUserService.getLoginUserDetails(testingForm.getApplicationId(), testingForm.getEnvironmentCategoryId()));
					}
				}
				
				if(testingForm.getEditOrViewMode().equals("ViewMode")){
					model.addAttribute("warning",
							"You don't have permission to modify");
				}
				model.addAttribute("waitTime", testingForm.getWaitTime());
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
		logger.debug("Exit: AnalyticsTesting, uiTestingSetupForm->");
		return "uitesting/UiAnalyticsTesting";
	}
	
	@RequestMapping(value = { "/AnalyticsTesting" }, method = RequestMethod.POST)
	public String analyticsTestingForm(
			@ModelAttribute TestingForm testingForm, Model model,
			RedirectAttributes redirAttr) {
		logger.debug("Entry: AnalyticsTesting, testingForm->"
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
		logger.debug("Exit: AnalyticsTesting");
		redirAttr.addFlashAttribute("testingForm", testingForm);
		return "redirect:/AnalyticsTesting.ftl";
	}		
	
	@RequestMapping(value = "/SaveAnalyticsTesting", method = RequestMethod.POST)
	public String saveAnalyticsTesting(RedirectAttributes redirAttr,
			Model model, @ModelAttribute TestingForm testingForm) {

		logger.debug("Entry: SaveAnalyticsTesting, functionalityTesting->"+ testingForm);
		Suit suit = new Suit();
		try {
			suit.setApplicationId(testingForm.getApplicationId());
			suit.setEnvironmentCategoryId(testingForm.getEnvironmentCategoryId());
			suit.setLoginId(testingForm.getLoginId());
			suit.setAnalyticsTesting(true);
			if(testingForm.isSmartUser() == true){
				suit.setRobotClicking(true);
			}else{
				suit.setRobotClicking(false);
			}
			suit.setScrollPage(testingForm.isScrollPage());
			suit.setWaitTime(testingForm.getWaitTime());
			suit.setBrowserTypeId(testingForm.getBrowserType());
			suit.setSuitName(testingForm.getSuitName());
			suit.setModuleId(getAsString(testingForm.getModules()));
			suit.setSolutionTypeId(1);
			suit.setPrivateSuit(testingForm.isPrivateSuit());
			suit.setTextCompare(false);
			suit.setScreenCompare(false);
			suit.setHtmlCompare(false);
			suit.setBrokenUrlReport(false);
			suit.setType("Analytics");
			suit.setFunctionalityTypeId(UIFunctionalityType.ANALYTICS_TESTING.getFunctionalityTypeId());
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
						"Analytics Testing Suit saved successfully");
			} else {
				redirAttr.addFlashAttribute("error",
						"Error while saving Analytics Testing Suit");
			}

		} catch (DataIntegrityViolationException es){
			redirAttr
			.addFlashAttribute("error",
					"Duplicate Suit Name. Please try with different Suit name.");
		}catch (Exception exception) {
			logger.error("Exception :Unexpected error occured while Saving Analytics Testing Suit");
			logger.error("Stack Trace :"
					+ ExceptionUtils.getStackTrace(exception));
			redirAttr
					.addFlashAttribute("error",
							"Unexpected error occured while Saving Analytics Testing Suit");
		}

		logger.debug("Exit: SaveAnalyticsTesting");
		return "redirect:/AnalyticsTesting.ftl";
	}
	
	@RequestMapping(value = "/UpdateAnalyticsTesting", method = RequestMethod.POST)
	public String updateAnalyticsTesting(RedirectAttributes redirAttr,
			Model model, @ModelAttribute TestingForm testingForm) {

		logger.debug("Entry: UpdateAnalyticsTesting, testingForm->"+ testingForm);
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
				/*if (testingForm.isSmartUser() != null && testingForm.isSmartUser().equals("on")) {	
					suit.setAnalyticsTesting(true);
				}*/
				//suit.setAnalyticsTesting(testingForm.isSmartUser() );
				suit.setAnalyticsTesting(true);
				if(testingForm.isSmartUser() == true){
					suit.setRobotClicking(true);
				}else{
					suit.setRobotClicking(false);
				}
				suit.setModuleId(getAsString(testingForm.getModules()));
				suit.setSolutionTypeId(1);
				suit.setPrivateSuit(testingForm.isPrivateSuit());
				suit.setType("Analytics");
				suit.setWaitTime(testingForm.getWaitTime());
				suit.setScrollPage(testingForm.isScrollPage());
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
						"Analytics Testing Suit updated successfully");
			} else {
				redirAttr.addFlashAttribute("error",
						"Error while updating Analytics Testing Suit");
			}
			
			

		} catch (DataIntegrityViolationException es){
			redirAttr
			.addFlashAttribute("error",
					"Duplicate Suit Name. Please try with different Suit name.");
		}catch (Exception exception) {
			logger.error("Exception :Unexpected error occured while UpdateAnalyticsTesting");
			logger.error("Stack Trace :"
					+ ExceptionUtils.getStackTrace(exception));
			redirAttr
					.addFlashAttribute("error",
							"Unexpected error occured while UpdateAnalyticsTesting");
		}

		logger.debug("Exit: UpdateAnalyticsTesting");
		return "redirect:/AnalyticsTesting.ftl";
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