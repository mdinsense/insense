package com.ensense.insense.core.utils;

public enum ExecutionStatus {
	    PENDING  (1, "Pending", "Pending"),
	    IN_PROGRESS(2, "In progress", "In progress"),
	    COMPLETE(3, "Completed", "Completed"),
	    //COMPARISON_NOT_APPLICABLE(4, "Comparison Not Applicable"),//DOn't user it
	    NOT_APPLICABLE(5, "Not Applicable", "Not Applicable"),
	    PAUSED(6, "Paused", "Pause"),
	    STOPPED(7, "Stopped", "Stop"),
	    RESTART(8, "Restart", "Restart"),
	    ERROR(9, "Error", "Error"),
	    FAILED(10, "Failed", "Failed")
	    ;


	    private final int statusCode;
	    private final String status;
	    private final String action;
	    
	    ExecutionStatus(int statusCode, String status, String action) {
	        this.statusCode = statusCode;
	        this.status = status;
	        this.action = action;
	    }
	    
	    public int getStatusCode() {
	        return this.statusCode;
	    }
	    
	    public String getStatus(){
	    	return this.status;
	    }
	    
	    
	    public String getAction() {
			return action;
		}

		public static ExecutionStatus getExecutionStatus(int statusCode) {
	        for (ExecutionStatus status : ExecutionStatus.values()) {
	            if (status.getStatusCode() == statusCode) {
	                return status;
	            }
	        }
	        // throw an IllegalArgumentException or return null
	        throw new IllegalArgumentException("The given number doesn't match any Status.");
	    }

		public static String getStatus(int statusCode) {
	        for (ExecutionStatus status : ExecutionStatus.values()) {
	            if (status.getStatusCode() == statusCode) {
	                return status.status;
	            }
	        }
	        // throw an IllegalArgumentException or return null
	        throw new IllegalArgumentException("The given number doesn't match any Status.");
	    }
		
		public static String getAction(int statusCode) {
	        for (ExecutionStatus status : ExecutionStatus.values()) {
	            if (status.getStatusCode() == statusCode) {
	                return status.action;
	            }
	        }
	        // throw an IllegalArgumentException or return null
	        throw new IllegalArgumentException("The given number doesn't match any Status.");
	    }
	    
}
