package com.ssmksh.closestack.worker;

import com.ssmksh.closestack.vm.VM;

import akka.actor.UntypedActor;

public class VMActor extends UntypedActor {

	@Override
	public void onReceive(Object message) throws Throwable {
		if (message instanceof VM) {

		} else {
			unhandled(message);
		}
	}
}
