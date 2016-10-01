package com.ssmksh.closestack.network;

import akka.actor.ActorRef;

public class UseExNet {
	public static void main(String[] args) {
		Net net = Net.getInstance();
		net.init();
		ActorRef netActor = net.getNetActor();
		netActor.tell("hello", ActorRef.noSender());

		//netActor.tell(CLASSNAME, ActorRef.noSender()); //class를 전달하고 싶은경우
		//"hello" -> netActor -> master
	}
}
