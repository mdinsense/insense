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
public class FilePickupScheduler {
	private static Logger logger = Logger.getLogger(FilePickupScheduler.class);
	@Autowired
	ScheduledService scheduledService;

	@Autowired
	private MessageSource schedulerProperties;

	@Scheduled(fixedDelayString = "${mint.scheduler.filescheduler.delaytime}")
	public void findFileSchedularTests() {
		try {
			if (! Boolean.parseBoolean(CommonUtils.getPropertyFromPropertyFile(
					schedulerProperties, SCHEDULER.FILE_SCHEDULER_ENABLE))) {
				
				//Schedule Job is disabled. Do nothing.
				return;
			}
		} catch (Exception e) {
			logger.error("Unable to start the scheduler");
			e.printStackTrace();
		}
		
		//Schedule Job is Enabled. Continue with the schedule Job.
		try{
			scheduledService.findFileSchedularTests();
		}catch(Exception e){
			
		}
	}
}
