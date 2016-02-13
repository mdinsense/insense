package com.ensense.insense.web.uitesting;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cts.mint.common.MintFileUtils;
import com.cts.mint.common.entity.ApplicationGroupReference;
import com.cts.mint.common.entity.EnvironmentCategoryGroupXref;
import com.cts.mint.common.utils.CommonUtils;
import com.cts.mint.common.utils.Constants;
import com.cts.mint.common.utils.Constants.FILE;
import com.cts.mint.common.utils.Constants.KEYS;
import com.cts.mint.common.utils.Constants.ModuleType;
import com.cts.mint.common.utils.Constants.UiTesting;
import com.cts.mint.common.utils.EncrypDecryptUtil;
import com.cts.mint.common.utils.EncryptionUtil;
import com.cts.mint.common.utils.ExcelReaderUtil;
import com.cts.mint.common.utils.FileDirectoryUtil;
import com.cts.mint.common.utils.TestCaseFileValidationUtil;
import com.cts.mint.common.utils.UserServiceUtils;
import com.cts.mint.uitesting.entity.AnalyticsExcludeLink;
import com.cts.mint.uitesting.entity.AppEnvEnvironmentCategoryXref;
import com.cts.mint.uitesting.entity.Application;
import com.cts.mint.uitesting.entity.ApplicationConfig;
import com.cts.mint.uitesting.entity.ApplicationModuleXref;
import com.cts.mint.uitesting.entity.EnvEnvironmentCategoryXref;
import com.cts.mint.uitesting.entity.Environment;
import com.cts.mint.uitesting.entity.EnvironmentCategory;
import com.cts.mint.uitesting.entity.EnvironmentLoginScript;
import com.cts.mint.uitesting.entity.ExcludeUrl;
import com.cts.mint.uitesting.entity.HtmlReportsConfig;
import com.cts.mint.uitesting.entity.IncludeUrl;
import com.cts.mint.uitesting.entity.LoginUserDetails;
import com.cts.mint.uitesting.entity.TransactionTestCase;
import com.cts.mint.uitesting.model.ScriptDownloadForm;
import com.cts.mint.uitesting.model.UiTestingSetupForm;
import com.cts.mint.uitesting.service.ApplicationConfigService;
import com.cts.mint.uitesting.service.ApplicationEnvironmentXrefService;
import com.cts.mint.uitesting.service.ApplicationModuleService;
import com.cts.mint.uitesting.service.ApplicationService;
import com.cts.mint.uitesting.service.EnvironmentService;
import com.cts.mint.uitesting.service.ExcludeURLService;
import com.cts.mint.uitesting.service.HtmlReportConfigService;
import com.cts.mint.uitesting.service.IncludeURLService;
import com.cts.mint.uitesting.service.LoginUserService;

@Controller
public class UiTestingSetupController {

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
	ApplicationEnvironmentXrefService applicationEnvironmentXrefService;

	private static Logger logger = Logger
			.getLogger(UiTestingSetupController.class);

	/* Application section begin here */

