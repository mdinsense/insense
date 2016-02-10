package com.ensense.insense.core.analytics.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipFile;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;

import com.cts.mint.analytics.entity.AnalyticsAuditSummary;
import com.cts.mint.analytics.entity.DetailedView;
import com.cts.mint.analytics.model.BrokenUrlData;
import com.cts.mint.analytics.model.SamePagesBrokenUrlDataStore;
import com.cts.mint.analytics.model.SamePagesDataStore;
import com.cts.mint.analytics.model.SupportedTagData;
import com.cts.mint.analytics.model.TagSignaturesObject;
import com.cts.mint.analytics.model.WebAnalyticsInventoryData;
import com.cts.mint.analytics.model.WebAnalyticsPageData;
import com.cts.mint.analytics.model.WebAnalyticsTagData;
import com.cts.mint.common.FileUtils;
import com.cts.mint.common.JsonReaderWriter;
import com.cts.mint.common.ZipFileUtils;
import com.cts.mint.common.model.Link;
import com.cts.mint.crawler.model.CrawlConfig;
import com.cts.mint.uitesting.model.UiTestingConstants;
import com.cts.mint.util.DateTimeUtil;
import com.cts.mint.util.FileListing;

import edu.umass.cs.benchlab.har.HarEntries;
import edu.umass.cs.benchlab.har.HarEntry;
import edu.umass.cs.benchlab.har.HarHeader;
import edu.umass.cs.benchlab.har.HarHeaders;
import edu.umass.cs.benchlab.har.HarLog;
import edu.umass.cs.benchlab.har.HarPage;
import edu.umass.cs.benchlab.har.HarQueryParam;
import edu.umass.cs.benchlab.har.HarRequest;
import edu.umass.cs.benchlab.har.HarResponse;
import edu.umass.cs.benchlab.har.HarWarning;
import edu.umass.cs.benchlab.har.tools.HarFileReader;
import edu.umass.cs.benchlab.har.tools.HarFileWriter;

public class WebAnalyticsUtils {

	private static final Logger logger = Logger
			.getLogger(WebAnalyticsUtils.class);

	// filer the tags that fire across multiple pages Example:tags on all menu
	// links fire for ever page visited
	// logic
	// go to the first page in audit, loop through all the tags on that page
	// search for a specific tagVarname and get its value: example: c34
	// if tagVarname is found continue to search the tags on other pages which
	// have same tagVarValue
	// if tagVarValue on other pages match, delete the tag from that page audit
	// data..

	// if not found, go to the next page and see if the tagVarValue is found. if
	// yes, continue the above process
	// return the filtered object.

	public Map<String, WebAnalyticsPageData> filterCommonTagsFiredFromPageAudit(
			Map<String, WebAnalyticsPageData> pageDataMap,
			String filterTagVarName) {

		String pageDataKey;
		String otherPagesDataKey;

		String tagDataKey;
		String otherTagDataKey;

		WebAnalyticsPageData pageData;

		Map<String, WebAnalyticsTagData> tagDataMap;
		Map<String, WebAnalyticsTagData> otherTagDataMap;

		Map<String, String> tagVarDataMap;
		Map<String, String> otherTagVarDataMap;

		Object tagVarName;
		Object otherTagVarName;
		String tagVarValue;

		Iterator<String> pageDataMapIterator = pageDataMap.keySet().iterator();
		Iterator<String> pageDataMapIteratorTemp = pageDataMap.keySet()
				.iterator();

		// copy one hashmap to another
		Map<String, WebAnalyticsPageData> pageDataMapCopy = new LinkedHashMap<String, WebAnalyticsPageData>();// pageDataMap;
		pageDataMapCopy.put("abc", new WebAnalyticsPageData());

		while (pageDataMapIteratorTemp.hasNext()) {
			String key = pageDataMapIteratorTemp.next();
			WebAnalyticsPageData value = pageDataMap.get(key);
			pageDataMapCopy.put(key, value);
		}

		// pageDataMap.putAll(pageDataMapCopy);
		// System.out.println("size copy :"+pageDataMapCopy.size() + "; "+
		// pageDataMapCopy.hashCode());
		// System.out.println("size  :"+pageDataMap.size()+"; "+
		// pageDataMap.hashCode());

		while (pageDataMapIterator.hasNext()) {
			// System.out.println("common tags filter");
			pageDataKey = pageDataMapIterator.next();
			pageData = pageDataMap.get(pageDataKey);

			tagDataMap = pageData.getWebAnalyticsTagDataMap();
			Iterator tagDataIterator = tagDataMap.keySet().iterator();

			while (tagDataIterator.hasNext()) {
				tagDataKey = tagDataIterator.next().toString();
				tagVarDataMap = tagDataMap.get(tagDataKey).getTagVariableData();
				Iterator tagVarDataIterator = tagVarDataMap.keySet().iterator();

				/*
				 * while(tagVarDataIterator.hasNext()){ tagVarName =
				 * tagVarDataIterator.next(); System.out.println("tagVarName :"
				 * + tagVarName); }
				 */

				// loop through the tag var data
				// if a specific tarVarName is found, delete it from subsequent
				// pages. Dont delete from the starting page.
				while (tagVarDataIterator.hasNext()) {
					tagVarName = tagVarDataIterator.next();
					// tagVarValue =tagVarDataMap.get(tagVarName);
					// filterTagVarName
					tagVarValue = tagVarDataMap.get(filterTagVarName);
					// clean up starts here
					Iterator<String> otherPagesDataMapIterator = pageDataMap
							.keySet().iterator();
					List<String> alreadyCheckedList = new LinkedList<String>();
					while (otherPagesDataMapIterator.hasNext()) {

						otherPagesDataKey = otherPagesDataMapIterator.next();
						// add this page to a alreadyChecked list
						if (!alreadyCheckedList.contains(otherPagesDataKey)) {

							// for other pages other than self
							// start deleting the tags where matching tagVarName
							// and values are found
							if (!otherPagesDataKey.equals(pageDataKey)) {
								otherTagDataMap = pageDataMap.get(
										otherPagesDataKey)
										.getWebAnalyticsTagDataMap();
								Iterator otherTagDataMapIterator = otherTagDataMap
										.keySet().iterator();
								boolean removeDuplicateTagFiring = false;
								while (otherTagDataMapIterator.hasNext()) {
									removeDuplicateTagFiring = false;
									otherTagDataKey = otherTagDataMapIterator
											.next().toString();

									otherTagVarDataMap = otherTagDataMap.get(
											otherTagDataKey)
											.getTagVariableData();
									Iterator otherTagVarDataMapIterator = otherTagVarDataMap
											.keySet().iterator();

									while (otherTagVarDataMapIterator.hasNext()) {
										otherTagVarName = otherTagVarDataMapIterator
												.next();

										if (otherTagVarDataMap.get(
												otherTagVarName).equals(
												tagVarValue)
												&& tagVarValue != null) {
											// start deleting the tag from the
											// page.
											// System.out.println("key : "+otherTagDataKey);
											// System.out.println("pageDataMapCopy"+pageDataMapCopy.hashCode());
											// System.out.println("pageDataMap"+pageDataMap.hashCode());
											// pageDataMapCopy.get(otherPagesDataKey).getWebAnalyticsTagDataMap().remove(otherTagDataKey);
											// System.out.println("remove");
											removeDuplicateTagFiring = true;

										}
									}
									// remove if duplicateTag firing
									if (removeDuplicateTagFiring) {
										// System.out.println("removed");
										otherTagDataMapIterator.remove();
									}
								}

							}

						}

						alreadyCheckedList.add(otherPagesDataKey);
					} // end if alreadycheckedList

				}

			}

		}
		return pageDataMap;

	}

