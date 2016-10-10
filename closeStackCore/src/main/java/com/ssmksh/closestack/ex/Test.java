package com.ssmksh.closestack.ex;

import java.net.URI;

public class Test {
	public static void main(String[] args) {
		String address = "akka.tcp://closestack@127.0.0.1:12345";
		String host = null;
		String ip_port = address.substring(22, address.length());
		System.out.println(ip_port);
		URI uri;
		try {
			uri = new URI("my://" + ip_port);
			host = uri.getHost();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		System.out.println(host);
	}
}
