package com.ssmksh.closestack.master;

import java.util.HashMap;

import com.ssmksh.closestack.util.Node;
import com.ssmksh.closestack.worker.Worker;

import akka.actor.ActorRef;
import akka.actor.Address;
import akka.actor.UntypedActor;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent.MemberUp;
import akka.cluster.ClusterEvent.UnreachableMember;
import akka.cluster.Member;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import lombok.Getter;

@Getter
public class Master extends UntypedActor {
	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	Cluster cluster = Cluster.get(getContext().system());
	public static final String REGISTRATION_TO_MASTER = "Master registrates the worker";
	HashMap<Address, Node> workers = new HashMap<Address, Node>();

	private ActorRef launcher;

	// subscribe to cluster changes, MemberUp
	@Override
	public void preStart() {
		cluster.subscribe(getSelf(), MemberUp.class, UnreachableMember.class);
	}

	// re-subscribe when restart
	@Override
	public void postStop() {
		cluster.unsubscribe(getSelf());
	}

	@Override
	public void onReceive(Object message) throws Throwable {
		// clustering part
		if (message.equals(Worker.REGISTRATION_TO_WORKER)) {
			getContext().watch(getSender());
			workers.put(getSender().path().address(), new Node(getSender(), false));
			log.info("workers : {}", workers);
		} else if (message instanceof MemberUp) {
			MemberUp mUp = (MemberUp) message;
			register(mUp.member());
		} else if (message instanceof UnreachableMember) {
			UnreachableMember mUnreachable = (UnreachableMember) message;
			workers.remove(mUnreachable.member().address());
		}

		else if (message instanceof String) {
			log.info("Get message = {}", (String) message);
		} else {
			unhandled(message);
		}
	}

	void register(Member member) {
		if (member.hasRole("worker")) {
			getContext().actorSelection(member.address() + "/user/worker").tell(REGISTRATION_TO_MASTER, getSelf());
		}
	}
}
