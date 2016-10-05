package com.ssmksh.closestack.worker;

import com.ssmksh.closestack.dto.Instance;
import com.ssmksh.closestack.dto.TellCommand;
import com.ssmksh.closestack.util.Util;

import akka.event.Logging;
import akka.event.LoggingAdapter;

public class LXDActor extends VMActor {
	LoggingAdapter log = Logging.getLogger(getContext().system(), this);

	
	@Override
	public void onReceive(Object message) throws Throwable {
		if (message instanceof TellCommand) {
			TellCommand tellCommand = (TellCommand)message;
			String cmd = tellCommand.getCommand();
			Instance instance = (Instance)tellCommand.getData();
			if(cmd.equals("generate")){				
				generate(instance);
				//setResource(instance);				
			} else if(cmd.equals("delete")){
				delete(instance);
			} 
		} else {
			unhandled(message);
		}
	}
	
	void generate(Instance instance){
		log.info("generate Instance: {}", instance);
		//Util.exec("lxc launch " + instance.getOs() + ": " + instance.getName());
		String cmd = "./lxd-generate.sh " 
				+ instance.getOs() + " " 
				+ instance.getName() + " "
				+ instance.getFlavor().getvCpus() + " "
				+ instance.getFlavor().getRam() + " "
				+ instance.getFlavor().getDisk();
		Util.exec(cmd);
	}
	
	void delete(Instance instance){
		log.info("delete Instance: {}", instance);
		Util.exec("lxc delete " + instance.getName() + " --force");
	}
	
	void start(Instance instance){
		log.info("start Instance: {}", instance);
		Util.exec("lxc start " + instance.getName());
	}
	
	void stop(Instance instance){
		log.info("stop Instance: {}", instance);
		Util.exec("lxc stop " + instance.getName());
	}
	
	/*void setResource(Instance instance){
		setCPU(instance);
		setRAM(instance);
		setDISK(instance);		
	}
	
	void setCPU(Instance instance){
		log.info("set Instance's cpu");
		Util.exec("lxc config set " + instance.getName() + " limits.cpu " + instance.getFlavor().getvCpus());
	}
	
	void setRAM(Instance instance){
		log.info("set Instance's ram");
		Util.exec("lxc config set " + instance.getName() + " limits.memory " + instance.getFlavor().getRam() + "GB");
	}
	
	void setDISK(Instance instance){
		log.info("set Instance's disk size");
		Util.exec("lxc config device set " + instance.getName() + " root size " + instance.getFlavor().getDisk() + "GB");
	}*/
}
