package com.cytx.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "uuidandname")
public class UuidAndName {
	@Column(name = "id", isId = true)
	private int id;
	
	@Column(name = "uuid")
	private String uuid;
	
	@Column(name = "name")
	private String name;
	
	public UuidAndName(){
		
	}
	
	public UuidAndName(String name, String uuid) {
		this.uuid = uuid;
		this.name = name;
	}
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
}
