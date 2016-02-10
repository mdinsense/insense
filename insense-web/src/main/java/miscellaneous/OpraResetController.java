package miscellaneous;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
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

import com.cts.mint.common.MintFileUtils;
import com.cts.mint.common.entity.Users;
import com.cts.mint.common.utils.CommonUtils;
import com.cts.mint.common.utils.Constants.OPRA;
import com.cts.mint.common.utils.FileDirectoryUtil;
import com.cts.mint.common.utils.TaskStatus;
import com.cts.mint.common.utils.TaskType;
import com.cts.mint.miscellaneous.entity.TaskSchedule;
import com.cts.mint.miscellaneous.entity.TaskScheduleDetail;
import com.cts.mint.miscellaneous.model.OpraResetTool;
import com.cts.mint.miscellaneous.model.TaskScheduleForm;
import com.cts.mint.miscellaneous.service.OpraResetService;
import com.cts.mint.miscellaneous.util.MiscUtil;
import com.cts.mint.util.DateTimeUtil;
import com.cts.mint.util.DateUtil;

@Controller
public class OpraResetController {

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
	private OpraResetService opraResetService;

	
	private static final String DATE_FORMAT = "MM_dd_yyyy_HH_mm_ss";
	
	private static Logger logger = Logger
			.getLogger(OpraResetController.class);

		
    @RequestMapping(value = "/OpraReset", method = RequestMethod.GET)
    public String OpraResetGet(Model model) {
           logger.debug("Entry: OpraResetGet");
           
           model.addAttribute("scheduleList", getTaskScheduleList());
           logger.debug("Exit: OpraResetGet");
           return OPRA.VIEW;
    }
    
    @RequestMapping(value = "/OpraReset", method = RequestMethod.POST)
    public String OpraReset(Model model) {
           logger.debug("Entry: OpraReset");
           logger.debug("Exit: OpraReset");
           return OPRA.REDIRECT_VIEW;
    }
    
	private List<TaskScheduleForm> getTaskScheduleList(){
		logger.debug("Entry: getTaskScheduleList");
		List<TaskSchedule> taskScheduleList = new ArrayList<TaskSchedule>();
		List<TaskScheduleForm> taskScheduleFormList = new ArrayList<TaskScheduleForm>();
		TaskScheduleForm taskForm = null;
		try{
			taskScheduleList = opraResetService.getTaskScheduleList();
			for(TaskSchedule task : taskScheduleList) {
				taskForm = new TaskScheduleForm();
				taskForm.setTaskScheduleId(task.getTaskScheduleId());
				taskForm.setTaskStatusValue(TaskStatus.getTaskStatus(task.getTaskStatus()).getStatus());
				taskForm.setTaskTypeValue(TaskType.getTaskType(task.getTaskType()).getTaskDesc());
				taskForm.setDateCreated(DateTimeUtil.convertToString(task.getDateCreated(), DateUtil.DATE_TIME_FORMAT));
				taskForm.setUserId(task.getUserId());
				taskScheduleFormList.add(taskForm);
			}
		}catch(Exception e){
			logger.error("Exception :GetTaskScheduleList");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}

		logger.debug("Exit: getTaskScheduleList");
		return taskScheduleFormList;
	}
    
