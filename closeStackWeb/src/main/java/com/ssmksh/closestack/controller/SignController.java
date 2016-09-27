package com.ssmksh.closestack.controller;

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

import com.ssmksh.closestack.dto.User;
import com.ssmksh.closestack.service.UserService;

@Controller
@Service
@RequestMapping("/sign")
public class SignController {
	private final Logger log = LoggerFactory.getLogger(SignController.class);

	@Autowired
	UserService userService;

	@RequestMapping(value = "/signIn", method = RequestMethod.GET)
	public String signIn() {
		log.info("signIn()");
		return "/sign/signIn";
	}

	@RequestMapping(value = "/signUp", method = RequestMethod.GET)
	private String signUp() {
		// TODO Auto-generated method stub
		log.info("signUp()");

		return "/sign/signUp";
	}

	@RequestMapping(value = "/signInProc", method = RequestMethod.POST)
	private String signInProc(HttpSession session,HttpServletRequest request, Model model) {
		// TODO Auto-generated method stub
		System.out.println("signInProc");
		String id = request.getParameter("id");
		String pwd = request.getParameter("password");

		User user = userService.getUser(id);

		System.out.println(user.getUsername() + "'");
		if (id.equals(user.getUsername()) && pwd.equals(user.getPassword())) {
			
			session.setAttribute("userLoginInfo", user);
			return "redirect:/overview/overviewMain";
		}

		return "redirect:/sign/signIn";
	}

	@RequestMapping(value = "/signUpProc", method = RequestMethod.POST)
	private String signUpProc(HttpServletRequest request, Model model) {
		// TODO Auto-generated method stub
		log.info("signUpProc()");
		model.addAttribute("request", request);
		userService.insertUser(model);
		return "redirect:/sign/signIn";
	}

}
