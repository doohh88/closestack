package com.ssmksh.closestack.master;

import com.ssmksh.closestack.query.QueryConf;
import com.ssmksh.closestack.util.Node;
import com.ssmksh.closestack.util.Util;

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
import lombok.Getter;
import scala.concurrent.Await;
import scala.concurrent.duration.Duration;

@Getter
public class Master extends UntypedActor {
	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	Cluster cluster = Cluster.get(getContext().system());
	Resource rcPool = null;

	// subscribe to cluster changes, MemberUp
	@Override
	public void preStart() {
		cluster.subscribe(getSelf(), MemberUp.class, UnreachableMember.class);
		rcPool = Resource.getInstance();
		String actorURL = cluster.selfAddress().toString() + "/user/master";
		Util.write("actor.url", actorURL);
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
			Node node = (Node) message;
			rcPool.put(node);
			log.info("total CPUs = {}", rcPool.getCPU());
			log.info("total RAMs = {}", rcPool.getRAM());
			log.info("total DISKs = {}", rcPool.getDISK());
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
			rcPool.remove(mRemoved);
		} else if (message instanceof MemberEvent) {
		}

		// Query part
		else if (message instanceof QueryConf) {
			QueryConf queryConf = (QueryConf) message;
			log.info("get QueryConf");
			log.info("query: {}", queryConf.getArgs());
			String[] args = queryConf.getArgs();
			String cmd = args[0];
			System.out.println("cmd: " + cmd);
			if(cmd.equals("getResource")){
				System.out.println("getResource");
				String rst = " vcpu : " + rcPool.getCPU() + " ram : " + rcPool.getRAM() + " disk : " + rcPool.getDISK();
				getSender().tell(rst, getSelf());	
			} else if(cmd.equals("stop")){
				System.out.println("stop");
				System.out.println("cluster.sefAddress(): " + cluster.selfAddress());
				//cluster.leave(cluster.selfAddress());
				cluster.leave(cluster.selfAddress());
//				Thread.sleep(5000);
//				getContext().system().terminate();
				//getContext().system().shutdown();
				//getContext().stop(getSelf());
				Await.ready(getContext().system().terminate(), Duration.Inf());
			}
		}
		
		

		// message & unhandled part
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

