package com.ssmksh.closestack.worker;

import com.ssmksh.closestack.dto.Instance;
import com.ssmksh.closestack.dto.TellCommand;
import com.ssmksh.closestack.util.Util;

import akka.event.Logging;
import akka.event.LoggingAdapter;

public class KVMActor extends VMActor {
	LoggingAdapter log = Logging.getLogger(getContext().system(), this);

	@Override
	public void onReceive(Object message) throws Throwable {
		if (message instanceof TellCommand) {
			TellCommand tellCommand = (TellCommand)message;
			String cmd = tellCommand.getCommand();
			Instance instance = (Instance)tellCommand.getData();
			if(cmd.equals("generate")){		
				generate(instance);
			} else if(cmd.equals("delete")){
				delete(instance);
			} else if(cmd.equals("start")){
				start(instance);
			} else if(cmd.equals("stop")){
				stop(instance);
			} else if(cmd.equals("restart")){
				restart(instance);
			} 
		} else {
			unhandled(message);
		}
	}
	
	void generate(Instance instance){
		log.info("generate Instance: {}", instance);
		//Util.exec("lxc launch " + instance.getOs() + ": " + instance.getName());
	}
	
	void delete(Instance instance){
		log.info("delete Instance: {}", instance);
		//Util.exec("lxc delete " + instance.getName() + " --force");
		
	}
	
	void start(Instance instance){
		log.info("start Instance: {}", instance);
		//Util.exec("lxc start " + instance.getName());
	}
	
	void stop(Instance instance){
		log.info("stop Instance: {}", instance);
		//Util.exec("lxc stop " + instance.getName());
	}
	
	void restart(Instance instance){
		log.info("restart Instance: {}", instance);
		//Util.exec("lxc restart " + instance.getName());
	}
}
