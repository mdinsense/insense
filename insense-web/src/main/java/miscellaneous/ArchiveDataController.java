package miscellaneous;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cts.mint.common.UsageReportsController;
import com.cts.mint.common.model.UsageReportForm;
import com.cts.mint.common.service.HomeService;
import com.cts.mint.common.service.UserService;
import com.cts.mint.common.utils.Constants.ARCHIVEDATA;
import com.cts.mint.common.utils.FileDirectoryUtil;
import com.cts.mint.common.utils.UIFunctionalityType;
import com.cts.mint.common.utils.Constants.MiscellaneousTools;
import com.cts.mint.common.utils.Constants.SolutionType;
import com.cts.mint.common.utils.Constants.USAGEREPORTS;
import com.cts.mint.common.utils.Constants.UsageReportConstants;
import com.cts.mint.crawler.ClearCacheTestCaseExecutor;
import com.cts.mint.miscellaneous.entity.ClearCache;
import com.cts.mint.miscellaneous.entity.ClearCacheExecutionStatus;
import com.cts.mint.reports.service.ScheduledService;
import com.cts.mint.uitesting.service.EnvironmentService;

@Controller
public class ArchiveDataController {

	private static Logger logger = Logger
	.getLogger(ArchiveDataController.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private EnvironmentService environmentService;
	
	@Autowired
	private HomeService homeService;
	
	@Autowired
	ScheduledService scheduledService;
	
	@RequestMapping(value = { "/ArchiveData" }, method = RequestMethod.GET)
	public String archiveDataGet(Model model) {

		logger.debug("Entry: archiveDataGet");
		UsageReportForm reportForm = null;
		if (null != model.asMap() && model.asMap().containsKey("reportForm")) {
			reportForm = (UsageReportForm) model.asMap().get("reportForm");
		} else {
			reportForm = new UsageReportForm();
			reportForm.setTab(getUiSetupTabNumber(model.asMap()));
		}
		model.addAttribute("reportForm", reportForm);
		try {
			
			model.addAttribute("usersDetails",
					userService.getAllUsersDetails());
			model.addAttribute("groupsDetails",
					userService.getAllGroupsDetails());
			model.addAttribute("functionalities",
					UIFunctionalityType.values());
			model.addAttribute("solutionTypes",
					SolutionType.values());
			model.addAttribute("environmentList",
					environmentService.getEnvironmentCategoryList());
			model.addAttribute("key", "");

		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while loading Application home");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: archiveDataGet ");
		return ARCHIVEDATA.VIEW;
	}
	
	

	@RequestMapping(value = { "/ArchiveData" }, method = RequestMethod.POST)
	public String archiveDataPost(@ModelAttribute UsageReportForm reportForm,
			Model model, RedirectAttributes redirAttr) {
		logger.debug("Entry: archiveDataPost");
		try {
					redirAttr.addFlashAttribute("usersDetails",
							userService.getAllUsersDetails());
					redirAttr.addFlashAttribute("groupsDetails",
							userService.getAllGroupsDetails());
					redirAttr.addFlashAttribute("functionalities",
							UIFunctionalityType.values());
					redirAttr.addFlashAttribute("solutionTypes",
							SolutionType.values());
					redirAttr.addFlashAttribute("environmentList",
							environmentService.getEnvironmentCategoryList());
					redirAttr.addFlashAttribute("key", "");

		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while loading Application home");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		redirAttr.addFlashAttribute("reportForm", reportForm);
		logger.debug("Exit: archiveDataPost");
	
		return ARCHIVEDATA.REDIRECT_VIEW;
	}

	private int getUiSetupTabNumber(Map<?, ?> map) {
		int setUpTabNumber = 1;

		if (map.containsKey("tab")) {
			UsageReportForm usageReportForm = (UsageReportForm) map
					.get("tab");
			setUpTabNumber = usageReportForm.getTab();
		}

		return setUpTabNumber;
	}
	
	@RequestMapping(value = { "/ArchiveGetReportList" }, method = RequestMethod.POST)
	public String getReportListForArchive(@ModelAttribute UsageReportForm form,
			Model model, RedirectAttributes redirAttr) {
		logger.debug("Entry: getReportListForArchive");
		try {
			redirAttr.addFlashAttribute("archiveDataList",
						scheduledService.getScheduleDetails(form));
			redirAttr.addFlashAttribute("key", form.getKey());
		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while loading Application home");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		redirAttr.addFlashAttribute("reportForm", form);
		redirAttr.addFlashAttribute("searchResult", true);
		logger.debug("Entry: getReportListForArchive");
		return ARCHIVEDATA.REDIRECT_VIEW;
	}
	
	

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/ArchiveSelectedSchedules", method = RequestMethod.POST)
	@ResponseBody
	public String archiveSelectedSchedules(@RequestParam(value = "schedules") String schedules) {
		logger.debug("Entry: archiveSelectedSchedules");
		String message = "";
		List<String> schedulesToRun = null;
		boolean isAllJobsSuccess = true;
		JSONObject obj = new JSONObject();
		StringWriter out = new StringWriter();
		
		try {
			if (schedules != null) {
			//	schedulesToRun = Arrays.asList(schedules.split(","));
			
				List<String> directoriesToBeDeleted = scheduledService.deleteScheduleExecutions(schedules);
				if (directoriesToBeDeleted.size() > 0) {
					FileDirectoryUtil.deleteDirectoryList(directoriesToBeDeleted);
					message = "Selected data has been archived successfully";
					obj.put("success", message);
					
				} else {
					message = "Data archive has been failed to complete.";
					obj.put("error", message);
				}
			}
			obj.writeJSONString(out);
		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while running Clear Cache Details");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
			message = "Unexpected error occured while executing clear cache testcases";
		}

		logger.debug("Exit: archiveSelectedSchedules");
		return out.toString();
	}
	
}
