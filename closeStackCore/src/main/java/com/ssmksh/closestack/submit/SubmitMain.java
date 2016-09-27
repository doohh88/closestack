package com.ssmksh.closestack.submit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;


public class SubmitMain {
	private static final Logger log = LoggerFactory.getLogger(SubmitMain.class);


	public static void main(String[] args) {
		Config conf = ConfigFactory.load("deploy");
		final ActorSystem system = ActorSystem.create("deploy", conf);
		final ActorRef submit = system.actorOf(Props.create(Submit.class), "submit");		
		submit.tell("hello workers", ActorRef.noSender());
	}
}
