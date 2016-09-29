package com.ssmksh.closestack.query;

import java.util.concurrent.TimeoutException;

import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.dispatch.OnComplete;
import akka.dispatch.OnFailure;
import akka.dispatch.OnSuccess;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.pattern.Patterns;
import akka.util.Timeout;
import scala.concurrent.Await;
import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

public class Query extends UntypedActor {
	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	private ActorSelection actor;
	private Timeout timeout = new Timeout(Duration.create(5, "seconds"));
	private final ExecutionContext ec;
	
	public Query() {
		ec = context().system().dispatcher();
	}
	
	@Override
	public void onReceive(Object message) throws Throwable {
		//send AppConf to master. After sending, shut down the Submit actor
		if (message instanceof QueryConf) {
			QueryConf queryConf = (QueryConf) message;
			actor = getContext().actorSelection(queryConf.actorURL);
			log.info("sending QueryConf");
			Future<Object> future = Patterns.ask(actor, queryConf, timeout);

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
			log.info("Succeeded with " + (String)result);
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
		public final void onComplete(Throwable t, T result) throws Exception {
			log.info("Completed.");
			getContext().system().terminate();
		}
	}
}