	@RequestMapping(value = "/ResetPINOrUserId", method = RequestMethod.POST)
	public String resetPINOrUserId(RedirectAttributes redirAttr,
			Model model, @ModelAttribute OpraResetTool opraResetTool) {
		logger.debug("Entry: resetPINOrUserId, opraResetTool->"+opraResetTool);
		String pinNumber = "";
		String resetType = "";
		String jobName = "";
		TaskSchedule taskSchedule = null;
		List<TaskScheduleDetail> taskScheduleDetails = null;
		try {
			pinNumber = opraResetTool.getCustomerPinNumber();
			resetType = opraResetTool.getResetType();
			Calendar calendar = Calendar.getInstance();
			jobName = "OPRA_" + DateUtil.convertToString(calendar.getTime(), DATE_FORMAT);
			Users currentUser = (Users) context.getSession().getAttribute("currentMintUser");
			if (StringUtils.isNotBlank(pinNumber) && StringUtils.isNotBlank(resetType) && StringUtils.isNotBlank(opraResetTool.getCustomerPinNumber())) {
				taskSchedule = new TaskSchedule();
				taskSchedule.setTaskScheduleName(jobName);
				taskSchedule.setTaskStatus(TaskStatus.PENDING.getStatusCode());
				taskSchedule.setTaskType(TaskType.OPRA_TASK.getTaskType());
				taskSchedule.setDateCreated(calendar.getTime());
				taskSchedule.setDateModified(calendar.getTime());
				taskSchedule.setUserId(currentUser.getUserName());
				Integer taskScheduleId = opraResetService
							.saveTaskSchedule(taskSchedule);
				taskScheduleDetails = new ArrayList<TaskScheduleDetail>();
				taskScheduleDetails.add(MiscUtil.getTaskScheduleDetail(taskScheduleId,
						"pinnumber", pinNumber, currentUser.getUserName()));
				taskScheduleDetails.add(MiscUtil.getTaskScheduleDetail(taskScheduleId,
						"resetType", resetType, currentUser.getUserName()));
				
				for ( TaskScheduleDetail taskScheduleDetail: taskScheduleDetails){
					opraResetService.saveTaskScheduleDetail(taskScheduleDetail);
				}
				
				
				redirAttr.addFlashAttribute("success", "OPRA reset job submitted successfully. Job name " + jobName);
			}  else {
				redirAttr.addFlashAttribute("error", "Customer Pin number/User Id is null or empty");
			}
			
		} catch (Exception e) {
			logger.error("Exception in resetPINOrUserId.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		
		logger.debug("Exit: resetPINOrUserId");
		 return OPRA.REDIRECT_VIEW;
		
	}

	
	@RequestMapping(value = "/BulkResetPINOrUserId", method = RequestMethod.POST)
	public String resetPINOrUserIdForGroup(RedirectAttributes redirAttr,
			Model model, @ModelAttribute OpraResetTool opraResetTool) {
		logger.debug("Entry: BulkResetPINOrUserId, opraResetTool->"+opraResetTool);
		String fileName = "";
		String destinationPath = "";
		String fileExtension = "";
		String jobName = "";
		TaskSchedule taskSchedule = null;
		List<TaskScheduleDetail> taskScheduleDetails = null;
		try {
			File newFile = new File(opraResetTool.getPinResetFile().getOriginalFilename());
			fileName = newFile.getName();

			String resultsBaseDirectory = CommonUtils.getPropertyFromPropertyFile(configProperties, "mint.opera.reportpath");
			resultsBaseDirectory = FileDirectoryUtil.getAbsolutePath(resultsBaseDirectory, FileDirectoryUtil.getMintRootDirectory(configProperties));
			MintFileUtils.createDirectory(resultsBaseDirectory);
			destinationPath = resultsBaseDirectory
					+ File.separator + fileName;
			fileExtension = FilenameUtils.getExtension(newFile.getName());
			Users currentUser = (Users) context.getSession().getAttribute("currentMintUser");
			if ("txt".equalsIgnoreCase(fileExtension)) {
				if (!MiscUtil.createDestinationFile(opraResetTool, destinationPath)) {
					model.addAttribute("error",
							"Unabel to save the uploaded file.");
				} else {
					Calendar calendar = Calendar.getInstance();
					jobName = "OPRA_"+ DateUtil.convertToString(calendar.getTime(),DATE_FORMAT);

					taskSchedule = new TaskSchedule();
					taskSchedule.setTaskScheduleName(jobName);
					taskSchedule.setTaskStatus(TaskStatus.PENDING
							.getStatusCode());
					taskSchedule.setTaskType(TaskType.OPRA_GROUP_TYPE
							.getTaskType());
					taskSchedule.setDateCreated(calendar.getTime());
					taskSchedule.setDateModified(calendar.getTime());
					taskSchedule.setUserId(currentUser.getUserName());
					Integer taskScheduleId = opraResetService
							.saveTaskSchedule(taskSchedule);
					logger.info("Task Id : " + taskScheduleId);
					logger.info("Job name : " + jobName);

					taskScheduleDetails = new ArrayList<TaskScheduleDetail>();
					taskScheduleDetails.add(MiscUtil.getTaskScheduleDetail(
							taskScheduleId, "sourceFile", destinationPath, currentUser.getUserName()));
					taskScheduleDetails.add(MiscUtil.getTaskScheduleDetail(
							taskScheduleId, "resetType",
							opraResetTool.getBulkResetType(), currentUser.getUserName()));
					
					for ( TaskScheduleDetail taskScheduleDetail: taskScheduleDetails){
						opraResetService.saveTaskScheduleDetail(taskScheduleDetail);
					}
		
					model.addAttribute("success",
							"OPRA reset job submitted successfully. Job name "
									+ jobName);
				}
			}
		} catch (Exception e) {
			logger.error("Exception in BulkResetPINOrUserId.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: BulkResetPINOrUserId");
		 return OPRA.REDIRECT_VIEW;
		
	}
	
	@RequestMapping(value = "/OPERAReportDownload/{taskScheduleId}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<byte[]> download(
			@PathVariable("taskScheduleId") String taskScheduleId) {
		logger.debug("Entry: download");
		final HttpHeaders headers = new HttpHeaders();
		ResponseEntity<byte[]> responseEntity = null;
		ByteArrayOutputStream byteOutput = null;
		FileInputStream inputStream = null;

		try {
			TaskScheduleDetail fileAttr = opraResetService.getTaskSceduleDetail(
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
	
	private ResponseEntity<byte[]> getDownloadErrorMessage() {
		HttpHeaders headers = new HttpHeaders();
		String msg = "ERROR: Could not find the file specified.";
		headers.setContentType(MediaType.TEXT_PLAIN);
		return new ResponseEntity<byte[]>(msg.getBytes(), headers,
				HttpStatus.NOT_FOUND);
	}
}