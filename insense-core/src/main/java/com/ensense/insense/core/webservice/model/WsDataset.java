package com.ensense.insense.core.webservice.model;

import java.util.Map;

public class WsDataset {
	
	private String dataSet;
	private Map<String, String> parameterValuesMap;
	
	public String getDataSet() {
		return dataSet;
	}
	public void setDataSet(String dataSet) {
		this.dataSet = dataSet;
	}
	public Map<String, String> getParameterValuesMap() {
		return parameterValuesMap;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dataSet == null) ? 0 : dataSet.hashCode());
		result = prime
				* result
				+ ((parameterValuesMap == null) ? 0 : parameterValuesMap
						.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		boolean result = true;
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WsDataset other = (WsDataset) obj;
		if (dataSet == null) {
			if (other.dataSet != null)
				result =  false;
		} else if (!dataSet.equals(other.dataSet))
			result = false;
		if (parameterValuesMap == null) {
			if (other.parameterValuesMap != null)
				result = false;
		} else if (!parameterValuesMap.equals(other.parameterValuesMap))
			result=  false;
		return result;
	}
	public void setParameterValuesMap(Map<String, String> parameterValuesMap) {
		this.parameterValuesMap = parameterValuesMap;
	}

}
