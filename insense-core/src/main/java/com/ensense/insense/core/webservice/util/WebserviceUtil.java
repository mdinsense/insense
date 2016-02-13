package com.ensense.insense.core.webservice.util;

import com.ensense.insense.core.webservice.model.ServiceDetails;
import com.ensense.insense.core.webservice.model.ServiceStatus;
import com.ensense.insense.core.webservice.model.ServiceUrl;
import com.ensense.insense.data.common.utils.DateTimeUtil;
import com.ensense.insense.data.common.utils.FileDirectoryUtil;
import com.ensense.insense.data.common.utils.Constants;
import com.ensense.insense.core.analytics.utils.EmailUtil;
import com.ensense.insense.data.webservice.entity.WSPingResults;
import com.ensense.insense.data.webservice.entity.WSPingSchedule;
import com.ensense.insense.data.webservice.entity.Webservices;
import com.ensense.insense.data.webservice.entity.WsOperationParameter;
import com.ensense.insense.data.webservice.model.WebserviceSetupForm;
import com.ensense.insense.services.webservice.WebserviceTestingService;
import com.eviware.soapui.impl.wsdl.*;
import com.eviware.soapui.impl.wsdl.support.wsdl.WsdlImporter;
import com.eviware.soapui.model.iface.Response;
import com.eviware.soapui.support.types.StringToStringMap;
import com.google.common.net.HttpHeaders;
import com.predic8.wadl.Method;
import com.predic8.wadl.Param;
import com.predic8.wadl.Resource;
import com.predic8.wsdl.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.log4j.Logger;
import org.pojoxml.core.PojoXml;
import org.pojoxml.core.PojoXmlFactory;
import org.springframework.context.MessageSource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.sql.Clob;
import java.text.SimpleDateFormat;
import java.util.*;
public class WebserviceUtil {
	
