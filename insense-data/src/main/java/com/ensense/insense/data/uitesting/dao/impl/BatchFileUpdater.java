package com.ensense.insense.data.uitesting.dao.impl;

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

import com.cts.mint.common.utils.CommonUtils;
import com.cts.mint.common.utils.Constants;
import com.cts.mint.uitesting.entity.ScheduleExecution;
import com.cts.mint.uitesting.entity.Suit;
import com.cts.mint.uitesting.service.UiTestingService;

@Component
public class BatchFileUpdater implements PostDeleteEventListener,
		PostInsertEventListener, PostUpdateEventListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private SessionFactory sessionFactory;

	@Inject
	UiTestingService uiTestingService;

	@Inject
	private MessageSource schedulerProperties;

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
		if (this.isBatchRelatedEntity(event.getEntity()) && batchJobEnabled()) {
			uiTestingService.syncBatchFileData();
		}
	}

	@Override
	public void onPostInsert(PostInsertEvent event) {
		if (this.isBatchRelatedEntity(event.getEntity()) && batchJobEnabled()) {
			uiTestingService.syncBatchFileData();
		}
	}

	@Override
	public void onPostDelete(PostDeleteEvent event) {
		if (this.isBatchRelatedEntity(event.getEntity()) && batchJobEnabled()) {
			uiTestingService.syncBatchFileData();
		}
	}

	private boolean isBatchRelatedEntity(Object entity) {
		boolean batchData = false;
		if (entity.getClass() == Suit.class
				|| entity.getClass() == ScheduleExecution.class) {
			batchData = true;
		}
		return batchData;
	}

	private boolean batchJobEnabled() {
		boolean isEnabled = false;
		try {
			isEnabled = Boolean.parseBoolean(CommonUtils
					.getPropertyFromPropertyFile(schedulerProperties,
							Constants.BATCH_FILE_ENABLED_KEY));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return isEnabled;
	}

}
