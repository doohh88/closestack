package com.doohh.akkaClustering.worker;

import java.util.HashMap;

import com.doohh.akkaClustering.deploy.AppConf;
import com.doohh.akkaClustering.master.Master;
import com.doohh.akkaClustering.util.Node;

import akka.actor.ActorRef;
import akka.actor.Address;
import akka.actor.Props;
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
	//HashMap<Address, ActorRef> masters = new HashMap<Address, ActorRef>();
	//ArrayList<Node> masters = new ArrayList<Node>();
	HashMap<Address, Node> masters = new HashMap<Address, Node>();
	AppConf userAppConf;

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
			//masters.put(getSender().path().address(), getSender());
			//masters.add(new Node(getSender().path().address(), getSender(), false));
			masters.put(getSender().path().address(), new Node(getSender(), false));
			log.info("master list = {}", masters.toString());
		} else if (message instanceof MemberUp) {
			MemberUp mUp = (MemberUp) message;
			register(mUp.member());
		}

		else if(message instanceof AppConf){
			AppConf appConf = (AppConf)message;
			log.info("appConf : {}", appConf);
			ActorRef task = context().actorOf(Props.create(Task.class), "task");
			task.tell(appConf, getSender());
		}
				
		else if (message instanceof String) {
			log.info("Get message = {}", (String) message);
		} 
		
		else {
			unhandled(message);
		}

	}

	void register(Member member) {
		// log.info("register() -> {} : {}",member.roles(), member.address());
		if (member.hasRole("master")) {
			getContext().actorSelection(member.address() + "/user/master").tell(REGISTRATION_TO_WORKER, getSelf());
			// log.info("master(member.address()) : {}", member.address());
		}
	}
}
