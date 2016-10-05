package com.ssmksh.closestack.util;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IP {
	String addr;
	boolean used;
	Node node;	
}
