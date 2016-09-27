package com.ssmksh.network;

import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class NetActor extends UntypedActor {
	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	private ActorSelection master = null;
	private String hostIP = "163.152.47.96";
	private String SystemName = "closestack";

	@Override
	public void preStart() throws Exception {
		master = getContext().actorSelection("akka.tcp://" + SystemName + "@" + hostIP + ":2551/user/master");
	}

	@Override
	public void onReceive(Object message) throws Throwable {
		master.tell(message, getSelf());		// 그냥 전달

/*		 //만약에 그냥 전달하지 않고 어떠한 처리를 하고 싶을 경우
		 if(message instanceof CLASSNAME){
			 CLASSNAME cl = (CLASSNAME)message;
			 cl.add();
			 mater.tell( , );
		 } else {
			 unhandled(message);
		 }*/

		// mater로부터 응답을 받아야하는 경우 (callback으로) 나한테 물어봐ㅋㅋ
	}
}
