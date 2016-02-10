package com.ensense.insense.services.uiadmin.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.cts.mint.common.MintFileUtils;
import com.cts.mint.common.dao.HomeDAO;
import com.cts.mint.common.utils.CommonUtils;
import com.cts.mint.common.utils.Constants;
import com.cts.mint.common.utils.Constants.FILE;
import com.cts.mint.common.utils.Constants.KEYS;
import com.cts.mint.common.utils.FileDirectoryUtil;
import com.cts.mint.common.utils.TestCaseFileValidationUtil;
import com.cts.mint.uitesting.dao.ApplicationModuleDAO;
import com.cts.mint.uitesting.entity.ApplicationModuleXref;
import com.cts.mint.uitesting.entity.ModuleType;
import com.cts.mint.uitesting.entity.Suit;
import com.cts.mint.uitesting.entity.TransactionTestCase;
import com.cts.mint.uitesting.service.ApplicationModuleService;

@Service
public class ApplicationModuleServiceImpl implements ApplicationModuleService {
	private static Logger logger = Logger
			.getLogger(ApplicationModuleServiceImpl.class);

	@Autowired
	ApplicationModuleDAO applicationModuleDAO;
	
	@Autowired
	private MessageSource configProperties;
	
	@Autowired
	private HomeDAO homeDao;
	
	@Override
	@Transactional
	public List<ApplicationModuleXref> getApplicationModuleList() {
		logger.info("Entry: getApplicationModuleList");
		return applicationModuleDAO.getApplicationModuleList();
	}

	@Override
	@Transactional
	public boolean addApplicationModule(ApplicationModuleXref applicationModuleXref) {
		logger.info("Entry: insertApplicationModule");
		boolean isAdded = applicationModuleDAO.addApplicationModule(applicationModuleXref);
		if(isAdded) {
			if(applicationModuleXref.getModuleTypeId() == Constants.ModuleType.TRANSACTION) {
				for(TransactionTestCase testCase : applicationModuleXref.getTransactionTestCaseList()) {
					testCase.setApplicationModuleXrefId(applicationModuleXref.getApplicationModuleXrefId());
					applicationModuleDAO.saveTransactionTestCase(testCase);
				}
			}
		}
		return isAdded;
	}

	@Override
	@Transactional
	public ApplicationModuleXref getApplicationModule(
			ApplicationModuleXref applicationModuleXref) {
		logger.info("Entry: getApplicationModule");
		return applicationModuleDAO.getApplicationModule(applicationModuleXref);
	}

	@Override
	@Transactional
	public boolean updateApplicationModule(
			ApplicationModuleXref applicationModuleXref) {
		logger.info("Entry: updateApplicationModule");
		boolean isAdded = applicationModuleDAO.updateApplicationModule(applicationModuleXref);
		if(isAdded) {
			if(applicationModuleXref.getModuleTypeId() == Constants.ModuleType.TRANSACTION) {
				for(TransactionTestCase testCase : applicationModuleXref.getTransactionTestCaseList()) {
					testCase.setApplicationModuleXrefId(applicationModuleXref.getApplicationModuleXrefId());
					applicationModuleDAO.saveTransactionTestCase(testCase);
				}
			}
		}
		return isAdded;
	}

	@Override
	@Transactional
	public boolean deleteApplicationModule(ApplicationModuleXref applicationModuleXref) {
		logger.info("Entry: deleteApplicationModule");
		boolean isDeleted;
		String filePathToDelete = applicationModuleXref.getTestCaseDirectory();
		int moduleTypeId = applicationModuleXref.getModuleTypeId();
		isDeleted = applicationModuleDAO.deleteApplicationModule(applicationModuleXref);
		if(isDeleted && moduleTypeId == Constants.ModuleType.TRANSACTION) {
			filePathToDelete = FileDirectoryUtil.getAbsolutePath(filePathToDelete,
					FileDirectoryUtil.getTransactionTestCaseRootPath(configProperties));
			FileUtils.deleteQuietly(new File(filePathToDelete));
		}
		return isDeleted;
	}
	
	@Override
	@Transactional
	public boolean deleteTestCaseByTestCaseId(int testCaseId, String filePathToDelete) {
		logger.info("Entry: deleteTestCaseByTestCaseId");
		boolean isDeleted = false;
		isDeleted = applicationModuleDAO.deleteTestCaseByTestCaseId(testCaseId);
		if(isDeleted) {
			filePathToDelete = FileDirectoryUtil.getAbsolutePath(filePathToDelete + FILE.CLASS,
					FileDirectoryUtil.getTransactionTestCaseRootPath(configProperties));
			MintFileUtils.deleteFile(filePathToDelete);
			filePathToDelete = filePathToDelete.replace(FILE.CLASS, FILE.JAVA);
			MintFileUtils.deleteFile(filePathToDelete);
			filePathToDelete = filePathToDelete.replace(FILE.JAVA, FILE.HTML);
			MintFileUtils.deleteFile(filePathToDelete);
		}
		logger.info("Exit: deleteTestCaseByTestCaseId");
		return isDeleted;
	}

