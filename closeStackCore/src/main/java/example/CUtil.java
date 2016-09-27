package example;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CUtil {
	 private static final Logger log = LoggerFactory.getLogger(CUtil.class);

	public static void load(String name) {
		try {
			log.info("Trying to load: {}", name);
			System.loadLibrary(name);
		} catch (Throwable e) {
			log.info("Failed: {}", e.getMessage());
			return;
		}
		log.info("success");
	}
	
	public static void parseArgs(String[] args, Object obj) {
		CmdLineParser parser = new CmdLineParser(obj);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
        	log.info(e.getMessage());
            parser.printUsage(System.err);
        }
	}

}
