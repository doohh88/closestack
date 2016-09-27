package com.doohh.akkaClustering.master;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.doohh.akkaClustering.deploy.AppConf;
import com.doohh.akkaClustering.util.Node;

import akka.actor.ActorRef;
import akka.actor.Address;
import akka.actor.UntypedActor;
import akka.dispatch.OnComplete;
import akka.dispatch.OnFailure;
import akka.dispatch.OnSuccess;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.pattern.Patterns;
import akka.routing.BroadcastGroup;
import akka.util.Timeout;
import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

public class Launcher extends UntypedActor {
	LoggingAdapter log = Logging.getLogger(getContext().system(), this);

	private Timeout timeout = new Timeout(Duration.create(5, "seconds"));
	private final ExecutionContext ec;
	// HashMap<Address, ActorRef> workers = new HashMap<Address, ActorRef>();
	//ArrayList<Node> workers = new ArrayList<Node>();
	HashMap<Address, Node> workers = new HashMap<Address, Node>();

	ActorRef router;
	AppConf appConf;

	public Launcher() {
		ec = context().system().dispatcher();
	}

	@Override
	public void onReceive(Object message) throws Throwable {
		if (message instanceof AppConf) {
			this.appConf = (AppConf) message;
			Future<Object> f = Patterns.ask(getSender(), "getWorkers()", timeout);
			f.onSuccess(new SaySuccess<Object>(), ec);
			f.onComplete(new SayComplete<Object>(), ec);
			f.onFailure(new SayFailure<Object>(), ec);
		}
		
		else if(message.equals("finish()")){
			workers.get(getSender().path().address()).setProc(false);
			log.info("workers : {}", workers);
		}
		
		else {
			unhandled(message);

		}
	}

	public final class SaySuccess<T> extends OnSuccess<T> {
		@Override
		public final void onSuccess(T result) {
			log.info("Succeeded with " + result);
			// workers = (HashMap<Address, ActorRef>) result;
			//workers = (ArrayList<Node>) result;
			workers = (HashMap<Address, Node>) result;
			router = selectNodes(workers);
			
			router.tell(appConf, getSelf());
			context().stop(router);
		}
	}

	public final class SayFailure<T> extends OnFailure {
		@Override
		public final void onFailure(Throwable t) {
			log.info("Failed with " + t);
		}
	}

	public final class SayComplete<T> extends OnComplete<T> {
		@Override
		public final void onComplete(Throwable t, T result) {
			log.info("Completed.");
		}
	}

	public ActorRef selectNodes(HashMap<Address, Node> workers) {
		List<String> routeePaths = new ArrayList<String>();
		int parallelism = this.appConf.getParallelism();
		for(Node node : workers.values()){
			if (node.isProc() == false) {
				routeePaths.add(node.getActorRef().path().toString());
				node.setProc(true);
				if(routeePaths.size() == parallelism + 1) break; 
			}
		}
		log.info("routeePaths : {}", routeePaths);
		ActorRef router = getContext().actorOf(new BroadcastGroup(routeePaths).props(), "router");
		return router;
	}
}


//Iterator<Node> keys = workers.iterator();
//while (keys.hasNext()) {
//	Node node = keys.next();
//	if (node.isProc() == false) {
//		routeePaths.add(node.getActorRef().path().toString());
//		node.setProc(true);
//		if(routeePaths.size() == parallelism) break; 
//	}
//}
