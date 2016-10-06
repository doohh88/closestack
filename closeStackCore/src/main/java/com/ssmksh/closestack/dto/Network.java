package com.ssmksh.closestack.dto;

import java.io.Serializable;
import java.util.ArrayList;

import lombok.Data;

@Data
public class Network implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ArrayList<IP> ips;
}
