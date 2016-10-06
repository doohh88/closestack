package com.ssmksh.closestack.dto;

import java.io.Serializable;

public class Instance implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String userName;
	private String pw;
	private String os;
	private String type;
	private String ip;
	private String status;
	private String time;
	private Flavor flavor;

	public Instance(String userName, String name, String pw, String os, String type, String ip, String status,
			String time, Flavor flavor) {
		this.userName = userName;
		this.name = name;
		this.pw = pw;
		this.os = os;
		this.type = type;
		this.ip = ip;
		this.status = status;
		this.time = time;
		this.flavor = flavor;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getUserName() {
		return userName;
	}

	public String getPw() {
		return pw;
	}

	public String getOs() {
		return os;
	}

	public String getType() {
		return type;
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

	public void setIp(String ip) {
		this.ip = ip;
	}

}