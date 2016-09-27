package com.doohh.akkaClustering.deploy;

import org.kohsuke.args4j.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.doohh.akkaClustering.util.Util;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;


public class SubmitMain {
	private static final Logger log = LoggerFactory.getLogger(SubmitMain.class);

	@Option(name = "--master", usage = "master", aliases = "-m")
	public static String master = "127.0.0.1";
	@Option(name = "--port", usage = "port", aliases = "-p")
	public static String port = "2551";
	@Option(name = "--jar", usage = "jarPath", aliases = "-j")
	public static String jarPath = null;
	@Option(name = "--class", usage = "classPath", aliases = "-c")
	public static String classPath = null;
	@Option(name = "--parallelism", usage = "parallelism", aliases = "-pr")
	public static int parallelism = 1;

	public static void main(String[] args) {
		//test
		args = new String[8];
		args[0] = "-pr";
		args[1] = "2";
		args[2] = "-j";
		args[3] = "C:/git/akkaClustering/jars/TestPjt-0.0.1-SNAPSHOT-allinone.jar";
		args[4] = "-c";
		args[5] = "TestMain.Main";
		args[6] = "hello";
		args[7] = "args";
		
		String[] appArgs = Util.parseArgs(args, new SubmitMain());
		if (jarPath == null || classPath == null) {
			log.error("please input --jar & --class option");
			return;
		}

		Config conf = ConfigFactory.load("deploy");
		final ActorSystem system = ActorSystem.create("deploy", conf);
		final ActorRef submit = system.actorOf(Props.create(Submit.class), "submit");
		
		AppConf appConf = new AppConf.Builder()
				.masterIP(master)
				.port(port)
				.jarPath(jarPath)
				.classPath(classPath)
				.parallelism(parallelism)
				.args(appArgs)
				.build();

		submit.tell(appConf, ActorRef.noSender());
	}
}
