package com.ssmksh.closestack.master;

import java.util.HashMap;

import com.ssmksh.closestack.util.Node;

import akka.actor.Address;
import akka.cluster.ClusterEvent.UnreachableMember;
import lombok.Data;

public enum Resource {
	INSTANCE;
	private static HashMap<Address, Node> nodes;
	
	public static Resource getInstance(){
		nodes = new HashMap<Address, Node>();
		return INSTANCE;
	}
	
	public static void put(Node node){
		nodes.put(node.getActorRef().path().address(), node);
	}
	
	public static void remove(UnreachableMember mUnreachable){
		nodes.remove(mUnreachable.member().address());
	}
	
	public static HashMap<Address, Node> getNodes() {
		return nodes;
	}
}
