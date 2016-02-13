package com.ensense.insense.data.utils;

import com.ensense.insense.data.common.utils.Constants;
import org.apache.poi.hssf.usermodel.HSSFFont;

public class ReportColumn {
    private String m_method;
    private String m_header;
    private Constants.FormatType m_type;
    private HSSFFont m_font;
    private Short m_color;
    private Short header_color;
 
    public ReportColumn(String method, String header, Constants.FormatType type,
            HSSFFont font, Short color, Short hColor) {
        this.m_method = method;
        this.m_header = header;
        this.m_type = type;
        this.m_font = font;
        this.m_color = color;
        this.setHeaderColor(hColor);
    }
    public ReportColumn(String method, String header, Constants.FormatType type,
            HSSFFont font) {
        this(method, header, type, font, null, null);
    }
    public ReportColumn(String method, String header, Constants.FormatType type,
            Short color, Short hColor) {
        this(method, header, type, null, color, hColor);
    }
    public ReportColumn(String method, String header, Constants.FormatType type,
            Short color) {
        this(method, header, type, null, color, null);
    }
 
 
    public ReportColumn(String method, String header, Constants.FormatType type) {
        this(method, header, type, null, null, null);
    }
 
    public String getMethod() {
        return m_method;
    }
 
    public void setMethod(String method) {
        this.m_method = method;
    }
 
    public String getHeader() {
        return m_header;
    }
 
    public void setHeader(String header) {
        this.m_header = header;
    }
 
    public Constants.FormatType getType() {
        return m_type;
    }
 
    public void setType(Constants.FormatType type) {
        this.m_type = type;
    }
 
    public HSSFFont getFont() {
        return m_font;
    }
 
    public void setFont(HSSFFont m_font) {
        this.m_font = m_font;
    }
 
    public Short getColor() {
        return m_color;
    }
 
    public void setColor(Short m_color) {
        this.m_color = m_color;
    }
	public Short getHeaderColor() {
		return header_color;
	}
	public void setHeaderColor(Short header_color) {
		this.header_color = header_color;
	}
}
