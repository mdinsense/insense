package com.ensense.insense.web.uitesting;

import com.ensense.insense.data.common.HomeService;
import com.ensense.insense.data.common.entity.SuitGroupXref;
import com.ensense.insense.data.common.entity.Users;
import com.ensense.insense.data.common.utils.Constants;
import com.ensense.insense.data.uitesting.entity.Suit;
import com.ensense.insense.data.uitesting.entity.SuitBrokenReportsXref;
import com.ensense.insense.data.utils.UIFunctionalityType;
import com.ensense.insense.data.utils.UserServiceUtils;
import com.ensense.insense.services.uiadmin.*;
import com.ensense.insense.web.uiadmin.form.schedule.TestingForm;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;


@Controller
public class BrokenLinkTestingController {

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
			.getLogger(BrokenLinkTestingController.class);


	/**
	 * gets the basic data for BrokenLink testing page display
	 * 
	 * @param model
	 */
	@RequestMapping(value = { "/BrokenLinkCheck" }, method = RequestMethod.GET)
	public String brokenLinkTestingGet(Model model) {

		logger.debug("Entry: BrokenLinkCheck");
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
		logger.debug("Exit: BrokenLinkCheck, testingForm->"+testingForm);
		return "uitesting/UiBrokenLinkTesting";
	}
	
	@RequestMapping(value = { "/BrokenLinkCheck" }, method = RequestMethod.POST)
	public String brokenTestingForm(
			@ModelAttribute TestingForm testingForm, Model model,
			RedirectAttributes redirAttr) {
		logger.debug("Entry: BrokenLinkCheck, testingForm->"
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
		logger.debug("Exit: BrokenLinkCheck");
		redirAttr.addFlashAttribute("testingForm", testingForm);
		return "redirect:/BrokenLinkCheck.ftl";
	}		
	
	@RequestMapping(value = "/SaveBrokenLinkTesting", method = RequestMethod.POST)
	public String saveBrokenTesting(RedirectAttributes redirAttr,
			Model model, @ModelAttribute TestingForm testingForm) {

		logger.debug("Entry: SaveBrokenTesting, testingForm->"+ testingForm);
		Suit suit = new Suit();
		SuitBrokenReportsXref suitBrokenReportsXref1=new SuitBrokenReportsXref();
		SuitBrokenReportsXref suitBrokenReportsXref2=new SuitBrokenReportsXref();
		SuitBrokenReportsXref suitBrokenReportsXref3=new SuitBrokenReportsXref();
		ArrayList<SuitBrokenReportsXref> suitBrokenReportsXrefList = new ArrayList<SuitBrokenReportsXref>();
		try {
			
			suit.setApplicationId(testingForm.getApplicationId());
			suit.setEnvironmentCategoryId(testingForm.getEnvironmentCategoryId());
			suit.setLoginId(testingForm.getLoginId());
			suit.setBrokenUrlReport(true);
			
			suit.setProcessChildUrls(testingForm.isProcessUrl());
			suit.setBrowserTypeId(testingForm.getBrowserType());
			suit.setSuitName(testingForm.getSuitName());
			suit.setModuleId(getAsString(testingForm.getModules()));
			suit.setSolutionTypeId(1);
			suit.setPrivateSuit(testingForm.isPrivateSuit());
			suit.setTextCompare(false);
			suit.setScreenCompare(false);
			suit.setHtmlCompare(false);
			suit.setAnalyticsTesting(false);
			suit.setType("Broken");
			suit.setFunctionalityTypeId(UIFunctionalityType.BROKENLINKS.getFunctionalityTypeId());
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
				
				
				if(testingForm.isBrokenLinks()){
					suitBrokenReportsXref1.setSuitId(suit.getSuitId());
					suitBrokenReportsXref1.setReportName("BrokenLink");
					suitBrokenReportsXrefList.add(suitBrokenReportsXref1);
				}
				
				if(testingForm.isBrokenLinksResources()){
					suitBrokenReportsXref2.setSuitId(suit.getSuitId());
					suitBrokenReportsXref2.setReportName("BrokenLinkResources");
					suitBrokenReportsXrefList.add(suitBrokenReportsXref2);
				}
				
				if(testingForm.isLoadAttributes()){
					suitBrokenReportsXref3.setSuitId(suit.getSuitId());
					suitBrokenReportsXref3.setReportName("LoadTimeAttributes");
					suitBrokenReportsXrefList.add(suitBrokenReportsXref3);
				}
				
				if(!suitBrokenReportsXrefList.isEmpty()){
					this.uiTestingService.saveSuitBrokenReportsXref(suitBrokenReportsXrefList);	
				}
				
				
				redirAttr.addFlashAttribute("success",
						"Broken Testing Suit saved successfully");
			} else {
				redirAttr.addFlashAttribute("error",
						"Error while saving Broken Testing Suit");
			}

		} catch (DataIntegrityViolationException es){
			redirAttr
			.addFlashAttribute("error",
					"Duplicate Suit Name. Please try with different Suit name.");
		}catch (Exception exception) {
			logger.error("Exception :Unexpected error occured while Saving Broken Testing Suit");
			logger.error("Stack Trace :"
					+ ExceptionUtils.getStackTrace(exception));
			redirAttr
					.addFlashAttribute("error",
							"Unexpected error occured while Saving Broken Testing Suit");
		}

		logger.debug("Exit: SaveBrokenTesting");
		return "redirect:/BrokenLinkCheck.ftl";
	}
	
