package com.ssmksh.closestack.master;

import java.util.ArrayList;
import java.util.Collection;

import com.ssmksh.closestack.dto.Flavor;
import com.ssmksh.closestack.dto.Instance;
import com.ssmksh.closestack.dto.Network;
import com.ssmksh.closestack.dto.Overall;
import com.ssmksh.closestack.dto.SnapShot;
import com.ssmksh.closestack.dto.TellCommand;
import com.ssmksh.closestack.query.QueryConf;
import com.ssmksh.closestack.util.IP;
import com.ssmksh.closestack.util.Node;
import com.ssmksh.closestack.util.PropFactory;
import com.ssmksh.closestack.util.Util;

import akka.actor.ActorSelection;
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
	ArrayList<IP> ipPool = null;
	ActorSelection web = null;
	// subscribe to cluster changes, MemberUp
	@Override
	public void preStart() {
		cluster.subscribe(getSelf(), MemberUp.class, UnreachableMember.class);
		rcPool = Resource.getInstance();
		ipPool = PropFactory.getInstance().getIpPool();
		String actorURL = cluster.selfAddress().toString() + "/user/master";
		Util.write("actor.url", actorURL);
		web = getContext().actorSelection("akka.tcp://" + MasterMain.systemName + "@" + "211.189.20.11" + ":2551/user/netActor");
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
//			log.info("total CPUs = {}", rcPool.getCPU());
//			log.info("total RAMs = {}", rcPool.getRAM());
//			log.info("total DISKs = {}", rcPool.getDISK());
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
			rcPool.remove(mUnreachable); //리소스 반환
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
			
			if(tellCommand.getCommand().equals("generate")){
				//web = getSender();
				Instance instance = (Instance) tellCommand.getData();
				Flavor flavor = (Flavor) instance.getFlavor();
				log.info("receive generate");
				int ipidx = getIp(instance);
				log.info("getip: {}", instance.getIp());
				String ip = ipPool.get(ipidx).getAddr();
				
				if(ipidx == -1){
					System.out.println("no IP");
					getSender().tell("fail", getSelf());
				}
				else{
					Node node = getNode(instance);
					if(node == null){
						System.out.println("no space");
						TellCommand rts = new TellCommand<String>("master", "tell", "createFail", "0");
						getSender().tell(rts, getSelf());
					}
					else{
						System.out.println("ok");
						log.info("send tellCommand to {}", node);
						log.info("cmd: {}", tellCommand.getCommand());
						//node.getActorRef().tell(tellCommand, getSender());
						node.getActorRef().tell(tellCommand, getSelf());
						log.info("complete to send tellCommand to {}", node);
						log.info("before assign resource, Node: {}", node);
						node.useResource(flavor);
						log.info("after assign resource, Node: {}", node);
						TellCommand rts = new TellCommand<Instance>("master", "tell", "createSuccess", instance);
						log.info("cmd: {}", rts.getCommand());
						log.info("ip: {}", ((Instance)rts.getData()).getIp());
						getSender().tell(rts, getSelf());
						log.info("complete to send tellCommand to web");						
						ipPool.get(ipidx).setNode(node);												
					}
				}
			}
			else if(tellCommand.getCommand().equals("getNetwork")){
				log.info("receive getNetwork");
				Network network = new Network();
				setNetwork(network, ipPool);
				TellCommand rts = new TellCommand<Network>("master", "tell", "networkSuccess", network);
				getSender().tell(rts, getSelf());
				log.info("Network: {}", network);
			}
			else if(tellCommand.getCommand().equals("getResource")){
				log.info("receive getResource");
				Overall overall = new Overall(rcPool.getCPU(), rcPool.getRAM(), rcPool.getDISK(), ipPool.size());
				TellCommand rts = new TellCommand<Overall>("master", "tell", "resourceSuccess", overall);
				getSender().tell(rts, getSelf());
				log.info("Overall: {}", overall);
			}
			else if(tellCommand.getCommand().equals("complete")){
				Instance instance = (Instance) tellCommand.getData();
				log.info("complete to generate lxd: {}", instance);
				TellCommand rts = new TellCommand<Instance>("master", "tell", "resourceSuccess", instance);
				//getSender().tell(rts, getSelf());
				web.tell(rts, getSelf());
			}
			else if(tellCommand.getCommand().equals("web")){
				//web = getSender();
			}
			else {
				//[delete, start, stop, restart, snapshot...]
				
				//Instance instance = (Instance) tellCommand.getData();
				//Flavor flavor = (Flavor) instance.getFlavor();
				log.info("receive {}", tellCommand.getCommand());
				//System.out.println(instance.getIp());
				Node node = findNodebyIP(tellCommand);
				log.info("node: {}", node.getActorRef());
				//node.getActorRef().tell(tellCommand, getSender());
				node.getActorRef().tell(tellCommand, getSelf());
				if(tellCommand.getCommand().equals("delete")){
					//delete일때 자원 회수
					log.info("before return resource, Node: {}", node);
					Instance instance = (Instance) tellCommand.getData();
					Flavor flavor = (Flavor) instance.getFlavor();
					node.returnResource(flavor);
					log.info("after return resource, Node: {}", node);
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
	
	int getIp(Instance instance){
		int len = ipPool.size();
		for(int i = 0 ; i < len ; i++){
			IP ip = ipPool.get(i);
			if(!ip.isUsed()){
				instance.setIp(ip.getAddr()); //ip setting
				ip.setUsed(true);
				return i;
			}
		}
		return -1;
	}
	
	Node getNode(Instance instance){
		Collection<Node> values = getCollection(instance);
		for (Node node : values) {
			if (node.canSave(instance.getFlavor())) {
				return node;
			}
		}
		return null;
	}
	
	Node findNodebyIP(TellCommand tellCommand){
		Node node = null;
		Instance instance = null;
		SnapShot snapshot = null;
		String findIp = null;
		if(tellCommand.getData() instanceof Instance){
			log.info("Data: Instnace");
			instance = (Instance) tellCommand.getData();
			findIp = instance.getIp();
		}
		else {
			log.info("Data: SnapShot");
			snapshot= (SnapShot) tellCommand.getData();
			findIp = snapshot.getIp();
		}		
		
		int len = ipPool.size();
		for(int i = 0 ; i < len ; i++){
			IP ip = ipPool.get(i);
			if(ip.getAddr().equals(findIp)){
				node = ip.getNode();
				//회수
				if(tellCommand.getCommand().equals("delete")){
					ip.setUsed(false);
					ip.setNode(null);
				}
				return node;
			}
		}
		return null;
	}
	
	Collection<Node> getCollection(Instance instance){
		if (instance.getType().equals("lxd")) {
			return rcPool.getLxdNodes().values();
		}
		else if(instance.getType().equals("kvm")){
			return rcPool.getKvmNodes().values();
		}
		return null;
	}
	
	void setNetwork(Network network, ArrayList<IP> ipPool){
		ArrayList<com.ssmksh.closestack.dto.IP> ips = new ArrayList<com.ssmksh.closestack.dto.IP>();  
		for(IP ip : ipPool){
			com.ssmksh.closestack.dto.IP i = null;
			if(ip.getNode() != null){
				i = new com.ssmksh.closestack.dto.IP(ip.getAddr(), ip.isUsed(), ip.getNode().getActorRef().path().address().toString());
			}
			else{
				i = new com.ssmksh.closestack.dto.IP(ip.getAddr(), ip.isUsed(), "0");
			}
			log.info("IP: {}", i);
			ips.add(i);
			//network.getIps().add(i);
			log.info("network.getIps(): {}", network.getIps());
		}
		network.setIps(ips);
	}
}
