package com.ssmksh.closestack.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IP implements Serializable{
   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
String ip;
   boolean used;
   String hostIp;
}