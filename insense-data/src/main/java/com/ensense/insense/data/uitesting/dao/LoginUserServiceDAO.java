package com.ensense.insense.data.uitesting.dao;

import java.util.List;

import com.cts.mint.uitesting.entity.LoginUserDetails;

public interface LoginUserServiceDAO {

	public boolean addLoginUserDetails(LoginUserDetails loginUserDetails);

	public List<LoginUserDetails> getLoginUserList();

	public LoginUserDetails getLoginUserDetails(LoginUserDetails loginUserDetails);

	public boolean deleteLoginUserDetails(LoginUserDetails loginUserDetails);

	public boolean deleteLoginUIElements(LoginUserDetails loginUserDetails);

	public boolean deleteLoginUserListdetails(LoginUserDetails loginUserDetails);

	public List<LoginUserDetails>  getLoginUserDetails(int applicationId,
													   int environmentCategoryId);
	
	public LoginUserDetails getLoginIdDetails(LoginUserDetails loginUserDetails);
}
