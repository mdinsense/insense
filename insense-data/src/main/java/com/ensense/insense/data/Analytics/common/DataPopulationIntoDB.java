package com.ensense.insense.data.analytics.common;

import com.ensense.insense.core.analytics.summarydashboard.SummaryDashBoardData;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;


public class DataPopulationIntoDB {

	private static final Logger logger = Logger
			.getLogger(DataPopulationIntoDB.class);

	public String populateSummaryViewXML(
			Map<String, AnalyticsSummaryReportData> webAnalyticsAppsSummary) {
		String summaryViewXML = null;
		Iterator it = webAnalyticsAppsSummary.keySet().iterator();
		SummaryDashBoardData xmlGeneration = new SummaryDashBoardData();
		String anAppName = "";
		while (it.hasNext()) {
			anAppName = it.next().toString();
			AnalyticsSummaryReportData analyticsSummaryReportData = webAnalyticsAppsSummary
					.get(anAppName);
			// ArrayList<TagVarNameValue> analyticTagList = new
			// ArrayList<TagVarNameValue>();
			SummaryDashBoardData.SummaryDashBoardEntries entrylist = new SummaryDashBoardData.SummaryDashBoardEntries();
			entrylist.setAppName(anAppName);

			for (Field field : analyticsSummaryReportData.getClass()
					.getDeclaredFields()) {
				if (field.isAnnotationPresent(Tags.class)) {
					SummaryDashBoardData.SummaryDashBoardEntries.TagVarNameValue taglist = new SummaryDashBoardData.SummaryDashBoardEntries.TagVarNameValue();
					taglist.setName(field.getAnnotation(Tags.class).value());
					try {
						if (field.get(analyticsSummaryReportData) instanceof Integer) {
							taglist.setValue(new Integer((Integer) field
									.get(analyticsSummaryReportData))
									.toString());
						} else{
							taglist.setValue((String) field
									.get(analyticsSummaryReportData));
						}
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						logger.error("IllegalArgumentException is thrown when iterating the tagvalues:"
								+ e);
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						logger.error("IllegalAccessException is thrown when iterating the tagvalues:"
								+ e);
						e.printStackTrace();
					}
					entrylist.getTagVarNameValue().add(taglist);
				}
			}
			xmlGeneration.getSummaryDashBoardEntries().add(entrylist);

		}

		try {
			summaryViewXML = AnalyticsMarshallUtility
					.doMarshalling(xmlGeneration);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Error is thrown  when performing marshalling:" + e);
			e.printStackTrace();
		}

		//logger.info("Summary View XML in DB " + summaryViewXML);
		// insertXMLToDB(summaryViewXML);

		return summaryViewXML;
	}
}
/*
 * public static String insertXMLToDB(String finaldata) throws
 * ClassNotFoundException, SQLException { String sql = "INSERT INTO user " +
 * "(first_name,last_name, gender, city) VALUES (?, ?, ?,?)"; DataSource ds=new
 * DataSource(); JdbcTemplate jdbcTemplate = new JdbcTemplate();
 * 
 * jdbcTemplate.update( sql, new Object[] { user.getFirstName(),
 * user.getLastName(), user.getGender(), user.getCity() });
 * 
 * }
 */

