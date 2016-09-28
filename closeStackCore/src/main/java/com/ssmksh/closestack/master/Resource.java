package com.ssmksh.closestack.master;

import java.util.HashMap;

import com.ssmksh.closestack.util.Node;

import akka.actor.Address;
import akka.cluster.ClusterEvent.MemberRemoved;

public enum Resource {
	INSTANCE;
	private static HashMap<Address, Node> nodes;
	private static int CPU = 0;
	private static int RAM = 0;
	private static int DISK = 0;
	
	public static Resource getInstance(){
		nodes = new HashMap<Address, Node>();
		return INSTANCE;
	}
	
	public static void put(Node node){
		CPU += node.getCPU();
		RAM += node.getRAM();
		DISK += node.getDISK();
		nodes.put(node.getActorRef().path().address(), node);		
	}
	
	public static void remove(MemberRemoved mRemoved){
		Node node = nodes.get(mRemoved.member().address());
		CPU -= node.getCPU();
		RAM -= node.getRAM();
		DISK -= node.getDISK();
		nodes.remove(mRemoved.member().address());
	}
	
	public static HashMap<Address, Node> getNodes() {
		return nodes;
	}
	
	public static int getCPU() {
		return CPU;
	}
	
	public static int getRAM() {
		return RAM;
	}
	
	public static int getDISK() {
		return DISK;
	}
}
