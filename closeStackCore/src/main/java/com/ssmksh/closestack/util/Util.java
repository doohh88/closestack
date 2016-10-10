package com.ssmksh.closestack.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URI;
import java.net.UnknownHostException;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ssmksh.closestack.dto.Instance;
import com.ssmksh.closestack.dto.Overall;
import com.ssmksh.closestack.dto.TellCommand;

import akka.actor.ActorRef;
import akka.actor.Address;

public class Util {
	private static final Logger log = LoggerFactory.getLogger(Util.class);

	public static String[] parseArgs(String[] args, Object obj) {
		int argsLen = args.length;
		String[] appArgs = null;
		String[] parseArgs = null;

		for (int i = 0; i < argsLen; i++) {
			if (args[i].equals("--class") || args[i].equals("-c")) {
				int appArgsLen = argsLen - i - 2;
				int parseArgsLen = i + 2;
				appArgs = new String[appArgsLen];
				parseArgs = new String[parseArgsLen];
				System.arraycopy(args, parseArgsLen, appArgs, 0, appArgsLen);
				System.arraycopy(args, 0, parseArgs, 0, parseArgsLen);
				break;
			}
		}

		CmdLineParser parser = new CmdLineParser(obj);
		try {
			if (parseArgs == null) {
				parser.parseArgument(args);
			} else {
				parser.parseArgument(parseArgs);
			}
		} catch (CmdLineException e) {
			log.info(e.getMessage());
			parser.printUsage(System.err);
		}

		return appArgs;
	}

	public static String exec(String cmd) {
		String rst = null;
		if (isUnix()) {
			try {
				log.info("executer command: {}", cmd);
				Process p = Runtime.getRuntime().exec(cmd);
				BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
				rst = br.readLine();
				String line;
				while ((line = br.readLine()) != null) {
					// System.out.println(line);
					log.info("{}", line);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return rst;
	}

	public static boolean isUnix() {
		String OS = System.getProperty("os.name").toLowerCase();
		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0);
	}

	public static void write(String fileName, String str) {
		try {
			File file = new File(fileName);
			FileWriter fw = new FileWriter(file, false);
			fw.write(str);
			fw.flush();
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void sshChecker(String hostIP, Instance instance, ActorRef actor) {
		Socket socket = null;
		BufferedOutputStream bos = null;
		BufferedInputStream bis = null;
		try {
			System.out.println("[Util] Execute ssh checker...");
			// socket = new Socket("211.189.20.168",1234);
			socket = new Socket(hostIP, 1234);

			bos = new BufferedOutputStream(socket.getOutputStream());
			byte[] byteStr = new byte[1024];

			String ab;
			if (instance.getOs().equals("centos")) {
				ab = "b";
			} else {
				ab = "a";
			}
			String sendStr = ab + "/" + instance.getIp();
			byteStr = sendStr.getBytes();
			bos.write(byteStr);
			bos.flush();

			bis = new BufferedInputStream(socket.getInputStream());
			byte[] readBuff = new byte[1024];
			int len = bis.read(readBuff);
			if (len > 0) {
				System.out.println("[" + instance.getIp() + "] Install Complete\n");
				TellCommand rts = new TellCommand<Instance>("master", "tell", "complete", instance);
				actor.tell(rts, ActorRef.noSender());
			}

			bos.close();
			socket.close();

		} catch (UnknownHostException e) {
			System.out.println("Unkonw exception " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IOException caught " + e.getMessage());
			e.printStackTrace();
		}
	}

	public static boolean execute_shell(Instance instance, String hi, ActorRef actor) {
		log.info("[KVM] execute_shell!!!!!");
		Socket socket = null;
		BufferedOutputStream bos = null;
		BufferedInputStream bis = null;

		try {
			socket = new Socket("211.189.20.168", 8197);

			// server -> client 출력용 스트림
			bos = new BufferedOutputStream(socket.getOutputStream());
			byte[] b = new byte[1024];
			b = hi.getBytes();
			bos.write(b);
			bos.flush();

			bis = new BufferedInputStream(socket.getInputStream());
			byte[] buff = new byte[1024];
			int len = bis.read(buff);
			if (len > 0) {
				// create vm complete
				System.out.println(buff);
				sshChecker("211.189.20.168", instance, actor);
			}
			bis.close();
			bos.close();
			socket.close();
		} catch (UnknownHostException e) {
			System.out.println("Unkonw exception " + e.getMessage());
			return false;
		} catch (IOException e) {
			System.out.println("IOException caught " + e.getMessage());
			e.printStackTrace();
			return false;
		}
		return true;
	}

//	public static String getHostIP(Address address) {
//		//String address = "akka.tcp://closestack@127.0.0.1:12345";s
//		log.info("address: {}", address);
//		String host = null;
//		String addr = address.toString();		
//		String ip_port = addr.substring(22, addr.length());
//		System.out.println(ip_port);
//		URI uri;
//		try {
//			uri = new URI("my://" + ip_port);
//			host = uri.getHost();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		System.out.println(host);
//		return host;
//	}
}