	public static Map<String, String> getQueryMap(String query) {
		String[] params = query.split("&");
		Map<String, String> map = new HashMap<String, String>();
		for (String param : params) {
			try {
				// System.out.println("param :"+ param);
				String name;
				String value;

				name = param.split("=")[0];
				if (param.split("=").length > 1) {
					value = param.split("=")[1];
				} else {
					value = ""; // if param value is blank
				}

				map.put(name, value);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return map;
	}

	// this method gets the funcationl groups on a site based on urls scanned.
	// takes input url startsWith : example:
	// siteUrlStartsWith=http://intranet.ops.tiaa-cref.org/public/home.html
	// gets a list of functional groups
	// for each functional group get the list of har/har.zip files
	// pass the file in a loop to getWebAnlyticsPageDataFromHarLog
	// assemble the result into Map<String, WebAnalyticsPageData>

	public String extractFunctinalGroupsFromPageUrl(String pageUrl, int maxDepth) {
		String urlPath = "FunctionalGroupNotFound";
		int beginIndex = 0;
		String pageName;
		Map<String, String> queryMap = new LinkedHashMap<String, String>();

		try {
			URL url = new URL(pageUrl);
			if (url.getQuery() != null) {
				queryMap = getQueryMap(url.getQuery());
			}

			urlPath = url.getPath();
			if (urlPath.contains(".")) {
				urlPath = urlPath.substring(beginIndex,
						urlPath.lastIndexOf("/") + 1);
			} else {
				urlPath = urlPath;
			}

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			urlPath = pageUrl;
			// e.printStackTrace();
		}

		// adjust the group maxDepth
		if (urlPath.contains("/")) {
			int index = 0;
			int prevIndex = 0;
			int loop = 0;
			index = urlPath.indexOf("/");
			while (index >= 0 && loop <= maxDepth - 1) {

				++loop;
				prevIndex = index;
				index = urlPath.indexOf("/", index + 1);
				if (index != -1) {
					prevIndex = index;
				}
				// System.out.println("index:"+index);
				// System.out.println("loop:"+loop);
				// System.out.println("max :"+maxDepth);
			}
			urlPath = urlPath.substring(0, prevIndex);
		}

		// for portal urls
		if (queryMap != null) {
			pageName = queryMap.get("_pageName");
			if (pageName != null) {
				urlPath = urlPath + "/" + pageName;
			}
		}

		// System.out.println(urlPath);
		return urlPath;
	}

	// this method provides the number of pages per functional group for all
	// functional groups in the site.
	public List<Map<String, Integer>> getFunctionalGroupStats(
			Map<String, WebAnalyticsPageData> pageDataMap,
			String siteUrlStartsWith, int maxDepth) {
		String key;
		Iterator pageDataMapIterator = pageDataMap.keySet().iterator();
		Set<String> functionalGroupSet = new HashSet<String>();
		Map<String, Integer> functionalGroupCountMap = new HashMap<String, Integer>();
		String funcGroup;
		String pageUrl;
		int counter = 0;

		while (pageDataMapIterator.hasNext()) {
			key = pageDataMapIterator.next().toString();
			pageUrl = pageDataMap.get(key).getPageUrl();
			funcGroup = extractFunctinalGroupsFromPageUrl(pageUrl, maxDepth);
			// System.out.println("functionalGroup :"+ funcGroup);

			if (siteUrlStartsWith.equals("*")
					|| pageUrl.contains(siteUrlStartsWith)) {
				functionalGroupSet.add(funcGroup);

				// get the value, if found, increment the count
				if (functionalGroupCountMap.get(funcGroup) != null) {
					int count = functionalGroupCountMap.get(funcGroup)
							.intValue();
					functionalGroupCountMap.put(funcGroup, new Integer(
							count + 1));
					counter++;
				} else {
					int count = 1;
					functionalGroupCountMap.put(funcGroup, new Integer(count));
					counter++;
				}

				// System.out.println("func " + funcGroup +" count :"
				// +functionalGroupCountMap.get(funcGroup).intValue());
			}

		}
		// System.out.println("functionalGroupSet.size :"
		// +functionalGroupSet.size());
		// System.out.println("functionalGroupMap.size :"
		// +functionalGroupCountMap.size());

		// group the functional group by pages count return the
		// groupsOfFunctionalGroup/
		// based on the depth/hierarchical level of functional group, the number
		// of pages within the functional group will be more than the count in
		// the map
		// example functional group shows the total count =1000, but it is just
		// counting at the top level of the hierarchy,
		groupFunctionalGroupsBySize(functionalGroupCountMap);
		// System.out.println(">>>>>>>>>>>>>>>>>pageDataMap.size :" +
		// pageDataMap.size());
		// System.out.println(">>>>> Counter :"+ counter);
		// return functionalGroupCountMap;
		return groupFunctionalGroupsBySize(functionalGroupCountMap);
	}

	// this method sorts the HashMap by values
	// custom sorter
	public static Map<String, Integer> sortMapByValue(Map<String, Integer> map) {
		List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(
				map.entrySet());

		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {

			public int compare(Map.Entry<String, Integer> m1,
					Map.Entry<String, Integer> m2) {
				return (m2.getValue()).compareTo(m1.getValue());
			}
		});

		Map<String, Integer> result = new LinkedHashMap<String, Integer>();
		for (Map.Entry<String, Integer> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

	public List<Map<String, Integer>> groupFunctionalGroupsBySize(
			Map<String, Integer> funtionalGroupCountMap) {

		// group functional groups based on size
		// if a functional group page count 1000 to 1200 fall under one group
		// if a functional group page count is between 500 to 1000, try to add
		// with another functional group page count, so that the total is less
		// than 1200
		ArrayList[][] functionalGroupsArray; // = new ArrayList()[][];
		int pagesPerWorkBook = 1000;
		int totalGroupsOfGroup;

		Map sortedFuntionalGroupCountMap = sortMapByValue(funtionalGroupCountMap);
		Iterator it = sortedFuntionalGroupCountMap.keySet().iterator();

		// get the total count of pages
		int totalPages = 0;
		while (it.hasNext()) {
			String key = it.next().toString();
			Integer value = (Integer) sortedFuntionalGroupCountMap.get(key);

			totalPages = totalPages + value.intValue();
			// System.out.println("key " + key + " : "+value.intValue() );
		}

		// System.out.println("totalPages for all functional groups :" +
		// totalPages);

		// calculate the total number of report workbooks required. This logic
		// would group functions such that a workbook can have a max of x pages
		// of data
		// example: assume if 1000 pages is specified as the max pages per
		// workbook, calculate the number of workbooks required =
		// totalPages/1000
		// pagesPerWorkBook

		totalGroupsOfGroup = (int) Math.round((totalPages / pagesPerWorkBook)) + 1;

		System.out.println("totalGroupsOfGroup :" + totalGroupsOfGroup);

		// group1 = fun1, fun2,
		// group2= fun3, fun5
		// group3 =fun4,fun6

		Iterator it2 = sortedFuntionalGroupCountMap.keySet().iterator();
		Map<String, Integer> sortedFuntionalGroupSubSetCountMap = new LinkedHashMap<String, Integer>();
		int totalPagesCount = 0;

		List<Map<String, Integer>> tempList = new ArrayList<Map<String, Integer>>();

		// arrange functional groups into groups
		// iterate through each functional group and group them with another
		// group based on the pages each function group has
		int functionGroupCounter = 0;
		while (it2.hasNext()) {
			functionGroupCounter++;

			String key = it2.next().toString(); // functional group name
			Integer value = (Integer) sortedFuntionalGroupCountMap.get(key); // the
																				// number
																				// of
																				// pages
																				// in
																				// the
																				// function
																				// group
			totalPagesCount = totalPagesCount + value.intValue();

			sortedFuntionalGroupSubSetCountMap.put(key, value);

			if (totalPagesCount >= 1000) {
				System.out.println("Subset size:  "
						+ sortedFuntionalGroupSubSetCountMap.size());

				tempList.add(sortedFuntionalGroupSubSetCountMap);

				Iterator it3 = sortedFuntionalGroupSubSetCountMap.keySet()
						.iterator();

				System.out
						.println("sortedFuntionalGroupSubSetCountMap size before :"
								+ sortedFuntionalGroupSubSetCountMap.size());
				sortedFuntionalGroupSubSetCountMap = new LinkedHashMap<String, Integer>();
				/*
				 * while(it3.hasNext()){ it3.next(); it3.remove(); }
				 */
				totalPagesCount = 0; // reset total count to 0, so that the
										// grouping can start over.

			}

			System.out.println("functionGroupCounter :" + functionGroupCounter);
			System.out.println("funtionalGroupCountMap.size() :"
					+ funtionalGroupCountMap.size());

			if (functionGroupCounter == funtionalGroupCountMap.size()) {
				System.out.println("else block");
				tempList.add(sortedFuntionalGroupSubSetCountMap);
			}

			System.out
					.println("sortedFuntionalGroupSubSetCountMap size after :"
							+ sortedFuntionalGroupSubSetCountMap.size());
			System.out.println("tempList size" + tempList.size());
			System.out.println("\n");
		}

		return tempList;
	}

	public void groupFunctionalGroupsBySize2(
			Map<String, Integer> funtionalGroupCountMap) {

		// group functional groups based on size
		// if a functional group page count 1000 to 1200 fall under one group
		// if a functional group page count is between 500 to 1000, try to add
		// with another functional group page count, so that the total is less
		// than 1200
		ArrayList[][] functionalGroupsArray; // = new ArrayList()[][];
		int pagesPerWorkBook = 1000;
		int totalGroupsOfGroup;

		Map sortedFuntionalGroupCountMap = sortMapByValue(funtionalGroupCountMap);
		Iterator it = sortedFuntionalGroupCountMap.keySet().iterator();

		// get the total count of pages
		int totalPages = 0;
		while (it.hasNext()) {
			String key = it.next().toString();
			Integer value = (Integer) sortedFuntionalGroupCountMap.get(key);

			totalPages = totalPages + value.intValue();
			System.out.println("key " + key + " : " + value.intValue());
		}

		System.out.println("totalPages for all functional groups :"
				+ totalPages);

		// calculate the total number of report workbooks required. This logic
		// would group functions such that a workbook can have a max of x pages
		// of data
		// example: assume if 1000 pages is specified as the max pages per
		// workbook, calculate the number of workbooks required =
		// totalPages/1000
		// pagesPerWorkBook

		totalGroupsOfGroup = (int) Math.round((totalPages / pagesPerWorkBook)) + 1;

		System.out.println("totalGroupsOfGroup :" + totalGroupsOfGroup);

		// group1 = fun1, fun2,
		// group2= fun3, fun5
		// group3 =fun4,fun6

		String[][] groupsOfGroupArrays = new String[totalGroupsOfGroup][];
		Iterator it2 = sortedFuntionalGroupCountMap.keySet().iterator();

		int totalPagesCount = 0;

		int groupOfGroupsCounter = 0;
		// Map<String, Integer> tempFuncGroupofGroupMap = new
		// LinkedHashMap<String,Integer>();
		List<String> tempFuncGroupsOfGroupList = new ArrayList<String>();
		// arrange functional groups into groups
		// iterate through each functional group and group them with another
		// group based on the pages each function group has
		while (it2.hasNext()) {
			String key = it2.next().toString(); // functional group name
			Integer value = (Integer) sortedFuntionalGroupCountMap.get(key); // the
																				// number
																				// of
																				// pages
																				// in
																				// the
																				// function
																				// group
			// check if the value =>1000
			// add the pages from each functional group, until the total is more
			// =>1000
			// if the total is >= 1000, put the individual function in the
			// string array

			// tempFuncGroupofGroup.put(key, value);
			tempFuncGroupsOfGroupList.add(key); // store the function group
												// already processed
			totalPagesCount = totalPagesCount + value.intValue();
			System.out.println("value.intValue() " + value.intValue());
			System.out.println("totalPagesCount :" + totalPagesCount);
			if (totalPagesCount >= 1000) {

				int temFuncGroupsOfGroupSize = tempFuncGroupsOfGroupList.size();
				System.out.println("temFuncGroupsOfGroupSize :"
						+ temFuncGroupsOfGroupSize);
				groupsOfGroupArrays[groupOfGroupsCounter] = new String[temFuncGroupsOfGroupSize];

				for (int i = 0; i < temFuncGroupsOfGroupSize; i++) {

					groupsOfGroupArrays[groupOfGroupsCounter][i] = tempFuncGroupsOfGroupList
							.get(i);
					System.out.println("indv values :"
							+ groupsOfGroupArrays[groupOfGroupsCounter][i]);
					tempFuncGroupsOfGroupList.remove(i);// remove the func group
														// after adding to the
														// group
				}
				groupOfGroupsCounter++;

				totalPagesCount = 0;
				System.out.println("totalPagesCount :" + totalPagesCount);
			}
		}

		System.out.println("groups of groups :" + groupsOfGroupArrays[0][0]);

	}

	public List<String> listFilesForFolder(final File folder) {
		List<String> filesInDir = new ArrayList<String>();
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				listFilesForFolder(fileEntry);
			} else {
				// System.out.println(fileEntry.getName());
				// convert from windows file path format to java
				filesInDir.add(fileEntry.getPath().replace("\\", "/"));
				// System.out.println(fileEntry.getPath().replace("\\", "/"));
			}
		}
		return filesInDir;
	}

	// inventory of pages, with no tags
	public void getPagesWithNoAnalyticsTags() {
		// get the referrer urls which dont have have any matching tag data
		// store the appname, page title, page url url (if tiaa), functional
		// name if possible(split the url to get extract the info)

	}

	// export to XML
	public void exportAnalyticsDataToXML(String harLogsDir, String applicationName, String tagSignatureJsonFilePath) {

		Map<String, WebAnalyticsPageData> webAnalyticsPageDataMap = getWebAnlyticsPageDataFromHarLogs(harLogsDir, applicationName, tagSignatureJsonFilePath);
		// sort the map
		// export to xml
	}

	// export to Excel
	public void exportAnalyticsDataToToXLSX(String harLogsDir, String applicationName, String tagSignatureJsonFilePath) {
		Map<String, WebAnalyticsPageData> webAnalyticsPageDataMap = getWebAnlyticsPageDataFromHarLogs(harLogsDir, applicationName, tagSignatureJsonFilePath);
		// sort the map
		// export to Excel
		// write appname, page title, page name(if applicable), baseline tag
		// type, baseline tag data variable and value, compareTo: tag
		// type,tagdata, tagvalue, teststatus:passed, failed, notfound(tagtype,
		// tag variable, tagvalue) referrer url, tagurl

	}

	// normalize the pageDataMaps. if baseline has more entries than testData,
	// add null entries to testDatamap and vice versa
	// result of normalization is both maps will have eqal entries
	// after normalization add the pagedatamaps to SamePageDataStore

	public WebAnalyticsPageData createDummyPageData() {
		WebAnalyticsPageData noMatchingAnalyticsPageDataFound = new WebAnalyticsPageData();
		noMatchingAnalyticsPageDataFound
				.setApplicationName(WebAnalyticsConstants.noDataFound);
		noMatchingAnalyticsPageDataFound
				.setHarLogFileName(WebAnalyticsConstants.noDataFound);
		noMatchingAnalyticsPageDataFound
				.setPageRef(WebAnalyticsConstants.noDataFound);
		noMatchingAnalyticsPageDataFound
				.setPageTitle(WebAnalyticsConstants.noDataFound);
		noMatchingAnalyticsPageDataFound
				.setPageUrl(WebAnalyticsConstants.noDataFound);
		noMatchingAnalyticsPageDataFound.setStartedDateTime((new Date()));
		noMatchingAnalyticsPageDataFound
				.setWebAnalyticsTagDataMap(createDummyTagDataMap());

		return noMatchingAnalyticsPageDataFound;
	}

	public Map<String, WebAnalyticsTagData> createDummyTagDataMap() {
		Map<String, WebAnalyticsTagData> noMatchingAnalyticsTagDataMapFound = new LinkedHashMap<String, WebAnalyticsTagData>();
		WebAnalyticsTagData noMatchingAnalyticsTagDataFound = new WebAnalyticsTagData();
		noMatchingAnalyticsTagDataFound
				.setTagDataKey(WebAnalyticsConstants.noDataFound);
		noMatchingAnalyticsTagDataFound.setStartedDateTime((new Date()));
		noMatchingAnalyticsTagDataFound
				.setTagName(WebAnalyticsConstants.noDataFound);
		noMatchingAnalyticsTagDataFound
				.setTagUrl(WebAnalyticsConstants.noDataFound);
		noMatchingAnalyticsTagDataFound
				.setTagVariableData(createDummyVariableDataMap());
		noMatchingAnalyticsTagDataMapFound.put(
				WebAnalyticsConstants.noDataFound,
				noMatchingAnalyticsTagDataFound);

		return noMatchingAnalyticsTagDataMapFound;
	}

	public WebAnalyticsTagData createDummyTagData() {

		WebAnalyticsTagData noMatchingAnalyticsTagDataFound = new WebAnalyticsTagData();
		noMatchingAnalyticsTagDataFound
				.setTagDataKey(WebAnalyticsConstants.noDataFound);
		noMatchingAnalyticsTagDataFound.setStartedDateTime((new Date()));
		noMatchingAnalyticsTagDataFound
				.setTagName(WebAnalyticsConstants.noDataFound);
		noMatchingAnalyticsTagDataFound
				.setTagUrl(WebAnalyticsConstants.noDataFound);
		noMatchingAnalyticsTagDataFound
				.setTagVariableData(createDummyVariableDataMap());

		return noMatchingAnalyticsTagDataFound;
	}

	public Map<String, String> createDummyVariableDataMap() {
		Map<String, String> noMatchingAnalyticsTagVariableMapFound = new LinkedHashMap<String, String>();
		noMatchingAnalyticsTagVariableMapFound.put(
				WebAnalyticsConstants.noTagVariableFound,
				WebAnalyticsConstants.noTagVariableValueFound);
		return noMatchingAnalyticsTagVariableMapFound;
	}

	public List<SamePagesDataStore> normalizePageDataMaps(
			Map<String, WebAnalyticsPageData> baseLinePageDataMap,
			Map<String, WebAnalyticsPageData> testPageDataMap) {

		List<SamePagesDataStore> allSideBySidePagesList = new ArrayList<SamePagesDataStore>();
		String baselineKey;
		String testDataKey;
		String allPageDataKey;
		SamePagesDataStore samePagesDataStore;// new SamePagesDataStore();
		Iterator<String> baseLinePageDataMapIterator = baseLinePageDataMap
				.keySet().iterator();
		// loop through the baseline iterator, if not found in testData, insert
		// null object in testData and vice versa
		while (baseLinePageDataMapIterator.hasNext()) {
			baselineKey = baseLinePageDataMapIterator.next();
			// System.out.println("baselineKey :"+ baselineKey);
			// System.out.println("tags size :"+testPageDataMap.get(baselineKey).getWebAnalyticsTagDataMap().size());
			if (testPageDataMap.get(baselineKey) == null) { // if key not found,
															// insert a dummy
															// page object in
															// the map
				// WebAnalyticsPageData noMatchingAnalyticsPageDataFound= new
				// WebAnalyticsPageData();
				// noMatchingAnalyticsPageDataFound.setPageTitle(noMatchingAnlyticsPageFound);
				testPageDataMap.put(baselineKey, createDummyPageData());
			}
			// System.out.println("tags size 1:"+testPageDataMap.get(baselineKey).getWebAnalyticsTagDataMap().size());
		}

		Iterator<String> testDataPageDataMapIterator = testPageDataMap.keySet()
				.iterator();

		while (testDataPageDataMapIterator.hasNext()) {
			testDataKey = testDataPageDataMapIterator.next();
			// System.out.println("testDataKey :"+ testDataKey);
			// System.out.println("tags size :"+baseLinePageDataMap.get(testDataKey).getWebAnalyticsTagDataMap().size());
			if (baseLinePageDataMap.get(testDataKey) == null) { // if key not
																// found, insert
																// a dummy page
																// object in the
																// map
				// WebAnalyticsPageData noMatchingAnalyticsPageDataFound= new
				// WebAnalyticsPageData();
				// noMatchingAnalyticsPageDataFound.setPageTitle(noMatchingAnlyticsPageFound);
				baseLinePageDataMap.put(testDataKey, createDummyPageData());
			}
			// System.out.println("tags size 2 :"+baseLinePageDataMap.get(testDataKey).getWebAnalyticsTagDataMap().size());
		}

		// after above normalization both maps will have equal entries
		// page1, page1
		// page2, page2
		// etc#, etc#
		// add these to SamePageDataStore
		// add SamePageDataStore to list
		Iterator<String> allPageDataIterator = baseLinePageDataMap.keySet()
				.iterator();

		while (allPageDataIterator.hasNext()) {
			samePagesDataStore = new SamePagesDataStore();
			allPageDataKey = allPageDataIterator.next();
			// System.out.println("allPageDataKey :"+ allPageDataKey);
			// System.out.println("tags size :"+baseLinePageDataMap.get(allPageDataKey).getWebAnalyticsTagDataMap().size());
			samePagesDataStore
					.setWebAnalyticsPageDataBaseline(baseLinePageDataMap
							.get(allPageDataKey));
			// System.out.println("tags size 3:"+samePagesDataStore.getWebAnalyticsPageDataBaseline().getWebAnalyticsTagDataMap().size());
			samePagesDataStore.setWebAnalyticsPageDataTestData(testPageDataMap
					.get(allPageDataKey));
			// System.out.println("tags size 4:"+samePagesDataStore.getWebAnalyticsPageDataTestData().getWebAnalyticsTagDataMap().size());
			allSideBySidePagesList.add(samePagesDataStore);
		}

		// normalize tagData
		// each page have same number of tags. if not equal insert null in the
		// position
		// each tag must have the same number of params, insert key, with null
		// value

		// normalize tag data
		return normalizeTagData(allSideBySidePagesList);
	}

	// this method receives the normalized pageData
	// normalize the tags and tag variable data and send it back to
	// normalizePageData to put in
	private List<SamePagesDataStore> normalizeTagData(
			List<SamePagesDataStore> allSideBySidePagesList) {

		SamePagesDataStore samePagesDataStoreNormalized;
		List<SamePagesDataStore> allSideBySidePagesListNormalized = new ArrayList<SamePagesDataStore>();
		Map<String, WebAnalyticsTagData> baselineTagDataMap;
		Map<String, WebAnalyticsTagData> testDataTagDataMap;

		WebAnalyticsPageData baselinePageData;
		WebAnalyticsPageData testDataPageData;

		for (SamePagesDataStore samePagesDataStore : allSideBySidePagesList) {
			// for two similar pages, get similar tag and normalize their hash
			// map data

			samePagesDataStoreNormalized = new SamePagesDataStore();

			baselinePageData = samePagesDataStore
					.getWebAnalyticsPageDataBaseline();
			testDataPageData = samePagesDataStore
					.getWebAnalyticsPageDataTestData();

			// System.out.println("testDataPageData :" + testDataPageData);
			// System.out.println("testDataPageData.getHarLogFileName() "+
			// testDataPageData.getHarLogFileName());
			// System.out.println("testDataPageData.getApplicationName() "+testDataPageData.getApplicationName());
			// System.out.println("testDataPageData.getPageUrl() "+
			// testDataPageData.getPageUrl());

			baselineTagDataMap = baselinePageData.getWebAnalyticsTagDataMap();
			testDataTagDataMap = testDataPageData.getWebAnalyticsTagDataMap();

			// find the position of each tag using key), if corresponding tag is
			// not found in the same position insert it, do this two ways,
			// baseline and testdata
			// System.out.println("file name :"+baselinePageData.getHarLogFileName()
			// );
			// System.out.println("baseLineTagDataMapsize :" +
			// baselineTagDataMap.size());
			Iterator<String> baselineTagDataMapItrator = baselineTagDataMap
					.keySet().iterator();

			String baselineKey;

			// normalize testDataMap
			while (baselineTagDataMapItrator.hasNext()) {
				baselineKey = baselineTagDataMapItrator.next();

				// System.out.println("testDataTagDataMap :" +
				// testDataTagDataMap);
				if (testDataTagDataMap.get(baselineKey) == null) { // if tag not
																	// found in
																	// testData,insert
																	// a
																	// "TagNotFound"
					// WebAnalyticsTagData noMatchingWebAnalyticsTagData=new
					// WebAnalyticsTagData();
					// noMatchingWebAnalyticsTagData.setTagType(noMatchingTagFound);
					testDataTagDataMap.put(baselineKey, createDummyTagData());

				}
			}

			// set the normalized testDataTagDataMap back in the
			// testDataPagaData
			testDataPageData.setWebAnalyticsTagDataMap(testDataTagDataMap);

			Iterator<String> testDataTagDataMapIterator = testDataTagDataMap
					.keySet().iterator();
			String testDataKey;
			// normalize baselineMap
			while (testDataTagDataMapIterator.hasNext()) {
				testDataKey = testDataTagDataMapIterator.next();
				if (baselineTagDataMap.get(testDataKey) == null) { // if tag not
																	// found in
																	// testData,insert
																	// a
																	// "TagNotFound"
					// WebAnalyticsTagData noMatchingWebAnalyticsTagData=new
					// WebAnalyticsTagData();
					// noMatchingWebAnalyticsTagData.setTagType(noMatchingTagFound);
					baselineTagDataMap.put(testDataKey, createDummyTagData());

				}
			}
			// set the normalized baselineTagDataMap back in the
			// baselinePageData
			baselinePageData.setWebAnalyticsTagDataMap(baselineTagDataMap);

			// populate the samePagesDatastore
			samePagesDataStoreNormalized
					.setWebAnalyticsPageDataBaseline(baselinePageData);
			samePagesDataStoreNormalized
					.setWebAnalyticsPageDataTestData(testDataPageData);

			// populate the allSideBySidePagesList with
			// samePagesDataStoreNormalized
			allSideBySidePagesListNormalized.add(samePagesDataStoreNormalized);
		}

		// normalize tagVariableData and return

		return normalizeTagVariableData(allSideBySidePagesListNormalized);

	}

	public List<SamePagesDataStore> normalizeTagVariableData(
			List<SamePagesDataStore> allSideBySidePagesList) {

		SamePagesDataStore samePagesDataStoreNormalized;
		List<SamePagesDataStore> allSideBySidePagesListNormalized = new ArrayList<SamePagesDataStore>();
		Map<String, WebAnalyticsTagData> baselineTagDataMap;
		Map<String, WebAnalyticsTagData> testDataTagDataMap;
		Map<String, String> baselineTagDataVariableMap;
		Map<String, String> testDataTagDataVariableMap;

		WebAnalyticsPageData baselinePageData;
		WebAnalyticsPageData testDataPageData;

		for (SamePagesDataStore samePagesDataStore : allSideBySidePagesList) {
			// for two similar pages, get similar tag and normalize their hash
			// map data

			samePagesDataStoreNormalized = new SamePagesDataStore();

			baselinePageData = samePagesDataStore
					.getWebAnalyticsPageDataBaseline();
			testDataPageData = samePagesDataStore
					.getWebAnalyticsPageDataTestData();

			baselineTagDataMap = baselinePageData.getWebAnalyticsTagDataMap();
			testDataTagDataMap = testDataPageData.getWebAnalyticsTagDataMap();

			// loop through each datatag..after the page and tag normalization,
			// there must be equal entries in both baseline and testData TagData
			// maps
			Iterator<String> baselineTagDataMapItrator = baselineTagDataMap
					.keySet().iterator();
			String baselineTagDataKey;

			// normalize tagDataVariableMap
			while (baselineTagDataMapItrator.hasNext()) {
				baselineTagDataKey = baselineTagDataMapItrator.next();
				// get the baseline and testData variable maps for each tag and
				// normalize

				baselineTagDataVariableMap = baselineTagDataMap.get(
						baselineTagDataKey).getTagVariableData();
				testDataTagDataVariableMap = testDataTagDataMap.get(
						baselineTagDataKey).getTagVariableData();

				Iterator baselineTagDataVariableIterator = baselineTagDataVariableMap
						.keySet().iterator();

				// normalize testDataVariableDataMap;
				String baselineVariableDataKey;

				while (baselineTagDataVariableIterator.hasNext()) {
					baselineVariableDataKey = baselineTagDataVariableIterator
							.next().toString();
					if (testDataTagDataVariableMap.get(baselineVariableDataKey) == null) { // if
																							// tag
																							// not
																							// found
																							// in
																							// testDataVariable,insert
																							// a
																							// "TagVariableNotFound"
						testDataTagDataVariableMap.put(baselineVariableDataKey,
								WebAnalyticsConstants.noTagVariableValueFound);
					}
				}

				Iterator testDataTagDataVariableIterator = testDataTagDataVariableMap
						.keySet().iterator();
				// normalize baselineDataVariableDataMap;
				String testDataVariableDataKey;

				while (testDataTagDataVariableIterator.hasNext()) {
					testDataVariableDataKey = testDataTagDataVariableIterator
							.next().toString();
					if (baselineTagDataVariableMap.get(testDataVariableDataKey) == null) { // if
																							// tag
																							// not
																							// found
																							// in
																							// testDataVariable,insert
																							// a
																							// "TagVariableNotFound"
						baselineTagDataVariableMap.put(testDataVariableDataKey,
								WebAnalyticsConstants.noTagVariableValueFound);
					}
				}

				// populate the tagData with normalized tagVariableData
				// populate the page data with tagData containing the normalized
				// tagvariableData
				baselineTagDataMap.get(baselineTagDataKey).setTagVariableData(
						baselineTagDataVariableMap);

				testDataTagDataMap.get(baselineTagDataKey).setTagVariableData(
						testDataTagDataVariableMap);

			}
			// populate the pageData with normalized tagData
			baselinePageData.setWebAnalyticsTagDataMap(baselineTagDataMap);
			testDataPageData.setWebAnalyticsTagDataMap(testDataTagDataMap);

			// populate samePagesDataStoreNormalized with the normalized
			// pageData
			samePagesDataStoreNormalized
					.setWebAnalyticsPageDataBaseline(baselinePageData);
			samePagesDataStoreNormalized
					.setWebAnalyticsPageDataTestData(testDataPageData);

			// populate the normalized pages into normalized list.
			allSideBySidePagesListNormalized.add(samePagesDataStoreNormalized);
		}

		return allSideBySidePagesListNormalized;
	}

	// compare data in two different maps each map representing data from harlog
	// diectories
	// write the result to excel sheet
	private void comparePageDataMaps(
			Map<String, WebAnalyticsPageData> baseLineMap,
			Map<String, WebAnalyticsPageData> testDataMap) {

		// System.out.println("baseline map size :" +baseLineMap.size());
		// System.out.println("testdata map size :" +testDataMap.size());

		// compare page by page analytics data
		// baseline:app->page ->tag->params(name,value)
		// testData:app->page ->tag->params(name,value)

		// TestXStream testXstream = new TestXStream();

		// testXstream.testXstream(baseLineMap);

		Iterator baselineMapKeyIterator = baseLineMap.keySet().iterator();
		WebAnalyticsPageData baseLinePageData;
		Map<String, WebAnalyticsTagData> webAnalyticsTagDataMap;
		Iterator webAnalyticsTagDataMapIterator;
		WebAnalyticsTagData webAnalyticsTagData;

		while (baselineMapKeyIterator.hasNext()) {
			baseLinePageData = (WebAnalyticsPageData) baseLineMap
					.get(baselineMapKeyIterator.next());
			// System.out.println("Tags Details for Page :" +
			// baseLinePageData.getApplicationName()+
			// " ,"+baseLinePageData.getPageTitle() +" ,"+
			// baseLinePageData.getWebAnalyticsTagDataMap().size() +
			// " ;"+baseLinePageData.getHarLogFileName());

			webAnalyticsTagDataMap = baseLinePageData
					.getWebAnalyticsTagDataMap();
			webAnalyticsTagDataMapIterator = webAnalyticsTagDataMap.keySet()
					.iterator();

			while (webAnalyticsTagDataMapIterator.hasNext()) {
				webAnalyticsTagData = (WebAnalyticsTagData) webAnalyticsTagDataMap
						.get(webAnalyticsTagDataMapIterator.next());
				// System.out.println("TagType :"
				// +webAnalyticsTagData.getTagType());
				// System.out.println("TagVariableData :"
				// +webAnalyticsTagData.getTagVariableData().toString());
			}
		}

		// sort the maps
		if (baseLineMap.size() == testDataMap.size()) {
			// no need to add any buffer cells to represent the data in excel
			// System.out.println("size is equal *****************");

		}

		if (baseLineMap.size() < testDataMap.size()) {
			// add buffer to map1 for writing data to excel
		}

		if (testDataMap.size() > testDataMap.size()) {
			// add buffer to map2 for writing data to excel.

		}

		// String resultsExcelFile =
		// "C:\\workspace\\CaptureNetworkTraffic\\test.xlsx";
		// new ExcelWriter(resultsExcelFile,
		// "HarReports").writeToExcel(baseLineMap);
	}

	// read all har files from a directory, populate in a hashmap and return the
	// hashmap
	// read files from directory
	// send one file at a time to readHarLog
	// create a hash map <key , webAnalyticsDataObject>
	// poplate the hashmap
	// return the hasmap

	public Map<String, WebAnalyticsPageData> getWebAnlyticsPageDataFromHarLogs(
			String harLogsDir, String applicationName, String tagSignatureJsonFilePath) {
		// contains the analytics data from all har/har zips files in a
		// directory
		Map<String, WebAnalyticsTagData> webAnalyticsTagsAllFilesDataMap = new LinkedHashMap<String, WebAnalyticsTagData>();
		Map<String, WebAnalyticsPageData> webAnalyticsPagesAllFilesDataMap = new LinkedHashMap<String, WebAnalyticsPageData>();
		// contains the analytics data from one file
		Map<String, WebAnalyticsTagData> webAnalyticsTagsOneFileDataMap = new LinkedHashMap<String, WebAnalyticsTagData>();
		Map<String, WebAnalyticsPageData> webAnalyticsPagesOneFileDataMap = new LinkedHashMap<String, WebAnalyticsPageData>();
		Iterator webAnalyticsTagsOneFileDataIterator = null;
		Iterator webAnalyticsPagesOneFileDataIterator = null;
		FileListing listing = new FileListing();

		try {
			List<String> harLogFiles = listFilesForFolder(new File(harLogsDir));

			int totalFiles = harLogFiles.size();
			
			logger.info("Total HAR log files :"+totalFiles);
			
			int index = 0;
			for (String filePath : harLogFiles) {
				index++;
				logger.info("Reading HAR file :"+index + " of :"+totalFiles);
				System.out.println("Reading HAR file :"+index + " of :"+totalFiles);
				//logger.info("filePath :" + filePath);
				System.out.println("filePath :" + filePath);

				if ((filePath.endsWith(HarFileConstants.harFileExtension) || filePath
						.endsWith(HarFileConstants.harZipFileExtension))
						
						&& (filePath.contains(WebAnalyticsConstants.domainName)
						|| WebAnalyticsConstants.domainName.equals("*"))) {
					
					webAnalyticsPagesOneFileDataMap = getWebAnlyticsPageDataFromHarLog(filePath, applicationName, tagSignatureJsonFilePath);
					//System.out.println("Har log path :" + filePath);
					//System.out.println("webAnalyticsPagesOneFileDataMap.size :"
					//+webAnalyticsPagesOneFileDataMap.size());
					//logger.info("webAnalyticsPagesOneFileDataMap :"+webAnalyticsPagesOneFileDataMap);
					
					//logger.info("Har log path :" + filePath);
					//logger.info("webAnalyticsPagesOneFileDataMap.size :"	+ webAnalyticsPagesOneFileDataMap.size());
					
					webAnalyticsTagsOneFileDataIterator =
					webAnalyticsTagsOneFileDataMap.keySet().iterator();
					webAnalyticsPagesOneFileDataIterator = webAnalyticsPagesOneFileDataMap
							.keySet().iterator();
					//System.out.println("webAnalyticsTagsOneFileDataMap.keySet()"+ webAnalyticsTagsOneFileDataMap.keySet().size());
					//logger.info("webAnalyticsTagsOneFileDataMap.keySet()"+ webAnalyticsTagsOneFileDataMap.keySet().size());
					
					String oneFileKey = null;
					WebAnalyticsPageData oneFileValue = null;

					while (webAnalyticsPagesOneFileDataIterator.hasNext()) {
						// iterate through all the objects in the map and copy
						// into a master map for one directory
						oneFileKey = webAnalyticsPagesOneFileDataIterator
								.next().toString();
						// System.out.println("oneFileKey :" +oneFileKey);
						oneFileValue = webAnalyticsPagesOneFileDataMap
								.get(oneFileKey);
						
						if ( webAnalyticsPagesAllFilesDataMap.get(oneFileKey) != null ){
							System.out.println("Duplicate Key :"+oneFileKey);
							
							System.out.println("Existing value :"+webAnalyticsPagesAllFilesDataMap.get(oneFileKey));
							System.out.println("New value :"+oneFileValue);
							//TODO: Not sure whether we need to normalize, currently taking which ever is having more webAnalyticsTagData
							oneFileValue = compareAndGetOneFileValue(webAnalyticsPagesAllFilesDataMap.get(oneFileKey), oneFileValue);
							
							System.out.println("Choosed value :"+oneFileValue);
						}
						webAnalyticsPagesAllFilesDataMap.put(oneFileKey,
								oneFileValue);
						/*logger.info("webAnalyticsPagesAllFilesDataMap.size :"
										+ webAnalyticsPagesAllFilesDataMap
												.size());
						System.out.println("webAnalyticsPagesAllFilesDataMap.size :"
								+ webAnalyticsPagesAllFilesDataMap
										.size());*/
					}
					//logger.info("webAnalyticsPagesAllFilesDataMap.size :"+webAnalyticsPagesAllFilesDataMap.size());
					//System.out.println("webAnalyticsPagesAllFilesDataMap.size :"+webAnalyticsPagesAllFilesDataMap.size());
				}

			} //end for

		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Error in getWebAnlyticsPageDataFromHarLogs :", e);
			e.printStackTrace();
		}

		return webAnalyticsPagesAllFilesDataMap;
	}

	
	private WebAnalyticsPageData compareAndGetOneFileValue(
			WebAnalyticsPageData webAnalyticsPageData,
			WebAnalyticsPageData newWebAnalyticsPageData) {
		Map<String, WebAnalyticsTagData> existingTagDataMap = webAnalyticsPageData
				.getWebAnalyticsTagDataMap();
		Map<String, WebAnalyticsTagData> newTagDataMap = newWebAnalyticsPageData
				.getWebAnalyticsTagDataMap();

		WebAnalyticsPageData maxEntriesWebAnalyticsPageData = webAnalyticsPageData;
		
		for (Map.Entry<String, WebAnalyticsTagData> newTagDataMapEntry : newTagDataMap
				.entrySet()) {
			WebAnalyticsTagData newWebAnalyticsTagDatavalue = newTagDataMapEntry
					.getValue();
			boolean found = false;
			for (Map.Entry<String, WebAnalyticsTagData> existingTagDataMapEntry : existingTagDataMap
					.entrySet()) {
				String key = existingTagDataMapEntry.getKey();
				WebAnalyticsTagData webAnalyticsTagDatavalue = existingTagDataMapEntry
						.getValue();

				if (webAnalyticsTagDatavalue.getTagDataKey().equalsIgnoreCase(
						newWebAnalyticsTagDatavalue.getTagDataKey())) {
					found = true;
					break;
				}
			}
			if (!found) {
				maxEntriesWebAnalyticsPageData = newWebAnalyticsPageData;
				break;
			}
		}

		return maxEntriesWebAnalyticsPageData;
	}

	// single harlog can contain information about more than one page, app and
	// the analytics tags on the page.
	// returns pages in a har log file with tags and with no-tags
	public Map<String, WebAnalyticsPageData> getWebAnlyticsPageDataFromHarLog(
			String filePath, String applicationName, String tagSignatureJsonFilePath) {

		// Hashmap to hold the analytics data for each page
		// key= <appName>::<pageName>::<refererUrl>::<requestUrl>::<tagType>
		// value=analyticsData populated with values required for excel test
		// report generation

		InputStream inputStream = null;
		ZipFile zipFile = null;
		HarFileReader harFileReader = null;

		List<TagSignaturesObject> tagSignatures = TagSignatureReader.getTagSignatures(tagSignatureJsonFilePath);

		Map<String, WebAnalyticsPageData> allPageDataInAFileMap = new LinkedHashMap<String, WebAnalyticsPageData>();
		Map<String, WebAnalyticsTagData> allTagsDataInAFileMap = new LinkedHashMap<String, WebAnalyticsTagData>();

		final String gomezTagID1 = "axf8.net/mr/b.gif";
		final String gomezTagID2 = "axf8.net/mr/e.gif";
		final String googleAnalyticsTagID1 = "g.doubleclick.net";
		final String googleAnalyticsTagID2 = "google-analytics.com";
		final String coreMetricsTag = "data.coremetrics.com";
		final String comScoreTagID = "b.scorecardsearch.com";
		final String sitecatalystTagID = "metrics.tiaa-cref.org";// "wfainternet.d1.sc.omtrdc.net";//
		final String addthisTagID = "addthis.com/live";
		final String doubleclickTagID = "fls.doubleclick.net";
		final String testAndTargetTagID = "tt.omtrdc.net"; // only for www1 url
															// //tiaacref.tt.omtrdc.net
		final String opinionLabTagID1 = "oo_conf_tab.js"; // oo_conf_tab.js,
															// oo_engine.min.js
		final String opinionLabTagID2 = "oo_engine.min.js";
		


		// har file might have data for more than one app
		Map<String, String> appNameMap = new LinkedHashMap<String, String>();
		Map<String, String> refererUrlMap = new LinkedHashMap<String, String>();

		String requestMethod = "GET";
		int responseStatusCode = 200;
		WebAnalyticsTagData webAnalyticsTagData = new WebAnalyticsTagData();
		WebAnalyticsPageData webAnalyticsPageData = new WebAnalyticsPageData();
		WebAnalyticsPageData webAnalyticsPageWithNoTags = new WebAnalyticsPageData();
		// List<WebAnalyticsTagData> webAnalyticsTagDataList;
		// initializing as these are leading to null pointer
		Map<String, WebAnalyticsTagData> webAnalyticsTagDataMap = new LinkedHashMap<String, WebAnalyticsTagData>();
		Map<String, WebAnalyticsTagData> webAnalyticsNoTagDataMap = new LinkedHashMap<String, WebAnalyticsTagData>();
		String appName = null;
		String pageName = null;
		String refererUrl = null;
		String requestUrl = null;
		String tagName = "No-Supported-Tag-Found";;
		String analyticsDataKey = null;
		
		Map<String, Integer> analyticsDataKeyCounter = new LinkedHashMap<String, Integer>();

		Map<String, BrokenUrlData> brokenUrlDatMap = new LinkedHashMap<String, BrokenUrlData>();
		;

		// pagename,tagsfound
		Map<String, Boolean> pageHasTagsMap = new LinkedHashMap<String, Boolean>();
		Boolean testboolean = new Boolean(false);

		File f = new File(filePath);


		// HarFileReader
		harFileReader = new HarFileReader();
		// HarFileWriter harFileWriter = new HarFileWriter();

		try {

			try {
				// log("Reading " + filePath, true);

				// All violations of the specification generate warnings
				List<HarWarning> warnings = new ArrayList<HarWarning>();
				HarLog harLog = null;

				if (filePath.endsWith(".har")) {
				
					harLog = harFileReader.readHarFile(f, warnings);
		
					} else if (filePath
						.endsWith(HarFileConstants.harZipFileExtension)) {
					zipFile = new ZipFile(filePath);
					inputStream = ZipFileUtils.readFromHarZipFile(zipFile);
					harLog = harFileReader.readHarFile(inputStream, warnings);
				}

				// mahesh -not complete yet
				/* code to detect any corruption in har file */
				/*for (HarWarning warn : warnings)
					logger.error("File:" + filePath + " - Warning:"
							+ warn);
				*/
				// HarLog log = r.readHarFile(f);

				// mahesh - not required, just data holder objects
				// Access all elements as objects
				//HarBrowser browser = harLog.getBrowser();
				//System.out.println("harlog object :" + harLog);
		
				HarEntries entries = harLog.getEntries();

				// Used for loops
				List<HarPage> pages = harLog.getPages().getPages();
				List<HarEntry> hentry = entries.getEntries();

				// getRefererUrlPerPage(hentry, pages,false);

				// to start with initialize the map assuming pages dont have
				// tags.
				for (HarPage page : pages) {
					pageHasTagsMap.put(page.getId(), new Boolean(false));
				}

				// har log may have more than one app data
				appNameMap = getApplicationsNamesFromHarLog(hentry, pages, applicationName);
				refererUrlMap = getRefererUrlPerPage(hentry, pages, true);
				HarPage pageVar;

				// for(HarPage page: pages){
				for (HarPage page : pages) {

					// create a new page object for every page
					// webAnalyticsPageData =new WebAnalyticsPageData();
					// webAnalyticsTagDataList = new
					// ArrayList<WebAnalyticsTagData>();
					webAnalyticsTagDataMap = new LinkedHashMap<String, WebAnalyticsTagData>();
					webAnalyticsPageData = new WebAnalyticsPageData();
					// webAnalyticsTagData = new WebAnalyticsTagData();

					// assuming tags will fire in sequence as the navigation
					// will be always be same by crawler
					
					/*
					int siteCatalystTagNum = 0;
					int ensightenTagNum = 0;
					int customLazyTagNum = 0;
					int addThisTagCountNum = 0;
					int gomezTagCountNum = 0;
					int doubleClickCountNum = 0;

					int googleAnalyticsCountNum = 0;
					int coreMetricsCountNum = 0;
					int opinionLabCountNum = 0;
					int testAndTargetCountNum = 0;
					int comScoreCountNum = 0;
					*/
					
					refererUrl = refererUrlMap.get(page.getId());
					// System.out.println("refererUrl " + refererUrl +" : "+
					// page.getId());

					// populate the analyticsData object and return to
					// getHarLogs
					for (HarEntry entry : hentry) {

						//change this to getting the context root two levels
						
						appName = appNameMap.get(entry.getPageRef()); // "Public";
																		// -
																		// mahesh
																		// determine
																		// if
																		// this
																		// is
																		// the
																		// way
																		// to
																		// get
																		// the
																		// appname
						// appName = getApplicationsNameFromFilePath(filePath);
						// //"Public";
						// mahesh -clean this up later

						//appName = applicationName;
						requestUrl = entry.getRequest().getUrl();
						requestMethod = entry.getRequest().getMethod();
						responseStatusCode = entry.getResponse().getStatus();

						// System.out.println("requestUrl :"+ requestUrl);
						// populate the AnalyticsData object. Represents data
						// for one
						// tag instance per page load

						// one analytics data object for per instance of tag
						// found
						// to-do process only analytics data from GET
						// requests...
						// analytics data from POST will be coded later
						// simply this line later wiht url comparing to map of
						// objects
					//	if(isSupportedTag()){}
						
						/*
						if ((requestUrl.contains(sitecatalystTagID)
								|| requestUrl.contains(gomezTagID1)
								|| requestUrl.contains(gomezTagID2)
								|| requestUrl.contains(coreMetricsTag)
								|| requestUrl.contains(addthisTagID)
								|| requestUrl.contains(addthisTagID)
								|| requestUrl.contains(googleAnalyticsTagID1)
								|| requestUrl.contains(googleAnalyticsTagID2)
								|| requestUrl.contains(opinionLabTagID1)
								|| requestUrl.contains(opinionLabTagID2)
								|| requestUrl.contains(comScoreTagID)
								|| requestUrl.contains(testAndTargetTagID) 
								|| requestUrl.contains(doubleclickTagID))
								|| isEnsightenTMS(requestUrl)
								|| isCustomLazyTag(requestUrl)
								)
						*/
						SupportedTagData std = TagSignatureUtils.getSupportedTagDetails(requestUrl, tagSignatures);
						
						if(std!=null && std.isSupportedTag()) 	{
							// System.out.println("supported tag found");

							/*
							 * && requestMethod.equalsIgnoreCase("GET") &&
							 * (requestUrl
							 * .contains("?")||requestUrl.contains(";")) //; is
							 * used as query param separator for doubleclick &&
							 * page
							 * .getId().equalsIgnoreCase(entry.getPageRef())) {
							 */

							webAnalyticsTagData = new WebAnalyticsTagData();

							pageName = getPageName(entry);

							// skip for addthis as addthis has its own referer
							// url different from tiaa-cref.
							/*
							 * if(!requestUrl.contains(addthisTagID)){
							 * refererUrl = getRefererUrl(entry); }
							 */
							tagName = std.getTagObject().getTagName();
							//tagType = getTagType(requestUrl);

							
							
							//maintains the tag sequence number for a specific tag type
							if(analyticsDataKeyCounter.containsKey(tagName)){
								//increment the value by one everytime the tag is found
								int tagSequenceNum =(analyticsDataKeyCounter.get(tagName).intValue())+1; 
								analyticsDataKeyCounter.put(tagName, new Integer(tagSequenceNum));
								
								analyticsDataKey = createAnalyticsDataKey(
										appName, pageName, refererUrl,
										requestUrl, tagName,
										tagSequenceNum);
								
							}
							else{
								int beginSequenceAt=1;
								analyticsDataKeyCounter.put(tagName, new Integer(beginSequenceAt));
								analyticsDataKey = createAnalyticsDataKey(
									appName, pageName, refererUrl,
									requestUrl, tagName,
									beginSequenceAt); //start the counter at 1 
							}
							
							// mark the pages which have tags;
							// since this marking is inside the if block
							// checking for tag presence on page
							if (pageHasTagsMap.containsKey(entry.getPageRef())) {
								// pages which dont have tags will have the
								// value=false in the map
								pageHasTagsMap.put(entry.getPageRef(),
										new Boolean(true));
								// System.out.println("total no of pages :" +
								// tagsFoundOnPages.size());
								// System.out.println("page name :"
								// +entry.getPageRef());
							}

							webAnalyticsTagData.setTagUrl(requestUrl);
							webAnalyticsTagData.setTagName(tagName);
							webAnalyticsTagData.setStartedDateTime(entry
									.getStartedDateTime());

							// key and value data pairs for each tag
							webAnalyticsTagData
									.setTagVariableData(getQueryParamNameValue(entry));

							webAnalyticsTagData.setTagDataKey(analyticsDataKey);

							// webAnalyticsTagData.setReadFromFile(filePath);

							// populate Page Data ->consists of the tags on the
							// page
							webAnalyticsPageData.setApplicationName(appName);
							webAnalyticsPageData.setPageRef(entry.getPageRef());

							webAnalyticsPageData.setStartedDateTime(entry
									.getStartedDateTime());

							// copy the each instance of analytics data to
							// master map
							allTagsDataInAFileMap.put(analyticsDataKey,
									webAnalyticsTagData);

							// add webAnalyticsTagData to the page object
							if (page.getId().equals(entry.getPageRef())) {
								// webAnalyticsTagDataList.add(webAnalyticsTagData);
								webAnalyticsTagDataMap.put(
										webAnalyticsTagData.getTagDataKey(),
										webAnalyticsTagData);

							}
							// System.out.println("abc :"+webAnalyticsTagDataMap.get(webAnalyticsTagData.getTagDataKey()));

							// System.out.println("page tags Map size>>>> :"
							// +webAnalyticsTagDataMap.size());
							webAnalyticsPageData
									.setWebAnalyticsTagDataMap(webAnalyticsTagDataMap);
							webAnalyticsPageData.setApplicationName(appName);
							webAnalyticsPageData.setPageTitle(page.getTitle());
							webAnalyticsPageData.setPageUrl(refererUrl);
							webAnalyticsPageData.setHarLogFileName(filePath);

							// get the pages with tags and put in the
							// allPageDataInAFileMap
							// add page objects to global page hashmap for all
							// entries, all files
							// page key = pageref +harlogfilepath

							allPageDataInAFileMap.put(
									createAnalyticsPageKey(appName,
											page.getTitle(), refererUrl,
											"filePath"), webAnalyticsPageData);
						}

					} // end entry for-loop

				}// end page for loop

				// scan the tagsFoundOnPages map to see if any page did not have
				// tags
				// go through the entries one more time to extract the request
				// url and referer url for those pages
				// print the appname, pagetitle, refererurl(only if it is
				// tiaa-cref.org and belongs to one of the apps),
				// create TagData object with "NotTagFoundValue and add to
				// PageData

				if (pageHasTagsMap.size() != 0) {
					// find if any of the entry has false
					Boolean pageValue = new Boolean(false);
					String pageKey = null;
					Iterator pageIterator = pageHasTagsMap.keySet().iterator();

					String appNameNoTag = "No-ApplicationName-Found";// null;
					String pageTitleNoTag = null;
					String refererUrlNoTag = null;
					// String noTagUrlFound="No-Tag-Url-Found";
					// String
					// noSupportedTagTypeFound="No-Supported-Tag-Type-Found";
					// String noTagVariableFound="No-Tag-Variable-Found";
					// String
					// noTagVariableValueFound="No-Tag-Variable-Value-Found";
					webAnalyticsNoTagDataMap = new LinkedHashMap<String, WebAnalyticsTagData>();
					Map<String, String> noTagVariableDataMap = new LinkedHashMap<String, String>();
					WebAnalyticsTagData webAnalyticsNoTagData;

					while (pageIterator.hasNext()) {

						pageKey = pageIterator.next().toString();
						pageValue = pageHasTagsMap.get(pageKey);
						// if any page had no tags
						// scan entries again to get the details of the page
						if (!pageValue.booleanValue()) {
							webAnalyticsPageWithNoTags = new WebAnalyticsPageData();

							for (HarEntry entry : hentry) {
								if (entry.getPageRef().equals(pageKey)) {

									pageTitleNoTag = getPageTitle(pages, entry); // page
																					// title
									// refererUrlNoTag=getRefererUrl(entry);
									// //page url
									refererUrlMap = getRefererUrlPerPage(
											hentry, pages, true);

									for (HarPage page : pages) {
										refererUrlNoTag = refererUrlMap
												.get(page.getId());
									}

									// refererUrlNoTag=getRefererUrl(entry);

									// appName =
									// appNameMap.get(entry.getPageRef());
									appNameNoTag = getApplicationsNamesFromHarLog(
											hentry, pages, applicationName).get(
											entry.getPageRef());// "Public";

									// System.out.println("appNameNoTag :"+appNameNoTag
									// +" : "+entry.getPageRef() + " : " +
									// filePath);

								}
								webAnalyticsPageWithNoTags
										.setApplicationName(appNameNoTag);
								webAnalyticsPageWithNoTags
										.setPageTitle(pageTitleNoTag);
								webAnalyticsPageWithNoTags
										.setPageUrl(refererUrlNoTag);
								webAnalyticsPageWithNoTags
										.setHarLogFileName(filePath);

								// create a dummy webAnalyticsTagData
								webAnalyticsNoTagData = new WebAnalyticsTagData();
								webAnalyticsNoTagData
										.setTagName(WebAnalyticsConstants.noSupportedTagTypeFound);
								webAnalyticsNoTagData
										.setTagUrl(WebAnalyticsConstants.noTagUrlFound);
								webAnalyticsNoTagData
										.setTagDataKey(WebAnalyticsConstants.noSupportedTagTypeFound);
								noTagVariableDataMap
										.put(WebAnalyticsConstants.noTagVariableFound,
												WebAnalyticsConstants.noTagVariableValueFound);
								webAnalyticsNoTagData
										.setTagVariableData(noTagVariableDataMap);
								webAnalyticsNoTagDataMap
										.put(WebAnalyticsConstants.noTagVariableFound,
												webAnalyticsNoTagData);
								// System.out.println("cbc :"+webAnalyticsNoTagDataMap.get(WebAnalyticsConstants.noTagVariableFound));

								// populate page with dummy analytics tag
								webAnalyticsPageWithNoTags
										.setWebAnalyticsTagDataMap(webAnalyticsNoTagDataMap);
								webAnalyticsPageWithNoTags
										.setStartedDateTime(entry
												.getStartedDateTime());

							}
							// put the pages with no-tags in the master page
							// list before returning
							allPageDataInAFileMap.put(
									createAnalyticsPageKey(appNameNoTag,
											pageTitleNoTag, refererUrlNoTag,
											"filePath"),
									webAnalyticsPageWithNoTags);
						} // if block checking for pages with no tags
					}// iterate all pages

				} // if block for pageHasTagsMap

				/*
				 * // Once you are done manipulating the objects, write back to
				 * a file System.out.println("Writing " + "fileName" + ".test");
				 * File f2 = new File("fileName" + ".test"); w.writeHarFile(log,
				 * f2);
				 */
			} catch (JsonParseException e) {
				e.printStackTrace();
				// fail("Parsing error during test");
			} catch (IOException e) {
				e.printStackTrace();
				// fail("IO exception during test");
			} catch (Exception e) {
				e.printStackTrace();
			}

		} finally {
			try {

				if (inputStream != null) {
					// System.out.println("closing streams");
					inputStream.close();
					inputStream = null;
				}

				if (zipFile != null) {
					// System.out.println("closing zip file handlers");
					zipFile.close();
					zipFile = null;
				}

				if (harFileReader != null) {
					// System.out.println("closing harFileReader");
					harFileReader = null;
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		/*	*/
		// return allTagsDataInAFileMap - contains pages with and without tags
		// System.out.println("size :" +allPageDataInAFileMap.size() +"; "+
		// filePath );
		return allPageDataInAFileMap;
	}

	private boolean isCustomLazyTag(String requestUrl) {
		
		
		if(
				( (			requestUrl.startsWith("https://") || requestUrl.startsWith("http://")) 
						&&  requestUrl.contains(".tiaa-cref.org/"))  &&
				
				(
				requestUrl.contains("/tiaacrefAnalytics.js")    ||
				requestUrl.contains("/js_site_catalyst_lazy.js")    
				)
				
				)//end if 			
				{
			return true;
				}
		else{
			return false;
		}
		
	}

	private boolean isEnsightenTMS(String requestUrl) {
		if(
				( (requestUrl.startsWith("https://") || requestUrl.startsWith("http://")) && (requestUrl.contains("nexus.ensighten.com/") || 
				requestUrl.contains(".tiaa-cref.org/")))  &&
				
				(
				requestUrl.contains("/Bootstrap.js")    ||
				requestUrl.contains("/bootstrap.js")    ||
				requestUrl.contains("/serverComponent.php") ||
				requestUrl.contains("/code/")
				)
				
				)//end if 			
				{
			return true;
				}
		else{
			return false;
		}
			
				
	}

	public Map<String, BrokenUrlData> getBrokenUrlDataFromHarLogs(
			String harLogsDir, String applicationName, boolean collectAllUrls, List<Link> navigationList, boolean generateReportsOnlyForRefererUrl) {

		BrokenUrlData brokenUrlData;// = new BrokenUrlData();
		InputStream inputStream = null;
		ZipFile zipFile = null;
		HarFileReader harFileReader = null;// = new HarFileReader();

		// map containing all the broken urls corresponding to pages found in
		// har log
		// this map will again be normalized(make unique) against all the files
		// in har directory
		Map<String, BrokenUrlData> brokenUrlDatMap = new LinkedHashMap<String, BrokenUrlData>();
		// har file might have data for more than one app
		Map<String, String> appNameMap;// = new LinkedHashMap<String, String>();

		try {
			List<String> harLogFiles = listFilesForFolder(new File(harLogsDir));
			int totalFiles = harLogFiles.size();
			logger.info("Total files to process :" + harLogFiles.size());

			int index =0;
			for (String filePath : harLogFiles) {
				boolean ajaxCallFound = false;
				index++;
				logger.info("Processing file "+ index + " of "+ totalFiles);
				System.out.println("Processing file "+ index + " of "+ totalFiles);
				if ((WebAnalyticsConstants.domainName.equals("*") || filePath
						.contains(WebAnalyticsConstants.domainName))
						&& (filePath
								.endsWith(HarFileConstants.harFileExtension) || filePath
								.endsWith(HarFileConstants.harZipFileExtension))) {

					//logger.info("Processing broken urls from file :" + filePath);

					File f = new File(filePath);
					// HarFileReader
					harFileReader = new HarFileReader();
					// HarFileWriter harFileWriter = new HarFileWriter();

					int responseStatusCode = 999;// =200;
					String responseStatusText = "Not Defined";
					String requestMethod;// ="GET";
					String requestUrl;
					String appName;
					String pageTitle;
					String refererUrl = WebAnalyticsConstants.noDataFound;
					String errorType = "";
					try {
						// All violations of the specification generate warnings
						List<HarWarning> warnings = new ArrayList<HarWarning>();

						HarLog harLog = null;// r.readHarFile(f, warnings);

						if (filePath
								.endsWith(HarFileConstants.harFileExtension)) {
							harLog = harFileReader.readHarFile(f, warnings);
						} else if (filePath
								.endsWith(HarFileConstants.harZipFileExtension)) {
							zipFile = new ZipFile(filePath);
							inputStream = ZipFileUtils
									.readFromHarZipFile(zipFile);
							harLog = harFileReader.readHarFile(inputStream,
									warnings);
						}

						HarEntries entries = harLog.getEntries();

						// Used for loops
						List<HarPage> pages = harLog.getPages().getPages();
						List<HarEntry> hentry = entries.getEntries();
						
						Map<String, String> refererUrlMap = getRefererUrlPerPage(
								hentry, pages, true);
						
						// populate the broken urls info in a map object to
						// avoid duplicates resources loaded from multiple
						// pages.
						// this data will be populated in a separate sheet of
						// excel workbook

						// har log may have more than one app data
						// appNameMap = new LinkedHashMap<String, String>();
						
						appNameMap = getApplicationsNamesFromHarLog(hentry,
								pages, applicationName);
						
						int noOfEntries = hentry.size();
						
						for (HarEntry entry : hentry) {
							brokenUrlData = new BrokenUrlData();
							
							responseStatusCode = entry.getResponse()
									.getStatus();
							responseStatusText = entry.getResponse()
									.getStatusText();
							appName = appNameMap.get(entry.getPageRef());

							// System.out.println("appName :" +appName );
							if (appName == null) {
								appName = "NoApplicationNameFound";
							}
							requestUrl = entry.getRequest().getUrl();
							requestMethod = entry.getRequest().getMethod();
							pageTitle = getPageTitle(pages, entry);

							Iterator okResponseStatusCodesIterator = WebAnalyticsConstants.okResponseStatusCodes
									.keySet().iterator();
							boolean matchesOkStatusCode = false;
							int okResponseCode;

							while (okResponseStatusCodesIterator.hasNext()) {
								matchesOkStatusCode = false;
								okResponseCode = ((Integer) okResponseStatusCodesIterator
										.next()).intValue();
								// System.out.println("okResponseCode :" +
								// okResponseCode);
								if (responseStatusCode == okResponseCode) {
									matchesOkStatusCode = true;
									break;
								}
							}

							//logger.info("pageTitle :"+pageTitle + " collectAllUrls:"+collectAllUrls);
							if ( collectAllUrls || !matchesOkStatusCode) {
								//logger.info("In if pageTitle:"+pageTitle);
								// System.out.println("in if");
								// System.out.println("requestUrl :" +
								// requestUrl);
								// System.out.println("statuscode :"
								// +responseStatusCode);

								brokenUrlData.setAppName(appName);
								brokenUrlData.setPageTitle(pageTitle);
								// brokenUrlData.setRefererUrl(getRefererUrl(entry));

								for (HarPage page : pages) {
									refererUrl = refererUrlMap
											.get(page.getId());
								}

								brokenUrlData.setRefererUrl(refererUrl);

								brokenUrlData.setResourceUrl(requestUrl);
								brokenUrlData.setStatusCode(responseStatusCode);
								brokenUrlData.setStatusText(responseStatusText);
								brokenUrlData.setRequestMethod(requestMethod);
								brokenUrlData.setHarFileName(filePath);
								
								if ( ! ajaxCallFound ){
									ajaxCallFound = checkIsAjaxCallMade(entry.getRequest());

									if ( ajaxCallFound ) {
										brokenUrlData.setAjaxCallMade("YES");
									}else{
										brokenUrlData.setAjaxCallMade("NO");
									}
								}
								// add to the map
								// this way for each page of har log, we are
								// getting unique broken url
								// brokenUrlDatMap.put(createBrokenUrlKey(appName,getRefererUrl(entry),requestUrl),
								// brokenUrlData);
								//logger.info("requestUrl :"+requestUrl);
								//logger.info("brokenUrlData :"+brokenUrlData);
								if ( generateReportsOnlyForRefererUrl && StringUtils.endsWithIgnoreCase(requestUrl, refererUrl)){
									//System.out.println("refererUrl :"+refererUrl);
									
									List<Link>navigationListForReferer = getNavigationListForRefererUrl(refererUrl, navigationList);
									if ( null != navigationListForReferer && navigationListForReferer.size() > 1 ){
										logger.warn("Referrer Url :"+refererUrl + " has more than One Navigation mapping Found.");
										logger.warn("navigationListForReferer :"+navigationListForReferer);
										
										//System.out.println("Referrer Url :"+refererUrl + " has more than One Navigation mapping Found.");
										//System.out.println("navigationListForReferer :"+navigationListForReferer);
									}
									//logger.info("navigationListForReferer :"+navigationListForReferer);
									//Getting from navigationList
									brokenUrlData.setParentUrl(getParentUrl(navigationListForReferer));
									brokenUrlData.setErrorPage(checkIsErrorPage(navigationListForReferer));
									
									//if ( brokenUrlData.isErrorPage() ){
										//brokenUrlData.setErrorType(getErrorType(navigationListForReferer));
									//}
									errorType = getErrorType(navigationListForReferer);
									if ( null != errorType && errorType.trim().length() > 0 ){
										brokenUrlData.setErrorPage(true);
										brokenUrlData.setErrorType(errorType);
									}
									
									brokenUrlData.setNavigationPath(getNavigationForReferer(navigationListForReferer));
									brokenUrlData.setLinkName(getLinkName(navigationListForReferer));

									Map<String, Long> pageLoadAttributes = getPageLoadAttributeMap(hentry.get(noOfEntries-1), pages, true);
									brokenUrlData.setPageLoadAttributes(pageLoadAttributes);
									
									brokenUrlDatMap.put(requestUrl, brokenUrlData);
								}
								if ( ! generateReportsOnlyForRefererUrl ) {
									brokenUrlDatMap.put(requestUrl, brokenUrlData);
								}

							} // end if

						} // end for
					} catch (JsonParseException e) {
						e.printStackTrace();
						// fail("Pasrsing error during test");
					} catch (IOException e) {
						e.printStackTrace();
						// fail("IO exception during test");
					}
				} // check if file ends with .har
			} // dir loop
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			try {
				//System.out.println("closing streams");
				if (inputStream != null) {
					inputStream.close();
					inputStream = null;
				}

				if (zipFile != null) {
					zipFile.close();
					zipFile = null;
				}

				if (harFileReader != null) {
					harFileReader = null;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//System.out.println("brokenUrlDatMap :"+brokenUrlDatMap);
		return brokenUrlDatMap;
	}

	
	private Map<String, Long> getPageLoadAttributeMap(HarEntry entry,
			List<HarPage> pages, boolean b) {
		//System.out.println("getPageLoadAttributeMap");
		Map<String, Long> pageLoadAttributesMap = new HashMap<String, Long>();
		for ( HarPage page: pages){
			
			if ( null != page.getId() && null != entry ){
				if (page.getId().equals(entry.getPageRef())) {
					pageLoadAttributesMap.put("onLoad", page.getPageTimings().getOnLoad());
					pageLoadAttributesMap.put("contentLoad", page.getPageTimings().getOnContentLoad());

					
					
					pageLoadAttributesMap.put("blocked", entry.getTimings().getBlocked());
					pageLoadAttributesMap.put("dns", entry.getTimings().getDns());
					pageLoadAttributesMap.put("connect", entry.getTimings().getConnect());
					pageLoadAttributesMap.put("send", entry.getTimings().getSend());
					pageLoadAttributesMap.put("wait", entry.getTimings().getWait());
					pageLoadAttributesMap.put("receive", entry.getTimings().getReceive());
					
					long lastEntryLoadTime = 0;
					
					for (Map.Entry<String, Long> pageLoadAttributesMapEntry : pageLoadAttributesMap.entrySet()) {
					    String key = pageLoadAttributesMapEntry.getKey();
					    Long value = pageLoadAttributesMapEntry.getValue();
					    if ( value > lastEntryLoadTime) {
					    	lastEntryLoadTime = value;
					    }
					}

					long timeInMilliSeconds = ( entry.getStartedDateTime().getTime() + lastEntryLoadTime ) - page.getStartedDateTime().getTime();
					pageLoadAttributesMap.put("totalLoadTime", timeInMilliSeconds);
				}
			}
		}
		//System.out.println("pageLoadAttributesMap :"+pageLoadAttributesMap);
		return pageLoadAttributesMap;
	}

	private boolean checkIsAjaxCallMade(HarRequest request) {
		HarHeaders header = request.getHeaders();
		
		List<HarHeader> headerList = header.getHeaders();
		
		for ( HarHeader harHeader: headerList ){
			
			if ( null != harHeader.getName() && harHeader.getName().toLowerCase().equals("x-requested-with")
					&& null != harHeader.getValue() && harHeader.getValue().toLowerCase().equals("xmlhttprequest")){
				return true;
			}
		}
		return false;
	}

	private String getLinkName(List<Link> navigationListForReferer) {
		String linkName = "";
		for(Link link : navigationListForReferer){
			linkName = link.getLinkName();
			
			if ( null != linkName && linkName.trim().length() > 0 ){
				return linkName;
			}
		}
		return linkName;
	}

	private String getErrorType(List<Link> navigationListForReferer) {
		String errorType = "";
		//System.out.println("Total Navigation links :"+navigationListForReferer.size());
		for(Link link : navigationListForReferer){
			errorType = link.getErrorType();
			
			if ( null != errorType && errorType.trim().length() > 0 ){
				return errorType;
			}
		}
		return errorType;
	}

	public void getWebAnalyticsSiteInventory(String harLogsDir, String tagSignatureJsonFile, String resultFilePath ){
		InputStream inputStream = null;
		ZipFile zipFile = null;
		HarFileReader harFileReader = null;// = new HarFileReader();
		Set<String> tagsSet = new HashSet<String>();
	
		List<TagSignaturesObject> tagSignatures = TagSignatureReader.getTagSignatures(tagSignatureJsonFile);

		// har file might have data for more than one app
		Map<String, String> appNameMap;// = new LinkedHashMap<String, String>();
		
		FileUtils fileUtils = new FileUtils();
		PrintWriter printWriter =fileUtils.openFile(resultFilePath,true,"someString");
	
			List<String> harLogFiles = listFilesForFolder(new File(harLogsDir));
			logger.info("Total files to process :" + harLogFiles.size());
			int i=0;
			for (String filePath : harLogFiles) {

				System.out.println("Processing file :" + i++ + ":" + filePath);
				
				if ((WebAnalyticsConstants.domainName.equals("*") || filePath
						.contains(WebAnalyticsConstants.domainName))
						&& (filePath
								.endsWith(HarFileConstants.harFileExtension) || filePath
								.endsWith(HarFileConstants.harZipFileExtension))) {

				
					File f = new File(filePath);
					// HarFileReader
					harFileReader = new HarFileReader();				
					String requestUrl;

					try {
						// All violations of the specification generate warnings
						List<HarWarning> warnings = new ArrayList<HarWarning>();

						HarLog harLog = null;// r.readHarFile(f, warnings);

						if (filePath
								.endsWith(HarFileConstants.harFileExtension)) {
							harLog = harFileReader.readHarFile(f, warnings);
						} else if (filePath
								.endsWith(HarFileConstants.harZipFileExtension)) {
							zipFile = new ZipFile(filePath);
							inputStream = ZipFileUtils
									.readFromHarZipFile(zipFile);
							harLog = harFileReader.readHarFile(inputStream,
									warnings);
						}

						HarEntries entries = harLog.getEntries();

						// Used for loops
						List<HarPage> pages = harLog.getPages().getPages();
						List<HarEntry> hentry = entries.getEntries();

						Map<String, String> pageUrlMap = getRefererUrlPerPage(
								hentry, pages, true);
						

						// har log may have more than one app data
						// appNameMap = new LinkedHashMap<String, String>();
						appNameMap = getApplicationsNamesFromHarLog(hentry,
								pages, "Public");
					
							WebAnalyticsInventoryData wid = new WebAnalyticsInventoryData();							

						for (HarEntry entry : hentry) {
						
							
							requestUrl = entry.getRequest().getUrl();
						//	System.out.println("Entry :" +requestUrl);
							SupportedTagData std = TagSignatureUtils.getSupportedTagDetails(requestUrl, tagSignatures);
						
						if(std!=null && std.isSupportedTag()) {//check if the url is a supported tag url
							//System.out.println("Tag found : "+ std.getTagObject().getTagName());
							wid.setAppName(appNameMap.get(entry.getPageRef()));
							wid.setPageTitle(getPageTitle(pages).get(entry.getPageRef()));	
							wid.setPageUrl(pageUrlMap.get(entry.getPageRef()));
							wid.setPageName(getPageName(entry));
							wid.setResponseCode(entry.getResponse().getStatus());
						    wid.setTagSize(entry.getResponse().getBodySize());
							wid.setTagUrl(requestUrl);
							String tagName=std.getTagObject().getTagName();
							String tagType=std.getTagObject().getTagType();
							wid.setTagName(tagName);
							wid.setTagType(tagType);
							//wid.setTagsList(tagsSet);
							
							if(!tagsSet.contains(tagName)){
							tagsSet.add(tagName);
							}

							String printResult=prepareForCSV(wid.getPageUrl()) + prepareForCSV(wid.getTagName()) + prepareForCSV(wid.getTagUrl()) + prepareForCSV(Long.toString(wid.getTagSize()));
							fileUtils.writeLineToFile(printWriter, printResult);
						
						} //if supported tag
						
						} //loop entries
						
					} catch (JsonParseException e) {
						e.printStackTrace();
						// fail("Parsing error during test");
					} catch (IOException e) {
						e.printStackTrace();
						// fail("IO exception during test");
					} catch(Exception e){
						e.printStackTrace();
					}
					finally {

						try {
							//System.out.println("closing streams");
							if (inputStream != null) {
								inputStream.close();
								inputStream = null;
							}

							if (zipFile != null) {
								zipFile.close();
								zipFile = null;
							}

							if (harFileReader != null) {
								harFileReader = null;
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					
				} // check if file ends with .har
			} // dir loop
		
			//print all tags from all the har logs
			for(String tagname:tagsSet){
			System.out.println("Tag :"+ tagname);	
			}
			
			fileUtils.closeFile(printWriter);	
					
	}
		
		
	private String prepareForCSV(String text){
		return "\""+ text + "\", ";
	}
	
	private String getNavigationForReferer(List<Link> navigationListForReferer) {
		String navigationPath = "";
		for(Link link : navigationListForReferer){
			navigationPath += link.getNavigationPath() + ",";
		}
		
		if ( navigationPath.length() > 1 ){
			navigationPath = navigationPath.substring(0, navigationPath.length()-1);
		}
		return navigationPath;
	}

	private boolean checkIsErrorPage(List<Link> navigationListForReferer) {
		for(Link link : navigationListForReferer){
			if ( link.isErrorPage()){
				return true;
			}
		}
		return false;
	}

	private String getParentUrl(List<Link> navigationListForReferer) {
		String parentUrl = "";
		for(Link link : navigationListForReferer){
			parentUrl += link.getParentUrl() + ",";
		}
		
		if ( parentUrl.length() > 1 ){
			parentUrl = parentUrl.substring(0, parentUrl.length()-1);
		}
		return parentUrl;
	}

	private List<Link> getNavigationListForRefererUrl(String refererUrl,
			List<Link> navigationList) {
		List<Link>navigationForReferer = new ArrayList<Link>();

		try{
			if ( null != navigationList ){
				
				for(Link link : navigationList){
					if ( null != link.getUrl() && link.getUrl().trim().equals(refererUrl.trim())){
						navigationForReferer.add(link);
					}
				}
			}
		}catch(Exception e){
			logger.error("Exception while finding navigation path for the URL :"+refererUrl);
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
		}
		return navigationForReferer;
	}

	public String getTagType(String url) {
		String tagKey;
		String tagType = "No-Supported-Tag-Found";
		Iterator tagTypeIterator = WebAnalyticsConstants.tagTypeMap.keySet()
				.iterator();

		while (tagTypeIterator.hasNext()) {
			tagKey = tagTypeIterator.next().toString();
			if (url.contains(tagKey)) {
				tagType = WebAnalyticsConstants.tagTypeMap.get(tagKey);
			}
		}

		// System.out.println("tagType :" +tagType);
		return tagType;

	}

	public String createBrokenUrlKey(String appName, String refererUrl,
			String resourceUrl) {
		StringBuffer buffer = new StringBuffer();
		String keyDelimiter = "[#]";

		buffer.append(appName);
		buffer.append(keyDelimiter);

		buffer.append(refererUrl);
		buffer.append(keyDelimiter);

		buffer.append(resourceUrl);
		buffer.append(keyDelimiter);

		return buffer.toString();
	}

	// key=<appName>::<pageTitle>::<pageRef>::<harLogPath>::<refererUrl>
	public String createAnalyticsPageKey(String appName, String pageTitle,
			String refererUrl, String filePath) {
		StringBuffer buffer = new StringBuffer();
		String keyDelimiter = "[#]";

		buffer.append(appName);
		buffer.append(keyDelimiter);

		buffer.append(pageTitle);
		buffer.append(keyDelimiter);
		buffer.append(filePath);
		buffer.append(keyDelimiter);

		/*
		 * buffer.append(pageRef); buffer.append(keyDelimiter);
		 */

		// referer url is the pageUrl
		buffer.append(refererUrl);
		// System.out.println("page key : "+ buffer.toString());

		return buffer.toString();
	}

	// key= <appName>::<refererUrl>::<requestUrl>::<tagType>::<tagSequence>
	public String createAnalyticsDataKey(String appName, String pageName,
			String refererUrl, String requestUrl, String tagType,
			int tagSequence) {

		StringBuffer buffer = new StringBuffer();
		String keyDelimiter = "[#]";

		
		//TODO, get the pev value and use it, this is only for adobe Analytics, as per disussion with Mahesh. If there is pev vlaue don't use the sequence.
		Map<String, String> queryMap = null;
		if ( tagType.equals("Adobe Analytics")){
			//get pev param value from requestUrl, there is a method to get it use. (use parameter name and value as in the URL)
			queryMap = getQueryMap(requestUrl);
		}
		if ( null != queryMap && null != queryMap.get("pev2") ){
			buffer.append(queryMap.get("pev2"));
		}else{
			buffer.append(tagSequence);
		}
		buffer.append(keyDelimiter);
		buffer.append(appName);
		buffer.append(keyDelimiter);
		// buffer.append(pageName);
		// buffer.append(keyDelimiter);
		buffer.append(tagType);
		buffer.append(keyDelimiter);
		buffer.append(refererUrl);
		// buffer.append(keyDelimiter);
		// buffer.append(requestUrl);

		return buffer.toString();
	}

	// obtain the requested page title
	public String getPageTitle(List<HarPage> pages, HarEntry entry) {

		String pageTitle = WebAnalyticsConstants.noDataFound;// "Not Available";
		// we will obtain the page title by collating the entry.pageref value
		// with pages.id
		for (HarPage page : pages) {
			if (page.getId().equals(entry.getPageRef())) {
				pageTitle = page.getTitle();
			}
		}

		return pageTitle;

	}
	
	public Map<String, String> getPageTitle(List<HarPage> pages){
		Map<String, String> pageTitleMap = new HashMap<String, String>();
		for (HarPage page : pages) {
			pageTitleMap.put(page.getId(), page.getTitle());
		}
		
		return pageTitleMap;
	}

	// obtain the referer url for the requested url
	// if you want the pageUrl do not use this method, use getRefererUrlPerPage instead 
	public String getRefererUrl(HarEntry entry) {
		String refererUrl =null;//= WebAnalyticsConstants.noDataFound;// "Not Available";
		Iterator iterator = entry.getRequest().getHeaders().getHeaders()
				.iterator();
		while (iterator.hasNext()) {
			HarHeader harHeader = (HarHeader) iterator.next();
			if (harHeader.getName().equalsIgnoreCase("Referer")){
				refererUrl = harHeader.getValue();
				// clean the refererurl before returning
				// found some double slashes for some url on public site
				refererUrl = refererUrl.replace("://", "***").replace("//", "/")
						.replace("***", "://");

			}
		}

		//System.out.println("url :"+ entry.getRequest().getUrl());
		//System.out.println("refererUrl: " + refererUrl);

		return refererUrl;
	}

	public String getPageName(HarEntry entry) {
		String pageName = WebAnalyticsConstants.noDataFound;// "Not Available";
		pageName = getQueryParamNameValue(entry).get("pageName");
		return pageName;
	}

	// get the name value pair form the GET request issued from browser to
	// analytics engine
	public Map<String, String> getQueryParamNameValue(HarEntry entry) {
		Map<String, String> queryParamNameValueMap = new LinkedHashMap<String, String>();
		String tagUrl = entry.getRequest().getUrl();
		
		if (tagUrl.contains("doubleclick.net")) {
			queryParamNameValueMap = processDoubleClickData(tagUrl);
			
		} else {

			Iterator<HarQueryParam> it = entry.getRequest().getQueryString()
					.getQueryParams().iterator();

			while (it.hasNext()) {
				HarQueryParam hqp = (HarQueryParam) it.next();
				//for sitecatalyst tags, filter by query param name
				//System.out.println(tagUrl);
				if(tagUrl.contains("metrics.") && WebAnalyticsConstants.siteCatalystPrefVars.contains(hqp.getName())){
				//System.out.println( "if :"+hqp.getName());
				queryParamNameValueMap.put(hqp.getName(), hqp.getValue());
				}
				else if (!tagUrl.contains("metrics.")){
					queryParamNameValueMap.put(hqp.getName(), hqp.getValue());
				}
				
			}
		}

		// some tags may not have any query params.. to avoid data corruption,
		// insert a "NoTagVarNameFound", "NoTagVarValueFound"
		if (queryParamNameValueMap.size() == 0) {
			queryParamNameValueMap.put("NoTagVarNameFound",
					"NoTagVarValueFound");
		}
		return queryParamNameValueMap;
	}
	
	
	public String getPageURL(String pageRef, List<HarEntry> entries,
			List<HarPage> pages, boolean includeHostPort){
		
		return	getRefererUrlPerPage(entries,
				pages, includeHostPort).get(pageRef);
	
	}

	public Map<String, String> getRefererUrlPerPage(List<HarEntry> entries,
			List<HarPage> pages, boolean includeHostPort) {
		String pageUrl = "";
		String pageRef;
		String refererUrl=null;

		Map<String, Integer> urlForPagesIndexMap = new LinkedHashMap<String, Integer>();
		Map<String, String> urlForPagesMap = new LinkedHashMap<String, String>();
		List<String> pageRefList = new ArrayList<String>();
		String id;
		int index = 900000;

		Map<String, Integer> maxOccuredRefererUrl = new LinkedHashMap<String, Integer>();
		Map<String, Map<String, Integer>> urlForPagesIndexMapMaxOccur = new LinkedHashMap<String, Map<String, Integer>>();
		
		for (HarEntry entry : entries) {
			// url = entry.getRequest().getUrl();

			pageRef = entry.getPageRef();
			pageRefList.add(pageRef);
			index = pageRefList.indexOf(pageRef);
			// add only the first found entry relevant to a pageref as that is
			// most likely the pageUrl for a page.

			refererUrl = getRefererUrl(entry);
			
			if ( refererUrl != null) {
				if ( maxOccuredRefererUrl.containsKey(refererUrl)) {
					Integer occurrenceCount = maxOccuredRefererUrl.get(refererUrl);
					occurrenceCount++;
					maxOccuredRefererUrl.put(refererUrl, occurrenceCount);
				}else{
					maxOccuredRefererUrl.put(refererUrl, 1);
				}
				
				urlForPagesIndexMapMaxOccur.put(pageRef, maxOccuredRefererUrl);
			}
			
			/*if (!urlForPagesIndexMap.containsKey(pageRef)) {
				urlForPagesIndexMap.put(pageRef, index);
				refererUrl = getRefererUrl(entries.get(index));
				System.out.println("refererUrl :"+refererUrl);
				
				
				if (refererUrl == null) { // if referer header is missing for
											// the first entry:
											// entry.getrequest.geturl is the
											// pageurl
					pageUrl = entries.get(index).getRequest().getUrl();
				} else { // if for the first entry for a pageref referer header
							// is not missing, set the referer header value as
							// the pageurl
					pageUrl = refererUrl;
				}

				// removes the protocol+host+port section of the url
				// this allows comparison of results across envioronments
				if (!includeHostPort) {
					try {
						URL urlTemp = new URL(pageUrl);
						pageUrl = urlTemp.getFile();

					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				// replacements if required to allow matching urls between two
				// envioronments

				// System.out.println("pageUrl :" + pageUrl);
				urlForPagesMap.put(pageRef, pageUrl);
			}*/
			
		}
		//System.out.println("maxOccuredRefererUrl :"+maxOccuredRefererUrl);
		//System.out.println("urlForPagesIndexMapMaxOccur :"+urlForPagesIndexMapMaxOccur);
		//System.out.println("urlForPagesMap :"+urlForPagesMap);
		
		/*
		 * Iterator it =urlForPagesMap.keySet().iterator(); String key;
		 * while(it.hasNext()){ key=it.next().toString();
		 * System.out.println("url : "+ key + " :"+ urlForPagesMap.get(key) ); }
		 */
		
		for( Map.Entry<String, Map<String, Integer>> mapByPageRef: urlForPagesIndexMapMaxOccur.entrySet()){
			
			String pageRefForMaxOccurKey = mapByPageRef.getKey();
			Map<String, String> maxOccurRefererMap = new LinkedHashMap<String, String>();
			
			int maxValue = 0;
			for ( Map.Entry<String, Integer> mapByMaxOccurReferer : mapByPageRef.getValue().entrySet()){
				if ( maxValue < mapByMaxOccurReferer.getValue()){
					maxValue = mapByMaxOccurReferer.getValue();
					maxOccurRefererMap.put(pageRefForMaxOccurKey, mapByMaxOccurReferer.getKey());
				}
			}
			
			if ( null != pageRefForMaxOccurKey && null != maxOccurRefererMap.get(pageRefForMaxOccurKey)){
				String maxOccurredRefererUrl = maxOccurRefererMap.get(pageRefForMaxOccurKey);
				if (!includeHostPort) {
					try {
						URL urlTemp = new URL(maxOccurredRefererUrl);
						maxOccurredRefererUrl = urlTemp.getFile();

					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				urlForPagesMap.put(pageRefForMaxOccurKey, maxOccurredRefererUrl);
			}
		}
		
		System.out.println("urlForPagesMap :"+urlForPagesMap);
		return urlForPagesMap;
	}

	public String getApplicationsNameFromFilePath(String filePath) {
		Map<String, String> appNamesMap = new LinkedHashMap<String, String>();
		Map<String, String> refererUrlForPagesMap = new LinkedHashMap<String, String>();
		Iterator appNameIterator;
		String contextRoot = null;
		String appName = null;

		if (filePath.contains(WebAnalyticsConstants.domainName)
				|| WebAnalyticsConstants.domainName.equals("*")) {
			// check for the context root
			appNameIterator = WebAnalyticsConstants.applicationsNameMap
					.keySet().iterator();

			while (appNameIterator.hasNext()) {
				// contextRoot is the key
				contextRoot = appNameIterator.next().toString();
				if (filePath.contains(contextRoot)) {
					// key is pageref value
					// value is the appName
					appNamesMap.put(filePath,
							WebAnalyticsConstants.applicationsNameMap
									.get(contextRoot));
					appName = WebAnalyticsConstants.applicationsNameMap.get(
							contextRoot).toString();
				}

				else {
					if (appNamesMap.get(filePath) == null
							|| appNamesMap.get(filePath).equals("")) {
						appNamesMap.put(filePath, filePath);
					}
				}

			}
		}

		return appName;
	}

	// determine the applications name from the har log data, the log may have
	// data for more than one app
	public Map<String, String> getApplicationsNamesFromHarLog(
			List<HarEntry> hentry, List<HarPage> pages, String applicationName) {
		Map<String, String> appNamesMap = new LinkedHashMap<String, String>();
		Map<String, String> refererUrlForPagesMap = new LinkedHashMap<String, String>();
		String url = null;
		String refererUrl = null;
		Iterator appNameIterator;
		String contextRoot = null;
		String pageRef;

		refererUrlForPagesMap = getRefererUrlPerPage(hentry, pages, true);
		
		//logger.info("refererUrlForPagesMap :"+refererUrlForPagesMap);
		//logger.info("applicationName :"+applicationName);

		for (HarEntry harEntry : hentry) {
			url = harEntry.getRequest().getUrl();
			pageRef = harEntry.getPageRef().toString();
			refererUrl = refererUrlForPagesMap.get(harEntry.getPageRef());// getRefererUrl(harEntry);

			//System.out.println("refererUrl :"+refererUrl);

			if ( null != refererUrl && refererUrl.contains(WebAnalyticsConstants.domainName)
					|| WebAnalyticsConstants.domainName.equals("*")) {
				// check for the context root
				appNameIterator = WebAnalyticsConstants.applicationsNameMap
						.keySet().iterator();

				while (appNameIterator.hasNext()) {
					// contextRoot is the key
					contextRoot = appNameIterator.next().toString();
					//System.out.println("contextRoot :"+contextRoot);
					if ( null != refererUrl && refererUrl.contains(contextRoot)) {
						// key is pageref value
						// value is the appName
						
						appNamesMap.put(
								pageRef,
								WebAnalyticsConstants.applicationsNameMap.get(
										contextRoot).toString());
					}

					else {
						if (appNamesMap.get(harEntry.getPageRef()) == null
								|| appNamesMap.get(harEntry.getPageRef())
										.equals("")) {
							//TODO: Dhinakar putting as application name
							//appNamesMap.put(harEntry.getPageRef().toString(),
									//refererUrl);
							//logger.info("refererUrl :"+refererUrl);
							//logger.info("applicationName :"+applicationName);
							appNamesMap.put(harEntry.getPageRef().toString(), applicationName);
						}
					}

				}
			}

		}
		return appNamesMap;
	}

	public void log(String log, boolean on) {
		if (on == true)
			System.out.println(log);
	}

	public Map<String, String> processDoubleClickData(String url) {
		Map<String, String> queryParamMap = new LinkedHashMap<String, String>();
		// System.out.println("doubleclick.net url" + url);
		String newurl = url.replace(";", "&").replaceFirst("&", "?");
		try {
			queryParamMap = splitQuery(new URL(newurl));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println("doubleclick.net url" + newurl);

		return queryParamMap;

	}

	public static Map<String, String> splitQuery(URL url)
			throws UnsupportedEncodingException {
		Map<String, String> query_pairs = new LinkedHashMap<String, String>();
		String query = url.getQuery();
		if ( null != query){
			String[] pairs = query.split("&");
			if ( null != pairs ){
				for (String pair : pairs) {
					if ( null != pair){
						int idx = pair.indexOf("=");
						
						if ( idx > 0 )
						query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"),
								URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
					}
				}
			}
		}
		return query_pairs;
	}

	// generate report based on functional groups
	// one excel for one functional group
	// if a functional group cannot be established, report for pages with no
	// functional group be generated separately

	// get a list of functional group
	public void generateAuditAndTestFuncGroupedExcelReport(
			WebAnalyticsUtils wau, String baseLineHarDirPath,
			String testHarDirPath, String excelReportFilePath,
			String siteUrlStartsWith, AnalyticsDataReportingUtils excelWriter, String applicationName, String tagSignatureJsonFilePath) {

		// get functional groups for baseline and testdata maps. this function
		// to return a set of functions

		Map<String, WebAnalyticsPageData> baseLinePageDataMap = wau
				.getWebAnlyticsPageDataFromHarLogs(baseLineHarDirPath, applicationName, tagSignatureJsonFilePath);
		// HashSet funcGroupBaseline =
		// getFunctionalGroupSet(baseLinePageDataMap);

		Map<String, WebAnalyticsPageData> baseLinePageDataFilteredMap = wau
				.filterCommonTagsFiredFromPageAudit(baseLinePageDataMap,
						siteUrlStartsWith); // "c34

		Map<String, WebAnalyticsPageData> testPageDataMap = wau
				.getWebAnlyticsPageDataFromHarLogs(testHarDirPath, applicationName, tagSignatureJsonFilePath);
		Map<String, WebAnalyticsPageData> testPageDataFilteredMap = wau
				.filterCommonTagsFiredFromPageAudit(testPageDataMap,
						siteUrlStartsWith); // "c34
	}

	public void generateAuditAndTestExcelReport(WebAnalyticsUtils wau,
			String baseLineHarDirPath, String testHarDirPath,
			String excelReportFilePath, AnalyticsDataReportingUtils excelWriter, String applicationName, String tempDirectory, String tagSignatureJsonFilePath, boolean collectOnlyFailureUrls) {
		try {
		Map<String, WebAnalyticsPageData> baseLinePageDataMap = wau
				.getWebAnlyticsPageDataFromHarLogs(baseLineHarDirPath, applicationName, tagSignatureJsonFilePath);
		Map<String, WebAnalyticsPageData> testPageDataMap = wau
				.getWebAnlyticsPageDataFromHarLogs(testHarDirPath, applicationName, tagSignatureJsonFilePath);
		List<SamePagesDataStore> samePagesDataStoreList = wau
				.normalizePageDataMaps(baseLinePageDataMap, testPageDataMap);
		//System.out.println("Total normalized pages in list >> :"
				//+ samePagesDataStoreList.size());
		//System.out.println("\n");
		String excelFileName = excelReportFilePath;
		excelWriter.writeAuditAndTestDetailsToExcel(samePagesDataStoreList,
				excelFileName, tempDirectory,false, "", "", collectOnlyFailureUrls);// writeNormalizedPagesListDataToExcel(samePagesDataStoreList);
		//System.out.println("written WebAnalytics Data to : " + excelFileName);
		} catch(Exception e) {
			logger.error("Error in WebAnalyticsUtils class:generateAuditAndTestExcelReport");
			e.printStackTrace();
		}
	}

	// this method removes the tags which are duplicates across multiple har
	// files
	public void generateAuditAndTestFilteredExcelReport(WebAnalyticsUtils wau,
			String baseLineHarDirPath, String testHarDirPath,
			String excelReportFilePath, String tagVarNameToFilter,
			AnalyticsDataReportingUtils excelWriter, String applicationName, String tempDirectory, String tagSignatureJsonFilePath, boolean collectOnlyFailureUrls) {

		Map<String, WebAnalyticsPageData> baseLinePageDataMap = wau
				.getWebAnlyticsPageDataFromHarLogs(baseLineHarDirPath, applicationName, tagSignatureJsonFilePath);
		Map<String, WebAnalyticsPageData> baseLinePageDataFilteredMap = wau
				.filterCommonTagsFiredFromPageAudit(baseLinePageDataMap,
						tagVarNameToFilter); // "c34

		Map<String, WebAnalyticsPageData> testPageDataMap = wau
				.getWebAnlyticsPageDataFromHarLogs(testHarDirPath, applicationName, tagSignatureJsonFilePath);
		Map<String, WebAnalyticsPageData> testPageDataFilteredMap = wau
				.filterCommonTagsFiredFromPageAudit(testPageDataMap,
						tagVarNameToFilter); // "c34

		List<SamePagesDataStore> samePagesDataStoreFilteredList = wau
				.normalizePageDataMaps(baseLinePageDataFilteredMap,
						testPageDataFilteredMap);

		//System.out
				//.println("Total normalized  pages in list with filtered Tag var >> :"
						//+ samePagesDataStoreFilteredList.size());
		//System.out.println("\n");

		String excelFileName = excelReportFilePath.replace(".xlsx",
				DateTimeUtil.getCurrentDateTime() + "_filtered.xlsx");
		excelWriter.writeAuditAndTestDetailsToExcel(
				samePagesDataStoreFilteredList, excelFileName, tempDirectory,false, "", "", collectOnlyFailureUrls);
		//System.out.println("written filtered WebAnalytics Data to : "
				//+ excelFileName);
	}

	public void generateBrokenUrlReport(WebAnalyticsUtils wau,
			String harDirPath, String batchID, String brokenUrlReportFilePath,
			AnalyticsDataReportingUtils excelWriter, String applicationName, boolean collectAllUrls, 
			String tempDirectory, List<Link> navigationList, boolean generateReportsOnlyForRefererUrl, boolean generatePageloadAttributes, boolean beautify) {

		Map<String, BrokenUrlData> brokenUrlDataMap = new LinkedHashMap<String, BrokenUrlData>();
		try {
		//boolean collectAllUrls = false;
		// test Broken url method
		brokenUrlDataMap = wau.getBrokenUrlDataFromHarLogs(harDirPath, applicationName, collectAllUrls, navigationList, generateReportsOnlyForRefererUrl);
		String csvEscaper = "\"";
		String csvSeparator = ",";
		Iterator brokenUrlDatIterator = brokenUrlDataMap.keySet().iterator();
		String key;
		//System.out.println("Broken Urls count :" + brokenUrlDataMap.size());
		/*
		 * FileUtils fileUtils = new FileUtils(); PrintWriter printWriter
		 * =fileUtils.openFile(pathToCsvFile, true, batchID);
		 */
		excelWriter.writeBrokenUrlDetailsToExcel(brokenUrlDataMap,
				brokenUrlReportFilePath, tempDirectory, generateReportsOnlyForRefererUrl, generatePageloadAttributes, beautify);
		} catch(Exception e) {
			logger.error("Exception in WebAnalyticsUtils class: generateBrokenUrlReport ");
			e.printStackTrace();
		}

		/*
		 * 
		 * while(brokenUrlDatIterator.hasNext()){
		 * key=brokenUrlDatIterator.next().toString();
		 * 
		 * fileUtils.writeLineToFile(printWriter, csvEscaper+
		 * brokenUrlDataMap.get(key).getResourceUrl() +csvEscaper+
		 * 
		 * csvSeparator+
		 * 
		 * csvEscaper+ brokenUrlDataMap.get(key).getRefererUrl() +csvEscaper+
		 * 
		 * csvSeparator+ brokenUrlDataMap.get(key).getStatusCode() );
		 * 
		 * 
		 * }
		 */

	}

	/*******
	 * For Status 200 value to be set added by 303780
	 * 
	 * @param model
	 * @return
	 */
	Map<String, Integer> getStatus200FromHarLogs(String harLogsDir, String applicationName) {
		int status200 = 0;
		HarResponse harResponse;
		// = new BrokenUrlData();

		// map containing all the broken urls corresponding to pages found in
		// har log
		// this map will again be normalized(make unique) against all the files
		// in har directory
		Map<String, Integer> status200Map = new LinkedHashMap<String, Integer>();
		// har file might have data for more than one app
		Map<String, String> appNameMap;// = new LinkedHashMap<String, String>();

		List<String> harLogFiles = listFilesForFolder(new File(harLogsDir));

		for (String filePath : harLogFiles) {
			if (filePath.endsWith(".har")) {

				File f = new File(filePath);
				HarFileReader r = new HarFileReader();
				HarFileWriter w = new HarFileWriter();

				int responseStatusCode;// =200;
				String requestMethod;// ="GET";
				String requestUrl;
				String appName;
				String pageTitle;

				try {
					// All violations of the specification generate warnings
					List<HarWarning> warnings = new ArrayList<HarWarning>();

					HarLog harLog = r.readHarFile(f, warnings);
					HarEntries entries = harLog.getEntries();

					// Used for loops
					List<HarPage> pages = harLog.getPages().getPages();
					List<HarEntry> hentry = entries.getEntries();

					// populate the broken urls info in a map object to avoid
					// duplicates resources loaded from multiple pages.
					// this data will be populated in a separate sheet of excel
					// workbook

					// har log may have more than one app data
					// appNameMap = new LinkedHashMap<String, String>();
					appNameMap = getApplicationsNamesFromHarLog(hentry, pages, applicationName);

					for (HarEntry entry : hentry) {

						responseStatusCode = entry.getResponse().getStatus();
						appName = appNameMap.get(entry.getPageRef());
						// System.out.println("appName :" +appName );
						if (appName == null) {
							appName = "NoApplicationNameFound";
						}
						requestUrl = entry.getRequest().getUrl();
						requestMethod = entry.getRequest().getMethod();
						pageTitle = getPageTitle(pages, entry);

						Iterator okResponseStatusCodesIterator = WebAnalyticsConstants.okResponseStatusCodes
								.keySet().iterator();
						boolean matchesOkStatusCode = false;
						int okResponseCode;

						while (okResponseStatusCodesIterator.hasNext()) {
							matchesOkStatusCode = false;
							okResponseCode = ((Integer) okResponseStatusCodesIterator
									.next()).intValue();
							// System.out.println("okResponseCode :" +
							// okResponseCode);
							if (responseStatusCode == okResponseCode) {
								matchesOkStatusCode = true;

								break;
							}
						}

						if (matchesOkStatusCode) {

							// System.out.println("in if");
							// System.out.println("requestUrl :" + requestUrl);
							// System.out.println("statuscode :"
							// +responseStatusCode);

							status200++;
							// add to the map
							// this way for each page of har log, we are getting
							// unique broken url
							// brokenUrlDatMap.put(createBrokenUrlKey(appName,getRefererUrl(entry),requestUrl),
							// brokenUrlData);
							status200Map.put(appName, status200++);

						} // end if

					} // end for
				} catch (JsonParseException e) {
					e.printStackTrace();
					// fail("Parsing error during test");
				} catch (IOException e) {
					e.printStackTrace();
					// fail("IO exception during test");
				}
			} // check if file ends with .har
		} // dir loop

		return status200Map;
	}

	/*******
	 * For Status 200 value to be set added by 303780
	 * 
	 * @param model
	 * @return
	 */

	public AnalyticsAuditSummary getSummaryView(String baseLineHarDirPath,
			String testHarDirPath, int scheduleId, String applicationName, String excelFilePath, String sheetName, boolean generateReportsOnlyForRefererUrl, String tagSignatureJsonFilePath) {
		// int scheduleID = 0 ;
		boolean transactionTestcaseFlag = false;
		Map<String, BrokenUrlData> brokenUrlDataMap = new LinkedHashMap<String, BrokenUrlData>();
		Map<String, Integer> status200Map = new LinkedHashMap<String, Integer>();
		status200Map = getStatus200FromHarLogs(testHarDirPath, applicationName);

		AnalyticsDataReportingUtils excelWriter = new AnalyticsDataReportingUtils(
				excelFilePath, sheetName);
		boolean collectAllUrls = false;
		List<Link> navigationList = new ArrayList<Link>();
		brokenUrlDataMap = getBrokenUrlDataFromHarLogs(testHarDirPath, applicationName, collectAllUrls, navigationList, generateReportsOnlyForRefererUrl);

		//logger.info("brokenUrlDataMap :" + brokenUrlDataMap);
		Map<String, WebAnalyticsPageData> baseLinePageDataMap = getWebAnlyticsPageDataFromHarLogs(testHarDirPath, applicationName, tagSignatureJsonFilePath);
		
		//logger.info("baseLinePageDataMap :"+baseLinePageDataMap);

		Map<String, WebAnalyticsPageData> testPageDataMap = getWebAnlyticsPageDataFromHarLogs(testHarDirPath, applicationName, tagSignatureJsonFilePath);

		//logger.info("testPageDataMap :"+testPageDataMap);
		
		List<SamePagesDataStore> samePagesDataStoreList = normalizePageDataMaps(
				baseLinePageDataMap, testPageDataMap);
		// generate analytics summary
		Map<String, AnalyticsSummaryReportData> webAnalyticsAppsSummary = excelWriter
				.writeWebAnalyticsAppsSummaryToExcel(samePagesDataStoreList,
						brokenUrlDataMap, status200Map);

		//logger.info("webAnalyticsAppsSummary :"+webAnalyticsAppsSummary);
		DataPopulationIntoDB dataPopulationIntoDB = new DataPopulationIntoDB();
		String finalXMLdata = dataPopulationIntoDB
				.populateSummaryViewXML(webAnalyticsAppsSummary);// data to be
																	// marshalled
																	// and the
																	// final
																	// output
																	// XML data
																	// is taken
		AnalyticsAuditSummary analyticsAuditSummary = new AnalyticsAuditSummary();
		analyticsAuditSummary.setScheduleId(scheduleId);
		analyticsAuditSummary.setAnalyticsAuditXml(finalXMLdata);
		analyticsAuditSummary
				.setTransactionTestcaseFlag(transactionTestcaseFlag);

		return analyticsAuditSummary;

	}

	/*******
	 * For Populating Values into DB with the values of the detailed view
	 * added by 303780
	 * 
	 * @param model
	 * @return
	 */
	
	public List<DetailedView> getDetailedView(String harDirPath, int scheduleId, String applicationName, String tagSignatureJsonFilePath) {

		Map<String, WebAnalyticsPageData> pageDataMap = getWebAnlyticsPageDataFromHarLogs(harDirPath, applicationName, tagSignatureJsonFilePath);
		Iterator it = pageDataMap.keySet().iterator();
		String firstkey;
		List<DetailedView> detailedViewList = new ArrayList<DetailedView>();

		while (it.hasNext()) {
			firstkey = it.next().toString();
			WebAnalyticsPageData detailedViewPageData = pageDataMap
					.get(firstkey);

			DetailedView detailedView = new DetailedView();
			detailedView.setScheduleId(scheduleId);
			detailedView.setApplicationName(detailedViewPageData
					.getApplicationName());
			detailedView.setPageURL(detailedViewPageData.getPageUrl());
			detailedView.setPageTitle(detailedViewPageData.getPageTitle());
			detailedView.setHarLogFileName(detailedViewPageData
					.getHarLogFileName());

			Map<String, WebAnalyticsTagData> detailedViewAppsSummary = detailedViewPageData
					.getWebAnalyticsTagDataMap();
			DataPopulationIntoDBforDetailedView dataPopulationIntoDBforDetailedView = new DataPopulationIntoDBforDetailedView();
			String finalXMLdata = dataPopulationIntoDBforDetailedView
					.DatapopulateforDetailedView(detailedViewAppsSummary);
			//logger.info("finalXMLdata :" + finalXMLdata);
			detailedView.setWebAnalyticsPageDataXml(finalXMLdata);

			detailedViewList.add(detailedView);
		}

		return detailedViewList;
	}

	public static void main(String[] args){
		WebAnalyticsUtils wau= new WebAnalyticsUtils();
		
		//generateAnalyticsReports();
		//new WebAnalyticsUtils().getBrokenUrlDataFromHarLogs("C:\\Dhinakar\\temp1\\UD\\UD", "UD_PROD_FIX", true, new ArrayList<Link>(), true);
		boolean generatePageloadAttributes = true;
		AnalyticsDataReportingUtils excelWriter = new AnalyticsDataReportingUtils("C:\\Dhinakar\\temp1\\UD_new\\UD\\PublicSite_BrokenUrlReport_.xlsx","BrokenUrlData");
		boolean beautify = true;
		new WebAnalyticsUtils().generateBrokenUrlReport(wau, "C:\\Tools\\mint\\Application\\ResultsDirectory\\PublicSite_PublicSite_ST4_Static_2015-Nov-23_09-15-16\\firefox\\HarReportsDirectory\\PublicSite", 
				"UD_PROD_FIX", "C:\\Dhinakar\\temp1\\check\\UD_new\\UDBrokenUrlReport1.xlsx", excelWriter, "UD", true, "C:\\Dhinakar\\temp1", 
				new ArrayList<Link>(), false, generatePageloadAttributes, beautify);
		

		CrawlConfig crawlconfig = null;
		
		try{
			JsonReaderWriter<CrawlConfig> jsonReaderWriter = new JsonReaderWriter<CrawlConfig>();
			crawlconfig = jsonReaderWriter.readJsonObjectFromFile(new CrawlConfig(), "C:\\Tools\\mint\\Application\\ResultsDirectory\\PublicSite_PublicSite_ST4_Static_2015-Nov-23_09-15-16\\serialize\\" + UiTestingConstants.CRAWL_CONFIG);
		}catch(Exception e){
			System.out.println(UiTestingConstants.CRAWL_CONFIG + ", is not available yet for the ScheduleExecutionId.");
		}
		
		String brokenUrlReportFilePath = "C:\\Tools\\mint\\Application\\ResultsDirectory\\PublicSite_PublicSite_ST4_Static_2015-Nov-23_09-15-16\\firefox\\HarReportsDirectory\\PublicSite_BrokenUrlReport_"+DateTimeUtil.getCurrentDateTimeyyyy_MM_dd_HH_mm_ss()+".xlsx";
		boolean generateReportsOnlyForRefererUrl = true;
		
		
		wau.generateBrokenUrlReport(wau, "C:\\Tools\\mint\\Application\\ResultsDirectory\\PublicSite_PublicSite_ST4_Static_2015-Nov-23_09-15-16\\firefox\\HarReportsDirectory\\PublicSite", "PublicSite", brokenUrlReportFilePath, excelWriter, "PublicSite", true, "C:\\Dhinakar\\temp1", 
				crawlconfig.getCrawlStatus().getNavigationList(), generateReportsOnlyForRefererUrl, generatePageloadAttributes, beautify);

	}

	private static void generateAnalyticsReports() {
		WebAnalyticsUtils wau = new WebAnalyticsUtils();
		
		Map<String, WebAnalyticsPageData> baseLinePageDataMap = null;
		
		String currentRunHarPath = "C:\\Dhinakar\\temp\\Analytics\\PublicSiteBaseline\\HarReportsDirectory\\PublicSite";
		String baselineHarPath = "C:\\\\Dhinakar\\\\temp\\\\Analytics\\\\PublicsiteCurrentNov19\\\\HarReportsDirectory\\\\PublicSite";
		String applicationName = "PublicSite";
		String analyticsExcelFilePath = "C:\\Dhinakar\\temp\\Analytics\\" + applicationName + DateTimeUtil.getCurrentDateTimeyyyy_MM_dd_HH_mm_ss()+".xlsx";
		
		System.out.println("*** Started Reading Current Run HAR reports for Analytics Audit. ****");
		Map<String, WebAnalyticsPageData> testPageDataMap = wau
				.getWebAnlyticsPageDataFromHarLogs(currentRunHarPath, applicationName, "C:\\Projects\\PortalCommonsV8A\\mintToolsV3\\mintCommonsJAR\\resources\\tag-signatures.json");
		System.out.println("*** Completed Reading Current Run HAR reports for Analytics Audit. ****");

		System.out.println("*** Started Reading Baseline HAR reports for Analytics Audit. ****");
		baseLinePageDataMap = wau
				.getWebAnlyticsPageDataFromHarLogs(baselineHarPath, applicationName, "C:\\Projects\\PortalCommonsV8A\\mintToolsV3\\mintCommonsJAR\\resources\\tag-signatures.json");
		System.out.println("*** Completed Reading Baseline HAR reports for Analytics Audit. ****");

		
		
		List<SamePagesDataStore> samePagesDataStoreList = wau
				.normalizePageDataMaps(baseLinePageDataMap, testPageDataMap);

		AnalyticsDataReportingUtils excelWriter = new AnalyticsDataReportingUtils(
				analyticsExcelFilePath, "Sheet");
		boolean collectOnlyFailureUrls = false;
		excelWriter.writeAuditAndTestDetailsToExcel(samePagesDataStoreList,
				analyticsExcelFilePath, "C:\\Dhinakar\\temp1",true, "2015-11-13_18:01:00", "2015-11-19_11:38:00", collectOnlyFailureUrls);
		System.out.println("Generation completed. file path :"+analyticsExcelFilePath);
		
	}

	public List<SamePagesBrokenUrlDataStore> normalizeBrokenUrlDataMap(
			Map<String, BrokenUrlData> currentRunbrokenUrlDataMap,
			Map<String, BrokenUrlData> baselineBrokenUrlDataMap) {
		
			List<SamePagesBrokenUrlDataStore> allSideBySidePagesList = new ArrayList<SamePagesBrokenUrlDataStore>();
			String allPageDataKey;
			SamePagesBrokenUrlDataStore samePagesBrokenUrlStore;// new SamePagesDataStore();
			
			for ( Map.Entry<String, BrokenUrlData> baselineBrokenUrlDataEntry: baselineBrokenUrlDataMap.entrySet()){
				if (currentRunbrokenUrlDataMap.get(baselineBrokenUrlDataEntry.getKey()) == null) { // if key not found,
					// insert a dummy
					// page object in
					// the map
					System.out.println("Current run not found for :"+baselineBrokenUrlDataEntry.getKey());
					currentRunbrokenUrlDataMap.put(baselineBrokenUrlDataEntry.getKey(), createDummyBrokenUrlData());
				}
			}
			
			for ( Map.Entry<String, BrokenUrlData> currentRunbrokenUrlDataEntry: currentRunbrokenUrlDataMap.entrySet()){
				if (baselineBrokenUrlDataMap.get(currentRunbrokenUrlDataEntry.getKey()) == null) { // if key not found,
					// insert a dummy
					// page object in
					// the map
					System.out.println("Baseline run not found for :"+currentRunbrokenUrlDataEntry.getKey());
					baselineBrokenUrlDataMap.put(currentRunbrokenUrlDataEntry.getKey(), createDummyBrokenUrlData());
				}
			}

			Iterator<String> allPageDataIterator = baselineBrokenUrlDataMap.keySet()
					.iterator();

			while (allPageDataIterator.hasNext()) {
				samePagesBrokenUrlStore = new SamePagesBrokenUrlDataStore();
				allPageDataKey = allPageDataIterator.next();
				// System.out.println("allPageDataKey :"+ allPageDataKey);
				// System.out.println("tags size :"+baseLinePageDataMap.get(allPageDataKey).getWebAnalyticsTagDataMap().size());
				samePagesBrokenUrlStore
						.setBaselineBrokenUrlData(baselineBrokenUrlDataMap
								.get(allPageDataKey));
				// System.out.println("tags size 3:"+samePagesDataStore.getWebAnalyticsPageDataBaseline().getWebAnalyticsTagDataMap().size());
				samePagesBrokenUrlStore.setCurentRunBrokenUrlData(currentRunbrokenUrlDataMap
						.get(allPageDataKey));
				// System.out.println("tags size 4:"+samePagesDataStore.getWebAnalyticsPageDataTestData().getWebAnalyticsTagDataMap().size());
				allSideBySidePagesList.add(samePagesBrokenUrlStore);
			}

			// normalize tagData
			// each page have same number of tags. if not equal insert null in the
			// position
			// each tag must have the same number of params, insert key, with null
			// value

			// normalize tag data
			return allSideBySidePagesList;
		
	}

	private BrokenUrlData createDummyBrokenUrlData() {
		BrokenUrlData noMatchingBrokenUrlDataFound = new BrokenUrlData();
		noMatchingBrokenUrlDataFound
				.setAppName(WebAnalyticsConstants.noDataFound);
		noMatchingBrokenUrlDataFound
				.setAjaxCallMade(WebAnalyticsConstants.noDataFound);
		noMatchingBrokenUrlDataFound.setErrorPage(true);
		noMatchingBrokenUrlDataFound.setErrorType(WebAnalyticsConstants.noDataFound);
		noMatchingBrokenUrlDataFound.setHarFileName(WebAnalyticsConstants.noDataFound);
		noMatchingBrokenUrlDataFound.setLinkName(WebAnalyticsConstants.noDataFound);
		noMatchingBrokenUrlDataFound.setNavigationPath(WebAnalyticsConstants.noDataFound);
		noMatchingBrokenUrlDataFound.setPageLoadAttributes(getPageLoadAttributesNotFound());
		noMatchingBrokenUrlDataFound.setPageTitle(WebAnalyticsConstants.noDataFound);
		noMatchingBrokenUrlDataFound.setParentUrl(WebAnalyticsConstants.noDataFound);
		noMatchingBrokenUrlDataFound.setRefererUrl(WebAnalyticsConstants.noDataFound);
		noMatchingBrokenUrlDataFound.setRequestMethod(WebAnalyticsConstants.noDataFound);
		noMatchingBrokenUrlDataFound.setResourceUrl(WebAnalyticsConstants.noDataFound);
		noMatchingBrokenUrlDataFound.setStatusCode(0);
		noMatchingBrokenUrlDataFound.setStatusText(WebAnalyticsConstants.noDataFound);
		
		return noMatchingBrokenUrlDataFound;
	}

	private Map<String, Long> getPageLoadAttributesNotFound() {
		Map<String, Long> pageLoadAttributesMap = new LinkedHashMap<String, Long>();
		Long temp = (long) 0;
		pageLoadAttributesMap.put("onLoad", temp);
		pageLoadAttributesMap.put("contentLoad", temp);

		pageLoadAttributesMap.put("blocked", temp);
		pageLoadAttributesMap.put("dns", temp);
		pageLoadAttributesMap.put("connect", temp);
		pageLoadAttributesMap.put("send", temp);
		pageLoadAttributesMap.put("wait", temp);
		pageLoadAttributesMap.put("receive", temp);
		pageLoadAttributesMap.put("totalLoadTime", temp);
		return pageLoadAttributesMap;
	}
}
