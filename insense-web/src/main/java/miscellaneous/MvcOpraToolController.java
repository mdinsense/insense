package miscellaneous;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cts.mint.common.entity.Users;
import com.cts.mint.common.utils.Constants.MVCOPRA;
import com.cts.mint.common.utils.TaskStatus;
import com.cts.mint.common.utils.TaskType;
import com.cts.mint.miscellaneous.entity.MvcOpraTaskSchedule;
import com.cts.mint.miscellaneous.entity.MvcOpraTaskScheduleDetail;
import com.cts.mint.miscellaneous.entity.TaskScheduleDetail;
import com.cts.mint.miscellaneous.model.MvcOpraResetTool;
import com.cts.mint.miscellaneous.model.TaskScheduleForm;
import com.cts.mint.miscellaneous.service.MvcOpraResetService;
import com.cts.mint.util.DateTimeUtil;
import com.cts.mint.util.DateUtil;

@Controller
public class MvcOpraToolController{
	@Autowired
	private HttpServletRequest context;

	@Autowired
	private MvcOpraResetService mvcOpraResetService;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private MessageSource configProperties;
	
	private static Logger logger = Logger.getLogger(MvcOpraToolController.class);
	private static final String DATE_FORMAT = "MM_dd_yyyy_HH_mm_ss";
	
	@RequestMapping(value = "/OpraMVCReset", method = RequestMethod.GET)
	public String mvcOPRAResetTool(Model model) {
		logger.debug("Entry: mvcOPRAResetTool");

		model.addAttribute("mvcOpraScheduleList",
				getMvcOpraStatusList());
		logger.debug("Exit: mvcOPRAResetTool");
		return MVCOPRA.VIEW;
	}

	@RequestMapping(value = "/MVCResetPINOrUserId", method = RequestMethod.POST)
	public String resetMvcPINOrUserId(RedirectAttributes redirAttr,
			Model model, @ModelAttribute MvcOpraResetTool opraResetTool) {
		logger.debug("Entry: resetMvcPINOrUserId, opraResetTool->"+opraResetTool);
		MvcOpraTaskSchedule taskSchedule = null;
		List<MvcOpraTaskScheduleDetail> taskScheduleDetails = null;
		Users user = null;
		String pinNumber = "";
		String resetType = "";
		String jobName = "";

		try {
			user = (Users)context.getSession().getAttribute("currentMintUser");

			Calendar calendar = Calendar.getInstance();
			pinNumber = opraResetTool.getCustomerPinNumber();
			resetType = opraResetTool.getResetType();

			jobName = "MVC_OPRA_"
					+ DateUtil.convertToString(calendar.getTime(), DATE_FORMAT);

			if (StringUtils.isNotBlank(pinNumber)
					&& StringUtils.isNotBlank(resetType)) {
				taskSchedule = new MvcOpraTaskSchedule();
				taskSchedule.setTaskScheduleName(jobName);
				taskSchedule.setTaskStatus(TaskStatus.PENDING.getStatusCode());
				taskSchedule.setTaskType(TaskType.MVC_OPRA_RESET.getTaskType());
				taskSchedule.setDateCreated(calendar.getTime());
				taskSchedule.setDateModified(calendar.getTime());
				taskSchedule.setUserId(user.getUserName());
				Integer taskScheduleId = mvcOpraResetService
						.saveTaskSchedule(taskSchedule);
				logger.info("Task Id : " + taskScheduleId);
				logger.info("Job name : " + jobName);

				taskScheduleDetails = new ArrayList<MvcOpraTaskScheduleDetail>();
				taskScheduleDetails.add(getTaskScheduleDetail(taskScheduleId,
						"pinnumber", pinNumber));
				taskScheduleDetails.add(getTaskScheduleDetail(taskScheduleId,
						"resetType", resetType));
				
				for (MvcOpraTaskScheduleDetail taskScheduleDetail : taskScheduleDetails) {
					mvcOpraResetService.saveTaskScheduleDetail(taskScheduleDetail);
				}

				redirAttr.addFlashAttribute("success",
						"MVC OPRA reset job submitted successfully. Job name "
								+ jobName);
			} else {
				redirAttr.addFlashAttribute("error",
						"Customer Pin number/User Id is null or empty");
			}
		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while submitting MVC reset job");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: resetMvcPINOrUserId");
		return MVCOPRA.REDIRECT_VIEW;
	}
	
