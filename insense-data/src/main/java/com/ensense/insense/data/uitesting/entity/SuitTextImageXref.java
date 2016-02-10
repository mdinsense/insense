package com.ensense.insense.data.uitesting.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SuitTextImageXref")
public class SuitTextImageXref implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "textOrImageId")
	private int textOrImageId;

	@Column(name = "suitId")
	private int suitId;
	
	@Column(name = "textOrImageName")
	private String textOrImageName;

	@Column(name = "isImage")
	private boolean isImage;
	
	@Column(name = "isText")
	private boolean isText;

	public int getTextOrImageId() {
		return textOrImageId;
	}

	public void setTextOrImageId(int textOrImageId) {
		this.textOrImageId = textOrImageId;
	}

	public int getSuitId() {
		return suitId;
	}

	public void setSuitId(int suitId) {
		this.suitId = suitId;
	}

	public String getTextOrImageName() {
		return textOrImageName;
	}

	public void setTextOrImageName(String textOrImageName) {
		this.textOrImageName = textOrImageName;
	}

	public boolean isImage() {
		return isImage;
	}

	public void setImage(boolean isImage) {
		this.isImage = isImage;
	}

	public boolean isText() {
		return isText;
	}

	public void setText(boolean isText) {
		this.isText = isText;
	}

	@Override
	public String toString() {
		return "SuitTextImageXref [textOrImageId=" + textOrImageId
				+ ", suitId=" + suitId + ", textOrImageName=" + textOrImageName
				+ ", isImage=" + isImage + ", isText=" + isText + "]";
	}
}