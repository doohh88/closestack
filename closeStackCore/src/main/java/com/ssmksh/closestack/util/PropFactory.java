package com.ssmksh.closestack.util;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ssmksh.closestack.master.MasterMain;

public class PropFactory {
	Logger log = LoggerFactory.getLogger(PropFactory.class);

	private static PropFactory instance;
	private static Properties props;
	String[] ipList;
	String[] seedList;
	String seedConf;
	ArrayList ipPool = new ArrayList<IP>(); 

	private PropFactory() {	}

	public static PropFactory getInstance() {
		if (instance == null) {
			instance = new PropFactory();
			props = new Properties();
			try {
				String propFile = System.getProperty("user.dir");
				int i = propFile.lastIndexOf("/bin");
				if(i != -1){
					propFile = propFile.substring(0, i);
				}
				propFile += "/conf/config.properties";
				FileInputStream fis = new FileInputStream(propFile);
				props.load(new java.io.BufferedInputStream(fis));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return instance;
	}

	public Properties getProperties() {
		return props;
	}

	private void setSeedList(String role) {
		String seedNodes = props.getProperty("seed-nodes");
		if (seedNodes == null) {
			log.info("if you have seed-nodes, please input seed-nodes IP in $CLOSESTACK_HOME/conf/config.properties");
			seedNodes = MasterMain.hostIP;
		} 		
		seedList = seedNodes.split(",");
	}

	public String[] getSeedList(String role) {
		return seedList;
	}

	public String getSeedConf(String role) {
		setSeedList(role);
		String seedNodes = "[";
		String pad = "\"akka.tcp://" + MasterMain.systemName + "@";
		for (String s : seedList) {
			seedNodes += pad + s + ":" + 2551 + "\", ";
		}
		seedNodes = seedNodes.substring(0, seedNodes.length() - 2);
		seedNodes += "]";
		return seedNodes;
	}
	
	public void setIpList() {
		String IPs = props.getProperty("ip-pool");
		ipList = IPs.split(",");
		for(String ip : ipList){
			ipPool.add(new IP(ip, false, null));
		}		
	}
	
	public String[] getIpList() {
		return ipList;
	}
	
	public ArrayList<IP> getIpPool() {
		setIpList();
		return ipPool;
	}
}
