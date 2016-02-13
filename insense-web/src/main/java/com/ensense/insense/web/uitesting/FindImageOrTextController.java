package com.ensense.insense.web.uitesting;

import com.ensense.insense.core.utils.Constants;
import com.ensense.insense.core.utils.UIFunctionalityType;
import com.ensense.insense.core.utils.UserServiceUtils;
import com.ensense.insense.data.common.entity.SuitGroupXref;
import com.ensense.insense.data.common.entity.Users;
import com.ensense.insense.data.uitesting.entity.Suit;
import com.ensense.insense.data.uitesting.entity.SuitTextImageXref;
import com.ensense.insense.services.common.HomeService;
import com.ensense.insense.services.uiadmin.*;
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
import com.ensense.insense.web.uiadmin.form.schedule.TestingForm;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Iterator;


@Controller
public class FindImageOrTextController {

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
			.getLogger(FindImageOrTextController.class);


	/**
	 * gets the basic data for Functionality testing page display
	 * 
	 * @param model
	 */
	@RequestMapping(value = { "/UiFindImageOrText" }, method = RequestMethod.GET)
	public String findImageOrTextGet(Model model) {

		logger.debug("Entry: findImageOrTextGet");
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
			logger.error("Exception :Unexpected error occured in findImageOrTextGet");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: FindImageOrText, uiTestingSetupForm->");
		
		return "uitesting/UiFindImageOrText";
	}
	
