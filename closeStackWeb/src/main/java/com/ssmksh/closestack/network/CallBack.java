package com.ssmksh.closestack.network;

import akka.dispatch.OnComplete;
import akka.dispatch.OnFailure;
import akka.dispatch.OnSuccess;

public class CallBack {

	public final class SaySuccess<TellCommand> extends OnSuccess<TellCommand> {
		@Override
		public final void onSuccess(TellCommand result) {
			System.out.println("Succeeded with " + (String) result);
		}
	}

	public final class SayFailure<TellCommand> extends OnFailure {
		@Override
		public final void onFailure(Throwable t) {
			System.out.println("Failed with " + t);
		}
	}

	public final class SayComplete<TellCommand> extends OnComplete<TellCommand> {
		@Override
		public final void onComplete(Throwable t, TellCommand result) throws Exception {
			System.out.println("Completed.");
			
		}
	}
}
