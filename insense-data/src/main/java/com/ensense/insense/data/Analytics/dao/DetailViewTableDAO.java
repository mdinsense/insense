package com.ensense.insense.data.analytics.dao;


import com.ensense.insense.data.analytics.entity.DetailedView;

import java.util.List;


public interface DetailViewTableDAO {
	
	public  boolean populateDetailedView(List<DetailedView> detailedView);
}
