package com.ssmksh.closestack.util;

import java.io.Serializable;

import akka.actor.ActorRef;
import lombok.Data;

@Data
public class Node implements Serializable {
	private ActorRef actorRef;
	private int CPU; // MB
	private int RAM; // MB
	private int DISK;
	private int usedCPU = 0;
	private int usedRAM = 0;
	private int usedDISK = 0;

	public Node(ActorRef actorRef) {
		this.actorRef = actorRef;
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
			return Integer.parseInt(rst);
		}
	}

}
