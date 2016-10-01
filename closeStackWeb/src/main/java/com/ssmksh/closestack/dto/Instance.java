package com.ssmksh.closestack.dto;

import java.io.Serializable;

import org.springframework.data.annotation.Id;

public class Instance implements Serializable{

	@Id
	private String id;
	private String name;
	private String userName;
	private String pw;
	private String os;
	private String ip;
	private String status;
	private String time;
	private Flavor flavor;
	
	

	public Instance(String userName, String name, String pw,String os,  Flavor flavor){
		this(userName,name, pw,os,"X","stop","0",flavor);
	}
	
	
	Instance(String userName, String name, String pw,String os, String ip, String status, String time, Flavor flavor){
		this.userName = userName;
		this.name = name;
		this.pw = pw;
		this.os = os;
		this.status = status;
		this.time = time;
		this.flavor = flavor;
	}
	
	public String getuserName() {
		return userName;
	}
	
	public String getName() {
		return name;
	}
	public String getPw() {
		return pw;
	}
	public String getOs() {
		return os;
	}
	public String getIp() {
		return ip;
	}
	public String getStatus() {
		return status;
	}
	public String getTime() {
		return time;
	}
	public Flavor getFlavor() {
		return flavor;
	}
	
}
