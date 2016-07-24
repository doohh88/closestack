package com.ssmksh.closestack.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/instance")
public class InstanceController {
	private final Logger log = LoggerFactory.getLogger(InstanceController.class);
	
	@RequestMapping(value = "/instanceMain", method = RequestMethod.GET)
	public String instanceMain(){
		log.info("instanceMain()");
		return "instance/instanceMain";
	}
}
