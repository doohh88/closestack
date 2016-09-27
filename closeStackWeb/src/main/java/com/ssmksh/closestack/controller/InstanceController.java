package com.ssmksh.closestack.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ssmksh.closestack.dto.Flavor;
import com.ssmksh.closestack.dto.User;

@Controller
@RequestMapping("/instance")
public class InstanceController {
	private final Logger log = LoggerFactory.getLogger(InstanceController.class);
	
	@RequestMapping(value = "/instanceMain", method = RequestMethod.GET)
	public String instanceMain(HttpSession session,	Model model){
		log.info("instanceMain()");
		
		User user = (User)session.getAttribute("userLoginInfo");
		log.info("session Id: "+user.getUsername());
		
		Flavor tiny = new Flavor("m1.tiny",1,1,512);
		Flavor small = new Flavor("m1.small",1,1,1);
		Flavor medium = new Flavor("m1.medium",1,1,2);
		
		List<Flavor> flavorList = new ArrayList<Flavor>(); 
		flavorList.add(tiny);
		flavorList.add(small);
		flavorList.add(medium);
		
		model.addAttribute("flavorList", flavorList);
		model.addAttribute("flavorList2", flavorList);
		
		HashMap<String, Flavor> map= new HashMap<String, Flavor>();
		for(int i=0;i<flavorList.size();i++)
			map.put(flavorList.get(i).getName(),flavorList.get(i));
		
		model.addAttribute("hashMapFlavor",map);
		
		
		return "instance/instanceMain";
	}
	
/*	@RequestMapping(value = "/instanceMain#", method = RequestMethod.POST)
	public String instanceModal(HttpSession session,HttpServletRequest request){
		log.info("instance#()");
		
		User user = (User)session.getAttribute("userLoginInfo");
		
		log.info("session Id: "+user.getUsername());
		
				
		
		return "overview/overviewMain";
	}*/
}
