package com.ssmksh.closestack.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ssmksh.closestack.dao.FlavorDAO;
import com.ssmksh.closestack.dao.InstanceDAO;
import com.ssmksh.closestack.dto.Instance;
import com.ssmksh.closestack.dto.Overall;
import com.ssmksh.closestack.dto.TellCommand;
import com.ssmksh.closestack.dto.User;
import com.ssmksh.closestack.network.Net;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.pattern.Patterns;
import akka.util.Timeout;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

@Service
@Controller
@RequestMapping("/overview")
public class OverviewController {
	private final Logger log = LoggerFactory.getLogger(OverviewController.class);

	@Autowired
	InstanceDAO instanceDAO;

	@Autowired
	FlavorDAO flavorDAO;

	@RequestMapping(value = "/overviewMain", method = RequestMethod.GET)
	public String overviewMain(HttpSession session, Model model) {
		log.info("overviewMain()");

		Overall overall=null;
		Net net = Net.getInstance();
		ActorSelection master = net.getMaster();
		Timeout timeout = new Timeout(Duration.create(5, "seconds"));
		
		Future<Object> future = Patterns.ask(master, new TellCommand<String>("web", "ask", "getResource", "1"), timeout);
		try {
			TellCommand result =(TellCommand) Await.result(future, timeout.duration());
			if(result.getCommand().equals("resourceSuccess")){
				overall = (Overall) result.getData();
				model.addAttribute("rVCPUs", overall.getCpu());
				model.addAttribute("rRam", overall.getRam()/1024);
				model.addAttribute("rDisk", overall.getDisk()/1024);
				model.addAttribute("rIP", overall.getNumIp());
				
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		User user = (User) session.getAttribute("userLoginInfo");

		List<Instance> instanceList = instanceDAO.getInstances(user.getUsername());

		model.addAttribute("instanceList", instanceList);
		
		
		int totalInstance = 0;
		int totalVCPUs = 0;
		int totalIP = 0;
		int totalRAM = 0;
		int totalDisk = 0;

		int usingInstance = 0;
		int usingVCPUs = 0;
		int usingIP = 0;
		int usingRAM = 0;

		for (Instance instance : instanceList) {
			if (instance.getStatus().equals("running")) {
				usingInstance++;
				usingVCPUs += instance.getFlavor().getvCpus();
				usingIP++;
				usingRAM += instance.getFlavor().getRam();
			}

			totalVCPUs += instance.getFlavor().getvCpus();
			totalRAM += instance.getFlavor().getRam();
			totalDisk += instance.getFlavor().getDisk();
			totalInstance++;
			totalIP++;
		}
		


		model.addAttribute("totalInstance", totalInstance);
		model.addAttribute("totalVCPUs", totalVCPUs);
		model.addAttribute("totalIP", totalIP);
		model.addAttribute("totalRAM", totalRAM);
		model.addAttribute("totalDisk", totalDisk);
		
		model.addAttribute("usingInstance", usingInstance);
		model.addAttribute("usingVCPUs", usingVCPUs);
		model.addAttribute("usingIP", usingIP);
		model.addAttribute("usingRAM", usingRAM);

		return "overview/overviewMain";
	}

}