	@RequestMapping(value = "/MVCOPERAReportDownload/{taskScheduleId}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<byte[]> download(
			@PathVariable("taskScheduleId") String taskScheduleId) {
		logger.debug("Entry: download");
		final HttpHeaders headers = new HttpHeaders();
		ResponseEntity<byte[]> responseEntity = null;
		ByteArrayOutputStream byteOutput = null;
		FileInputStream inputStream = null;

		try {
			MvcOpraTaskScheduleDetail fileAttr = mvcOpraResetService.getTaskSceduleDetail(
					Integer.valueOf(taskScheduleId), "reportLocation");
			if (fileAttr == null
					|| StringUtils.isBlank(fileAttr.getTaskAttributeValue())) {
				responseEntity = getDownloadErrorMessage();
			} else {
				File file = new File(fileAttr.getTaskAttributeValue());
				if (!file.exists()) {
					responseEntity = getDownloadErrorMessage();
				} else {
					byteOutput = new ByteArrayOutputStream();
					inputStream = new FileInputStream(file);
					byte buffer[] = new byte[1024];
					int bytesRead = 0;
					while ((bytesRead = inputStream.read(buffer)) > 0) {
						byteOutput.write(buffer, 0, bytesRead);
					}

					byte[] documentBody = byteOutput.toByteArray();
					headers.setContentType(new MediaType("application", "xls"));
					headers.set("Content-Disposition",
							"attachment; filename=\"" + file.getName() + "\"");
					headers.setContentLength(documentBody.length);
					responseEntity = new ResponseEntity<byte[]>(documentBody,
							headers, HttpStatus.OK);
				}
			}
		} catch (Exception e) {
			logger.error("Error occurred while downloading OPRA report", e);
			responseEntity = getDownloadErrorMessage();
		} finally {
			try {
				if (byteOutput != null) {
					byteOutput.close();
				}
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (Exception e) {
				logger.error("Error occurred while downloading OPRA report", e);
			}
		}
		logger.debug("Exit: download");
		return responseEntity;
	}
	
	private List<TaskScheduleForm> getMvcOpraStatusList() {
		List<MvcOpraTaskSchedule> taskScheduleList = new ArrayList<MvcOpraTaskSchedule>();
		List<TaskScheduleForm> taskScheduleFormList = new ArrayList<TaskScheduleForm>();
		TaskScheduleForm taskForm = null;
		try{
			taskScheduleList = mvcOpraResetService.getTaskScheduleList();
			for(MvcOpraTaskSchedule task : taskScheduleList) {
				taskForm = new TaskScheduleForm();
				taskForm.setTaskScheduleId(task.getTaskScheduleId());
				taskForm.setTaskStatusValue(TaskStatus.getTaskStatus(task.getTaskStatus()).getStatus());
				taskForm.setTaskTypeValue(TaskType.getTaskType(task.getTaskType()).getTaskDesc());
				taskForm.setDateCreated(DateTimeUtil.convertToString(task.getDateCreated(), DateUtil.DATE_TIME_FORMAT));
				taskForm.setUserId(task.getUserId());
				taskScheduleFormList.add(taskForm);
			}
		}catch(Exception e){
			logger.error("Exception :getMvcOpraStatusList");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		return taskScheduleFormList;
	}
	
	private MvcOpraTaskScheduleDetail getTaskScheduleDetail(Integer taskScheduleId,
			String attrName, String attValue) {
		Calendar calendar = Calendar.getInstance();
		MvcOpraTaskScheduleDetail taskScheduleDetail = new MvcOpraTaskScheduleDetail();
		taskScheduleDetail.setTaskAttributeName(attrName);
		taskScheduleDetail.setTaskAttributeValue(attValue);
		taskScheduleDetail.setDateCreated(calendar.getTime());
		taskScheduleDetail.setDateModified(calendar.getTime());
		taskScheduleDetail.setUserId(((Users)context.getSession()
				.getAttribute("currentMintUser")).getUserName());
		taskScheduleDetail.setTaskScheduleId(taskScheduleId);
		return taskScheduleDetail;
	}
	
	private ResponseEntity<byte[]> getDownloadErrorMessage() {
		HttpHeaders headers = new HttpHeaders();
		String msg = "ERROR: Could not find the file specified.";
		headers.setContentType(MediaType.TEXT_PLAIN);
		return new ResponseEntity<byte[]>(msg.getBytes(), headers,
				HttpStatus.NOT_FOUND);
	}
}