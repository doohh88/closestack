package com.doohh.akkaClustering.deploy;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;

import com.doohh.akkaClustering.util.Node;

import akka.actor.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class AppConf implements Serializable{
	String masterIP;
	String port;
	String jarPath;
	String classPath;	
	int parallelism;
	String[] args;

	//decide in build
	String masterURL;
	File jarFile;
	
	//decide in runtime
	
	private AppConf(Builder builder){
		masterIP = builder.masterIP;
		port = builder.port;
		jarPath = builder.jarPath;
		classPath = builder.classPath;
		parallelism = builder.parallelism;
		args = builder.args;
		masterURL = builder.masterURL;
		jarFile = builder.jarFile;		
	}
	
	public static class Builder {
		String masterIP = "127.0.0.1";
		String port = "2551";
		String jarPath = null;
		String classPath = null;
		int parallelism = 1;
		String[] args = null;
		
		String masterURL = null;
		File jarFile = null;

		
		public Builder() {};
		
		public Builder masterIP(String masterIP){
			this.masterIP = masterIP;
			return this;
		}
		public Builder port(String port){
			this.port = port;
			return this;
		}
		public Builder jarPath(String jarPath){
			this.jarPath = jarPath;
			return this;
		}
		public Builder classPath(String classPath){
			this.classPath = classPath;
			return this;
		}
		public Builder parallelism(int parallelism){
			this.parallelism = parallelism;
			return this;
		}
		public Builder args(String[] args){
			this.args = args;
			return this;
		}
		
		public AppConf build(){
			this.masterURL = "akka.tcp://deepDist@" + this.masterIP + ":" + this.port + "/user/master";
			this.jarFile = new File(this.jarPath);
			return new AppConf(this);
		}
		
	}
}
