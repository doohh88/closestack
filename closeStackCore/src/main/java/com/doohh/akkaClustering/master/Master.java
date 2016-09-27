package com.doohh.akkaClustering.master;

import java.util.HashMap;

import com.doohh.akkaClustering.deploy.AppConf;
import com.doohh.akkaClustering.util.Node;
import com.doohh.akkaClustering.worker.Worker;

import akka.actor.ActorRef;
import akka.actor.Address;
import akka.actor.Props;
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
	public static final String REGISTRATION_TO_MASTER = "Master registrate the worker";
	//HashMap<Address, ActorRef> workers = new HashMap<Address, ActorRef>();
	//ArrayList<Node> workers = new ArrayList<Node>();
	HashMap<Address, Node> workers = new HashMap<Address, Node>();

	private ActorRef launcher;

	// subscribe to cluster changes, MemberUp
	@Override
	public void preStart() {
		cluster.subscribe(getSelf(), MemberUp.class, UnreachableMember.class);
		this.launcher = context().actorOf(Props.create(Launcher.class), "launcher");
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
			//workers.put(getSender().path().address(), getSender());
			//workers.add(new Node(getSender().path().address(), getSender(), false));
			workers.put(getSender().path().address(), new Node(getSender(), false));
			log.info("workers : {}", workers);
		} else if (message instanceof MemberUp) {
			MemberUp mUp = (MemberUp) message;
			register(mUp.member());
		} else if (message instanceof UnreachableMember) {
			UnreachableMember mUnreachable = (UnreachableMember) message;
			workers.remove(mUnreachable.member().address());
		}

		// deploy part
		else if (message instanceof AppConf) {
			AppConf appConf = (AppConf) message;
			launcher.tell(appConf, getSelf());
			getSender().tell("received UserAppConf instance", getSelf());
		}

		//send worker list
		else if (message.equals("getWorkers()")) {
			getSender().tell(workers, getSelf());
		}

		else if (message instanceof String) {
			log.info("Get message = {}", (String) message);
		} 
		
		else {
			unhandled(message);
		}
	}

	void register(Member member) {
		if (member.hasRole("worker")) {
			getContext().actorSelection(member.address() + "/user/worker").tell(REGISTRATION_TO_MASTER, getSelf());
		}
	}
}
