package com.doohh.akkaClustering.util;

import akka.actor.ActorRef;
import akka.actor.Address;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Node {
	//Address address;
	ActorRef actorRef;
	boolean proc;
}
