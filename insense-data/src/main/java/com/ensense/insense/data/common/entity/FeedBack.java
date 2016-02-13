package com.ensense.insense.data.common.entity;

import com.ensense.insense.data.common.utils.JsonReaderWriter;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * The persistent class for the menu database table.
 * 
 */
@Entity
@Table(name = "FeedBack")
public class FeedBack implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger
			.getLogger(FeedBack.class);
	
	@Id
	@GenericGenerator(name = "sequenceGen", strategy = "com.cts.mint.common.entity.SequenceTable") 
	@GeneratedValue(generator = "sequenceGen")
	@Column(name = "feedBackId")
	private int feedBackId;
	
	@Column(name = "userId")
	private int userId;
	
	@Column(name = "userEmailId")
	private String userEmailId;

	@Column(name = "menuId")
	private int menuId;
	
	//@Lob
	//@Column(name = "text", columnDefinition= "clob")
	@Column(name = "text")
	private String text;

	public int getFeedBackId() {
		return feedBackId;
	}

	public void setFeedBackId(int feedBackId) {
		this.feedBackId = feedBackId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserEmailId() {
		return userEmailId;
	}

	public void setUserEmailId(String userEmailId) {
		this.userEmailId = userEmailId;
	}

	public int getMenuId() {
		return menuId;
	}

	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public boolean setTextpath(String filecontent,String filePath) {
		JsonReaderWriter<String> jsonReaderWriter = new JsonReaderWriter<String>();
		boolean status = true;
		try {
			jsonReaderWriter.writeJsonObjectToFile(filecontent, filePath);
			this.text = filePath;
		} catch (Exception e) {
			status = false;
			logger.error("Exception while writing Feedback details to File");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
		}
		return status;
	}

	@Override
	public String toString() {
		return "FeedBack [feedBackId=" + feedBackId + ", userId=" + userId
				+ ", userEmailId=" + userEmailId + ", menuId=" + menuId
				+ ", text=" + text + "]";
	}


}