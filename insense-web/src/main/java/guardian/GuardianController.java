package guardian;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

import com.cts.mint.common.utils.Constants.GUARDIAN;
import com.cts.mint.common.utils.Constants.UiTesting;
import com.cts.mint.guardian.entity.Monitor;
import com.cts.mint.guardian.entity.Signature;
import com.cts.mint.guardian.model.GuardianForm;
import com.cts.mint.guardian.model.GuardianReport;

@Controller
public class GuardianController {

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
	private static Logger logger = Logger.getLogger(GuardianController.class);

	/* Application section begin here */

	@RequestMapping(value = { "/GuardianHome" }, method = RequestMethod.GET)
	public String guardianHomeGet(Model model) {

		logger.debug("Entry: guardianHomeGet, GuardianForm->"
				+ model.asMap().get("guardianForm"));

		GuardianForm guardianForm = null;

		if (null != model.asMap() && model.asMap().containsKey("guardianForm")) {
			guardianForm = (GuardianForm) model.asMap().get("guardianForm");
		} else {
			guardianForm = new GuardianForm();
			guardianForm.setTabNumber(getUiSetupTabNumber(model.asMap()));
		}
		try {

			switch (guardianForm.getTabNumber()) {
			case 1:
				ArrayList<Signature> signatureList = new ArrayList<Signature>();
				Signature record1 = new Signature("1","ERROR","NULL POINTER EXCEPTION","","ADMIN","01-Aug-2015");
				Signature record2 = new Signature("2","ERROR","AIRTHMETIC EXCEPTION","","ADMIN","02-Aug-2015");
				Signature record3 = new Signature("3","ERROR","ARRAY INDEX OUT OF BOUND EXCEPTION","","MANNIN","02-Aug-2015");
				Signature record4 = new Signature("4","WARNING","Internal Server Error","","MANNIN","01-Aug-2015");
				Signature record5 = new Signature("5","FATAL","Temporarily Unavailable","","ADMIN","01-Aug-2015");
				signatureList.add(record1);
				signatureList.add(record2);
				signatureList.add(record3);
				signatureList.add(record4);
				signatureList.add(record5);
				
				model.addAttribute("signatureList", signatureList);
				if (!isApplicationSetupDetailsAvailable(model.asMap())) {
					/*
					 * model.addAttribute("applications",
					 * applicationService.getAllApplicationDetails());
					 */
				}
				break;
			case 2:
				ArrayList<Monitor> monitorList = new ArrayList<Monitor>();
				Monitor row1 = new Monitor("1","MyTC","PRODFIX","Internal Server Error","TIER-1","01-Aug-2015");
				Monitor row2 = new Monitor("2","MyTC","PROD","Internal Server Error","TIER-2","02-Aug-2015");
				Monitor row3 = new Monitor("3","IFA","PROD","Temporarily Unavailable","TIER-1","02-Aug-2015");
				Monitor row4 = new Monitor("4","PLANFOCUS","PROD","NULL POINTER EXCEPTION","TIER-2","01-Aug-2015");
				Monitor row5 = new Monitor("5","IFA","PRODFIX","Temporarily Unavailable","TIER-1","01-Aug-2015");
				monitorList.add(row1);
				monitorList.add(row2);
				monitorList.add(row3);
				monitorList.add(row4);
				monitorList.add(row5);
				
				model.addAttribute("monitorList", monitorList);
				break;
			case 3:
				List<GuardianReport> gnlist = new ArrayList<GuardianReport>();
				GuardianReport gn = new GuardianReport();
				gn.setReprotId(1);
				gn.setChannel("MyTC");
				gn.setApplication("Estatements");
				gn.setEnvironment("PROD");
				gnlist.add(gn);
				
				gn = new GuardianReport();
				gn.setReprotId(2);
				gn.setChannel("IFA");
				gn.setApplication("IFA");
				gn.setEnvironment("PROD");
				gnlist.add(gn);
				
				gn = new GuardianReport();
				gn.setReprotId(2);
				gn.setChannel("MyTC");
				gn.setApplication("ART");
				gn.setEnvironment("PROD");
				gnlist.add(gn);
				
				model.addAttribute("guardianReportList", gnlist);
				break;
			}
		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while loading Application home");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		model.addAttribute("guardianForm", guardianForm);
		logger.debug("Exit: guardianHomeGet, guardianForm->" + guardianForm);
		return GUARDIAN.VIEW;
	}

	@RequestMapping(value = { "/GuardianHome" }, method = RequestMethod.POST)
	public String guardianHome(@ModelAttribute GuardianForm guardianForm,
			Model model, RedirectAttributes redirAttr) {
		logger.debug("Entry: uiTestingSetup, guardianForm->" + guardianForm);
		try {

			switch (guardianForm.getTabNumber()) {
			case 1:
				//redirAttr.addFlashAttribute("applications", "");
				ArrayList<Signature> signatureList = new ArrayList<Signature>();
				Signature record1 = new Signature("1","INFO","NULL POINTER EXCEPTION","","ADMIN","01-Aug-2015");
				Signature record2 = new Signature("2","ERROR","AIRTHMETIC EXCEPTION","","ADMIN","02-Aug-2015");
				Signature record3 = new Signature("3","ERROR","ARRAY INDEX OUT OF BOUND EXCEPTION","","MANNIN","02-Aug-2015");
				Signature record4 = new Signature("4","WARNING","Internal Server Error","","MANNIN","01-Aug-2015");
				Signature record5 = new Signature("5","FATAL","Temporarily Unavailable","","ADMIN","01-Aug-2015");
				signatureList.add(record1);
				signatureList.add(record2);
				signatureList.add(record3);
				signatureList.add(record4);
				signatureList.add(record5);
				
				
				redirAttr.addFlashAttribute("signatureList", signatureList);
				break;
			case 2:
				//redirAttr.addFlashAttribute("applications", "");
				ArrayList<Monitor> monitorList = new ArrayList<Monitor>();
				Monitor row1 = new Monitor("1","IWC","PRODFIX","Internal Server Error","TIER-1","01-Aug-2015");
				Monitor row2 = new Monitor("2","IFA","PROD","Internal Server Error","TIER-2","02-Aug-2015");
				Monitor row3 = new Monitor("3","IFA","PRODFIX","Temporarily Unavailable","TIER-1","02-Aug-2015");
				Monitor row4 = new Monitor("4","PLANFOCUS","PROD","NULL POINTER EXCEPTION","TIER-2","01-Aug-2015");
				Monitor row5 = new Monitor("5","IFA","PRODFIX","Temporarily Unavailable","TIER-1","01-Aug-2015");
				monitorList.add(row1);
				monitorList.add(row2);
				monitorList.add(row3);
				monitorList.add(row4);
				monitorList.add(row5);
				
				model.addAttribute("monitorList", monitorList);
				break;
			case 3:
				List<GuardianReport> gnlist = new ArrayList<GuardianReport>();
				GuardianReport gn = null;
				for(int i=1;i<6;i++) {
					 gn = new GuardianReport();
					 gn.setReprotId(i);
					 gn.setChannel("Channel-"+i);
					 gn.setApplication("Application-"+i);
					 gn.setEnvironment("Environment-"+i);
					 gnlist.add(gn);
				}
				redirAttr.addFlashAttribute("guardianReportList", gnlist);
				break;
			}

		} catch (Exception e) {
			logger.error("Exception in GuardianHome.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}

		logger.debug("Exit: GuardianHome");
		redirAttr.addFlashAttribute("guardianForm", guardianForm);
		return GUARDIAN.REDIRECT_VIEW;
	}

	@RequestMapping(value = "/SaveSignature", method = RequestMethod.POST)
	public String SaveSignature(RedirectAttributes redirAttr, Model model,
			@ModelAttribute GuardianForm guardianForm) {

		logger.debug("Entry: UpdateApplication");
		try {
			guardianForm.setTabNumber(UiTesting.UI_APPLICATION_SETUP);
			redirAttr.addFlashAttribute("guardianForm", guardianForm);
		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while deleting application");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}

		logger.debug("Exit: deleteApplication");
		return GUARDIAN.REDIRECT_VIEW;
	}

	@RequestMapping(value = "/UpdateSignature", method = RequestMethod.POST)
	public String UpdateSignature(RedirectAttributes redirAttr, Model model,
			@ModelAttribute GuardianForm guardianForm) {
		logger.debug("Entry: UpdateApplication");
		try {
			guardianForm.setTabNumber(UiTesting.UI_APPLICATION_SETUP);
			redirAttr.addFlashAttribute("guardianForm", guardianForm);
		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while deleting application");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}

		logger.debug("Exit: deleteApplication");
		return GUARDIAN.REDIRECT_VIEW;
	}

	@RequestMapping(value = "/EditSignature", method = RequestMethod.POST)
	public String EditSignature(RedirectAttributes redirAttr, Model model,
			@ModelAttribute GuardianForm guardianForm) {

		logger.debug("Entry: editApplicationDetails");

		logger.debug("Entry: deleteApplication");
		try {
			guardianForm.setTabNumber(UiTesting.UI_APPLICATION_SETUP);
			redirAttr.addFlashAttribute("guardianForm", guardianForm);
		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while deleting application");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}

		logger.debug("Exit: deleteApplication");
		return GUARDIAN.REDIRECT_VIEW;
	}

	@RequestMapping(value = "/DeleteSignature", method = RequestMethod.POST)
	public String DeleteSignature(RedirectAttributes redirAttr, Model model,
			@ModelAttribute GuardianForm guardianForm) {

		logger.debug("Entry: deleteApplication");
		try {
			guardianForm.setTabNumber(UiTesting.UI_APPLICATION_SETUP);
			redirAttr.addFlashAttribute("guardianForm", guardianForm);
		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while deleting application");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}

		logger.debug("Exit: deleteApplication");
		return GUARDIAN.REDIRECT_VIEW;

	}

	private boolean isApplicationSetupDetailsAvailable(Map<String, Object> asMap) {
		if (asMap.containsKey("applications")) {
			return true;
		}
		return false;
	}

	private int getUiSetupTabNumber(Map<?, ?> map) {
		int setUpTabNumber = 1;

		if (map.containsKey("tabNumber")) {
			GuardianForm guardianForm = (GuardianForm) map.get("tabNumber");
			setUpTabNumber = guardianForm.getTabNumber();
		}

		return setUpTabNumber;
	}

}