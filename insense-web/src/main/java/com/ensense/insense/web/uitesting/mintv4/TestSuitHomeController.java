package com.ensense.insense.web.uitesting.mintv4;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cts.mint.common.service.HomeService;
import com.cts.mint.uitesting.model.TestingForm;
import com.cts.mint.uitesting.model.mintv4.TestSuitForm;
import com.cts.mint.uitesting.service.mintv4.TestSuitService;

@Controller
public class TestSuitHomeController {

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
	private HomeService homeService;
	
	@Autowired
	private TestSuitService suitService;
	
	private static Logger logger = Logger
			.getLogger(TestSuitHomeController.class);

	/**
	 * @param model
	 */
	@RequestMapping(value = { "TestSuitHome" }, method = RequestMethod.GET)
	public String testSuitHome(Model model) {
		logger.debug("Entry: TestSuitHome");
		TestingForm testingForm = null;
		if (null != model.asMap()
				&& model.asMap().containsKey("testingForm")) {
			testingForm = (TestingForm) model.asMap().get(
					"testingForm");
		} 
		try {
			model.addAttribute("browserTypeList",
					homeService.getBrowserTypes());
			if(testingForm != null) {
				
			}
			model.addAttribute("testingForm", testingForm);
		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while loading TestSuitHome home");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: TestSuitHome");
		return "mint4/TestSuitHome";
	}
	
	@RequestMapping(value = "/SaveSuit", method = RequestMethod.POST)
	public String SaveApplicationDetails(RedirectAttributes redirAttr, Model model, @ModelAttribute TestSuitForm testSuitForm) {
		logger.debug("Entry: SaveSuit");
		try {
			redirAttr.addFlashAttribute("browserTypeList",
					homeService.getBrowserTypes());
			if(testSuitForm != null) {
				if(suitService.saveSuit(testSuitForm)) {
					redirAttr.addFlashAttribute("Success","Suit Details Saved successfully");
				} else {
					redirAttr.addFlashAttribute("error","Error while saving Suit Details");
				}
			}
		} catch (Exception e) {
			redirAttr.addFlashAttribute("error","Unexpected error occured while Save Suit");
			logger.error("Exception :Unexpected error occured while Save Suit");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: SaveSuit");
		return "redirect:/TestSuitHome.ftl";
	}
	
}