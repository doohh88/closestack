package com.doohh.akkaClustering.worker;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import com.doohh.akkaClustering.deploy.AppConf;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Task extends UntypedActor {
	LoggingAdapter log = Logging.getLogger(getContext().system(), this);

	@Override
	public void onReceive(Object message) throws Throwable {
		if (message instanceof AppConf) {
			AppConf appConf = (AppConf) message;
			log.info("i'm task : {}", appConf);
			runApp(appConf);
			getSender().tell("complete task", getSelf());
			context().stop(getSelf());
		}
	}

	void runApp(AppConf appConf) {
		File jarFile = appConf.getJarFile();
		String classPath = appConf.getClassPath();
		String[] args = appConf.getArgs();
		
		try {
			URL classURL = new URL("jar:" + jarFile.toURI().toURL() + "!/");
			URLClassLoader classLoader = new URLClassLoader(new URL[] {classURL});
			Class<?> clazz = classLoader.loadClass(classPath);			
			Method method = clazz.getMethod("main", String[].class);
			method.invoke(null, (Object)args);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
