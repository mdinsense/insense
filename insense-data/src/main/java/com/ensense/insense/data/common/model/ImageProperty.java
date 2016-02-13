package com.ensense.insense.data.common.model;

import java.io.Serializable;

public class ImageProperty implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String imageName;
	long size;
	String imageUrl;
	
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((imageUrl == null) ? 0 : imageUrl.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ImageProperty other = (ImageProperty) obj;
		if (imageUrl == null) {
			if (other.imageUrl != null)
				return false;
		} else if (!imageUrl.equals(other.imageUrl))
			return false;

		return true;
	}
	@Override
	public String toString() {
		return "ImageProperty [imageName=" + imageName + ", size=" + size
				+ ", imageUrl=" + imageUrl + "]";
	}
	
	
}
