package example;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class JarLoad {
	public static void main(String[] args) {
		URLClassLoader classLoader;		
		String jarPath = System.getProperty("user.dir") + "/jars/TestPjt-0.0.1-SNAPSHOT-allinone.jar";
		System.out.println(jarPath);
		
		File jarFile = new File(jarPath);
		
		try {
			URL classURL = new URL("jar:" + jarFile.toURI().toURL() + "!/");
			classLoader = new URLClassLoader(new URL[] {classURL});
			Class<?> clazz = classLoader.loadClass("TestMain.Main");			
			Method method = clazz.getMethod("main", String[].class);
			String[] params = null; 
			method.invoke(null, (Object)params);			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
