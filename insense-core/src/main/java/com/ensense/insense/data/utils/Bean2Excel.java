package com.ensense.insense.data.utils;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFCellUtil;
import org.apache.poi.ss.util.CellUtil;

import java.io.OutputStream;
import java.util.Date;
import java.util.List;

public class Bean2Excel {
    private HSSFWorkbook workbook;
    private HSSFFont boldFont;
    private HSSFDataFormat format;
     
    public Bean2Excel() {
        workbook = new HSSFWorkbook();
        boldFont = workbook.createFont();
        boldFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        format = workbook.createDataFormat();
    }
 
    public void addSheet(List<?> data, ReportColumn[] columns, String sheetName) {
 
        HSSFSheet sheet = workbook.createSheet(sheetName);
        int numCols = columns.length;
        int currentRow = 0;
        HSSFRow row;
 
        try {
 
            // Create the report header at row 0
            row = sheet.createRow(currentRow);
            // Loop over all the column beans and populate the report headers
            for (int i = 0; i < numCols; i++) {
                // Get the header text from the bean and write it to the cell
                writeCell(row, i, columns[i].getHeader(), Constants.FormatType.TEXT,
                		columns[i].getHeaderColor(), this.boldFont);
            }
 
            currentRow++; // increment the spreadsheet row before we step into
                            // the data
 
            // Write report rows
            for (int i = 0; i < data.size(); i++) {
                // create a row in the spreadsheet
                row = sheet.createRow(currentRow++);
                // get the bean for the current row
                Object bean = data.get(i);
                // For each column object, create a column on the current row
                for (int y = 0; y < numCols; y++) {
                    Object value = PropertyUtils.getProperty(bean,
                            columns[y].getMethod());
                    writeCell(row, y, value, columns[y].getType(),
                            columns[y].getColor(), columns[y].getFont());
                }
            }
 
            // Autosize columns
            for (int i = 0; i < numCols; i++) {
                sheet.autoSizeColumn((short) i);
            }
 
        } catch (Exception e) {
            System.err.println("Caught Generate Error exception: "
                    + e.getMessage());
        }
 
    }
 
    public HSSFFont boldFont() {
        return boldFont;
    }
 
    public void write(OutputStream outputStream) throws Exception {
        workbook.write(outputStream);
    }
 
    private void writeCell(HSSFRow row, int col, Object value,
                           Constants.FormatType formatType, Short bgColor, HSSFFont font)
            throws Exception {
 
        HSSFCell cell = HSSFCellUtil.createCell(row, col, null);
 
        if (value == null) {
            return;
        }
 
        if (font != null) {
            HSSFCellStyle style = workbook.createCellStyle();
            style.setFont(font);
            cell.setCellStyle(style);
        }
 
        switch (formatType) {
        case TEXT:
            cell.setCellValue(value.toString());
            break;
        case INTEGER:
            cell.setCellValue(((Number) value).intValue());
            HSSFCellUtil.setCellStyleProperty(cell, workbook,
                    CellUtil.DATA_FORMAT,
                    HSSFDataFormat.getBuiltinFormat(("#,##0")));
            break;
        case FLOAT:
            cell.setCellValue(((Number) value).doubleValue());
            HSSFCellUtil.setCellStyleProperty(cell, workbook,
                    CellUtil.DATA_FORMAT,
                    HSSFDataFormat.getBuiltinFormat(("#,##0.00")));
            break;
        case DATE:
            cell.setCellValue((Date) value);
            HSSFCellUtil.setCellStyleProperty(cell, workbook,
                    CellUtil.DATA_FORMAT,
                    HSSFDataFormat.getBuiltinFormat(("m/d/yy")));
            break;
        case MONEY:
            cell.setCellValue(((Number) value).intValue());
            HSSFCellUtil.setCellStyleProperty(cell, workbook,
                    CellUtil.DATA_FORMAT,
                    format.getFormat("$#,##0.00;$#,##0.00"));
            break;
        case PERCENTAGE:
            cell.setCellValue(((Number) value).doubleValue());
            HSSFCellUtil.setCellStyleProperty(cell, workbook,
                    CellUtil.DATA_FORMAT,
                    HSSFDataFormat.getBuiltinFormat("0.00%"));
        }
 
        if (bgColor != null) {
            HSSFCellUtil.setCellStyleProperty(cell, workbook,
                    CellUtil.FILL_FOREGROUND_COLOR, bgColor);
            HSSFCellUtil.setCellStyleProperty(cell, workbook,
                    CellUtil.FILL_PATTERN, HSSFCellStyle.SOLID_FOREGROUND);
        }
 
    }
}
