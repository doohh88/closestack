package com.ssmksh.closestack.network;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import akka.actor.ActorRef;
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
	
	void init(){
		Config conf = ConfigFactory.load("akkaWeb");
		final ActorSystem system = ActorSystem.create("akkaWeb", conf);
		netActor = system.actorOf(new RoundRobinPool(5).props(Props.create(NetActor.class)), "netActor");//thread 5°³ÀÌ¿ë
	}
	
	ActorRef getNetActor(){
		return netActor;
	}	 
	
	

}
