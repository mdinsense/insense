package com.ensense.insense.services.uiadmin.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cts.mint.uitesting.dao.IncludeUrlDAO;
import com.cts.mint.uitesting.entity.IncludeUrl;
import com.cts.mint.uitesting.service.IncludeURLService;

@Service
public class IncludeURLServiceImpl implements IncludeURLService {
	private static Logger logger = Logger
			.getLogger(IncludeURLServiceImpl.class);

	@Autowired
	IncludeUrlDAO includeUrlDAO;

	@Override
	@Transactional
	/*
	 * Calling IncludeURLSetupDAOImpl class to get IncludeURL List
	 */
	public List<IncludeUrl> getIncludeURLList() {
		logger.debug("Entry: getIncludeURLList");
		return includeUrlDAO.getIncludeURLList();
	}

	/*
	 * Calling IncludeURLSetupDAOImpl class to add new IncludeURL to the List
	 */
	@Override
	@Transactional
	public boolean addIncludeURL(IncludeUrl includeURL) {
		logger.debug("Entry: addIncludeURL");
		return includeUrlDAO.addIncludeURL(includeURL);
	}

	/*
	 * Calling IncludeURLSetupDAOImpl class to get Includeurl
	 */
	@Override
	@Transactional
	public IncludeUrl getIncludeURL(IncludeUrl includeURL) {
		logger.debug("Entry: getIncludeURL");
		return includeUrlDAO.getIncludeURL(includeURL);
	}

	@Override
	@Transactional
	public boolean deleteIncludeURLDetails(IncludeUrl includeURL) {
		logger.debug("Entry: deleteIncludeURLDetails");
		return includeUrlDAO.deleteIncludeURLDetails(includeURL);
	}

	@Override
	@Transactional
	public List<IncludeUrl> getIncludeURLList(int applicationId,
			int environmentId) {
		return includeUrlDAO.getIncludeURLList(applicationId, environmentId);
	}


}
