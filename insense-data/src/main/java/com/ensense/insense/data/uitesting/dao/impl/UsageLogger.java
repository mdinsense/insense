package com.ensense.insense.data.uitesting.dao.impl;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.hibernate.SessionFactory;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.event.spi.PostDeleteEvent;
import org.hibernate.event.spi.PostDeleteEventListener;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostInsertEventListener;
import org.hibernate.event.spi.PostUpdateEvent;
import org.hibernate.event.spi.PostUpdateEventListener;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import com.cts.mint.common.entity.UsageReport;
import com.cts.mint.common.entity.Users;
import com.cts.mint.common.service.HomeService;
import com.cts.mint.common.service.UserService;
import com.cts.mint.common.utils.Constants.Application;
import com.cts.mint.common.utils.Constants.EnvironmentCategoryENUM;
import com.cts.mint.common.utils.Constants.SolutionType;
import com.cts.mint.common.utils.UIFunctionalityType;
import com.cts.mint.miscellaneous.entity.MvcOpraTaskScheduleDetail;
import com.cts.mint.miscellaneous.entity.TaskScheduleDetail;
import com.cts.mint.reports.service.ScheduledService;
import com.cts.mint.uitesting.entity.Schedule;
import com.cts.mint.uitesting.entity.ScheduleExecution;
import com.cts.mint.uitesting.entity.Suit;
import com.cts.mint.webservice.entity.WSExecutionStatus;
import com.cts.mint.webservice.entity.WebserviceSuite;
import com.cts.mint.webservice.service.WebserviceTestingService;

