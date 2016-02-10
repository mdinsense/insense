package com.ensense.insense.services.scheduler;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.cts.mint.common.utils.CommonUtils;
import com.cts.mint.common.utils.Constants.SCHEDULER;
import com.cts.mint.reports.service.ScheduledService;

@Service
public class PickupScheduler {
	private static Logger logger = Logger
			.getLogger(PickupScheduler.class);
	@Autowired
	ScheduledService scheduledService;
	
	@Autowired
	private MessageSource schedulerProperties;
	
	@Scheduled(fixedDelayString = "${mint.scheduler.uischedule.pickup.delaytime}")
	public void findOndemandTests(){
		
		try {
			if (! Boolean.parseBoolean(CommonUtils.getPropertyFromPropertyFile(
					schedulerProperties, SCHEDULER.UI_PICKUP_SCHEDULER_ENABLE))) {
				
				//Schedule Job is disabled. Do nothing.
				return;
			}
		} catch (Exception e) {
			logger.error("Unable to start the UI Pickup Scheduler.");
			e.printStackTrace();
		}
		scheduledService.schedulePendingTestSuits();
	}
}
