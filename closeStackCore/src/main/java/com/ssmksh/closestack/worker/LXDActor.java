package com.ssmksh.closestack.worker;

import com.ssmksh.closestack.vm.LXD;

import akka.actor.UntypedActor;

public class LXDActor extends UntypedActor {

	@Override
	public void onReceive(Object message) throws Throwable {
		if (message instanceof LXD) {
			
		} else {
			unhandled(message);
		}
	}
	
	void generate(LXD lxd){
		
	}
	

	
}
