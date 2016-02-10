package uiadmin.form.schedule;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.cts.mint.uitesting.entity.AppEnvEnvironmentCategoryXref;
import com.cts.mint.uitesting.entity.Application;
import com.cts.mint.uitesting.entity.ApplicationConfig;
import com.cts.mint.uitesting.entity.ApplicationModuleXref;
import com.cts.mint.uitesting.entity.Environment;
import com.cts.mint.uitesting.entity.ExcludeUrl;
import com.cts.mint.uitesting.entity.IncludeUrl;
import com.cts.mint.uitesting.entity.LoginUserDetails;
import com.cts.mint.uitesting.entity.Reports;
import com.cts.mint.uitesting.entity.Results;
import com.cts.mint.uitesting.entity.Schedule;
import com.cts.mint.uitesting.entity.ScheduleExecution;
import com.cts.mint.uitesting.entity.SuitBrokenReportsXref;
import com.cts.mint.uitesting.entity.SuitTextImageXref;

public class ScheduleExecutionDetail implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int suitId;
	private AppEnvEnvironmentCategoryXref appEnvEnvironmentCategoryXref = new AppEnvEnvironmentCategoryXref();
	private Schedule schedule = new Schedule();
	private ScheduleExecution scheduleExecution;
	private Reports report = new Reports();
	private Results results = new Results();
	private List<SuitTextImageXref> suitTextImageList;
	private List<SuitBrokenReportsXref> suitBrokenTypeXref;
	
	public List<SuitBrokenReportsXref> getSuitBrokenTypeXref() {
		return suitBrokenTypeXref;
	}
	public void setSuitBrokenTypeXref(List<SuitBrokenReportsXref> suitBrokenTypeXref) {
		this.suitBrokenTypeXref = suitBrokenTypeXref;
	}
	
	public int getSuitId() {
		return suitId;
	}
	public void setSuitId(int suitId) {
		this.suitId = suitId;
	}
	public AppEnvEnvironmentCategoryXref getAppEnvEnvironmentCategoryXref() {
		return appEnvEnvironmentCategoryXref;
	}
	public void setAppEnvEnvironmentCategoryXref(
			AppEnvEnvironmentCategoryXref appEnvEnvironmentCategoryXref) {
		this.appEnvEnvironmentCategoryXref = appEnvEnvironmentCategoryXref;
	}
	public Schedule getSchedule() {
		return schedule;
	}
	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
	}
	public Reports getReport() {
		return report;
	}
	public void setReport(Reports report) {
		this.report = report;
	}
	public Results getResults() {
		return results;
	}
	public void setResults(Results results) {
		this.results = results;
	}
	
	public ScheduleExecution getScheduleExecution() {
		return scheduleExecution;
	}
	public void setScheduleExecution(ScheduleExecution scheduleExecution) {
		this.scheduleExecution = scheduleExecution;
	}
	public List<SuitTextImageXref> getSuitTextImageList() {
		
		if ( null == suitTextImageList ){
			suitTextImageList = new ArrayList<SuitTextImageXref>();
		}
		return suitTextImageList;
	}
	public void setSuitTextImageList(List<SuitTextImageXref> suitTextImageList) {
		this.suitTextImageList = suitTextImageList;
	}
	@Override
	public String toString() {
		return "ScheduleExecutionDetail [suitId=" + suitId
				+ ", appEnvEnvironmentCategoryXref="
				+ appEnvEnvironmentCategoryXref + ", schedule=" + schedule
				+ ", scheduleExecution=" + scheduleExecution + ", report="
				+ report + ", results=" + results + ", suitTextImageList="
				+ suitTextImageList + ", suitBrokenTypeXref="
				+ suitBrokenTypeXref + "]";
	}
}
