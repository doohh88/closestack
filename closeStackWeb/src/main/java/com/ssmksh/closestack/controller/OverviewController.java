package com.ssmksh.closestack.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/overview")
public class OverviewController {
	private final Logger log = LoggerFactory.getLogger(OverviewController.class);
	
	@RequestMapping(value = "/overviewMain", method = RequestMethod.GET)
	public String overviewMain(){
		log.info("overviewMain()");
		return "overview/overviewMain";
	}
	
}
