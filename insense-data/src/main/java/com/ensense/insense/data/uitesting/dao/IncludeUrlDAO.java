package com.ensense.insense.data.uitesting.dao;

import com.ensense.insense.data.uitesting.entity.IncludeUrl;

import java.util.List;


public interface IncludeUrlDAO {

	List<IncludeUrl> getIncludeURLList();

	boolean addIncludeURL(IncludeUrl includeURL);

	IncludeUrl getIncludeURL(IncludeUrl includeURL);

	boolean deleteIncludeURLDetails(IncludeUrl includeURL);

	public List<IncludeUrl> getIncludeURLList(int applicationId, int environmentId);
}
