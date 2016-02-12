package com.ensense.insense.data.analytics.common;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class WebAnalyticsConstants {
	

	public static final String noMatchingTagFound="No-Matching-Tag-Found";
	public static String noSupportedTagTypeFound="No-Supported-Tag-Type-Found";
	
	public static final String noMatchingAnlyticsPageFound ="No-Matching-Page-Found";
	public static final String noDataFound ="No-Data-Found";
	
	public static String noTagUrlFound="No-Tag-Url-Found";
	
	public static String noTagVariableFound="No-Tag-Variable-Found";
	public static String noTagVariableValueFound="No-Tag-Variable-Value-Found";
	
	public static String notApplicable = "Not-Applicable";
	
	public static String domainName="*";
	
	public static final Map<String, String> siteCatalystVariableMap;
	
	
	public static String tagSignatureJsonFile="C:\\projects\\PortalCommonsV8A\\mintToolsV2\\mintCommonsJAR\\resources\\tag-signatures.json"; //move this into a configuraiton
	
	static {
		siteCatalystVariableMap = new LinkedHashMap<String, String>();
		// populate all the props
		for (int i = 1; i < 76; i++) {// 1 thru 75 for eVars and props
			// data map from sitecatalyst javascript to java variables
			siteCatalystVariableMap.put("v" + i, "eVar" + i);
			siteCatalystVariableMap.put("c" + i, "prop" + i);
		}
	}
	
	
	public static final Map<String, String> applicationsNameMap;
	static {
		applicationsNameMap = new LinkedHashMap<String, String>();

		// secure sites
		applicationsNameMap.put("google.com", "Google");
		
		
		

	}
	
	//specify the http status codes which need not be tracked as error responses
	public static final Map<Integer, Integer> okResponseStatusCodes;
	static{
		okResponseStatusCodes = new LinkedHashMap<Integer, Integer>();
		okResponseStatusCodes.put(Integer.valueOf(200), Integer.valueOf(200));
		okResponseStatusCodes.put(Integer.valueOf(999), Integer.valueOf(999));
		okResponseStatusCodes.put(Integer.valueOf(302), Integer.valueOf(302));
		okResponseStatusCodes.put(Integer.valueOf(204), Integer.valueOf(204));
		okResponseStatusCodes.put(Integer.valueOf(304), Integer.valueOf(304));
	}
	
	
	public static final Map<String, String> tagTypeMap;
	static {
		tagTypeMap = new LinkedHashMap<String, String>();

		// Analytics Tags
		tagTypeMap.put("axf8.net/mr/b.gif", "Gomez");
	//	tagTypeMap.put("axf8.net/mr/e.gif", "Gomez");
		tagTypeMap.put("g.doubleclick.net","GoogleAnalytics"); //stats.g.doubleclick.net
		tagTypeMap.put("google-analytics.com","GoogleAnalytics");
		tagTypeMap.put("data.coremetrics.com","CoreMetrics");
		tagTypeMap.put("b.scorecardsearch.com","ComScore"); //b.scorecardsearch.com //TODO changed to b.scorecardsearch.com, need to check with Mahesh
		tagTypeMap.put("metrics.tiaa-cref.org", "SiteCatalyst");
		tagTypeMap.put("addthis.com", "AddThis");
		tagTypeMap.put("fls.doubleclick.net", "DoubleClick");
		tagTypeMap.put("oo_conf_tab.js","OpinionLab"); //seems to be available only on www1// tiaacref.tt.omtrdc.net
		tagTypeMap.put("oo_engine.min.js","OpinionLab");
	
		//wells
		tagTypeMap.put("/b/ss/", "SiteCatalyst");
		tagTypeMap.put("foresee", "ForeSee");
		tagTypeMap.put("ad.doubleclick.net", "DART Floodlight");
		
		tagTypeMap.put("/Bootstrap.js", "Ensighten Boostrap");
		tagTypeMap.put("/bootstrap.js", "Ensighten Boostrap");
		tagTypeMap.put("/serverComponent.php", "Ensighten ServerComponent");
		
		tagTypeMap.put("nexus.ensighten.com/tiaa-cref/public/code/", "Ensighten PageRules");
		tagTypeMap.put("nexus.ensighten.com/tiaa-cref/iwc/code/", "Ensighten PageRules");
		tagTypeMap.put("nexus.ensighten.com/tiaa-cref/IFA/code/", "Ensighten PageRules");
		tagTypeMap.put("nexus.ensighten.com/tiaa-cref/plan-focus/code/", "Ensighten PageRules");
		
		tagTypeMap.put("nexus.ensighten.com/tiaa-cref/public-dev/code/", "Ensighten PageRules");
		tagTypeMap.put("nexus.ensighten.com/tiaa-cref/iwc-dev/code/", "Ensighten PageRules");
		tagTypeMap.put("nexus.ensighten.com/tiaa-cref/ifa-dev/code/", "Ensighten PageRules");
		tagTypeMap.put("nexus.ensighten.com/tiaa-cref/plan-focus-dev/code/", "Ensighten PageRules");
		
		
		tagTypeMap.put("/tiaacrefAnalytics.js", "TIAA Analytics JS");
		tagTypeMap.put("/js_site_catalyst_lazy.js", "TIAA Lazy JS");
		
		
	}
	
	
	public static final List<String> siteCatalystPrefVars;
	static {
		siteCatalystPrefVars = new LinkedList<String>();
		
		siteCatalystPrefVars.add("events");
		//Other vars
		siteCatalystPrefVars.add("pageName");
		//channel
		siteCatalystPrefVars.add("ch");
		
		//list
		siteCatalystPrefVars.add("l1");
		siteCatalystPrefVars.add("l2");
		//hier
		siteCatalystPrefVars.add("h2");
		
		siteCatalystPrefVars.add("pageType");
		
		//server
		siteCatalystPrefVars.add("server");
		
		//products
		siteCatalystPrefVars.add("products");
		
		
		
		//campaign
		
		siteCatalystPrefVars.add("v0");
		
		//props
		siteCatalystPrefVars.add("c1");
		siteCatalystPrefVars.add("c3");
		siteCatalystPrefVars.add("c10");
		siteCatalystPrefVars.add("c11");
		siteCatalystPrefVars.add("c12");
		siteCatalystPrefVars.add("c14");
		siteCatalystPrefVars.add("c16");
		siteCatalystPrefVars.add("c18");
		siteCatalystPrefVars.add("c19");
		siteCatalystPrefVars.add("c21");
		siteCatalystPrefVars.add("c30");
		siteCatalystPrefVars.add("c31");
		siteCatalystPrefVars.add("c33");
		siteCatalystPrefVars.add("c34");
		siteCatalystPrefVars.add("c36");
		siteCatalystPrefVars.add("c38");
		siteCatalystPrefVars.add("c39");
		siteCatalystPrefVars.add("c40");
		siteCatalystPrefVars.add("c41");
		siteCatalystPrefVars.add("c42");
		siteCatalystPrefVars.add("c43");
		siteCatalystPrefVars.add("c45");
		siteCatalystPrefVars.add("c46");
		siteCatalystPrefVars.add("c47");
		siteCatalystPrefVars.add("c111");
		
		//evars
		siteCatalystPrefVars.add("v1");
		siteCatalystPrefVars.add("v3");
		siteCatalystPrefVars.add("v6");
		siteCatalystPrefVars.add("v7");
		siteCatalystPrefVars.add("v9");
		siteCatalystPrefVars.add("v10");
		siteCatalystPrefVars.add("v11");
		siteCatalystPrefVars.add("v12");
		siteCatalystPrefVars.add("v13");
		siteCatalystPrefVars.add("v14");
		siteCatalystPrefVars.add("v18");
		siteCatalystPrefVars.add("v23");
		siteCatalystPrefVars.add("v24");
		siteCatalystPrefVars.add("v31");
		siteCatalystPrefVars.add("v32");
		siteCatalystPrefVars.add("v34");
		siteCatalystPrefVars.add("v35");
		siteCatalystPrefVars.add("v36");
		siteCatalystPrefVars.add("v38");
		siteCatalystPrefVars.add("v39");
		siteCatalystPrefVars.add("v42");
		siteCatalystPrefVars.add("v45");
		siteCatalystPrefVars.add("v46");
		siteCatalystPrefVars.add("v48");
		siteCatalystPrefVars.add("v49");
		siteCatalystPrefVars.add("v50");
		siteCatalystPrefVars.add("pev2");
	}
	

}
