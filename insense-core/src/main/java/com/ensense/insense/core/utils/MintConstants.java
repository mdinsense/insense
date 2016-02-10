/**
 * 
 */
package com.ensense.insense.core.utils;

/**
 * @author 361494
 *
 */
public class MintConstants {
	
	public static final String AWC_FILE = "xml/AWC.xml";// TODO load all the files from properties
	public static final String IWC_FILE = "xml/IWC.xml";// TODO load all the files from properties
	public static final String UD_FILE = "xml/UD.xml";// TODO load all the files from properties
	public static final String IFA_FILE = "xml/IFA.xml";// TODO load all the files from properties
	public static final String PLANSPONSOR_FILE = "xml/PLANSPONSOR.xml";// TODO load all the files from properties
	
	public static final String APP_CONFIG= "xml/AppConfig.xml";
	
	public static enum TIAA_APP {AWC,IWC,UnifiedDesktop,IFA,PLANSPONSOR};
	
	public static final String TIAA_STRING = "tiaa";
	public static final String TIAA_CREF_STRING = "tiaa-cref";
	
	public static final String NON_TRANSACTION_TESTING ="Non Transaction Testing";
	public static final String TRANSACTION_TESTING ="Transaction Testing";
	public static final String OPRA_RESET ="OPRA Reset";
	public static final String OPRA_BULK_RESET ="OPRA Bulk Reset";
	public static final String WEBSERVICE_TESTING ="Webservice Testing";
	public static final String[] DAYS = {"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
	public static final String WEBTESTING_COMPLETION_CONTENT = "On demand - successfully scheduled. BaseLine Details are Added Successfully";
	public static final String WEBTESTING_FAILURE_CONTENT = "Failed scheduling On-Demand for the provided data set";
	
}
