package com.ssmksh.closestack.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Overall implements Serializable {
	private int cpu;
	private int ram;
	private int disk;
	private int numIp;
}
