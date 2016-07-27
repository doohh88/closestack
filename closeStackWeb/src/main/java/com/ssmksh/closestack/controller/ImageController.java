
package com.ssmksh.closestack.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/image")
public class ImageController {
	private final Logger log = LoggerFactory.getLogger(ImageController.class);
	
	@RequestMapping(value = "/imageMain", method = RequestMethod.GET)
	public String imageMain(){
		log.info("imageMain()");
		return "image/imageMain";
	}
}
