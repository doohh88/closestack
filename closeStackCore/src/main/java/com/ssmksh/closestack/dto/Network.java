package com.ssmksh.closestack.dto;

import java.io.Serializable;
import java.util.ArrayList;

import lombok.Data;

@Data
public class Network implements Serializable {
	ArrayList<IP> ips;
}
