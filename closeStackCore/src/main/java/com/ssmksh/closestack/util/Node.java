package com.ssmksh.closestack.util;

import java.io.Serializable;

import akka.actor.ActorRef;
import lombok.Data;

@Data
public class Node implements Serializable{
	private ActorRef actorRef;
	private int CPU; //MB
	private int RAM; //MB
	private int DISK;
	
	public Node(ActorRef actorRef) {
		this.actorRef = actorRef;
		//init();
	}
	
	void init(){
		this.CPU = getCPUfromNode();
		this.RAM = getRAMfromNode();
		this.DISK = getDISKfromNode();
	}
	
	private int getCPUfromNode(){
		String cpu = Util.exec("grep -c processor /proc/cpuinfo");
		return Integer.parseInt(cpu);
	}
	
	private int getRAMfromNode(){
		String ram = Util.exec("cat /proc/meminfo | grep MemTotal");
		int rst = Integer.parseInt(ram.replaceAll("[^0-9]", ""))/1024;
		return rst;
	}
	
	private int getDISKfromNode(){
		String disk = Util.exec("df -P | grep -v ^Filesystem | awk '{sum += $2} END { print sum/1024}'");
		return Integer.parseInt(disk);
	}
	
}
