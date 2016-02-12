package com.ensense.insense.data.utils;

public enum TaskStatus
{
	PENDING("PENDING", 1), RUNNING("RUNNING", 2), SUCCESS("SUCCESS", 3), FAILED("FAILED", 4), CANCELLED(
			"CANCELLED", 5), COMPLETED_WITH_ERRORS("Completed with Errors/Warnings", 6);

	private Integer statusCode;
	private String status;

	private TaskStatus(String status, Integer statusCode)
	{
		this.status = status;
		this.statusCode = statusCode;
	}

	public Integer getStatusCode()
	{
		return statusCode;
	}

	public String getStatus()
	{
		return status;
	}

	public static TaskStatus getTaskStatus(Integer taskStatusCode)
	{
		for (TaskStatus task : TaskStatus.values())
		{
			if (task.getStatusCode() == taskStatusCode)
			{
				return task;
			}
		}

		throw new RuntimeException("Invalid task status code " + taskStatusCode);
	}
}
