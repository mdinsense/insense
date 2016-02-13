package com.ensense.insense.data.common.model;

public class IdNamePair {
	
	private int Id;
	private String name;
	
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "IdNamePair [Id=" + Id + ", name=" + name + "]";
	}
}
