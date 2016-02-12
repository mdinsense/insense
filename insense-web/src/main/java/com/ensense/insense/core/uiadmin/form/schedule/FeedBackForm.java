package com.ensense.insense.core.uiadmin.form.schedule;


public class FeedBackForm {

	private String menuId;
	private String comments;
	private Integer feedBackId;
	public String getMenuId() {
		return menuId;
	}
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public Integer getFeedBackId() {
		return feedBackId;
	}
	public void setFeedBackId(Integer feedBackId) {
		this.feedBackId = feedBackId;
	}
	@Override
	public String toString() {
		return "FeedBackForm [menuId=" + menuId + ", comments=" + comments
				+ ", feedBackId=" + feedBackId + "]";
	}
	
}
