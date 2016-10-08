package com.ssmksh.closestack.network;

import org.springframework.stereotype.Service;

import com.ssmksh.closestack.C;
import com.ssmksh.closestack.dto.TellCommand;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.RoundRobinPool;

public enum Net {
	INSTANCE;
	
	public static Net getInstance(){
		return INSTANCE;
	}
	
	private Net(){
		init();
	}
	
	private ActorRef netActor = null;
	private ActorSelection broadcast = null;
	private ActorSelection master = null;
	private String hostIP = C.Url.MASTERSERVER;
	private String SystemName = "closestack";
	
	public void init(){
		Config conf = ConfigFactory.load("akkaWeb");
		final ActorSystem system = ActorSystem.create("akkaWeb", conf);
		netActor = system.actorOf(new RoundRobinPool(5).props(Props.create(NetActor.class)), "netActor");//thread 5°³ÀÌ¿ë
		System.out.println(netActor.path());
		broadcast = system.actorSelection("/user/netActor*");
		master = system.actorSelection("akka.tcp://" + SystemName + "@" + hostIP + ":2551/user/master");
		
		System.out.println("akka.tcp://" + SystemName + "@" + hostIP + ":2551/user/master");
		
	}
	
	public ActorRef getNetActor(){
		return netActor;
	}	 
	
	public ActorSelection getBroadcast() {
		return broadcast;
	}
	
	public ActorSelection getMaster() {
		return master;
	}	
}
