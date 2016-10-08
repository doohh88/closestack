package com.ssmksh.closestack.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ssmksh.closestack.C;
import com.ssmksh.closestack.dao.FlavorDAO;
import com.ssmksh.closestack.dao.InstanceDAO;
import com.ssmksh.closestack.dao.SnapShotDAO;
import com.ssmksh.closestack.dto.Flavor;
import com.ssmksh.closestack.dto.Instance;
import com.ssmksh.closestack.dto.SnapShot;
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
@RequestMapping("/instance")
public class InstanceController {
	private final Logger log = LoggerFactory.getLogger(InstanceController.class);

	@Autowired
	InstanceDAO instanceDAO;

	@Autowired
	FlavorDAO flavorDAO;

	@Autowired
	SnapShotDAO snapShotDAO;

	private Instance crrInctance;
	private User crrUser;

	@RequestMapping(value = "/instanceMain", method = RequestMethod.GET)
	public String instanceMain(HttpSession session, Model model) {
		log.info("instanceMain()");

		User user = (User) session.getAttribute("userLoginInfo");
		log.info("session Id: " + user.getUsername());

		/*
		 * Flavor tiny = new Flavor("tiny",1,10,1); Flavor small = new
		 * Flavor("small",2,20,1); Flavor medium = new Flavor("medium",3,30,1);
		 * Flavor large = new Flavor("large",4,50,1);
		 * 
		 * flavorDAO.insertFlavor(tiny); flavorDAO.insertFlavor(small);
		 * flavorDAO.insertFlavor(medium); flavorDAO.insertFlavor(large);
		 */

		// ¿Ã∫Œ∫– error
		List<Instance> instanceList = instanceDAO.getInstances(user.getUsername());
		List<Flavor> flavorList = flavorDAO.getFlavors();

		List<String> osList = new ArrayList<String>();
		osList.add("ubuntu");
		osList.add("centos");

		model.addAttribute("instanceList", instanceList);
		model.addAttribute("flavorList", flavorList);
		model.addAttribute("flavorList2", flavorList);
		model.addAttribute("osList", osList);

		return "instance/instanceMain";
	}

	@RequestMapping(value = "/createInstanceProc", method = RequestMethod.POST)
	private String createInstanceProc(HttpSession session, HttpServletRequest request, Model model) {
		// TODO Auto-generated method stub

		User user = (User) session.getAttribute("userLoginInfo");

		System.out.println("createInstanceProc : " + user.getUsername());

		model.addAttribute("request", request);

		String instanceId = request.getParameter("instanceId");
		String instancePW = request.getParameter("instancePw");
		String flavorName = request.getParameter("dropFlavorName");
		String osName = request.getParameter("osName");
		String type = request.getParameter("type");

		System.out.println(instanceId + " " + instancePW + " " + flavorName + " " + osName + " " + type);

		Flavor flavor = flavorDAO.getFlavor(flavorName);
		Instance instance = new Instance(user.getUsername(), instanceId, instancePW, osName, type, "0", "running",
				"0", flavor);
		crrInctance = instance;
		crrUser = user;

		Net net = Net.getInstance();
		ActorRef netActor = net.getNetActor();

		ActorSelection master = net.getMaster();
		Timeout timeout = new Timeout(Duration.create(5, "seconds"));
		Future<Object> future = Patterns.ask(master, new TellCommand<Instance>("web", "ask", "generate", instance),
				timeout);
		try {
			TellCommand result = (TellCommand) Await.result(future, timeout.duration());
			if (result.getCommand().equals("createSuccess")) {
				Instance instance2 = (Instance) result.getData();

				System.out.println("createSuccess  " + instance2.getIp());
				instanceDAO.insertInstance(instance2);

			} else if (result.getCommand().equals("createFail")) {
				System.out.println("createFail  ");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "redirect:/instance/instanceMain";
	}

	@RequestMapping(value = "/commandInstanceProc", method = RequestMethod.POST)
	private String deleteInstanceProc(HttpSession session, HttpServletRequest request, Model model) {
		// TODO Auto-generated method stub

		User user = (User) session.getAttribute("userLoginInfo");

		String instanceName = request.getParameter("instanceName");
		String command = request.getParameter("command");
		Instance instance = instanceDAO.getInstance(instanceName, user.getUsername());
		System.out.println("commandInstanceProc : " + instance.getName() + " do : " + command);

		Net net = Net.getInstance();

		ActorRef netActor = net.getNetActor();
		netActor.tell(new TellCommand<Instance>("web", "tell", command, instance), ActorRef.noSender());

		if (command.equals("delete"))
			instanceDAO.deleteInstance(instanceName, user.getUsername());
		else if (command.equals("start")) {
			instanceDAO.updateStatus(user.getUsername(), instanceName, "running");
		} else if (command.equals("restart")) {
			instanceDAO.updateStatus(user.getUsername(), instanceName, "running");
		} else if (command.equals("stop")) {
			instanceDAO.updateStatus(user.getUsername(), instanceName, "stop");
		}

		return "redirect:/instance/instanceMain";
	}

	@RequestMapping(value = "/createSnapShotProc", method = RequestMethod.POST)
	private String createSnapShotProc(HttpSession session, HttpServletRequest request, Model model) {
		// TODO Auto-generated method stub
		System.out.println("createSnapShotProc : ");

		User user = (User) session.getAttribute("userLoginInfo");
		String instanceName = request.getParameter("instanceName");
		String snapShopName = request.getParameter("snapShotName");
		String newName = request.getParameter("newName");

		System.out.println("createSnapShotProc : " + instanceName + "/" + snapShopName + "/" + newName);

		LocalDate today = LocalDate.now();
		LocalTime now = LocalTime.now();
		int hour = now.getHour();
		int min = now.getMinute();

		String sHour;
		String sMin;

		if (hour < 10) {
			sHour = "0" + hour;
		} else {
			sHour = "" + hour;
		}

		if (min < 10) {
			sMin = "0" + min;
		} else {
			sMin = "" + min;
		}
		String time = today.getYear() + "-" + today.getMonthValue() + "-" + today.getDayOfMonth() + " " + sHour + ":" + sMin;
		System.out.println(time);

		Instance instance = instanceDAO.getInstance(instanceName, user.getUsername());

		SnapShot snapshot = new SnapShot(null, snapShopName, instanceName, instance.getType(), time, newName,
				instance.getIp());
		Net net = Net.getInstance();

		ActorRef netActor = net.getNetActor();
		netActor.tell(new TellCommand<SnapShot>("web", "tell", "snapshot", snapshot), ActorRef.noSender());

		snapShotDAO.insertSnapShot(snapshot);

		return "redirect:/instance/instanceMain";
	}

}
