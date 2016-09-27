package com.ssmksh.closestack.master;

import org.kohsuke.args4j.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ssmksh.closestack.util.PropFactory;
import com.ssmksh.closestack.util.Util;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import akka.actor.ActorSystem;
import akka.actor.Props;

public class MasterMain {
	private static final Logger log = LoggerFactory.getLogger(MasterMain.class);
	@Option(name = "--hostIP", usage = "hostIP", aliases = "-h")
	public static String hostIP = "127.0.0.1";
	@Option(name = "--port", usage = "port", aliases = "-p")
	public static String port = "2551";
	public static String systemName = "deepDist";

	public static void main(String[] args) {
		Util.parseArgs(args, new MasterMain());

		String seedNodes = PropFactory.getInstance().getSeedConf("master");
		String role = "[master]";

		log.info("Starting distDepp Master");
		Config conf = ConfigFactory.parseString("akka.remote.netty.tcp.hostname=" + hostIP)
				.withFallback(ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port))
				.withFallback(ConfigFactory.parseString("akka.cluster.seed-nodes=" + seedNodes))
				.withFallback(ConfigFactory.parseString("akka.cluster.roles=" + role))
				.withFallback(ConfigFactory.load("application"));

		ActorSystem actorSystem = ActorSystem.create(systemName, conf);
		actorSystem.actorOf(Props.create(Master.class), "master");
	}
}