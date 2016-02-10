package com.ensense.insense.core.analytics.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import com.cts.mint.analytics.entity.DetailedView;
import com.cts.mint.analytics.model.WebAnalyticsTagData;
import com.cts.mint.generated.jaxb.analytics.detailedview.DetailedViewWebAnalyticsTagData;
import com.cts.mint.generated.jaxb.analytics.detailedview.DetailedViewWebAnalyticsTagData.DetailedViewTags;
import com.cts.mint.generated.jaxb.analytics.detailedview.DetailedViewWebAnalyticsTagData.DetailedViewTags.TagVariablesData;
import com.cts.mint.util.PropertyReader;

public class DataPopulationIntoDBforDetailedView {

	private static final Logger logger = Logger
			.getLogger(DataPopulationIntoDBforDetailedView.class);

	/*******For  Populating Values into DB with the values of the detailed view
	 * added by 303780
	 * @param model
	 * @return*/
	
	public String DatapopulateforDetailedView(Map<String, WebAnalyticsTagData> webAnalyticsAppsSummary)
	{

		String DetailedViewXML = null;
		Iterator it = webAnalyticsAppsSummary.keySet().iterator();
		//com.example.tests.Entry xmlGeneration = new com.example.tests.Entry();

		DetailedViewWebAnalyticsTagData xmlGeneration = new DetailedViewWebAnalyticsTagData();
		String firstKey;
		while (it.hasNext()) {
			firstKey = it.next().toString();
			WebAnalyticsTagData webAnalyticsTagData = webAnalyticsAppsSummary.get(firstKey);
			DetailedViewTags entrylist = new DetailedViewTags();
			entrylist.setTagtype(webAnalyticsTagData.getTagName());
			entrylist.setTagUrl(webAnalyticsTagData.getTagUrl() );
			entrylist.setTagDataKey(webAnalyticsTagData.getTagDataKey());
			entrylist.setStartedDateTime(webAnalyticsTagData.getStartedDateTime());
			
			Map<String, String> tagname = webAnalyticsTagData.getTagVariableData();
			Iterator it1 = tagname.keySet().iterator();
			while (it1.hasNext()) {
				
				String data=it1.next().toString();
				
				TagVariablesData tagVarNameValue = new TagVariablesData();
				tagVarNameValue.setName(data);
				tagVarNameValue.setValue(tagname.get(data));
				
				entrylist.getTagVariablesData().add(tagVarNameValue);
			}
			xmlGeneration.getDetailedViewTags().add(entrylist);
		}
		try {
			DetailedViewXML = AnalyticsMarshallUtility.doMarshalling(xmlGeneration);
		} catch (Exception e) {
			logger.error("Error is thrown  when performing marshalling:" + e);
			e.printStackTrace();
		}
		return DetailedViewXML;
	}
	
	
	/*******For  Populating Values into DB with the values of the detailed view
	 * added by 303780
	 * @param model
	 * @return*/
	public void  insertXMLToDB(DetailedView detailedView) throws ClassNotFoundException, SQLException
    {
          
                Connection connection = null;
          
          try {
                PropertyReader proReader = new PropertyReader();
                Class.forName("com.mysql.jdbc.Driver");
                String url = proReader.getProperty("urlString");
                String dbusername = proReader.getProperty("dbusername");
                String dbpwd = proReader.getProperty("dbpwd");
          
          
                connection= DriverManager.getConnection(url,dbusername,dbpwd);      
    
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO detail_view_table" 
            		    + "(test_schedule_id,app_name ,page_URL,page_title,harlog_file_name,webanalyics_pagedata_xml) VALUES (?,?,?,?,?,?)"  );           
                preparedStatement.setInt (1,detailedView.getScheduleId());
                preparedStatement.setString(2,detailedView.getApplicationName());
                preparedStatement.setString(3,detailedView.getPageURL());
                preparedStatement.setString(4,detailedView.getPageTitle());
                preparedStatement.setString(5,detailedView.getHarLogFileName());
                preparedStatement.setString(6,detailedView.getWebAnalyticsPageDataXml());
    
                preparedStatement.execute();
          //return null;
          }catch(Exception e){
                //Handle errors for Class.forName
                e.printStackTrace();
             }
          finally {
          connection.close();
          
    }
}
}