	@RequestMapping(value = "/UpdateBrokenLinkTesting", method = RequestMethod.POST)
	public String updateBrokenTesting(RedirectAttributes redirAttr,
			Model model, @ModelAttribute TestingForm testingForm) {

		logger.debug("Entry: UpdateBrokenTesting, testingForm->"+ testingForm);
		Suit suit = new Suit();
		SuitBrokenReportsXref suitBrokenReportsXref1=new SuitBrokenReportsXref();
		SuitBrokenReportsXref suitBrokenReportsXref2=new SuitBrokenReportsXref();
		SuitBrokenReportsXref suitBrokenReportsXref3=new SuitBrokenReportsXref();
		ArrayList<SuitBrokenReportsXref> suitBrokenReportsXrefList = new ArrayList<SuitBrokenReportsXref>();
		try {
			suit = homeService.getSavedSuits(testingForm.getSuitId());
			
			if(suit.getSuitId()>0){
				suit.setSuitId(testingForm.getSuitId());
				suit.setApplicationId(testingForm.getApplicationId());
				suit.setEnvironmentCategoryId(testingForm.getEnvironmentCategoryId());
				suit.setBrowserTypeId(testingForm.getBrowserType());
				suit.setSuitName(testingForm.getSuitName());
				suit.setLoginId(testingForm.getLoginId());
				suit.setBrokenUrlReport(testingForm.isProcessUrl());
				suit.setModuleId(getAsString(testingForm.getModules()));
				suit.setSolutionTypeId(1);
				suit.setPrivateSuit(testingForm.isPrivateSuit());
				suit.setType("Broken");
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
				this.uiTestingService.deleteSuitBrokenReportsXref(testingForm.getSuitId());
				if(testingForm.isBrokenLinks()){
					suitBrokenReportsXref1.setSuitId(suit.getSuitId());
					suitBrokenReportsXref1.setReportName("BrokenLink");
					suitBrokenReportsXrefList.add(suitBrokenReportsXref1);
				}
				
				if(testingForm.isBrokenLinksResources()){
					suitBrokenReportsXref2.setSuitId(suit.getSuitId());
					suitBrokenReportsXref2.setReportName("BrokenLinkResources");
					suitBrokenReportsXrefList.add(suitBrokenReportsXref2);
				}
				
				if(testingForm.isLoadAttributes()){
					suitBrokenReportsXref3.setSuitId(suit.getSuitId());
					suitBrokenReportsXref3.setReportName("LoadTimeAttributes");
					suitBrokenReportsXrefList.add(suitBrokenReportsXref3);
				}
				
				if(!suitBrokenReportsXrefList.isEmpty()){
					this.uiTestingService.saveSuitBrokenReportsXref(suitBrokenReportsXrefList);	
				}
				
				redirAttr.addFlashAttribute("success",
						"Broken Testing Suit updated successfully");
			} else {
				redirAttr.addFlashAttribute("error",
						"Error while updating Broken Testing Suit");
			}
			
			

		} catch (DataIntegrityViolationException es){
			redirAttr
			.addFlashAttribute("error",
					"Duplicate Suit Name. Please try with different Suit name.");
		}catch (Exception exception) {
			logger.error("Exception :Unexpected error occured while UpdateBrokenTesting");
			logger.error("Stack Trace :"
					+ ExceptionUtils.getStackTrace(exception));
			redirAttr
					.addFlashAttribute("error",
							"Unexpected error occured while UpdateBrokenTesting");
		}

		logger.debug("Exit: UpdateBrokenTesting");
		return "redirect:/BrokenLinkCheck.ftl";
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