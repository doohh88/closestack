package com.ssmksh.closestack.master;

import com.ssmksh.closestack.submit.Submit;
import com.ssmksh.closestack.util.Node;

import akka.actor.ActorRef;
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
	Resource rcPool = null;

	private ActorRef launcher;

	// subscribe to cluster changes, MemberUp
	@Override
	public void preStart() {
		cluster.subscribe(getSelf(), MemberUp.class, UnreachableMember.class);
		rcPool = Resource.getInstance();
	}

	// re-subscribe when restart
	@Override
	public void postStop() {
		cluster.unsubscribe(getSelf());
	}

	@Override
	public void onReceive(Object message) throws Throwable {
		// clustering part
		if (message instanceof Node) {
			Node node = (Node)message;
			getContext().watch(getSender());
			rcPool.put(node);
		} else if (message instanceof MemberUp) {
			MemberUp mUp = (MemberUp) message;
			register(mUp.member());
		} else if (message instanceof UnreachableMember) {
			UnreachableMember mUnreachable = (UnreachableMember) message;
			rcPool.remove(mUnreachable);
		}
//		
//		else if (getSender().getClass() instanceof Submit){
//			
//		}
		

		else if (message instanceof String) {
			log.info("Get message = {}", (String) message);
		} else {
			unhandled(message);
		}
	}

	void register(Member member) {
		if (member.hasRole("worker")) {
			Node node = new Node(getSelf());
			getContext().actorSelection(member.address() + "/user/worker").tell(node, getSelf());
		}
	}
}
