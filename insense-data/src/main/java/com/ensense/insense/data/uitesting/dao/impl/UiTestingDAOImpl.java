package com.ensense.insense.data.uitesting.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.mint.common.entity.SuitGroupXref;
import com.cts.mint.common.utils.Constants.KEYS;
import com.cts.mint.uitesting.dao.UiTestingDAO;
import com.cts.mint.uitesting.entity.Suit;
import com.cts.mint.uitesting.entity.SuitBrokenReportsXref;
import com.cts.mint.uitesting.entity.SuitTextImageXref;
import com.cts.mint.util.DateTimeUtil;

@Service
public class UiTestingDAOImpl implements UiTestingDAO {

	private static Logger logger = Logger.getLogger(UiTestingDAOImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public boolean saveFunctionalityTestingSuit(Suit suit) {
		boolean result = false;
		logger.debug("Entry: SaveFunctionalityTestingSuit");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(suit);
			sessionFactory.getCurrentSession().flush();
			result = true;
		} catch (Exception e) {
			logger.error("Exception in SaveFunctionalityTestingSuit :" + e);
			logger.error("Stack Trace :"
					+ ExceptionUtils.getRootCauseMessage(e));
		}
		logger.debug("Exit: SaveFunctionalityTestingSuit");
		return result;
	}

