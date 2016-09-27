package com.ssmksh.closestack.worker;

import java.util.HashMap;

import com.ssmksh.closestack.master.Master;
import com.ssmksh.closestack.util.Node;

import akka.actor.Address;
import akka.actor.UntypedActor;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent.MemberUp;
import akka.cluster.Member;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import lombok.Getter;

@Getter
public class Worker extends UntypedActor {
	public static final String REGISTRATION_TO_WORKER = "Worker registrate the master";
	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	Cluster cluster = Cluster.get(getContext().system());
	HashMap<Address, Node> masters = new HashMap<Address, Node>();

	// subscribe to cluster changes, MemberUp
	@Override
	public void preStart() {
		cluster.subscribe(getSelf(), MemberUp.class);
	}

	// re-subscribe when restart
	@Override
	public void postStop() {
		cluster.unsubscribe(getSelf());
	}

	@Override
	public void onReceive(Object message) throws Throwable {
		if (message.equals(Master.REGISTRATION_TO_MASTER)) {
			getContext().watch(getSender());
			masters.put(getSender().path().address(), new Node(getSender(), false));
			log.info("master list = {}", masters.toString());
		} else if (message instanceof MemberUp) {
			MemberUp mUp = (MemberUp) message;
			register(mUp.member());
		}

		else if (message instanceof String) {
			log.info("Get message = {}", (String) message);
		}

		else {
			unhandled(message);
		}

	}

	void register(Member member) {
		if (member.hasRole("master")) {
			getContext().actorSelection(member.address() + "/user/master").tell(REGISTRATION_TO_WORKER, getSelf());
		}
	}
}
