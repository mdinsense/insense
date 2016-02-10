package com.ensense.insense.core.webservice.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import javax.mail.MessagingException;
import javax.mail.internet.MimeUtility;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.log4j.Logger;

import com.cts.mint.webservice.entity.WsOperationParameter;
import com.cts.mint.webservice.entity.WsOperationXmlParameter;
import com.cts.mint.webservice.util.WebServiceSecurity;
import com.eviware.soapui.support.types.StringToStringMap;
import com.predic8.wadl.Application;
import com.predic8.wadl.Resource;
import com.predic8.wadl.Resources;
import com.predic8.wadl.WADLParser;

/**
* <h1>RestServiceUtil</h1>
* RestServiceUtil contains the common methods to parse WADL
*/
public class RestServiceUtil {

	private static final Logger logger = Logger.getLogger(RestServiceUtil.class);
	
	/**
	 * This method will help to generate the header tiaa-digest value 
	 * @param timestamp This should be the current EST timestamp
	 * @param password This is the Environment password for the requested RESful service
	 * @return String
	 */
	private static String createDigest(String timestamp, String password) {
		byte[] data = (timestamp + password).getBytes();
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA");
			
		} catch (NoSuchAlgorithmException e) {
			//throw new FaultException(GLOBAL_PARAMETERS_ERROR, e);
		}
		md.reset();
		md.update(data, 0, data.length);
		return encode(md.digest());
	}
	
	/**
	 * This method returns encoded value of MessageDigest
	 * @param cipherText This is the MessageDigest value
	 * @return String This is the encoded digest value
	 */
	public static String encode(byte[] cipherText) {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		OutputStream out = null;
		try {
			out = MimeUtility.encode(bout, "base64");
		} catch (MessagingException e) {
			//throw new FaultException(GLOBAL_PARAMETERS_ERROR, e);
		}
		try {
			out.write(cipherText);
			out.flush();
		} catch (IOException e) {
			logger.error("Exception in encode.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				logger.error("Exception in encode.");
				logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
			}
		}
		System.out.println("Password Digest:::"+bout.toString().trim());
		return bout.toString().trim();
	}
	static String machineName;
	static {
		try {
			machineName = InetAddress.getLocalHost().getHostName();
		} catch (Exception ex) {
			machineName = "unknown";
		}
	}
	
	
	/**
	 * This method is used to populate the required http headers to consume the RESTful service 
	 * @return Map<String, String>
	 * @throws IOException
	 */
	public static Map<String, String> populateHttpHeaders(int environmentID) throws IOException{
		Map<String, String> httpHeaders = new HashMap<String, String>();
		String mediaType = MediaType.APPLICATION_XML.toString();
		String digest ="";

		Date date = new Date(System.currentTimeMillis() + ((7*60) + 30) * 60 * 1000);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	//	System.out.println("Current Time:: "+dateFormat.format(date));
		try {
			if ( WebServiceSecurity.PROD.getEnvironmentId() == environmentID || WebServiceSecurity.PRODFIX.getEnvironmentId() == environmentID) {
				digest = createDigest(dateFormat.format(date), WebServiceSecurity.PROD.getPasswd());
				httpHeaders.put("tiaa-consumer", WebServiceSecurity.PROD.getUserId());
			} else {
				digest = createDigest(dateFormat.format(date), WebServiceSecurity.DEV.getPasswd());
				httpHeaders.put("tiaa-consumer", WebServiceSecurity.DEV.getUserId());
			}
			/*System.out.println("Digest :::::::::"+ digest);
			System.out.println("Timestamp :::::::::"+ dateFormat.format(date))*/;
			httpHeaders.put("tiaa-digest", digest);
			httpHeaders.put("tiaa-timestamp", dateFormat.format(date));

			httpHeaders.put("tiaa-guid", "12asda43423423aq");
			httpHeaders.put("tiaa-user-ref","123456");
			httpHeaders.put("tiaa-sender-machine", machineName);
			//httpHeaders.put("tiaa-consumer", "esbsupport");
			
			httpHeaders.put("tiaa-correlation-id", "1234567");
			httpHeaders.put("tiaa-partner-id", "12345");

		}catch(Exception e){
			logger.error("Exception in populateHttpHeaders.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		return httpHeaders;
	}

	/**
	 * This method retrieves all the resources of a resource. It works recursively.
	 * @param resources
	 * @param listOfResources
	 * @param RESTURI
	 * @param flag
	 * @return Map<Resource, String>
	 */
	public static Map<Resource, String> getListOfResources(List<Resource> resources, Map<Resource, String> listOfResources, String RESTURI, Integer flag) {
		try {
			String  PATH ="";
			String firstURI= RESTURI;
			for (Resource rsrc : resources) {

				if(flag==1) {
					if(RESTURI.endsWith("/") || rsrc.getPath().startsWith("/"))
					PATH= RESTURI+rsrc.getPath();
					else
					PATH= RESTURI+"/"+rsrc.getPath();
				}
				else { 
					if(RESTURI.endsWith("/") || rsrc.getPath().startsWith("/"))
					PATH= firstURI+rsrc.getPath();
					else
					PATH= firstURI+"/"+rsrc.getPath();
				}
				if(rsrc.getMethods().size() > 0) {
					listOfResources.put(rsrc, PATH);
				}
				if (rsrc.getResources().size() > 0) {
					RESTURI = PATH;
					getListOfResources(rsrc.getResources(), listOfResources, PATH, 1);

				} 
			}
		} catch (Exception e) {
			logger.error("Exception in getListOfResources.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		return listOfResources;
	}
	
	/**
	 * This method helps to generate the request XML from XSD, associated with the resource
	 * @param selectedNewResource 
	 * @param xsdFileName Name of the XSD file 
	 * @return xmlReq The sesult XML
	 */
	public static String getRequestXMLFromXSD(Resource selectedNewResource, String xsdFileName ) {
		String requestingObject = selectedNewResource.getMethods().get(0).getRequest().getRepresentations().get(0).getElementPN().getLocalName();
		Boolean optionalParamFlag = true;
		String xmlReq = XMLGeneratorFromXSD.generateXML(requestingObject, optionalParamFlag, xsdFileName);
		return xmlReq;
	}
	
	public static String executeRESTfulPost(Resource selectedNewResource, String selectedPath, 
					HttpParams queryParams,  HttpParams httpParams, String tiaaConsumer, String requestXML) {
		String responseXML ="";
		try {
		
		HttpClient client = new DefaultHttpClient();
		String mediaType = selectedNewResource.getMethods().get(0).getRequest().getRepresentations().get(0).getMediaType();
		HttpPost post = new HttpPost(selectedPath);
		if(queryParams != null){
			post.setParams(queryParams);
		}
		post.addHeader("Content-Type", mediaType);
		post.addHeader("tiaa-timestamp", (String) httpParams.getParameter("tiaa-timestamp"));
		post.addHeader("tiaa-digest", (String) httpParams.getParameter("tiaa-digest"));
		post.addHeader("tiaa-consumer", tiaaConsumer);
		requestXML = requestXML.replaceAll(">\\s*<", "><");
		post.setEntity(new StringEntity(requestXML));
		HttpResponse response = client.execute(post);
		responseXML = getResponseAsString(response);
		    
		} catch (UnsupportedEncodingException e) {
			logger.error("Exception in executeRESTfulPost.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}catch (Exception e) {
			logger.error("Exception in executeRESTfulPost.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		return responseXML;
	}
	
	public static String getResponseAsString(HttpResponse response) {
	    String text = "";
	    try {
	      text = getResponseAsString(response.getEntity().getContent());
	    } catch (Exception ex) {
	    }
	    return text;
	  }
	
	 public static String getResponseAsString(InputStream in) {
		    String text = "";
		    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		    StringBuilder sb = new StringBuilder();
		    String line = null;
		    try {
		      while ((line = reader.readLine()) != null) {
		        sb.append(line + "\n");
		      }
		      text = sb.toString();
		    } catch (Exception e) {
		    	logger.error("Exception in getResponseAsString.");
				logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		    } finally {
		      try {
		        in.close();
		      } catch (Exception e) {
		    	  logger.error("Exception in getResponseAsString.");
				  logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		      }
		    }
		    return text;
		  }

	 public static String generatePathWithPathParameterFromXSD(Resource selectedNewResource, String selectedPath, String xsdFileName){
		 	String pathParamfromXSDObject = selectedNewResource.getMethods().get(0).getId();
			String xmlReq = XMLGeneratorFromXSD.generateXML(pathParamfromXSDObject, true, xsdFileName);
			XmlParser xs = new XmlParser(xmlReq);
			
			Map<String, String> reqElements = xs.generateMap(xmlReq);
			
			Set mapSet = (Set) reqElements.entrySet();
			Iterator mapIterator = mapSet.iterator();
			while (mapIterator.hasNext()) {
             Map.Entry mapEntry = (Map.Entry) mapIterator.next();
             
             String key = (String) mapEntry.getKey();
             if(!key.startsWith("xmlns")){
             Scanner pathParam = new Scanner(System.in);
             System.out.println("Enter the value for "+ key);
             String keyvalue = pathParam.nextLine();
             
             selectedPath = selectedPath.replace("{"+ key.toLowerCase() + "}", keyvalue);
             }
			}
		 return selectedPath;
	 }
	 public static String executeRESTfulGet(Resource selectedNewResource, String selectedPath, 
			 HttpParams queryParams,  HttpParams httpParams, String tiaaConsumer, String requestXML) {
		 String responseXML ="";
		 try {

			 HttpClient client = new DefaultHttpClient();
			 String mediaType = selectedNewResource.getMethods().get(0).getRequest().getRepresentations().get(0).getMediaType();
			 HttpGet request = new HttpGet(selectedPath);
			 if(queryParams != null){
				 request.setParams(queryParams);
			 }
			 request.addHeader("Content-Type", mediaType);
			 request.addHeader("tiaa-timestamp", (String) httpParams.getParameter("tiaa-timestamp"));
			 request.addHeader("tiaa-digest", (String) httpParams.getParameter("tiaa-digest"));
			 request.addHeader("tiaa-consumer", tiaaConsumer);
			
			 HttpResponse response = client.execute(request);
			 responseXML = getResponseAsString(response);

		 } catch (UnsupportedEncodingException e) {
			  logger.error("Exception in executeRESTfulGet.");
			  logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		 }catch (Exception e) {
			 logger.error("Exception in executeRESTfulGet.");
			 logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		 }
		 return responseXML;
	 }

	public static String checkServiceType(String filepath, String serviceName, String type) {
		String serviceType ="";
		File folder = new File(filepath);
		File[] listOfFiles = folder.listFiles();
		for(int i = 0; i < listOfFiles.length; i++){
		    String filename = listOfFiles[i].getName();
		    if(filename.startsWith(serviceName)){
		    	serviceType = type;
		    }
		}
		
		return serviceType;
	}

	public static String getRestServiceResponse(String requestPath, String methodType, HttpParams queryParams,
			String contentType, StringEntity stringEntity, List<WsOperationParameter> headers, int environmentID,
			StringToStringMap customHeaders) {
		
		String responseString = "";
		HttpClient client = new DefaultHttpClient();
		try {
			Map<String, String> tiaaHeaders = RestServiceUtil.populateHttpHeaders(environmentID);
			if(methodType.equalsIgnoreCase("GET")){
				HttpGet request = new HttpGet(requestPath);
				if(queryParams != null){
					request.setParams(queryParams);
				}
				request.addHeader("Content-Type", contentType.split(";")[0]);
				request.addHeader("Accept",contentType.split(";")[0]);
				/*request.addHeader("tiaa-timestamp", (String) tiaaHeaders.get("tiaa-timestamp"));
				request.addHeader("tiaa-digest", (String) tiaaHeaders.get("tiaa-digest"));
				request.addHeader("tiaa-consumer", "esbsupport");*/
				for(WsOperationParameter t :headers){
					request.addHeader(t.getParameterName(),(String) tiaaHeaders.get(t.getParameterName()));
				}
				logger.info("Request path:::::::::::::::"+ requestPath);
				logger.info("URI::::::::::::::::::::::::"+ request.getURI());
				Set mapSet = (Set) customHeaders.entrySet();
				Iterator mapIterator = mapSet.iterator();
				while (mapIterator.hasNext()) {
					Map.Entry mapEntry = (Map.Entry) mapIterator.next();
					String parameter = (String) mapEntry.getKey();
					String parameterVal = (String) mapEntry.getKey();
					logger.info("Parameter Name:" + parameter);
					logger.info("Parameter Value:" + parameterVal);
					request.addHeader(parameter,parameterVal);
				}
				HttpResponse response = client.execute(request);
				responseString = new BasicResponseHandler().handleResponse(response);
				System.out.println(responseString);
				
			} else if(methodType.equalsIgnoreCase("POST")){
				logger.info("Request path:::::::::::::::"+ requestPath);
				HttpPost post = new HttpPost(requestPath);
				logger.info("URI::::::::::::::::::::::::"+ post.getURI());
				logger.info("queryParams :"+queryParams);
				logger.info("contentType :"+contentType);
				logger.info("stringEntity :"+stringEntity);
				post.addHeader("Content-Type", contentType);
				/*post.addHeader("tiaa-timestamp", (String) tiaaHeaders.get("tiaa-timestamp"));
				post.addHeader("tiaa-digest", (String) tiaaHeaders.get("tiaa-digest"));
				post.addHeader("tiaa-consumer", "esbsupport");*/
				for(WsOperationParameter t :headers){
					post.addHeader(t.getParameterName(),(String) tiaaHeaders.get(t.getParameterName()));
				}
				if(queryParams != null){
					post.setParams(queryParams);
				}
				post.setEntity(stringEntity);
				HttpResponse response = client.execute(post);
				responseString = new BasicResponseHandler().handleResponse(response);
				System.out.println(responseString);
			}
		}
		catch (ClientProtocolException e) {
			 logger.error("Exception in getRestServiceResponse.");
			 logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		} catch (IOException e) {
			 logger.error("Exception in getRestServiceResponse.");
			 logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}  catch(Exception e) {
			 logger.error("Exception in getRestServiceResponse.");
			 logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		return responseString;
	}
	 
	public static Map<Resource, String> getResources(String wadlFile) {
		WADLParser parser = new WADLParser();
		String REST_URI = "";
		Map<Resource, String> listOfResources = null;
		Application app = parser.parse(wadlFile);
		List<Resources> res = app.getRscss();
		listOfResources = new HashMap<Resource, String>();
		for(Resources rsrc: res){
			List<Resource> listResource = rsrc.getResources();
		
			for(int i=0; i < listResource.size() ;i++){
				Resource resource = listResource.get(i);
				List<Resource> resources = resource.getResources();
				Resources basePathRsrc = (Resources) resource.getParent();
				//basePathRsrc.setBase("http://pf-int5-dpw.test.tiaa-cref.org/retirement-plan-transaction-rs-v1");
				//REST_URI = basePathRsrc.getBase()+resource.getPath();
				REST_URI = resource.getPath();
				if(resource.getMethods().size() > 0) {
					listOfResources.put(resource, REST_URI);
				}
				listOfResources = RestServiceUtil.getListOfResources(resources, listOfResources, REST_URI, 0);
		
		}
		}
		return listOfResources;
		}

	public static String getXSDFileName(String wadlFile) {
		WADLParser parser = new WADLParser();
		Application app = parser.parse(wadlFile);
		String xsdFileName ="";
		if(app.getGrammars().getIncludes().size() >0){
			xsdFileName = app.getGrammars().getIncludes().get(0).getHref();
		}
		String[] xsdFileNames = xsdFileName.split("/");
		xsdFileName = xsdFileNames[xsdFileNames.length -1];
		return xsdFileName;
	}
	public static String getPingServiceResponse(String requestPath, String methodType) {
		
		String responseString = "";
		String mediaType = "application/xml";
		HttpClient client = new DefaultHttpClient();
		try {
			HttpGet request = new HttpGet(requestPath);
			HttpResponse response = client.execute(request);
			responseString = new BasicResponseHandler().handleResponse(response);
			System.out.println(responseString);
		}
		 catch(Exception e) {
			 logger.error("Exception in getPingServiceResponse.");
			 logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		return responseString;
	}

	public static String getXSDFilePath(String wadlFilePath, String xsdFileName) {

		String xsdFilePath = "";
		File dir = new File(wadlFilePath);
	
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) {

			for (File child : directoryListing) {
				if (!child.isDirectory()) {

					String name = child.getName();

					if (!name.contains(".")) {
					}
					else {
						if (name.equalsIgnoreCase(xsdFileName)){
							xsdFilePath= child.getAbsolutePath();
							break;
						}
					}
				} else {
					getXSDFilePath(child.getAbsolutePath(), xsdFileName);
				}
			}
		}
	
	
		return xsdFilePath;
		}
	
	public static void main(String ar[]) {
		String k = "mu".split(";")[0];
		System.out.println(k);
	}
	
	}
	
	 