@Component
public class UsageLogger implements PostDeleteEventListener,
		PostInsertEventListener, PostUpdateEventListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private SessionFactory sessionFactory;

	@Inject
	HomeService homeService;

	@Inject
	private MessageSource schedulerProperties;

	@Inject
	private ScheduledService scheduledService;

	@Inject
	private UserService userService;
	
	@Inject
	private WebserviceTestingService webserviceTestingService;

	@PostConstruct
	public void registerListeners() {
		EventListenerRegistry registry = ((SessionFactoryImpl) sessionFactory)
				.getServiceRegistry().getService(EventListenerRegistry.class);
		registry.getEventListenerGroup(EventType.POST_COMMIT_INSERT)
				.appendListener(this);
		registry.getEventListenerGroup(EventType.POST_COMMIT_UPDATE)
				.appendListener(this);
		registry.getEventListenerGroup(EventType.POST_COMMIT_DELETE)
				.appendListener(this);
	}

	@Override
	public void onPostUpdate(PostUpdateEvent event) {
		this.logUsageEvent(event.getEntity(), false);
	}

	@Override
	public void onPostInsert(PostInsertEvent event) {
		this.logUsageEvent(event.getEntity(), true);
	}

	@Override
	public void onPostDelete(PostDeleteEvent event) {
		this.logUsageEvent(event.getEntity(), false);
	}

	private void logUsageEvent(Object entity, boolean isInsert) {
		try {
			if (entity instanceof ScheduleExecution) {
				this.logUiTestingUsage((ScheduleExecution) entity, isInsert);
			} else if (entity instanceof TaskScheduleDetail) {
				this.logOpraUsage((TaskScheduleDetail) entity, isInsert);
			} else if (entity instanceof MvcOpraTaskScheduleDetail) {
				this.logMvcOpraUsage((MvcOpraTaskScheduleDetail) entity,
						isInsert);
			} else if(entity instanceof WSExecutionStatus) {
				this.logWebserviceUsage((WSExecutionStatus) entity, isInsert);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void logUiTestingUsage(ScheduleExecution scheduleExecution,
			boolean isInsert) throws Exception {
		UsageReport usageReport = new UsageReport();
		if (isInsert) {
			Schedule schedule = scheduledService.getSchedule(scheduleExecution
					.getScheduleId());
			Suit suit = homeService.getSavedSuits(schedule.getSuitId());
			Users user = userService.getMintUserById(schedule.getUserId());
			usageReport.setApplicationId(suit.getApplicationId());
			usageReport.setEnvironmentId(suit.getEnvironmentCategoryId());
			usageReport.setUserId(schedule.getUserId());
			usageReport.setGroupId(user.getGroupId());
			usageReport.setReferenceId(scheduleExecution
					.getScheduleExecutionId());
			usageReport.setSolutionTypeId(SolutionType.UI_TESTING.getSolutionTypeId());
			usageReport.setNotes(schedule.getNotes());
			usageReport.setFunctionalityTypeId(this.getFucntionalityIdForUiTesting(suit.getType()));
		} else {
			usageReport = homeService.getUsageReport(scheduleExecution
					.getScheduleExecutionId());
		}
		this.saveOrUpdateUsageReport(usageReport, isInsert);
	}

	private void logOpraUsage(TaskScheduleDetail taskScheduleDetail,
			boolean isInsert) throws Exception {
		UsageReport usageReport = new UsageReport();
		if (isInsert) {
			Users user = userService
					.getMintUser(taskScheduleDetail.getUserId());
			usageReport.setApplicationId(Application.UD_OPRA.getApplicationId());
			usageReport.setEnvironmentId(EnvironmentCategoryENUM.PROD_FIX.getEnvironmentCategoryId());
			usageReport.setUserId(user.getUserId());
			usageReport.setGroupId(user.getGroupId());
			usageReport.setReferenceId(taskScheduleDetail
					.getTaskScheduleDetailId());
			usageReport.setSolutionTypeId(SolutionType.MISCELANEOUS.getSolutionTypeId());
			usageReport.setFunctionalityTypeId(UIFunctionalityType.OPRA_RESET.getFunctionalityTypeId());
		} else {
			usageReport = homeService.getUsageReport(taskScheduleDetail
					.getTaskScheduleDetailId());
		}
		this.saveOrUpdateUsageReport(usageReport, isInsert);
	}

	private void logMvcOpraUsage(
			MvcOpraTaskScheduleDetail mvcOpraTaskScheduleDetail,
			boolean isInsert) throws Exception {
		UsageReport usageReport = new UsageReport();
		if (isInsert) {
			Users user = userService.getMintUser(mvcOpraTaskScheduleDetail
					.getUserId());
			usageReport.setApplicationId(Application.MVC_OPRA.getApplicationId());
			usageReport.setEnvironmentId(EnvironmentCategoryENUM.PROD_FIX.getEnvironmentCategoryId());
			usageReport.setUserId(user.getUserId());
			usageReport.setGroupId(user.getGroupId());
			usageReport.setReferenceId(mvcOpraTaskScheduleDetail
					.getTaskScheduleDetailId());
			usageReport.setSolutionTypeId(SolutionType.MISCELANEOUS.getSolutionTypeId());
			usageReport.setFunctionalityTypeId(UIFunctionalityType.MVC_OPRA_RESET.getFunctionalityTypeId());
		} else {
			usageReport = homeService.getUsageReport(mvcOpraTaskScheduleDetail
					.getTaskScheduleDetailId());
		}
		this.saveOrUpdateUsageReport(usageReport, isInsert);
	}
	
	private void logWebserviceUsage(WSExecutionStatus entity, boolean isInsert) throws Exception {
		UsageReport usageReport = new UsageReport();
		if (isInsert) {
			WebserviceSuite wsSuit = webserviceTestingService.getWebserviceSuite(entity.getWebserviceSuiteId());
			Users user = userService.getMintUserById(wsSuit.getUserId());
			usageReport.setApplicationId(entity.getServiceId());
			usageReport.setEnvironmentId(wsSuit.getEnvironmentId());
			usageReport.setUserId(user.getUserId());
			usageReport.setGroupId(user.getGroupId());
			usageReport.setReferenceId(entity.getExecutionStatusRefId());
			usageReport.setSolutionTypeId(SolutionType.WEBSERVICE.getSolutionTypeId());
			usageReport.setFunctionalityTypeId(UIFunctionalityType.WEBSERVICE_TESTING.getFunctionalityTypeId());
		} else {
			usageReport = homeService.getUsageReport(entity
					.getExecutionStatusRefId());
		}
		this.saveOrUpdateUsageReport(usageReport, isInsert);
	}


	private void saveOrUpdateUsageReport(UsageReport usageReport,
			boolean isInsert) throws Exception {
		if (isInsert) {
			usageReport.setStartDateTime(new Date());
			usageReport.setEndDateTime(new Date());
			homeService.saveUsageReport(usageReport);
		} else {
			usageReport.setEndDateTime(new Date());
			homeService.updateUsageReport(usageReport);
		}
	}
	
	private int getFucntionalityIdForUiTesting(String functionName) {
		int functionalityId = 0;
		for(UIFunctionalityType uType : UIFunctionalityType.values()) {
			if(uType.getFunctionalityName().contains(functionName)) {
				functionalityId = uType.getFunctionalityTypeId();
			}
		}
		return functionalityId;
	}
}
