package com.ensense.insense.core.utils.htmlParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities.EscapeMode;
import org.jsoup.nodes.FormElement;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.cts.mint.common.MintFileCaptureUtil;
import com.cts.mint.common.MintFileUtils;
import com.cts.mint.common.utils.CommonUtils;
import com.cts.mint.crawler.model.FormElements;
import com.cts.mint.crawler.model.UrlForm;
public class HtmlParserUtil {
	private static final Logger logger = Logger
			.getLogger(HtmlParserUtil.class);
	public static String getUnwantedTagRemovedHtmlSource(
			String htmlTagOrAttributeToRemove, String pageSource)
			throws Exception {
		boolean found = false;
		Document doc = Jsoup.parse(pageSource);

		try {
			for (Element element : doc.select(htmlTagOrAttributeToRemove)) {
				element.remove();
	
				found = true;
			}
		}catch(Exception e){
		}

		if (!found) {
			try{
				for (Element element : doc.getAllElements()) {
					for (Attribute attribute : element.attributes()) {
						if (null != attribute.getValue() && attribute.getValue().equalsIgnoreCase(
								htmlTagOrAttributeToRemove)) {
							element.remove();
						}
					}
				}
			}catch(Exception e){
			}
		}
		doc.outputSettings().escapeMode(EscapeMode.xhtml);
		return doc.html();
	}

	public static String getHtmlContentWithoutHtmlTags(String pageSource) throws Exception{
		Document doc = Jsoup.parse(pageSource);
		doc.outputSettings().escapeMode(EscapeMode.xhtml);
		return doc.body().text();
	}

	public static String getHtmlContent(String pageSource, String tagValue) throws Exception{
		Document doc = Jsoup.parse(pageSource);
		
		Element element= null;
		for( Element elementTemp : doc.select(tagValue) ) {
			element = elementTemp;
			break;
		}
		
		if ( null != element ){
			return element.html();
		}else {
			return "";
		}
	}
	
	public static String clean(String html) throws Exception{
		Document doc = Jsoup.parse(html);
	    return doc.body().html();
	}
	
	
	public static List<UrlForm> captureInputElements(WebDriver driver, String htmlContents, String url, String path)  {
		FormElements formElement = null;
		List<FormElements> formElementList = null;
		List<String> fieldTypesToIgnore = new ArrayList<String>();
		List<UrlForm> formList = new ArrayList<UrlForm>();
		UrlForm urlForm = null; ;
		fieldTypesToIgnore.add("hidden");
		try {
		//	File file = new File("C:\\Tools\\mint\\Application\\Mint\\Resources\\transaction.txt");
			
			File file = new File(path);
			
			FileWriter fileWritter = null;
			BufferedWriter bw = null;
			PrintWriter pw = null;
		
			if(!file.exists()){
				
				file.createNewFile();
				fileWritter = new FileWriter(file,true);
				bw = new BufferedWriter(fileWritter);
				pw = new PrintWriter(bw);
			}

			Document doc = Jsoup.parse(htmlContents);
			Elements forms = doc.select("form");
			for( Element form : forms) {
				urlForm = new UrlForm();
				// Element form =  doc.select("form").first();
				urlForm.setFormId(form.attr("id"));
				urlForm.setFormName(form.attr("name"));
				formElementList = new ArrayList<FormElements>();
				for (Element field: form.select("input, textarea")) {
					if (field.attr("type")!=null && !field.attr("type").isEmpty() && !fieldTypesToIgnore.contains(field.attr("type").toLowerCase())) {
						formElement = new FormElements();
						formElement.setElementId(field.attr("id"));
						formElement.setElementName(field.attr("name"));
						formElement.setElementType(field.attr("type").toLowerCase());
						//   formElement.setElementValue(field.attr("value"));
						if(field.attr("id").length() > 0){
							if (isElementPresent(driver, By.id(field.attr("id")))) {
								formElement.setElementValue(driver.findElement(By.id(field.attr("id"))).getAttribute("value"));
							}

						}
						if(fileWritter!=null){
							pw.println(url +"," + field.attr("type").toLowerCase() +"," +field.attr("id")+"," +field.attr("name")+"," +field.attr("value") + "\n");
						}
						formElementList.add(formElement);
					}
				}
				for (Element field: form.select("select")) {
					formElement = new FormElements();
					formElement.setElementId(field.attr("id"));
					formElement.setElementName(field.attr("name"));
					formElement.setElementType("select");
					formElement.setElementValue(field.attr("value"));
					//  writer.write(field.attr("type").toLowerCase() +"," +field.attr("id")+"," +field.attr("name")+"," +field.attr("value") +"\n");
					if(fileWritter!=null){
						pw.println(url +"," + field.attr("type").toLowerCase() +"," +field.attr("id")+"," +field.attr("name")+"," +field.attr("value") + "\n");
					}
					formElementList.add(formElement);
				}

				/* for (Element field: form.select("submit, button")) {
		    	  formElement = new FormElements();
			      formElement.setElementId(field.attr("id"));
			      formElement.setElementName(field.attr("name"));
			      formElement.setElementType(field.attr("type"));
			      if(buttons.contains(formElement.getElementId())) {
			    	  formElement.setEvent("click");
			      }
			      formElementList.add(formElement);
		    }*/
				urlForm.setFormElements(formElementList);
				formList.add(urlForm);
			}
			if(fileWritter!=null){
				pw.close();
			}

		}catch(Exception e) {
			e.printStackTrace();
		}
		return formList;
	}
	
	 private static boolean isElementPresent(WebDriver driver, By by) {
		    try {
		      driver.findElement(by);
		      return true;
		    } catch (NoSuchElementException e) {
		      return false;
		    }
		  }
	public static void main(String[] args){
		//String htmlContents = MintFileUtils.readFromFile("C:\\Tools\\mint\\Application\\ResultsDirectory\\IWC_ST2_SSO_2015-Sep-11_10-58-00\\firefox\\PH8072466\\IWC_home.html");
		try {
			File file = new File("C:\\transaction.txt");
			//File file = new File(path);
			BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
