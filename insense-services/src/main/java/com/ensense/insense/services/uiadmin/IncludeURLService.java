package com.ensense.insense.services.uiadmin;

import java.util.List;

import com.cts.mint.uitesting.entity.IncludeUrl;

public interface IncludeURLService {

	List<IncludeUrl> getIncludeURLList();

	boolean addIncludeURL(IncludeUrl includeURL);

	IncludeUrl getIncludeURL(IncludeUrl includeURL);

	boolean deleteIncludeURLDetails(IncludeUrl includeURL);
	public List<IncludeUrl> getIncludeURLList(int applicationId, int environmentId);
}
