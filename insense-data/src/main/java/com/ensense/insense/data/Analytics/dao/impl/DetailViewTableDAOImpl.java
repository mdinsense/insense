package com.ensense.insense.data.analytics.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.mint.analytics.dao.DetailViewTableDAO;
import com.cts.mint.analytics.entity.DetailedView;

//import com.example.tests.DataSource;
@Service
public class DetailViewTableDAOImpl implements DetailViewTableDAO {

	private static Logger logger = Logger
			.getLogger(DetailViewTableDAOImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	public boolean populateDetailedView(List<DetailedView> detailedViewList) {
		/*
		 * String sql = "INSERT INTO detailed_view_table" +
		 * "(schedule_id,application_name,page_url,page_title,harlog_filename,webanalyics_pagedata_xml) VALUES (?,?,?,?,?,?)"
		 * ;
		 * 
		 * DataSource dataSource = new DataSource();
		 * 
		 * JdbcTemplate jdbcTemplate= new
		 * JdbcTemplate(dataSource.getDataSource()); //JdbcTemplate jdbcTemplate
		 * = new JdbcTemplate(dataSource.getDataSource());
		 * 
		 * //int row = jdbcTemplate.update(sql,objects,types);
		 * //jdbcTemplate.update(sql); //jdbcTemplate.insert(sql, objects,
		 * types); // jdbcTemplate.insert(sql1, objects, types);
		 * //jdbcTemplate.update(sql, objects ,types);
		 * System.out.println(detailedView
		 * .getApplicationName()+"getDetailedSummary"
		 * +detailedView.getWebAnalyticsPageDataXml
		 * ()+"getHarlogFileName"+detailedView
		 * .getHarLogFileName()+"getPageTitle"
		 * +detailedView.getPageTitle()+"getPageURL "
		 * +detailedView.getPageURL()+"getTestscheduleid"
		 * +detailedView.getScheduleId());
		 * 
		 * jdbcTemplate.update(sql,new Object[] {
		 * detailedView.getScheduleId(),detailedView
		 * .getApplicationName(),detailedView
		 * .getPageURL(),detailedView.getPageTitle
		 * (),detailedView.getHarLogFileName
		 * (),detailedView.getWebAnalyticsPageDataXml() });
		 */

		boolean result = false;
		logger.info("Entry: populateDetailedView");
		try {
			Session session = sessionFactory.getCurrentSession();
			for ( DetailedView detailedView : detailedViewList) {
				session.save(detailedView);
			}
		} catch (HibernateException exp) {
			logger.error(exp, new HibernateException(exp)); // TODO implement
															// custom exception.
		} catch (Exception exp) {
			logger.error(exp, new Exception(exp)); // TODO implement custom
													// exception.
		}
		logger.info("Exit: populateDetailedView");
		return result;

	}

}
