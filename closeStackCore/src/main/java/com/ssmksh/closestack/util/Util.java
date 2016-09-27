package com.ssmksh.closestack.util;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ssmksh.closestack.master.MasterMain;

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
			if(parseArgs == null){
				parser.parseArgument(args);
			}
			else {
				parser.parseArgument(parseArgs);
			}
		} catch (CmdLineException e) {
			log.info(e.getMessage());
			parser.printUsage(System.err);
		}

		return appArgs;

	}
}
