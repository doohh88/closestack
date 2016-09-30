package com.ssmksh.closestack.master;

import java.util.HashMap;

import com.ssmksh.closestack.util.Node;

import akka.actor.Address;
import akka.cluster.ClusterEvent.UnreachableMember;

public enum Resource {
	INSTANCE;
	private static HashMap<Address, Node> kvmNodes;
	private static HashMap<Address, Node> lxdNodes;	
	private static int CPU = 0;
	private static int RAM = 0;
	private static int DISK = 0;
	
	public static Resource getInstance(){
		kvmNodes = new HashMap<Address, Node>();
		lxdNodes = new HashMap<Address, Node>();
		return INSTANCE;
	}
	
	public static void put(Node node){
		CPU += node.getCPU();
		RAM += node.getRAM();
		DISK += node.getDISK();
		if(node.getRole().equals("kvm")){
			kvmNodes.put(node.getActorRef().path().address(), node);
			System.out.println("kvmNodes");
			System.out.println(kvmNodes);
		} else if(node.getRole().equals("lxd")){
			lxdNodes.put(node.getActorRef().path().address(), node);
			System.out.println("lxdNodes");
			System.out.println(lxdNodes);
		}				
	}
	
	public static void remove(UnreachableMember mUnreachable){
		Node node = kvmNodes.get(mUnreachable.member().address());		
		if(node == null){
			node = lxdNodes.get(mUnreachable.member().address());			
		}
		
		CPU -= node.getCPU();
		RAM -= node.getRAM();
		DISK -= node.getDISK();
		
		kvmNodes.remove(mUnreachable.member().address());
		lxdNodes.remove(mUnreachable.member().address());
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
