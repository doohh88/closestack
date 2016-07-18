package com.ssmksh.closestack.dto;

import java.io.Serializable;

public class User implements Serializable {
	private static final long serialVersionUID = -7667147479819193393L;
	private long id;
	private String userName;
	private String password;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
