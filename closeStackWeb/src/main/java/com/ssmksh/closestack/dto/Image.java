package com.ssmksh.closestack.dto;

import org.springframework.data.annotation.Id;

public class Image {

	@Id
	private String name;
	private String type;
	private String status;
	private boolean share;
	private boolean protect;
	private String format;

	public Image(String name, String type, String status, boolean share, boolean protect, String format) {
		this.name = name;
		this.type = type;
		this.status = status;
		this.share = share;
		this.protect = protect;
		this.format = format;
	}
	
	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public String getStatus() {
		return status;
	}

	public boolean isShare() {
		return share;
	}

	public boolean isProtect() {
		return protect;
	}

	public String getFormat() {
		return format;
	}



	
}
