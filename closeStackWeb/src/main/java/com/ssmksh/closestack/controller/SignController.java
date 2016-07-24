package com.ssmksh.closestack.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ssmksh.closestack.service.UserService;

@Controller
@RequestMapping("/sign")
public class SignController {
	private final Logger log = LoggerFactory.getLogger(SignController.class);
	
	@Autowired
	UserService userService;
		
	@RequestMapping(value = "/signIn", method = RequestMethod.GET)
	public String signIn(){
		log.info("signIn()");
		return "/sign/signIn";
	}
	
	@RequestMapping(value="/signInProc", method=RequestMethod.POST)
	private String signInProc(HttpServletRequest request) {
		// TODO Auto-generated method stub
		log.info("signInProc()");
		
		
		return "redirect:/overview/overviewMain";
	}
	
	@RequestMapping(value="/signUp", method=RequestMethod.GET)
	private String signUp() {
		// TODO Auto-generated method stub
		log.info("signUp()");
		
		return "/sign/signUp";
	}
	
	@RequestMapping(value="/signUpProc", method=RequestMethod.POST)
	private String signUpProc(HttpServletRequest request, Model model) {
		// TODO Auto-generated method stub
		log.info("signUpProc()");
		userService.insertUser(model);
		return "redirect:/sign/signIn";
	}
}