	@Override
	@Transactional
	public List<ModuleType> getModuleTypeList() {
		logger.info("Entry: getModuleTypeList");
		return applicationModuleDAO.getModuleTypeList();
	}

	@Override
	@Transactional
	public List<ApplicationModuleXref> getModuleListForApplicationAndCategory(
			int applicationId, int categoryId) {
		logger.info("Entry: getModuleListForApplicationAndCategory");
		return applicationModuleDAO.getModuleListForApplicationAndCategory(applicationId, categoryId);
	}

	@Override
	@Transactional
	public boolean confirmModuleIdForALL(ApplicationModuleXref applicationModuleXref){
		logger.info("Entry: confirmModuleIdForALL");
		return applicationModuleDAO.confirmModuleIdForALL(applicationModuleXref);
	}
	
	@Override
	@Transactional
	public List<ApplicationModuleXref> getTransactionalModulesForSuit(int suitId) {
		logger.info("Entry: getModulesForSuit");
		Suit suit = homeDao.getSavedSuits(suitId);
		List<ApplicationModuleXref> modules = applicationModuleDAO.getModulesForSuit(suit.getModuleId());
		List<ApplicationModuleXref> transactionModules = new ArrayList<ApplicationModuleXref>();
		for(ApplicationModuleXref module : modules) {
			if(module.getModuleTypeId() == Constants.ModuleType.TRANSACTION) {
				transactionModules.add(module);
			}
		}
		if(transactionModules.size()<=0) {
			transactionModules = null;
		}
		return transactionModules;
	}

	@Override
	@Transactional
	public TransactionTestCase getTransactionTestCase(int testCaseId) {
		logger.info("Entry: getTransactionTestCase");
		return applicationModuleDAO.getTransactionTestCase(testCaseId);
	}

	@Override
	@Transactional
	public boolean updateTransactionTestCase(TransactionTestCase transactionTestCase, CommonsMultipartFile javafile, CommonsMultipartFile htmlfile) {
		logger.info("Entry: updateTransactionTestCase");
		boolean isUpdated = false;
		if(this.uploadTransactionScript(transactionTestCase, javafile, htmlfile)) {
			isUpdated = applicationModuleDAO.saveTransactionTestCase(transactionTestCase);
		}
		return isUpdated;
	}
	
	private boolean uploadTransactionScript(TransactionTestCase transactionTestCase, CommonsMultipartFile newFile, CommonsMultipartFile htmlfile) {
		String packageDirectory = "";
		String scriptDirectory = "";
		String packageName = "";
		String jarLocation = "";
		String destinationFileName = "";
		String testCaseFileRelativePath = "";
		String testCaseRootPath = "";
		boolean status = false;
		File destinationFile;
		TransactionTestCase testCase = new TransactionTestCase();
		try {	
			packageDirectory = CommonUtils.getPropertyFromPropertyFile(
					configProperties,
					"mint.transactional.testcase.packageDirectory");
			if (StringUtils.isEmpty(packageDirectory)) {
				logger.error("Invalid test case directory.");
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
			scriptDirectory = packageDirectory + StringUtils.substringBeforeLast(transactionTestCase.getTestCasePath(),"\\");
			destinationFileName = scriptDirectory + "\\" +newFile.getOriginalFilename();
			FileDirectoryUtil.createDirectories(scriptDirectory);
			destinationFile = new File(destinationFileName);
			logger.debug("destinationFileName :" + destinationFileName);
			if (!this.createDestinationFile(newFile, destinationFileName)) {
				return false;
			}
			packageName += StringUtils.replaceChars(StringUtils.substringBeforeLast(transactionTestCase.getTestCasePath(),"\\"), "\\", ".");
			status = new TestCaseFileValidationUtil().validateFile(new File(
					newFile.getOriginalFilename()), destinationFile, packageName, jarLocation);
			if (!status) {
				return false;
			}
			testCaseFileRelativePath = StringUtils.substringBeforeLast(destinationFile.getAbsolutePath(), KEYS.DOT);
			testCaseRootPath = new File(
					FileDirectoryUtil
							.getTransactionTestCaseRootPath(configProperties))
					.getAbsolutePath();
			testCaseFileRelativePath = StringUtils.substringAfterLast(
					testCaseFileRelativePath, testCaseRootPath);
			testCase.setTestCasePath(testCaseFileRelativePath);
			if(htmlfile != null) {
				this.uploadTransactionHtmlScript(destinationFileName.replace(FILE.JAVA, FILE.HTML), htmlfile);
			}
			status = true;
			
		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while uploading login user script.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
			status = false;
		}
		return status;
	}

	private boolean uploadTransactionHtmlScript(String destFilePath, CommonsMultipartFile newFile) {
		boolean status = false;
		logger.info("HTMLFile :" + destFilePath);
		try {
			if (!this.createDestinationFile(newFile, destFilePath)) {
				return false;
			}
			status = true;
		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while uploading login user script.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
			status = false;
		}
		return status;
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

}
