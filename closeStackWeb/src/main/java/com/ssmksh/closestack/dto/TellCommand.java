package com.ssmksh.closestack.dto;

import java.io.Serializable;

public class TellCommand<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String who; // master, web
	private String sendType; // ask, tell
	private String command;
	private T data;
	
	public TellCommand(String who, String sendType, String command, T data) {
		this.who = who;
		this.sendType = sendType;
		this.command = command;
		this.data = data;
	}

	public String getCommand() {
		return command;
	}
	
	public void setCommand(String command) {
		this.command = command;
	}

	public T getData() {
		return data;
	}

	public String getWho() {
		return who;
	}

	public String getSendType() {
		return sendType;
	}
}
