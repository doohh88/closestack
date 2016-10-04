package com.ssmksh.closestack.worker;

import com.ssmksh.closestack.dto.Instance;

import akka.event.Logging;
import akka.event.LoggingAdapter;

public class KVMActor extends VMActor {
	LoggingAdapter log = Logging.getLogger(getContext().system(), this);

	@Override
	public void onReceive(Object message) throws Throwable {
		if (message instanceof Instance) {

		} else {
			unhandled(message);
		}
	}
}
