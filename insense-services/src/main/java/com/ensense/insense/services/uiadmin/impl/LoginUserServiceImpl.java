package com.ensense.insense.services.uiadmin.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cts.mint.uitesting.dao.LoginUserServiceDAO;
import com.cts.mint.uitesting.entity.LoginUserDetails;
import com.cts.mint.uitesting.service.LoginUserService;

@Service
public class LoginUserServiceImpl implements LoginUserService {
	private static Logger logger = Logger.getLogger(LoginUserServiceImpl.class);

	@Autowired
	LoginUserServiceDAO testLoginUserDAO;

	@Override
	@Transactional
	public List<LoginUserDetails> getLoginUserList() {
		logger.info("Entry And Exit : getLoginUserList");
		return testLoginUserDAO.getLoginUserList();
	}

	@Override
	@Transactional
	public boolean addLoginUserDetails(LoginUserDetails loginUserDetails) {
		logger.info("Entry And Exit : addLoginUserDetails");
		return testLoginUserDAO.addLoginUserDetails(loginUserDetails);
	}

	@Override
	@Transactional
	public LoginUserDetails getLoginUserDetails(
			LoginUserDetails loginUserDetails) {
		logger.info("Entry And Exit : addLoginUserDetails");
		loginUserDetails = testLoginUserDAO
				.getLoginUserDetails(loginUserDetails);
		return loginUserDetails;
	}

	@Override
	@Transactional
	public boolean deleteLoginUserDetails(LoginUserDetails loginUserDetails) {
		logger.info("Entry And Exit : deleteLoginUserDetails");
		return testLoginUserDAO.deleteLoginUserDetails(loginUserDetails);
	}

	@Override
	@Transactional
	public boolean deleteLoginUIElements(LoginUserDetails loginUserDetails) {
		logger.info("Entry And Exit : deleteLoginUIElements");
		return testLoginUserDAO.deleteLoginUIElements(loginUserDetails);
	}

	@Override
	@Transactional
	public boolean deleteLoginUserListdetails(LoginUserDetails loginUserDetails) {
		logger.info("Entry And Exit : deleteLoginUserListdetails");
		return testLoginUserDAO.deleteLoginUserListdetails(loginUserDetails);
	}

	@Override
	@Transactional
	public List<LoginUserDetails>  getLoginUserDetails(int applicationId,
			int environmentCategoryId) {
		logger.info("Entry And Exit : getLoginUserDetails");
		return testLoginUserDAO.getLoginUserDetails(applicationId, environmentCategoryId);
	}
	
	@Override
	@Transactional
	public LoginUserDetails getLoginIdDetails(LoginUserDetails loginUserDetails){
		logger.info("Entry And Exit : getLoginIdDetails");
		return testLoginUserDAO.getLoginIdDetails(loginUserDetails);
	}
}
