package miscellaneous;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cts.mint.common.service.MenuService;
import com.cts.mint.common.utils.CommonUtils;
import com.cts.mint.common.utils.Constants.MiscellaneousTools;
import com.cts.mint.common.utils.FileDirectoryUtil;
import com.cts.mint.common.utils.UserServiceUtils;
import com.cts.mint.crawler.ClearCacheTestCaseExecutor;
import com.cts.mint.miscellaneous.entity.ClearCache;
import com.cts.mint.miscellaneous.entity.ClearCacheExecutionStatus;
import com.cts.mint.miscellaneous.entity.MiscellaneousTool;
import com.cts.mint.miscellaneous.model.MiscellaneousToolForm;
import com.cts.mint.miscellaneous.service.MiscellaneousToolsService;
import com.cts.mint.uitesting.service.UiTestingService;
import com.cts.mint.util.TestCaseFileValidateUtil;

@Controller
public class MiscellaneousToolsController {

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
	private MenuService menuService;

	@Autowired
	private UiTestingService uiTestingService;

	@Autowired
	private MiscellaneousToolsService miscellaneousToolsService;

	
	
	private static Logger logger = Logger
			.getLogger(MiscellaneousToolsController.class);

	/**
	 * gets the basic data for Functionality testing page display
	 * 
	 * @param model
	 */
	@RequestMapping(value = { "/MiscellaneousToolsHome" }, method = RequestMethod.GET)
	public String miscellaneousToolsHome(Model model) {
		logger.debug("Entry: MiscellaneousToolsHome");
		try {
			List<MiscellaneousTool> miscellaneousToolsList = menuService.getToolsList();
			List<MiscellaneousTool> miscellaneousToolsListForGroup = menuService.getToolsListForGroup(UserServiceUtils.getCurrentUserGroupIdFromRequest(context));
			this.setPermissionForMiscellaneousTools(miscellaneousToolsList,miscellaneousToolsListForGroup);
			model.addAttribute("toolsList", miscellaneousToolsList);
		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while loading Application home");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: MiscellaneousToolsHome");
		return MiscellaneousTools.HOME;
	}

	@RequestMapping(value = "/ClearCacheHome", method = RequestMethod.GET)
	public String clearCacheHomeGet(Model model) {
		logger.debug("Entry: ClearCacheHomeGet");
		try {
			model.addAttribute("clearCacheList",
					miscellaneousToolsService.getClearCacheExecutionDataList());
		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while loading Application home");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: ClearCacheHomeGet");
		return MiscellaneousTools.CLEAR_CACHE_URL;
	}
	
	@RequestMapping(value = "/ClearCacheHomePost", method = RequestMethod.POST)
	public String clearCacheHomePost(Model model) {
		logger.debug("Entry: ClearCacheHomePost");
		try {
			model.addAttribute("clearCacheList",
					miscellaneousToolsService.getClearCacheExecutionDataList());
		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while loading Application home");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: ClearCacheHomePost");
		return MiscellaneousTools.CLEAR_CACHE_URL;
	}

