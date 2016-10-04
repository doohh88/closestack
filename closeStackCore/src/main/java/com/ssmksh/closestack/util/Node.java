package com.ssmksh.closestack.util;

import java.io.Serializable;

import com.ssmksh.closestack.dto.Flavor;

import akka.actor.ActorRef;
import lombok.Data;

@Data
public class Node implements Serializable {
	private ActorRef actorRef;
	private String role;
	private int CPU; // MB	
	private int RAM; // MB
	private int DISK;
	private int usedCPU = 0;
	private int usedRAM = 0;
	private int usedDISK = 0;
	private int restCPU = 0;
	private int restRAM = 0;
	private int restDISK = 0;

	public Node(ActorRef actorRef, String role) {
		this.actorRef = actorRef;
		this.role = role;
		init();
	}

	void init() {
		this.CPU = getCPUfromNode();
		this.RAM = getRAMfromNode();
		this.DISK = getDISKfromNode();
	}

	private int getCPUfromNode() {
		String rst = Util.exec("grep -c processor /proc/cpuinfo");
		if (rst == null) {
			return 0;
		} else {
			return Integer.parseInt(rst);
		}
	}

	private int getRAMfromNode() {
		String rst = Util.exec("cat /proc/meminfo | grep MemTotal");
		if (rst == null) {
			return 0;
		} else {
			return Integer.parseInt(rst.replaceAll("[^0-9]", "")) / 1024;
		}
	}

	private int getDISKfromNode() {
		String rst = Util.exec("./disk.sh");
		if (rst == null) {
			return 0;
		} else {
			return (int) Double.parseDouble(rst);
		}
	}
	
	public boolean canSave(Flavor flavor){
		restCPU = CPU - usedCPU;
		restRAM = RAM - usedRAM;
		restDISK = DISK - usedDISK;
		
		if(flavor.getvCpus() > restCPU || flavor.getRam() > restRAM || flavor.getDisk() > restDISK)
			return false;
				
		return true;
	}
}
