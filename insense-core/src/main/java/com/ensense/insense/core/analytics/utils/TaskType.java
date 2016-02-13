package com.ensense.insense.core.analytics.utils;

public enum TaskType
{
	OPRA_TASK("OPERA reset", 1), OPRA_GROUP_TYPE("OPERA bulk reset", 2), 	UD_RSA_TASK("RSA/ATOM", 3), RETIREMENT_AT_WORK_OPRA_TASK("R@W OPRA", 4), RETIREMENT_AT_WORK_OPRA_GROUP_TASK("R@W OPRA group", 5), MVC_OPRA_RESET("MVC OPRA", 6), MVC_OPRA_GROUP_TYPE("MVC OPERA bulk reset", 7);

	private Integer taskType;
	private String taskdesc;

	private TaskType(String taskdesc, Integer taskType)
	{
		this.taskdesc = taskdesc;
		this.taskType = taskType;
	}

	public Integer getTaskType()
	{
		return taskType;
	}
	
	public String getTaskDesc()
	{
		return taskdesc;
	}
	
	public static TaskType getTaskType(Integer taskType)
	{
		for(TaskType task : TaskType.values())
		{
			if (task.getTaskType() == taskType)
			{
				return task;
			}
		}

		throw new RuntimeException("Invalid task type code " + taskType);
	}
}
