package com.ssmksh.closestack.query;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class QueryConf implements Serializable{
	String actorURL;
	String[] args;
	
	private QueryConf(Builder builder){
		actorURL = builder.actorURL;
		args = builder.args;
	}
	
	public static class Builder {
		String actorURL = null;
		String[] args = null;
		
		public Builder() {};
		
		public Builder actorURL(String actorURL){
			this.actorURL = actorURL;
			return this;
		}
		public Builder args(String[] args){
			this.args = args;
			return this;
		}
		
		public QueryConf build(){
			return new QueryConf(this);
		}
		
	}
}
