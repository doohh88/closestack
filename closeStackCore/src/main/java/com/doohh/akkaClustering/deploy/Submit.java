package com.doohh.akkaClustering.deploy;

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
	private ActorSelection master;
	private Timeout timeout = new Timeout(Duration.create(10, "seconds"));
	private final ExecutionContext ec;
	
	public Submit() {
		ec = context().system().dispatcher();
	}
	
	@Override
	public void onReceive(Object message) throws Throwable {
		//send AppConf to master. After sending, shut down the Submit actor
		if (message instanceof AppConf) {
			AppConf appConf = (AppConf) message;
			master = getContext().actorSelection(appConf.masterURL);
			Future<Object> future = Patterns.ask(master, appConf, timeout);

			future.onSuccess(new SaySuccess<Object>(), ec);
			future.onComplete(new SayComplete<Object>(), ec);
			future.onFailure(new SayFailure<Object>(), ec);

		} else {
			unhandled(message);
		}
	}

	public final class SaySuccess<T> extends OnSuccess<T> {
		@Override
		public final void onSuccess(T result) {
			log.info("Succeeded with " + result);
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
			getContext().system().shutdown();
		}
	}
}
