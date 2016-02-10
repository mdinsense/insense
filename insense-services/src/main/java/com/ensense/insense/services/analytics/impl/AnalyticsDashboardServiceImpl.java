package com.ensense.insense.services.analytics.impl;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cts.mint.analytics.common.AnalyticsDataReportingUtils;
import com.cts.mint.analytics.common.WebAnalyticsUtils;
import com.cts.mint.analytics.dao.AnalyticsAuditSummaryTableDAO;
import com.cts.mint.analytics.dao.AnalyticsDashboardDAO;
import com.cts.mint.analytics.dao.DetailViewTableDAO;
import com.cts.mint.analytics.entity.AnalyticsAuditSummary;
import com.cts.mint.analytics.entity.DashboardsHomeDetails;
import com.cts.mint.analytics.entity.DetailedView;
import com.cts.mint.analytics.model.AnalyticsAuditSummaryReportDates;
import com.cts.mint.analytics.model.DetailedViewContents;
import com.cts.mint.analytics.model.TagTypes;
import com.cts.mint.analytics.service.AnalyticsDashboardService;
import com.cts.mint.generated.jaxb.analytics.detailedview.DetailedViewWebAnalyticsTagData;
import com.cts.mint.generated.jaxb.analytics.summarydashboard.SummaryDashBoardData;
import com.cts.mint.uitesting.entity.LoginUserDetails;
import com.cts.mint.uitesting.entity.Reports;
import com.cts.mint.uitesting.entity.Results;
import com.cts.mint.uitesting.model.ScheduleDetails;
import com.cts.mint.uitesting.transaction.entity.TestCaseFileUpload;


@Service
public class AnalyticsDashboardServiceImpl implements AnalyticsDashboardService {

	@Autowired
	AnalyticsDashboardDAO analyticsdashboardDAO;

	@Autowired
	AnalyticsAuditSummaryTableDAO analyticsAuditSummaryTableDAO;
	
	@Autowired
	DetailViewTableDAO detailViewTableDao;
	
	private static Logger log = Logger.getLogger(AnalyticsDashboardServiceImpl.class);

	

