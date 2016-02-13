package com.ensense.insense.data.uitesting.entity.mintv4;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TestSuitRemoveTagOrTextXref")
public class TestSuitRemoveTagOrTextXref implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "tagOrTextXrefId")
	private int tagOrTextXrefId;

	@Column(name = "suitId")
	private int suitId;
	
	@Column(name = "tagOrText")
	private String tagOrText;

	@Column(name = "isTag")
	private boolean isTag;
	
	@Column(name = "isText")
	private boolean isText;

	public int getTagOrTextXrefId() {
		return tagOrTextXrefId;
	}

	public void setTagOrTextXrefId(int tagOrTextXrefId) {
		this.tagOrTextXrefId = tagOrTextXrefId;
	}

	public int getSuitId() {
		return suitId;
	}

	public void setSuitId(int suitId) {
		this.suitId = suitId;
	}

	public String getTagOrText() {
		return tagOrText;
	}

	public void setTagOrText(String tagOrText) {
		this.tagOrText = tagOrText;
	}

	public boolean isTag() {
		return isTag;
	}

	public void setTag(boolean isTag) {
		this.isTag = isTag;
	}

	public boolean isText() {
		return isText;
	}

	public void setText(boolean isText) {
		this.isText = isText;
	}

	@Override
	public String toString() {
		return "TestSuitRemoveTagOrTextXref [tagOrTextXrefId="
				+ tagOrTextXrefId + ", suitId=" + suitId + ", tagOrText="
				+ tagOrText + ", isTag=" + isTag + ", isText=" + isText + "]";
	}

}