	/**
	 * gets the basic data for Application page display
	 * 
	 * @param model
	 */
	@RequestMapping(value = { "/UiTestingSetup" }, method = RequestMethod.GET)
	public String UiTestingSetupGet(Model model) {

		logger.debug("Entry: UiTestingSetupGet, uiTestingSetupForm->"
				+ model.asMap().get("uiTestingSetupForm"));

		UiTestingSetupForm uiTestingSetupForm = null;

		if (null != model.asMap()
				&& model.asMap().containsKey("uiTestingSetupForm")) {
			uiTestingSetupForm = (UiTestingSetupForm) model.asMap().get(
					"uiTestingSetupForm");
		} else {
			uiTestingSetupForm = new UiTestingSetupForm();
			uiTestingSetupForm.setSetupTabNumber(getUiSetupTabNumber(model
					.asMap()));
		}
		try {

			switch (uiTestingSetupForm.getSetupTabNumber()) {

			case UiTesting.UI_APPLICATION_SETUP:
				if (!isApplicationSetupDetailsAvailable(model.asMap())) {
					model.addAttribute("applications",
							applicationService.getAllApplicationDetails());
				}
				break;

			case UiTesting.UI_ENVIRONMENT_SETUP:
				if (!isEnvironmentSetupDetailsAvailable(model.asMap())) {
					this.setUiEnvironmentDetailsToModel(model);
				}
				break;

			case UiTesting.UI_INCLUDE_EXCLUDE_URL_SETUP:
				if (!isIncludeExcludeUrlDetailsAvailable(model.asMap())) {
					this.setCommonTabDetailsToModel(uiTestingSetupForm, model);
					this.setUiIncludeExcludeDetailsToModel(model);
				}

				break;

			case UiTesting.UI_LOGIN_USER_SETUP:
				if (!isLoginUserSetupDetailsAvailable(model.asMap())) {
					this.setCommonTabDetailsToModel(uiTestingSetupForm, model);
					this.setUiLoginUserDetailsToModel(model);
				}
				break;

			case UiTesting.UI_APPLICATION_CONFIG_SETUP:
				if (!isApplicationConfigDetailsExists(model.asMap())) {
					this.setCommonTabDetailsToModel(uiTestingSetupForm, model);
					model.addAttribute("applicationConfigList",
							applicationConfigService.getApplicationConfigList());
				}
				break;

			case UiTesting.UI_HTML_REPORTS_CONFIG_SETUP:
				if (!isHtmlConfigDetailsExists(model.asMap())) {
					this.setCommonTabDetailsToModel(uiTestingSetupForm, model);
					model.addAttribute("htmlReportConfigList",
							htmlReportConfigService.getAllHtmlReportsConfig());
				}
				break;

			case UiTesting.UI_MODULE_SETUP:
				if (!isApplicationModuleDetailsAvailable(model.asMap())) {
					this.setCommonTabDetailsToModel(uiTestingSetupForm, model);
					this.setUiModuleDetailsToModel(model);
				}
				break;

			case UiTesting.UI_ANALYTICS_EXCLUDE_SETUP:
				if (!isAnalyticsExcludeSetupDetailsAvailable(model.asMap())) {
					this.setCommonTabDetailsToModel(uiTestingSetupForm, model);
					this.setAnalyticsExcludeDetailsToModel(model);
				}
				break;
			}

		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while loading Application home");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		model.addAttribute("uiTestingSetupForm", uiTestingSetupForm);
		logger.debug("Exit: UiTestingSetupGet, uiTestingSetupForm->"
				+ uiTestingSetupForm);
		return UiTesting.VIEW;
	}

	@RequestMapping(value = { "/UiTestingSetup" }, method = RequestMethod.POST)
	public String UiTestingSetupForm(
			@ModelAttribute UiTestingSetupForm uiTestingSetupForm, Model model,
			RedirectAttributes redirAttr) {
		logger.debug("Entry: uiTestingSetup, uiTestingSetupForm->"
				+ uiTestingSetupForm);
		try {

			switch (uiTestingSetupForm.getSetupTabNumber()) {

			case UiTesting.UI_APPLICATION_SETUP:
				this.setUiApplicationDetails(redirAttr);
				break;

			case UiTesting.UI_ENVIRONMENT_SETUP:
				uiTestingSetupForm.setSecureSite(false);
				this.setUiEnvironmentDetails(redirAttr);
				break;

			case UiTesting.UI_INCLUDE_EXCLUDE_URL_SETUP:
				this.setCommonTabDetailsToRedirectAttributes(
						uiTestingSetupForm, redirAttr);
				this.setUiIncludeExcludeDetails(redirAttr);
				break;

			case UiTesting.UI_LOGIN_USER_SETUP:
				this.setCommonTabDetailsToRedirectAttributes(
						uiTestingSetupForm, redirAttr);
				redirAttr.addFlashAttribute("loginList",
						loginUserService.getLoginUserList());
				logger.debug("loginList :"
						+ loginUserService.getLoginUserList());
				break;

			case UiTesting.UI_APPLICATION_CONFIG_SETUP:
				this.setCommonTabDetailsToRedirectAttributes(
						uiTestingSetupForm, redirAttr);
				redirAttr.addFlashAttribute("applicationConfigList",
						applicationConfigService.getApplicationConfigList());
				break;

			case UiTesting.UI_HTML_REPORTS_CONFIG_SETUP:
				this.setCommonTabDetailsToRedirectAttributes(
						uiTestingSetupForm, redirAttr);
				redirAttr.addFlashAttribute("htmlReportConfigList",
						htmlReportConfigService.getAllHtmlReportsConfig());
				break;

			case UiTesting.UI_MODULE_SETUP:
				this.setCommonTabDetailsToRedirectAttributes(
						uiTestingSetupForm, redirAttr);
				this.setUiModuleSetupDetails(redirAttr);
				break;

			case UiTesting.UI_ANALYTICS_EXCLUDE_SETUP:
				this.setCommonTabDetailsToRedirectAttributes(
						uiTestingSetupForm, redirAttr);
				this.setUiAnalyticsExcludeDetails(redirAttr);
				break;
			}

		} catch (Exception e) {
			logger.error("Exception in uiTestingSetup.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}

		logger.debug("Exit: uiTestingSetup");
		redirAttr.addFlashAttribute("uiTestingSetupForm", uiTestingSetupForm);
		return UiTesting.REDIRECT_VIEW;
	}

	/**
	 * Save new application details to application table
	 * 
	 * @param model
	 * @param applicationForm
	 */
	@RequestMapping(value = "/SaveApplication", method = RequestMethod.POST)
	public String SaveApplicationDetails(RedirectAttributes redirAttr,
			Model model, @ModelAttribute UiTestingSetupForm uiTestingSetupForm) {

		logger.debug("Entry: SaveApplication, uiTestingSetupForm->"
				+ uiTestingSetupForm);
		Application application = new Application();
		try {
			uiTestingSetupForm
					.setSetupTabNumber(UiTesting.UI_APPLICATION_SETUP);

			if (this.applicationService
					.isApplicationNameExists(uiTestingSetupForm
							.getApplicationName())) {
				redirAttr.addFlashAttribute("error",
						"Application details already exists");
				return UiTesting.REDIRECT_VIEW;
			}

			application.setApplicationDescription(uiTestingSetupForm
					.getApplicationDescription());

			application.setDateTimeCreated(new Date());
			application.setDateTimeModified(new Date());
			application.setApplicationName(uiTestingSetupForm
					.getApplicationName());

			if (this.applicationService.addApplication(application)) {
				updateUiTestingSetupFormForApplication(uiTestingSetupForm,
						application);
				// saving application group reference
				this.saveApplicationGroupReference(application
						.getApplicationId());

				redirAttr.addFlashAttribute("Success",
						"Application saved successfully");
				redirAttr.addFlashAttribute("uiTestingSetupForm",
						uiTestingSetupForm);

			} else {
				redirAttr.addFlashAttribute("error",
						"Error while saving application details");
			}

		} catch (Exception exception) {
			logger.error("Exception :Unexpected error occured while Saving Applicaiton details");
			logger.error("Stack Trace :"
					+ ExceptionUtils.getStackTrace(exception));
			redirAttr
					.addFlashAttribute("error",
							"Unexpected error occured while Saving Applicaiton details");
		}

		logger.debug("Exit: SaveApplication");
		return UiTesting.REDIRECT_VIEW;
	}

	/**
	 * update an existing application record in application table
	 * 
	 * @param model
	 * @param applicationForm
	 */
	@RequestMapping(value = "/UpdateApplication", method = RequestMethod.POST)
	public String UpdateApplicationDetails(RedirectAttributes redirAttr,
			Model model, @ModelAttribute UiTestingSetupForm uiTestingSetupForm) {
		logger.debug("Entry: UpdateApplication");

		Application application = new Application();
		try {
			uiTestingSetupForm
					.setSetupTabNumber(UiTesting.UI_APPLICATION_SETUP);
			redirAttr.addFlashAttribute("uiTestingSetupForm",
					uiTestingSetupForm);
			application = applicationService.getApplication(uiTestingSetupForm
					.getApplicationId());
			application.setApplicationId(uiTestingSetupForm.getApplicationId());
			application.setApplicationName(uiTestingSetupForm
					.getApplicationName());
			application.setDateTimeModified(new Date());
			application.setApplicationDescription(uiTestingSetupForm
					.getApplicationDescription());
			if (applicationService.addApplication(application)) {
				updateUiTestingSetupFormForApplication(uiTestingSetupForm,
						application);
				redirAttr.addFlashAttribute("Success",
						"Application updated successfully");
				redirAttr.addFlashAttribute("savedApplication", application);
			} else {
				redirAttr.addFlashAttribute("error",
						"Error occured while updating Application details");
			}
		} catch (DataIntegrityViolationException e) {
			logger.error("Exception :Duplicate Applicaiton details entered. Please check the values.");
			redirAttr
					.addFlashAttribute("error",
							"Duplicate Applicaiton details entered. Please check the values.");
		} catch (Exception exception) {
			logger.error("Exception :Unexpected error occured while updating Applicaiton details");
			logger.error("Stack Trace :"
					+ ExceptionUtils.getStackTrace(exception));
			redirAttr
					.addFlashAttribute("error",
							"Unexpected error occured while updating Applicaiton details");
		}

		logger.debug("Exit: UpdateApplication");
		return UiTesting.REDIRECT_VIEW;
	}

	/**
	 * gets the application record details for the selected application
	 * 
	 * @param model
	 * @param applicationForm
	 */
	@RequestMapping(value = "/EditApplicationDetails", method = RequestMethod.POST)
	public String editApplicationDetails(RedirectAttributes redirAttr,
			Model model, @ModelAttribute UiTestingSetupForm uiTestingSetupForm) {

		logger.debug("Entry: editApplicationDetails");

		Application application = new Application();
		try {
			uiTestingSetupForm
					.setSetupTabNumber(UiTesting.UI_APPLICATION_SETUP);

			if (uiTestingSetupForm.getApplicationId() != 0) {

				application.setApplicationId(uiTestingSetupForm
						.getApplicationId());
				application = applicationService.getApplication(application
						.getApplicationId());
				updateUiTestingSetupFormForApplication(uiTestingSetupForm,
						application);
				redirAttr.addFlashAttribute("uiTestingSetupForm",
						uiTestingSetupForm);
			} else {
				logger.error("Error while fetching application details: application id is blank");
			}

		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while loading Application home");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}

		logger.debug("Exit: editApplicationDetails");
		return UiTesting.REDIRECT_VIEW;
	}

	/**
	 * Deletes the application record from the application table
	 * 
	 * @param model
	 * @param applicationForm
	 */
	@RequestMapping(value = "/DeleteApplication", method = RequestMethod.POST)
	public String deleteApplication(RedirectAttributes redirAttr, Model model,
			@ModelAttribute UiTestingSetupForm uiTestingSetupForm) {

		logger.debug("Entry: deleteApplication");
		try {
			uiTestingSetupForm
					.setSetupTabNumber(UiTesting.UI_APPLICATION_SETUP);
			Application application = new Application();
			if (uiTestingSetupForm.getApplicationId() != 0) {
				application.setApplicationId(uiTestingSetupForm
						.getApplicationId());
				if (applicationService.deleteApplication(application)) {
					redirAttr.addFlashAttribute("Success",
							"Application setup details deleted successfully");
					uiTestingSetupForm = new UiTestingSetupForm();
					uiTestingSetupForm
							.setSetupTabNumber(UiTesting.UI_APPLICATION_SETUP);
					redirAttr.addFlashAttribute("uiTestingSetupForm",
							uiTestingSetupForm);
				} else {
					redirAttr
							.addAttribute("Error",
									"Error occured while deleting Application setup details");
				}
			} else {
				logger.error("Error occured while deleting application details: application id is blank");
			}

		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while deleting application");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}

		logger.debug("Exit: deleteApplication");
		return UiTesting.REDIRECT_VIEW;

	}

	/* Application section ends here */

	/* Environment section starts here */

	/**
	 * Save/setup environment
	 * 
	 * @param redirAttr
	 * @param model
	 * @param uiTestingSetupForm
	 */
	@RequestMapping(value = "/SaveEnvironment", method = RequestMethod.POST)
	public String saveEnvironment(RedirectAttributes redirAttr, Model model,
			@ModelAttribute UiTestingSetupForm uiTestingSetupForm) {

		logger.debug("Entry: SaveEnvironment, uiTestingSetupForm->"
				+ uiTestingSetupForm);
		Environment environment = new Environment();
		uiTestingSetupForm.setSetupTabNumber(UiTesting.UI_ENVIRONMENT_SETUP);
		redirAttr.addFlashAttribute("uiTestingSetupForm", uiTestingSetupForm);

		try {

			if (null != uiTestingSetupForm.getEnvironmentId()
					&& !environmentService
							.isMachesWithExistingEnvironmentAndEnvrionmentCategory(
									uiTestingSetupForm.getApplicationId(),
									uiTestingSetupForm.getEnvironmentId(),
									uiTestingSetupForm
											.getEnvironmentCategoryId())) {
				redirAttr
						.addFlashAttribute("error",
								"Duplicate Environment Category. Please delete the existing category and try.");
				return UiTesting.REDIRECT_VIEW;

			}

			// saving a new environment
			if (uiTestingSetupForm.getSaveEnvironment()) {

				// check whether environment name already exists
				if (environmentService.isEnvironmentExists(uiTestingSetupForm
						.getEnvironmentName())) {
					redirAttr.addFlashAttribute("error",
							"Environment Name already exists");
					return UiTesting.REDIRECT_VIEW;
				}

				environment.setEnvironmentName(uiTestingSetupForm
						.getEnvironmentName());
				environment.setLoginOrHomeUrl(uiTestingSetupForm
						.getLoginOrHomeUrl());
				environment.setSecureSite(uiTestingSetupForm.getSecureSite());
				environment.setDateTimeCreated(new Date());
				environment.setDateTimeModified(new Date());

				if (environmentService.addEnvironment(environment)) {
					// saving environment group reference
					this.saveEnvironmentGroupReference(uiTestingSetupForm
							.getEnvironmentCategoryId());
					updateUiTestingSetupFormForEnvironment(uiTestingSetupForm,
							environment);
					if (environment.getSecureSite()
							&& uiTestingSetupForm.getLoginScriptFile() != null) {
						this.uploadScript(environment, uiTestingSetupForm,
								redirAttr, false);
					}

					saveEnvironmentEnvironmentCategoryXref(uiTestingSetupForm);
				} else {
					redirAttr.addFlashAttribute("error",
							"Error while saving Environment details.");
					redirAttr.addFlashAttribute("uiTestingSetupForm",
							uiTestingSetupForm);
					return UiTesting.REDIRECT_VIEW;
				}

			}

			/* saving environmentCategoryXref */
			if (this.saveEnvironmentCategoryXref(uiTestingSetupForm)) {
				this.saveUitestingConfig(uiTestingSetupForm);
				redirAttr.addFlashAttribute("Success",
						"Environment and reference details saved successfully");
			} else {
				redirAttr.addFlashAttribute("error",
						"Error while saving Environment Category reference");
			}

		} catch (DataIntegrityViolationException e) {
			logger.error("Exception :Duplicate Environment details entered. Please check the values.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
			redirAttr
					.addFlashAttribute("error",
							"Duplicate Environment details entered. Please check the values.");
		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while saving Environment details");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
			redirAttr
					.addFlashAttribute("error",
							"Exception :Unexpected error occured while saving Environment details.");
		}
		logger.debug("Exit: SaveEnvironment");

		return UiTesting.REDIRECT_VIEW;
	}

	/**
	 * Updating environment details
	 * 
	 * @param redirAttr
	 * @param model
	 * @param uiTestingSetupForm
	 */
	@RequestMapping(value = "/UpdateEnvironment", method = RequestMethod.POST)
	public String updateEnvironment(RedirectAttributes redirAttr, Model model,
			@ModelAttribute UiTestingSetupForm uiTestingSetupForm) {
		logger.debug("Entry: UpdateEnvironment");

		Environment environment = new Environment();
		uiTestingSetupForm.setSetupTabNumber(UiTesting.UI_ENVIRONMENT_SETUP);

		try {

			environment = environmentService.getEnvironment(uiTestingSetupForm
					.getEnvironmentId());
			environment.setEnvironmentId(uiTestingSetupForm.getEnvironmentId());
			environment.setLoginOrHomeUrl(uiTestingSetupForm
					.getLoginOrHomeUrl());
			environment.setEnvironmentName(uiTestingSetupForm
					.getEnvironmentName());
			environment.setSecureSite(uiTestingSetupForm.getSecureSite());
			environment.setDateTimeModified(new Date());

			if (environmentService.addEnvironment(environment)) {
				updateUiTestingSetupFormForEnvironment(uiTestingSetupForm,
						environment);
				if (environment.getSecureSite()) {
					this.uploadScript(environment, uiTestingSetupForm,
							redirAttr, false);
				}
				redirAttr.addFlashAttribute("Success",
						"Environment details updated successfully");
			} else {
				redirAttr.addFlashAttribute("error",
						"Error while updating Environment details");
			}
		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while updating Environment details");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: UpdateEnvironment");
		redirAttr.addFlashAttribute("uiTestingSetupForm", uiTestingSetupForm);
		return UiTesting.REDIRECT_VIEW;
	}

	/**
	 * get environment details for editting
	 * 
	 * @param redirAttr
	 * @param model
	 * @param uiTestingSetupForm
	 */
	@RequestMapping(value = "/EditEnvironmentDetails", method = RequestMethod.POST)
	public String editEnvironmentDetails(RedirectAttributes redirAttr,
			Model model, @ModelAttribute UiTestingSetupForm uiTestingSetupForm) {

		logger.debug("Entry: EditEnvironmentDetails");
		Environment environment = new Environment();
		uiTestingSetupForm.setSetupTabNumber(UiTesting.UI_ENVIRONMENT_SETUP);

		try {
			environment = environmentService.getEnvironment(uiTestingSetupForm
					.getEnvironmentId());
			this.updateUiTestingSetupFormForEnvironment(uiTestingSetupForm,
					environment);
			uiTestingSetupForm.setIsEnvironmentEdit(1);
		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while get AppConfiguration details");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}

		logger.debug("Exit: EditEnvironmentDetails");
		redirAttr.addFlashAttribute("uiTestingSetupForm", uiTestingSetupForm);
		return UiTesting.REDIRECT_VIEW;

	}

	/**
	 * deleting the environment
	 * 
	 * @param redirAttr
	 * @param model
	 * @param uiTestingSetupForm
	 */
	@RequestMapping(value = "/DeleteEnvironment", method = RequestMethod.POST)
	public String deleteEnvironment(RedirectAttributes redirAttr, Model model,
			@ModelAttribute UiTestingSetupForm uiTestingSetupForm) {
		logger.debug("Entry: DeleteEnvironment, uiTestingSetupForm->"
				+ uiTestingSetupForm);
		uiTestingSetupForm.setSetupTabNumber(UiTesting.UI_ENVIRONMENT_SETUP);
		try {
			if (environmentService.deleteEnvironment(uiTestingSetupForm)) {
				redirAttr.addFlashAttribute("Success",
						"Environment details deleted successfully");
			} else {
				redirAttr.addFlashAttribute("error",
						"Error while deleting Environment details");
			}
		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while deleting Environment details");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: DeleteEnvironment");
		redirAttr.addFlashAttribute("uiTestingSetupForm", uiTestingSetupForm);
		return UiTesting.REDIRECT_VIEW;
	}

	/* Environment Section ends here */

	/* Application Config section begins here */

	/**
	 * Saving application configuration
	 * 
	 * @param redirAttr
	 * @param model
	 * @param uiTestingSetupForm
	 */
	@RequestMapping(value = "/SaveApplicationConfig", method = RequestMethod.POST)
	public String saveApplicationConfig(RedirectAttributes redirAttr,
			Model model, @ModelAttribute UiTestingSetupForm uiTestingSetupForm) {
		logger.debug("Entry: SaveApplicationConfig");
		ApplicationConfig applicationConfig;
		EnvironmentCategory environmentCategory;
		uiTestingSetupForm
				.setSetupTabNumber(UiTesting.UI_APPLICATION_CONFIG_SETUP);
		try {
			applicationConfig = applicationConfigService.getApplicationConfig(
					uiTestingSetupForm.getApplicationId(),
					uiTestingSetupForm.getEnvironmentId());
			if (applicationConfig != null
					&& applicationConfig.getConfigId() != null
					&& applicationConfig.getConfigId() > 0) {
				redirAttr
						.addFlashAttribute("error",
								"Application configuration already exists please use edit to change");
			} else {
				applicationConfig = new ApplicationConfig();
				applicationConfig.setApplicationId(uiTestingSetupForm
						.getApplicationId());
				applicationConfig.setEnvironmentId(uiTestingSetupForm
						.getEnvironmentId());
				applicationConfig.setBrowserRestart(uiTestingSetupForm
						.getBrowserRestart());
				applicationConfig.setNoOfBrowsers(uiTestingSetupForm
						.getNoOfBrowsers());
				applicationConfig.setBrowserTimeout(uiTestingSetupForm
						.getBrowserTimeout());
				if (applicationConfigService
						.saveApplicationConfig(applicationConfig)) {
					this.updateUiTestingSetupFormForApplicationConfig(
							uiTestingSetupForm, applicationConfig);
					redirAttr
							.addFlashAttribute("Success",
									"Successfully saved the application configuirations");
					environmentCategory = environmentService
							.getEnvironmentCategoryName(uiTestingSetupForm
									.getEnvironmentId());
					uiTestingSetupForm
							.setEnvironmentCategoryId(environmentCategory
									.getEnvironmentCategoryId());
				} else {
					redirAttr.addFlashAttribute("error",
							"Error while saving application configuirations");
				}
			}

		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while Saving Application config");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
			redirAttr.addFlashAttribute("error",
					"Unexpected error occured while Saving Application config");
		}
		logger.debug("Exit: SaveApplicationConfig");
		redirAttr.addFlashAttribute("uiTestingSetupForm", uiTestingSetupForm);
		return UiTesting.REDIRECT_VIEW;
	}

	/**
	 * Updating the applcation config record
	 * 
	 * @param redirAttr
	 * @param model
	 * @param uiTestingSetupForm
	 */
	@RequestMapping(value = "/UpdateApplicationConfig", method = RequestMethod.POST)
	public String updateApplicationConfig(RedirectAttributes redirAttr,
			Model model, @ModelAttribute UiTestingSetupForm uiTestingSetupForm) {
		logger.debug("Entry: UpdateApplicationConfig");
		ApplicationConfig applicationConfig = new ApplicationConfig();
		EnvironmentCategory environmentCategory;
		uiTestingSetupForm
				.setSetupTabNumber(UiTesting.UI_APPLICATION_CONFIG_SETUP);
		try {
			applicationConfig = applicationConfigService
					.getApplicationConfig(uiTestingSetupForm.getConfigId());
			applicationConfig.setApplicationId(uiTestingSetupForm
					.getApplicationId());
			applicationConfig.setEnvironmentId(uiTestingSetupForm
					.getEnvironmentId());
			applicationConfig.setBrowserRestart(uiTestingSetupForm
					.getBrowserRestart());
			applicationConfig.setNoOfBrowsers(uiTestingSetupForm
					.getNoOfBrowsers());
			applicationConfig.setBrowserTimeout(uiTestingSetupForm
					.getBrowserTimeout());
			if (applicationConfigService
					.saveApplicationConfig(applicationConfig)) {
				this.updateUiTestingSetupFormForApplicationConfig(
						uiTestingSetupForm, applicationConfig);
				redirAttr.addFlashAttribute("Success",
						"Successfully Updated the application configuirations");
				environmentCategory = environmentService
						.getEnvironmentCategoryName(uiTestingSetupForm
								.getEnvironmentId());
				uiTestingSetupForm.setEnvironmentCategoryId(environmentCategory
						.getEnvironmentCategoryId());
			} else {
				redirAttr.addFlashAttribute("error",
						"Error while updating application configuirations");
			}

		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while Saving Application config");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
			redirAttr.addFlashAttribute("error",
					"Unexpected error occured while Saving Application config");
		}
		logger.debug("Exit: UpdateApplicationConfig");
		redirAttr.addFlashAttribute("uiTestingSetupForm", uiTestingSetupForm);
		return UiTesting.REDIRECT_VIEW;
	}

	/**
	 * deleting the application config record
	 * 
	 * @param redirAttr
	 * @param model
	 * @param uiTestingSetupForm
	 */
	@RequestMapping(value = "/DeleteApplicationConfig", method = RequestMethod.POST)
	public String deleteApplicationConfig(RedirectAttributes redirAttr,
			Model model, @ModelAttribute UiTestingSetupForm uiTestingSetupForm) {
		logger.debug("Entry: deleteAppConfiguration");
		ApplicationConfig applicationConfig = new ApplicationConfig();
		EnvironmentCategory environmentCategory;
		uiTestingSetupForm
				.setSetupTabNumber(UiTesting.UI_APPLICATION_CONFIG_SETUP);
		try {
			applicationConfig.setConfigId(uiTestingSetupForm.getConfigId());
			if (applicationConfigService
					.deleteAppConfiguration(applicationConfig)) {
				redirAttr.addFlashAttribute("Success",
						"Application config details deleted successfully");
				environmentCategory = environmentService
						.getEnvironmentCategoryName(uiTestingSetupForm
								.getEnvironmentId());
				uiTestingSetupForm.setEnvironmentCategoryId(environmentCategory
						.getEnvironmentCategoryId());
			} else {
				redirAttr
						.addFlashAttribute("error",
								"Error occured while deleting application config details");
			}
		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while deleting Application config details");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: deleteAppConfiguration");
		redirAttr.addFlashAttribute("uiTestingSetupForm", uiTestingSetupForm);
		return UiTesting.REDIRECT_VIEW;
	}

	/**
	 * get the appliocation config record for editting
	 * 
	 * @param redirAttr
	 * @param model
	 * @param uiTestingSetupForm
	 */
	@RequestMapping(value = "/EditApplicationConfig", method = RequestMethod.POST)
	public String editApplicationConfig(RedirectAttributes redirAttr,
			Model model, @ModelAttribute UiTestingSetupForm uiTestingSetupForm) {
		logger.debug("Entry: EditApplicationConfig");
		ApplicationConfig applicationConfig;
		EnvironmentCategory environmentCategory;
		uiTestingSetupForm
				.setSetupTabNumber(UiTesting.UI_APPLICATION_CONFIG_SETUP);
		try {
			applicationConfig = applicationConfigService
					.getApplicationConfig(uiTestingSetupForm.getConfigId());
			if (applicationConfig != null) {
				this.updateUiTestingSetupFormForApplicationConfig(
						uiTestingSetupForm, applicationConfig);
				environmentCategory = environmentService
						.getEnvironmentCategoryName(uiTestingSetupForm
								.getEnvironmentId());
				uiTestingSetupForm.setEnvironmentCategoryId(environmentCategory
						.getEnvironmentCategoryId());
			}
		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while get AppConfiguration details");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: EditApplicationConfig");
		redirAttr.addFlashAttribute("uiTestingSetupForm", uiTestingSetupForm);
		return UiTesting.REDIRECT_VIEW;
	}

	/* Application Config section ends here */

	/* Include/Exclued section begins here */

	// Saving IncludeExcludeURL Changes--Start
	@RequestMapping(value = "/SaveIncludeExcludeURL", method = RequestMethod.POST)
	public String SaveIncludeExcludeURLDetails(RedirectAttributes redirAttr,
			Model model, @ModelAttribute UiTestingSetupForm uiTestingSetupForm) {

		logger.debug("Entry: SaveIncludeExcludeURLDetails, uiTestingSetupForm ->"
				+ uiTestingSetupForm);
		// String view = "redirect:/UiTestingSetupForm";
		// setting application id and environment id to model attribute
		// this.setAppEnvToModel(incExcUrlSetupForm, model);
		IncludeUrl include;
		ExcludeUrl exclude;

		try {
			uiTestingSetupForm
					.setSetupTabNumber(UiTesting.UI_INCLUDE_EXCLUDE_URL_SETUP);
			redirAttr.addFlashAttribute("uiTestingSetupForm",
					uiTestingSetupForm);
			// saving Include url details
			if (StringUtils.isNotBlank(uiTestingSetupForm.getIncludeUrl())) {
				include = new IncludeUrl();

				if (this.isIncludeUrlExists(include)) {
					redirAttr
							.addFlashAttribute("error",
									"Include url exist for the selected Application and environment");
					return UiTesting.REDIRECT_VIEW;
				}

				include.setApplicationId(uiTestingSetupForm.getApplicationId());
				include.setIncludeUrl(uiTestingSetupForm.getIncludeUrl());
				include.setEnvironmentId(uiTestingSetupForm.getEnvironmentId());

				if (includeUrlService.addIncludeURL(include)) {
					redirAttr.addFlashAttribute("Success",
							"Include url details Saved Successfully");
					updateUiTestingSetupFormForIncludeURL(uiTestingSetupForm,
							include);

					redirAttr.addFlashAttribute("includeDetail", include);
					redirAttr.addFlashAttribute("applicationId",
							include.getApplicationId());
					redirAttr.addFlashAttribute("environmentId",
							include.getEnvironmentId());

					uiTestingSetupForm
							.setEnvironmentCategoryId(uiTestingSetupForm
									.getCategoryId());
				} else {
					redirAttr.addFlashAttribute("error",
							"error occured while Saving Include url details");
				}
			}
			// saving Exclude url details
			if (StringUtils.isNotBlank(uiTestingSetupForm.getExcludeUrl())) {
				exclude = new ExcludeUrl();

				if (this.isExcludeUrlExists(exclude)) {
					redirAttr
							.addFlashAttribute("error",
									"Exclude url exist for the selected Application and environment");
					return UiTesting.REDIRECT_VIEW;
				}

				exclude.setApplicationId(uiTestingSetupForm.getApplicationId());
				exclude.setExcludeUrl(uiTestingSetupForm.getExcludeUrl());
				exclude.setEnvironmentId(uiTestingSetupForm.getEnvironmentId());

				if (excludeURLService.addExcludeURL(exclude)) {
					updateUiTestingSetupFormForExcludeURL(uiTestingSetupForm,
							exclude);

					redirAttr.addFlashAttribute("Success",
							"Exclude url details Saved Successfully");
					redirAttr.addFlashAttribute("skipDetail", exclude);
					redirAttr.addFlashAttribute("applicationId",
							exclude.getApplicationId());
					redirAttr.addFlashAttribute("environmentId",
							exclude.getEnvironmentId());
					redirAttr.addFlashAttribute("environmentCategoryId",
							uiTestingSetupForm.getEnvironmentCategoryId());
				} else {
					redirAttr.addFlashAttribute("error",
							"Error occured while  Saving Include url details");
				}
			}
		} catch (DataIntegrityViolationException e) {
			logger.error("Exception :Duplication Include/Exclude URL");
			redirAttr.addFlashAttribute("error",
					"Duplication Include/Exclude URL");
		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while Saving Include/Exclude URL");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
			redirAttr
					.addFlashAttribute("error",
							"Unexpected error occured while Saving Include/Exclude URL");
		}
		uiTestingSetupForm.setEnvironmentCategoryId(uiTestingSetupForm
				.getCategoryId());
		redirAttr.addFlashAttribute("uiTestingSetupForm", uiTestingSetupForm);
		logger.debug("Exit: SaveIncludeExcludeURLDetails");
		return UiTesting.REDIRECT_VIEW;
	}

	// Saving IncludeExcludeURL Changes--End

	// - get Exclude Craw lUrl Details -- start
	@RequestMapping(value = "/EditIncludeExcludeDetails", method = RequestMethod.POST)
	public String editIncludeExcludeDetails(RedirectAttributes redirAttr,
			Model model, @ModelAttribute UiTestingSetupForm uiTestingSetupForm) {

		logger.debug("Entry: editIncludeExcludeDetails");

		// this.setAppEnvToModel(crawlForm, model);
		IncludeUrl include;
		ExcludeUrl exclude;

		try {
			uiTestingSetupForm
					.setSetupTabNumber(UiTesting.UI_INCLUDE_EXCLUDE_URL_SETUP);
			redirAttr.addFlashAttribute("uiTestingSetupForm",
					uiTestingSetupForm);

			if (null != uiTestingSetupForm.getIncludeUrlId()
					&& uiTestingSetupForm.getIncludeUrlId() > 0) {
				include = new IncludeUrl();
				include.setIncludeUrlId(uiTestingSetupForm.getIncludeUrlId());
				include = includeUrlService.getIncludeURL(include);

				updateUiTestingSetupFormForIncludeURL(uiTestingSetupForm,
						include);
				uiTestingSetupForm.setExcludeUrl("");
				uiTestingSetupForm
						.setEnvironmentCategoryId(getEnvironmentCategoryId(
								uiTestingSetupForm.getApplicationId(),
								uiTestingSetupForm.getEnvironmentId()));

				redirAttr.addFlashAttribute("uiTestingSetupForm",
						uiTestingSetupForm);
				redirAttr.addFlashAttribute("environmentCategoryId",
						uiTestingSetupForm.getEnvironmentCategoryId());
				redirAttr.addFlashAttribute("includeDetail", include);
				redirAttr.addFlashAttribute("applicationId",
						include.getApplicationId());
				redirAttr.addFlashAttribute("environmentId",
						include.getEnvironmentId());

			} else if (null != uiTestingSetupForm.getExcludeUrlId()
					&& uiTestingSetupForm.getExcludeUrlId() > 0) {
				exclude = new ExcludeUrl();
				exclude.setExcludeUrlId(uiTestingSetupForm.getExcludeUrlId());
				exclude = excludeURLService.getExcludeURLBO(exclude);
				updateUiTestingSetupFormForExcludeURL(uiTestingSetupForm,
						exclude);
				uiTestingSetupForm.setIncludeUrl("");
				uiTestingSetupForm
						.setEnvironmentCategoryId(getEnvironmentCategoryId(
								uiTestingSetupForm.getApplicationId(),
								uiTestingSetupForm.getEnvironmentId()));
				redirAttr.addFlashAttribute("uiTestingSetupForm",
						uiTestingSetupForm);
				redirAttr.addFlashAttribute("environmentCategoryId",
						uiTestingSetupForm.getEnvironmentCategoryId());

				redirAttr.addFlashAttribute("excludeDetail", exclude);
				redirAttr.addFlashAttribute("applicationId",
						exclude.getApplicationId());
				redirAttr.addFlashAttribute("environmentId",
						exclude.getEnvironmentId());
			}
		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while getting Include/Exclude Url details");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: editIncludeExcludeDetails");
		return UiTesting.REDIRECT_VIEW;
	}

	private Integer getEnvironmentCategoryId(int applicationId,
			int environmentId) {
		AppEnvEnvironmentCategoryXref appEnvEnvironmentCategoryXref = applicationEnvironmentXrefService
				.getAppEnvEnvironmentCategoryXref(applicationId, environmentId);
		return appEnvEnvironmentCategoryXref.getEnvironmentCategoryId();
	}

	// - get Exclude Craw lUrl Details -- End

	// delete Exclude Include Url -- Start
	@RequestMapping(value = "/DeleteIncludeExcludeUrl", method = RequestMethod.POST)
	public String deleteIncludeExcludeUrl(RedirectAttributes redirAttr,
			Model model, @ModelAttribute UiTestingSetupForm uiTestingSetupForm) {
		logger.debug("Entry: DeleteIncludeExcludeUrl ");
		// setting application id and environment id to model attribute
		// this.setAppEnvToModel(crawlForm, model);
		IncludeUrl include;
		ExcludeUrl exclude;

		try {
			uiTestingSetupForm
					.setSetupTabNumber(UiTesting.UI_INCLUDE_EXCLUDE_URL_SETUP);
			redirAttr.addFlashAttribute("uiTestingSetupForm",
					uiTestingSetupForm);

			if (null != uiTestingSetupForm.getIncludeUrlId()
					&& uiTestingSetupForm.getIncludeUrlId() > 0) {
				include = new IncludeUrl();
				include.setIncludeUrlId(uiTestingSetupForm.getIncludeUrlId());
				if (includeUrlService.deleteIncludeURLDetails(include)) {
					redirAttr.addFlashAttribute("Success",
							"Include Url deleted successfully");
				} else {
					redirAttr.addFlashAttribute("error",
							"Error while deleting Include Url");

				}
			} else if (null != uiTestingSetupForm.getExcludeUrlId()
					&& uiTestingSetupForm.getExcludeUrlId() > 0) {
				exclude = new ExcludeUrl();
				exclude.setExcludeUrlId(uiTestingSetupForm.getExcludeUrlId());
				if (excludeURLService.deleteExcludeURLDetails(exclude)) {
					redirAttr.addFlashAttribute("Success",
							"Exclude Url deleted successfully");
				} else {
					redirAttr.addFlashAttribute("error",
							"Error while deleting Exclude Url");
				}
			}
		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while deleting Include/Exclude Url");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: DeleteIncludeExcludeUrl");
		return UiTesting.REDIRECT_VIEW;
	}

	// delete Exclude Include Url -- End

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/GetEnvironmentCategoryDropdown", method = RequestMethod.POST)
	public @ResponseBody
	String GetEnvironmentCategoryDropdown(
			@RequestParam(value = "applicationId") int applicationId) {
		logger.debug("Entry: GetEnvironmentCategoryDropdown ");

		List<EnvironmentCategoryGroupXref> environmentCategoryList = new ArrayList<EnvironmentCategoryGroupXref>();
		JSONObject obj = new JSONObject();
		StringWriter out = new StringWriter();

		try {
			if (applicationId > 0) {
				environmentCategoryList = environmentService
						.getEnvironmentCategoryGroupRef(
								applicationId,
								UserServiceUtils
										.getCurrentUserGroupIdFromRequest(context));

				if (environmentCategoryList != null) {
					for (EnvironmentCategoryGroupXref environmentCategoryGroupXref : environmentCategoryList) {
						obj.put(environmentCategoryGroupXref
								.getEnvironmentCategoryId(),
								environmentCategoryGroupXref
										.getEnvironmentCategory()
										.getEnvironmentCategoryName());
					}
				} else {
					obj.put("", "");
				}

				obj.writeJSONString(out);
			}

		} catch (Exception exp) {
			logger.error(
					"Exception occured during GetEnvironmentCategoryDropdown execution.",
					exp);
		}
		logger.debug("Exit: GetEnvironmentCategoryDropdown");
		return out.toString();
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/GetEnvironmentCategory", method = RequestMethod.POST)
	public @ResponseBody
	String getEnvironmentCategory(
			@RequestParam(value = "environmentId", required = true) String environmentId,
			@RequestBody String text) {
		logger.debug("Entry: GetEnvironmentCategory ");

		EnvironmentCategory environmentCategory = null;
		JSONObject obj = new JSONObject();
		StringWriter out = new StringWriter();
		try {
			if (StringUtils.isNotBlank(environmentId)) {
				environmentCategory = environmentService
						.getEnvironmentCategoryForEnvironment(Integer
								.parseInt(environmentId));

				if (environmentCategory != null) {
					if (environmentCategory.getEnvironmentCategoryId() != 1) {
						obj.put(environmentCategory.getEnvironmentCategoryId(),
								environmentCategory
										.getEnvironmentCategoryName());
					}
				} else {
					obj.put("", "");
				}
				obj.writeJSONString(out);
			}

		} catch (Exception exp) {
			logger.error(
					"Exception occured during GetEnvironmentCategoryDropdown execution.",
					exp);
		}
		logger.debug("Exit: GetEnvironmentCategory");
		return out.toString();
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/GetEnvironmentCategoryWithAll", method = RequestMethod.POST)
	public @ResponseBody
	String getEnvironmentCategoryWithAll(
			@RequestParam(value = "environmentId", required = true) String environmentId,
			@RequestBody String text) {
		logger.debug("Entry: GetEnvironmentCategory ");

		EnvironmentCategory environmentCategory = null;
		JSONObject obj = new JSONObject();
		StringWriter out = new StringWriter();
		try {
			if (StringUtils.isNotBlank(environmentId)) {
				environmentCategory = environmentService
						.getEnvironmentCategoryForEnvironment(Integer
								.parseInt(environmentId));

				if (environmentCategory != null) {
					obj.put(environmentCategory.getEnvironmentCategoryId(),
							environmentCategory.getEnvironmentCategoryName());
				} else {
					obj.put("", "");
				}
				obj.writeJSONString(out);
			}

		} catch (Exception exp) {
			logger.error(
					"Exception occured during GetEnvironmentCategoryDropdown execution.",
					exp);
		}
		logger.debug("Exit: GetEnvironmentCategory");
		return out.toString();
	}

	/**
	 * update an existing application record in application table
	 * 
	 * @param model
	 * @param applicationForm
	 */
	@RequestMapping(value = "/UpdateIncludeExcludeURL", method = RequestMethod.POST)
	public String UpdateIncludeExcludeURLDetails(RedirectAttributes redirAttr,
			Model model, @ModelAttribute UiTestingSetupForm uiTestingSetupForm) {
		logger.debug("Entry: UpdateIncludeExcludeURLDetails, uiTestingSetupForm->"
				+ uiTestingSetupForm);

		IncludeUrl include = new IncludeUrl();
		ExcludeUrl exclude = new ExcludeUrl();
		try {
			uiTestingSetupForm
					.setSetupTabNumber(UiTesting.UI_INCLUDE_EXCLUDE_URL_SETUP);
			redirAttr.addFlashAttribute("uiTestingSetupForm",
					uiTestingSetupForm);
			if (StringUtils.isNotBlank(uiTestingSetupForm.getIncludeUrl())
					&& uiTestingSetupForm.getIncludeUrlId() > 0) {
				include.setIncludeUrlId(uiTestingSetupForm.getIncludeUrlId());
				// include = includeUrlService.getIncludeURL(include);
				include.setApplicationId(uiTestingSetupForm.getApplicationId());
				include.setEnvironmentId(uiTestingSetupForm.getEnvironmentId());
				include.setIncludeUrl(uiTestingSetupForm.getIncludeUrl());

				if (includeUrlService.addIncludeURL(include)) {
					redirAttr.addFlashAttribute("Success",
							"Include url details Updated Successfully");
					updateUiTestingSetupFormForIncludeURL(uiTestingSetupForm,
							include);

					redirAttr.addFlashAttribute("uiTestingSetupForm",
							uiTestingSetupForm);

					redirAttr.addFlashAttribute("includeDetail", include);
					redirAttr.addFlashAttribute("applicationId",
							include.getApplicationId());
					redirAttr.addFlashAttribute("environmentId",
							include.getEnvironmentId());
				} else {
					redirAttr
							.addFlashAttribute("error",
									"Error occured while updating Include/Exclude details");
				}

			} else if (StringUtils.isNotBlank(uiTestingSetupForm
					.getExcludeUrl())
					&& uiTestingSetupForm.getExcludeUrlId() > 0) {
				exclude.setExcludeUrlId(uiTestingSetupForm.getExcludeUrlId());

				exclude.setApplicationId(uiTestingSetupForm.getApplicationId());
				exclude.setEnvironmentId(uiTestingSetupForm.getEnvironmentId());
				exclude.setExcludeUrl(uiTestingSetupForm.getExcludeUrl());

				if (excludeURLService.addExcludeURL(exclude)) {
					redirAttr.addFlashAttribute("Success",
							"Exclude url details Updated Successfully");
					updateUiTestingSetupFormForExcludeURL(uiTestingSetupForm,
							exclude);
					redirAttr.addFlashAttribute("uiTestingSetupForm",
							uiTestingSetupForm);

					redirAttr.addFlashAttribute("excludeDetail", exclude);
					redirAttr.addFlashAttribute("applicationId",
							include.getApplicationId());
					redirAttr.addFlashAttribute("environmentId",
							include.getEnvironmentId());
				} else {
					redirAttr
							.addFlashAttribute("error",
									"Error occured while updating Include/Exclude details");
				}

			}

		} catch (Exception exception) {
			logger.error("Exception :Unexpected error occured while updating Include/Exclude details");
			logger.error("Stack Trace :"
					+ ExceptionUtils.getStackTrace(exception));
			redirAttr
					.addFlashAttribute("error",
							"Unexpected error occured while updating Include/Exclude details");
		}

		logger.debug("Exit: UpdateIncludeExcludeURLDetails");
		return UiTesting.REDIRECT_VIEW;
	}

	// Displaying IncludeExcludeURL Changes --end

	private boolean isExcludeUrlExists(ExcludeUrl exclude) {
		boolean isExists = false;
		ExcludeUrl excl = excludeURLService.getExcludeURLBO(exclude);
		if (excl != null) {
			isExists = true;
		}
		return isExists;
	}

	private boolean isIncludeUrlExists(IncludeUrl incUrl) {
		boolean isExists = false;
		IncludeUrl include = includeUrlService.getIncludeURL(incUrl);
		if (include.getIncludeUrlId() != null && include.getIncludeUrlId() > 0) {
			isExists = true;
		}
		return isExists;
	}

	/* Include/Exclued section ends here */

	private void updateUiTestingSetupFormForApplication(
			UiTestingSetupForm uiTestingSetupForm, Application application) {
		uiTestingSetupForm.setApplicationId(application.getApplicationId());
		uiTestingSetupForm.setApplicationName(application.getApplicationName());
		uiTestingSetupForm.setApplicationDescription(application
				.getApplicationDescription());
	}

	private boolean saveEnvironmentEnvironmentCategoryXref(
			UiTestingSetupForm uiTestingSetupForm) {
		boolean isSaved = false;
		try {
			EnvEnvironmentCategoryXref envEnvironmentCategoryXref = new EnvEnvironmentCategoryXref();
			envEnvironmentCategoryXref
					.setEnvironmentCategoryId(uiTestingSetupForm
							.getEnvironmentCategoryId());
			envEnvironmentCategoryXref.setEnvironmentId(uiTestingSetupForm
					.getEnvironmentId());

			isSaved = environmentService
					.addEnvEnvironmentCategoryXref(envEnvironmentCategoryXref);
		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while saving EnvEnvironmentCategoryXref details");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		return isSaved;
	}

	/**
	 * saving environment category reference
	 * 
	 * @param uiTestingSetupForm
	 */
	private boolean saveEnvironmentCategoryXref(
			UiTestingSetupForm uiTestingSetupForm) {
		logger.debug("Entry: saveEnvironmentCategoryXref");
		boolean isSaved = false;
		try {
			AppEnvEnvironmentCategoryXref appEnvEnvironmentCategoryXref = new AppEnvEnvironmentCategoryXref();
			appEnvEnvironmentCategoryXref
					.setEnvironmentCategoryId(uiTestingSetupForm
							.getEnvironmentCategoryId());
			appEnvEnvironmentCategoryXref.setEnvironmentId(uiTestingSetupForm
					.getEnvironmentId());
			appEnvEnvironmentCategoryXref.setApplicationId(uiTestingSetupForm
					.getApplicationId());

			isSaved = environmentService
					.addEnvironmentCategoryXref(appEnvEnvironmentCategoryXref);
		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while saving environmentCategoryXref details");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: saveEnvironmentCategoryXref");
		return isSaved;
	}

	@RequestMapping(value = "/SaveLoginUserDetails", method = RequestMethod.POST)
	public String saveLoginUserDetails(RedirectAttributes redirAttr,
			Model model, @ModelAttribute UiTestingSetupForm uiTestingSetupForm) {

		logger.debug("Entry: SaveLoginUserDetails, uiTestingSetupForm->"
				+ uiTestingSetupForm);

		LoginUserDetails loginUser = null;
		LoginUserDetails loginUserOld = null;
		/*
		 * LoginUiElement loginUiElement; List<LoginUiElement>
		 * loginUiElementList = null;
		 */
		try {

			uiTestingSetupForm.setSetupTabNumber(UiTesting.UI_LOGIN_USER_SETUP);
			loginUser = new LoginUserDetails();

			loginUser.setApplicationId(uiTestingSetupForm.getApplicationId());
			loginUser.setEnvironmentId(uiTestingSetupForm.getEnvironmentId());
			loginUser.setLoginId(uiTestingSetupForm.getLoginId());
			loginUser
					.setPassword(EncrypDecryptUtil.encryptPassword(
							uiTestingSetupForm.getPassword(),
							EncryptionUtil.secretKey));
			loginUser.setSecurityAnswer(uiTestingSetupForm.getSecurityAnswer());
			loginUser.setRsaEnabled(uiTestingSetupForm.isRsaEnabled());
			loginUser.setDateTimeCreated(new Date());
			loginUser.setDateTimeModified(new Date());

			loginUserOld = loginUserService.getLoginIdDetails(loginUser);
			if (loginUserOld.getLoginId() != null) {
				redirAttr.addFlashAttribute("error",
						"Test Login Id already exists");
				redirAttr.addFlashAttribute("uiTestingSetupForm",
						uiTestingSetupForm);
				return UiTesting.REDIRECT_VIEW;
			}
			if (loginUserService.addLoginUserDetails(loginUser)) {
				updateUiTestingSetupFormForLoginUserDetails(uiTestingSetupForm,
						loginUser);
				redirAttr.addFlashAttribute("Success",
						"Login User Details Saved Successfully");
				loginUser = loginUserService.getLoginUserDetails(loginUser);
				loginUser.setPassword(EncrypDecryptUtil.decryptPassword(
						loginUser.getPassword(), EncryptionUtil.secretKey));
				redirAttr.addFlashAttribute("addLoginUserList", loginUser);
			} else {
				redirAttr.addFlashAttribute("error",
						"Failed to save Login User Details");
			}

		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while Saving LoginUserDetails");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
			model.addAttribute("error",
					"Unexpected error occured while Saving LoginUserDetails");
		}
		logger.debug("saveLoginUserDetails uiTestingSetupForm :"
				+ uiTestingSetupForm);
		redirAttr.addFlashAttribute("uiTestingSetupForm", uiTestingSetupForm);
		logger.debug("Exit: saveLoginUserDetails");
		return UiTesting.REDIRECT_VIEW;

	}

	@RequestMapping(value = "/UpdateLoginUserDetails", method = RequestMethod.POST)
	public String UpdateLoginUserDetails(RedirectAttributes redirAttr,
			Model model, @ModelAttribute UiTestingSetupForm uiTestingSetupForm) {

		logger.debug("Entry: UpdateLoginUserDetails");

		LoginUserDetails loginUser;
		try {
			uiTestingSetupForm.setSetupTabNumber(UiTesting.UI_LOGIN_USER_SETUP);
			loginUser = new LoginUserDetails();

			if (uiTestingSetupForm.getLoginUserId() > 0
					&& uiTestingSetupForm.getLoginUserId() != null) {

				loginUser.setLoginUserDetailId(uiTestingSetupForm
						.getLoginUserId());
				loginUser = loginUserService.getLoginUserDetails(loginUser);
				loginUser.setApplicationId(uiTestingSetupForm
						.getApplicationId());
				loginUser.setEnvironmentId(uiTestingSetupForm
						.getEnvironmentId());
				loginUser.setLoginId(uiTestingSetupForm.getLoginId());
				loginUser.setPassword(EncrypDecryptUtil.encryptPassword(
						uiTestingSetupForm.getPassword(),
						EncryptionUtil.secretKey));
				loginUser.setSecurityAnswer(uiTestingSetupForm
						.getSecurityAnswer());
				loginUser.setRsaEnabled(uiTestingSetupForm.isRsaEnabled());
				loginUser.setDateTimeModified(new Date());

				if (loginUserService.addLoginUserDetails(loginUser)) {
					updateUiTestingSetupFormForLoginUserDetails(
							uiTestingSetupForm, loginUser);
					redirAttr.addFlashAttribute("Success",
							"Login User Details Updated Successfully");
					loginUser = loginUserService.getLoginUserDetails(loginUser);
					loginUser.setPassword(EncrypDecryptUtil.decryptPassword(
							loginUser.getPassword(), EncryptionUtil.secretKey));
					redirAttr.addFlashAttribute("addLoginUserList", loginUser);

				} else {
					redirAttr.addFlashAttribute("error",
							"Failed to save Login User Details");
				}

			}

		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while Saving LoginUserDetails");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
			model.addAttribute("error",
					"Unexpected error occured while Saving LoginUserDetails");
		}

		redirAttr.addFlashAttribute("uiTestingSetupForm", uiTestingSetupForm);
		logger.debug("Exit: UpdateLoginUserDetails");
		return UiTesting.REDIRECT_VIEW;

	}

	@RequestMapping(value = "/deleteLoginUserDetails", method = RequestMethod.POST)
	public String deleteLoginUserDetails(RedirectAttributes redirAttr,
			Model model, @ModelAttribute UiTestingSetupForm uiTestingSetupForm) {
		logger.debug("Entry: deleteLoginUserDetails");

		LoginUserDetails loginUserDetails = new LoginUserDetails();
		try {
			uiTestingSetupForm.setSetupTabNumber(UiTesting.UI_LOGIN_USER_SETUP);
			loginUserDetails.setLoginUserDetailId(uiTestingSetupForm
					.getLoginUserId());
			if (loginUserService.deleteLoginUserDetails(loginUserDetails)) {
				redirAttr
						.addFlashAttribute("Success",
								"Sucessfully deleted the environment login user details");
			} else {
				redirAttr.addFlashAttribute("error",
						"error while deleting environment login user details");
			}
			redirAttr.addFlashAttribute("uiTestingSetupForm",
					uiTestingSetupForm);

		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while deleting login user details");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
			redirAttr
					.addFlashAttribute("error",
							"Unexpected error occured while deleting login user details");
		}
		logger.debug("Exit: deleteLoginUserDetails");
		return UiTesting.REDIRECT_VIEW;
	}

	@RequestMapping(value = "/editLoginUserDetails", method = RequestMethod.POST)
	public String editLoginUserDetails(RedirectAttributes redirAttr,
			Model model, @ModelAttribute UiTestingSetupForm uiTestingSetupForm) {
		logger.debug("Entry: editLoginUserDetails");
		// setting application id and environment id to model attribute
		LoginUserDetails loginUser = new LoginUserDetails();
		try {
			uiTestingSetupForm.setSetupTabNumber(UiTesting.UI_LOGIN_USER_SETUP);
			loginUser.setLoginUserDetailId(uiTestingSetupForm.getLoginUserId());
			loginUser = loginUserService.getLoginUserDetails(loginUser);
			loginUser.setPassword(EncrypDecryptUtil.decryptPassword(
					loginUser.getPassword(), EncryptionUtil.secretKey));
			updateUiTestingSetupFormForLoginUserDetails(uiTestingSetupForm,
					loginUser);
			uiTestingSetupForm
					.setEnvironmentCategoryId(getEnvironmentCategoryId(
							uiTestingSetupForm.getApplicationId(),
							uiTestingSetupForm.getEnvironmentId()));
			uiTestingSetupForm.setRsaEnabled(loginUser.isRsaEnabled());
			redirAttr.addFlashAttribute("loginList",
					loginUserService.getLoginUserList());
			redirAttr.addFlashAttribute("addLoginUserList", loginUser);
			redirAttr.addFlashAttribute("uiTestingSetupForm",
					uiTestingSetupForm);
		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while Savig login user details");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
			redirAttr.addFlashAttribute("error",
					"Unexpected error occured while Saving login user details");
		}
		logger.debug("Exit: editLoginUserDetails");
		return UiTesting.REDIRECT_VIEW;
	}

	private void updateUiTestingSetupFormForLoginUserDetails(
			UiTestingSetupForm uiTestingSetupForm,
			LoginUserDetails loginUserDetails) {
		uiTestingSetupForm.setLoginUserId(loginUserDetails
				.getLoginUserDetailId());
		uiTestingSetupForm
				.setApplicationId(loginUserDetails.getApplicationId());
		uiTestingSetupForm
				.setEnvironmentId(loginUserDetails.getEnvironmentId());
		uiTestingSetupForm.setLoginId(loginUserDetails.getLoginId());
		uiTestingSetupForm.setPassword(loginUserDetails.getPassword());
		uiTestingSetupForm.setSecurityAnswer(loginUserDetails
				.getSecurityAnswer());

	}

	/* html report config section begins here */
	@RequestMapping(value = "/SaveHtmlReportsConfig", method = RequestMethod.POST)
	public String saveHtmlReportsConfig(RedirectAttributes redirAttr,
			@ModelAttribute("model") ModelMap model,
			UiTestingSetupForm uiTestingSetupForm) {
		logger.debug("Entry: SaveHtmlReportsConfig");
		HtmlReportsConfig htmlReportsConfig = null;
		uiTestingSetupForm
				.setSetupTabNumber(UiTesting.UI_HTML_REPORTS_CONFIG_SETUP);
		try {

			htmlReportsConfig = new HtmlReportsConfig();
			htmlReportsConfig.setEnvironmentId(uiTestingSetupForm
					.getEnvironmentId());
			htmlReportsConfig.setApplicationId(uiTestingSetupForm
					.getApplicationId());
			if (htmlReportConfigService
					.isHtlmConfigExistForAppEnv(htmlReportsConfig)) {
				redirAttr
						.addFlashAttribute(
								"error",
								"Configuration already exists for one of the application environment combination");
				return UiTesting.REDIRECT_VIEW;
			}
			htmlReportsConfig.setRemoveTagName(uiTestingSetupForm
					.getRemoveTagName());
			if (uiTestingSetupForm.getRemoveTagOrSplitTag() == 1) {
				htmlReportsConfig.setRemoveTag(true);
				htmlReportsConfig.setSplitByContent(false);
				htmlReportsConfig.setRemoveText(false);
			} else if(uiTestingSetupForm.getRemoveTagOrSplitTag() == 2){
				htmlReportsConfig.setRemoveTag(false);
				htmlReportsConfig.setSplitByContent(true);
				htmlReportsConfig.setRemoveText(false);
			} else {
				htmlReportsConfig.setRemoveTag(false);
				htmlReportsConfig.setSplitByContent(false);
				htmlReportsConfig.setRemoveText(true);
			}
			htmlReportsConfig.setSplitContentName(uiTestingSetupForm
					.getSplitContentName());
			htmlReportsConfig.setSplitTagName(uiTestingSetupForm
					.getSplitTagName());
			htmlReportsConfig.setRemoveTextContent(
					uiTestingSetupForm.getRemoveTextContent());
			if (!htmlReportConfigService
					.addHtmlReportsConfig(htmlReportsConfig)) {
				redirAttr
						.addFlashAttribute("error",
								"Error while saving Configuration for one of the application environment");
				return UiTesting.REDIRECT_VIEW;
			} else {

				redirAttr.addFlashAttribute("Success",
						"Details Saved successfully.");
			}

		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while htmlReportsConfig details");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
			redirAttr.addFlashAttribute("error",
					"Unexpected error occured while htmlReportsConfig details");
		}
		logger.debug("Exit: SaveHtmlReportsConfig");
		redirAttr.addFlashAttribute("uiTestingSetupForm", uiTestingSetupForm);
		return UiTesting.REDIRECT_VIEW;
	}

	@RequestMapping(value = "/UpdateHtmlReportsConfig", method = RequestMethod.POST)
	public String updateHtmlReportsConfig(RedirectAttributes redirAttr,
			@ModelAttribute("model") ModelMap model,
			UiTestingSetupForm uiTestingSetupForm) {
		logger.debug("Entry: UpdateHtmlReportsConfig");
		HtmlReportsConfig htmlReportsConfig = null;
		uiTestingSetupForm
				.setSetupTabNumber(UiTesting.UI_HTML_REPORTS_CONFIG_SETUP);
		try {

			htmlReportsConfig = htmlReportConfigService
					.getHtmlReportsConfig(uiTestingSetupForm.getConfigId());

			htmlReportsConfig.setEnvironmentId(uiTestingSetupForm
					.getEnvironmentId());
			htmlReportsConfig.setApplicationId(uiTestingSetupForm
					.getApplicationId());

			htmlReportsConfig.setRemoveTagName(uiTestingSetupForm
					.getRemoveTagName());

			if (uiTestingSetupForm.getRemoveTagOrSplitTag() == 1) {
				htmlReportsConfig.setRemoveTag(true);
				htmlReportsConfig.setSplitByContent(false);
				htmlReportsConfig.setRemoveText(false);
			} else if(uiTestingSetupForm.getRemoveTagOrSplitTag() == 2){
				htmlReportsConfig.setRemoveTag(false);
				htmlReportsConfig.setSplitByContent(true);
				htmlReportsConfig.setRemoveText(false);
			} else {
				htmlReportsConfig.setRemoveTag(false);
				htmlReportsConfig.setSplitByContent(false);
				htmlReportsConfig.setRemoveText(true);
			}
			
			htmlReportsConfig.setSplitContentName(uiTestingSetupForm
					.getSplitContentName());
			htmlReportsConfig.setSplitTagName(uiTestingSetupForm
					.getSplitTagName());
			
			htmlReportsConfig.setRemoveTextContent(
					uiTestingSetupForm.getRemoveTextContent());
			
			if (!htmlReportConfigService
					.addHtmlReportsConfig(htmlReportsConfig)) {
				redirAttr
						.addFlashAttribute("error",
								"Error while saving Configuration for one of the application environment");
				return UiTesting.REDIRECT_VIEW;
			} else {
				uiTestingSetupForm
						.setEnvironmentCategoryId(getEnvironmentCategoryId(
								uiTestingSetupForm.getApplicationId(),
								uiTestingSetupForm.getEnvironmentId()));
				redirAttr.addFlashAttribute("Success",
						"Details updated successfully.");
			}

		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while updating htmlReportsConfig details");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
			redirAttr
					.addFlashAttribute("error",
							"Unexpected error occured while updating htmlReportsConfig details");
		}
		logger.debug("Exit: UpdateHtmlReportsConfig");
		redirAttr.addFlashAttribute("uiTestingSetupForm", uiTestingSetupForm);
		return UiTesting.REDIRECT_VIEW;
	}

	@RequestMapping(value = "/EditHtmlReportsConfig", method = RequestMethod.POST)
	public String editHtmlReportsConfig(RedirectAttributes redirAttr,
			@ModelAttribute("model") ModelMap model,
			UiTestingSetupForm uiTestingSetupForm) {
		logger.debug("Entry: EditHtmlReportsConfig");
		HtmlReportsConfig htmlReportsConfig = null;
		uiTestingSetupForm
				.setSetupTabNumber(UiTesting.UI_HTML_REPORTS_CONFIG_SETUP);
		try {

			htmlReportsConfig = htmlReportConfigService
					.getHtmlReportsConfig(uiTestingSetupForm.getConfigId());
			if (htmlReportsConfig != null) {
				uiTestingSetupForm.setApplicationId(htmlReportsConfig
						.getApplicationId());
				uiTestingSetupForm.setEnvironmentId(htmlReportsConfig
						.getEnvironmentId());
				uiTestingSetupForm
						.setEnvironmentCategoryId(getEnvironmentCategoryId(
								uiTestingSetupForm.getApplicationId(),
								uiTestingSetupForm.getEnvironmentId()));
				if (htmlReportsConfig.getRemoveTag()) {
					uiTestingSetupForm.setRemoveTagOrSplitTag(1);
					uiTestingSetupForm.setRemoveTagName(htmlReportsConfig
							.getRemoveTagName());
				} else if(htmlReportsConfig.getSplitByContent()){
					uiTestingSetupForm.setRemoveTagOrSplitTag(2);
					uiTestingSetupForm.setSplitTagName(htmlReportsConfig
							.getSplitTagName());
					uiTestingSetupForm.setSplitContentName(htmlReportsConfig
							.getSplitContentName());
				} else {
					uiTestingSetupForm.setRemoveTextContent(htmlReportsConfig.getRemoveTextContent());
					uiTestingSetupForm.setRemoveTagOrSplitTag(3);
				}
			}

			redirAttr.addFlashAttribute("uiTestingSetupForm",
					uiTestingSetupForm);
		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while getting htmlReportsConfig details");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
			redirAttr
					.addFlashAttribute("error",
							"Unexpected error occured while getting htmlReportsConfig details");
		}
		logger.debug("Exit: EditHtmlReportsConfig");
		redirAttr.addFlashAttribute("uiTestingSetupForm", uiTestingSetupForm);
		return UiTesting.REDIRECT_VIEW;
	}

	@RequestMapping(value = "/DeleteHtmlReportsConfig", method = RequestMethod.POST)
	public String deleteHtmlReportsConfig(RedirectAttributes redirAttr,
			@ModelAttribute("model") ModelMap model,
			UiTestingSetupForm uiTestingSetupForm) {
		logger.debug("Entry: deleteHtmlReportsConfig");
		HtmlReportsConfig hRConfig = null;
		uiTestingSetupForm
				.setSetupTabNumber(UiTesting.UI_HTML_REPORTS_CONFIG_SETUP);
		try {
			if (uiTestingSetupForm.getConfigId() > 0) {
				hRConfig = new HtmlReportsConfig();
				hRConfig.setHtmlReportsConfigId(uiTestingSetupForm
						.getConfigId());
				if (htmlReportConfigService.deleteHtmlReportsConfig(hRConfig)) {
					redirAttr.addFlashAttribute("Success",
							"Details deleted successfully.");
					uiTestingSetupForm.setConfigId(-1);
				} else {
					redirAttr.addFlashAttribute("error",
							"Unable to delete the details.");
				}
			}

		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while  deleting Html Reports Config details");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
			redirAttr
					.addFlashAttribute("error",
							"Unexpected error occured while deleting Html Reports Config details");
		}
		logger.debug("Exit: deleteHtmlReportsConfig");
		redirAttr.addFlashAttribute("uiTestingSetupForm", uiTestingSetupForm);
		return UiTesting.REDIRECT_VIEW;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/GetEnvironmentDropdown", method = RequestMethod.POST)
	public @ResponseBody
	String getEnvironmentByCategory(
			@RequestParam(value = "applicationId") int applicationId,
			@RequestParam(value = "environmentCategoryId") int environmentCategoryId) {

		logger.debug("Entry: GetEnvironmentDropdown ");

		List<Environment> environment = null;
		JSONObject obj = new JSONObject();
		StringWriter out = new StringWriter();
		try {
			if (applicationId > 0 && environmentCategoryId > 0) {
				environment = environmentService.getEnvironmentsByCategory(
						environmentCategoryId, applicationId);

				if (environment != null && environment.size() != 0) {
					for (Environment env : environment) {
						obj.put(env.getEnvironmentId(),
								env.getEnvironmentName());
					}
				} else {
					obj.put("", "");
				}
			}
			obj.writeJSONString(out);

		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while GetEnvironmentDropdown");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));

		}
		logger.debug("Exit: GetEnvironmentDropdown");
		return out.toString();
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/GetSecureEnvironmentDropdown", method = RequestMethod.POST)
	public @ResponseBody
	String getSecureEnvironmentByCategory(
			@RequestParam(value = "applicationId") int applicationId,
			@RequestParam(value = "environmentCategoryId") int environmentCategoryId) {

		logger.debug("Entry: GetSecureEnvironmentDropdown ");

		List<Environment> environment = null;
		JSONObject obj = new JSONObject();
		StringWriter out = new StringWriter();
		try {
			if (applicationId > 0 && environmentCategoryId > 0) {
				environment = environmentService
						.getSecureEnvironmentsByCategory(environmentCategoryId,
								applicationId);

				if (environment != null && environment.size() != 0) {
					for (Environment env : environment) {
						obj.put(env.getEnvironmentId(),
								env.getEnvironmentName());
					}
				} else {
					obj.put("", "");
				}
			}
			obj.writeJSONString(out);

		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while GetSecureEnvironmentDropdown");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: GetSecureEnvironmentDropdown");
		return out.toString();
	}

	@RequestMapping(value = "/SaveApplicationModule", method = RequestMethod.POST)
	public String saveApplicationModule(@Valid RedirectAttributes redirAttr,
			Model model,
			@Valid @ModelAttribute UiTestingSetupForm uiTestingSetupForm,
			BindingResult bindingResult) {
		logger.debug("Entry: SaveApplicationModule, uiTestingSetupForm->"
				+ uiTestingSetupForm);
		ApplicationModuleXref applicationModuleXref = new ApplicationModuleXref();
		boolean isUploaded = true;
		List<TransactionTestCase> transactionTestCaseList = new ArrayList<TransactionTestCase>();

		try {
			uiTestingSetupForm.setSetupTabNumber(UiTesting.UI_MODULE_SETUP);
			applicationModuleXref.setApplicationId(uiTestingSetupForm
					.getApplicationId());
			applicationModuleXref.setModuleName(uiTestingSetupForm
					.getModuleName());
			applicationModuleXref.setEnvironmentId(uiTestingSetupForm
					.getEnvironmentId());
			applicationModuleXref.setIncludeUrlPattern(uiTestingSetupForm
					.getIncludeUrlPattern());
			applicationModuleXref.setTestUrlPattern(uiTestingSetupForm
					.getTestUrlPattern());
			applicationModuleXref.setStartUrl(uiTestingSetupForm
					.getStartUrlPattern());
			applicationModuleXref.setModuleTypeId(uiTestingSetupForm
					.getModuleTypeId());

			ArrayList<String> staticUrlList = new ArrayList<String>();

			if (uiTestingSetupForm.getModuleTypeId() == ModuleType.TRANSACTION
					&& null != uiTestingSetupForm.getTestCaseFile()
					&& uiTestingSetupForm.getTestCaseFile().length > 0) {
				int index = 0;
				for (CommonsMultipartFile file : uiTestingSetupForm
						.getTestCaseFile()) {
					this.uploadTransactionScript(applicationModuleXref,
							uiTestingSetupForm, redirAttr, file,
							transactionTestCaseList,
							uiTestingSetupForm.getTestCaseName()[index]);
					if (!isUploaded) {
						return UiTesting.REDIRECT_VIEW;
					}
					index++;
				}
				if (transactionTestCaseList != null
						&& transactionTestCaseList.size() > 0) {
					applicationModuleXref
							.setTransactionTestCaseList(transactionTestCaseList);
					String destinationFolder = applicationModuleXref
							.getTestCaseDirectory();
					destinationFolder = FileDirectoryUtil
							.getAbsolutePath(
									destinationFolder,
									FileDirectoryUtil
											.getTransactionTestCaseRootPath(configProperties));
					logger.info("HTML Folder: " + destinationFolder);
					if(uiTestingSetupForm.getTestHtmlFile() != null && uiTestingSetupForm.getTestHtmlFile().length > 0) {
						index = 0;
						for (CommonsMultipartFile file : uiTestingSetupForm
								.getTestHtmlFile()) {
							this.uploadTransactionHtmlScript(destinationFolder,
									uiTestingSetupForm.getTestCaseFile()[index],
									file, redirAttr);
							index++;
						}
					}
				}
			}

			if (null != uiTestingSetupForm.getStaticUrlFile()
					&& null != uiTestingSetupForm.getColumnPosition()) {
				ExcelReaderUtil excelReaderUtil = new ExcelReaderUtil();
				staticUrlList = excelReaderUtil.getExcelColumnAsList(
						uiTestingSetupForm.getStaticUrlFile().getInputStream(),
						uiTestingSetupForm.getStaticUrlFile()
								.getOriginalFilename(), uiTestingSetupForm
								.getColumnPosition());
				logger.info("staticUrlList :" + staticUrlList);
				applicationModuleXref.setStaticUrls(null);
				int staticUrlCount = 0;
				if (CollectionUtils.isNotEmpty(staticUrlList)) {
					staticUrlCount = staticUrlList.size();
				}
				applicationModuleXref.setStaticUrlCount(staticUrlCount);
			}

			if (applicationModuleService
					.addApplicationModule(applicationModuleXref)) {
				if (null != uiTestingSetupForm.getStaticUrlFile()
						&& null != uiTestingSetupForm.getColumnPosition()) {
					String filePath = FileDirectoryUtil.getAbsolutePath(
							CommonUtils.getPropertyFromPropertyFile(
									configProperties, "mint.jsonclob"),
							FileDirectoryUtil
									.getMintRootDirectory(configProperties));
					applicationModuleXref.setStaticUrlJsonData(
							staticUrlList,
							filePath
									+ applicationModuleXref
											.getApplicationModuleXrefId() + "_"
									+ Constants.MODULE_XREF);
					applicationModuleXref.setStaticUrls(filePath
							+ applicationModuleXref
									.getApplicationModuleXrefId() + "_"
							+ Constants.MODULE_XREF);
					applicationModuleService
							.updateApplicationModule(applicationModuleXref);
				}
				updateUiTestingSetupFormForApplicationModule(
						uiTestingSetupForm, applicationModuleXref);
				redirAttr.addFlashAttribute("Success",
						"Application Module saved successfully");
			} else {
				redirAttr.addFlashAttribute("error",
						"Error while saving Application Module details");
			}
			redirAttr.addFlashAttribute("testCaseList",
					applicationModuleXref.getTransactionTestCaseList());

		} catch (Exception e) {
			logger.error("Exception in SaveApplicationModule.", e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
			redirAttr
					.addFlashAttribute("error",
							"Unexpected Error occured while saving Application Module details");
		}
		redirAttr.addFlashAttribute("uiTestingSetupForm", uiTestingSetupForm);
		logger.debug("Exit: SaveApplicationModule");
		return UiTesting.REDIRECT_VIEW;
	}

	@RequestMapping(value = "/EditApplicationModuleDetails", method = RequestMethod.POST)
	public String editApplicationModuleDetails(RedirectAttributes redirAttr,
			Model model, @ModelAttribute UiTestingSetupForm uiTestingSetupForm) {

		logger.debug("Entry: editApplicationModuleDetails");
		uiTestingSetupForm.setSetupTabNumber(UiTesting.UI_MODULE_SETUP);

		try {
			ApplicationModuleXref applicationModuleXref = new ApplicationModuleXref();
			applicationModuleXref.setApplicationModuleXrefId(uiTestingSetupForm
					.getApplicationModuleXrefId());
			applicationModuleXref = applicationModuleService
					.getApplicationModule(applicationModuleXref);
			updateUiTestingSetupFormForApplicationModule(uiTestingSetupForm,
					applicationModuleXref);
			uiTestingSetupForm
					.setEnvironmentCategoryId(getEnvironmentCategoryId(
							uiTestingSetupForm.getApplicationId(),
							uiTestingSetupForm.getEnvironmentId()));
			redirAttr.addFlashAttribute("testCaseList",
					applicationModuleXref.getTransactionTestCaseList());
		} catch (Exception e) {
			logger.error("Exception in EditApplicationModuleDetails.", e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		redirAttr.addFlashAttribute("uiTestingSetupForm", uiTestingSetupForm);
		logger.debug("Exit: editApplicationModuleDetails");
		return UiTesting.REDIRECT_VIEW;
	}


	@RequestMapping(value = "/UpdateApplicationModule", method = RequestMethod.POST)
	public String UpdateApplicationModule(RedirectAttributes redirAttr,
			Model model, @ModelAttribute UiTestingSetupForm uiTestingSetupForm) {

		logger.debug("Entry: UpdateApplicationModuleDetails");
		ApplicationModuleXref applicationModuleXref = new ApplicationModuleXref();
		boolean isUploaded = true;
		List<TransactionTestCase> transactionTestCaseList = new ArrayList<TransactionTestCase>();

		try {

			uiTestingSetupForm.setSetupTabNumber(UiTesting.UI_MODULE_SETUP);
			redirAttr.addFlashAttribute("uiTestingSetupForm",
					uiTestingSetupForm);
			applicationModuleXref.setApplicationModuleXrefId(uiTestingSetupForm
					.getApplicationModuleXrefId());
			applicationModuleXref = applicationModuleService
					.getApplicationModule(applicationModuleXref);

			applicationModuleXref.setModuleName(uiTestingSetupForm
					.getModuleName());
			applicationModuleXref.setIncludeUrlPattern(uiTestingSetupForm
					.getIncludeUrlPattern());
			applicationModuleXref.setTestUrlPattern(uiTestingSetupForm
					.getTestUrlPattern());
			applicationModuleXref.setStartUrl(uiTestingSetupForm
					.getStartUrlPattern());
			applicationModuleXref.setModuleTypeId(uiTestingSetupForm
					.getModuleTypeId());
			ArrayList<String> staticUrlList = new ArrayList<String>();

	/*		if (uiTestingSetupForm.getOldTestCaseId() > 0) {
				String filePathToDelete = null;
				for (TransactionTestCase tst : applicationModuleXref
						.getTransactionTestCaseList()) {
					if (uiTestingSetupForm.getOldTestCaseId() == tst
							.getTestCaseId()) {
						filePathToDelete = tst.getTestCasePath();
					}
				}
				applicationModuleService
						.deleteTestCaseByTestCaseId(
								uiTestingSetupForm.getOldTestCaseId(),
								filePathToDelete);
			}*/
			if (uiTestingSetupForm.getModuleTypeId() == ModuleType.TRANSACTION
					&& null != uiTestingSetupForm.getTestCaseFile()
					&& uiTestingSetupForm.getTestCaseFile().length > 0) {
				int index = 0;
				for (CommonsMultipartFile file : uiTestingSetupForm
						.getTestCaseFile()) {
					isUploaded = this.uploadTransactionScript(
							applicationModuleXref, uiTestingSetupForm,
							redirAttr, file, transactionTestCaseList,
							uiTestingSetupForm.getTestCaseName()[index]);
					if (!isUploaded) {
						return UiTesting.REDIRECT_VIEW;
					}
					index++;
				}
				applicationModuleXref
						.setTransactionTestCaseList(transactionTestCaseList);
				
				if (transactionTestCaseList != null
						&& transactionTestCaseList.size() > 0) {
					applicationModuleXref
							.setTransactionTestCaseList(transactionTestCaseList);
					String destinationFolder = applicationModuleXref
							.getTestCaseDirectory();
					destinationFolder = FileDirectoryUtil
							.getAbsolutePath(
									destinationFolder,
									FileDirectoryUtil
											.getTransactionTestCaseRootPath(configProperties));
					logger.info("HTML Folder: " + destinationFolder);
					if(uiTestingSetupForm.getTestHtmlFile() != null && uiTestingSetupForm.getTestHtmlFile().length > 0) {
						index = 0;
						for (CommonsMultipartFile file : uiTestingSetupForm
								.getTestHtmlFile()) {
							this.uploadTransactionHtmlScript(destinationFolder,
									uiTestingSetupForm.getTestCaseFile()[index],
									file, redirAttr);
							index++;
						}
					}
				}
			}
			
		

			if (null != uiTestingSetupForm.getStaticUrlFile()
					&& null != uiTestingSetupForm.getColumnPosition()) {
				ExcelReaderUtil excelReaderUtil = new ExcelReaderUtil();
				staticUrlList = excelReaderUtil.getExcelColumnAsList(
						uiTestingSetupForm.getStaticUrlFile().getInputStream(),
						uiTestingSetupForm.getStaticUrlFile()
								.getOriginalFilename(), uiTestingSetupForm
								.getColumnPosition());
				logger.info("staticUrlList :" + staticUrlList);
				int staticUrlCount = 0;
				if (CollectionUtils.isNotEmpty(staticUrlList)) {
					staticUrlCount = staticUrlList.size();
				}
				applicationModuleXref.setStaticUrlCount(staticUrlCount);
			}

			if (applicationModuleService
					.updateApplicationModule(applicationModuleXref)) {
				if (null != uiTestingSetupForm.getStaticUrlFile()
						&& null != uiTestingSetupForm.getColumnPosition()) {
					applicationModuleXref.setStaticUrlJsonData(staticUrlList,
							applicationModuleXref.getStaticUrls());
				}

				updateUiTestingSetupFormForApplicationModule(
						uiTestingSetupForm, applicationModuleXref);

				redirAttr.addFlashAttribute("Success",
						"Application Module details updated successfully");
			} else {
				redirAttr.addFlashAttribute("error",
						"Error while updating Application Module details");
			}
			applicationModuleXref = applicationModuleService
					.getApplicationModule(applicationModuleXref);
			redirAttr.addFlashAttribute("testCaseList",
					applicationModuleXref.getTransactionTestCaseList());

		} catch (Exception e) {
			logger.error("Exception in UpdateApplicationModuleDetails error", e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
			redirAttr
					.addFlashAttribute("error",
							"Unexpected Error occured while Updating Application Module details");
		}
		redirAttr.addFlashAttribute("uiTestingSetupForm", uiTestingSetupForm);
		logger.debug("Exit: updateApplicationModuleDetails");
		return UiTesting.REDIRECT_VIEW;
	}
	
	@RequestMapping(value = "/UpdateTestCase", method = RequestMethod.POST)
	public @ResponseBody
	String UpdateTestCase(@RequestParam("javafile") CommonsMultipartFile javafile,
			@RequestParam("htmlfile") CommonsMultipartFile htmlfile, String oldTestcaseId, String testCaseName) {
		logger.debug("Entry: UpdateTestCase");
		TransactionTestCase transactionTestCase = null;
		String message = "Error";
		try {
			transactionTestCase =  applicationModuleService.getTransactionTestCase(Integer.parseInt(oldTestcaseId));
			transactionTestCase.setTransactionName(testCaseName);
			//applicationModuleService.deleteTestCaseByTestCaseId(transactionTestCase.getTestCaseId(), transactionTestCase.getTestCasePath());
			if(applicationModuleService.updateTransactionTestCase(transactionTestCase, javafile, htmlfile)) {
				message = "Success";
			}
		} catch (Exception e) {
			logger.error("Exception in UpdateTestCase error", e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: UpdateTestCase");
		return message;
	}
	
	@RequestMapping(value = "/UpdateTestCaseJavaOnly", method = RequestMethod.POST)
	public @ResponseBody
	String UpdateTestCaseJavaOnly(@RequestParam("javafile") CommonsMultipartFile javafile, String oldTestcaseId, String testCaseName) {
		logger.debug("Entry: UpdateTestCase");
		TransactionTestCase transactionTestCase = null;
		String message = "Error";
		try {
			transactionTestCase =  applicationModuleService.getTransactionTestCase(Integer.parseInt(oldTestcaseId));
			transactionTestCase.setTransactionName(testCaseName);
			//applicationModuleService.deleteTestCaseByTestCaseId(transactionTestCase.getTestCaseId(), transactionTestCase.getTestCasePath());
			if(applicationModuleService.updateTransactionTestCase(transactionTestCase, javafile, null)) {
				message = "Success";
			}
		} catch (Exception e) {
			logger.error("Exception in UpdateTestCase error", e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: UpdateTestCase");
		return message;
	}

	@RequestMapping(value = "/DeleteApplicationModule", method = RequestMethod.POST)
	public String deleteApplicationModuleDetails(RedirectAttributes redirAttr,
			Model model, @ModelAttribute UiTestingSetupForm uiTestingSetupForm) {

		logger.debug("Entry: deleteApplicationModuleDetails");
		ApplicationModuleXref applicationModuleXref = new ApplicationModuleXref();
		try {

			uiTestingSetupForm.setSetupTabNumber(UiTesting.UI_MODULE_SETUP);
			redirAttr.addFlashAttribute("uiTestingSetupForm",
					uiTestingSetupForm);
			applicationModuleXref.setApplicationModuleXrefId(uiTestingSetupForm
					.getApplicationModuleXrefId());
			applicationModuleXref = applicationModuleService
					.getApplicationModule(applicationModuleXref);
			applicationModuleXref.setIncludeUrlPattern(uiTestingSetupForm
					.getIncludeUrlPattern());
			applicationModuleXref.setTestUrlPattern(uiTestingSetupForm
					.getTestUrlPattern());

			if (applicationModuleService
					.deleteApplicationModule(applicationModuleXref)) {
				uiTestingSetupForm = new UiTestingSetupForm();
				uiTestingSetupForm.setSetupTabNumber(UiTesting.UI_MODULE_SETUP);
				redirAttr.addFlashAttribute("Success",
						"Application Module details deleted successfully");
			} else {
				redirAttr.addFlashAttribute("error",
						"Error while deleting Application Module details");
			}

		} catch (Exception e) {
			logger.error("Exception in deleteApplicationModuleDetails error", e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: deleteApplicationModuleDetails");
		return UiTesting.REDIRECT_VIEW;
	}

	private UiTestingSetupForm updateUiTestingSetupFormForApplicationModule(
			UiTestingSetupForm uiTestingSetupForm,
			ApplicationModuleXref applicationModuleXref) {
		uiTestingSetupForm.setConfigId(applicationModuleXref
				.getApplicationModuleXrefId());
		uiTestingSetupForm.setApplicationId(applicationModuleXref
				.getApplicationId());
		uiTestingSetupForm.setEnvironmentId(applicationModuleXref
				.getEnvironmentId());
		uiTestingSetupForm.setModuleName(applicationModuleXref.getModuleName());
		uiTestingSetupForm.setIncludeUrlPattern(applicationModuleXref
				.getIncludeUrlPattern());
		uiTestingSetupForm.setTestUrlPattern(applicationModuleXref
				.getTestUrlPattern());
		uiTestingSetupForm.setStartUrlPattern(applicationModuleXref
				.getStartUrl());
		uiTestingSetupForm.setModuleTypeId(applicationModuleXref
				.getModuleTypeId());
		return uiTestingSetupForm;
	}

	private boolean isIncludeExcludeUrlDetailsAvailable(
			Map<String, Object> asMap) {
		if (asMap.containsKey("includeURLList")
				&& asMap.containsKey("excludeURLList")) {
			return true;
		}
		return false;
	}

	private boolean isEnvironmentSetupDetailsAvailable(Map<String, Object> asMap) {
		if (asMap.containsKey("environmentList")
				&& asMap.containsKey("environmentCategoryList")) {
			return true;
		}
		return false;
	}

	private boolean isApplicationConfigDetailsExists(Map<String, Object> asMap) {
		if (asMap.containsKey("applications")
				&& asMap.containsKey("environmentList")
				&& asMap.containsKey("environmentCategoryList")
				&& asMap.containsKey("applicationConfigList")) {
			return true;
		}
		return false;
	}

	private boolean isHtmlConfigDetailsExists(Map<String, Object> asMap) {
		if (asMap.containsKey("applications")
				&& asMap.containsKey("environmentList")
				&& asMap.containsKey("environmentCategoryList")
				&& asMap.containsKey("htmlReportConfigList")) {
			return true;
		}
		return false;
	}

	private boolean isApplicationModuleDetailsAvailable(
			Map<String, Object> asMap) {
		if (asMap.containsKey("applicationModuleList")
				&& asMap.containsKey("moduleTypeList")) {
			return true;
		}
		return false;
	}

	private boolean createDestinationFile(MultipartFile file, String fileName) {
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			if (file != null && file.getSize() > 0) {
				inputStream = file.getInputStream();
			} else {
				return false;
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

	private boolean isApplicationSetupDetailsAvailable(Map<String, Object> asMap) {
		if (asMap.containsKey("applications")) {
			return true;
		}
		return false;
	}

	private boolean isLoginUserSetupDetailsAvailable(Map<String, Object> asMap) {
		if (asMap.containsKey("applications")
				&& asMap.containsKey("environmentList")
				&& asMap.containsKey("environmentCategoryList")
				&& asMap.containsKey("loginList")) {
			return true;
		}
		return false;
	}

	private int getUiSetupTabNumber(Map<?, ?> map) {
		int setUpTabNumber = 1;

		if (map.containsKey("setupTab")) {
			UiTestingSetupForm uiTestingSetupForm = (UiTestingSetupForm) map
					.get("setupTab");
			setUpTabNumber = uiTestingSetupForm.getSetupTabNumber();
		}

		return setUpTabNumber;
	}

	private void updateUiTestingSetupFormForEnvironment(
			UiTestingSetupForm uiTestingSetupForm, Environment environment) {
		uiTestingSetupForm.setEnvironmentId(environment.getEnvironmentId());
		uiTestingSetupForm.setEnvironmentName(environment.getEnvironmentName());
		uiTestingSetupForm.setLoginOrHomeUrl(environment.getLoginOrHomeUrl());
		uiTestingSetupForm.setSecureSite(environment.getSecureSite());
	}

	private void updateUiTestingSetupFormForApplicationConfig(
			UiTestingSetupForm uiTestingSetupForm,
			ApplicationConfig applicationConfig) {
		uiTestingSetupForm.setConfigId(applicationConfig.getConfigId());
		uiTestingSetupForm.setApplicationId(applicationConfig
				.getApplicationId());
		uiTestingSetupForm.setEnvironmentId(applicationConfig
				.getEnvironmentId());
		uiTestingSetupForm.setBrowserRestart(applicationConfig
				.getBrowserRestart());
		uiTestingSetupForm.setNoOfBrowsers(applicationConfig.getNoOfBrowsers());
		uiTestingSetupForm.setBrowserTimeout(applicationConfig
				.getBrowserTimeout());
	}

	/**
	 * validate and Uploads the login script file
	 * 
	 * @param environment
	 * @param uiTestingSetupForm
	 * @param redirAttr
	 */
	private boolean uploadScript(Environment environment,
			UiTestingSetupForm uiTestingSetupForm,
			RedirectAttributes redirAttr, boolean isTransactional) {
		String fileName = "";
		String packageDirectory = "";
		String packageName = "";
		String jarLocation = "";
		String fileExtension = "";
		String destinationFileName = "";
		boolean status = false;
		String environmentName = "", applicationName = "";
		File destinationFile;
		int environmentId = 0;
		EnvironmentLoginScript environmentLoginScript = null;
		Application application = new Application();

		try {

			environmentId = environment.getEnvironmentId();
			environmentName = environment.getEnvironmentName();
			application = applicationService.getApplication(uiTestingSetupForm
					.getApplicationId());
			applicationName = application.getApplicationName();
			File newFile = new File(uiTestingSetupForm.getLoginScriptFile()
					.getOriginalFilename());
			fileName = newFile.getName();
			String scriptName = fileName.split("\\.")[0];
			packageDirectory = CommonUtils.getPropertyFromPropertyFile(
					configProperties, "mint.loginScript.packageDirectory");

			if (StringUtils.isEmpty(packageDirectory)) {
				logger.error("Invalid login script login Directory.");
				redirAttr.addFlashAttribute("error",
						"Invalid login script login Directory.");
				status = false;

				return status;
			}
			packageDirectory = FileDirectoryUtil.getAbsolutePath(
					packageDirectory,
					FileDirectoryUtil.getMintRootDirectory(configProperties));

			packageName = CommonUtils.getPropertyFromPropertyFile(
					configProperties, "mint.loginScript.packageName");

			jarLocation = CommonUtils.getPropertyFromPropertyFile(
					configProperties, "mint.testcase.jarLocation");
			jarLocation = FileDirectoryUtil.getAbsolutePath(jarLocation,
					FileDirectoryUtil.getMintRootDirectory(configProperties));
			logger.debug("jarLocation :" + jarLocation);
			environmentName = environmentName.trim().replaceAll(" ", "");
			applicationName = applicationName.trim().replaceAll(" ", "");
			destinationFileName = packageDirectory + "\\" + "\\"
					+ applicationName + "_" + environmentName;
			destinationFileName = destinationFileName + "\\" + fileName;
			MintFileUtils.deleteDir(new File(destinationFileName.substring(0,
					destinationFileName.lastIndexOf("\\"))));
			FileDirectoryUtil.createDirectories(destinationFileName.substring(
					0, destinationFileName.lastIndexOf("\\")));
			destinationFile = new File(destinationFileName);
			fileExtension = FilenameUtils.getExtension(newFile.getName());
			logger.debug("destinationFileName :" + destinationFileName);
			if (fileExtension.equals("java")) {
				if (!this.createDestinationFile(
						uiTestingSetupForm.getLoginScriptFile(),
						destinationFileName)) {

					redirAttr.addFlashAttribute("error",
							"Error while saving the uploaded file.");
					return status;
				}
				packageName += "." + applicationName + "_" + environmentName;
				status = new TestCaseFileValidationUtil().validateFile(newFile,
						destinationFile, packageName, jarLocation);

				if (!status) {
					redirAttr
							.addFlashAttribute(
									"error",
									"Issue while validating the uploaded file. There might be compilation issues. See log for more details.");
					return status;
				}
				if (status) {
					List<EnvironmentLoginScript> oldEnvironmentScript = environmentService
							.getEnvironmentScriptListDetails();

					boolean dbValueExists = false;
					boolean dbFileExists = false;
					for (EnvironmentLoginScript test : oldEnvironmentScript) {
						if (test.getEnvironmentId() == environmentId
								&& test.getSourceFileName().equalsIgnoreCase(
										uiTestingSetupForm.getLoginScriptFile()
												.getOriginalFilename())) {
							dbValueExists = true;
						} else if (test.getEnvironmentId() == environmentId
								&& test.getSourceFileName().equals(
										destinationFile.getName())) {
							dbFileExists = true;
						}
					}
					if (dbValueExists) {
						redirAttr
								.addFlashAttribute("error",
										"TestCase Details Are Already Exist.Please Add New Test Case Details");
						status = false;
						return status;
					} else if (dbFileExists) {
						redirAttr.addFlashAttribute("error",
								"File already available with the name "
										+ destinationFile.getName()
										+ " Please upload another file.");
						status = false;
						return status;
					}
					environmentLoginScript = new EnvironmentLoginScript();
					if (environment.getEnvironmentLoginScript() != null) {
						environmentLoginScript
								.setEnvironmentLoginScriptId(environment
										.getEnvironmentLoginScript()
										.getEnvironmentLoginScriptId());
					}
					environmentLoginScript.setEnvironmentId(environment
							.getEnvironmentId());
					environmentLoginScript.setSourceFileName(scriptName);
					environmentLoginScript.setSourcefolderPath(destinationFile
							.getAbsolutePath());
					if (environmentService
							.addEnvironmentLoginScript(environmentLoginScript)) {
						redirAttr
								.addFlashAttribute("Success",
										"Environment And Login Script Details are Added Successfully");
					} else {
						redirAttr
								.addFlashAttribute("error",
										"Error occured while saving Login Script Details");
					}
				}
				redirAttr.addFlashAttribute("environmentScriptDetails",
						environmentService
								.getEnvironmentLoginScript(environmentId));
				status = true;
			}
		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while uploading login user script.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
			redirAttr
					.addFlashAttribute("error",
							"Unexpected error occured while uploading login user script.");
			status = false;
		}
		return status;
	}

	private boolean uploadTransactionScript(
			ApplicationModuleXref applicationModuleXref,
			UiTestingSetupForm uiTestingSetupForm,
			RedirectAttributes redirAttr, CommonsMultipartFile newFile,
			List<TransactionTestCase> transactionTestCaseList,
			String trnsactionName) {
		String packageDirectory = "";
		String scriptDirectory = "";
		String scriptName = "";
		String packageName = "";
		String jarLocation = "";
		String destinationFileName = "";
		String testCaseFileRelativePath = "";
		String testCaseRootPath = "";
		boolean status = false;
		File destinationFile;
		Application application = new Application();
		Environment environment = new Environment();
		TransactionTestCase testCase = new TransactionTestCase();

		try {
			application = applicationService.getApplication(uiTestingSetupForm
					.getApplicationId());
			environment = environmentService.getEnvironment(uiTestingSetupForm
					.getEnvironmentId());
			scriptName = new File(newFile.getOriginalFilename()).getName()
					.split("\\.")[0];
			packageDirectory = CommonUtils.getPropertyFromPropertyFile(
					configProperties,
					"mint.transactional.testcase.packageDirectory");

			if (StringUtils.isEmpty(packageDirectory)) {
				logger.error("Invalid test case directory.");
				redirAttr.addFlashAttribute("error",
						"Invalid test case directory.");
				return false;
			}

			packageDirectory = FileDirectoryUtil.getAbsolutePath(
					packageDirectory,
					FileDirectoryUtil.getMintRootDirectory(configProperties));
			packageName = CommonUtils
					.getPropertyFromPropertyFile(configProperties,
							"mint.transactional.testcase.packageName");
			jarLocation = FileDirectoryUtil.getAbsolutePath(CommonUtils
					.getPropertyFromPropertyFile(configProperties,
							"mint.testcase.jarLocation"), FileDirectoryUtil
					.getMintRootDirectory(configProperties));
			logger.debug("jarLocation :" + jarLocation);

			String environmentName = environment.getEnvironmentName().trim()
					.replaceAll(" ", "");
			String applicationName = application.getApplicationName().trim()
					.replaceAll(" ", "");

			scriptDirectory = packageDirectory + "\\" + applicationName + "_"
					+ environmentName + "\\"
					+ uiTestingSetupForm.getModuleName();
			destinationFileName = scriptDirectory + "\\" + scriptName
					+ FILE.JAVA;
			FileDirectoryUtil.createDirectories(scriptDirectory);
			destinationFile = new File(destinationFileName);
			logger.debug("destinationFileName :" + destinationFileName);

			if (!this.createDestinationFile(newFile, destinationFileName)) {
				redirAttr.addFlashAttribute("error",
						"Error while saving the uploaded file.");
				return false;
			}
			packageName += "." + applicationName + "_" + environmentName + "." +uiTestingSetupForm.getModuleName();
			status = new TestCaseFileValidationUtil().validateFile(new File(
					newFile.getOriginalFilename()), destinationFile, packageName, jarLocation);

			if (!status) {
				redirAttr
						.addFlashAttribute(
								"error",
								"Issue while validating the uploaded file. There might be compilation issues. See log for more details.");
				return false;
			}
			testCaseFileRelativePath = StringUtils.substringBeforeLast(
					destinationFile.getAbsolutePath(), KEYS.DOT);
			testCaseRootPath = new File(
					FileDirectoryUtil
							.getTransactionTestCaseRootPath(configProperties))
					.getAbsolutePath();
			testCaseFileRelativePath = StringUtils.substringAfterLast(
					testCaseFileRelativePath, testCaseRootPath);
			testCase.setTestCasePath(testCaseFileRelativePath);
			testCase.setTransactionName(trnsactionName);
			transactionTestCaseList.add(testCase);
			status = true;
		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while uploading login user script.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
			redirAttr
					.addFlashAttribute("error",
							"Unexpected error occured while uploading login user script.");
			status = false;
		}
		return status;
	}

	private boolean uploadTransactionHtmlScript(String destinationPath,
			CommonsMultipartFile destFile, CommonsMultipartFile newFile,
			RedirectAttributes redirAttr) {
		boolean status = false;
		String destFileName = destinationPath + "\\"
				+ destFile.getOriginalFilename().replace(FILE.JAVA, FILE.HTML);
		logger.info("HTMLFile :" + destFileName);
		try {
			if (!this.createDestinationFile(newFile, destFileName)) {
				redirAttr.addFlashAttribute("error",
						"Error while saving the uploaded file.");
				return false;
			}
			status = true;
		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while uploading login user script.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
			redirAttr
					.addFlashAttribute("error",
							"Unexpected error occured while uploading login user script.");
			status = false;
		}
		return status;
	}

	private void updateUiTestingSetupFormForIncludeURL(
			UiTestingSetupForm uiTestingSetupForm, IncludeUrl includeUrl) {
		uiTestingSetupForm.setIncludeUrlId(includeUrl.getIncludeUrlId());
		uiTestingSetupForm.setApplicationId(includeUrl.getApplicationId());
		uiTestingSetupForm.setEnvironmentId(includeUrl.getEnvironmentId());
		uiTestingSetupForm.setIncludeUrl(includeUrl.getIncludeUrl());

	}

	private void updateUiTestingSetupFormForExcludeURL(
			UiTestingSetupForm uiTestingSetupForm, ExcludeUrl excludeUrl) {
		uiTestingSetupForm.setExcludeUrlId(excludeUrl.getExcludeUrlId());
		uiTestingSetupForm.setApplicationId(excludeUrl.getApplicationId());
		uiTestingSetupForm.setEnvironmentId(excludeUrl.getEnvironmentId());
		uiTestingSetupForm.setExcludeUrl(excludeUrl.getExcludeUrl());
	}

	private boolean saveApplicationGroupReference(int applicationId)
			throws Exception {
		boolean saved = false;
		ApplicationGroupReference applicationGroupReference = new ApplicationGroupReference();
		applicationGroupReference.setApplicationId(applicationId);
		applicationGroupReference.setGroupId(UserServiceUtils
				.getCurrentUserGroupIdFromRequest(context));
		saved = applicationService
				.saveApplicationGroupReference(applicationGroupReference);
		return saved;
	}

	private boolean saveEnvironmentGroupReference(int environmentcategoryId)
			throws Exception {
		boolean saved = false;
		EnvironmentCategoryGroupXref environmentCategoryGroupXref = new EnvironmentCategoryGroupXref();
		environmentCategoryGroupXref
				.setEnvironmentCategoryId(environmentcategoryId);
		environmentCategoryGroupXref.setGroupId(UserServiceUtils
				.getCurrentUserGroupIdFromRequest(context));
		saved = environmentService
				.saveEnvironmentGroupReference(environmentCategoryGroupXref);
		return saved;
	}

	private void setCommonTabDetailsToModel(
			UiTestingSetupForm uiTestingSetupForm, Model model) {
		model.addAttribute("applications",
				applicationService.getAllApplicationDetails());

		if (uiTestingSetupForm.getApplicationId() != null) {

			model.addAttribute("environmentCategoryList", environmentService
					.getEnvironmentCategoryGroupRef(uiTestingSetupForm
							.getApplicationId(), UserServiceUtils
							.getCurrentUserGroupIdFromRequest(context)));

			if (uiTestingSetupForm.getEnvironmentId() != null
					&& uiTestingSetupForm.getCategoryId() == null) {
				uiTestingSetupForm.setCategoryId(this.getEnvironmentCategoryId(
						uiTestingSetupForm.getApplicationId(),
						uiTestingSetupForm.getEnvironmentId()));
			}

			if (uiTestingSetupForm.getEnvironmentId() != null
					&& uiTestingSetupForm.getEnvironmentCategoryId() == null) {
				uiTestingSetupForm.setEnvironmentCategoryId(this
						.getEnvironmentCategoryId(
								uiTestingSetupForm.getApplicationId(),
								uiTestingSetupForm.getEnvironmentId()));
			}
		} else {
			model.addAttribute("environmentCategoryList", environmentService
					.getEnvironmentCategoryGroupRefByGroupId(UserServiceUtils
							.getCurrentUserGroupIdFromRequest(context)));
		}

		if (uiTestingSetupForm.getApplicationId() != null
				&& uiTestingSetupForm.getEnvironmentCategoryId() != null) {
			model.addAttribute("environmentList", environmentService
					.getEnvironmentsByCategory(
							uiTestingSetupForm.getEnvironmentCategoryId(),
							uiTestingSetupForm.getApplicationId()));
		} else {
			model.addAttribute("environmentList",
					environmentService.getAllEnvironmentDetails());
		}
	}

	private void setCommonTabDetailsToRedirectAttributes(
			UiTestingSetupForm uiTestingSetupForm, RedirectAttributes redirAttr) {

		redirAttr.addFlashAttribute("applications",
				applicationService.getAllApplicationDetails());

		if (uiTestingSetupForm.getApplicationId() != null) {
			redirAttr
					.addFlashAttribute(
							"environmentCategoryList",
							environmentService.getEnvironmentCategoryGroupRef(
									uiTestingSetupForm.getApplicationId(),
									UserServiceUtils
											.getCurrentUserGroupIdFromRequest(context)));

			if (uiTestingSetupForm.getEnvironmentId() != null
					&& uiTestingSetupForm.getCategoryId() == null) {
				uiTestingSetupForm.setCategoryId(this.getEnvironmentCategoryId(
						uiTestingSetupForm.getApplicationId(),
						uiTestingSetupForm.getEnvironmentId()));
			}
			if (uiTestingSetupForm.getEnvironmentId() != null
					&& uiTestingSetupForm.getEnvironmentCategoryId() == null) {
				uiTestingSetupForm.setEnvironmentCategoryId(this
						.getEnvironmentCategoryId(
								uiTestingSetupForm.getApplicationId(),
								uiTestingSetupForm.getEnvironmentId()));
			}
		} else {

			redirAttr
					.addFlashAttribute(
							"environmentCategoryList",
							environmentService
									.getEnvironmentCategoryGroupRefByGroupId(UserServiceUtils
											.getCurrentUserGroupIdFromRequest(context)));
		}

		if (uiTestingSetupForm.getApplicationId() != null
				&& uiTestingSetupForm.getEnvironmentCategoryId() != null) {
			redirAttr.addFlashAttribute("environmentList", environmentService
					.getEnvironmentsByCategory(
							uiTestingSetupForm.getEnvironmentCategoryId(),
							uiTestingSetupForm.getApplicationId()));
		} else {
			redirAttr.addFlashAttribute("environmentList",
					environmentService.getAllEnvironmentDetails());
		}
	}

	private boolean saveUitestingConfig(
			com.cts.mint.uitesting.model.UiTestingSetupForm uiTestingSetupForm) {
		ApplicationConfig applicationConfig = new ApplicationConfig();
		applicationConfig.setApplicationId(uiTestingSetupForm
				.getApplicationId());
		applicationConfig.setEnvironmentId(uiTestingSetupForm
				.getEnvironmentId());
		applicationConfig.setBrowserTimeout(60);
		applicationConfig.setBrowserRestart(200);
		applicationConfig.setNoOfBrowsers(5);
		return applicationConfigService
				.saveApplicationConfig(applicationConfig);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/CheckSecureEnvironmentDropdown", method = RequestMethod.POST)
	public @ResponseBody
	String checkSecureEnvironment(
			@RequestParam(value = "applicationId") int applicationId,
			@RequestParam(value = "categoryId") int categoryId) {

		logger.debug("Entry: CheckSecureEnvironmentDropdown ");

		List<Environment> environment = null;
		JSONObject obj = new JSONObject();
		StringWriter out = new StringWriter();
		try {
			if (applicationId > 0 && categoryId > 0) {
				environment = environmentService.getEnvironmentsByCategory(
						categoryId, applicationId);

				if (environment != null && environment.size() != 0) {
					for (Environment env : environment) {
						obj.put(env.getEnvironmentId(), env.getSecureSite());
					}
				} else {
					obj.put("", "");
				}
			}
			obj.writeJSONString(out);

		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while CheckSecureEnvironmentDropdown");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));

		}
		logger.debug("Exit: CheckSecureEnvironmentDropdown");
		return out.toString();
	}

	/* Analytics Exclude Setup section begins here */

	// Saving AnalyticsExcludeSetup Changes--Start
	@RequestMapping(value = "/SaveAnalyticsExcludeSetup", method = RequestMethod.POST)
	public String SaveAnalyticsExcludeSetupDetails(
			RedirectAttributes redirAttr, Model model,
			@ModelAttribute UiTestingSetupForm uiTestingSetupForm) {

		logger.debug("Entry: SaveAnalyticsExcludeSetup, uiTestingSetupForm ->"
				+ uiTestingSetupForm);
		AnalyticsExcludeLink analyticsExcludeLink;

		try {
			uiTestingSetupForm
					.setSetupTabNumber(UiTesting.UI_ANALYTICS_EXCLUDE_SETUP);
			redirAttr.addFlashAttribute("uiTestingSetupForm",
					uiTestingSetupForm);
			// saving Include url details
			if (StringUtils.isNotBlank(uiTestingSetupForm.getExcludeLink())) {
				analyticsExcludeLink = new AnalyticsExcludeLink();

				analyticsExcludeLink.setApplicationId(uiTestingSetupForm
						.getApplicationId());
				analyticsExcludeLink.setEnvironmentId(uiTestingSetupForm
						.getEnvironmentId());
				analyticsExcludeLink.setExcludeLinktypeId(uiTestingSetupForm
						.getExcludeLinkType());
				analyticsExcludeLink.setExcludeLink(uiTestingSetupForm
						.getExcludeLink());

				if (excludeURLService.addExcludeLink(analyticsExcludeLink)) {
					redirAttr
							.addFlashAttribute("Success",
									"Analytics Exclude Link Setup details Saved Successfully");
					updateUiTestingSetupFormForAnalyticsExcludeLink(
							uiTestingSetupForm, analyticsExcludeLink);

					redirAttr.addFlashAttribute("analyticsExcludeLinkDetail",
							analyticsExcludeLink);
					redirAttr.addFlashAttribute("applicationId",
							analyticsExcludeLink.getApplicationId());
					redirAttr.addFlashAttribute("environmentId",
							analyticsExcludeLink.getEnvironmentId());

				} else {
					redirAttr.addFlashAttribute("error",
							"error occured while Saving Include url details");
				}
			}

		} catch (DataIntegrityViolationException e) {
			logger.error("Exception :Duplication SaveAnalyticsExcludeSetup URL");
			redirAttr.addFlashAttribute("error",
					"Duplication SaveAnalyticsExcludeSetup URL");
		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while Saving SaveAnalyticsExcludeSetup");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
			redirAttr
					.addFlashAttribute("error",
							"Unexpected error occured while Saving SaveAnalyticsExcludeSetup");
		}
		uiTestingSetupForm.setEnvironmentCategoryId(uiTestingSetupForm
				.getCategoryId());
		redirAttr.addFlashAttribute("uiTestingSetupForm", uiTestingSetupForm);
		logger.debug("Exit: SaveAnalyticsExcludeSetup");
		return UiTesting.REDIRECT_VIEW;
	}

	// Saving AnalyticsExcludeSetup Changes--End

	// editing AnalyticsExcludeSetup Changes--beg
	@RequestMapping(value = "/EditAnalyticsExcludeSetupDetails", method = RequestMethod.POST)
	public String editAnalyticsExcludeSetupDetailsDetails(
			RedirectAttributes redirAttr, Model model,
			@ModelAttribute UiTestingSetupForm uiTestingSetupForm) {

		logger.debug("Entry: EditAnalyticsExcludeSetupDetails");
		uiTestingSetupForm
				.setSetupTabNumber(UiTesting.UI_ANALYTICS_EXCLUDE_SETUP);
		AnalyticsExcludeLink analyticsExcludeLink;
		try {
			if (uiTestingSetupForm.getAnalyticsExcludeLinkId() > 0) {
				analyticsExcludeLink = new AnalyticsExcludeLink();
				analyticsExcludeLink
						.setAnalyticsExcludeLinkId(uiTestingSetupForm
								.getAnalyticsExcludeLinkId());
				analyticsExcludeLink = excludeURLService
						.getAnalyticsExcludeSetupModule(analyticsExcludeLink);

				updateUiTestingSetupFormForAnalyticsExcludeLink(
						uiTestingSetupForm, analyticsExcludeLink);
				uiTestingSetupForm
						.setAnalyticsExcludeLinkId(analyticsExcludeLink
								.getAnalyticsExcludeLinkId());
				uiTestingSetupForm.setEnvironmentCategoryId(uiTestingSetupForm
						.getCategoryId());

				redirAttr.addFlashAttribute("uiTestingSetupForm",
						uiTestingSetupForm);
			}
		} catch (Exception e) {
			logger.error("Exception in EditAnalyticsExcludeSetupDetails.", e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: EditAnalyticsExcludeSetupDetails");
		return UiTesting.REDIRECT_VIEW;
	}

	// editing AnalyticsExcludeSetup Changes--End

	// deleting AnalyticsExcludeSetup --beg
	@RequestMapping(value = "/DeleteAnalyticsExcludeSetup", method = RequestMethod.POST)
	public String deleteAnalyticsExcludeSetup(RedirectAttributes redirAttr,
			Model model, @ModelAttribute UiTestingSetupForm uiTestingSetupForm) {

		logger.debug("Entry: DeleteAnalyticsExcludeSetup");
		uiTestingSetupForm
				.setSetupTabNumber(UiTesting.UI_ANALYTICS_EXCLUDE_SETUP);
		AnalyticsExcludeLink analyticsExcludeLink;
		try {

			if (uiTestingSetupForm.getAnalyticsExcludeLinkId() != 0) {
				analyticsExcludeLink = new AnalyticsExcludeLink();
				analyticsExcludeLink
						.setAnalyticsExcludeLinkId(uiTestingSetupForm
								.getAnalyticsExcludeLinkId());

				if (excludeURLService
						.deleteAnalyticsExcludeSetup(analyticsExcludeLink)) {
					redirAttr
							.addFlashAttribute("Success",
									"Analytics Exclude setup details deleted successfully");
					uiTestingSetupForm = new UiTestingSetupForm();
					uiTestingSetupForm
							.setSetupTabNumber(UiTesting.UI_ANALYTICS_EXCLUDE_SETUP);
					redirAttr.addFlashAttribute("uiTestingSetupForm",
							uiTestingSetupForm);
				} else {
					redirAttr
							.addAttribute("Error",
									"Error occured while deleting Analytics Exclude setup details");
				}
			} else {
				logger.error("Error occured while deleting Analytics Exclude details: AnalyticsExcludeId is blank");
			}

		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while deleting Analytics Exclude Setup");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}

		logger.debug("Exit: DeleteAnalyticsExcludeSetup");
		return UiTesting.REDIRECT_VIEW;

	}

	// deleting AnalyticsExcludeSetup --End

	// updating AnalyticsExcludeSetup Changes--beg
	@RequestMapping(value = "/UpdateAnalyticsExcludeSetup", method = RequestMethod.POST)
	public String updateAnalyticsExcludeSetupDetails(
			RedirectAttributes redirAttr, Model model,
			@ModelAttribute UiTestingSetupForm uiTestingSetupForm) {
		logger.debug("Entry: UpdateAnalyticsExcludeSetup");

		AnalyticsExcludeLink analyticsExcludeLink = new AnalyticsExcludeLink();
		try {
			uiTestingSetupForm
					.setSetupTabNumber(UiTesting.UI_ANALYTICS_EXCLUDE_SETUP);
			redirAttr.addFlashAttribute("uiTestingSetupForm",
					uiTestingSetupForm);
			if (uiTestingSetupForm.getAnalyticsExcludeLinkId() != null
					&& uiTestingSetupForm.getAnalyticsExcludeLinkId() > 0) {
				analyticsExcludeLink
						.setAnalyticsExcludeLinkId(uiTestingSetupForm
								.getAnalyticsExcludeLinkId());
				analyticsExcludeLink = excludeURLService
						.getAnalyticsExcludeSetupModule(analyticsExcludeLink);

				analyticsExcludeLink.setApplicationId(uiTestingSetupForm
						.getApplicationId());
				analyticsExcludeLink.setEnvironmentId(uiTestingSetupForm
						.getEnvironmentId());
				analyticsExcludeLink.setExcludeLinktypeId(uiTestingSetupForm
						.getExcludeLinkType());
				analyticsExcludeLink.setExcludeLink(uiTestingSetupForm
						.getExcludeLink());

				if (excludeURLService.addExcludeLink(analyticsExcludeLink)) {
					redirAttr
							.addFlashAttribute("Success",
									"Analytics Exclude Link Setup details Updated Successfully");
					updateUiTestingSetupFormForAnalyticsExcludeLink(
							uiTestingSetupForm, analyticsExcludeLink);
				} else {
					redirAttr
							.addFlashAttribute("error",
									"Error occured while updating Analytics Exclude Setup details");
				}
			}
		} catch (DataIntegrityViolationException e) {
			logger.error("Exception :Duplicate Analytics Exclude Setup details entered. Please check the values.");
			redirAttr
					.addFlashAttribute("error",
							"Duplicate Analytics Exclude Setup details entered. Please check the values.");
		} catch (Exception exception) {
			logger.error("Exception :Unexpected error occured while updating Analytics Exclude Setup details");
			logger.error("Stack Trace :"
					+ ExceptionUtils.getStackTrace(exception));
			redirAttr
					.addFlashAttribute("error",
							"Unexpected error occured while updating Analytics Exclude Setup details");
		}

		logger.debug("Exit: UpdateAnalyticsExcludeSetup");
		return UiTesting.REDIRECT_VIEW;
	}

	// updating AnalyticsExcludeSetup Changes--End

	private void updateUiTestingSetupFormForAnalyticsExcludeLink(
			UiTestingSetupForm uiTestingSetupForm,
			AnalyticsExcludeLink analyticsExcludeLink) {
		uiTestingSetupForm
				.setExcludeLink(analyticsExcludeLink.getExcludeLink());
		uiTestingSetupForm.setExcludeLinkType(analyticsExcludeLink
				.getExcludeLinktypeId());
		uiTestingSetupForm.setApplicationId(analyticsExcludeLink
				.getApplicationId());
		uiTestingSetupForm.setEnvironmentId(analyticsExcludeLink
				.getEnvironmentId());
	}

	private boolean isAnalyticsExcludeSetupDetailsAvailable(
			Map<String, Object> asMap) {
		if (asMap.containsKey("applications")
				&& asMap.containsKey("environmentList")
				&& asMap.containsKey("environmentCategoryList")
				&& asMap.containsKey("excludeLinkTypeList")
				&& asMap.containsKey("analyticsExcludeLinkDetail")) {
			return true;
		}
		return false;
	}

	private void setUiApplicationDetails(RedirectAttributes redirAttr) {
		redirAttr.addFlashAttribute("applications",
				applicationService.getAllApplicationDetails());
		redirAttr.addFlashAttribute("environmentList",
				environmentService.getAllEnvironmentDetails());
		redirAttr.addFlashAttribute("environmentCategoryList",
				environmentService.getEnvironmentCategoryList());
	}

	private void setUiEnvironmentDetails(RedirectAttributes redirAttr) {
		redirAttr.addFlashAttribute("applications",
				applicationService.getAllApplicationDetails());
		redirAttr.addFlashAttribute("environmentList",
				environmentService.getAllEnvironmentDetails());

		redirAttr
				.addFlashAttribute(
						"environmentCategoryList",
						environmentService
								.getEnvironmentCategoryGroupRefByGroupId(UserServiceUtils
										.getCurrentUserGroupIdFromRequest(context)));
		redirAttr.addFlashAttribute("applicationEnvironmentCategoryList",
				applicationEnvironmentXrefService
						.getAllApplicationEnvironmentCategory());
		logger.debug("environmentService.getAllApplicationEnvironmentCategory() :"
				+ applicationEnvironmentXrefService
						.getAllApplicationEnvironmentCategory());
	}

	private void setUiEnvironmentDetailsToModel(Model model) {
		model.addAttribute("applications",
				applicationService.getAllApplicationDetails());

		model.addAttribute("environmentCategoryList", environmentService
				.getEnvironmentCategoryGroupRefByGroupId(UserServiceUtils
						.getCurrentUserGroupIdFromRequest(context)));
		model.addAttribute("environmentList",
				environmentService.getAllEnvironmentDetails());

		model.addAttribute("applicationEnvironmentCategoryList",
				applicationEnvironmentXrefService
						.getAllApplicationEnvironmentCategory());
	}

	private void setUiIncludeExcludeDetailsToModel(Model model) {
		model.addAttribute("includeURLList",
				includeUrlService.getIncludeURLList());
		model.addAttribute("excludeURLList",
				excludeURLService.getExcludeURLList());
		model.addAttribute("applicationEnvironmentCategoryList",
				applicationEnvironmentXrefService
						.getAllApplicationEnvironmentCategory());

		model.addAttribute("systemIncludeUrlList", CommonUtils
				.getStringAsList(CommonUtils.getPropertyFromPropertyFile(
						configProperties, "mint.systemincludeurl")));
		model.addAttribute("systemExcludeUrlList", CommonUtils
				.getStringAsList(CommonUtils.getPropertyFromPropertyFile(
						configProperties, "mint.systemexcludeurl")));
	}

	private void setUiLoginUserDetailsToModel(Model model) {
		model.addAttribute("applicationEnvironmentCategoryList",
				applicationEnvironmentXrefService
						.getAllApplicationEnvironmentCategory());
		model.addAttribute("loginList", loginUserService.getLoginUserList());
	}

	private void setUiModuleDetailsToModel(Model model) {
		model.addAttribute("applicationEnvironmentCategoryList",
				applicationEnvironmentXrefService
						.getAllApplicationEnvironmentCategory());
		model.addAttribute("applicationModuleList",
				applicationModuleService.getApplicationModuleList());
		model.addAttribute("moduleTypeList",
				applicationModuleService.getModuleTypeList());
	}

	private void setAnalyticsExcludeDetailsToModel(Model model) {
		model.addAttribute("excludeLinkTypeList",
				excludeURLService.getExcludeLinkType());
		model.addAttribute("analyticsExcludeLinkDetail",
				excludeURLService.getAnalyticsExcludeLinksetup());
		model.addAttribute("systemExcludeLinkList", CommonUtils
				.getStringAsList(CommonUtils.getPropertyFromPropertyFile(
						configProperties, "mint.systemexcludelink")));
	}

	private void setUiIncludeExcludeDetails(RedirectAttributes redirAttr) {
		redirAttr.addFlashAttribute("isPublic", 0);
		redirAttr.addFlashAttribute("includeURLList",
				includeUrlService.getIncludeURLList());
		redirAttr.addFlashAttribute("excludeURLList",
				excludeURLService.getExcludeURLList());
		redirAttr.addFlashAttribute("systemIncludeUrlList", CommonUtils
				.getStringAsList(CommonUtils.getPropertyFromPropertyFile(
						configProperties, "mint.systemincludeurl")));
		redirAttr.addFlashAttribute("systemExcludeUrlList", CommonUtils
				.getStringAsList(CommonUtils.getPropertyFromPropertyFile(
						configProperties, "mint.systemexcludeurl")));

		logger.info("excludeURLService.getExcludeURLList() :"
				+ excludeURLService.getExcludeURLList());
	}

	private void setUiModuleSetupDetails(RedirectAttributes redirAttr) {
		redirAttr.addFlashAttribute("applicationEnvironmentCategoryList",
				applicationEnvironmentXrefService
						.getAllApplicationEnvironmentCategory());
		redirAttr.addFlashAttribute("applicationModuleList",
				applicationModuleService.getApplicationModuleList());
		redirAttr.addFlashAttribute("moduleTypeList",
				applicationModuleService.getModuleTypeList());
	}

	private void setUiAnalyticsExcludeDetails(RedirectAttributes redirAttr) {
		redirAttr.addFlashAttribute("isPublic", 0);
		redirAttr.addFlashAttribute("excludeLinkTypeList",
				excludeURLService.getExcludeLinkType());
		redirAttr.addFlashAttribute("analyticsExcludeLinkDetail",
				excludeURLService.getAnalyticsExcludeLinksetup());
		redirAttr.addFlashAttribute("systemExcludeLinkList", CommonUtils
				.getStringAsList(CommonUtils.getPropertyFromPropertyFile(
						configProperties, "mint.systemexcludelink")));
	}

	@RequestMapping(value = "/getTestCases", method = RequestMethod.POST)
	public @ResponseBody
	List<ScriptDownloadForm> getTestCases(
			@RequestParam(value = "moduleId") int moduleId) {
		logger.debug("Entry: getTestCases, moduleId->" + moduleId);
		ApplicationModuleXref appModule = new ApplicationModuleXref();
		List<TransactionTestCase> testCaseList = null;
		List<ScriptDownloadForm> scriptDownloadList = new ArrayList<ScriptDownloadForm>();
		ScriptDownloadForm script = null;
		try {
			appModule.setApplicationModuleXrefId(moduleId);
			appModule = applicationModuleService
					.getApplicationModule(appModule);
			testCaseList = appModule.getTransactionTestCaseList();
			for(TransactionTestCase ts : testCaseList) {
				script  = new ScriptDownloadForm();
				script.setTestCase(ts);
				String filePath = FileDirectoryUtil.getAbsolutePath(ts
						.getTestCasePath(), FileDirectoryUtil
						.getTransactionTestCaseRootPath(configProperties));
				script.setTestCaseAvailable(MintFileUtils.isFileExists(filePath+FILE.JAVA));
				script.setHtmlAvailable(MintFileUtils.isFileExists(filePath+FILE.HTML));
				scriptDownloadList.add(script);
			}
		} catch (Exception e) {

		}
		logger.info("getTestCases :" + testCaseList);
		logger.debug("Exit: getTestCases, scheduleExectionId->" + testCaseList);
		return scriptDownloadList;
	}

	@RequestMapping(value = "/downloadTestCase", method = RequestMethod.POST)
	public void downloadTestCase(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "testCaseId") String testCaseId,
			@RequestParam(value = "typeId") String typeId) {
		logger.debug("Entry :downloadTestCase, imagePath->" + testCaseId);
		TransactionTestCase testCase = applicationModuleService
				.getTransactionTestCase(Integer.parseInt(testCaseId));
		String filePath = FileDirectoryUtil.getAbsolutePath(testCase
				.getTestCasePath(), FileDirectoryUtil
				.getTransactionTestCaseRootPath(configProperties));
		try {
			if (Integer.parseInt(typeId) == 3) {
				filePath = this.downloadAllTestCases(filePath, response,
						servletContext);
			} else if (Integer.parseInt(typeId) == 1) {
				filePath = filePath + FILE.JAVA;
			} else {
				filePath = filePath + FILE.HTML;
			}
			new MintFileUtils().writeFileToStream(filePath, response,
					servletContext);
			if (Integer.parseInt(typeId) == 3) {
				MintFileUtils.deleteFile(filePath);
			}

		} catch (Exception e) {
			logger.error("Exception in DownloadBrokenLinkReport");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: downloadTestCase");
	}
	
	@RequestMapping(value = "/DeleteTestCase", method = RequestMethod.POST)
	public @ResponseBody
	String DeleteTestCase(
			@RequestParam(value = "testCaseId", required = true) int testCaseId, @RequestParam(value = "moduleId", required = true) int moduleId,
			@RequestBody String text) {
		logger.debug("Entry: DeleteTestCase");
		ApplicationModuleXref applicationModuleXref = new ApplicationModuleXref();
		String message = "error";
		try {
			applicationModuleXref.setApplicationModuleXrefId(moduleId);
			applicationModuleXref = applicationModuleService
					.getApplicationModule(applicationModuleXref);
			if (testCaseId > 0
					&& applicationModuleXref.getApplicationModuleXrefId() > 0) {
				String filePathToDelete = null;
				for (TransactionTestCase tst : applicationModuleXref
						.getTransactionTestCaseList()) {
					if (testCaseId == tst.getTestCaseId()) {
						filePathToDelete = tst.getTestCasePath();
					}
				}
				if (applicationModuleService.deleteTestCaseByTestCaseId(
						testCaseId, filePathToDelete)) {
					message = "Success";
				} 
			}
		} catch (Exception e) {
			logger.error("Exception in DeleteTestCase.", e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: DeleteTestCase");
		return message;
	}
	
	private String downloadAllTestCases(String filePath,
			HttpServletResponse response, ServletContext servletContext) {
		logger.debug("Entry: downloadAllTestCases, filePath->" + filePath);
		String moudleFolderPath = StringUtils.substringBeforeLast(filePath,
				"\\");
		String zipFilePath = moudleFolderPath + "\\TestCasesZip.zip";
		String[] extensions = new String[] { "html", "java" };
		File dir = new File(moudleFolderPath);
		List<File> files = (List<File>) FileUtils.listFiles(dir, extensions,
				true);
		MintFileUtils.zipIt(zipFilePath, files);
		logger.debug("Entry: downloadAllTestCases, zipFilePath->" + zipFilePath);
		return zipFilePath;
	}
}