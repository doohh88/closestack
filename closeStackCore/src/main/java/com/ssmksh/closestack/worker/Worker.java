package com.ssmksh.closestack.worker;

import java.util.HashMap;

import com.ssmksh.closestack.query.QueryConf;
import com.ssmksh.closestack.util.Node;
import com.ssmksh.closestack.util.Util;
import com.ssmksh.closestack.vm.LXD;
import com.ssmksh.closestack.vm.VM;

import akka.actor.ActorRef;
import akka.actor.Address;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent.CurrentClusterState;
import akka.cluster.ClusterEvent.MemberEvent;
import akka.cluster.ClusterEvent.MemberRemoved;
import akka.cluster.ClusterEvent.MemberUp;
import akka.cluster.ClusterEvent.UnreachableMember;
import akka.cluster.Member;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.routing.RoundRobinPool;
import lombok.Getter;
import scala.concurrent.Await;
import scala.concurrent.duration.Duration;

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
		String actorURL = cluster.selfAddress().toString() + "/user/worker";
		Util.write("actor.url", actorURL);
	}

	// re-subscribe when restart
	@Override
	public void postStop() {
		cluster.unsubscribe(getSelf());
	}

	@Override
	public void onReceive(Object message) throws Throwable {
		if (message instanceof Node) {
			Node node = (Node) message;
			masters.put(getSender().path().address(), node);
		} else if (message instanceof CurrentClusterState) {
			CurrentClusterState state = (CurrentClusterState) message;
			log.info("Current members: {}", state.members());
		} else if (message instanceof MemberUp) {
			MemberUp mUp = (MemberUp) message;
			log.info("Member is Up: {}", mUp.member());
			register(mUp.member());
		} else if (message instanceof UnreachableMember) {
			UnreachableMember mUnreachable = (UnreachableMember) message;
			log.info("Member detected as unreachable: {}", mUnreachable.member());
		} else if (message instanceof MemberRemoved) {
			MemberRemoved mRemoved = (MemberRemoved) message;
			log.info("Member is Removed: {}", mRemoved.member());
		} else if (message instanceof MemberEvent) {
		}

		else if (message instanceof VM) {

		} else if (message instanceof LXD) {

		}

		// Query part
		else if (message instanceof QueryConf) {
			QueryConf queryConf = (QueryConf) message;
			log.info("get QueryConf");
			log.info("query: {}", queryConf.getArgs());
			String[] args = queryConf.getArgs();
			String cmd = args[0];
			
			if(cmd.equals("stop")){
				System.out.println("cluster.sefAddress(): " + cluster.selfAddress());
				cluster.leave(cluster.selfAddress());
				//getContext().system().terminate();
				Await.ready(getContext().system().terminate(), Duration.Inf());

			}
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