	private static Logger logger = Logger.getLogger(WebserviceUtil.class);
	
	
	public static List<String> getServicesToConfigure(MessageSource configProperties, 
			WebserviceTestingService webserviceTestingService) {
		
		List<String> servicesToBeAdded = new ArrayList<String>();
		
		
		// Getting soap services to be added
		
		Map<String, String> serviceMap = getSoapServices(configProperties);
		Iterator<Map.Entry<String, String>> itr = serviceMap.entrySet().iterator();

		while (itr.hasNext()) {
			Map.Entry<String, String> entry = itr.next();
			Webservices webservice = webserviceTestingService.getServicefromServiceName(entry.getValue());
			if (webservice.getServiceId() == 0) {
				logger.info("Service not already there:" + entry.getValue());
				servicesToBeAdded.add(entry.getValue());
			} else {
				logger.info("Service already exists :" + entry.getValue());
			}
		}
		
		
		//Getting restful services to be added
	
		Map<String, String> serviceMapForWadl = getRestServices(configProperties);
		
		Iterator<Map.Entry<String, String>> iterator = serviceMapForWadl.entrySet().iterator();

		while (iterator.hasNext()) {
			Map.Entry<String, String> entry = iterator.next();
			Webservices webservice = webserviceTestingService.getServicefromServiceName(entry.getValue());

			if (webservice.getServiceId() == 0) {
				logger.info("Service not already there:" + entry.getValue());
				servicesToBeAdded.add(entry.getValue());
			} else {
				logger.info("Service already exists :" + entry.getValue());
			}
		}
		
		return servicesToBeAdded;
	}
	
	
	public static Map<String, String> getServiceList(String filepath, String extn, Map<String, String> serviceNameList) {
		File dir = new File(filepath);
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) {

			for (File child : directoryListing) {
				if (!child.isDirectory()) {

					String name = child.getName();

					// if there is no extention, don't do anything
					if (!name.contains(".")) {
						// logger.info("namehere :"+name);
					}
					// Otherwise, remove the last 'extension type thing'
					else {
						if (name.endsWith(extn)){
							serviceNameList.put(child.getAbsolutePath(), name.substring(0,
									name.lastIndexOf('.')));

						}
					}
				} else {
					getServiceList(child.getAbsolutePath(), extn, serviceNameList);
				}
			}
		}
		logger.info("serviceNameList :"+serviceNameList);
		return serviceNameList;
	}
	
	public static Map<String, String> getSoapServices(MessageSource configProperties) {
		String wsdlFilePath = FileDirectoryUtil.getWSDLFilePath(configProperties);
		Map<String, String> serviceMap = new HashMap<String, String>();
		return serviceMap = getServiceList(wsdlFilePath, ".wsdl", serviceMap);
	}
	
	public static Map<String, String> getRestServices(MessageSource configProperties) {
		String wsdlFilePath = FileDirectoryUtil.getWADLFilePath(configProperties);
		Map<String, String> serviceMap = new HashMap<String, String>();
		return serviceMap = getServiceList(wsdlFilePath, ".wadl", serviceMap);
	}
	
	public static Boolean parseWSDL(String serviceName, String filepath, WebserviceTestingService webserviceTestingService) {
		logger.debug("Entry: parseWSDL, filepath->"+filepath+", serviceName->"+serviceName);
		
		Boolean result = false;
		WSDLParser parser = new WSDLParser();
		Definitions defs = parser.parse(filepath);
		
		File wsdlFilePath = new File(filepath);
		String wsdlServiceName = wsdlFilePath.getName();
		String serviceNameFromFile = wsdlServiceName.substring(0,wsdlServiceName.lastIndexOf('.'));
		
		logger.info("serviceName From File: " + serviceNameFromFile);
		logger.info("serviceName from user: " + serviceName);
		
		// inserting the service name
		webserviceTestingService.insertServiceNames(serviceName, "wsdl", filepath);
		Integer serviceId = webserviceTestingService.getServicefromServiceName(
				serviceName).getServiceId();
		
		
		// Inserting opration name and parameters
		for (PortType pt : defs.getPortTypes()) {
			logger.info(pt.getName());
			for (Operation op : pt.getOperations()) {
				logger.info(" -" + op.getName());
				String operationName = op.getName();
				
				if (!operationName.equalsIgnoreCase("ping")) {
					
					result = webserviceTestingService.insertOperationNames(operationName,
							serviceId,null, "");
					
					Integer operationId = webserviceTestingService
					.getOperationIdfromOperationName(serviceId, operationName, "")
							.getOperationId();
				
					if (result) {
						for (Part part : op.getInput().getMessage().getParts()) {
							String requestXml = part.getElement()
									.getRequestTemplate();

							try {
								result = updateRequestParameters(requestXml, operationName,
										serviceId, operationId, webserviceTestingService);
							} catch (Exception e) {
								e.printStackTrace();
							}

						}
					}
				}
			}
		}
		return result;
	}


	public static Boolean updateRequestParameters(String requestXml, String operationName,
			Integer serviceId, Integer operationId, WebserviceTestingService webserviceTestingService) throws Exception {
		Boolean result = false;
		try {

			String text = requestXml;
			Reader reader1 = new StringReader(text);
			XMLInputFactory factory = XMLInputFactory.newInstance(); 
			XMLStreamReader xmlReader = factory.createXMLStreamReader(reader1);
			Set<String> namespaces = new HashSet<String>();

			while ((xmlReader).hasNext()) {
				int evt = xmlReader.next();
				if (evt == XMLStreamConstants.START_ELEMENT) {
					QName qName = xmlReader.getName();
					if (qName != null) {
						if (qName.getPrefix() != null && qName.getPrefix().compareTo("") != 0)
							namespaces.add(String.format("%s",
									qName.getLocalPart()));
					}
				}
			}
			namespaces.remove(operationName);
			List<String> reqParameters = new ArrayList<String>();

			for (String namespace : namespaces) {
				reqParameters.add(namespace);
			}

			for (String parameter : reqParameters) {
				
				WsOperationParameter wsOperationParameter = new WsOperationParameter();
				wsOperationParameter.setServiceId(serviceId);
				wsOperationParameter.setOperationId(operationId);
				wsOperationParameter.setParameterName(parameter);
				wsOperationParameter.setParamType("");
				String sampleXML = "";
				result = webserviceTestingService
						.insertRequestParameterName(wsOperationParameter, sampleXML);
			}

		} catch (XMLStreamException e) {
			logger.error("Exception in updateRequestParameters.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			logger.error("Exception in updateRequestParameters.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}

		return result;
	}

	public static Boolean checkWADL(String serviceName, String servicePath,  MessageSource configProperties, WebserviceTestingService webserviceTestingService) {
		logger.info("Entry: checkWADL ");
		Boolean result = false;
		Map<String, String>  serviceNamesMap = getRestServices(configProperties);
		if (serviceNamesMap.containsValue(serviceName)) {
			result = parseWADL(serviceName, servicePath, configProperties, webserviceTestingService);
		} else {
			logger.info("WADL file for the service: " + serviceName
					+ "does not exist in the given directory ");
		}
		return result;
	}
	
	public static Boolean checkWSDL(String serviceName, String servicepath, MessageSource configProperties,
			WebserviceTestingService webserviceTestingService) {
		logger.debug("Entry: checkWSDL, serviceName->"+serviceName+", servicepath->"+servicepath);
		Boolean result = false;
		
		Map<String, String>  serviceNamesMap = getSoapServices(configProperties);
		boolean isServiceFound = false;
		for (Map.Entry<String,String> entry : serviceNamesMap.entrySet()) {
		    if ( StringUtils.equals(serviceName, entry.getValue())) {
		    	result = parseWSDL(serviceName, entry.getKey(), webserviceTestingService);
		    	isServiceFound = true;
		    }
		}
		if ( !isServiceFound ) {
			logger.info("WSDL file for the service: " + serviceName
					+ ", does not exist in the given directory");
		}

		return result;
	}

	
	public static Boolean parseWADL(String serviceName, String filepath,MessageSource configProperties, 
			WebserviceTestingService webserviceTestingService) {
		Map<Resource, String> listOfResources = null;
		HttpParams httpParams = null,queryParams = null;
		Map<String, String> tiaaHeaders =null;
		HttpHeaders httpHeaders = null;
		String xsdFileName = "";
		String xmlReq ="";
		logger.info("Entry: parseWADL ");
		Boolean result = false;
		try {
			//File f = new File(filepath + "\\"+ serviceName + ".wadl");
			File f = new File(filepath);
			String REST_URI = "";
			String servName = f.getName();
			logger.info("service name: "
					+ servName.substring(0, servName.lastIndexOf('.')));
			String serviceNameFromFile = servName.substring(0,
					servName.lastIndexOf('.'));
			logger.info("serviceNameFromFile: " + serviceNameFromFile);
			logger.info("serviceName from user: " + serviceName);
			// inserting the service name
			webserviceTestingService.insertServiceNames(serviceName, "wadl", filepath);
			Integer serviceId = webserviceTestingService.getServicefromServiceName(serviceName).getServiceId();
			String wadlPath = FileDirectoryUtil.getWADLFilePath(configProperties);
		
			String wadlFile = filepath;
			xsdFileName = RestServiceUtil.getXSDFileName(wadlFile);
			String xsdFilePath = RestServiceUtil.getXSDFilePath(wadlPath, xsdFileName);
			listOfResources = RestServiceUtil.getResources(wadlFile);
			Iterator<Resource> keyIterator = listOfResources.keySet().iterator();
			while(keyIterator.hasNext()) {
				Resource keyResource = keyIterator.next();
				String operName = listOfResources.get(keyResource);
				String requestingObject = "";
				String methodType ="", contentType ="", requestObjectName ="";
				WsOperationParameter wsOperationParameter = new WsOperationParameter();
				wsOperationParameter.setServiceId(serviceId);
				for(Method method: keyResource.getMethods()) {
					if (!operName.endsWith("ping")) {
						if( method.getRequest() !=null 
								&& method.getRequest().getRepresentations().size() > 0 &&
								method.getRequest().getRepresentations().get(0)!= null){
							//requestingObject = keyResource.getMethods().get(0).getRequest().getRepresentations().get(0).getElementPN().getLocalName();

							methodType = method.getName();								
							contentType = method.getRequest().getRepresentations().get(0).getMediaType();
							//TODO: Commented because of the exception
							//requestObjectName = method.getRequest().
							//getRepresentations().get(0).getElementPN().getLocalName();
							

						}
						if (contentType == null || contentType.isEmpty()) {
							if (keyResource.getMethods().size() > 0
									&& keyResource.getMethods().get(0)
									.getResponse() != null
									&& keyResource.getMethods().get(0)
									.getResponse().getRepresentations()
									.size() > 0
									&& keyResource.getMethods().get(0)
									.getResponse().getRepresentations()
									.get(0) != null) {
								contentType = keyResource.getMethods().get(0)
								.getResponse().getRepresentations().get(0)
								.getMediaType();
							}
						}

						if(requestObjectName.equals("") || requestObjectName == null){
							requestObjectName = keyResource.getMethods().get(0).getId();
						}
						if(contentType == null || contentType.isEmpty()) {
							if( method.getResponse() !=null 
									&& method.getResponse().getRepresentations().size() > 0 &&
									method.getResponse().getRepresentations().get(0)!= null){
								contentType	= method.getResponse().getRepresentations().get(0).getMediaType();
							}
						}
						if(methodType.isEmpty())
							methodType ="GET";
						
						result = webserviceTestingService.insertOperationNames(operName,
								serviceId, methodType, contentType);

						if( result == true) {
							Integer operationID = webserviceTestingService
							.getOperationIdfromOperationName(serviceId, operName, methodType)
							.getOperationId();
							wsOperationParameter.setOperationId(operationID);

							if(method.getId()!= null  && !method.getId().isEmpty()){

								//xmlReq = XMLGeneratorFromXSD.generateXML(requestObjectName, true, wadlPath + "\\" + xsdFileName);
								xmlReq = XMLGeneratorFromXSD.generateXML(requestObjectName, true, xsdFilePath);

								if(methodType.equalsIgnoreCase("POST")){
									wsOperationParameter.setParameterName("ReqXML");
									wsOperationParameter.setParamType("X");
								
									//testOperationParameter.setSampleXML(xmlReq);
									result = webserviceTestingService.insertRequestParameterName(wsOperationParameter, xmlReq);
								} else if(methodType.equalsIgnoreCase("GET")){
									if(!xmlReq.isEmpty()){
										XmlParser xs = new XmlParser(xmlReq);

										Map<String, String> reqElements = xs.generateMap(xmlReq);

										Set mapSet = (Set) reqElements.entrySet();
										Iterator mapIterator = mapSet.iterator();
										while (mapIterator.hasNext()) {
											Map.Entry mapEntry = (Map.Entry) mapIterator.next();

											String key = (String) mapEntry.getKey();
											if(!key.startsWith("xmlns")){
												//String sampleXML = "";
												wsOperationParameter.setParameterName(key);
												wsOperationParameter.setParamType("T");
												result = webserviceTestingService
												.insertRequestParameterName(wsOperationParameter, xmlReq);
											}
										}
									}
								}
							}
						}
						List<Param> params = keyResource.getParams();
						if(params.size() > 0){
							httpParams = new BasicHttpParams();
							for(Param p: params) {
								String name = p.getName();
								wsOperationParameter.setParameterName(name);
								String defVal = p.getDfault();
								String style= p.getStyle();
								if(style.equalsIgnoreCase("query")){
									wsOperationParameter.setParamType("Q");
								} else if(style.equalsIgnoreCase("header")) {
									wsOperationParameter.setParamType("H");
								} else if(style.equalsIgnoreCase("template")) {
									wsOperationParameter.setParamType("T");
								}else{
									wsOperationParameter.setParamType("");	
								}
								String sampleXML = "";
								result = webserviceTestingService
								.insertRequestParameterName(wsOperationParameter, sampleXML);
							}
						}

						Resource tempResource = keyResource;
						while(tempResource.getParent() instanceof Resource) {
							Resource parentResource = (Resource) tempResource.getParent();
							if(parentResource.getParams().size() > 0){
								List<Param> parentParams = parentResource.getParams();
								for(Param p: parentParams) {
									String name = p.getName();

									wsOperationParameter.setParameterName(name);
									String defVal = p.getDfault();
									String style= p.getStyle();
									if(style.equalsIgnoreCase("query")){
										wsOperationParameter.setParamType("Q");
									} else if(style.equalsIgnoreCase("header")) {
										wsOperationParameter.setParamType("H");
									} else if(style.equalsIgnoreCase("template")) {
										wsOperationParameter.setParamType("T");
									} else{
										wsOperationParameter.setParamType("");
									}
									String sampleXML = "";
									result = webserviceTestingService
									.insertRequestParameterName(wsOperationParameter, sampleXML);
								}

							}

							tempResource = parentResource;
						}
						if( method.getRequest() !=null && method.getRequest().getParams().size() > 0) {
							List<Param> parentParams = method.getRequest().getParams();
							for(Param p: parentParams) {
								String name = p.getName();
								wsOperationParameter.setParameterName(name);
								String defVal = p.getDfault();
								String style= p.getStyle();
								if(style.equalsIgnoreCase("query")){
									wsOperationParameter.setParamType("Q");
								} else if(style.equalsIgnoreCase("header")) {
									wsOperationParameter.setParamType("H");
								} else if(style.equalsIgnoreCase("template")) {
									wsOperationParameter.setParamType("T");
								} else {
									wsOperationParameter.setParamType("");
								}
								String sampleXML = "";
								result = webserviceTestingService
								.insertRequestParameterName(wsOperationParameter, sampleXML);
							}
						}
					}
				}
			}
		}
		catch(Exception e) {
			logger.error("Exception in parseWADL.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		return result;
	}
	
	public static String createSOAPreq(String WSDLFileName, String serviceName, String operationName,
			Map<String, String> requestValuesMap) {

		logger.info("Entry : createSOAPreq");
		
		WsdlInterface[] wsdls;
		String processed = null;
		try {
			WsdlProject project = new WsdlProject();
			wsdls = WsdlImporter.importWsdl(project, WSDLFileName);

			WsdlInterface wsdl = wsdls[0];
			for (com.eviware.soapui.model.iface.Operation operation : wsdl
					.getOperationList()) {

				WsdlOperation op = (WsdlOperation) operation;
				if (op.getName().equalsIgnoreCase(operationName)) {

					String rawRequest = op.createRequest(true);

					processed = setRequestParameters(rawRequest,
							requestValuesMap);

					processed = getHeaderValues(processed);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Exit : createSOAPreq");
		return processed;
	}
	
	private static String setRequestParameters(String requestXML,
			Map<String, String> requestValuesMap) {
		logger.info("Entry: setRequestParameters");
		String processed = requestXML;
		Iterator reqvaluesIterator = requestValuesMap.keySet().iterator();
		try {
			if(processed!=null){
				while (reqvaluesIterator.hasNext()) {
	
					String key = (String) reqvaluesIterator.next();
	
					if(requestValuesMap.get(key).isEmpty()){
						processed=	deleteTag(processed, key, requestValuesMap);
					}
					else{
						processed = replaceUsingXPath(processed,
								"//" + key + "/text()", requestValuesMap.get(key));
					}
				}
			}
			
		} catch (Exception e) {
			logger.error("Exception in setRequestParameter :"+e);
			e.printStackTrace();
		}
		logger.info("Exit: setRequestParameters");
		return processed;
	}
	
	private static String deleteTag(String requestXml, String tagToBeRemoved, Map<String, String> requestValuesMap) {
		 StringWriter writer = new StringWriter();
	     StreamResult result = new StreamResult(writer);
		 try {
			 logger.info("tagToBeRemoved :"+tagToBeRemoved);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder2 = factory.newDocumentBuilder();
			Document doc = builder2.parse(new ByteArrayInputStream(requestXml.getBytes("UTF-8"))); 
			Element element = (Element)doc.getElementsByTagName("typ:"+tagToBeRemoved).item(0);
			
			logger.info("requestXml :"+requestXml);
			if ( element != null && element.getParentNode() != null && ! elementChildContainsRequestParamValue(element, requestValuesMap) ){
				element.getParentNode().removeChild(element);
			}
		    
		    DOMSource domSource = new DOMSource(doc);
			
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.transform(domSource, result);
		 }
		    catch (Exception e) {
				logger.error("Exception while removing the optional node:"+e);
				e.printStackTrace();
			}
		    return writer.toString();
		 }
	
	private static boolean elementChildContainsRequestParamValue(Element element,
			Map<String, String> requestValuesMap) {
		try{ 
			NodeList nodes = element.getChildNodes();
			for (int i = 0; i < element.getChildNodes().getLength(); i++) {
				nodes.item(i);
				
				if ( null!= nodes.item(i).getNodeName() && nodes.item(i).getNodeName().contains("typ:")) {
					String key = nodes.item(i).getNodeName();
					key = key.replace("typ:", "");
					String value = requestValuesMap.get(key);
					
					if ( null != value && value.length() > 0 ){
						return true;
					}
				}
			}
		}catch(Exception e){
			logger.error("Exception while check the child nodes."+e);
		}

		return false;
	}
	
	public static String replaceUsingXPath(String requestXml, String path,
			String value) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(new ByteArrayInputStream(requestXml
				.getBytes("UTF-8")));
		XPathFactory xPathfactory = XPathFactory.newInstance();
		XPath xpath = xPathfactory.newXPath();
		NodeList myNodeList = (NodeList) xpath.compile(path).evaluate(doc,
				XPathConstants.NODESET);
		myNodeList.item(0).setNodeValue(value);
		DOMSource domSource = new DOMSource(doc);
		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.transform(domSource, result);

		return writer.toString();
	}
	
	public static String getHeaderValues(String processed) {
		logger.info("Entry : setHeaderValues");
		try {
			processed = replaceUsingXPath(processed, "//sinfo/@conversationID",
					" 1222");
			processed = replaceUsingXPath(processed, "//sinfo/@correlationID",
					" 5678");
			processed = replaceUsingXPath(processed, "//sinfo/@guid",
					HeaderHelper.createGuid());
			processed = replaceUsingXPath(processed, "//sinfo/@ping", " ping1");
			processed = replaceUsingXPath(processed, "//sinfo/@senderMachine",
					" chapd");
			processed = replaceUsingXPath(processed, "//sinfo/@userRef",
					"1000100");
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		logger.info("Exit : setHeaderValues");
		return processed;
	}
	
	public static String executeWebServiceOperation(String wsdlFilePath, String requestXml, String serviceName, String operationName, String endPoint, Integer environmentId, StringToStringMap customHeaders) {

		logger.info("Enter : executeWebServiceOperation");

		WsdlInterface[] wsdls;
		String healthCheck = null;
		String operationResponse = "";
		
		try {
			File file = new File(wsdlFilePath);
			
			if ( ! file.exists() ) {
				logger.error("WSDL File not found. WSDL File ->"+wsdlFilePath);
			} 
		} catch(Exception e){
			logger.error("WSDL File not found. WSDL File ->"+wsdlFilePath);
			e.printStackTrace();
			return null;
		}
		
		try {

			WsdlProject project = new WsdlProject();
			
			wsdls = WsdlImporter.importWsdl(project, wsdlFilePath);

			WsdlInterface wsdl = wsdls[0];
			
			for (com.eviware.soapui.model.iface.Operation operation : wsdl
					.getOperationList()) {

				WsdlOperation op = (WsdlOperation) operation;
				
				if (op.getName().equalsIgnoreCase(operationName)) {
					String rawRequest = op.createRequest(true);
				
					WsdlRequest request = op.addNewRequest(operationName);
					request.setRequestHeaders(customHeaders);
					request.setRequestContent(requestXml);
					request.setEndpoint(endPoint);
					
					if ( WebServiceSecurity.PROD.getEnvironmentId() == environmentId || WebServiceSecurity.PROD_WEBSERVICE.getEnvironmentId() == environmentId) {
						request.setUsername(WebServiceSecurity.PROD.getUserId());
						request.setPassword(WebServiceSecurity.PROD.getPasswd());
						request.setWssPasswordType(WebServiceSecurity.PROD.getPasswdType());
					} else {
						request.setUsername(WebServiceSecurity.DEV.getUserId());
						request.setPassword(WebServiceSecurity.DEV.getPasswd());
						request.setWssPasswordType(WebServiceSecurity.DEV.getPasswdType());
					}
					
					// submit the request
					WsdlSubmit submit =(WsdlSubmit) request.submit( new WsdlSubmitContext( request ), false );


					// print the response
					Response  response = submit.getResponse();
					operationResponse = response.getContentAsString();
				}
			}
			logger.info("Exit : executeWebServiceOperation");
		} catch (Exception e) {
			logger.error("Exception in executeWebServiceOperation :"+e);
			e.printStackTrace();
		}
		return operationResponse;
	}
	
	public static ServiceUrl getEsbPingServiceDetails(String serviceName,
													  WebserviceSetupForm webserviceSetupForm, MessageSource esbpingUrlProperty) {
	
		ServiceUrl serviceUrl = new ServiceUrl();
		String esbPingUrl = "";
		String esbServiceProperty = "";

		if (null != webserviceSetupForm.getEnvironment()
				&& webserviceSetupForm.getEnvironment().length() > 0
				&& null != serviceName && serviceName.length() > 0) {
			serviceUrl.setServiceName(serviceName);
			esbServiceProperty = webserviceSetupForm.getEnvironment() + "_"
					+ serviceName;
			logger.info("esbServiceProperty :" + esbServiceProperty);
			try {
				esbPingUrl = esbpingUrlProperty.getMessage(esbServiceProperty,
						null, Locale.getDefault());
				String[] property = esbPingUrl.split(",");
				serviceUrl.setEnvironment(webserviceSetupForm.getEnvironment());
				serviceUrl.setEsbPingUrl(property[3]);
				serviceUrl.setServiceFound(true);
			} catch (Exception e) {
				serviceUrl.setServiceFound(false);
			}
		}

		logger.info("Exit : getEsbPingServiceDetails, serviceUrl->"
				+ serviceUrl);
		return serviceUrl;
	}

	public static boolean esbPingServiceStatus(String pingresponse) throws SAXException, IOException, ParserConfigurationException{
		boolean serviceStatus = false;
		
		try {
			String xmlRecords =pingresponse;
		    DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		    InputSource is = new InputSource();
		    is.setCharacterStream(new StringReader(xmlRecords));
	
		    Document doc = db.parse(is);
		    NodeList nodes = doc.getElementsByTagName("ns3:pingResp");
	    
		    
		    if ( null != nodes && nodes.getLength() == 0 ){
		    	nodes = doc.getElementsByTagName("ns3:PingResp");
		    }
		    if ( null != nodes && nodes.getLength() == 0 ){
		    	nodes = doc.getElementsByTagName("ns2:pingResp");
		    }
		    
		    if ( null != nodes && nodes.getLength() == 0 ){
		    	nodes = doc.getElementsByTagName("ns4:pingResp");
		    }
		    
		    if ( null != nodes && nodes.getLength() == 0 ){
		    	nodes = doc.getElementsByTagName("ns5:pingResp");
		    }
		    
		    if ( null != nodes && nodes.getLength() == 0 ){
		    	nodes = doc.getElementsByTagName("ns11:pingResp");
		    }
		    
		    if ( null != nodes && nodes.getLength() == 0 ){
		    	nodes = doc.getElementsByTagName("PingResp");
		    }
		    
		    if ( null != nodes && nodes.getLength() == 0 ){
		    	nodes = doc.getElementsByTagName("pingResp");
		    }
		    
		    if ( null != nodes && nodes.getLength() == 0 ){
		    	nodes = doc.getElementsByTagName("pingVendorResponse");
		    }
		    
		    if ( null != nodes && nodes.getLength() == 0 ){
		    	//nodes = doc.getElementsByTagName("dp-ping-response");

                try {
    		    	NodeList firstNameList = doc.getElementsByTagName("dp-ping-response");
                    Element firstNameElement = (Element)firstNameList.item(0);

                    NodeList textFNList = firstNameElement.getChildNodes();
	                String pingResponse = ((Node)textFNList.item(0)).getNodeValue().trim();
	                logger.info("pingResponse "+pingResponse);
	                if (pingResponse.equals("SUCCESS")){
	                	serviceStatus = true;
	                }
                }catch(Exception e){
                	e.printStackTrace();
                }
		    }
		    
		    for (int i = 0; i < nodes.getLength(); i++) {
		    	logger.info("Inside for loop: ");
				Element element = (Element) nodes.item(i);
				
				NodeList name = element.getElementsByTagName(String.format("ns3:HealthCheck"));		   
				
				if ( null != name && name.getLength() == 0 ){
					name = element.getElementsByTagName(String.format("ns2:HealthCheck"));
				}
				
				if ( null != name && name.getLength() == 0 ){
					name = element.getElementsByTagName(String.format("ns4:HealthCheck"));
				}
				
				if ( null != name && name.getLength() == 0 ){
					name = element.getElementsByTagName(String.format("ns5:HealthCheck"));
				}
				
				if ( null != name && name.getLength() == 0 ){
					name = element.getElementsByTagName(String.format("ns11:HealthCheck"));
				}
				
				if ( null != name && name.getLength() == 0 ){
					name = element.getElementsByTagName(String.format("HealthCheck"));
				}
				
				String healthcheck = "";
				
				if ( null != name && null != name.item(0)){
					healthcheck =  name.item(0).getTextContent();
				}
				 
				if ( null !=  healthcheck && healthcheck.equalsIgnoreCase("SUCCESS")) {
					serviceStatus = true;
				}
		    }
		}catch ( Exception e){
			logger.error("Exception in esbPingIsServiceStatusSuccess, while checking the response status:"+e);
			e.printStackTrace();
			serviceStatus = false;
		}
		return serviceStatus;
	}
	
	public static EmailUtil configureEmailForESBPingTesting(WSPingSchedule wSPingSchedule,
															List<ServiceUrl> serviceUrlList, String scheduleType, String[] emailTriggerOption, MessageSource configProperties ) {
		
		boolean sendEmail = true;
		
		String smtpHost = configProperties.getMessage("mint.webservice.smtphost", null, Locale.getDefault());
		String emailFrom = configProperties.getMessage("mint.webservice.emailfrom", null, Locale.getDefault());
		EmailUtil emailUtil = new EmailUtil(smtpHost,emailFrom);
		for(String triggerOption : emailTriggerOption) {
		if ( triggerOption.equals("failure")){
			if ( ! areAnyServiceFailed(serviceUrlList) ){
				sendEmail = false;
			}
		}
		
		if ( sendEmail ){
			boolean emailOnlyOnFailure = emailTriggerOption.equals("failure");
			emailUtil.setRecepientList(emailUtil.getRecepientsList(wSPingSchedule.getEmailIds()));
			emailUtil.setSubject(getSubjectForESBPingTesting(wSPingSchedule.getEnvironment(), triggerOption, configProperties));
			//emailUtil.setEmailContent(getEmailContentForESBPingTest(serviceUrlList, emailOnlyOnFailure));
		}
		}
		return emailUtil;
	}
	
	private static boolean areAnyServiceFailed(List<ServiceUrl> serviceUrlList) {

		for ( ServiceUrl serviceUrl : serviceUrlList){
			if ( ! serviceUrl.isStatus() ){
				return true;
			}
		}
		return false;
	}
	
	public static String getEmailContentForESBPingTest(List<ServiceUrl> serviceUrlList) {
		StringBuffer emailContent = new StringBuffer();
		String serviceExecutionStatus = "";
		
		emailContent.append("<table style=\"table-layout:fixed;\">");
		int count = 1;
		emailContent.append("<tr>");
		emailContent.append("<td style='font-weight:bold; background-color:#F0F5E6;'><font face=\"verdana,arial\" >S.No</font></td>");
		emailContent.append("<td width=\"150\" style='font-weight:bold; background-color:#F0F5E6;'><font face=\"verdana,arial\" >Service Name</font></td>");
		emailContent.append("<td style='font-weight:bold; background-color:#F0F5E6;'><font face=\"verdana,arial\" >Ping Status</font></td>");
		emailContent.append("<td style='font-weight:bold; background-color:#F0F5E6;'><font face=\"verdana,arial\" >Ping Response(Failed)</font></td>");
		
		emailContent.append("</tr>");
		for ( ServiceUrl serviceUrl : serviceUrlList){
			if ( serviceUrl.isStatus() ){
			
				serviceExecutionStatus = "SUCCESS";

			} else if ( ! serviceUrl.isServiceFound() ){
				serviceExecutionStatus = "SERVICE Not Found";
			} else {
				serviceExecutionStatus = "FAILED";
			}
			
			
				emailContent.append("<tr>");
				emailContent.append("<td>"+count+"</td>");
				count++;
				
				emailContent.append("<td>"+serviceUrl.getServiceName()+"</td>");
				
				emailContent.append("<td>");
				emailContent.append(serviceExecutionStatus);
				
				emailContent.append("</td>");
				emailContent.append("<td>");

				if ( null != serviceUrl.getHtmlSource() && serviceUrl.getHtmlSource().length() > 0 ) {
					if ( ! serviceUrl.isStatus() ){
					emailContent.append(serviceUrl.getHtmlSource());
					}
					
				}
				emailContent.append("</td></tr>");
			}
		String messageContent = emailContent.toString();
		messageContent = messageContent.replace(Constants.SCHEDULER.REPORT_SUMMARY_PLACE_HOLDER, messageContent);
		return messageContent;
	}
	
	
	public static String getSubjectForESBPingTesting(String environment, String emailTriggerOption, MessageSource configProperties) {
		String subject = "";
		
		if ( emailTriggerOption.equals("failure")){
			subject = configProperties.getMessage("mint.webservice.onDemandFailure", null, Locale.getDefault());
		}else {
			subject = configProperties.getMessage("mint.webservice.onDemandCompletion", null, Locale.getDefault());
		}
	
	return environment +": "+ subject;
	}
	
	
	public static List<ServiceStatus> removeEscapeCharFromXML(
			List<ServiceStatus> serviceStatusList) {
		for (ServiceStatus serviceStatus : serviceStatusList) {
			String htmlSource = serviceStatus.getHtmlSource();
			if (null != htmlSource && htmlSource.length() > 0) {
				htmlSource = StringEscapeUtils.escapeXml(htmlSource);
				serviceStatus.setHtmlSource(htmlSource);
			}
		}

		return serviceStatusList;
	}

	public static List<ServiceStatus> getPingResults(
			List<WSPingResults> listWSPingResults, MessageSource messageSource , MessageSource configProperties) {
		WSPingResults wsPingResults = new WSPingResults();
		if (null != listWSPingResults && listWSPingResults.size() > 0) {
			wsPingResults = listWSPingResults.get(0);
		}

		String tempDir = messageSource.getMessage(
				"mint.testcase.tempDirectory", null, Locale.getDefault());
		tempDir = FileDirectoryUtil.getAbsolutePath(tempDir,
				FileDirectoryUtil.getMintRootDirectory(configProperties));

		String tempXMLFile = tempDir + File.separator + "results"
				+ DateTimeUtil.getCurrentDateTimeyyyy_MM_dd_HH_mm_ss() + ".xml";
		try {
			String resultXml="";
			Clob clob = (Clob)wsPingResults.getResultsXml();
			if (clob != null) {
				resultXml = clob.getSubString(1, (int) clob.length());
			}
			FileUtils.writeStringToFile(new File(tempXMLFile),
					resultXml);
			
		} catch (Exception e) {
		}

		PojoXml pojoXml = PojoXmlFactory.createPojoXml();
		pojoXml.addCollectionClass("ServiceStatus", ServiceStatus.class);
		ServiceDetails serviceDetails = (ServiceDetails) pojoXml.getPojo(
				tempXMLFile, ServiceDetails.class);
		List<ServiceStatus> serviceStatusList = new ArrayList<ServiceStatus>();

		if (null != serviceDetails && null != serviceDetails.getService()
				&& serviceDetails.getService().size() > 0) {
			serviceStatusList = serviceDetails.getService();
		}
		return serviceStatusList;
	}


	public static EmailUtil configureEmailForWebserviceTesting(
			String testsuiteName, boolean runStatus,
			String emailAddresses, 
			MessageSource configProperties, String messageContent) {
		String smtpHost = configProperties.getMessage("mint.webservice.smtphost", null, Locale.getDefault());
		String emailFrom = configProperties.getMessage("mint.webservice.emailfrom", null, Locale.getDefault());
		EmailUtil emailUtil = new EmailUtil(smtpHost,emailFrom);
		emailUtil.setRecepientList(emailUtil.getRecepientsList(emailAddresses));
		/*emailUtil.setSubject(CommonUtils.getPropertyFromPropertyFile(configProperties,
				WebserviceTesting.WEBSERVICE_SUIT_COMPLETE_NOTIFICATION_MSG)+ runStatus);*/
		emailUtil.setEmailContent(messageContent);
		return emailUtil;
	}
	
	public static void main(String[] args) throws Exception{
		String time1 = "12:07:00";
		String time2 = "12:01:00";
 
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		Date date1 = format.parse(time1);
		Date date2 = format.parse(time2);
		long difference = date2.getTime() - date1.getTime();
		System.out.println(difference/1000);
		
		}
		
	

}
