package com.ssmksh.closestack.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ssmksh.closestack.dto.IP;
import com.ssmksh.closestack.dto.Network;
import com.ssmksh.closestack.dto.TellCommand;
import com.ssmksh.closestack.network.Net;

import akka.actor.ActorSelection;
import akka.pattern.Patterns;
import akka.util.Timeout;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

@Controller
@RequestMapping("/network")
public class NetworkController {
	private final Logger log = LoggerFactory.getLogger(NetworkController.class);

	@RequestMapping(value = "/networkMain", method = RequestMethod.GET)
	public String networkMain(Model model) {
		log.info("networkMain()");

		Net net = Net.getInstance();
		ActorSelection master = net.getMaster();
		Timeout timeout = new Timeout(Duration.create(5, "seconds"));

		Future<Object> future = Patterns.ask(master, new TellCommand<String>("web", "ask", "getNetwork", "1"), timeout);
		try {
			TellCommand result = (TellCommand) Await.result(future, timeout.duration());

			if (result.getCommand().equals("networkSuccess")) {
				Network network = (Network) result.getData();

				List<IP> ipList = network.getIps();

				for (IP ip : ipList) {

					String host = ip.getHostIp();
					if (host.length() > 22)
						ip.setHostIp(host.substring(22, host.length()));
				}
				model.addAttribute("ipList", ipList);

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "network/networkMain";

	}
}
