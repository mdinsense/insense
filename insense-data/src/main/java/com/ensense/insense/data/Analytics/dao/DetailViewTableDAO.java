package com.ensense.insense.data.analytics.dao;


import java.util.List;

import com.cts.mint.analytics.entity.DetailedView;


public interface DetailViewTableDAO {
	
	public  boolean populateDetailedView(List<DetailedView> detailedView);
}
