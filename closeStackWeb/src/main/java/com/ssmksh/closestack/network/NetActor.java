package com.ssmksh.closestack.network;

import org.springframework.beans.factory.annotation.Autowired;

import com.ssmksh.closestack.C;
import com.ssmksh.closestack.dao.InstanceDAO;
import com.ssmksh.closestack.dto.Instance;
import com.ssmksh.closestack.dto.TellCommand;

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

public class NetActor extends UntypedActor {
	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	private ActorSelection master = null;
	private String hostIP = C.Url.MASTERSERVER;
	private String SystemName = "closestack";

	private Timeout timeout = new Timeout(Duration.create(5, "seconds"));
	private ExecutionContext ec;

	@Autowired
	InstanceDAO instanceDAO;
	
	@Override
	public void preStart() throws Exception {
		master = getContext().actorSelection("akka.tcp://" + SystemName + "@" + hostIP + ":2551/user/master");
		ec = context().system().dispatcher();
	}

	@Override
	public void onReceive(Object message) throws Throwable {
		// master.tell(message, getSelf()); // 그냥 전달
		
		TellCommand tellCommand = (TellCommand) message;
		System.out.println("I am "+tellCommand.getWho()+ " I command "+tellCommand.getCommand());
		
		// master -> web
		if (tellCommand.getWho().equals("master")) {
			
			if(tellCommand.getCommand().equals("compelete")){
				Instance instance = (Instance) tellCommand.getData();
				instanceDAO.updateStatus(instance.getUserName(), instance.getName(), instance.getStatus());	
			}
			
			

		} else {

			if (tellCommand.getSendType().equals("tell")) {
				master.tell(tellCommand, getSelf());
			} else if (tellCommand.getSendType().equals("ask")) {
				ask(tellCommand);
			} else {

				
			}
		}

	}

	public void ask(TellCommand tellCommand) throws Exception {
		Future<Object> future = Patterns.ask(master, tellCommand, timeout);
		future.onSuccess(saySuccess, ec);
		future.onFailure(sayFailure, ec);
		future.onComplete(sayComplete, ec);
	}
	

	OnSuccess<Object> saySuccess = new OnSuccess<Object>() {

		@Override
		public void onSuccess(Object arg0) throws Throwable {
			// TODO Auto-generated method stub
			System.out.println("onSuccess -actor");
			
			
			
		}
	};

	OnFailure sayFailure = new OnFailure() {

		@Override
		public void onFailure(Throwable arg0) throws Throwable {
			// TODO Auto-generated method stub
			System.out.println("onFailure -actor");
		}
	};

	OnComplete<Object> sayComplete = new OnComplete<Object>() {

		@Override
		public void onComplete(Throwable arg0, Object arg1) throws Throwable {
			// TODO Auto-generated method stub
			System.out.println("onComplete -actor");
		}
	};

	public void setOnSuccess(OnSuccess success) {
		saySuccess = success;
	}

	public void setOnFalure(OnFailure failure) {
		sayFailure = failure;
	}

	public void setOnComplete(OnComplete complete) {
		sayComplete = complete;
	}
}
