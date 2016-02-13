package com.ensense.insense.services.uiadmin.impl.mintv4;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cts.mint.common.MintFileUtils;
import com.cts.mint.common.utils.CommonUtils;
import com.cts.mint.common.utils.TestCaseFileValidationUtil;
import com.cts.mint.uitesting.dao.mintv4.TestSuitDAO;
import com.cts.mint.uitesting.entity.mintv4.TestEnvironment;
import com.cts.mint.uitesting.entity.mintv4.TestLoginDetail;
import com.cts.mint.uitesting.entity.mintv4.TestSuit;
import com.cts.mint.uitesting.entity.mintv4.TestSuitBrowserConfig;
import com.cts.mint.uitesting.entity.mintv4.TestSuitCompareConfigXref;
import com.cts.mint.uitesting.entity.mintv4.TestSuitIncludeExcludeXref;
import com.cts.mint.uitesting.entity.mintv4.TestSuitRemoveTagOrTextXref;
import com.cts.mint.uitesting.entity.mintv4.TestSuitTextImageXref;
import com.cts.mint.uitesting.model.mintv4.TestSuitDetails;
import com.cts.mint.uitesting.model.mintv4.TestSuitForm;
import com.cts.mint.uitesting.service.mintv4.TestSuitService;
import com.cts.mint.util.FileDirectoryUtil;

@Service
public class TestSuitServiceImpl implements TestSuitService {
	private static Logger logger = Logger.getLogger(TestSuitServiceImpl.class);

	@Autowired
	private MessageSource configProperties;

	@Autowired
	private TestSuitDAO testSuitDAO;

	@Override
	@Transactional
	public boolean saveSuit(TestSuitForm testSuitForm) {

		logger.debug("Entry : saveSuit");
		boolean isSaveComplete = false;
		TestSuit testSuit = new TestSuit();
		TestEnvironment testEnvironment = new TestEnvironment();
		TestLoginDetail testLoginDetail = new TestLoginDetail();
		TestSuitBrowserConfig testSuitBrowserConfig = new TestSuitBrowserConfig();
		TestSuitCompareConfigXref testSuitCompareConfigXref = new TestSuitCompareConfigXref();

		try {
			if (testSuitForm != null) {

				testSuit.setSuitName(testSuitForm.getSuitName());
				testSuit.setApplicationName(testSuitForm.getApplicationName());
				testSuitDAO.saveSuit(testSuit);

				testEnvironment.setSuitId(testSuit.getSuitId());
				testEnvironment.setEnvironmentName(testSuitForm
						.getEnvironmentName());
				testEnvironment.setLoginOrHomeUrl(testSuitForm
						.getLoginOrHomeUrl());
				testEnvironment.setSecureSite(testSuitForm.isSecureFlag());
				if (testEnvironment.isSecureSite()) {
					this.uploadLoginScript(testEnvironment, testSuitForm);
					testLoginDetail.setSuitId(testSuit.getSuitId());
					testLoginDetail.setLoginId(testSuitForm.getLoginId());
					testLoginDetail.setPassword(testSuitForm.getPassword());
					testLoginDetail.setRsaEnabled(testSuitForm.isRsaSecured());
					testLoginDetail.setSecurityAnswer(testSuitForm
							.getSecurityAnswer());
					testSuitDAO.saveLoginDetails(testLoginDetail);
				}
				testEnvironment.setStaticUrl(testSuitForm.isStaticFlag());
				if (testEnvironment.isStaticUrl()) {
					this.uploadStaticUrlFile(testEnvironment, testSuitForm);
					testEnvironment.setUrlColumnPosition(testSuitForm
							.getColumnPosition());
				}
		
				testSuitDAO.saveEnvironment(testEnvironment);

				

				testSuitBrowserConfig.setSuitId(testSuit.getSuitId());
				testSuitBrowserConfig.setBrowserTypeId(testSuitForm
						.getBrowserTypeId());
				testSuitDAO.saveTestSuitBrowserConfig(testSuitBrowserConfig);

				testSuitCompareConfigXref.setSuitId(testSuit.getSuitId());
				testSuitCompareConfigXref.setHtmlCompare(testSuitForm
						.isHtmlCompare());
				testSuitCompareConfigXref.setTextCompare(testSuitForm
						.isTextCompare());
				testSuitCompareConfigXref.setScreenCompare(testSuitForm
						.isScreenCompare());
				testSuitDAO
						.saveTestSuitCompareConfigXref(testSuitCompareConfigXref);

				if (this.saveFindTextOrImageList(testSuit.getSuitId(),
						testSuitForm.getTextName(), testSuitForm.isFindText(),
						testSuitForm.getImageName(),
						testSuitForm.isFindImage(), "TEXTORIMAGE")) {

				}

				if (this.saveIncludeOrExcludeList(testSuit.getSuitId(),
						testSuitForm.getRemoveTag(), testSuitForm.isTag(),
						testSuitForm.getRemoveText(), testSuitForm.isText(),
						"INCLUDEOREXCLUDE")) {

				}

 				if (this.saveRemoveTagOrTextList(testSuit.getSuitId(),
						testSuitForm.getRemoveTag(), testSuitForm.isTag(),
						testSuitForm.getRemoveText(), testSuitForm.isText(),
						"TAGORTEXT")) {

				}
 				testSuitForm.setSuitId(testSuit.getSuitId());
				isSaveComplete = true;
			}
		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while save Suit");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit : saveSuit");
		return isSaveComplete;
	}
	
