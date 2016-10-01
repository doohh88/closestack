package com.ssmksh.closestack.controller;

import java.util.ArrayList;
import java.util.List;

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

import com.ssmksh.closestack.dao.FlavorDAO;
import com.ssmksh.closestack.dao.InstanceDAO;
import com.ssmksh.closestack.dto.Flavor;
import com.ssmksh.closestack.dto.Instance;
import com.ssmksh.closestack.dto.User;
import com.ssmksh.closestack.service.InstanceService;

@Service
@Controller
@RequestMapping("/instance")
public class InstanceController {
	private final Logger log = LoggerFactory.getLogger(InstanceController.class);
	
	@Autowired
	InstanceService instanceService;
	

	
	@Autowired
	FlavorDAO flavorDAO;

	
	@RequestMapping(value = "/instanceMain", method = RequestMethod.GET)
	public String instanceMain(HttpSession session,	Model model){
		log.info("instanceMain()");
		
		User user = (User)session.getAttribute("userLoginInfo");
		log.info("session Id: "+user.getUsername());

		
/*		Flavor tiny = new Flavor("tiny",1,1,512);
		Flavor small = new Flavor("small",1,1,1);
		Flavor medium = new Flavor("medium",2,2,2);
		Flavor large = new Flavor("large",4,4,4);
		
		flavorDAO.insertFlavor(tiny);
		flavorDAO.insertFlavor(small);
		flavorDAO.insertFlavor(medium);
		flavorDAO.insertFlavor(large);
		*/
		
		List<Instance> instanceList = instanceService.getInstances(user.getUsername());
		List<Flavor> flavorList = flavorDAO.getFlavors();
		
		List<String> osList = new ArrayList<String>();
		osList.add("ubuntu");
		osList.add("centOs");
		osList.add("container");
	
		//model.addAttribute("instanceList", instanceList);
		model.addAttribute("flavorList", flavorList);
		model.addAttribute("flavorList2", flavorList);
		model.addAttribute("osList", osList);
		
		
		
		
		return "instance/instanceMain";
	}
	
	@RequestMapping(value = "/createInstanceProc", method = RequestMethod.POST)
	private String createInstanceProc(HttpSession session,HttpServletRequest request, Model model) {
		// TODO Auto-generated method stub

		User user = (User)session.getAttribute("userLoginInfo");
		
		System.out.println("createInstanceProc : "+ user.getUsername());
	
		model.addAttribute("request", request);
		instanceService.insertInstance(model, user.getUsername());

		return "redirect:/instance/instanceMain";
	}

	

}
