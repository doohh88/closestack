package com.ssmksh.closestack;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ssmksh.closestack.dao.UserDAO;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger log = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		log.info("home()");
		return "redirect:sign/signIn";
	}
		
	@RequestMapping("/overview")
	public String overview(Model model){
		System.out.println("overview()");
		return "overview";
	}
	
	@RequestMapping("/instance")
	public String instance(Model model){
		System.out.println("instance");
		return "instance";
	}
	
	@RequestMapping("/image")
	public String image(Model model){
		System.out.println("image()");
		return "image";
	}
	
	@RequestMapping("/network")
	public String network(Model model){
		System.out.println("network()");
		return "network";
	}
	
	
}