	@Override
	@Transactional
	public TestSuitDetails getTestSuitDetails(int suitId) {
		logger.debug("Entry and Exit : getTestSuitDetails");
		return testSuitDAO.getTestSuitDetails(suitId);
	}
	

	@Override
	public List<TestSuitDetails> getAllTestSuitDetails() {
		logger.debug("Entry and Exit : getAllTestSuitDetails");
		return testSuitDAO.getAllTestSuitDetails();
	}
	
	private boolean saveIncludeOrExcludeList(int suitId, String[] include,
			boolean isInclude, String[] exclude, boolean isExclude,
			String includeOrExclude) {

		logger.debug("Entry: saveIncludeOrExcludeList");
		boolean isSaved = false;
		try {
			isSaved = saveFindTextOrImageList(suitId, include, isInclude,
					exclude, isExclude, includeOrExclude);
		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while getting saveIncludeOrExcludeList"
					+ e);
		}
		logger.debug("Exit: saveIncludeOrExcludeList");
		return isSaved;
	}

	private boolean saveRemoveTagOrTextList(int suitId, String[] removeTag,
			boolean istag, String[] removeText, boolean isText, String tagOrText)
			throws Exception {
		logger.debug("Entry: saveRemoveTagOrTextList");
		boolean isSaved = false;
		try {
			isSaved = saveFindTextOrImageList(suitId, removeTag, istag,
					removeText, isText, tagOrText);
		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while getting saveRemoveTagOrTextList"
					+ e);
		}
		logger.debug("Exit: saveRemoveTagOrTextList");
		return isSaved;
	}

	private boolean saveFindTextOrImageList(int suitId, String[] textName,
			boolean istext, String[] imageName, boolean isImage,
			String textOrImage) throws Exception {
		logger.debug("Entry: saveFindTextOrImageList");
		StringBuffer tempTextBuffer = new StringBuffer();
		StringBuffer tempImageBuffer = new StringBuffer();
		ArrayList<String> findTextList = new ArrayList<String>();
		ArrayList<String> findImageList = new ArrayList<String>();
		boolean isSaved = false;
		try {
			// Consolidated and ArrayList generation for findText values
			if (textName != null && istext) {
				for (int index = 0; index < textName.length; index++) {
					tempTextBuffer = tempTextBuffer.append(textName[index])
							.append(",");
				}

				tempTextBuffer.deleteCharAt(tempTextBuffer.lastIndexOf(","));
				findTextList = this.getFindTextOrImageList(tempTextBuffer);

				if (!findTextList.isEmpty()) {
					Iterator<String> itr = findTextList.iterator();
					while (itr.hasNext()) {
						if (textOrImage.equals("TEXTORIMAGE")) { // Find Text
							this.saveTextImageXref(suitId, istext, false,
									itr.next());
						} else if (textOrImage.equals("TAGORTEXT")) { // Remove
																		// Tag
							this.saveTagTextXref(suitId, istext, false,
									itr.next());
						} else if (textOrImage.equals("INCLUDEOREXCLUDE")) { // Include
							this.saveIncludeExcludeXref(suitId, istext, false,
									itr.next());
						}

					}
				}

			}

			// Consolidated and ArrayList generation for findImage values
			if (imageName != null && isImage) {
				for (int index = 0; index < imageName.length; index++) {
					tempImageBuffer = tempImageBuffer.append(imageName[index])
							.append(",");
				}

				tempImageBuffer.deleteCharAt(tempImageBuffer.lastIndexOf(","));
				findImageList = this.getFindTextOrImageList(tempImageBuffer);

				if (!findImageList.isEmpty()) {
					Iterator<String> itr = findImageList.iterator();
					while (itr.hasNext()) {
						if (textOrImage.equals("TEXTORIMAGE")) {// Find Image
							this.saveTextImageXref(suitId, false, isImage,
									itr.next());
						} else if (textOrImage.equals("TAGORTEXT")) { // Remove
																		// Text
							this.saveTagTextXref(suitId, false, isImage,
									itr.next());
						} else if (textOrImage.equals("INCLUDEOREXCLUDE")) { // Exclude
							this.saveIncludeExcludeXref(suitId, false, isImage,
									itr.next());
						}

					}
				}

			}
			isSaved = true;
		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while getting saveFindTextOrImageList"
					+ e);
			isSaved = false;
		}
		logger.debug("Exit: saveFindTextOrImageList");
		return isSaved;
	}

	private ArrayList<String> getFindTextOrImageList(
			StringBuffer tempTextOrImageBuffer) throws Exception {
		logger.debug("Entry: getFindTextOrImageList");
		int tempTextCount = 0;
		StringBuffer textOrImageBuffer = new StringBuffer();
		ArrayList<String> findTextOrImageList = new ArrayList<String>();
		try {
			for (String tempTextOrImageValue : tempTextOrImageBuffer.toString()
					.split(",")) {
				tempTextCount = tempTextCount + tempTextOrImageValue.length()
						+ 1;
				if (tempTextCount <= 250) {
					textOrImageBuffer = textOrImageBuffer.append(
							tempTextOrImageValue).append(",");
				} else {
					findTextOrImageList.add(textOrImageBuffer.toString());
					tempTextCount = 0;
					tempTextCount = tempTextOrImageValue.length() + 1;
					textOrImageBuffer.setLength(0);
					textOrImageBuffer.append(tempTextOrImageValue).append(",");
				}
			}
			if (textOrImageBuffer.length() != 0) {
				textOrImageBuffer.deleteCharAt(textOrImageBuffer.length() - 1);
				findTextOrImageList.add(textOrImageBuffer.toString());
				tempTextCount = 0;
				textOrImageBuffer.setLength(0);
			}
		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while getting getFindTextList"
					+ e);
		}
		logger.debug("Entry: getFindTextOrImageList");
		return findTextOrImageList;
	}

	private boolean saveTextImageXref(int suitId, Boolean isText,
			Boolean isImage, String textOrImageName) throws Exception {
		boolean saved = false;
		TestSuitTextImageXref suitTextImageXref = new TestSuitTextImageXref();
		suitTextImageXref.setText(isText);
		suitTextImageXref.setImage(isImage);
		suitTextImageXref.setSuitId(suitId);
		suitTextImageXref.setTextOrImageName(textOrImageName);

		saved = testSuitDAO.saveTestSuitTextImageXref(suitTextImageXref);
		return saved;
	}

	private boolean saveTagTextXref(int suitId, Boolean istag, Boolean isText,
			String tagOrTextName) throws Exception {
		boolean saved = false;
		TestSuitRemoveTagOrTextXref suitRemoveTagOrTextXref = new TestSuitRemoveTagOrTextXref();
		suitRemoveTagOrTextXref.setText(isText);
		suitRemoveTagOrTextXref.setTag(istag);
		suitRemoveTagOrTextXref.setSuitId(suitId);
		suitRemoveTagOrTextXref.setTagOrText(tagOrTextName);

		saved = testSuitDAO.saveTestSuitTagTextXref(suitRemoveTagOrTextXref);
		return saved;
	}

	private boolean saveIncludeExcludeXref(int suitId, Boolean isInclude,
			Boolean isExclude, String includeOrExcludeUrl) throws Exception {
		boolean saved = false;
		TestSuitIncludeExcludeXref suitIncludeExcludeXref = new TestSuitIncludeExcludeXref();
		suitIncludeExcludeXref.setIncludeUrl(isInclude);
		suitIncludeExcludeXref.setExcludeUrl(isExclude);
		suitIncludeExcludeXref.setSuitId(suitId);
		suitIncludeExcludeXref.setIncludeOrExcludeUrl(includeOrExcludeUrl);

		saved = testSuitDAO
				.saveTestSuitIncludeExcludeXref(suitIncludeExcludeXref);
		return saved;
	}

	private boolean uploadLoginScript(TestEnvironment testEnvironment,
			TestSuitForm testSuitForm) {
		boolean isUploaded = false;
		String loginScriptPath = FileDirectoryUtil
				.getLoginScriptPath(configProperties);
		File fileToUpload = new File(testSuitForm.getLoginScriptFile()
				.getOriginalFilename());
		String fileName = fileToUpload.getName();
		String destinationFileName = loginScriptPath
				+ testSuitForm.getSuitName() + "\\" + fileName;
		MintFileUtils.uploadFile(testSuitForm.getLoginScriptFile(),
				destinationFileName);
		String packageName = CommonUtils.getPropertyFromPropertyFile(
				configProperties, "mint.transactional.testcase.packageName");
		String jarLocation = FileDirectoryUtil.getAbsolutePath(CommonUtils
				.getPropertyFromPropertyFile(configProperties,
						"mint.testcase.jarLocation"), FileDirectoryUtil
				.getMintRootDirectory(configProperties));
		if (TestCaseFileValidationUtil.validateFile(fileToUpload, new File(
				destinationFileName), packageName, jarLocation)) {
			isUploaded = true;
		}
		return isUploaded;
	}

	private boolean uploadStaticUrlFile(TestEnvironment testEnvironment,
			TestSuitForm testSuitForm) {
		boolean isUploaded = false;
		String destinationPath = FileDirectoryUtil
				.getStaticUrlPath(configProperties);
		destinationPath = destinationPath
				+ testSuitForm.getSuitName()
				+ "\\"
				+ new File(testSuitForm.getLoginScriptFile()
						.getOriginalFilename()).getName();
		isUploaded = MintFileUtils.uploadFile(
				testSuitForm.getLoginScriptFile(), destinationPath);
		return isUploaded;
	}
}
