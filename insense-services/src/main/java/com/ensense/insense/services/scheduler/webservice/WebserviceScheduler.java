package com.ensense.insense.services.scheduler.webservice;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.cts.mint.common.utils.CommonUtils;
import com.cts.mint.common.utils.Constants.SCHEDULER;
import com.cts.mint.webservice.service.WebserviceTestingService;

@Service
public class WebserviceScheduler {

	@Autowired
	private WebserviceTestingService webserviceTestingService;
	
	@Autowired
	private MessageSource schedulerProperties;

	private static final Logger logger = Logger.getLogger(WebserviceScheduler.class);

	@Async
	@Scheduled(fixedDelayString = "${mint.scheduler.webservice.delaytime}")
	public void pickUpWSSchedules() {
		//logger.debug("Enter: pickUpWSSchedules");		
		try {
			if (! Boolean.parseBoolean(CommonUtils.getPropertyFromPropertyFile(
					schedulerProperties, SCHEDULER.WEBSERVICE_SCHEDULER_ENABLE))) {
				
				//Schedule Job is disabled. Do nothing.
				return;
			}
		} catch (Exception e) {
			logger.error("Unable to start the Webservice Scheduler.");
			e.printStackTrace();
		}
		
		// Pick up the current schedules and insert into
		// TASK_EXECUTION_STATUS_TABLE
		try {
			int schedulePickUp = webserviceTestingService.pickUpSchedules();
			if (schedulePickUp == 0) {
				//logger.info("No Webservice Scheduled/OnDemand Jobs found!!!.");
			}
		} catch (Exception e) {
			logger.error("Error while looking for the schedules.", e);
		}

		//logger.info("Exit: pickUpSchedules");
	}
	
}
