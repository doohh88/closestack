package com.ssmksh.closestack.ex;

import org.kohsuke.args4j.Option;

import com.ssmksh.closestack.dto.Instance;
import com.ssmksh.closestack.util.Util;

public class Main {
	@Option(name = "--cmd", usage = "cmd", aliases = "-cm")
	public static String cmd = "none";
	@Option(name = "--name", usage = "name", aliases = "-n")
	public static String name = "none";
	@Option(name = "--cpu", usage = "cpu", aliases = "-c")
	public static String cpu = "none";
	@Option(name = "--ram", usage = "ram", aliases = "-r")
	public static String ram = "none";
	@Option(name = "--disk", usage = "disk", aliases = "-d")
	public static String disk = "none";

	public static void main(String[] args) {
		Util.parseArgs(args, new Main());

		if (cmd.equals("list")) {
			list();
		} else if (cmd.equals("generate")) {
			generate(name);
		} else if (cmd.equals("delete")) {
			delete(name);
		} else if (cmd.equals("start")) {
			start(name);
		} else if (cmd.equals("stop")) {
			stop(name);
		}
	}

	public static void list() {
		System.out.println("lxc list");
		Util.exec("lxc list");
	}

	public static void generate(String name) {
		System.out.println("lxc launch ubuntu: " + name);
		Util.exec("lxc launch ubuntu: " + name);
	}

	public static void delete(String name) {
		System.out.println("lxc delete " + name);
		Util.exec("lxc delete " + name);
	}

	public static void start(String name) {
		System.out.println("lxc start " + name);
		Util.exec("lxc start " + name);
	}

	public static void stop(String name) {
		System.out.println("lxc stop " + name);
		Util.exec("lxc stop " + name);
	}

	void setCPU() {
		Util.exec("lxc config set " + name + " limits.cpu " + cpu);
	}

	void setRAM() {
		Util.exec("lxc config set " + name + " limits.memory " + ram + "GB");
	}

	void setDISK() {
		Util.exec("lxc config device set " + name + " root size " + disk + "GB");
	}
}
