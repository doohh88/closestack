package com.ssmksh.closestack.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
				if (rst.contains("Permission denied")) {
					rst = br.readLine();
				}
//				String line;
//				while((line = br.readLine()) != null){
//			        System.out.println(line);
//			    }
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
	
	public static void write(String fileName, String str){
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

}
