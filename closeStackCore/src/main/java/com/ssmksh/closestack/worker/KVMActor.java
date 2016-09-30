package com.ssmksh.closestack.worker;

import com.ssmksh.closestack.vm.KVM;

import akka.actor.UntypedActor;

public class KVMActor extends UntypedActor {

	@Override
	public void onReceive(Object message) throws Throwable {
		if (message instanceof KVM) {

		} else {
			unhandled(message);
		}
	}
}
