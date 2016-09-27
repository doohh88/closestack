package com.ssmksh.closestack.submit;

import java.util.ArrayList;
import java.util.List;

import com.ssmksh.closestack.master.Resource;
import com.ssmksh.closestack.util.Node;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.routing.BroadcastGroup;

public class Launcher extends UntypedActor {
	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	Resource rcPool = Resource.getInstance();
	ActorRef router = null;

	@Override
	public void onReceive(Object message) throws Throwable {
		if (message instanceof String) {
			this.router = selectNodes(rcPool);
			router.tell(message, getSelf());
		}
		else {
			unhandled(message);

		}
	}
	
	public ActorRef selectNodes(Resource rcPool) {
		List<String> routeePaths = new ArrayList<String>();
		for(Node node : rcPool.getNodes().values()){
				routeePaths.add(node.getActorRef().path().toString());
		}
		log.info("routeePaths : {}", routeePaths);
		ActorRef rst = getContext().actorOf(new BroadcastGroup(routeePaths).props(), "router");
		return rst;
	}
}
