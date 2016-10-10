package com.ssmksh.closestack.worker;

import com.ssmksh.closestack.dto.Instance;
import com.ssmksh.closestack.dto.SnapShot;
import com.ssmksh.closestack.dto.TellCommand;
import com.ssmksh.closestack.util.Util;

import akka.event.Logging;
import akka.event.LoggingAdapter;

public class LXDActor extends VMActor {
	LoggingAdapter log = Logging.getLogger(getContext().system(), this);

	
	@Override
	public void onReceive(Object message) throws Throwable {
		if (message instanceof TellCommand) {
			log.info("receive tellCommand [LXDActor]");
			TellCommand tellCommand = (TellCommand)message;
			String cmd = tellCommand.getCommand();
			Instance instance = null;
			SnapShot snapshot = null;
			if(tellCommand.getData() instanceof Instance){
				instance = (Instance)tellCommand.getData();
			} else{
				snapshot = (SnapShot)tellCommand.getData();
			}
			
			log.info("command: {}", tellCommand.getCommand());
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
			} else if(cmd.equals("snapshot")){
				snapshot(snapshot);
			} else if(cmd.equals("deleteSnapshot")){
				deleteSanpshot(snapshot);
			} else if(cmd.equals("restoreSnapshot")){
				restoreSanpshot(snapshot);
			} else if(cmd.equals("generateSnapshot")){
				generateSanpshot(snapshot);
			} else{
				log.info("no operate: {}", cmd);
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
				+ instance.getFlavor().getDisk() + " "
				+ instance.getIp() + " "
				+ instance.getUserName() + " "
				+ instance.getPw();
		Util.exec(cmd);
		//String hostIP = Util.getHostIP(getSelf().path().address());
		//Util.sshChecker(hostIP,instance, getSender());
		Util.sshChecker(WorkerMain.hostIP, instance, getSender());
		
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
	
	void restart(Instance instance){
		log.info("restart Instance: {}", instance);
		Util.exec("lxc restart " + instance.getName());
	}
	
	void snapshot(SnapShot snapshot){
		log.info("snapshot Instance: {}", snapshot);
		Util.exec("lxc snapshot " + snapshot.getVmName() + " " + snapshot.getName());
	}
	
	void deleteSanpshot(SnapShot snapshot){
		log.info("deleteSanpshot Instance: {}", snapshot);
		Util.exec("lxc delete " + snapshot.getVmName() + " " + snapshot.getName());
	}
	
	void restoreSanpshot(SnapShot snapshot){
		log.info("restoreSanpshot Instance: {}", snapshot);
		Util.exec("lxc restore " + snapshot.getVmName() + " " + snapshot.getName());
	}
	
	void generateSanpshot(SnapShot snapshot){
		log.info("generateSanpshot Instance: {}", snapshot);
		Util.exec("lxc copy " + snapshot.getVmName() + "/" + snapshot.getName() + " " + snapshot.getNewName());
	}
}
