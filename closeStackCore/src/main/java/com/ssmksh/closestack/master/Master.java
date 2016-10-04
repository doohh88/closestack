package com.ssmksh.closestack.master;

import java.util.Collection;

import com.ssmksh.closestack.dto.Flavor;
import com.ssmksh.closestack.dto.Instance;
import com.ssmksh.closestack.dto.TellCommand;
import com.ssmksh.closestack.query.QueryConf;
import com.ssmksh.closestack.util.Node;
import com.ssmksh.closestack.util.Util;

import akka.actor.ActorRef;
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
			System.out.println(node);
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
			rcPool.remove(mUnreachable);
		} else if (message instanceof MemberRemoved) {
			MemberRemoved mRemoved = (MemberRemoved) message;
			log.info("Member is Removed: {}", mRemoved.member());
		} else if (message instanceof MemberEvent) {
		}

		// Query part
		else if (message instanceof QueryConf) {
			QueryConf queryConf = (QueryConf) message;
			log.info("get query: {}", queryConf.getArgs());
			String[] args = queryConf.getArgs();
			String cmd = args[0];
			System.out.println("cmd: " + cmd);
			if (cmd.equals("getResource")) {
				log.info("exec query: {}", queryConf.getArgs());
				String rst = " vcpu : " + rcPool.getCPU() + " ram : " + rcPool.getRAM() + " disk : " + rcPool.getDISK();
				getSender().tell(rst, getSelf());
			} else if (cmd.equals("stop")) {
				log.info("exec query: {}", queryConf.getArgs());
				cluster.leave(cluster.selfAddress());
				getContext().system().terminate();
			} else if (cmd.equals("generate LXD")) {
				log.info("exec query: {}", queryConf.getArgs());
			}
		}

		// TellCommand
		else if (message instanceof TellCommand) {
			System.out.println("get TellCommand");
			TellCommand tellCommand = (TellCommand) message;
			Instance instance = (Instance) tellCommand.getData();
			Flavor flavor = (Flavor) instance.getFlavor();
			if (instance.getType().equals("lxd")) {
				Collection<Node> values = rcPool.getLxdNodes().values();
				ActorRef worker = null;
				for (Node node : values) {
					if (node.canSave(flavor)) {
						worker = node.getActorRef();
						break;
					}
				}
				if (worker == null) { //생성할 공간이 없음
					System.out.println("no space");
					getSender().tell("fail", getSelf());
				} else{
					System.out.println("ok");
					getSender().tell("ok", getSelf());
					worker.tell(tellCommand, getSender());
				}

			} else if (instance.getType().equals("kvm")) {
				Collection<Node> values = rcPool.getKvmNodes().values();
				ActorRef worker = null;
				for (Node node : values) {
					if (node.canSave(flavor)) {
						worker = node.getActorRef();
						break;
					}
				}
				if (worker == null) { 
					System.out.println("no space");
					getSender().tell("fail", getSelf());
				} else{
					System.out.println("ok");
					getSender().tell("ok", getSelf());
					worker.tell(tellCommand, getSender());
				}
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
		System.out.println(member.getRoles());
		if (member.hasRole("lxd") || member.hasRole("kvm")) {
			Node masterNode = new Node(getSelf(), "master");
			getContext().actorSelection(member.address() + "/user/worker").tell(masterNode, getSelf());
		}
	}
}
