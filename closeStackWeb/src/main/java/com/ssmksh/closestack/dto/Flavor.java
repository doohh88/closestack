package com.ssmksh.closestack.dto;

import org.springframework.data.annotation.Id;

public class Flavor {
	
	@Id
	private String name;
	private int vCpus;
	private int disk;
	private int ram;
	
	public Flavor(String name, int vCpus, int disk, int ram){
		this.name = name;
		this.vCpus = vCpus;
		this.disk = disk;
		this.ram = ram;
	}
	
	
	public String getName() {
		return name;
	}
	public int getvCpus() {
		return vCpus;
	}
	public int getDisk() {
		return disk;
	}
	public int getRam() {
		return ram;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setvCpus(int vCpus) {
		this.vCpus = vCpus;
	}
	public void setDisk(int disk) {
		this.disk = disk;
	}
	public void setRam(int ram) {
		this.ram = ram;
	}

}