	@RequestMapping(value = { "/UiFindImageOrText" }, method = RequestMethod.POST)
	public String findImageOrTextPost(
			@ModelAttribute TestingForm testingForm, Model model,
			RedirectAttributes redirAttr) {
		logger.debug("Entry: FindImageOrText, findImageOrTextPost->"
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
		logger.debug("Exit: FindImageOrText, findImageOrTextPost->");
		redirAttr.addFlashAttribute("testingForm", testingForm);
		return "redirect:/UiFindImageOrText.ftl";
	}		
			
	
	@RequestMapping(value = "/SaveTextOrImageSuit", method = RequestMethod.POST)
	public String saveTextOrImageSuit(RedirectAttributes redirAttr,
			Model model, @ModelAttribute TestingForm testingForm) {

		logger.debug("Entry: SaveTextOrImageSuit, TextOrImageSuit->"+ testingForm);
		Suit suit = new Suit();
		try {
			suit.setSuitName(testingForm.getSuitName());
			suit.setPrivateSuit(testingForm.isPrivateSuit());
			
			suit.setApplicationId(testingForm.getApplicationId());
			suit.setEnvironmentCategoryId(testingForm.getEnvironmentCategoryId());
			suit.setLoginId(testingForm.getLoginId());
			
			//Default values
			suit.setBrokenUrlReport(false);
			suit.setType("TextOrImage");
			suit.setFunctionalityTypeId(UIFunctionalityType.FIND_TEXT_IMAGE.getFunctionalityTypeId());
			suit.setBrowserTypeId(0);
			suit.setSolutionTypeId(1);
			
			Users currentMintUser = (Users)context.getSession().getAttribute("currentMintUser");
			suit.setUserId(currentMintUser.getUserId());
			
			if (this.uiTestingService.saveTextOrImageSuit(suit)) {
				this.saveSuitGroupXref(suit
						.getSuitId());
				
				if(this.saveFindTextOrImageList(suit.getSuitId(),testingForm.getTextName(),testingForm.isFindText(),
						testingForm.getImageName(),testingForm.isFindImage())){
					redirAttr.addFlashAttribute("success",
							"TextOrImageSuit saved successfully");	
				}else {
					redirAttr.addFlashAttribute("error",
							"Error while updating saveFindTextOrImageList");
				}
			} else {
				redirAttr.addFlashAttribute("error",
						"Error while saving TextOrImageSuit");
			}

		} catch (DataIntegrityViolationException es){
			redirAttr
			.addFlashAttribute("error",
					"Duplicate Suit Name. Please try with different Suit name.");
		}catch (Exception e) {
			logger.error("Exception :Unexpected error occured while Saving TextOrImageSuit");
			logger.error("Stack Trace :"
					+ ExceptionUtils.getStackTrace(e));
			redirAttr
					.addFlashAttribute("error",
							"Unexpected error occured while Saving TextOrImageSuit");
		}

		logger.debug("Exit: SaveTextOrImageSuit");
		return "redirect:/UiFindImageOrText.ftl";
	}

	@RequestMapping(value = "/UpdateTextOrImageSuit", method = RequestMethod.POST)
	public String updateTextOrImageSuit(RedirectAttributes redirAttr,
			Model model, @ModelAttribute TestingForm testingForm) {

		logger.debug("Entry: UpdateTextOrImageSuit, TextOrImageSuit->"+ testingForm);
		Suit suit = new Suit();
		try {
			suit = homeService.getSavedSuits(testingForm.getSuitId());
			
			if(suit.getSuitId()>0){
				suit.setSuitId(testingForm.getSuitId());
				
				suit.setSuitName(testingForm.getSuitName());
				suit.setPrivateSuit(testingForm.isPrivateSuit());
				
				suit.setApplicationId(testingForm.getApplicationId());
				suit.setEnvironmentCategoryId(testingForm.getEnvironmentCategoryId());
				suit.setLoginId(testingForm.getLoginId());

				//Default values
				suit.setBrokenUrlReport(false);
				suit.setType("TextOrImage");
				suit.setBrowserTypeId(testingForm.getBrowserType());
				suit.setSolutionTypeId(1);
			}
			
			Users currentMintUser = (Users)context.getSession().getAttribute("currentMintUser");
			suit.setUserId(currentMintUser.getUserId());
			
			if (this.uiTestingService.saveTextOrImageSuit(suit)) {
				if (uiTestingService.deleteSuitTextImageXref(suit.getSuitId())){
					
					if(this.saveFindTextOrImageList(suit.getSuitId(),testingForm.getTextName(),testingForm.isFindText(),
							testingForm.getImageName(),testingForm.isFindImage())){
						redirAttr.addFlashAttribute("success",
								"TextOrImageSuit saved successfully");	
					}else {
						redirAttr.addFlashAttribute("error",
								"Error while updating saveFindTextOrImageList");
					}
				}else {
					redirAttr.addFlashAttribute("error",
							"Error while deleting deleteSuitTextImageXref");
				}
			} else {
				redirAttr.addFlashAttribute("error",
						"Error while updating TextOrImageSuit");
			}
			
			

		} catch (DataIntegrityViolationException es){
			redirAttr
			.addFlashAttribute("error",
					"Duplicate Suit Name. Please try with different Suit name.");
		}catch (Exception exception) {
			logger.error("Exception :Unexpected error occured while UpdateTextOrImageSuit");
			logger.error("Stack Trace :"
					+ ExceptionUtils.getStackTrace(exception));
			redirAttr
					.addFlashAttribute("error",
							"Unexpected error occured while UpdateTextOrImageSuit");
		}

		logger.debug("Exit: UpdateTextOrImageSuit");
		return "redirect:/UiFindImageOrText.ftl";
	}

	private boolean saveTextImageXref(int suitId,Boolean isText,Boolean isImage,String textOrImageName)  throws Exception{
		boolean saved = false;
		SuitTextImageXref suitTextImageXref = new SuitTextImageXref();
		suitTextImageXref.setText(isText);
		suitTextImageXref.setImage(isImage);
		suitTextImageXref.setSuitId(suitId);
		suitTextImageXref.setTextOrImageName(textOrImageName);
		
		saved = uiTestingService
				.saveSuitTextImageXref(suitTextImageXref);
		return saved;
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
	
	private ArrayList<String> getFindTextOrImageList(StringBuffer tempTextOrImageBuffer)  throws Exception{
		logger.debug("Entry: getFindTextOrImageList");
		int tempTextCount = 0;
		StringBuffer textOrImageBuffer=new StringBuffer();
		ArrayList<String> findTextOrImageList = new ArrayList<String>();
		try{
			for(String tempTextOrImageValue:tempTextOrImageBuffer.toString().split(",")){  
				tempTextCount = tempTextCount + tempTextOrImageValue.length()+1;
				if(tempTextCount <=250){
					textOrImageBuffer = textOrImageBuffer.append(tempTextOrImageValue).append(",");
				}else{
					findTextOrImageList.add(textOrImageBuffer.toString());
					tempTextCount =0;
					tempTextCount = tempTextOrImageValue.length()+1;
					textOrImageBuffer.setLength(0);
					textOrImageBuffer.append(tempTextOrImageValue).append(",");
				}
			}  
			if(textOrImageBuffer.length() !=0){
				textOrImageBuffer.deleteCharAt(textOrImageBuffer.length()-1);
				findTextOrImageList.add(textOrImageBuffer.toString());
				tempTextCount =0;
				textOrImageBuffer.setLength(0);
			}
		}catch(Exception e){
			logger.error("Exception :Unexpected error occured while getting getFindTextList"+ e);
		}
		logger.debug("Entry: getFindTextOrImageList");
		return findTextOrImageList;
	}
	
	private boolean saveFindTextOrImageList(int suitId, String[] textName, boolean istext, String[] imageName,boolean isImage)  throws Exception{
		logger.debug("Entry: saveFindTextOrImageList");
		StringBuffer tempTextBuffer=new StringBuffer();
		StringBuffer tempImageBuffer=new StringBuffer();
		ArrayList<String> findTextList = new ArrayList<String>();
		ArrayList<String> findImageList = new ArrayList<String>();
		boolean isSaved = false;
		try{
			//Consolidated and ArrayList generation for findText values
			if(textName!=null && istext){
				for (int index =0; index < textName.length; index++) {
					tempTextBuffer = tempTextBuffer.append(textName[index]).append(",");
				}	
				
				tempTextBuffer.deleteCharAt(tempTextBuffer.lastIndexOf(","));
				findTextList = this.getFindTextOrImageList(tempTextBuffer);
				
				if(!findTextList.isEmpty()){
					Iterator<String> itr=findTextList.iterator();  
					while(itr.hasNext()){  
						this.saveTextImageXref(suitId,istext,false,itr.next());
					}	
				}
				
			}
			
			//Consolidated and ArrayList generation for findImage values
			if(imageName!=null && isImage){
				for (int index =0; index < imageName.length; index++) {
					tempImageBuffer = tempImageBuffer.append(imageName[index]).append(",");
				}	
				
				tempImageBuffer.deleteCharAt(tempImageBuffer.lastIndexOf(","));
				findImageList = this.getFindTextOrImageList(tempImageBuffer);
				
				if(!findImageList.isEmpty()){
					Iterator<String> itr=findImageList.iterator();  
					while(itr.hasNext()){  
						this.saveTextImageXref(suitId,false,isImage,itr.next());
					}	
				}
				
			}
			isSaved = true;
		}catch(Exception e){
			logger.error("Exception :Unexpected error occured while getting saveFindTextOrImageList"+ e);
			isSaved = false;
		}
		logger.debug("Exit: saveFindTextOrImageList");
		return isSaved;
	}
}