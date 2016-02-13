package com.ensense.insense.core.analytics.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReaderUtil {

	public ArrayList<String> getExcelColumnAsList(InputStream inputStream, String excelFileName,
			int columnPosition) throws Exception{
		ArrayList<String> excelColumnList = new ArrayList<String>();
		if ( columnPosition > 0 ){
			columnPosition = columnPosition - 1;
		}
		try {
			// Create Workbook instance for xlsx/xls file input stream
			Workbook workbook = null;
			if (excelFileName.toLowerCase().endsWith("xlsx")) {
				workbook = new XSSFWorkbook(inputStream);
			} else if (excelFileName.toLowerCase().endsWith("xls")) {
				workbook = new HSSFWorkbook(inputStream);
			}else{
				throw new Exception();
			}
			Sheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rows = sheet.rowIterator();

			while (rows.hasNext()) {
				Row row = (Row) rows.next();

				if (row.getCell(columnPosition) != null
						&& (row.getCell(columnPosition).toString()
								.contains("http://")
								|| row.getCell(columnPosition).toString()
										.contains("https://") || row
								.getCell(columnPosition).toString()
								.contains("www."))) {
					excelColumnList.add(row.getCell(columnPosition).toString());
				}
			}
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} catch (Exception e){
			throw e;
		}
		
		return excelColumnList;
	}
}
