package com.ensense.insense.data.webservice.dao.impl;

import com.ensense.insense.data.common.entity.Users;
import com.ensense.insense.data.common.utils.DateTimeUtil;
import com.ensense.insense.data.webservice.dao.WebserviceTestingDAO;
import com.ensense.insense.data.webservice.entity.*;
import com.ensense.insense.data.webservice.model.WSReportsData;
import com.ensense.insense.data.webservice.model.WSSuiteDetails;
import com.ensense.insense.data.webservice.model.WebserviceSetupForm;
import com.ensense.insense.data.webservice.model.WsDataset;
import com.eviware.soapui.support.types.StringToStringMap;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.sql.Clob;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;


@Service
public class WebserviceTestingDAOImpl implements WebserviceTestingDAO {

	private static Logger logger = Logger
			.getLogger(WebserviceTestingDAOImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public List<Webservices> getAllWebservices() {
		logger.debug("Entry: getAllWebservices");
		List<Webservices> webservices = new ArrayList<Webservices>();
		Query query = null;
		try {
			query = sessionFactory.getCurrentSession().createQuery(
					"from Webservices");
			webservices = query.list();

		} catch (Exception e) {
			logger.error("Exception in getAllWebservices.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getAllWebservices");
		return webservices;
	}

	@Override
	public Webservices getServicefromServiceName(String serviceName) {
		logger.debug("Entry: getServicefromServiceName");
		Query query = null;
		Webservices webservice = new Webservices();
		List<Webservices> webserviceList = new ArrayList<Webservices>();
		try {

			query = sessionFactory.getCurrentSession().createQuery(
					"from Webservices where serviceName = :serviceName)");
			query.setParameter("serviceName", serviceName);
			webserviceList = query.list();
			if (webserviceList != null && webserviceList.size() > 0) {
				webservice = (Webservices) query.list().get(0);

			} else {
				webservice.setServiceId(0);
			}

		} catch (Exception e) {
			logger.error("Exception in getServicefromServiceName.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getServicefromServiceName");
		return webservice;
	}

	@Override
	public Boolean insertServiceNames(String serviceName, String serviceType,
			String filePath) {
		logger.debug("Entry: insertServiceNames");

		Boolean result = false;
		Query query = null;
		List<Webservices> webservicesList = new ArrayList<Webservices>();
		try {

			query = sessionFactory.getCurrentSession().createQuery(
					"from Webservices where serviceName = :serviceName)");

			query.setParameter("serviceName", serviceName);
			webservicesList = query.list();

			// Get time from Date string
			SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");// time
			DateFormat formatter = new SimpleDateFormat("HH:mm:ss");// time
			Date now = new Date();
			/*String strTime = sdfTime.format(now);
			Time timeValue = new Time(formatter.parse(strTime).getTime());*/

			if (null != webservicesList && webservicesList.size() > 0) {
				// Updating the services
				Webservices webservice = new Webservices();
				webservice = (Webservices) query.list().get(0);
				webservice.setServiceId(webservice.getServiceId());
				//webservice.setModifiedTime(timeValue);
				webservice.setModifiedDate(now);
				webservice.setServiceName(serviceName);
				webservice.setServiceType(serviceType);
				webservice.setServiceFilePath(filePath);
				sessionFactory.getCurrentSession().update(webservice);
				result = true;
			} else {
				// Inserting the new service
				Webservices webservice = new Webservices();
			//	webservice.setAddedTime(timeValue);
			// webservice.setModifiedTime(timeValue);
				webservice.setAddedDate(now);
				webservice.setModifiedDate(now);
				webservice.setServiceName(serviceName);
				webservice.setServiceType(serviceType);
				webservice.setServiceFilePath(filePath);
				sessionFactory.getCurrentSession().save(webservice);
				result = true;
			}

		} catch (Exception e) {
			logger.error("Exception in insertServiceNames.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: insertServiceNames");
		return result;
	}

	@Override
	public Boolean insertOperationNames(String operationName,
			Integer serviceId, String methodType, String contentType) {
		logger.debug("Entry: insertServiceNames");

		Boolean result = false;
		try {
			WebserviceOperations testServicesOperation = new WebserviceOperations();
			testServicesOperation.setServiceId(serviceId);
			testServicesOperation.setOperationName(operationName);
			testServicesOperation.setMethodType(methodType);
			testServicesOperation.setContentType(contentType);
			sessionFactory.getCurrentSession().save(testServicesOperation);
			sessionFactory.getCurrentSession().flush();
			result = true;
		} catch (Exception e) {
			logger.error("Exception in insertOperationNames.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: insertServiceNames");
		return result;
	}

	@Override
	public WebserviceOperations getOperationIdfromOperationName(
			Integer serviceId, String operationName, String methodType) {
		logger.debug("Entry: getOperationIdfromOperationName");

		Query query = null;
		List<WebserviceOperations> webserviceOperationsList = new ArrayList<WebserviceOperations>();
		WebserviceOperations webserviceOperation = new WebserviceOperations();
		try {

			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from WebserviceOperations where operationName = :operationName "
									+ "and serviceId=:serviceId and methodType=:methodType)");

			query.setParameter("serviceId", serviceId);
			query.setParameter("operationName", operationName);
			query.setParameter("methodType", methodType);

			webserviceOperationsList = query.list();

			if (webserviceOperationsList != null
					&& webserviceOperationsList.size() > 0) {

				webserviceOperation = (WebserviceOperations) query.list()
						.get(0);

			} else {
				if (methodType.isEmpty() || methodType == null) {
					query = sessionFactory.getCurrentSession().createQuery(
							"from WebserviceOperations where operationName = :operationName "
									+ "and serviceId=:serviceId)");
					query.setParameter("serviceId", serviceId);
					query.setParameter("operationName", operationName);
					webserviceOperationsList = query.list();

					if (webserviceOperationsList != null
							&& webserviceOperationsList.size() > 0) {

						webserviceOperation = (WebserviceOperations) query
								.list().get(0);

					}
				}

			}

		} catch (Exception e) {
			logger.error("Exception in getOperationIdfromOperationName.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getOperationIdfromOperationName");
		return webserviceOperation;
	}

	@Override
	public Boolean insertRequestParameterName(
			WsOperationParameter wsOperationParameter, String sampleXML) {
		logger.debug("Entry: insertRequestParameterName");

		boolean insertStatus = false;
		try {
			Clob sampleXMLClob = Hibernate.getLobCreator(
					sessionFactory.getCurrentSession()).createClob(sampleXML);
			wsOperationParameter.setSampleXML(sampleXMLClob);
			sessionFactory.getCurrentSession().save(wsOperationParameter);
			insertStatus = true;

		} catch (Exception e) {
			insertStatus = false;
			logger.error("Exception in insertRequestParameterName.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Ending: insertRequestParameterName");
		return insertStatus;
	}

	@Override
	public List<WebserviceOperations> getOperationsByService(
			Webservices webservice) {
		logger.debug("Entry: getOperationsByService");
		List<WebserviceOperations> webserviceOperations = new ArrayList<WebserviceOperations>();
		Query query = null;
		try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from WebserviceOperations where serviceId= :serviceId and environmentId is null ");
			query.setParameter("serviceId", webservice.getServiceId());
			webserviceOperations = query.list();

		} catch (Exception e) {
			logger.error("Exception in getOperationsByService.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getOperationsByService");
		return webserviceOperations;
	}

	@Override
	public Boolean insertEndpointDetails(WsEndpointDetails wsEndpointDetails) {
		logger.debug("Entry: insertEndpointDetails");
		Boolean insertStatus = false;
		Query query = null;
		try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from WsEndpointDetails where serviceId=:serviceId and environmentId=:environmentId");

			query.setParameter("serviceId", wsEndpointDetails.getServiceId());
			query.setParameter("environmentId",
					wsEndpointDetails.getEnvironmentId());

			if (query.list().size() == 0) {
				sessionFactory.getCurrentSession().save(wsEndpointDetails);
				insertStatus = true;
			} else {

				WsEndpointDetails wsEndPoint = (WsEndpointDetails) query.list()
						.get(0);
				wsEndPoint.setWsEndpointDetailsId(wsEndPoint
						.getWsEndpointDetailsId());
				wsEndPoint.setEndpoint(wsEndpointDetails.getEndpoint());
				wsEndPoint.setEnvironmentId(wsEndpointDetails
						.getEnvironmentId());
				wsEndPoint.setServiceId(wsEndpointDetails.getServiceId());
				sessionFactory.getCurrentSession().update(wsEndPoint);
				insertStatus = true;
				logger.info("Row updated");
			}

		} catch (Exception e) {
			logger.error("Exception in insertEndpointDetails.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: insertEndpointDetails");
		return insertStatus;
	}

	@Override
	public Integer insertOperationWithEnvId(int operationId, int serviceId,
			int environmentId, String methodType, String contentType) {
		logger.debug("Entry: insertOperationWithEnvId");
		Query query = null;
		Boolean insertStatus = false;
		Integer newOperationId = 0;
		try {
			// List<WebserviceOperations> webserviceOperationsList = new
			// ArrayList<WebserviceOperations>();
			WebserviceOperations webserviceOperations = new WebserviceOperations();
			webserviceOperations.setOperationId(operationId);
			String operationName = getOperationfromOperationId(
					webserviceOperations).getOperationName();
			if (methodType.isEmpty()) {
				methodType = null;
			}
			if (methodType != null) {
				query = sessionFactory
						.getCurrentSession()
						.createQuery(
								"from WebserviceOperations where operationName=:operationName "
										+ " and serviceId=:serviceId and environmentId=:environmentId and methodType=:methodType");
				query.setParameter("operationName", operationName);
				query.setParameter("serviceId", serviceId);
				query.setParameter("environmentId", environmentId);
				query.setParameter("methodType", methodType);

			} else {
				query = sessionFactory
						.getCurrentSession()
						.createQuery(
								"from WebserviceOperations where operationName=:operationName "
										+ " and serviceId=:serviceId and environmentId=:environmentId");
				query.setParameter("operationName", operationName);
				query.setParameter("serviceId", serviceId);
				query.setParameter("environmentId", environmentId);
				// query.setParameter("methodType", methodType);
			}
			if (query != null && query.list() != null
					&& query.list().size() > 0) {
				insertStatus = false;
				logger.info("Entry present");

			} else {
				WebserviceOperations webserviceOperationsNew = new WebserviceOperations();
				webserviceOperationsNew.setOperationName(operationName);
				webserviceOperationsNew.setServiceId(serviceId);
				webserviceOperationsNew.setEnvironmentId(environmentId);
				webserviceOperationsNew.setMethodType(methodType);
				webserviceOperationsNew.setContentType(contentType);
				newOperationId = (Integer) sessionFactory.getCurrentSession()
						.save(webserviceOperationsNew);

				logger.info("Old operation id :" + operationId);
				logger.info("new operation id :" + newOperationId);
				insertStatus = true;
				logger.info("Entry newly added");
				insertStatus = true;
			}
		} catch (Exception e) {
			logger.error("Exception in insertOperationWithEnvId.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: insertOperationWithEnvId");
		return newOperationId;
	}

	@Override
	public WebserviceOperations getOperationfromOperationId(
			WebserviceOperations webserviceOperations) {

		logger.debug("Entry: getOperationfromOperationId");
		WebserviceOperations operation = new WebserviceOperations();
		List<WebserviceOperations> webserviceOperationsList = new ArrayList<WebserviceOperations>();
		Query query = null;
		try {
			query = sessionFactory.getCurrentSession().createQuery(
					"from WebserviceOperations where "
							+ "operationId= :operationId ");
			query.setParameter("operationId",
					webserviceOperations.getOperationId());
			webserviceOperationsList = query.list();
			if (webserviceOperationsList != null
					&& webserviceOperationsList.size() > 0) {
				operation = (WebserviceOperations) query.list().get(0);
			}
		} catch (Exception e) {
			logger.error("Exception in getOperationfromOperationId.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}

		logger.debug("Exit: getOperationfromOperationId");
		return operation;
	}

	@Override
	public Integer getOperationIdFromNameAndEnvId(Integer operationId,
			int serviceId, int environmentId) {
		logger.debug("Entry: getOpearationIdFromNameAndEnvId");
		int reqdOprId = 0;
		Query query = null;
		try {
			WebserviceOperations webserviceOperations = new WebserviceOperations();
			webserviceOperations.setOperationId(operationId);
			String operationName = getOperationfromOperationId(
					webserviceOperations).getOperationName();

			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"select operationId from WebserviceOperations where operationName=:operationName "
									+ " and serviceId=:serviceId and environmentId=:environmentId");

			query.setParameter("operationName", operationName);
			query.setParameter("serviceId", serviceId);
			query.setParameter("environmentId", environmentId);
			List<Integer> opearationIdList = query.list();
			if (opearationIdList != null && opearationIdList.size() > 0) {
				reqdOprId = ((Integer) query.list().get(0));
				logger.info("reqdOprId: " + reqdOprId);
			}

		} catch (Exception e) {
			logger.error("Exception in getOperationfromOperationId.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getOpearationIdFromNameAndEnvId");
		return reqdOprId;
	}

	@Override
	public Boolean deleteOperations(Integer operationId, int environmentId) {
		logger.debug("Entry: deleteOperations");

		Boolean deleted = false;
		try {
			// Delete from WsOperationParameterValue
			Query query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"delete from WsOperationParameterValue where paramSetId.parameterId in "
									+ "(select parameterId from WsOperationParameter where operationId=:operationId)");
			query.setParameter("operationId", operationId);
			int result1 = query.executeUpdate();
			sessionFactory.getCurrentSession().flush();
			sessionFactory.getCurrentSession().clear();

			// Delete from WsOperationParameter
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"delete from WsOperationParameter where operationId=:operationId ");
			query.setParameter("operationId", operationId);
			int result2 = query.executeUpdate();

			sessionFactory.getCurrentSession().flush();
			sessionFactory.getCurrentSession().clear();
			// Delete from WebserviceOperations
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"delete from WebserviceOperations where operationId=:operationId ");
			query.setParameter("operationId", operationId);
			int result3 = query.executeUpdate();

			// sessionFactory.getCurrentSession().flush();
			// sessionFactory.getCurrentSession().clear();
			sessionFactory.getCurrentSession().flush();
			sessionFactory.getCurrentSession().clear();
			deleted = true;

		} catch (Exception e) {
			logger.error("Exception in deleteOperations.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: deleteOperations");
		return deleted;
	}

	@Override
	public Webservices getServicefromServiceId(int serviceId) {
		logger.debug("Entry: getServicefromServiceId");

		Query query = null;
		List<Webservices> webservicesList = new ArrayList<Webservices>();
		Webservices webservice = new Webservices();
		try {
			query = sessionFactory.getCurrentSession().createQuery(
					"from Webservices where serviceId= :serviceId ");

			query.setParameter("serviceId", serviceId);
			webservicesList = query.list();

			if (webservicesList != null && webservicesList.size() > 0) {
				webservice = (Webservices) query.list().get(0);
			}

		} catch (Exception e) {
			logger.error("Exception in getServicefromServiceId.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getServicefromServiceId");
		return webservice;
	}

	@Override
	public WsEndpointDetails getEndPoint(int environmentId, String serviceName) {
		logger.debug("Entry: getEndPoint");

		Query query = null;
		List<WsEndpointDetails> wsEndpointDetailsList = new ArrayList<WsEndpointDetails>();
		WsEndpointDetails wsEndpointDetails = new WsEndpointDetails();
		try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from WsEndpointDetails where environmentId=:environmentId "
									+ " and serviceId = (select serviceId from Webservices where serviceName=:serviceName )");
			query.setParameter("environmentId", environmentId);
			query.setParameter("serviceName", serviceName);

			wsEndpointDetailsList = query.list();
			if (wsEndpointDetailsList != null
					&& wsEndpointDetailsList.size() > 0) {
				wsEndpointDetails = (WsEndpointDetails) query.list().get(0);
			}

		} catch (Exception e) {
			logger.error("Exception in getEndPoint.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getEndPoint");
		return wsEndpointDetails;
	}

	@Override
	public List<WebserviceOperations> getOperationsFromServiceEnv(
			int serviceId, int environmentId) {
		logger.debug("Entry: getOperationsFromServiceEnv");
		Query query = null;
		List<WebserviceOperations> webserviceOperationsList = new ArrayList<WebserviceOperations>();
		try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from WebserviceOperations where "
									+ " serviceId=:serviceId and environmentId=:environmentId");
			query.setParameter("serviceId", serviceId);
			query.setParameter("environmentId", environmentId);
			webserviceOperationsList = query.list();
		} catch (Exception e) {
			logger.error("Exception in getEndPoint.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getOperationsFromServiceEnv");
		return webserviceOperationsList;

	}

	@Override
	public int getOperationIdOfNullEnv(int serviceId, int operationId) {
		logger.debug("Entry: getOperationIdOfNullEnv");

		Query query = null;
		int reqdOprId = 0;
		try {
			WebserviceOperations webserviceOperation = new WebserviceOperations();
			webserviceOperation.setOperationId(operationId);
			webserviceOperation = getOperationfromOperationId(webserviceOperation);
			String operationName = webserviceOperation.getOperationName();
			String methodType = webserviceOperation.getMethodType();
			if (methodType != null || methodType == "") {
				query = sessionFactory
						.getCurrentSession()
						.createQuery(
								"select operationId from WebserviceOperations "
										+ "where serviceId =:serviceId and operationName=:operationName "
										+ " and methodType =:methodType and environmentId is null");
				query.setParameter("serviceId", serviceId);
				query.setParameter("operationName", operationName);
				query.setParameter("methodType", methodType);
			} else {
				query = sessionFactory
						.getCurrentSession()
						.createQuery(
								"select operationId from WebserviceOperations "
										+ "where serviceId =:serviceId and operationName=:operationName "
										+ " and environmentId is null");
				query.setParameter("serviceId", serviceId);
				query.setParameter("operationName", operationName);
			}
			List<Integer> operationIdList = query.list();
			if (operationIdList != null && operationIdList.size() > 0) {
				reqdOprId = operationIdList.get(0);
				logger.info("reqdOprId: " + reqdOprId);
			} else {
				reqdOprId = 0;
				logger.info("reqdOprId is 0 ");
			}

		} catch (Exception e) {
			logger.error("Exception in getOperationIdOfNullEnv.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getOperationIdOfNullEnv");
		return reqdOprId;

	}

	@Override
	public Map<String, Map<String, String>> getParameterSets(int operationId,
			int environmentId) {
		logger.debug("Entry: getParameterSets");

		Query query = null;
		Map<String, Map<String, String>> parameterSetMap = new LinkedHashMap<String, Map<String, String>>();
		try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"select paramvalue.paramSetId.parameterSetId, paramvalue.datasetName, paramname.parameterName, paramvalue.parameterValue "
									+ "from WsOperationParameterValue paramvalue, WsOperationParameter paramname "
									+ "where paramvalue.paramSetId.parameterId in (select paramname.parameterId from "
									+ "WsOperationParameter paramname where operationId=:operationId) and "
									+ "paramvalue.paramSetId.parameterId= paramname.parameterId "
									+ "and paramvalue.environmentId=:environmentId order by paramvalue.datasetName");
			query.setParameter("operationId", operationId);
			query.setParameter("environmentId", environmentId);
			List list = query.list();
			Map<String, String> requestValuesMap = null;
			if (list != null && list.size() > 0) {

				for (Object obj : list) {
					Object[] value = (Object[]) obj;
					 requestValuesMap = new LinkedHashMap<String, String>();
					Integer parameterSetId = (Integer) value[0];
					String datasetName = (String) value[1];
					String paramName = (String) value[2];
					String paramValue = "";

					Clob clob = (Clob) value[3];

					if (clob != null) {
						paramValue = clob.getSubString(1, (int) clob.length());
					}
					if (paramName.equalsIgnoreCase("ReqXml")) {
						paramValue = StringEscapeUtils.escapeXml(paramValue);
					}

					if (parameterSetMap.get(parameterSetId) == null) {
						requestValuesMap.put(paramName, paramValue);
					} else {
						requestValuesMap = parameterSetMap.get(parameterSetId);
						requestValuesMap.put(paramName, paramValue);
					}
					parameterSetMap.put(parameterSetId + ":" + datasetName,
							requestValuesMap);
				}

			}
		} catch (Exception e) {
			logger.error("Exception in getOperationIdOfNullEnv.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getParameterSets");
		return parameterSetMap;
	}

	@Override
	public List<WsDataset> getTestSoapParameterSets(int reqdOprId,
													int environmentId) {
		logger.debug("Entry: getTestSoapParameterSets");

		Query query = null;
		Map<String, Map<String, String>> parameterSetMap = new LinkedHashMap<String, Map<String, String>>();
		List<WsOperationXmlParameter> wsOperationXmlParameterlist = new ArrayList<WsOperationXmlParameter>();
		List<WsDataset> wsDatasets = new ArrayList<WsDataset>();
		WsDataset wsDataset = null;
		try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from WsOperationXmlParameter wsOperationXmlParameter "
									+ "where wsOperationXmlParameter.operationId =:operationId and "
									+ "wsOperationXmlParameter.environmentId =:environmentId order by wsOperationXmlParameter.datasetName");

			query.setParameter("operationId", reqdOprId);
			query.setParameter("environmentId", environmentId);
			logger.info("query value in DAO - getTestParameterSets: " + query);
			wsOperationXmlParameterlist = query.list();
			if (wsOperationXmlParameterlist != null
					&& wsOperationXmlParameterlist.size() > 0) {

				for (WsOperationXmlParameter wsOperationXmlParameter : wsOperationXmlParameterlist) {
					Map<String, String> parameterSetMap1 = new LinkedHashMap<String, String>();
					parameterSetMap1.put(wsOperationXmlParameter
							.getParameterName(), StringEscapeUtils
							.escapeXml(wsOperationXmlParameter
									.getParameterValue()));

					List<WsOperationHeaderParameters> headers = getTestSoapParameterHeaders(wsOperationXmlParameter
							.getParameterId());
					for (WsOperationHeaderParameters headerParam : headers) {
						parameterSetMap1.put(headerParam.getParameterName(),
								StringEscapeUtils.escapeXml(headerParam
										.getParameterValue()));
					}
					wsDataset = new WsDataset();
					wsDataset.setDataSet(wsOperationXmlParameter
							.getParameterId()
							+ ":"
							+ wsOperationXmlParameter.getDatasetName());
					wsDataset.setParameterValuesMap(parameterSetMap1);
					// parameterSetMap.put(wsOperationXmlParameter.getParameterId()+":"
					// + wsOperationXmlParameter.getDatasetName(),
					// parameterSetMap1);
					wsDatasets.add(wsDataset);
				}
			}
		} catch (Exception e) {
			logger.error("Exception in getTestSoapParameterSets.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getTestSoapParameterSets");
		return wsDatasets;
	}

	@Override
	public List<WsOperationHeaderParameters> getTestSoapParameterHeaders(
			Integer parameterId) {
		logger.debug("Entry: getTestSoapParameterHeaders");
		Query query = null;
		List<WsOperationHeaderParameters> wsOperationHeaderParametersList = new ArrayList<WsOperationHeaderParameters>();
		try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from WsOperationHeaderParameters wsOperationHeaderParameters "
									+ "where wsOperationHeaderParameters.parameterId =:parameterId");
			query.setParameter("parameterId", parameterId);
			wsOperationHeaderParametersList = query.list();

		} catch (Exception e) {
			logger.error("Exception in getTestSoapParameterHeaders.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}

		logger.debug("Entry: getTestSoapParameterHeaders");
		return wsOperationHeaderParametersList;
	}

	@Override
	public List<WsEndpointDetails> getServiceByEnvId(int environmentId) {
		logger.debug("Entry: getTestSoapParameterHeaders");
		Query query = null;
		List<WsEndpointDetails> wsEndpointDetailsList = new ArrayList<WsEndpointDetails>();
		try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from WsEndpointDetails where environmentId=:environmentId");
			query.setParameter("environmentId", environmentId);
			wsEndpointDetailsList = query.list();

		} catch (Exception e) {
			logger.error("Exception in getTestSoapParameterHeaders.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Entry: getTestSoapParameterHeaders");
		return wsEndpointDetailsList;
	}

	@Override
	public List<WsOperationParameter> getRequestParamNames(
			WsOperationParameter wsOperationParameter) {
		logger.debug("Entry: getRequestParamNames");
		Query query = null;
		List<WsOperationParameter> wsOperationParameterList = new ArrayList<WsOperationParameter>();
		try {
			Webservices webservice = getServicefromServiceId(wsOperationParameter
					.getServiceId());
			if (webservice.getServiceType().equalsIgnoreCase("wadl")) {
				query = sessionFactory.getCurrentSession().createQuery(
						"from WsOperationParameter where operationId=:operationId"
								+ " and serviceId=:serviceId"
								+ " and paramType!='H' and paramType!='C'");// filtering
																			// default
																			// header
																			// and
																			// custom
																			// header
			} else {
				query = sessionFactory
						.getCurrentSession()
						.createQuery(
								"from WsOperationParameter where operationId=:operationId"
										+ " and serviceId=:serviceId and paramType=null");
			}
			query.setParameter("operationId",
					wsOperationParameter.getOperationId());
			query.setParameter("serviceId", wsOperationParameter.getServiceId());
			wsOperationParameterList = query.list();
		} catch (Exception e) {
			logger.error("Exception in getRequestParamNames.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Entry: getRequestParamNames");
		return wsOperationParameterList;
	}

	@Override
	public Integer getMaxTestParameterSetId() {
		logger.debug("Entry: getMaxTestParameterSetId");
		Query query = null;
		Integer maxId = 1;
		try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"select max(paramSetId.parameterSetId) from WsOperationParameterValue");
			List<Integer> paramIdList = query.list();

			if (paramIdList != null && paramIdList.size() > 0) {
				Integer maxIdfromTable = (Integer) paramIdList.get(0);
				if (maxIdfromTable != null) {
					maxId = maxIdfromTable + 1;
				}
			}
		} catch (Exception e) {
			logger.error("Exception in getRequestParamNames.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Entry: getMaxTestParameterSetId");
		return maxId;
	}

	@Override
	public boolean addTestOperationData(
			WsOperationParameter wsOperationParameter,
			Map<String, String> requestValuesMap,
			WsParameterAndSetId wsParameterAndSetId, Integer environmentId,
			Integer operationId, String datasetName) throws DataIntegrityViolationException {
		logger.debug("Entry: addTestOperationData");

		boolean insertStatus = false;
		Query query = null;
		List<WsOperationParameter> wsOperationParameterList = new ArrayList<WsOperationParameter>();
	//	try {
			Iterator reqvaluesIterator = requestValuesMap.keySet().iterator();
			int i = 1;
			while (reqvaluesIterator.hasNext()) {
				String key = (String) reqvaluesIterator.next();
				query = sessionFactory
						.getCurrentSession()
						.createQuery(
								"from WsOperationParameter where parameterName=:parameterName "
										+ "and operationId=:operationId and serviceId=:serviceId");
				query.setParameter("operationId",
						wsOperationParameter.getOperationId());
				query.setParameter("serviceId",
						wsOperationParameter.getServiceId());
				query.setParameter("parameterName", key);
				wsOperationParameterList = query.list();
				WsOperationParameter wsOperationParam = new WsOperationParameter();
				if (wsOperationParameterList != null
						&& wsOperationParameterList.size() > 0) {
					wsOperationParam = (WsOperationParameter) query.list().get(
							0);
				}
				
				wsParameterAndSetId.setParameterId(wsOperationParam
						.getParameterId());
				Integer paramsetId = wsParameterAndSetId.getParameterId();
				
				
								
				List<String> obj = new ArrayList<String>();
				try {
					Query query1 = sessionFactory.getCurrentSession().createQuery("select pVal.datasetName from WsOperationParameter wsOperationParameter, " +
							"WsOperationParameterValue pVal where " +
								"pVal.datasetName =:datasetName and pVal.environmentId=:environmentId " +
								"and wsOperationParameter.serviceId =:serviceId and wsOperationParameter.operationId =:operationId and " +
								"pVal.paramSetId.parameterId = :parameterId");
					query1.setParameter("datasetName", datasetName);
					query1.setParameter("environmentId", environmentId);
					query1.setParameter("operationId", wsOperationParameter.getOperationId());
					query1.setParameter("serviceId", wsOperationParameter.getServiceId());
					query1.setParameter("parameterId", paramsetId);
					obj = query1.list();
				} catch (Exception e1) {
					logger.error("Exception in addTestOperationData.");
					logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e1));
				}
				if(obj.size()<=0) {
				
					try {
						Clob parameterValue = Hibernate.getLobCreator(
								sessionFactory.getCurrentSession()).createClob(
								requestValuesMap.get(key));
						
						
						WsOperationParameterValue wsOperationParameterValue = new WsOperationParameterValue();
						wsOperationParameterValue.setDatasetName(datasetName);
						wsOperationParameterValue.setParamSetId(wsParameterAndSetId);
						wsOperationParameterValue.setEnvironmentId(environmentId);
						wsOperationParameterValue.setParameterValue(parameterValue);
						//wsOperationParameterValue.setEnvironmentId(environmentId);
						sessionFactory.getCurrentSession().save(
								wsOperationParameterValue);
						sessionFactory.getCurrentSession().flush();
						sessionFactory.getCurrentSession().clear();
						insertStatus = true;
						i = i + 1;
					} catch (Exception e) {
						logger.error("Exception in addTestOperationData.");
						logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
					}
				} else {
					throw new DataIntegrityViolationException("Duplicate Dataset Name");
				}
					
			}

		/*} catch (Exception e) {
			logger.error("Exception in addTestOperationData.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}*/
		logger.debug("Exit: addTestOperationData");
		return insertStatus;
	}

	@Override
	public WsOperationXmlParameter submitParameters(String requestXML,
			Integer environmentId, int operationId, Integer serviceId,
			String datasetName) {
		logger.debug("Entry: submitParameters");
		boolean insertStatus = false;
		WsOperationXmlParameter wsOperationXmlParameter = null;
		try {
			
						
			wsOperationXmlParameter = new WsOperationXmlParameter();
			wsOperationXmlParameter.setServiceId(serviceId);
			wsOperationXmlParameter.setOperationId(operationId);
			wsOperationXmlParameter.setEnvironmentId(environmentId);
			wsOperationXmlParameter.setParameterName("requestXML");
			wsOperationXmlParameter.setParameterValue(requestXML);
			wsOperationXmlParameter.setDatasetName(datasetName);
			Integer id = (Integer) sessionFactory.getCurrentSession().save(
					wsOperationXmlParameter);

			wsOperationXmlParameter = (WsOperationXmlParameter) sessionFactory
					.getCurrentSession().get(WsOperationXmlParameter.class, id);
		} catch (Exception e) {
			logger.error("Exception in submitParameters.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: submitParameters");
		return wsOperationXmlParameter;
	}

	@Override
	public Boolean insertRequestParameterHeaders(
			WsOperationHeaderParameters wsOperationHeaderParameters) {
		logger.debug("Entry: insertRequestParameterHeaders");
		Boolean insertStatus = false;
		try {
			sessionFactory.getCurrentSession()
					.save(wsOperationHeaderParameters);
			insertStatus = true;

		} catch (Exception e) {
			insertStatus = false;
			logger.error("Exception in insertRequestParameterHeaders.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: insertRequestParameterHeaders");
		return insertStatus;
	}

	@Override
	public List<WsOperationParameter> getRequestParamNamesForDataset(
			WsOperationParameter wsOperationParameter) {
		logger.debug("Entry: getRequestParamNamesForDataset");
		Query query = null;
		List<WsOperationParameter> wsOperationParameterList = new ArrayList<WsOperationParameter>();
		try {
			Webservices webservice = getServicefromServiceId(wsOperationParameter
					.getServiceId());
			if (webservice.getServiceType().equalsIgnoreCase("wadl")) {
				query = sessionFactory.getCurrentSession().createQuery(
						"from WsOperationParameter where operationId=:operationId"
								+ " and serviceId=:serviceId"
								+ " and paramType!='H'");// filtering default
															// header and custom
															// header
			} else {
				query = sessionFactory.getCurrentSession().createQuery(
						"from WsOperationParameter where operationId=:operationId"
								+ " and serviceId=:serviceId");
			}
			query.setParameter("operationId",
					wsOperationParameter.getOperationId());
			query.setParameter("serviceId", wsOperationParameter.getServiceId());
			wsOperationParameterList = query.list();
		} catch (Exception e) {
			logger.error("Exception in getRequestParamNamesForDataset.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getRequestParamNamesForDataset");
		return wsOperationParameterList;
	}

	@Override
	public WsOperationParameterValue getRequestParameterValue(
			WsParameterAndSetId wsParameterAndSetId) {
		logger.debug("Entry: getRequestParameterValue");
		Query query = null;
		WsOperationParameterValue wsOperationParameterValue = new WsOperationParameterValue();
		List<WsOperationParameterValue> wsOperationParameterValueList = new ArrayList<WsOperationParameterValue>();
		try {

			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from WsOperationParameterValue "
									+ "where paramSetId.parameterSetId =:paramSetId and "
									+ "paramSetId.parameterId =:paramId");

			query.setParameter("paramSetId",
					wsParameterAndSetId.getParameterSetId());
			query.setParameter("paramId", wsParameterAndSetId.getParameterId());
			wsOperationParameterValueList = query.list();
			if (wsOperationParameterValueList.size() > 0) {
				wsOperationParameterValue = wsOperationParameterValueList
						.get(0);
			}

		} catch (Exception e) {
			logger.error("Exception in getRequestParameterValue.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getRequestParameterValue");
		return wsOperationParameterValue;
	}

	@Override
	public List<WsOperationParameter> getRequestHeaders(
			WsOperationParameter wsOperationParameter) {
		logger.debug("Entry: getRequestHeaders");

		Query query = null;
		List<WsOperationParameter> wsOperationParameterList = new ArrayList<WsOperationParameter>();
		try {
			query = sessionFactory.getCurrentSession().createQuery(
					"from WsOperationParameter where operationId=:operationId"
							+ " and serviceId=:serviceId"
							+ " and paramType=:parameterType");
			query.setParameter("operationId",
					wsOperationParameter.getOperationId());
			query.setParameter("serviceId", wsOperationParameter.getServiceId());
			query.setParameter("parameterType", "H");

			wsOperationParameterList = query.list();
		} catch (Exception e) {
			logger.error("Exception in getRequestHeaders.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getRequestHeaders");
		return wsOperationParameterList;
	}

	@Override
	public WsOperationXmlParameter getTestSoapParameters(int paramSetId) {
		logger.debug("Entry: getTestSoapParameters");
		Query query = null;
		WsOperationXmlParameter wsOperationXmlParameter = new WsOperationXmlParameter();
		try {

			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from WsOperationXmlParameter wsOperationXmlParameter "
									+ "where wsOperationXmlParameter.parameterId =:parameterId");
			query.setParameter("parameterId", paramSetId);
			List<WsOperationXmlParameter> list = query.list();
			if (list.size() > 0) {
				wsOperationXmlParameter = list.get(0);
			}

		} catch (Exception e) {
			logger.error("Exception in getTestSoapParameters.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getTestSoapParameters");
		return wsOperationXmlParameter;
	}

	@Override
	public List<Integer> getTestParamSetIDsforOperation(int operationId) {
		logger.debug("Entry: getTestParamSetIDsforOperation");
		List<Integer> testOperationParameterValueList = new ArrayList<Integer>();
		Query query = null;
		try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"select distinct paramvalue.paramSetId.parameterSetId "
									+ "from WsOperationParameterValue paramvalue "
									+ "where paramvalue.paramSetId.parameterId in (select paramname.parameterId from "
									+ "WsOperationXmlParameter paramname where operationId=:operationId)");
			query.setParameter("operationId", operationId);
			testOperationParameterValueList = query.list();

		} catch (Exception e) {
			logger.error("Exception in getTestParamSetIDsforOperation.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getTestParamSetIDsforOperation");
		return testOperationParameterValueList;
	}

	@Override
	public Boolean insertTestWSSuite(WebserviceSuite webserviceSuite,
			List<WebserviceSetupForm> listWebserviceSetupForm) {
		logger.debug("Entry: insertTestWSSuite");

		Boolean wsSuiteServiceResult = false;
		Boolean wsParameterResult = false;
		Boolean wsSuiteResult = false;
		Boolean insertStatus = false;
		Query query = null;
		try {
			SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");// time
			DateFormat formatter = new SimpleDateFormat("HH:mm:ss");// time
			Date now = new Date();
			String strTime = sdfTime.format(now);
			Time timeValue = new Time(formatter.parse(strTime).getTime());
			int suitId = 0;
			if(webserviceSuite.getWebserviceSuiteId() > 0){
				suitId = webserviceSuite.getWebserviceSuiteId();
				sessionFactory.getCurrentSession().update(webserviceSuite);
			} else {
				
			webserviceSuite.setCreatedDate(now);
			webserviceSuite.setCreatedTime(timeValue);
			sessionFactory.getCurrentSession().save(webserviceSuite);
			
			}
			wsSuiteResult = true;
			Integer webserviceSuiteId = 0;
			if(suitId > 0){
				query = sessionFactory.getCurrentSession().createQuery("from WebserviceSuiteService where webserviceSuiteId=:webserviceSuiteId");
				query.setParameter("webserviceSuiteId", suitId);
				List<WebserviceSuiteService> listWebserviceSuiteService = query.list();
				for(WebserviceSuiteService webserviceSuiteService: listWebserviceSuiteService) {
					this.deleteWSParameterSet(webserviceSuiteService.getWebserviceSuiteServiceId());
				}
				this.deleteWSSuiteService(suitId);
			}
			
			query = sessionFactory.getCurrentSession().createQuery(
					"select max(webserviceSuiteId) from WebserviceSuite ");
			List list = query.list();
			webserviceSuiteId = (Integer) list.get(0);

			for (WebserviceSetupForm webserviceSetupForm : listWebserviceSetupForm) {
				wsSuiteServiceResult = insertWSSuiteService(webserviceSuiteId,
						webserviceSetupForm.getServiceId(),
						webserviceSetupForm.getOperationId(),
						webserviceSetupForm.getReqInputType());
				Integer webserviceSuiteServiceId = getMaxsuiteServiceId();
				wsParameterResult = insertWSParameterSet(
						webserviceSuiteServiceId,
						Integer.parseInt(webserviceSetupForm.getParamSetId()));
				/*for (String paramSetId : webserviceSetupForm.getParams()) {
					wsParameterResult = insertWSParameterSet(
							webserviceSuiteServiceId,
							Integer.parseInt(paramSetId));
				}*/
			}
			
		
			if (wsSuiteResult && wsSuiteServiceResult && wsParameterResult) {
				insertStatus = true;
			}

		} catch (Exception e) {
			logger.error("Exception in insertTestWSSuite.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: insertTestWSSuite");
		return insertStatus;
	}

	public int deleteWSSuiteService(int webserviceSuiteId) {
		Query query = null;int result1=0;
		try {
			query = sessionFactory
			.getCurrentSession()
			.createQuery("delete from WebserviceSuiteService where webserviceSuiteId=:webserviceSuiteId ");
			query.setParameter("webserviceSuiteId", webserviceSuiteId);
			result1 = query.executeUpdate();
			sessionFactory.getCurrentSession().flush();
			sessionFactory.getCurrentSession().clear();
		} catch(Exception e){
			logger.error("Exception in deleteWSParameterSet.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		return result1;
		
	}

	public int deleteWSParameterSet(int webserviceSuiteServiceId) {
		Query query = null;int result1=0;
		try {
			query = sessionFactory
			.getCurrentSession()
			.createQuery("delete from WebserviceSuiteParamSetTable where webserviceSuiteServiceId=:webserviceSuiteServiceId ");
			query.setParameter("webserviceSuiteServiceId", webserviceSuiteServiceId);
			result1 = query.executeUpdate();
			
		} catch(Exception e){
			logger.error("Exception in deleteWSParameterSet.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		return result1;
	}

	public Boolean insertWSParameterSet(Integer webserviceSuiteServiceId,
			Integer parameterSetId) {
		logger.debug("Entry: insertWSParameterSet");
		Boolean insertStatus = false;
		try {

			WebserviceSuiteParamSetTable webserviceSuiteParamSetTable = new WebserviceSuiteParamSetTable();
			webserviceSuiteParamSetTable.setParameterSetId(parameterSetId);
			webserviceSuiteParamSetTable
					.setWebserviceSuiteServiceId(webserviceSuiteServiceId);

			sessionFactory.getCurrentSession().save(
					webserviceSuiteParamSetTable);
			sessionFactory.getCurrentSession().flush();
			sessionFactory.getCurrentSession().clear();
			insertStatus = true;
		} catch (Exception exp) {
			logger.error("Exception occured in insertTestWSParameterSet ", exp);
			exp.printStackTrace();
		}
		logger.debug("Exit: insertWSParameterSet");
		return insertStatus;
	}

	public Integer getMaxsuiteServiceId() {
		logger.debug("Entry: getMaxsuiteServiceId");
		Integer webserviceSuiteServiceId = 0;
		Query query = null;
		try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"select max(webserviceSuiteServiceId) from WebserviceSuiteService");
			List listObj = query.list();
			if(listObj != null) {
				webserviceSuiteServiceId = ((Integer) query.list().get(0))
				.intValue();
			} else {
				webserviceSuiteServiceId = 1;
			}
		} catch (Exception exp) {
			logger.error("Exception occured in insertTestWSSuiteService ", exp);
			exp.printStackTrace();
		}
		logger.debug("Exit: getMaxsuiteServiceId");
		return webserviceSuiteServiceId;
	}

	public Boolean insertWSSuiteService(Integer webserviceSuiteId,
			Integer serviceId, Integer operationId, String inputType) {
		logger.debug("Entry: insertWSSuiteService");

		Boolean insertStatus = false;
		try {
			WebserviceSuiteService webserviceSuiteService = new WebserviceSuiteService();
			webserviceSuiteService.setWebserviceSuiteId(webserviceSuiteId);
			webserviceSuiteService.setOperationId(operationId);
			webserviceSuiteService.setServiceId(serviceId);
			webserviceSuiteService.setInputType(inputType);
			sessionFactory.getCurrentSession().save(webserviceSuiteService);
			/*sessionFactory.getCurrentSession().flush();
			sessionFactory.getCurrentSession().clear();*/
			insertStatus = true;
		} catch (Exception e) {
			logger.error("Exception in insertWSSuiteService.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: insertWSSuiteService");
		return insertStatus;
	}

	@Override
	public Boolean deleteServiceDetails(Integer serviceId) {
		logger.debug("Entry: deleteServiceDetails");
		Boolean deleted = false;
		try {
			Query query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"delete from WebserviceOperations where serviceId=:serviceId ");
			query.setParameter("serviceId", serviceId);
			int result = query.executeUpdate();

			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"delete from WebserviceSuiteParamSetTable where parameterSetId in "
									+ "(select paramSetId.parameterSetId from WsOperationParameterValue where paramSetId.parameterId in "
									+ "(select parameterId from WsOperationXmlParameter where serviceId=:serviceId) "
									+ ")");
			query.setParameter("serviceId", serviceId);
			result = query.executeUpdate();

			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"delete from WsOperationParameterValue where paramSetId.parameterId in "
									+ "(select parameterId from WsOperationXmlParameter where serviceId=:serviceId)");
			query.setParameter("serviceId", serviceId);
			result = query.executeUpdate();

			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"delete from WsOperationXmlParameter where serviceId=:serviceId");
			query.setParameter("serviceId", serviceId);
			result = query.executeUpdate();

			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"delete from WsOperationParameter where serviceId=:serviceId");
			query.setParameter("serviceId", serviceId);
			result = query.executeUpdate();

			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"delete from WebserviceSuiteService where serviceId=:serviceId");
			query.setParameter("serviceId", serviceId);
			result = query.executeUpdate();

			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							" select webserviceSuiteId from WebserviceSuiteService where serviceId=:serviceId");
			query.setParameter("serviceId", serviceId);
			List<Integer> testWSSuiteIdsList = query.list();

			if (!testWSSuiteIdsList.isEmpty()) {
				for (Integer testWSSuiteId : testWSSuiteIdsList) {
					query = sessionFactory
							.getCurrentSession()
							.createQuery(
									"delete from WebserviceSuite where webserviceSuiteId=:testWSSuiteId");
					query.setParameter("testWSSuiteId", testWSSuiteId);
					result = query.executeUpdate();
				}
			}
			deleted = true;

		} catch (Exception e) {
			logger.error("Exception in deleteServiceDetails.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: deleteServiceDetails");
		return deleted;
	}

	@Override
	public List<WebservicesPingTest> getWebservicesPingTestList() {
		logger.debug("Entry: getWebservicesPingTestList");

		Query query = null;
		List<WebservicesPingTest> webservicesPingTestList = new ArrayList<WebservicesPingTest>();
		try {
			query = sessionFactory.getCurrentSession().createQuery(
					"from WebservicesPingTest");
			webservicesPingTestList = query.list();
		} catch (Exception e) {
			logger.error("Exception in getWebservicesPingTestList.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getWebservicesPingTestList");
		return webservicesPingTestList;
	}

	@Override
	public boolean deletePingServices() {
		logger.debug("Entry: deletePingServices");
		Boolean deleted = false;
		try {
			Query delserviceValues = sessionFactory.getCurrentSession()
					.createQuery("delete from WebservicesPingTest");
			delserviceValues.executeUpdate();
			sessionFactory.getCurrentSession().flush();
			sessionFactory.getCurrentSession().clear();
			deleted = true;
		} catch (Exception e) {
			logger.error("Exception in deletePingServices.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: deletePingServices");
		return deleted;
	}

	@Override
	public List<WebservicesPingTest> getDuplicateService(String serviceName) {
		logger.debug("Entry: getDuplicateService");
		Query query = null;
		List<WebservicesPingTest> dupServiceLists = new ArrayList<WebservicesPingTest>();
		try {
			query = sessionFactory.getCurrentSession().createQuery(
					"from WebservicesPingTest where serviceName=:serviceName");
			query.setParameter("serviceName", serviceName);
			dupServiceLists = query.list();
		} catch (Exception e) {
			logger.error("Exception in getDuplicateService.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getDuplicateService");
		return dupServiceLists;

	}

	@Override
	public boolean insertExcelServiceData(
			WebservicesPingTest webservicesPingTest) {
		logger.debug("Entry: insertExcelServiceData");
		boolean insertStatus = false;
		try {
			sessionFactory.getCurrentSession().save(webservicesPingTest);
			insertStatus = true;

		} catch (Exception e) {
			logger.error("Exception in insertExcelServiceData.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: insertExcelServiceData");
		return insertStatus;
	}

	@Override
	public int insertWSPingSchedules(WSPingSchedule wSPingSchedule) {
		logger.debug("Entry: insertWSPingSchedules");
		int insertId = 0;
		try {
			insertId = (Integer) sessionFactory.getCurrentSession().save(
					wSPingSchedule);
		} catch (Exception e) {
			logger.error("Exception in insertWSPingSchedules.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: insertWSPingSchedules");
		return insertId;
	}

	@Override
	public List<WSPingResults> getESBPingServiceDetails(String environmentId,
			String wsPingScheduleId) {
		logger.debug("Entry: getESBPingServiceDetails");
		Query query = null;
		List<WSPingResults> listWSPingResults = new ArrayList<WSPingResults>();
		try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from WSPingResults "
									+ "where environmentId=:environmentId and wsPingScheduleId=:wsPingScheduleId");

			query.setParameter("environmentId", environmentId);
			query.setParameter("wsPingScheduleId",
					Integer.parseInt(wsPingScheduleId));

			listWSPingResults = query.list();

		} catch (Exception e) {
			logger.error("Exception in getESBPingServiceDetails.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getESBPingServiceDetails");
		return listWSPingResults;

	}

	@Override
	public List<WSPingResults> getESBPingDates(String environment) {
		logger.debug("Entry: getESBPingDates");
		Query query = null;
		List<WSPingResults> wsPingResultsList = new ArrayList<WSPingResults>();
		try {
			query = sessionFactory.getCurrentSession().createQuery(
					"from WSPingResults where environmentId=:environmentId"
							+ " ORDER BY startDate DESC, startTime DESC");
			query.setParameter("environmentId", environment);
			wsPingResultsList = query.list();
		} catch (Exception e) {
			logger.error("Exception in getESBPingServiceDetails.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getESBPingDates");
		return wsPingResultsList;
	}

	@Override
	public List<WebserviceSuite> getWSTestSuites(int userid, int environmentId) {
		logger.debug("Entry: getWSTestSuites");
		Query query = null;
		List<WebserviceSuite> testWebserviceSuiteList = new ArrayList<WebserviceSuite>();
		try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from WebserviceSuite where environmentId=:environmentId and userId=:userId");
			query.setParameter("userId", userid);
			query.setParameter("environmentId", environmentId);
			testWebserviceSuiteList = query.list();
			
		} catch (Exception e) {
			logger.error("Exception in getWSTestSuites.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getWSTestSuites");
		return testWebserviceSuiteList;
	}
	
	@Override
	public List<WebserviceSuite> getAllWsTestSuites(int environmentId) {
		logger.debug("Entry: getAllWsTestSuites");
		Query query = null;
		List<WebserviceSuite> testWebserviceSuiteList = new ArrayList<WebserviceSuite>();
		try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from WebserviceSuite where environmentId=:environmentId and privateSuit =:privateSuit");
			query.setParameter("privateSuit", false);
			query.setParameter("environmentId", environmentId);
			testWebserviceSuiteList = query.list();
		} catch (Exception e) {
			logger.error("Exception in getWSTestSuites.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getWSTestSuites");
		return testWebserviceSuiteList;
	}
	
	@Override
	public List<WebserviceSuite> getWSTestSuitesCreatedByAll() {
		logger.debug("Entry: getWSTestSuites");
		Query query = null;
		List<WebserviceSuite> testWebserviceSuiteList = new ArrayList<WebserviceSuite>();
		try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from WebserviceSuite");
			testWebserviceSuiteList = query.list();
		} catch (Exception e) {
			logger.error("Exception in getWSTestSuites.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getWSTestSuites");
		return testWebserviceSuiteList;
	}

	@Override
	public boolean isWSSchedulePresent() {
		// logger.debug("Entry: isWSSchedulePresent");
		boolean result = false;
		try {
			Query query = sessionFactory
					.getCurrentSession()
					.createQuery(
							" select wsScheduleId from WSSchedule where wSScheduleId "
									+ "  in (select wsScheduleId from WSExecutionStatus where executionStatusRefId=1 )");
			if (query.list().size() > 0) {
				result = true;
			}

		} catch (Exception e) {
			logger.error("Exception in isWSSchedulePresent.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		// logger.debug("Exit: isWSSchedulePresent");
		return result;
	}
	
	@Override
	public List<WSSchedule> pendingSchedules() {
		// logger.debug("Entry: isWSSchedulePresent");
		boolean result = false;
		List<WSSchedule> wsSchedules =  null;
		try {
			Query query = sessionFactory
					.getCurrentSession()
					.createQuery("from WSSchedule where wSScheduleId "
									+ "  in (select wsScheduleId from WSExecutionStatus where executionStatusRefId=1 )");
			
			wsSchedules = query.list();
		} catch (Exception e) {
			logger.error("Exception in isWSSchedulePresent.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		// logger.debug("Exit: isWSSchedulePresent");
		return wsSchedules;
	}

	@Override
	public List<WSExecutionStatus> getWSExecutionStatusList() {
		logger.debug("Entry: getWSExecutionStatusList");
		List<WSExecutionStatus> wSExecutionStatusList = new ArrayList<WSExecutionStatus>();
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					"from WSExecutionStatus tes "
							+ "where tes.executionStatusRefId=1 ");
			wSExecutionStatusList = query.list();
		} catch (Exception e) {
			logger.error("Exception in getWSExecutionStatusList.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getWSExecutionStatusList");
		return wSExecutionStatusList;
	}
	
	@Override
	public List<WSExecutionStatus> getWSExecutionStatusListByScheduleId(int scheduleId) {
		logger.debug("Entry: getWSExecutionStatusListByScheduleId");
		List<WSExecutionStatus> wSExecutionStatusList = new ArrayList<WSExecutionStatus>();
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					"from WSExecutionStatus tes "
							+ "where tes.wsScheduleId=:scheduleId ");
			query.setParameter("scheduleId", scheduleId);
			wSExecutionStatusList = query.list();
		} catch (Exception e) {
			logger.error("Exception in getWSExecutionStatusListByScheduleId.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getWSExecutionStatusListByScheduleId");
		return wSExecutionStatusList;
	}

	@Override
	public WSSchedule getWSSchedule(int wSScheduleId) {
		logger.debug("Entry: getWSSchedule");
		WSSchedule wSTestSchedule = new WSSchedule();
		List<WSSchedule> wSScheduleList = new ArrayList<WSSchedule>();
		Query query = null;

		try {
			query = sessionFactory.getCurrentSession().createQuery(
					"from WSSchedule where wSScheduleId =:wSScheduleId");
			query.setParameter("wSScheduleId", wSScheduleId);
			wSScheduleList = query.list();
			if (wSScheduleList.size() > 0) {
				wSTestSchedule = (WSSchedule) wSScheduleList.get(0);
			}
		} catch (Exception e) {
			logger.error("Exception in getWSSchedule.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getWSSchedule");
		return wSTestSchedule;
	}

	@Override
	public Integer getEnvironment(Integer webserviceSuiteId) {
		logger.debug("Entry: getEnvironment");

		Query query = null;
		Integer environmentId = 0;
		try {
			query = sessionFactory.getCurrentSession().createQuery(
					"select t.environmentId from  WebserviceSuite t "
							+ "where t.webserviceSuiteId=:webserviceSuiteId ");
			query.setParameter("webserviceSuiteId", webserviceSuiteId);
			List<Integer> envIdList = query.list();
			if (envIdList != null && envIdList.size() > 0) {
				environmentId = (Integer) envIdList.get(0);
			}

		} catch (Exception e) {
			logger.error("Exception in getEnvironment.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getEnvironment");
		return environmentId;
	}

	@Override
	public WebserviceSuite getWebserviceSuite(Integer webserviceSuiteId) {
		logger.debug("Entry: getWebserviceSuite");
		Query query = null;
		WebserviceSuite webserviceSuite = new WebserviceSuite();
		List<WebserviceSuite> webserviceSuiteList = new ArrayList<WebserviceSuite>();
		try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from WebserviceSuite where webserviceSuiteId =:webserviceSuiteId");

			query.setParameter("webserviceSuiteId", webserviceSuiteId);
			webserviceSuiteList = query.list();
			if (webserviceSuiteList.size() > 0) {
				webserviceSuite = (WebserviceSuite) webserviceSuiteList.get(0);
			}
		} catch (Exception e) {
			logger.error("Exception in getEnvironment.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getWebserviceSuite");
		return webserviceSuite;
	}

	@Override
	public List<WebserviceSuiteService> getWebserviceSuiteService(
			Integer webserviceSuiteId) {
		logger.debug("Entry: getWebserviceSuiteService");

		Query query = null;
		List<WebserviceSuiteService> webserviceSuiteServiceList = new ArrayList<WebserviceSuiteService>();
		try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from WebserviceSuiteService webserviceSuiteService "
									+ "where webserviceSuiteService.webserviceSuiteId =:webserviceSuiteId");
			query.setParameter("webserviceSuiteId", webserviceSuiteId);

			logger.info("query value in DAO - getTestParameterSets: " + query);
			webserviceSuiteServiceList = query.list();
		} catch (Exception e) {
			logger.error("Exception in getWebserviceSuiteService.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getWebserviceSuiteService");
		return webserviceSuiteServiceList;
	}

	@Override
	public WsDataset getParameterValues(Integer environmentId,
			int parameterSetId, Integer operationId) {
		logger.debug("Entry: getParameterValues");

		Query query = null;
		Map<String, String> parameterValuesMap = new LinkedHashMap<String, String>();
	
		WsDataset wsDataset = null;
		try {

			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"select paramname.parameterName, paramvalue.parameterValue,paramname.paramType, paramvalue.datasetName "
									+ "from WsOperationParameter paramname, WsOperationParameterValue paramvalue "
									+ "where paramvalue.paramSetId.parameterId = paramname.parameterId  and "
									+ "paramvalue.paramSetId.parameterSetId = :parameterSetId and paramvalue.environmentId = :environmentId "
									+ "and paramname.operationId = :operationId ");
			query.setParameter("environmentId", environmentId);
			query.setParameter("parameterSetId", parameterSetId);
			query.setParameter("operationId", operationId);
			List list = query.list();
			if (list != null && list.size() > 0) {

				for (Object obj : list) {
					Object[] value = (Object[]) obj;
					String paramName = (String) value[0];
					String paramValue = "";
					Clob clob = (Clob) value[1];
					if (clob != null) {
						paramValue = clob.getSubString(1, (int) clob.length());
					}
					String paramType = (String) value[2];
					if (paramType != null
							&& paramType.toString().equalsIgnoreCase("C")) {
					} else {
						parameterValuesMap.put(paramName, paramValue);
					}
					wsDataset = new WsDataset();
					wsDataset.setDataSet((String) value[3]);
					wsDataset.setParameterValuesMap(parameterValuesMap);

				}
			}
		} catch (Exception e) {
			logger.error("Exception in getParameterValues.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getParameterValues");
		return wsDataset;
	}

	@Override
	public WsDataset getXmlParameterValues(Integer environmentId,
			int parameterSetId, Integer operationId) {
		logger.debug("Entry: getParameterValues");

		Query query = null;
		Map<String, String> parameterValuesMap = new LinkedHashMap<String, String>();
		List<WsDataset> listWsDataset = new ArrayList<WsDataset>();
		WsDataset wsDataset = null;
		try {

			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"select paramname.parameterName, paramname.parameterValue,paramname.paramType, paramname.datasetName "
									+ "from WsOperationXmlParameter paramname "
									+ "where paramname.parameterId =:parameterSetId and "
									+ " paramname.environmentId = :environmentId "
									+ "and paramname.operationId =:operationId ");
			query.setParameter("environmentId", environmentId);
			query.setParameter("parameterSetId", parameterSetId);
			query.setParameter("operationId", operationId);
			List list = query.list();
			if (list != null && list.size() > 0) {

				for (Object obj : list) {
					Object[] value = (Object[]) obj;
					String paramName = (String) value[0];
					String paramValue = "";
					/*
					 * Clob clob = (Clob) value[1]; if (clob != null) {
					 * paramValue = clob.getSubString(1, (int) clob.length()); }
					 */
					paramValue = (String) value[1];
					String paramType = (String) value[2];
					if (paramType != null
							&& paramType.toString().equalsIgnoreCase("C")) {
					} else {
						parameterValuesMap.put(paramName, paramValue);
					}
					wsDataset = new WsDataset();
					wsDataset.setDataSet((String) value[3]);
					wsDataset.setParameterValuesMap(parameterValuesMap);

				}
			}
		} catch (Exception e) {
			logger.error("Exception in getParameterValues.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getParameterValues");
		return wsDataset;
	}

	@Override
	public StringToStringMap getCustomHeadersValues(Integer environmentId,
													int parameterSetId, Integer operationId) {
		logger.debug("Entry: getCustomHeadersValues");

		Query query = null;
		StringToStringMap parameterValuesMap = new StringToStringMap();
		try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"select paramname.parameterName, paramvalue.parameterValue,paramname.paramType "
									+ "from WsOperationXmlParameter paramname, WsOperationParameterValue paramvalue "
									+ "where paramvalue.paramSetId.parameterId = paramname.parameterId and "
									+ "paramvalue.paramSetId.parameterSetId = :parameterSetId and paramvalue.environmentId = :environmentId "
									+ "and paramname.operationId = :operationId");
			query.setParameter("environmentId", environmentId);
			query.setParameter("parameterSetId", parameterSetId);
			query.setParameter("operationId", operationId);
			List list = query.list();
			if (list != null && list.size() > 0) {
				for (Object obj : list) {
					Object[] value = (Object[]) obj;
					String paramName = (String) value[0];
					String paramValue = "";
					Clob clob = (Clob) value[1];
					if (clob != null) {
						paramValue = clob.getSubString(1, (int) clob.length());
					}
					Object paramType = value[2];
					if (paramType != null
							&& paramType.toString().equalsIgnoreCase("C")) {
						parameterValuesMap.put(paramName, paramValue);
					}
				}
			}

		} catch (Exception e) {
			logger.error("Exception in getParameterValues.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getCustomHeadersValues");
		return parameterValuesMap;
	}

	@Override
	public boolean insertWSTestResults(WSResults wsResult) {
		logger.debug("Entry: insertWSTestResults");
		boolean result = false;
		try {
			sessionFactory.getCurrentSession().save(wsResult);
			result = true;
		} catch (Exception e) {
			logger.error("Exception in insertWSTestResults.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: insertWSTestResults");
		return result;
	}

	@Override
	public WSReports getWSReports(WSReports wsReport){
		logger.debug("Entry: getWSReports, wsReport->"+wsReport);
		WSReports existingWSReports = new WSReports();
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					"from WSReports where wsScheduleId =:wsScheduleId "
					+ "and webserviceSuiteId = :webserviceSuiteId and operationId = :operationId and parameterSetId = :parameterSetId");
			query.setParameter("wsScheduleId", wsReport.getWsScheduleId());
			//query.setParameter("wsBaselineScheduleId", wsReport.getWsBaselineScheduleId());
			query.setParameter("webserviceSuiteId", wsReport.getWebserviceSuiteId());
			query.setParameter("operationId", wsReport.getOperationId());
			query.setParameter("parameterSetId", wsReport.getParameterSetId());
			List<WSReports> wsReportList = query.list();
			
			if ( null != wsReportList && wsReportList.size() > 0 ){
				existingWSReports = wsReportList.get(0);
			}
		}catch(Exception e){
			logger.error("Exception in getWSReports");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getWSReports, existingWSReports->"+existingWSReports);
		return existingWSReports;
		
	}
	
	@Override
	public Boolean wsSaveComparisonResults(WSReports wsReport) {
		logger.debug("Entry: wsSaveComparisonResults");
		Boolean insertStatus = false;
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(wsReport);
			insertStatus = true;
		} catch (Exception e) {
			logger.error("Exception in getParameterValues.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: wsSaveComparisonResults");
		return insertStatus;
	}

	@Override
	public boolean isBaseLineFound(int wsScheduleId) {
		logger.debug("Entry: isBaseLineFound");
		boolean status = false;
		Query query = null;
		try {
			query = sessionFactory.getCurrentSession().createQuery(
					"from WSBaseline where wsScheduleId =:wsScheduleId");
			query.setParameter("wsScheduleId", wsScheduleId);
			if (query.list().size() > 0) {
				status = true;
			}
		} catch (Exception e) {
			logger.error("Exception in isBaseLineFound.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: isBaseLineFound");
		return status;
	}

	@Override
	public boolean setWSCompletion(WSExecutionStatus wstestsuite,
			boolean runStatus) {
		logger.debug("Entry: setWSCompletion");

		boolean result = false;

		int executionStatus = 3;// 3 - completed

		if (runStatus == false) {
			executionStatus = 5; // 5 - failed
		}
		try {
			Query updateStatus = sessionFactory
					.getCurrentSession()
					.createQuery(
							"Update WSExecutionStatus tes set executionStatusRefId = :executionStatus, "
									+ " executionEndTime = current_timestamp() "
									+ " where wsExecutionStatusId = :wsExecutionStatusId ");
			updateStatus.setParameter("executionStatus", executionStatus);
			updateStatus.setParameter("wsExecutionStatusId",
					wstestsuite.getWsExecutionStatusId());
			updateStatus.executeUpdate();
			result = true;
		} catch (Exception e) {
			logger.error("Exception in setWSCompletion.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: setWSCompletion");
		return result;
	}

	@Override
	public boolean updateWSSchedule(WSSchedule wsSchedule) {
		logger.debug("Entry: updateWSSchedule");
		boolean updateStatus = false;
		try {
			sessionFactory.getCurrentSession().update(wsSchedule);
			updateStatus = true;

		} catch (Exception e) {
			logger.error("Exception in updateWSSchedule.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: updateWSSchedule");
		return updateStatus;
	}

	@Override
	public int insertWSSchedule(WSSchedule wsSchedule) {
		logger.debug("Entry: insertWSSchedule");
		int insertId = 0;
		try {
			wsSchedule.setCompareFlag(false);
			insertId = (Integer) sessionFactory.getCurrentSession().save(
					wsSchedule);
		} catch (Exception e) {
			logger.error("Exception in updateWSSchedule.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: insertWSSchedule");
		return insertId;
	}

	@Override
	public List<WSPingSchedule> getPingSchedules() {
		// logger.debug("Entry: getPingSchedules");
		Query query = null;
		List<WSPingSchedule> listWSPingSchedule = null;
		try {

			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from WSPingSchedule wspts where wspts.reocurrence = true and wspts.startTime <= current_timestamp() and "
									+ "wspts.isScheduled = true and wspts.wsPingScheduleId not in ( select wsPingScheduleId from WSPingResults where "
									+ "startDate = :currentDate)");

			query.setDate("currentDate", new Date());
			listWSPingSchedule = query.list();

			if (null == listWSPingSchedule || listWSPingSchedule.size() < 1) {

				query = sessionFactory
						.getCurrentSession()
						.createQuery(
								"from WSPingSchedule wspts where wspts.reocurrence = false and wspts.startTime <= current_timestamp() and "
										+ "wspts.isScheduled = true and wspts.wsPingScheduleId not in ( select wsPingScheduleId from WSPingResults)");
				listWSPingSchedule = query.list();
			}
		} catch (Exception e) {
			logger.error("Exception in getPingSchedules.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		// logger.debug("Exit: getPingSchedules");
		return listWSPingSchedule;
	}

	@Override
	public boolean insertWSPingResults(WSPingResults wsPingResults,
			String resultXml) {
		logger.debug("Entry: getPingSchedules");
		boolean insertStatus = false;
		try {
			Clob resultXmlClob = Hibernate.getLobCreator(
					sessionFactory.getCurrentSession()).createClob(resultXml);
			wsPingResults.setResultsXml(resultXmlClob);
			sessionFactory.getCurrentSession().save(wsPingResults);
			insertStatus = true;
		} catch (Exception e) {
			logger.error("Exception in insertWSPingResults.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getPingSchedules");
		return insertStatus;
	}

	@Override
	public List<WSBaseline> getSchedulesToBeCompared() {
		// logger.debug("Entry: getSchedulesToBeCompared");
		Query query = null;
		List<WSBaseline> wsBaselineSchedules = new ArrayList<WSBaseline>();
		try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from WSBaseline where wsBaselineId not in "
									+ " (select wsBaselineId from  WSReports) and wsScheduleId in"
									+ "(select wsScheduleId from WSSchedule where compareFlag = true) ");
			wsBaselineSchedules = query.list();
		} catch (Exception e) {
			logger.error("Exception in getSchedulesToBeCompared.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		// logger.debug("Exit: getSchedulesToBeCompared");
		return wsBaselineSchedules;
	}

	@Override
	public WSResults getScheduleIdfromResultId(Integer wsResultId) {
		logger.debug("Entry: getScheduleIdfromResultId");
		Query query = null;
		WSResults wsResults = new WSResults();
		try {

			query = sessionFactory.getCurrentSession().createQuery(
					"from WSResults where wsResultsId=:wsResultId");

			query.setParameter("wsResultId", wsResultId);

			List list = query.list();
			if (list != null && list.size() > 0) {
				wsResults = (WSResults) query.list().get(0);
			}

		} catch (Exception e) {
			logger.error("Exception in getSchedulesToBeCompared.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getScheduleIdfromResultId");
		return wsResults;
	}

	@Override
	public List<WSResults> getSchedulesFromScheduleId(Integer wsScheduleId) {
		logger.debug("Entry: getSchedulesFromScheduleId");

		Query query = null;
		List<WSResults> wsResult = new ArrayList<WSResults>();
		try {
			query = sessionFactory.getCurrentSession().createQuery(
					"from WSResults where wsScheduleId=:wsScheduleId");
			query.setParameter("wsScheduleId", wsScheduleId);
			List<WSResults> list = query.list();
			if (list != null && list.size() > 0) {
				wsResult = query.list();
			}

		} catch (Exception e) {
			logger.error("Exception in getSchedulesFromScheduleId.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getSchedulesFromScheduleId");
		return wsResult;
	}

	@Override
	public int pickUpSchedules() {
		// logger.debug("Entry: pickUpSchedules");
		int result = 0;
		List<Object[]> objList = null;
		try {
			
			
			Query query = sessionFactory
			.getCurrentSession()
			.createQuery("select ts.wsScheduleId, ts.webserviceSuiteId, twss.serviceId,twss.operationId, twps.parameterSetId, "
					+ " 1, twss.inputType  "
					+ "from WSSchedule ts, WebserviceSuiteService twss, WebserviceSuiteParamSetTable twps "
					+ "where ts.webserviceSuiteId=twss.webserviceSuiteId "
					+ "and twss.webserviceSuiteServiceId=twps.webserviceSuiteServiceId "
					+ "and ts.isScheduled=false "
					+ "and ts.wsScheduleId not in (select wsScheduleId from WSExecutionStatus )");
			objList = query.list();
			if (null != objList && objList.size() > 0) {
				insertSchedules(objList);
			}
			
			query = sessionFactory
			.getCurrentSession()
			.createQuery("select ts.wsScheduleId, ts.webserviceSuiteId, twss.serviceId,twss.operationId, twps.parameterSetId, "
							+ "1, twss.inputType  "
							+ "from WSSchedule ts, WebserviceSuiteService twss, WebserviceSuiteParamSetTable twps "
							+ "where ts.recurrenceDateWise<=:currentDateTime "
							+ "and ts.webserviceSuiteId=twss.webserviceSuiteId "
							+ "and twss.webserviceSuiteServiceId=twps.webserviceSuiteServiceId "
							+ "and ts.isScheduled=true and ts.scheduleType = 'D' and ts.reocurrence = false " 
							+ "and ts.wsScheduleId not in (select wsScheduleId from WSExecutionStatus )");
			query.setTimestamp("currentDateTime",new Date());
			objList = query.list();
			if (null != objList && objList.size() > 0) {
				insertSchedules(objList);
			}
			
			
			query = sessionFactory
			.getCurrentSession()
			.createQuery("select ts.wsScheduleId, ts.webserviceSuiteId, twss.serviceId,twss.operationId, twps.parameterSetId, "
							+ "1, twss.inputType  "
							+ "from WSSchedule ts, WebserviceSuiteService twss, WebserviceSuiteParamSetTable twps "
							+ "where ts.webserviceSuiteId=twss.webserviceSuiteId "
							//+ "and to_number(to_char(ts.startTime, 'HH24MI'))<=to_number(to_char(current_timestamp, 'HH24MI')) "
							+ "and extract(HOUR from ts.startTime)=:currentHour " 
							+ "and extract(MINUTE from ts.startTime)=:currentMins " 
							+ "and twss.webserviceSuiteServiceId=twps.webserviceSuiteServiceId "
							+ "and ts.isScheduled=true and ts.scheduleType = 'W' and ts.reocurrence = false and ts.scheduledDay like :dayName " 
							+ "and ts.wsScheduleId not in (select wsScheduleId from WSExecutionStatus)");
			int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
			int currentMins = Calendar.getInstance().get(Calendar.MINUTE);
			//query.setParameter("dayName", "%"+ String.valueOf(DateUtil.DAYS[new Date().getDay()]) +"%");
			query.setParameter("currentHour",currentHour);
			query.setParameter("currentMins",currentMins);
		objList = query.list();
			if (null != objList && objList.size() > 0) {
				insertSchedules(objList);
			}
			
			Object dialect = "";
			       //PropertyUtils.getProperty(
			    	//	   sessionFactory
			   		//	.getCurrentSession().getSessionFactory(), "dialect");

		    String dialectString = dialect.toString();
		    if(dialectString.contains("MySQL")){
				query = sessionFactory
				.getCurrentSession()
				.createQuery("select ts.wsScheduleId, ts.webserviceSuiteId, twss.serviceId,twss.operationId, twps.parameterSetId, "
								+ " 1, twss.inputType  "
								+ "from WSSchedule ts, WebserviceSuiteService twss, WebserviceSuiteParamSetTable twps "
								+ "where ts.webserviceSuiteId=twss.webserviceSuiteId "
							//	+ "and to_number(to_char(ts.startTime, 'HH24MI'))<=to_number(to_char(current_timestamp, 'HH24MI')) "
								+ "and extract(HOUR from ts.startTime)=:currentHour " 
								+ "and extract(MINUTE from ts.startTime)=:currentMins " 
								+ "and twss.webserviceSuiteServiceId=twps.webserviceSuiteServiceId "
								+ "and ts.isScheduled=true and ts.scheduleType = 'W' and ts.reocurrence = true and ts.scheduledDay like :dayName " 
								+ "and ts.wsScheduleId not in (select wsScheduleId from WSExecutionStatus where createdDate between :CurrentDateStart and :CurrentDateEnd) ");
			//	+ "and ts.wsScheduleId not in (select wsScheduleId from WSExecutionStatus where to_char(createdDate,'DD-MM-YYYY') = :currentDate)");
				currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
				currentMins = Calendar.getInstance().get(Calendar.MINUTE);
				query.setParameter("currentHour",currentHour);
				query.setParameter("currentMins",currentMins);
				//query.setParameter("dayName", "%"+ String.valueOf(DateUtil.DAYS[new Date().getDay()]) +"%");
				 //query.setTimestamp("CurrentDateStart", DateTimeUtil.getStartOfTheDay(new Date()));
				 //query.setTimestamp("CurrentDateEnd", DateTimeUtil.getEndOfTheDay(new Date()));
		    } else {
		    	query = sessionFactory
				.getCurrentSession()
				.createQuery("select ts.wsScheduleId, ts.webserviceSuiteId, twss.serviceId,twss.operationId, twps.parameterSetId, "
								+ " 1, twss.inputType  "
								+ "from WSSchedule ts, WebserviceSuiteService twss, WebserviceSuiteParamSetTable twps "
								+ "where ts.webserviceSuiteId=twss.webserviceSuiteId "
							//	+ "and to_number(to_char(ts.startTime, 'HH24MI'))<=to_number(to_char(current_timestamp, 'HH24MI')) "
								+ "and extract(HOUR from ts.startTime)=:currentHour " 
								+ "and extract(MINUTE from ts.startTime)=:currentMins " 
								+ "and twss.webserviceSuiteServiceId=twps.webserviceSuiteServiceId "
								+ "and ts.isScheduled=true and ts.scheduleType = 'W' and ts.reocurrence = true and ts.scheduledDay like :dayName " 
								+ "and ts.wsScheduleId not in (select wsScheduleId from WSExecutionStatus where trunc(createdDate) = trunc(:currentDateTime))");
							
		    	query.setTimestamp("currentDateTime",new Date());
		    	currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
				currentMins = Calendar.getInstance().get(Calendar.MINUTE);
				query.setParameter("currentHour",currentHour);
				query.setParameter("currentMins",currentMins);
				//query.setParameter("dayName", "%"+ String.valueOf(DateUtil.DAYS[new Date().getDay()]) +"%");
				
		    }
			/*String currentDate = DateUtil.convertToString(new Date(), "dd-MM-yyyy");
			query.setParameter("currentDate", currentDate );*/
			objList = query.list();
			if (null != objList && objList.size() > 0) {
				insertSchedules(objList);
			}
			
			/*
			//Ondemand execution - find all the Ondemand suites
			Query query = sessionFactory
			.getCurrentSession()
			.createQuery(
					"insert into WSExecutionStatus "
							+ "(wsScheduleId, webserviceSuiteId, serviceId, operationId, parameterSetId, createdDate, createdTime, executionStatusRefId, inputType) "
							+ "select ts.wsScheduleId, ts.webserviceSuiteId, twss.serviceId,twss.operationId, twps.parameterSetId, "
							+ "current_timestamp(), current_timestamp(), 1, twss.inputType  "
							+ "from WSSchedule ts, WebserviceSuiteService twss, WebserviceSuiteParamSetTable twps "
							+ "where ts.webserviceSuiteId=twss.webserviceSuiteId "
							+ "and twss.webserviceSuiteServiceId=twps.webserviceSuiteServiceId "
							+ "and ts.isScheduled=false "
							+ "and ts.wsScheduleId not in (select wsScheduleId from WSExecutionStatus )	"

			);
			result = query.executeUpdate();
			
			
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"insert into WSExecutionStatus "
									+ "(wsScheduleId, webserviceSuiteId, serviceId, operationId, parameterSetId, createdDate, createdTime, executionStatusRefId, inputType) "
									+ "select ts.wsScheduleId, ts.webserviceSuiteId, twss.serviceId,twss.operationId, twps.parameterSetId, "
									+ "current_timestamp(), current_timestamp(), 1, twss.inputType  "
									+ "from WSSchedule ts, WebserviceSuiteService twss, WebserviceSuiteParamSetTable twps "
									+ "where ts.startTime<=current_timestamp() "
									+ "and ts.webserviceSuiteId=twss.webserviceSuiteId "
									+ "and twss.webserviceSuiteServiceId=twps.webserviceSuiteServiceId "
									+ "and ts.isScheduled=true "
									+ "and ts.wsScheduleId not in (select wsScheduleId from WSExecutionStatus )	"

					);
			result = query.executeUpdate();
			
			
			
			
			
			//Weekly execution - Future schedules 
			query = sessionFactory
			.getCurrentSession()
			.createQuery(
					"insert into WSExecutionStatus "
							+ "(wsScheduleId, webserviceSuiteId, serviceId, operationId, parameterSetId, createdDate, createdTime, executionStatusRefId, inputType) "
							+ "select ts.wsScheduleId, ts.webserviceSuiteId, twss.serviceId,twss.operationId, twps.parameterSetId, "
							+ "current_timestamp(), current_timestamp(), 1, twss.inputType  "
							+ "from WSSchedule ts, WebserviceSuiteService twss, WebserviceSuiteParamSetTable twps "
							+ "where ts.webserviceSuiteId=twss.webserviceSuiteId "
							+ "and to_number(to_char(ts.startTime, 'HH24MI'))<=to_number(to_char(current_timestamp, 'HH24MI')) "
							+ "and twss.webserviceSuiteServiceId=twps.webserviceSuiteServiceId "
							+ "and ts.isScheduled=true and ts.scheduleType = 'W' and ts.reocurrence = false and ts.scheduledDay like :dayName " 
							+ "and ts.wsScheduleId not in (select wsScheduleId from WSExecutionStatus)	"

			);
			
		//	query.setTimestamp("currentDateTime",new Date());
			query.setParameter("dayName", "%"+ String.valueOf(DateUtil.DAYS[new Date().getDay()]) +"%");
			result = query.executeUpdate();
			
			//Weekly execution - Future schedules - Recurrence
			query = sessionFactory
			.getCurrentSession()
			.createQuery(
					"insert into WSExecutionStatus "
							+ "(wsScheduleId, webserviceSuiteId, serviceId, operationId, parameterSetId, createdDate, createdTime, executionStatusRefId, inputType) "
							+ "select ts.wsScheduleId, ts.webserviceSuiteId, twss.serviceId,twss.operationId, twps.parameterSetId, "
							+ "current_timestamp(), current_timestamp(), 1, twss.inputType  "
							+ "from WSSchedule ts, WebserviceSuiteService twss, WebserviceSuiteParamSetTable twps "
							+ "where ts.webserviceSuiteId=twss.webserviceSuiteId "
							+ "and to_number(to_char(ts.startTime, 'HH24MI'))<=to_number(to_char(current_timestamp, 'HH24MI')) "
							+ "and twss.webserviceSuiteServiceId=twps.webserviceSuiteServiceId "
							+ "and ts.isScheduled=true and ts.scheduleType = 'W' and ts.reocurrence = true and ts.scheduledDay like :dayName " 
							+ "and ts.wsScheduleId not in (select wsScheduleId from WSExecutionStatus where to_char(createdDate,'DD-MM-YYYY') = :currentDate)"

			);
			query.setParameter("dayName", "%"+ String.valueOf(DateUtil.DAYS[new Date().getDay()]) +"%");
			String currentDate = DateUtil.convertToString(new Date(), "dd-MM-yyyy");

			query.setParameter("currentDate", currentDate );
			result = query.executeUpdate();
*/
		} catch (Exception e) {
			logger.error("Exception in pickUpSchedules.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		// logger.debug("Exit: pickUpSchedules");
		return result;
	}

	private void insertSchedules(List<Object[]> results) {
		
		try {
			if (results != null && results.size() > 0) {
				
				for ( Object[] obj: results){
					WSExecutionStatus wsExecutionStatus = new WSExecutionStatus();
					wsExecutionStatus.setWsScheduleId((Integer) obj[0]);
					wsExecutionStatus.setWebserviceSuiteId((Integer)obj[1]);
					wsExecutionStatus.setServiceId((Integer)obj[2]);
					wsExecutionStatus.setOperationId((Integer)obj[3]);
					wsExecutionStatus.setParameterSetId((Integer)obj[4]);
					wsExecutionStatus.setCreatedDate(new Date());
					wsExecutionStatus.setExecutionStatusRefId((Integer)obj[5]);
					wsExecutionStatus.setInputType((String)obj[6]);
					sessionFactory.getCurrentSession().saveOrUpdate(wsExecutionStatus);
				}
			}
		} catch(Exception e){
			
		}
		
	}

	@Override
	public List<WSReports> getWSReportDatesFromSuiteId(int wsSuiteId) {
		logger.debug("Entry: getWSReportDatesFromSuiteId, wsSuiteId->"
				+ wsSuiteId);
		Query query = null;
		List<WSReports> wsReportsList = new ArrayList<WSReports>();
		try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"select wsScheduleId, max(generatedDateTime) as generatedDateTime from WSReports where webserviceSuiteId=:webserviceSuiteId "
									+ "GROUP BY wsScheduleId order by generatedDateTime desc");

			query.setParameter("webserviceSuiteId", wsSuiteId);

			List<Object[]> resultset = query.list();
			if (resultset != null && resultset.size() > 0) {
				for (Object[] obj : resultset) {
					try {
						WSReports wsReports = new WSReports();
						wsReports.setWsScheduleId((Integer) obj[0]);
						wsReports.setGeneratedDateTime((Date) obj[1]);
						wsReportsList.add(wsReports);
					} catch (Exception e) {
						logger.error("Exception while getting baseline details");
						logger.error("Stack Trace :"
								+ ExceptionUtils.getStackTrace(e));
					}
				}
			}
		} catch (Exception e) {
			logger.error("Exception in getWSReportDatesFromSuiteId.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getWSReportDatesFromSuiteId, wsReportsList->"
				+ wsReportsList);
		return wsReportsList;
	}

	@Override
	public List<WSReportsData> getWSReports(int webserviceSuiteId,
											int wsScheduleId) {
		logger.info("Entry :getWSReports, webserviceSuiteId->"
				+ webserviceSuiteId + " wsScheduleId->" + wsScheduleId);
		List<WSReportsData> WSReportsDataList = new ArrayList<WSReportsData>();
		Query query = null;

		boolean isBaseAvail = false;

		try {

			query = sessionFactory
					.getCurrentSession()
					.createSQLQuery(
							"select s.serviceName, o.operationName, rep.generatedDateTime, res.createdDateTime, res.requestXml, res.resultXml, "
									 + " rep.comparisonResultsContent, res.createdDateTime as time, rep.wsReportsId, res.wsResultsId, base.wsBaselineScheduleId, wsspt.webserviceParameterSetId "
									 + " from "
									 + " WSReports rep, WebserviceOperations o, Webservices s, WSResults res, WSBaseline base, WebserviceSuiteService wss, WebserviceSuiteParamSetTable wsspt"
									 + " where rep.operationId = o.operationId "
									 + " and o.serviceId = s.serviceId "
									 + " and wss.serviceId = s.serviceId" 
									 + " and wss.operationId = o.operationId"
									 + " and res.wsScheduleId = rep.wsScheduleId "
									 + " and rep.operationId = res.operationId "
									 + " and wsspt.webserviceSuiteServiceId = wss.webserviceSuiteServiceId"
									 + " and wsspt.parameterSetId = res.parameterSetId"
									 + " and rep.parameterSetId = res.parameterSetId"
									 + " and base.wsScheduleId (+)= rep.wsScheduleId"
									 + " and rep.webserviceSuiteId = res.webserviceSuiteId "
									 + " and rep.webserviceSuiteId = wss.webserviceSuiteId "
									 + " and wss.webserviceSuiteId = :webserviceSuiteId "
									 + " and rep.wsScheduleId = :wsScheduleId");
			
			query.setParameter("webserviceSuiteId", webserviceSuiteId);
			query.setParameter("wsScheduleId", wsScheduleId);
			List<Object> list = query.list();
			
			if (list != null && list.size() > 0) {
				for (Iterator<Object> i = list.iterator(); i.hasNext();) {  
				    Object[] values = (Object[]) i.next();
					try {
						WSReportsData wsReportsData = new WSReportsData();
						wsReportsData.setService(values[0].toString());
						wsReportsData.setOperation(values[1].toString());
						wsReportsData.setExecutionDate(DateTimeUtil
								.formatDateString(values[7].toString(),
										DateTimeUtil.DATE_FORMAT).toString()
								+ " "
								+ DateTimeUtil.formatDateString(
										values[3].toString(),
										DateTimeUtil.TIME_FORMAT).toString());// current
																				// date

						if ( null != values[10] ){
							wsReportsData.setWsBaselineScheduleId(((Number) values[10]).intValue());
							
							if ( wsReportsData.getWsBaselineScheduleId() > 0 ){
								isBaseAvail  = true;
							}else{
								isBaseAvail = false;
							}
						}
						
						Clob clob = (Clob) values[4];

						if (clob != null && clob.toString().length() > 0 ) {
							String results = clob.getSubString(1, (int) clob.length());
							results = StringEscapeUtils
									.escapeXml(results.toString());
							results.replaceAll("/",
									Matcher.quoteReplacement("\\/"));

							wsReportsData.setRequestXML(results);
						}else{
							wsReportsData.setRequestXML("-");
						}
						
						clob = (Clob) values[5];

						if (clob != null && clob.toString().length() > 0 ) {
							String results = clob.getSubString(1, (int) clob.length());
							results = StringEscapeUtils
									.escapeXml(results.toString());
							results.replaceAll("/",
									Matcher.quoteReplacement("\\/"));

							wsReportsData.setResponseXML(results);
						}else{
							wsReportsData.setResponseXML("-");
						}

						clob = (Clob) values[6];
						
						if (clob != null && clob.toString().length() > 0 ) {
							String results = clob.getSubString(1, (int) clob.length());
							results = StringEscapeUtils
									.escapeXml(results.toString());
							results.replaceAll("/",
									Matcher.quoteReplacement("\\/"));

							if ( null != results && results.equals("-")){
								wsReportsData.setDifferences("-");
								if (isBaseAvail) {
									wsReportsData.setMatched("YES");
								} else {
									wsReportsData.setMatched("NA");
								}
							}else{
								wsReportsData.setDifferences(results);
								if (isBaseAvail) {
									wsReportsData.setMatched("NO");
								} else {
									wsReportsData.setMatched("NA");
								}
							}
							
						}else{
							if (isBaseAvail) {
								wsReportsData.setMatched("NO");
							} else {
								wsReportsData.setMatched("NA");
							}
						}

						wsReportsData.setReportsId(((Number) values[8]).intValue());
						wsReportsData.setResultsId(((Number) values[9]).intValue());
						
						if ( null != values[11]){
							wsReportsData.setWebserviceParameterSetId(((Number) values[11]).intValue());
						}
						
						WSReportsDataList.add(wsReportsData);
					} catch (Exception e) {
						logger.error("Exception while getting Report details");
						logger.error("Stack Trace :"
								+ ExceptionUtils.getStackTrace(e));
					}
				}
				
				if ( isBaseAvail && null != WSReportsDataList && WSReportsDataList.size() > 0 ){
					int wsBaselineScheduleId = WSReportsDataList.get(0).getWsBaselineScheduleId();
					
					query.setParameter("webserviceSuiteId", webserviceSuiteId);
					query.setParameter("wsScheduleId", wsBaselineScheduleId);
					list = query.list();
				}
				
				//Collect baseline details.
				if (list != null && list.size() > 0) {
					for (Iterator<Object> i = list.iterator(); i.hasNext();) {  
					    Object[] values = (Object[]) i.next();
						try {
							for ( WSReportsData wsReportsData : WSReportsDataList){
								if ( null != values[0] && null != values[1] && null != values[11] && wsReportsData.getService().equals(values[0].toString()) 
										&& wsReportsData.getOperation().equals(values[1].toString()) && wsReportsData.getWebserviceParameterSetId() == ((Number) values[11]).intValue()){
									wsReportsData.setBaselineDate(DateTimeUtil
											.formatDateString(values[7].toString(),
													DateTimeUtil.DATE_FORMAT).toString()
											+ " "
											+ DateTimeUtil.formatDateString(
													values[3].toString(),
													DateTimeUtil.TIME_FORMAT).toString());// current
																							// date

									
									Clob clob = (Clob) values[4];
									
									if (clob != null && clob.toString().length() > 0 ) {
										String results = clob.getSubString(1, (int) clob.length());
										results = StringEscapeUtils
												.escapeXml(results.toString());

										results.replaceAll("/",
												Matcher.quoteReplacement("\\/"));

										wsReportsData.setBaselineRequestXML(results);
									}else{
										wsReportsData.setBaselineRequestXML("-");
									}
									if(!isBaseAvail){
										wsReportsData.setBaselineRequestXML("-");
									}
									clob = (Clob) values[5];
									if (clob != null && clob.toString().length() > 0 ) {
										String results = clob.getSubString(1, (int) clob.length());

										results = StringEscapeUtils
												.escapeXml(results.toString());
										results.replaceAll("/",
												Matcher.quoteReplacement("\\/"));

										wsReportsData.setBaselineResponseXML(results);
									}else{
										wsReportsData.setBaselineResponseXML("-");
									}
									if(!isBaseAvail){
										wsReportsData.setBaselineResponseXML("-");
									}
								}
							}
						}catch(Exception e){
							
						}
					}
				}
			}

			sessionFactory.getCurrentSession().flush();
			sessionFactory.getCurrentSession().clear();

		} catch (Exception e) {
			logger.error("Exception in getWSReports.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}

		logger.info("Exit :getWSReports");
		return WSReportsDataList;
	}

	@Override
	public List<WSSuiteDetails> getWSSuiteDetails(int wsSuiteId) {
		logger.debug("Entry: getWSSuiteDetails");
		Query query = null;
		List<WSSuiteDetails> wsSuiteDetailsList = new ArrayList<WSSuiteDetails>();
		try {

			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"select a.webserviceSuiteId, s.serviceName, o.operationName, b.parameterSetId, c.parameterName,d.datasetName, d.parameterValue "
									+ "from WebserviceSuiteService a, WebserviceSuiteParamSetTable b, WsOperationParameter c, "
									+ "WsOperationParameterValue d, Webservices s, WebserviceOperations o "
									+ "where a.webserviceSuiteId=:wsSuiteId "
									+ "and a.webserviceSuiteServiceId=b.webserviceSuiteServiceId "
									+ "and b.parameterSetId=d.paramSetId.parameterSetId "
									+ "and d.paramSetId.parameterId=c.parameterId "
									+ "and a.serviceId=s.serviceId "
									+ "and a.operationId=o.operationId "
									+ "and a.inputType='rawInput'");
			query.setParameter("wsSuiteId", wsSuiteId);
			List<Object> result = new ArrayList();
			result = query.list();
			for (Iterator<Object> i = result.iterator(); i.hasNext();) {
				Object[] values = (Object[]) i.next();
				WSSuiteDetails wsSuiteDetails = new WSSuiteDetails();

				wsSuiteDetails.setWebserviceSuiteId(Integer.parseInt(values[0]
						.toString()));
				wsSuiteDetails.setServiceName(values[1].toString());
				wsSuiteDetails.setOperationName(values[2].toString());
				wsSuiteDetails.setParamSetId(Integer.parseInt(values[3]
						.toString()));
				wsSuiteDetails.setParameterName(values[4].toString());
				wsSuiteDetails.setDatasetName(values[5].toString());

				try {
					Clob clob = (Clob) values[6];
					if (clob != null) {
						wsSuiteDetails.setParameterValue(clob.getSubString(1,
								(int) clob.length()));
					}
				} catch (ClassCastException e) {
					try {
						if (null != values[6]) {
							wsSuiteDetails.setParameterValue(values[6]
									.toString());
						}
					} catch (Exception ex) {
						logger.error("Not a String values[6] :" + values[6]);
					}
				}
				wsSuiteDetails.setInputType("rawInput");
				wsSuiteDetailsList.add(wsSuiteDetails);

			}
			if (result.size() <= 0) {
				query = sessionFactory
						.getCurrentSession()
						.createQuery(
								"select a.webserviceSuiteId, s.serviceName, o.operationName, b.parameterSetId, c.parameterName, c.parameterValue, c.datasetName "
										+ "from WebserviceSuiteService a, WebserviceSuiteParamSetTable b, WsOperationXmlParameter c, "
										+ "Webservices s, WebserviceOperations o "
										+ "where a.webserviceSuiteId=:wsSuiteId "
										+ "and a.webserviceSuiteServiceId=b.webserviceSuiteServiceId "
										+ "and b.parameterSetId=c.parameterId "
										+ "and a.serviceId=s.serviceId "
										+ "and a.operationId=o.operationId "
										+ "and a.inputType='xmlInput'");
				query.setParameter("wsSuiteId", wsSuiteId);

				result = query.list();
				for (Iterator<Object> i = result.iterator(); i.hasNext();) {
					Object[] values = (Object[]) i.next();
					WSSuiteDetails wsSuiteDetails = new WSSuiteDetails();

					wsSuiteDetails.setWebserviceSuiteId(Integer
							.parseInt(values[0].toString()));
					wsSuiteDetails.setServiceName(values[1].toString());
					wsSuiteDetails.setOperationName(values[2].toString());
					wsSuiteDetails.setParamSetId(Integer.parseInt(values[3]
							.toString()));
					wsSuiteDetails.setParameterName(values[4].toString());

					try {
						Clob clob = (Clob) values[5];
						if (clob != null) {
							wsSuiteDetails.setParameterValue(clob.getSubString(
									1, (int) clob.length()));
						}
					} catch (ClassCastException e) {
						try {
							if (null != values[5]) {
								wsSuiteDetails.setParameterValue(values[5]
										.toString());
							}
						} catch (Exception ex) {
							logger.error("Not a String values[5] :" + values[5]);
						}
					}

					if (null != values[6]) {
						wsSuiteDetails.setDatasetName(values[6].toString());
					}

					wsSuiteDetailsList.add(wsSuiteDetails);

					List<WsOperationHeaderParameters> headers = getTestSoapParameterHeaders(wsSuiteDetails
							.getParamSetId());
					for (WsOperationHeaderParameters headerParam : headers) {

						wsSuiteDetails = new WSSuiteDetails();

						wsSuiteDetails.setWebserviceSuiteId(Integer
								.parseInt(values[0].toString()));
						wsSuiteDetails.setServiceName(values[1].toString());
						wsSuiteDetails.setOperationName(values[2].toString());
						wsSuiteDetails.setParamSetId(Integer.parseInt(values[3]
								.toString()));
						wsSuiteDetails.setParameterName(headerParam
								.getParameterName());
						wsSuiteDetails.setParameterValue(headerParam
								.getParameterValue());
						wsSuiteDetailsList.add(wsSuiteDetails);
					}

				}
			}

		} catch (Exception e) {
			logger.error("Exception in getWSSuiteDetails.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getWSSuiteDetails");
		return wsSuiteDetailsList;
	}

	@Override
	public Boolean getEndPointForService(Integer serviceId, int environmentId) {
		logger.debug("Entry: getEndPointForService");
		Boolean result = false;
		Query query = null;
		try {
			query = sessionFactory.getCurrentSession().createQuery(
					"select w.serviceId, w.endpoint from  WsEndpointDetails w "
							+ "where w.serviceId=:serviceId "
							+ "and w.environmentId=:environmentId ");
			query.setParameter("serviceId", serviceId);
			query.setParameter("environmentId", environmentId);
			if (query.list() != null && query.list().size() > 0) {
				result = true;
			}
		} catch (Exception e) {
			logger.error("Exception in getEndPointForService.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getEndPointForService");
		return result;
	}

	@Override
	public boolean wsSaveBaseline(WSBaseline wsBaseline) {
		logger.debug("Entry: getEndPointForService");
		Boolean insertStatus = false;
		try {

			Integer id = (Integer) sessionFactory.getCurrentSession().save(
					wsBaseline);
			insertStatus = true;

		} catch (Exception e) {
			logger.error("Exception in wsSaveBaseline.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
			insertStatus = false;
		}
		logger.debug("Exit: wsSaveBaseline");
		return insertStatus;
	}

	@Override
	public List<WSResults> getWSBaselineDates(int wsSuiteId) {
		logger.debug("Entry: getWSBaselineDates");
		Query query = null;
		List<WSResults> wsResultsList = new ArrayList<WSResults>();
		try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"select wsScheduleId, max(createdDateTime) as createdDateTime from WSResults where webserviceSuiteId=:webserviceSuiteId"
									+ " and executionStatusRefId =3 group by wsScheduleId order by createdDateTime desc ");

			query.setParameter("webserviceSuiteId", wsSuiteId);

			List<Object[]> resultset = query.list();
			if (resultset != null && resultset.size() > 0) {
				for (Object[] obj : resultset) {
					try {
						WSResults wsResults = new WSResults();
						wsResults.setWsScheduleId((Integer) obj[0]);
						wsResults.setCreatedDateTime((Date) obj[1]);
						wsResultsList.add(wsResults);
					} catch (Exception e) {
						logger.error("Exception while getting baseline details");
						logger.error("Stack Trace :"
								+ ExceptionUtils.getStackTrace(e));
					}
				}
			}
			logger.info("wsResultsList :" + wsResultsList);
		} catch (Exception e) {
			logger.error("Exception in getWSBaselineDates.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getWSBaselineDates");
		return wsResultsList;
	}

	@Override
	public List<WebserviceSuite> getWSTestSuitesForUser(int currentUserId) {
		logger.debug("Entry: getWSTestSuitesForUser");
		Query query = null;
		List<WebserviceSuite> testWebserviceSuiteList = new ArrayList<WebserviceSuite>();
		try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery("from WebserviceSuite where userId =:userId and privateSuit = :privateSuit");
			query.setParameter("userId", currentUserId);
			query.setParameter("privateSuit", true);
			testWebserviceSuiteList = query.list();
		} catch (Exception e) {
			logger.error("Exception in getWSTestSuitesForUser.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getWSTestSuitesForUser");
		return testWebserviceSuiteList;
	}
	
	@Override
	public List<WebserviceSuite> getWSTestSuitesForGroup(int currentGroupId) {
		logger.debug("Entry: getWSTestSuitesForGroup");
		Query query = null;
		List<WebserviceSuite> testWebserviceSuiteList = new ArrayList<WebserviceSuite>();
		try {
			query = sessionFactory
					.getCurrentSession()
					.createQuery("select suite.webserviceSuiteId, suite.webserviceSuiteName,suite.environmentId,suite.userId, " +
							"suite.privateSuit,suite.createdDate, suite.createdTime, u from Users u, WebserviceSuite suite " +
							"where u.groupId =:groupId and u.userId = suite.userId");
			query.setParameter("groupId", currentGroupId);
			
			List<Object[]> resultset = query.list();
			if (resultset != null && resultset.size() > 0) {
				for (Object[] obj : resultset) {
					try {
						WebserviceSuite webserviceSuite = new WebserviceSuite();
						webserviceSuite.setWebserviceSuiteId((Integer) obj[0]);
						webserviceSuite.setWebserviceSuiteName((String) obj[1]);
						webserviceSuite.setEnvironmentId((Integer)obj[2]);
						webserviceSuite.setUserId((Integer)obj[3]);
						webserviceSuite.setPrivateSuit((Boolean)obj[4]);
						webserviceSuite.setCreatedDate((Date) obj[5]);
						webserviceSuite.setCreatedTime((Time) obj[6]);
						webserviceSuite.setUsers((Users) obj[7]);
						testWebserviceSuiteList.add(webserviceSuite);
						/*wsResults.setWsScheduleId((Integer) obj[0]);
						wsResults.setCreatedDateTime((Date) obj[1]);
						wsResultsList.add(wsResults);*/
					} catch (Exception e) {
						logger.error("Exception while getting baseline details");
						logger.error("Stack Trace :"
								+ ExceptionUtils.getStackTrace(e));
					}
				}
			}
			
			
			
			
		} catch (Exception e) {
			logger.error("Exception in getWSTestSuitesForGroup.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getWSTestSuitesForGroup");
		return testWebserviceSuiteList;
	}
	
	@Override
	public boolean removeSuitDetails(WebserviceSuite suit) {
		logger.debug("Entry: removeSuitDetails : webservice Suite");
		Query query = null;
		boolean result = false;int count =0;
		try{
		if(suit.getWebserviceSuiteId() > 0){
			query = sessionFactory.getCurrentSession().createQuery("from WebserviceSuiteService where webserviceSuiteId=:webserviceSuiteId");
			query.setParameter("webserviceSuiteId", suit.getWebserviceSuiteId());
			List<WebserviceSuiteService> listWebserviceSuiteService = query.list();
			for(WebserviceSuiteService webserviceSuiteService: listWebserviceSuiteService) {
				this.deleteWSParameterSet(webserviceSuiteService.getWebserviceSuiteServiceId());
			}
			this.deleteWSSuiteService(suit.getWebserviceSuiteId());
			
			query = sessionFactory
			.getCurrentSession()
			.createQuery("delete from WebserviceSuite where webserviceSuiteId=:webserviceSuiteId ");
			query.setParameter("webserviceSuiteId", suit.getWebserviceSuiteId());
			count = query.executeUpdate();
			result =  true;
		}
		} catch(Exception e){
			logger.error("Exception in removeSuitDetails.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
			result = false;
		}
		logger.debug("Exit: removeSuitDetails : webservice Suite");
		return result;
	}
}
