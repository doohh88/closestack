package com.ssmksh.closestack.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ssmksh.closestack.dto.User;

@Controller
@RequestMapping("/overview")
public class OverviewController {
	private final Logger log = LoggerFactory.getLogger(OverviewController.class);
	
	@RequestMapping(value = "/overviewMain", method = RequestMethod.GET)
	public String overviewMain(HttpSession session){
		log.info("overviewMain()");
		
		User user = (User)session.getAttribute("userLoginInfo");
		
		log.info("session Id: "+user.getUsername());
		
				
		
		return "overview/overviewMain";
	}
	
	
}
