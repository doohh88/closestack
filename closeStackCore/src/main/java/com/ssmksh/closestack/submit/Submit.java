package com.ssmksh.closestack.submit;

import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.dispatch.OnComplete;
import akka.dispatch.OnFailure;
import akka.dispatch.OnSuccess;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.pattern.Patterns;
import akka.util.Timeout;
import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

public class Submit extends UntypedActor {
	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	private ActorSelection master = null;
	private String hostIP = "127.0.0.1";
	private String SystemName = "closestack";
	
	@Override
	public void preStart() throws Exception {
		//master = getContext().actorSelection("akka.tcp://" + SystemName + "@" + hostIP + ":2551/user/master");
		master = getContext().actorSelection("akka.tcp://closestack@127.0.0.1:2551/user/master");
	}	
	
	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof String){
			String msg = (String)message;
			System.out.println(msg);
			master.tell(message, getSelf());
			//getContext().system().shutdown();
		} else {
			unhandled(message);
		}
	}
}
