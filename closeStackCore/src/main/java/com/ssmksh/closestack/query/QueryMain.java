package com.ssmksh.closestack.query;

import org.kohsuke.args4j.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ssmksh.closestack.util.Util;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class QueryMain {
	private static final Logger log = LoggerFactory.getLogger(QueryMain.class);

	@Option(name = "--hostIP", usage = "hostIP", aliases = "-h")
	public static String hostIP = "127.0.0.1";
	@Option(name = "--actorAddress", usage = "actorAddress", aliases = "-a")
	public static String actorURL = "akka.tcp://closestack@127.0.0.1:2551/user/master";
	@Option(name = "--class", usage = "classPath", aliases = "-c")
	public static String classPath = null;
	
	public static void main(String[] args) {
		String[] appArgs = Util.parseArgs(args, new QueryMain());
		Config conf = ConfigFactory.parseString("akka.remote.netty.tcp.hostname=" + hostIP)
				.withFallback(ConfigFactory.load("deploy"));

		final ActorSystem system = ActorSystem.create("query", conf);
		final ActorRef query = system.actorOf(Props.create(Query.class), "query");

		QueryConf queryConf = new QueryConf.Builder().actorURL(actorURL).args(appArgs).build();
		query.tell(queryConf, ActorRef.noSender());
	}
}
