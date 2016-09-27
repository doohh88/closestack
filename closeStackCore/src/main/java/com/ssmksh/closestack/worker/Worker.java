package com.ssmksh.closestack.worker;

import java.util.HashMap;

import com.ssmksh.closestack.util.Node;
import com.ssmksh.closestack.vm.LXD;
import com.ssmksh.closestack.vm.VM;

import akka.actor.ActorRef;
import akka.actor.Address;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent.MemberUp;
import akka.cluster.Member;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.routing.RoundRobinPool;
import lombok.Getter;

@Getter
public class Worker extends UntypedActor {
	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	Cluster cluster = Cluster.get(getContext().system());
	HashMap<Address, Node> masters = new HashMap<Address, Node>();
	private ActorRef LXDActor = null;
	private ActorRef VMActor = null;
	
	// subscribe to cluster changes, MemberUp
	@Override
	public void preStart() {
		cluster.subscribe(getSelf(), MemberUp.class);
		LXDActor = getContext().actorOf(new RoundRobinPool(5).props(Props.create(LXDActor.class)), "LXDActor");
		VMActor = getContext().actorOf(new RoundRobinPool(5).props(Props.create(VMActor.class)), "VMActor");
	}

	// re-subscribe when restart
	@Override
	public void postStop() {
		cluster.unsubscribe(getSelf());
	}

	@Override
	public void onReceive(Object message) throws Throwable {
		if (message instanceof Node) {
			Node node = (Node)message;
			getContext().watch(getSender());
			masters.put(getSender().path().address(), node);
		} else if (message instanceof MemberUp) {
			MemberUp mUp = (MemberUp) message;
			register(mUp.member());
		}

		else if (message instanceof VM){
			
		} else if (message instanceof LXD){
			
		}
		
		
		
		else if (message instanceof String) {
			log.info("Get message = {}", (String) message);
		} else {
			unhandled(message);
		}

	}

	void register(Member member) {
		if (member.hasRole("master")) {
			Node node = new Node(getSelf());
			getContext().actorSelection(member.address() + "/user/master").tell(node, getSelf());
		}
	}
}
