package com.ssmksh.closestack.vm;

import lombok.Data;

@Data
public class LXD {
	private String name;
	private String IP;
	private String state;
	private int vCPU;
	private int RAM;
	private int DISK;
}