	/* added by 303741
	 * method returns data for dashboard for IWC - production 
	 * which is the default chart
	 */
	@Override
	@Transactional
	public  SummaryDashBoardData getDashBoardDetailsDefault(LoginUserDetails loginUser) {
		
		SummaryDashBoardData entries = new SummaryDashBoardData();		
		DashboardsHomeDetails dashboardsHomeDetails = analyticsdashboardDAO.getDashBoardHomeDataDefault(loginUser);
		log.info("dashboardsHomeDetailsList default ..."+dashboardsHomeDetails);		
		JAXBContext jc;
		try {
			jc = JAXBContext.newInstance(SummaryDashBoardData.class);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			//StringBuffer xmlStr = new StringBuffer(dashboardsHomeDetails.getAuditAnalyticsXml());			
			if ( dashboardsHomeDetails.getAuditAnalyticsXml() != null ){
				entries =(SummaryDashBoardData) unmarshaller.unmarshal( new StreamSource( new StringReader( dashboardsHomeDetails.getAuditAnalyticsXml() ) ) );
			}
			
		} catch (JAXBException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
		return entries;
	
	}
	
	/* added by 303741
	 * method returns data for detailed view based on latest xml structure
	 * default - for home_page  
	 * app, env,login id,date dropdowns
	 */

	@Override
	@Transactional
	public List<DetailedViewContents> getDetailedViewData(String pageURLdefault, LoginUserDetails loginUser, Reports forGeneratedDate) {


		DetailedViewWebAnalyticsTagData webAnalyticsTagData= new DetailedViewWebAnalyticsTagData();
		List<DetailedViewContents> detailedViewContentsList = new ArrayList<DetailedViewContents>();
		System.out.println("query list size: "+analyticsdashboardDAO.getDetailedViewDataFromDB(pageURLdefault, loginUser,forGeneratedDate).size());
		for(DetailedView detailedView:analyticsdashboardDAO.getDetailedViewDataFromDB(pageURLdefault, loginUser,forGeneratedDate)){
			
				    DetailedViewContents detailedViewContents= new DetailedViewContents();
					detailedViewContents.setApplicationName(detailedView.getApplicationName());
					detailedViewContents.setHarLogFileName(detailedView.getHarLogFileName());
					detailedViewContents.setPageTitle(detailedView.getPageTitle());
					detailedViewContents.setPageURL(detailedView.getPageURL());

			
		
		JAXBContext jc;
		try {

			webAnalyticsTagData=null;
			
			jc = JAXBContext.newInstance(DetailedViewWebAnalyticsTagData.class);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			webAnalyticsTagData =(DetailedViewWebAnalyticsTagData)unmarshaller.unmarshal(new StreamSource(new StringReader(detailedView.getWebAnalyticsPageDataXml())));


		} catch (JAXBException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		detailedViewContents.setWebAnalyticsTagData(webAnalyticsTagData);
	
		detailedViewContentsList.add(detailedViewContents);
	//	}

		}
		return detailedViewContentsList;
	}

	/* added by 303741
	 * method returns data for detailed view based on latest xml structure
	 * for url selected by user 
	 * app, env,login id,date dropdowns
	 */

	@Override
	@Transactional
	public List<DetailedViewContents> getDetailedViewDataUserSearchBased(String pageURLSelected, LoginUserDetails loginUser, Results forGeneratedDate) {


		DetailedViewWebAnalyticsTagData webAnalyticsTagData= new DetailedViewWebAnalyticsTagData();
		List<DetailedViewContents> detailedViewContentsList = new ArrayList<DetailedViewContents>();
		for(DetailedView detailedView:analyticsdashboardDAO.getDetailedViewDataUserSearchBased(pageURLSelected, loginUser, forGeneratedDate)){
						
					DetailedViewContents detailedViewContents= new DetailedViewContents();
					detailedViewContents.setApplicationName(detailedView.getApplicationName());
					detailedViewContents.setHarLogFileName(detailedView.getHarLogFileName());
					detailedViewContents.setPageTitle(detailedView.getPageTitle());
					detailedViewContents.setPageURL(detailedView.getPageURL());

			
		
		JAXBContext jc;
		try {

			webAnalyticsTagData=null;
			
			jc = JAXBContext.newInstance(DetailedViewWebAnalyticsTagData.class);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			webAnalyticsTagData =(DetailedViewWebAnalyticsTagData)unmarshaller.unmarshal(new StreamSource(new StringReader(detailedView.getWebAnalyticsPageDataXml())));


		} catch (JAXBException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		detailedViewContents.setWebAnalyticsTagData(webAnalyticsTagData);
	
		detailedViewContentsList.add(detailedViewContents);
		

		}
		return detailedViewContentsList;
	}
	
	/* added by 303741
	 * method returns data for detailed view based on latest xml structure
	 * default - for home_page  
	 * app, env,testcase id,date dropdowns
	 */

	@Override
	@Transactional
	public List<DetailedViewContents> getDetailedViewDataForTestCase(String pageURLdefault, TestCaseFileUpload testcase, Reports forGeneratedDate){


		DetailedViewWebAnalyticsTagData webAnalyticsTagData= new DetailedViewWebAnalyticsTagData();
		List<DetailedViewContents> detailedViewContentsList = new ArrayList<DetailedViewContents>();
		System.out.println("Entry: getDetailedViewDataForTestCase- query list size: "+analyticsdashboardDAO.getDetailedViewDataForTestCase(pageURLdefault, testcase,forGeneratedDate).size());
		for(DetailedView detailedView:analyticsdashboardDAO.getDetailedViewDataForTestCase(pageURLdefault, testcase,forGeneratedDate)){
			
				    DetailedViewContents detailedViewContents= new DetailedViewContents();
					detailedViewContents.setApplicationName(detailedView.getApplicationName());
					detailedViewContents.setHarLogFileName(detailedView.getHarLogFileName());
					detailedViewContents.setPageTitle(detailedView.getPageTitle());
					detailedViewContents.setPageURL(detailedView.getPageURL());
		
		JAXBContext jc;
		try {

			webAnalyticsTagData=null;
			
			jc = JAXBContext.newInstance(DetailedViewWebAnalyticsTagData.class);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			webAnalyticsTagData =(DetailedViewWebAnalyticsTagData)unmarshaller.unmarshal(new StreamSource(new StringReader(detailedView.getWebAnalyticsPageDataXml())));


		} catch (JAXBException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		detailedViewContents.setWebAnalyticsTagData(webAnalyticsTagData);
	
		detailedViewContentsList.add(detailedViewContents);
	//	}

		}
		return detailedViewContentsList;
	}

	/* added by 303741  - 6/12/2014
	 * method returns data for detailed view based on latest xml structure
	 * based on the selected pageurl for transaction types
	 */

	@Override
	@Transactional
	public List<DetailedViewContents> getDetailedViewDataUserSearchBasedtc(String pageURLSelected, TestCaseFileUpload testcase, Results forGeneratedDate) {


		DetailedViewWebAnalyticsTagData webAnalyticsTagData= new DetailedViewWebAnalyticsTagData();
		List<DetailedViewContents> detailedViewContentsList = new ArrayList<DetailedViewContents>();
		for(DetailedView detailedView:analyticsdashboardDAO.getDetailedViewDataUserSearchBasedtc(pageURLSelected, testcase, forGeneratedDate)){
						
					DetailedViewContents detailedViewContents= new DetailedViewContents();
					detailedViewContents.setApplicationName(detailedView.getApplicationName());
					detailedViewContents.setHarLogFileName(detailedView.getHarLogFileName());
					detailedViewContents.setPageTitle(detailedView.getPageTitle());
					detailedViewContents.setPageURL(detailedView.getPageURL());

			
		
		JAXBContext jc;
		try {

			webAnalyticsTagData=null;
			
			jc = JAXBContext.newInstance(DetailedViewWebAnalyticsTagData.class);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			webAnalyticsTagData =(DetailedViewWebAnalyticsTagData)unmarshaller.unmarshal(new StreamSource(new StringReader(detailedView.getWebAnalyticsPageDataXml())));


		} catch (JAXBException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		detailedViewContents.setWebAnalyticsTagData(webAnalyticsTagData);
	
		detailedViewContentsList.add(detailedViewContents);
		

		}
		return detailedViewContentsList;
	}
	/* added by 303741  - 6/12/2014
	 * method returns data for detailed view based on latest xml structure
	 * based on the selected pageurl for transaction types
	 */

	@Override
	@Transactional
	public List<DetailedViewContents> getDetailedViewDataUserSearchBasedPublic(String pageURLSelected, Reports forGeneratedDate, Integer applicationId,
			Integer environmentId){

		DetailedViewWebAnalyticsTagData webAnalyticsTagData= new DetailedViewWebAnalyticsTagData();
		List<DetailedViewContents> detailedViewContentsList = new ArrayList<DetailedViewContents>();
		for(DetailedView detailedView:analyticsdashboardDAO.getDetailedViewDataUserSearchBasedPublic(pageURLSelected, forGeneratedDate, applicationId, environmentId)){
						
					DetailedViewContents detailedViewContents= new DetailedViewContents();
					detailedViewContents.setApplicationName(detailedView.getApplicationName());
					detailedViewContents.setHarLogFileName(detailedView.getHarLogFileName());
					detailedViewContents.setPageTitle(detailedView.getPageTitle());
					detailedViewContents.setPageURL(detailedView.getPageURL());

			
		
		JAXBContext jc;
		try {

			webAnalyticsTagData=null;
			
			jc = JAXBContext.newInstance(DetailedViewWebAnalyticsTagData.class);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			webAnalyticsTagData =(DetailedViewWebAnalyticsTagData)unmarshaller.unmarshal(new StreamSource(new StringReader(detailedView.getWebAnalyticsPageDataXml())));


		} catch (JAXBException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		detailedViewContents.setWebAnalyticsTagData(webAnalyticsTagData);
	
		detailedViewContentsList.add(detailedViewContents);
		

		}
		return detailedViewContentsList;
	}

	@Override
	@Transactional
	public List<String> getRelatedPageURLs(String searchKey, String applicationName) {

		//page url list based on pattern and app name
		List<String> pageURLs = null;

		
		try {

			pageURLs=  analyticsdashboardDAO.getRelatedPageURLsfromDB(searchKey, applicationName);
				

		} catch (Exception exp) {
			log.error("Exception occured in getRelatedPageURLs ", exp);
		}

		return pageURLs;
	}
	
	@Override
	@Transactional
	public List<String> getRelatedPageURLs(String searchKey,
			Results testResult) {
		// page url list based on pattern and app name
		List<String> pageURLs = null;

		try {

			pageURLs = analyticsdashboardDAO.getRelatedPageURLs(
					searchKey, testResult);

		} catch (Exception exp) {
			log.error("Exception occured in getRelatedPageURLs ", exp);
		}

		return pageURLs;
	}
	
	@Override
	@Transactional
	public boolean populateSummaryView(String baseLineHarDirPath,
			String testHarDirPath, Integer scheduleId, ScheduleDetails appConfig, String excelFilePath, String sheetName, String tempDirectory, int excelMaxRowCount, boolean generateReportsOnlyForRefererUrl, String tagSignatureJsonFilePath ) {
		WebAnalyticsUtils wau = new WebAnalyticsUtils();
		
		try{
			//AnalyticsAuditSummary analyticsAuditSummary = wau.getSummaryView(baseLineHarDirPath, testHarDirPath, scheduleId, appConfig.getApplicationName(), excelFilePath, sheetName, generateReportsOnlyForRefererUrl, tagSignatureJsonFilePath);
			//analyticsAuditSummary.setReportGeneratedDate(new Date());
			//analyticsAuditSummaryTableDAO.populateSummaryView(analyticsAuditSummary);
			
			log.info("Analytics report start.");
			log.info("*****************");
			
			log.info("excelFilePath :"+excelFilePath);
			AnalyticsDataReportingUtils excelWriter = new AnalyticsDataReportingUtils(
					excelFilePath, sheetName);
			boolean collectOnlyFailure = false;
			wau.generateAuditAndTestExcelReport(wau, baseLineHarDirPath, testHarDirPath, excelFilePath, excelWriter, appConfig.getApplicationName(), tempDirectory, tagSignatureJsonFilePath, collectOnlyFailure);
			
			log.info("*****************");
			log.info("Analytics report end.");
		} catch ( Exception e){
			log.error("Exception occured in populateSummaryView ", e);
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	public boolean populateDetailedView(String testHarDirPath,
			Integer scheduleId, ScheduleDetails appConfig, String tagSignatureJsonFilePath) {
		WebAnalyticsUtils wau = new WebAnalyticsUtils();
		
		try{
			List<DetailedView> detailedViewList = wau.getDetailedView(testHarDirPath, scheduleId, appConfig.getApplicationName(), tagSignatureJsonFilePath);

			detailViewTableDao.populateDetailedView(detailedViewList);
			
		} catch ( Exception e){
			log.error("Exception occured in populateDetailedView ", e);
			return false;
		}
		
		return true;
	}
	
	
/*	 added by 303741
	 * method returns data for detailed view based on newer xml structure	
	 

	@Override
	@Transactional
	public DetailedViewContents getDetailedViewData() {


		DetailedViewWebAnalyticsTagData webAnalyticsTagData= new DetailedViewWebAnalyticsTagData();
		DetailedViewDetails detailedViewDetails = analyticsdashboardDAO.getDetailedViewDataFromDB();
			
		
		detailedViewDetails = analyticsdashboardDAO.getDetailedViewDataFromDB().get(0);
		
		DetailedViewContents detailedViewContents= new DetailedViewContents();
		detailedViewContents.setApplicationName(detailedViewDetails.getApplicationName());
		detailedViewContents.setHarLogFileName(detailedViewDetails.getHarLogFileName());
		detailedViewContents.setPageTitle(detailedViewDetails.getPageTitle());
		detailedViewContents.setPageURL(detailedViewDetails.getPageURL());

		
		JAXBContext jc;
		try {

			webAnalyticsTagData=null;
			
			jc = JAXBContext.newInstance(DetailedViewWebAnalyticsTagData.class);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			webAnalyticsTagData =(DetailedViewWebAnalyticsTagData)unmarshaller.unmarshal(new StreamSource(new StringReader(detailedViewDetails.getWebAnalyticsPageDataXml())));


		} catch (JAXBException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		detailedViewContents.setWebAnalyticsTagData(webAnalyticsTagData);
	
		return detailedViewContents;
	}
	*/
	
/*	@Override
	@Transactional
	public List<DetailedViewDetails> getXmlData() {		
		return analyticsdashboardDAO.getXMLDataFromDB();
	}
	*/
	
	
	@Override
	@Transactional
	public SummaryDashBoardData getDashBoardDetailsBasedOnDropdown(AnalyticsAuditSummary forGeneratedDate) {
		
		SummaryDashBoardData entries = new SummaryDashBoardData();
		List<TagTypes> tagTypesList = null;
		AnalyticsAuditSummary analyticsAuditSummaryDetails = analyticsdashboardDAO.getDashBoardHomeDataForUserChosen(forGeneratedDate);
		JAXBContext jc;
		try {
			jc = JAXBContext.newInstance(SummaryDashBoardData.class);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
		    entries =(SummaryDashBoardData) unmarshaller.unmarshal( new StreamSource( new StringReader( analyticsAuditSummaryDetails.getAnalyticsAuditXml() ) ) );
			
		} catch (JAXBException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return entries;
	}
	
	@Override
	@Transactional
	public AnalyticsAuditSummary getAnalyticsAuditSummaryForAnalyticsAuditId(AnalyticsAuditSummary analyticsAuditDetails){
		AnalyticsAuditSummary analyticsAuditSummary = analyticsdashboardDAO.getDashBoardHomeDataForUserChosen(analyticsAuditDetails);
		
		return analyticsAuditSummary;
	}
	/*
	@Override
	@Transactional
	public SummaryDashBoardData getDashBoardDetailsBasedOnDropdownPublic(TestLoginUser loginUser, AnalyticsAuditSummary forGeneratedDate) {
		
		SummaryDashBoardData entries = new SummaryDashBoardData();
		List<TagTypes> tagTypesList = null;
		AnalyticsAuditSummary dashboardsHomeDetails = analyticsdashboardDAO.getDashBoardHomeDataForUserChosen(forGeneratedDate);
		
		
		
		
		JAXBContext jc;
		try {
			jc = JAXBContext.newInstance(SummaryDashBoardData.class);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
		    entries =(SummaryDashBoardData) unmarshaller.unmarshal( new StreamSource( new StringReader( dashboardsHomeDetails.getAnalyticsAuditXml() ) ) );
			
		} catch (JAXBException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return entries;
	
	}
	
	@Override
	@Transactional
	public SummaryDashBoardData getDashBoardDetailsBasedOnDropdownTxn(AnalyticsAuditSummary forGeneratedDate){
		SummaryDashBoardData entries = new SummaryDashBoardData();
		List<TagTypes> tagTypesList = null;
		AnalyticsAuditSummary dashboardsHomeDetails = analyticsdashboardDAO.getDashBoardHomeDataForUserChosen(forGeneratedDate);
		
		JAXBContext jc;
		try {
			jc = JAXBContext.newInstance(SummaryDashBoardData.class);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
		    entries =(SummaryDashBoardData) unmarshaller.unmarshal( new StreamSource( new StringReader( dashboardsHomeDetails.getAnalyticsAuditXml() ) ) );
		
		} catch (JAXBException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return entries;
	}
	
	*/
	/* added by 424596 on 06/19/2014
	 * method returns data for detailed view based on latest xml structure
	 * default - for home_page  
	 * app, env,login id,date dropdowns
	 */

	@Override
	@Transactional
	public List<DetailedViewContents> getTestResultDetailedViewData(String pageURLdefault, LoginUserDetails loginUser, Results forGeneratedDate) {

		DetailedViewWebAnalyticsTagData webAnalyticsTagData= new DetailedViewWebAnalyticsTagData();
		List<DetailedViewContents> detailedViewContentsList = new ArrayList<DetailedViewContents>();
		
		System.out.println("query list size: "+analyticsdashboardDAO.getTestResultDetailedViewDataFromDB(pageURLdefault, loginUser,forGeneratedDate).size());
		for(DetailedView detailedView:analyticsdashboardDAO.getTestResultDetailedViewDataFromDB(pageURLdefault, loginUser,forGeneratedDate)){
			
				    DetailedViewContents detailedViewContents= new DetailedViewContents();
					detailedViewContents.setApplicationName(detailedView.getApplicationName());
					detailedViewContents.setHarLogFileName(detailedView.getHarLogFileName());
					detailedViewContents.setPageTitle(detailedView.getPageTitle());
					detailedViewContents.setPageURL(detailedView.getPageURL());
					detailedViewContents.setDetailedViewId(detailedView.getDetailedViewId());
					
		
		JAXBContext jc;
		try {

			webAnalyticsTagData=null;
			
			jc = JAXBContext.newInstance(DetailedViewWebAnalyticsTagData.class);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			webAnalyticsTagData =(DetailedViewWebAnalyticsTagData)unmarshaller.unmarshal(new StreamSource(new StringReader(detailedView.getWebAnalyticsPageDataXml())));


		} catch (JAXBException e1) {
			e1.printStackTrace();
		}

		detailedViewContents.setWebAnalyticsTagData(webAnalyticsTagData);
		detailedViewContentsList.add(detailedViewContents);

		}
		return detailedViewContentsList;
	}

	
	@Override
	@Transactional
	public List<DetailedViewContents> getTestResultDetailedViewDataForTestCase(String pageURLdefault, TestCaseFileUpload testcase, Results forGeneratedDate){


		DetailedViewWebAnalyticsTagData webAnalyticsTagData= new DetailedViewWebAnalyticsTagData();
		List<DetailedViewContents> detailedViewContentsList = new ArrayList<DetailedViewContents>();
		System.out.println("Entry: getDetailedViewDataForTestCase- query list size: "+analyticsdashboardDAO.getTestResultDetailedViewDataForTestCase(pageURLdefault, testcase,forGeneratedDate).size());
		for(DetailedView detailedView:analyticsdashboardDAO.getTestResultDetailedViewDataForTestCase(pageURLdefault, testcase,forGeneratedDate)){
			
				    DetailedViewContents detailedViewContents= new DetailedViewContents();
					detailedViewContents.setApplicationName(detailedView.getApplicationName());
					detailedViewContents.setHarLogFileName(detailedView.getHarLogFileName());
					detailedViewContents.setPageTitle(detailedView.getPageTitle());
					detailedViewContents.setPageURL(detailedView.getPageURL());
		
		JAXBContext jc;
		try {

			webAnalyticsTagData=null;
			
			jc = JAXBContext.newInstance(DetailedViewWebAnalyticsTagData.class);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			webAnalyticsTagData =(DetailedViewWebAnalyticsTagData)unmarshaller.unmarshal(new StreamSource(new StringReader(detailedView.getWebAnalyticsPageDataXml())));


		} catch (JAXBException e1) {
			e1.printStackTrace();
		}

		detailedViewContents.setWebAnalyticsTagData(webAnalyticsTagData);
		detailedViewContentsList.add(detailedViewContents);

		}
		return detailedViewContentsList;
	}

	@Override
	@Transactional
	public String getDefaultPageUrl(int testScheduleId){
		return analyticsdashboardDAO.getDefaultPageUrl(testScheduleId);
	}
	
	/* added by 303741  - 6/12/2014
	 * method returns data for detailed view based on latest xml structure
	 * based on the selected pageurl for transaction types
	 */

	@Override
	@Transactional
	public List<DetailedViewContents> getTestResultDetailedViewDataUserSearchBasedPublic(String pageURLSelected, Results forGeneratedDate, Integer applicationId,
			Integer environmentId){

		DetailedViewWebAnalyticsTagData webAnalyticsTagData= new DetailedViewWebAnalyticsTagData();
		List<DetailedViewContents> detailedViewContentsList = new ArrayList<DetailedViewContents>();
		for(DetailedView detailedView:analyticsdashboardDAO.getTestResultDetailedViewDataUserSearchBasedPublic(pageURLSelected, forGeneratedDate, applicationId, environmentId)){
						
					DetailedViewContents detailedViewContents= new DetailedViewContents();
					detailedViewContents.setApplicationName(detailedView.getApplicationName());
					detailedViewContents.setHarLogFileName(detailedView.getHarLogFileName());
					detailedViewContents.setPageTitle(detailedView.getPageTitle());
					detailedViewContents.setPageURL(detailedView.getPageURL());
		
		JAXBContext jc;
		try {

			webAnalyticsTagData=null;
			
			jc = JAXBContext.newInstance(DetailedViewWebAnalyticsTagData.class);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			webAnalyticsTagData =(DetailedViewWebAnalyticsTagData)unmarshaller.unmarshal(new StreamSource(new StringReader(detailedView.getWebAnalyticsPageDataXml())));

		} catch (JAXBException e1) {
			e1.printStackTrace();
		}
		detailedViewContents.setWebAnalyticsTagData(webAnalyticsTagData);
		detailedViewContentsList.add(detailedViewContents);
		}
		return detailedViewContentsList;
	}

	@Override
	@Transactional
	public List<AnalyticsAuditSummaryReportDates> retrieveAnalyticsSummaryPublicSiteReportsDates(LoginUserDetails testLoginUser){
		return analyticsdashboardDAO.retrieveAnalyticsSummaryPublicSiteReportsDates(testLoginUser);	
	}
	
	@Override
	@Transactional
	public List<AnalyticsAuditSummaryReportDates> retrieveAnalyticsSummaryReportsDates(LoginUserDetails testLoginUser){
		return analyticsdashboardDAO.retrieveAnalyticsSummaryReportsDates(testLoginUser);
	}

	@Override
    @Transactional
    public String getDetailedviewxml(int detailedViewId){
           return analyticsdashboardDAO.getDetailedviewxml(detailedViewId);
    }
    
    @Override
    @Transactional
    public int getUpdateDetailedviewxml(int detailedViewId,String updatedetailvalue){
           return analyticsdashboardDAO.getUpdateDetailedviewxml(detailedViewId,updatedetailvalue);
    }

	@Override
	@Transactional
	public List<AnalyticsAuditSummaryReportDates> retrieveAnalyticsSummaryReportsDatesForTransaction(int applicationId, int environmentId, int testCaseId) {
		return analyticsdashboardDAO.retrieveAnalyticsSummaryReportsDatesForTransaction(applicationId, environmentId, testCaseId);
	}
}