	@RequestMapping(value = "/SaveClearCacheDetails", method = RequestMethod.POST)
	public String saveClearCacheDetails(RedirectAttributes redirAttr,
			Model model,
			@ModelAttribute MiscellaneousToolForm miscellaneousToolForm) {
		logger.debug("Entry: SaveClearCacheDetails, miscellaneousToolForm->"
				+ miscellaneousToolForm);
		ClearCache clearCache = new ClearCache();
		ClearCacheExecutionStatus clearCacheExecutionStatus = new ClearCacheExecutionStatus();
		try {

			clearCache.setApplicationName(miscellaneousToolForm
					.getApplicationName());
			clearCache.setEnvironmentName(miscellaneousToolForm
					.getEnvironmentName());

			if (this.uploadTestCase(miscellaneousToolForm, redirAttr)) {
				clearCache.setScriptPath(miscellaneousToolForm.getScriptPath());
				if (miscellaneousToolsService.saveClearCacheDetails(clearCache)) {
					redirAttr.addFlashAttribute("Success",
							"Clear Cache Details Saved Successfully");
					clearCacheExecutionStatus.setClearCacheId(clearCache
							.getClearCacheId());
					clearCacheExecutionStatus
							.setExecutionStatus(MiscellaneousTools.STATUS_NEW);
					miscellaneousToolsService
							.saveClearCacheExecutionStatus(clearCacheExecutionStatus);
				} else {
					redirAttr.addFlashAttribute("error",
							"Error occured while saving Clear Cache Details");
				}
			} else {
				logger.info("Error while upload.");
				// TODO, handle error case.
			}

		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while saving Clear Cache Details");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}

		logger.debug("Exit: SaveClearCacheDetails");
		return MiscellaneousTools.CLEAR_CACHE_REDIRECT;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/RunClearCacheTestCases", method = RequestMethod.POST)
	@ResponseBody
	public String runClearCacheTestCases(@RequestParam(value = "clearCacheIdsToRun") String clearCacheIdsToRun) {
		logger.debug("Entry: runClearCacheTestCases");
		String message = "";
		List<String> cacheIdsToRun = null;
		ClearCacheExecutionStatus clearCacheExecutionStatus = null;
		ClearCacheTestCaseExecutor cacheJob = null;
		boolean isAllJobsSuccess = true;
		JSONObject obj = new JSONObject();
		StringWriter out = new StringWriter();
		
		try {
			if (clearCacheIdsToRun != null) {
				cacheIdsToRun = Arrays.asList(clearCacheIdsToRun.split(","));
				List<ClearCache> clearCacheList = miscellaneousToolsService
						.getClearCacheDetails();
				for (ClearCache clearCache : clearCacheList) {
					if (cacheIdsToRun.contains(String.valueOf(clearCache.getClearCacheId()))) {
						clearCacheExecutionStatus = new ClearCacheExecutionStatus();
						clearCacheExecutionStatus.setClearCacheId(clearCache
								.getClearCacheId());
						clearCacheExecutionStatus
								.setExecutionStatus(MiscellaneousTools.STATUS_RUNNING);
						clearCacheExecutionStatus.setRunDate(new Date());
						miscellaneousToolsService
								.saveClearCacheExecutionStatus(clearCacheExecutionStatus);
						cacheJob = new ClearCacheTestCaseExecutor(messageSource);
						if (cacheJob.executeCacheClearJoB(clearCache,
								configProperties, messageSource)) {
							clearCacheExecutionStatus
									.setExecutionStatus(MiscellaneousTools.STATUS_COMPLETED);

						} else {
							clearCacheExecutionStatus
									.setExecutionStatus(MiscellaneousTools.STATUS_FAILED);
							isAllJobsSuccess = false;
						}
						miscellaneousToolsService
								.saveClearCacheExecutionStatus(clearCacheExecutionStatus);
					}
				}
				if (isAllJobsSuccess) {
					message = "All Submitted Tasks completed successfully";
					obj.put("Success", message);
				} else {
					message = "Submitted Tasks Failed to complete.";
					obj.put("error", message);
				}

			}
			obj.writeJSONString(out);

		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while running Clear Cache Details");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
			message = "Unexpected error occured while executing clear cache testcases";
		}

		logger.debug("Exit: runClearCacheTestCases");
		return out.toString();
	}
	
	@RequestMapping(value = "/DeleteClearCacheDetails", method = RequestMethod.POST)
	public String deleteClearCacheDetails(RedirectAttributes redirAttr,
			Model model,
			@ModelAttribute MiscellaneousToolForm miscellaneousToolForm) {
		logger.debug("Entry: deleteClearCacheDetails, miscellaneousToolForm->"
				+ miscellaneousToolForm);
		try {
			if(miscellaneousToolsService.deleteClearCacheDetails(miscellaneousToolForm.getClearCacheId())) {
				redirAttr.addFlashAttribute("Success",
						"Clear Cache Details have been deleted successfully");
			} else {
				redirAttr.addFlashAttribute("error",
						"Error occured while deleting Clear Cache Details");
			}
		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while deleting Clear Cache Details");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}

		logger.debug("Exit: deleteClearCacheDetails");
		return MiscellaneousTools.CLEAR_CACHE_REDIRECT;
	}
	
  
	private boolean uploadTestCase(MiscellaneousToolForm miscellaneousToolForm,
			RedirectAttributes reditAttr) {

		String environment = "";
		String application = "";
		String status = "";
		String fileName = "";
		String packageDirectory = "";
		String packageName = "";
		String jarLocation = "";
		String fileExtension = "";
		String destinationFileName = "";
		File destinationFile;
		boolean uploaded = false;
		try {
			application = miscellaneousToolForm.getApplicationName();
			environment = miscellaneousToolForm.getEnvironmentName();

			File newFile = new File(miscellaneousToolForm.getScriptFile()
					.getOriginalFilename());
			fileName = newFile.getName();
			packageDirectory = CommonUtils.getPropertyFromPropertyFile(configProperties, "mint.testcase.packageDirectory");
			packageDirectory = FileDirectoryUtil.getAbsolutePath(
					packageDirectory,
					FileDirectoryUtil.getMintRootDirectory(configProperties));
			packageName = CommonUtils.getPropertyFromPropertyFile(configProperties, "mint.testcase.packageName");
			jarLocation = CommonUtils.getPropertyFromPropertyFile(configProperties, "mint.testcase.jarLocation");
			jarLocation = FileDirectoryUtil.getAbsolutePath(jarLocation,
					FileDirectoryUtil.getMintRootDirectory(configProperties));

			application = application.trim().replaceAll(" ", "");
			environment = environment.trim().replaceAll(" ", "");
			destinationFileName = packageDirectory + "\\" + application + "\\"
					+ environment + "\\" + fileName;
			FileDirectoryUtil.createDirectories(packageDirectory + "\\"
					+ application + "\\" + environment);
			destinationFile = new File(destinationFileName);
			fileExtension = FilenameUtils.getExtension(newFile.getName());

			if (fileExtension.equals("java")) {
				if (!createDestinationFile(miscellaneousToolForm,
						destinationFileName)) {
					reditAttr.addFlashAttribute("error",
							"Unabel to save the uploaded file.");
					return false;
				}
				status = new TestCaseFileValidateUtil().validateFile(newFile,
						destinationFile, application, environment, packageName,
						jarLocation);
				if (status.equals("success")) {
					miscellaneousToolForm.setScriptPath(destinationFileName);
					uploaded = true;
				} else {
					reditAttr.addFlashAttribute("error",
							"Compilation errors while compiling the file.");
					uploaded = false;
				}
			} else {
				uploaded = false;
				reditAttr.addFlashAttribute("NotValidFormat",
						"Invalid File Format.Please Upload java file");
			}
		} catch (Exception e) {
			logger.error("Error: while Saving TestCaseDetails");
		}
		logger.info("Exit: uploadTestCase");
		return uploaded;
	}

	private boolean createDestinationFile(
			MiscellaneousToolForm miscellaneousToolForm, String fileName) {
		InputStream inputStream = null;
		OutputStream outputStream = null;
		MultipartFile file;
		try {
			file = miscellaneousToolForm.getScriptFile();

			if (file.getSize() > 0) {
				inputStream = file.getInputStream();
			}
			outputStream = new FileOutputStream(fileName);
			int readBytes = 0;
			byte[] buffer = new byte[10000];
			while ((readBytes = inputStream.read(buffer, 0, 10000)) != -1) {
				outputStream.write(buffer, 0, readBytes);
			}
		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while creating destination file");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
			return false;
		} finally {
			try {
				outputStream.close();
				inputStream.close();
			} catch (IOException e) {
				logger.error("Exception :Unexpected error occured while uploading login user script");
				logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
			}
		}
		return true;
	}
	
	private void setPermissionForMiscellaneousTools(
			List<MiscellaneousTool> miscellaneousToolsList,
			List<MiscellaneousTool> miscellaneousToolsListForGroup) {
		for(MiscellaneousTool alltool : miscellaneousToolsList)	{
			for(MiscellaneousTool toolHasAccess:miscellaneousToolsListForGroup) {
				if(alltool.getToolId() == toolHasAccess.getToolId()) {
					alltool.setHasAccess(true);
				}
			}
		}
	}

}