	@Override
	public boolean saveSuitGroupXref(SuitGroupXref suitGroup) {
		boolean result = false;
		logger.debug("Entry: saveSuitGroupXref");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(suitGroup);
			result = true;
		} catch (Exception e) {
			logger.error("Exception in saveSuitGroupXref :" + e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: saveSuitGroupXref");
		return result;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getBatchFileData() {
		SQLQuery query = null;
		List<String> batchFileData = new ArrayList<String>();
		Set<String> suitSet = new HashSet<String>();
		//String dateString = "";
		query = sessionFactory
				.getCurrentSession()
				.createSQLQuery(
						"select s.suitId, s.suitName, se.scheduleId, se.testExecutionStartTime from Suit s, Schedule sc, ScheduleExecution se "
								+ "where se.testExecutionStatusRefId = 3 "
								+ "and s.suitId = sc.suitId and sc.scheduleId = se.scheduleId "
								/*+ "and (se.reportStatusRefId = 3 or se.reportStatusRefId = 5 ) "
								+ "and ( se.comparisonStatusRefId = 3 or se.comparisonStatusRefId = 5) "
								+ "and ( se.comparisonStatusRefId = 3 or se.comparisonStatusRefId = 5) " */
								+ "order by s.suitId, se.testExecutionStartTime desc");

		//TODO - loop through the list and create seperate list for each Suit.
		List<Object[]> resultset = query.list();
		
		if (resultset != null && resultset.size() > 0) {
			for (Object[] obj : resultset) {
				String suitName = String.valueOf(obj[1]);
				suitSet.add(suitName);
			}
			for(String suit : suitSet) {
				String batchRow = KEYS.BLANK;
				List<String> dates = new ArrayList<String>();
				batchRow += suit + KEYS.COMMA;
				int rowcount = 1;
				for (Object[] obj : resultset) {
					String suitName = String.valueOf(obj[1]);
					if(suit.equalsIgnoreCase(suitName)) {
						if(rowcount <=5) {
							dates.add(DateTimeUtil.formatDateString(String.valueOf(obj[3]), "MMM/dd/yyyy"));
							rowcount ++;
						}
					}
				}
				Collections.reverse(dates);
				for(int i=0;i<=(5-rowcount);i++) {
					dates.add(i, "NA");
				}
				batchRow += dates.toString();
				batchRow += ",true,{true/false},{date}";
				batchRow = batchRow.replace("[", KEYS.BLANK);
				batchRow = batchRow.replace("]", KEYS.BLANK);
				batchFileData.add(batchRow);
			}
			
		}
		
	/*	if (resultset != null && resultset.size() > 0) {
			for (Object[] obj : resultset) {
				// suit name
				String str = String.valueOf(obj[1]);
				// check whether completion date available
				if (obj[2] != null && StringUtils.isNotBlank(obj[2].toString())) {
					dateString = String.valueOf(obj[2]);
					int count = 5;
					// when more than one completed dates available
					if (dateString.contains(KEYS.COMMA)) {
						count = count - dateString.split(KEYS.COMMA).length;
					} else {
						count = 4;
					}
					for (int i = 0; i < count; i++) {
						str = str + KEYS.COMMA + "NA";
					}
					str = str + KEYS.COMMA + dateString;
				} else {
					str = str + ",NA,NA,NA,NA,NA";
				}
				str = str + "
				str = str.replaceAll(" ", "");
				batchFileData.add(str);
			}
		}*/
		return batchFileData;
	}
	
	@Override
	public boolean saveTextOrImageSuit(Suit suit) {
		boolean result = false;
		logger.debug("Entry: saveTextOrImageSuit");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(suit);
			sessionFactory.getCurrentSession().flush();
			result = true;
		} catch (Exception e) {
			logger.error("Exception in saveTextOrImageSuit :" + e);
			logger.error("Stack Trace :"
					+ ExceptionUtils.getRootCauseMessage(e));
		}
		logger.debug("Exit: saveTextOrImageSuit");
		return result;
	}

	@Override
	public boolean saveSuitTextImageXref(SuitTextImageXref suitTextImage) {
		boolean result = false;
		logger.debug("Entry: saveSuitTextImageXref");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(suitTextImage);
			result = true;
		} catch (Exception e) {
			logger.error("Exception in saveSuitTextImageXref :" + e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: saveSuitTextImageXref");
		return result;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<SuitTextImageXref> getSuitTextImageXref(int suitId){
		logger.debug("Entry: getSuitTextImageXref");
		Query query = null;
		List<SuitTextImageXref> suitTextImageXref = new ArrayList<SuitTextImageXref>();
		try {
			query = sessionFactory.getCurrentSession().createQuery("from SuitTextImageXref where suitId =:suitId ");

			query.setParameter("suitId", suitId);
			suitTextImageXref = query.list();
		} catch (Exception e) {
			logger.error("Exception in getSuitTextImageXref :" + e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getSuitTextImageXref");
		return suitTextImageXref;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<SuitBrokenReportsXref> getSuitBrokenReportsXref(int suitId){
		logger.debug("Entry: getSuitBrokenReportsXref");
		Query query = null;
		List<SuitBrokenReportsXref> suitBrokenReportsXrefList = new ArrayList<SuitBrokenReportsXref>();
		try {
			query = sessionFactory.getCurrentSession().createQuery("from SuitBrokenReportsXref where suitId =:suitId ");
			query.setParameter("suitId", suitId);
			suitBrokenReportsXrefList = query.list();
			
		} catch (Exception e) {
			logger.error("Exception in getSuitBrokenReportsXref :" + e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getSuitBrokenReportsXref");
		return suitBrokenReportsXrefList;

	}
	
	@Override
	public boolean deleteSuitTextImageXref(int suitId) {
		logger.debug("Entry: deleteSuitTextImageXref");
		boolean result = true;
		try {
			Query query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"delete from SuitTextImageXref where suitId=:suitId");
			query.setParameter("suitId", suitId);
			query.executeUpdate();
		} catch (Exception e) {
			result = false;
			logger.error("Exception in deleteSuitTextImageXref :" + e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: deleteSuitTextImageXref");
		return result;
	}
	
	@Override
	public boolean saveSuitBrokenReportsXref(ArrayList<SuitBrokenReportsXref> suitBrokenReportsXrefList){
		boolean result = false;
		logger.debug("Entry: saveSuitBrokenReportsXref");
		try {
			
			ListIterator<SuitBrokenReportsXref>  suitBrokenReport= suitBrokenReportsXrefList.listIterator();
			while(suitBrokenReport.hasNext()){
				sessionFactory.getCurrentSession().saveOrUpdate(suitBrokenReport.next());	
				sessionFactory.getCurrentSession().flush();
			}
			result = true;
		} catch (Exception e) {
			logger.error("Exception in saveSuitBrokenReportsXref :" + e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: saveSuitBrokenReportsXref");
		return result;
	}
	
	@Override
	public boolean deleteSuitBrokenReportsXref(int suitId) {
		logger.debug("Entry: deleteSuitBrokenReportsXref");
		boolean result = true;
		try {
			Query query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"delete from SuitBrokenReportsXref where suitId=:suitId");
			query.setParameter("suitId", suitId);
			query.executeUpdate();
		} catch (Exception e) {
			result = false;
			logger.error("Exception in deleteSuitBrokenReportsXref :" + e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: deleteSuitBrokenReportsXref");
		return result;
	}

	@Override
	public boolean getCountSuitTextImageXref(int suitId) {
		logger.debug("Entry: getSuitTextImageXref");
		boolean result = false;
		try {
			Query query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from SuitTextImageXref where suitId=:suitId");
			query.setParameter("suitId", suitId);
			List<Object[]> resultset = query.list();
			if (resultset != null && resultset.size() > 0) {
				result = true;
			}
			
		} catch (Exception e) {
			
			logger.error("Exception in getSuitTextImageXref :" + e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getSuitTextImageXref");
		return result;
	}
}
