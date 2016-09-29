package com.ssmksh.closestack.master;

import java.util.HashMap;

import com.ssmksh.closestack.util.Node;

import akka.actor.Address;
import akka.cluster.ClusterEvent.UnreachableMember;

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
	
	public static void remove(UnreachableMember mUnreachable){
		Node node = nodes.get(mUnreachable.member().address());
		CPU -= node.getCPU();
		RAM -= node.getRAM();
		DISK -= node.getDISK();
		nodes.remove(mUnreachable.member().address());
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
