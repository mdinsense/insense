package com.ensense.insense.data.utils;

public class SerializeStatus {
	private int noOfUrlsSerialized;
	private boolean stopOrPauseCalled;
	
	public int getNoOfUrlsSerialized() {
		return noOfUrlsSerialized;
	}
	public void setNoOfUrlsSerialized(int noOfUrlsSerialized) {
		this.noOfUrlsSerialized = noOfUrlsSerialized;
	}
	public boolean isStopOrPauseCalled() {
		return stopOrPauseCalled;
	}
	public void setStopOrPauseCalled(boolean stopOrPauseCalled) {
		this.stopOrPauseCalled = stopOrPauseCalled;
	}
	
	@Override
	public String toString() {
		return "SerializeStatus [noOfUrlsSerialized=" + noOfUrlsSerialized
				+ ", stopOrPauseCalled=" + stopOrPauseCalled + "]";
	}
}
