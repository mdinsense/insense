package com.ensense.insense.core.analytics.utils;

import com.ensense.insense.core.reports.model.TextAndImageDetail;
import com.ensense.insense.core.reports.model.TextToFind;
import com.ensense.insense.core.analytics.model.AnalyticsData;
import com.ensense.insense.core.analytics.model.HarReportMap;
import com.ensense.insense.data.common.model.Link;
import com.ensense.insense.data.common.model.PartialText;
import com.ensense.insense.data.common.model.UsageReportResult;
import com.ensense.insense.data.common.utils.Constants;
import com.ensense.insense.data.model.uiadmin.form.schedule.CompareLink;
import com.ensense.insense.data.utils.DateTimeUtil;
import com.ensense.insense.services.scheduler.ReportsScheduler;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

public class ExcelWriter {
	private static Logger logger = Logger.getLogger(ReportsScheduler.class);
	
	private String excelFileName;
	private String sheetName;

	public ExcelWriter(String excelFileName, String sheetName) {
		this.excelFileName = excelFileName;
		this.sheetName = sheetName;
	}

	public boolean writeToExcel( SortedMap<String, HarReportMap> hashMap ){
		
		try {
			// Blank workbook 
			XSSFWorkbook workbook = new XSSFWorkbook();
			//Create a blank sheet 
			XSSFSheet sheet = workbook.createSheet(sheetName);
			int rownum = 1;
			
			writeHeader(sheet);
			
			for (Map.Entry<String, HarReportMap> entry : hashMap.entrySet()) {
				
				HarReportMap map = entry.getValue();

				for ( AnalyticsData analyticsData: map.getHarReports() ) {
					Row row = sheet.createRow(rownum++);
					
					int cellnum = 0;
					Cell cell = row.createCell(cellnum++);
					cell.setCellValue((String) analyticsData.getApplicationName());
					cell = row.createCell(cellnum++);
					cell.setCellValue((String) analyticsData.getPageTitle());
					cell = row.createCell(cellnum++);
					cell.setCellValue((String) analyticsData.getUrl());
					cell = row.createCell(cellnum++);
					cell.setCellValue((String) analyticsData.getRefererUrl());
					cell = row.createCell(cellnum++);
					cell.setCellValue((String) analyticsData.getTagType());
					
					cell = row.createCell(cellnum++);
					cell.setCellValue((String) analyticsData.getPageName());
					
					if ( checkTestCasePassed(analyticsData, map.getBaselineHarReports()) ){
						cell = row.createCell(cellnum++);
						cell.setCellValue((String) analyticsData.getTagName());
		
						cell = row.createCell(cellnum++);
						cell.setCellValue((String) analyticsData.getTagValue());
						
						cell = row.createCell(cellnum++);
						cell.setCellValue((String) analyticsData.getTagName());
						
						cell = row.createCell(cellnum++);
						cell.setCellValue((String) analyticsData.getTagValue());
						
						cell = row.createCell(cellnum++);
						cell.setCellValue((String) analyticsData.getTestStatus());
					}else{
						AnalyticsData nextBaselineData = getAvailableBaselineData(map.getBaselineHarReports());

						if ( nextBaselineData == null ){
							cell = row.createCell(cellnum++);
							cell.setCellValue("");
							
							cell = row.createCell(cellnum++);
							cell.setCellValue("");

							cell = row.createCell(cellnum++);
							cell.setCellValue((String) analyticsData.getTagName());

							cell = row.createCell(cellnum++);
							cell.setCellValue((String) analyticsData.getTagValue());
						} else {
							cell = row.createCell(cellnum++);
							cell.setCellValue((String) nextBaselineData.getTagName());
							
							cell = row.createCell(cellnum++);
							cell.setCellValue((String) nextBaselineData.getTagValue());
							
							cell = row.createCell(cellnum++);
							cell.setCellValue((String) analyticsData.getTagName());

							cell = row.createCell(cellnum++);
							cell.setCellValue((String) analyticsData.getTagValue());
						}
						
						cell = row.createCell(cellnum++);
						cell.setCellValue((String) analyticsData.getTestStatus());
					}
					
				}
				
				for ( AnalyticsData analyticsData: map.getBaselineHarReports() ) {
					Row row = sheet.createRow(rownum++);
					
					int cellnum = 0;
					Cell cell = row.createCell(cellnum++);
					cell.setCellValue((String) analyticsData.getApplicationName());
					cell = row.createCell(cellnum++);
					cell.setCellValue((String) analyticsData.getPageTitle());
					cell = row.createCell(cellnum++);
					cell.setCellValue((String) analyticsData.getUrl());
					cell = row.createCell(cellnum++);
					cell.setCellValue((String) analyticsData.getRefererUrl());
					cell = row.createCell(cellnum++);
					cell.setCellValue((String) analyticsData.getTagType());
					
					cell = row.createCell(cellnum++);
					cell.setCellValue((String) analyticsData.getPageName());
					
					cell = row.createCell(cellnum++);
					cell.setCellValue((String) analyticsData.getTagName());
					
					cell = row.createCell(cellnum++);
					cell.setCellValue((String) analyticsData.getTagValue());
					
					cell = row.createCell(cellnum++);
					cell.setCellValue("");
					
					cell = row.createCell(cellnum++);
					cell.setCellValue("");
					
					cell = row.createCell(cellnum++);
					cell.setCellValue("Failed");
				}
			}
			FileOutputStream out = new FileOutputStream(new File(excelFileName));            
			workbook.write(out);            
			out.close();            
			System.out.println(excelFileName + "written successfully on disk.");        
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private void writeHeader(XSSFSheet sheet) {
		Row row = sheet.createRow(0);
		
		int cellnum = 0;
		Cell cell = row.createCell(cellnum++);
		cell.setCellValue("Appication Name");
		
		cell = row.createCell(cellnum++);
		cell.setCellValue("Page title");
		
		cell = row.createCell(cellnum++);
		cell.setCellValue("Url");
		
		cell = row.createCell(cellnum++);
		cell.setCellValue("Referrer URL");
		
		cell = row.createCell(cellnum++);
		cell.setCellValue("Tag type");
		
		cell = row.createCell(cellnum++);
		cell.setCellValue("Page Name");
		
		cell = row.createCell(cellnum++);
		cell.setCellValue("Baseline Tag Name");
		
		cell = row.createCell(cellnum++);
		cell.setCellValue("Baseline Tag Value");
		
		cell = row.createCell(cellnum++);
		cell.setCellValue("Current Tag Name");
		
		cell = row.createCell(cellnum++);
		cell.setCellValue("Current Tag Value");
		
		cell = row.createCell(cellnum++);
		cell.setCellValue("Test status");
		
	}

	private AnalyticsData getAvailableBaselineData(List<AnalyticsData> baselineHarReports) {
		int index = 0;
		AnalyticsData newAnalyticsData = new AnalyticsData();
		
		for ( AnalyticsData existingAnalyticsData: baselineHarReports){
			newAnalyticsData.setApplicationName(existingAnalyticsData.getApplicationName());
			newAnalyticsData.setBaseline(existingAnalyticsData.isBaseline());
			newAnalyticsData.setPageTitle(existingAnalyticsData.getPageTitle());
			newAnalyticsData.setPageName(existingAnalyticsData.getPageName());
			newAnalyticsData.setTagName(existingAnalyticsData.getTagName());
			newAnalyticsData.setTagType(existingAnalyticsData.getTagType());
			newAnalyticsData.setTagValue(existingAnalyticsData.getTagValue());
			newAnalyticsData.setTestStatus(existingAnalyticsData.getTestStatus());
			newAnalyticsData.setUrl(existingAnalyticsData.getUrl());
			newAnalyticsData.setRefererUrl(existingAnalyticsData.getRefererUrl());
			baselineHarReports.remove(index);
			return newAnalyticsData;
		}
		return null;
	}

	private boolean checkTestCasePassed(AnalyticsData analyticsData, List<AnalyticsData> baselineHarReports) {
		int index = 0;
		for ( AnalyticsData existingAnalyticsData: baselineHarReports){
			if ( analyticsData.getTagName().equals(existingAnalyticsData.getTagName()) && analyticsData.getTagValue().equals(existingAnalyticsData.getTagValue() )){
				baselineHarReports.remove(index);
				return true;
			}
			index++;
		}
		return false;
	}
	
	public static boolean writeTextComparisonDetailsToExcel(List<CompareLink> textCompareReportList, String excelReportFilePath, String tempDirectory){
		boolean status = false;
		SXSSFWorkbook wb = new SXSSFWorkbook();
		Row row;
		Cell cell0, cell1, cell2, cell3, cell4, cell5, cell6, cell7, cell8, cell9;

		// wb.setCompressTempFiles(true); // temp files will be gzipped
		System.setProperty("java.io.tmpdir", tempDirectory);
		Sheet sheet1 = wb.createSheet("Text Comparison Report");
		Sheet sheet = writeTextComparisonReportHeader(wb, sheet1);
		int currentRowNum = 1;
		
		for ( CompareLink compareLink : textCompareReportList){
			try{
				row = sheet.createRow(currentRowNum++);
				
				cell0 = row.createCell(0);
				cell0.setCellValue(compareLink.getUrlNo());
				
				cell1 = row.createCell(1);//
				cell1.setCellValue(compareLink.getUrl());
				
				cell2 = row.createCell(2);
				if ( compareLink.isUrlFoundInBaseline() && compareLink.isUrlFoundInCurrentRun()){
					cell2.setCellValue("YES");
				}else if (compareLink.isUrlFoundInBaseline()){
					cell2.setCellValue("NOT Found");
				}else if (compareLink.isUrlFoundInCurrentRun()){
					cell2.setCellValue("NEW URL");
				}
				
				cell3 = row.createCell(3);//
				cell3.setCellValue(compareLink.getPercentageMatch());
				
				cell4 = row.createCell(4);//
				
				if ( null != compareLink.getCurrentRunPartialTextList() && compareLink.getCurrentRunPartialTextList().size() > 0 ){
					for ( PartialText partialText : compareLink.getCurrentRunPartialTextList()){
						cell4.setCellValue(partialText.getPercentageBaselineMatch());
					}
				}
				
				cell5 = row.createCell(5);
				cell5.setCellValue(compareLink.getParentUrl());
				
				cell6 = row.createCell(6);
				cell6.setCellValue(compareLink.getLinkName());
				
				cell7 = row.createCell(7);
				cell7.setCellValue(compareLink.getNavigationPath());
				
				cell8 = row.createCell(8);
				cell8.setCellValue(compareLink.getPageTitle());
	
				cell9 = row.createCell(9);
				cell9.setCellValue(compareLink.getCurrentImageFilePath());

			}catch(Exception e){
				logger.error("Exception while writing Text Comparison report.");
				logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
			}
		}
		
		// Write the output to a file
		FileOutputStream fileOut = null;
		try {
			fileOut = new FileOutputStream(excelReportFilePath);
			wb.write(fileOut);
			status = true;
			
		} catch (Exception e) {
			logger.error("Exception while writing Text Comparison details to Excel.");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
		}finally{
			try {
				fileOut.close();
			} catch (IOException e) {
				logger.error("Exception while closing the FileOutputStream.");
				logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
			}
			wb.dispose();
		}
		
		return status;
	}
	
	private static Sheet writeTextComparisonReportHeader(SXSSFWorkbook wb, Sheet sheet) {
		CellStyle style = createHeaderCellStyle(wb);

		Row row = sheet.createRow(0);

		int cellnum = 0;

		Cell cell = row.createCell(cellnum++);
		cell.setCellValue("URL No");
		cell.setCellStyle(style);
		
		cell = row.createCell(cellnum++);
		cell.setCellValue("Url");
		cell.setCellStyle(style);
		
		cell = row.createCell(cellnum++);
		cell.setCellValue("Url Found");
		cell.setCellStyle(style);
		
		cell = row.createCell(cellnum++);
		cell.setCellValue("Percentage Matched");
		cell.setCellStyle(style);
		
		cell = row.createCell(cellnum++);
		cell.setCellValue("Mega menu Matched with Baseline");
		cell.setCellStyle(style);
		
		cell = row.createCell(cellnum++);
		cell.setCellValue("Parent Url");
		cell.setCellStyle(style);
		
		cell = row.createCell(cellnum++);
		cell.setCellValue("Link Name");
		cell.setCellStyle(style);

		
		cell = row.createCell(cellnum++);
		cell.setCellValue("Navigation Path");
		cell.setCellStyle(style);
		
		cell = row.createCell(cellnum++);
		cell.setCellValue("Page Title");
		cell.setCellStyle(style);

		cell = row.createCell(cellnum++);
		cell.setCellValue("Image File Path");
		cell.setCellStyle(style);
		
		/*cell = row.createCell(cellnum++);
		cell.setCellValue("Mega menu Matched with HomePage");
		cell.setCellStyle(style);*/
		
		return sheet;
	}
	
	private static CellStyle createHeaderCellStyle(SXSSFWorkbook wb) {

		CellStyle style = wb.createCellStyle();

		// style.setFillBackgroundColor(IndexedColors.ROYAL_BLUE.getIndex());
		// style.setFillForegroundColor(IndexedColors.ROYAL_BLUE.getIndex());
		// style.setFillPattern(CellStyle.SPARSE_DOTS);
		style.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setBorderBottom(CellStyle.BORDER_MEDIUM);
		style.setBorderLeft(CellStyle.BORDER_MEDIUM);
		style.setBorderRight(CellStyle.BORDER_MEDIUM);
		style.setBorderTop(CellStyle.BORDER_MEDIUM);

		return style;
	}
	
	public static boolean writeTextCountAndImageDetailsToExcel(Map<String, TextAndImageDetail> textAndImageDetailMap, String excelReportFilePath, String tempDirectory){
		SXSSFWorkbook wb = new SXSSFWorkbook();
		Row row;
		Cell cell;
		boolean status = false;
		// wb.setCompressTempFiles(true); // temp files will be gzipped
		System.setProperty("java.io.tmpdir", tempDirectory);
		Sheet sheet1 = wb.createSheet("Text Count & Image Details Report");
		
		TextAndImageDetail textAndImageDetailTemp = null;
		
		for (Map.Entry<String, TextAndImageDetail> entry: textAndImageDetailMap.entrySet()) {
		    String url = entry.getKey();
		    textAndImageDetailTemp = entry.getValue();
		    break;
		}
		
		Sheet sheet = writeTextCountAndImageDetailReportHeader(wb, sheet1, textAndImageDetailTemp);
		int currentRowNum = 1;
		
		for (Map.Entry<String, TextAndImageDetail> entry: textAndImageDetailMap.entrySet()) {
		    String url = entry.getKey();
		    TextAndImageDetail textAndImageDetail = entry.getValue();
		    
		    try{
		    	row = sheet.createRow(currentRowNum++);
				int cellCount = 0;
				
				cell = row.createCell(cellCount);
				cell.setCellValue(textAndImageDetail.getUrlNo());
				
				cellCount++;
				cell = row.createCell(cellCount);//
				cell.setCellValue(textAndImageDetail.getUrl());
				
				cellCount++;
				cell = row.createCell(cellCount);
				cell.setCellValue(textAndImageDetail.getPageTitle());
				
				for( TextToFind textToFind : textAndImageDetail.getTextToFindList()){
					cellCount++;
					cell = row.createCell(cellCount);
					cell.setCellValue(textToFind.getFindCount());
				}
				
				cellCount++;
				cell = row.createCell(cellCount);
				cell.setCellValue(textAndImageDetail.getTiaaImageCount());
				
				cellCount++;
				cell = row.createCell(cellCount);
				cell.setCellValue(textAndImageDetail.getTiaaImageUrls().toString());
				
				cellCount++;
				cell = row.createCell(cellCount);
				cell.setCellValue(textAndImageDetail.getParentUrl());
				
				cellCount++;
				cell = row.createCell(cellCount);
				cell.setCellValue(textAndImageDetail.getLinkName());
				
				cellCount++;
				cell = row.createCell(cellCount);
				cell.setCellValue(textAndImageDetail.getNavigationPath());
				
		    }catch(Exception e){
		    	logger.error("Exception while generating Excel report for Text count and Image detail");
		    	logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
		    }
		}
		
		// Write the output to a file
		FileOutputStream fileOut = null;
		try {
			fileOut = new FileOutputStream(excelReportFilePath);
			wb.write(fileOut);
			status = true;
			
		} catch (Exception e) {
			logger.error("Exception while writing Text Comparison details to Excel.");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
		}finally{
			try {
				fileOut.close();
			} catch (IOException e) {
				logger.error("Exception while closing the FileOutputStream.");
				logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
			}
			wb.dispose();
		}
		
		return status;
	}

	private static Sheet writeTextCountAndImageDetailReportHeader(
			SXSSFWorkbook wb, Sheet sheet, TextAndImageDetail textAndImageDetail) {
		CellStyle style = createHeaderCellStyle(wb);

		Row row = sheet.createRow(0);

		int cellnum = 0;

		Cell cell = row.createCell(cellnum++);
		cell.setCellValue("URL No");
		cell.setCellStyle(style);
		
		cell = row.createCell(cellnum++);
		cell.setCellValue("Url");
		cell.setCellStyle(style);
		
		cell = row.createCell(cellnum++);
		cell.setCellValue("Page Tile");
		cell.setCellStyle(style);
		
		for( TextToFind textToFind : textAndImageDetail.getTextToFindList()){
			cell = row.createCell(cellnum++);
			cell.setCellValue(textToFind.getText() + " Count");
			cell.setCellStyle(style);
		}

		cell = row.createCell(cellnum++);
		cell.setCellValue("TIAA Image Count");
		cell.setCellStyle(style);
		
		cell = row.createCell(cellnum++);
		cell.setCellValue("TIAA Image Details");
		cell.setCellStyle(style);
		
		cell = row.createCell(cellnum++);
		cell.setCellValue("Parent Url");
		cell.setCellStyle(style);
		
		cell = row.createCell(cellnum++);
		cell.setCellValue("Link Name");
		cell.setCellStyle(style);

		
		cell = row.createCell(cellnum++);
		cell.setCellValue("Navigation Path");
		cell.setCellStyle(style);
		
		return sheet;
	}
	
	public static void downloadUsageReport(List<UsageReportResult> usageReport, HttpServletResponse response) {
	        try {
	            // Create the report object
	            Bean2Excel oReport = new Bean2Excel();
	            // Create an array of report column objects
	            ReportColumn[] reportColumns = new ReportColumn[] {
	                    new ReportColumn("slno", "SL NO", Constants.FormatType.TEXT, null, HSSFColor.LIGHT_BLUE.index),
	                    new ReportColumn("applicationName", "Application Name", Constants.FormatType.TEXT, null, HSSFColor.LIGHT_BLUE.index),
	                    new ReportColumn("environmentCategoryName", "Environment Name", Constants.FormatType.TEXT, null, HSSFColor.LIGHT_BLUE.index),
	                    new ReportColumn("groupName", "User Group", Constants.FormatType.TEXT, null, HSSFColor.LIGHT_BLUE.index),
	                    new ReportColumn("userName", "User Name", Constants.FormatType.TEXT, null, HSSFColor.LIGHT_BLUE.index),
	                    new ReportColumn("solutionTypeName", "Solution Type", Constants.FormatType.TEXT, null, HSSFColor.LIGHT_BLUE.index),
	                    new ReportColumn("functionalityName", "Functionality", Constants.FormatType.TEXT, null, HSSFColor.LIGHT_BLUE.index),
	                    new ReportColumn("startDate", "Start Date", Constants.FormatType.TEXT, null, HSSFColor.LIGHT_BLUE.index),
	                    new ReportColumn("endDate", "End Date", Constants.FormatType.TEXT, null, HSSFColor.LIGHT_BLUE.index),
	                    new ReportColumn("notes", "Notes", Constants.FormatType.TEXT, null, HSSFColor.LIGHT_BLUE.index)};
	 
	            // Create a worksheet with our usageReport data and report columns
	            oReport.addSheet(usageReport, reportColumns, "sheet1"); 
	            // Create an output stream to write the report to.
	            OutputStream output1 = response.getOutputStream();
	            response.setContentType("application/vnd.ms-excel");
				// set headers for the response
				String headerKey = "Content-Disposition";
				String headerValue = String.format("attachment; filename=\"%s\"","UasgeReport_"+ DateTimeUtil.getCurrentDateTimeyyyy_MM_dd_HH_mm_ss()+".xls");
				response.setHeader(headerKey, headerValue);
	            // Write the report to the output stream
	            oReport.write(output1);
	        } catch(Exception e) {
	        	e.printStackTrace();
	        }
	}
	
	private static Sheet writeTextOrImageReportHeader(SXSSFWorkbook wb, Sheet sheet, boolean writeBaselieData) {
        CellStyle style = createHeaderCellStyle(wb);
        Row row = sheet.createRow(0);
        int cellnum = 0;
        
        Cell cell = row.createCell(cellnum++);
        cell.setCellValue("URL No");
        cell.setCellStyle(style);

        cell = row.createCell(cellnum++);
        cell.setCellValue("URL");
        cell.setCellStyle(style);

        if ( writeBaselieData ){
	        cell = row.createCell(cellnum++);
	        cell.setCellValue("BaselineTotaImagesCount");
	        cell.setCellStyle(style);
        }
        
        cell = row.createCell(cellnum++);
        cell.setCellValue("TotaImagesCount");
        cell.setCellStyle(style);
        
        if ( writeBaselieData ){
	        cell = row.createCell(cellnum++);
	        cell.setCellValue("BaselineImageDetails");
	        cell.setCellStyle(style);
        }

        cell = row.createCell(cellnum++);
        cell.setCellValue("ImageDetails");
        cell.setCellStyle(style);
        
        if ( writeBaselieData ){
	        cell = row.createCell(cellnum++);
	        cell.setCellValue("BaselineTiaaImageDetails");
	        cell.setCellStyle(style);
        }
        
        cell = row.createCell(cellnum++);
        cell.setCellValue("TiaaImageDetails");
        cell.setCellStyle(style);
        
        if ( writeBaselieData ){
	        cell = row.createCell(cellnum++);
	        cell.setCellValue("BaselineTiaaImageCount");
	        cell.setCellStyle(style);
        }

        cell = row.createCell(cellnum++);
        cell.setCellValue("TiaaImageCount");
        cell.setCellStyle(style);
        
        if ( writeBaselieData ){
        	cell = row.createCell(cellnum++);
            cell.setCellValue("BaselineTiaaStringCount");
            cell.setCellStyle(style);
        }
        
        cell = row.createCell(cellnum++);
        cell.setCellValue("TiaaStringCount");
        cell.setCellStyle(style);
        
        if ( writeBaselieData ){
	        cell = row.createCell(cellnum++);
	        cell.setCellValue("BaselineTiaaCrefStringCount");
	        cell.setCellStyle(style);
        }
        
        cell = row.createCell(cellnum++);
        cell.setCellValue("TiaaCrefStringCount");
        cell.setCellStyle(style);

        return sheet;
 }
 
	public static void writeTextOrImageReportDataToExcel(
			List<Pair> matchingList, String excelReportFilePath,
			String tempDirectory, boolean writeBaselieData) {
		SXSSFWorkbook wb = new SXSSFWorkbook();
		Row row;
		Cell cell;
		
		System.setProperty("java.io.tmpdir", tempDirectory);
		Sheet sheet1 = wb.createSheet("TextOrImage Report Data");
		Sheet sheet = writeTextOrImageReportHeader(wb, sheet1, writeBaselieData);
		int currentRowNum = 1;

		for (Pair pair : matchingList) {
			int cellnum = 0;
			Link baselineLink = (Link) pair.getFirst();
			Link currentLink = (Link) pair.getSecond();
			//System.out.println(currentRowNum + "-->" + currentLink);
			row = sheet.createRow(currentRowNum++);
			cell = row.createCell(cellnum++);
			cell.setCellValue(currentLink.getUrlNo());

			// column 2 - Url
			cell = row.createCell(cellnum++);
			cell.setCellValue(currentLink.getUrl());

			if (writeBaselieData) {
				cell = row.createCell(cellnum++);//
				cell.setCellValue(baselineLink.getTextImageReportData()
						.getAllImageCount());
			}

			// column 3 -TotaImagesCount
			cell = row.createCell(cellnum++);//
			try{
				cell.setCellValue(currentLink.getTextImageReportData()
					.getAllImageCount());
			}catch(Exception e){
				
			}

			// column 4 -allImageList

			if (writeBaselieData && baselineLink.getTextImageReportData()
					.getAllImageList()!=null) {
				cell = row.createCell(cellnum++);//
				cell.setCellValue(baselineLink.getTextImageReportData()
						.getAllImageList().toString());
			}
			
			if(null != currentLink && null != currentLink.getTextImageReportData() && currentLink.getTextImageReportData()
						.getAllImageList()!=null) {
			// column 5 -allImageList
			cell = row.createCell(cellnum++);//
				try {
					cell.setCellValue(currentLink.getTextImageReportData()
							.getAllImageList().toString());
				} catch (Exception e) {
	
				}
			}

			if (writeBaselieData && baselineLink.getTextImageReportData()
					.getTiaaImageList()!=null) {
				cell = row.createCell(cellnum++);//
				cell.setCellValue(baselineLink.getTextImageReportData()
						.getTiaaImageList().toString());
			}
			if( null != currentLink && null != currentLink.getTextImageReportData() && currentLink.getTextImageReportData()
					.getTiaaImageList()!=null){
				cell = row.createCell(cellnum++);//
				try {
					cell.setCellValue(currentLink.getTextImageReportData()
							.getTiaaImageList().toString());
				} catch (Exception e) {
	
				}
			}
			if (writeBaselieData) {
				cell = row.createCell(cellnum++);//
				cell.setCellValue(baselineLink.getTextImageReportData()
						.getTiaaImageCount());
			}

			// column 7 - TiaaImageCount
			cell = row.createCell(cellnum++);//
			try{
				cell.setCellValue(currentLink.getTextImageReportData()
					.getTiaaImageCount());
			}catch(Exception e){
				
			}

			// column 9 - TiaaStringCount
			if (writeBaselieData) {
				cell = row.createCell(cellnum++);//
				cell.setCellValue(baselineLink.getTextImageReportData()
						.getTiaaStringCount());
			}

			cell = row.createCell(cellnum++);//
			try{
				cell.setCellValue(currentLink.getTextImageReportData()
					.getTiaaStringCount());
			}catch(Exception e){
				
			}

			if (writeBaselieData) {
				cell = row.createCell(cellnum++);//
				cell.setCellValue(baselineLink.getTextImageReportData()
						.getTiaaCrefStringCount());
			}

			// column 11 - TiaaCrefStringCount
			cell = row.createCell(cellnum++);//
			try{
				cell.setCellValue(currentLink.getTextImageReportData()
					.getTiaaCrefStringCount());
			}catch(Exception e){
				
			}
		}

		// Write the output to a file
		FileOutputStream fileOut;
		try {
			fileOut = new FileOutputStream(excelReportFilePath);
			wb.write(fileOut);
			fileOut.close();
			wb.dispose();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * for(Link value : navigationDetails){ if(value != null &&
		 * value.getTextImageReportData() != null) {
		 * System.out.println(currentRowNum + "-->" + value); row =
		 * sheet.createRow(currentRowNum++); cell0 = row.createCell(0);
		 * cell0.setCellValue(value.getUrlNo());
		 * 
		 * // column 2 - Url cell1 = row.createCell(1);
		 * cell1.setCellValue(value.getUrl());
		 * 
		 * // column 3 -TotaImagesCount cell2 = row.createCell(2);//
		 * cell2.setCellValue
		 * (value.getTextImageReportData().getAllImageCount());
		 * 
		 * // column 4 -allImageList cell3 = row.createCell(3);//
		 * cell3.setCellValue
		 * (value.getTextImageReportData().getAllImageList().toString());
		 * 
		 * // column 5 - TiaaImageCount cell4 = row.createCell(4);//
		 * cell4.setCellValue
		 * (value.getTextImageReportData().getTiaaImageCount());
		 * 
		 * // column 6 - TiaaStringCount cell5 = row.createCell(5);//
		 * cell5.setCellValue
		 * (value.getTextImageReportData().getTiaaStringCount());
		 * 
		 * // column 7 - TiaaCrefStringCount cell6 = row.createCell(6);//
		 * cell6.setCellValue
		 * (value.getTextImageReportData().getTiaaCrefStringCount()); }
		 */
	}

}
