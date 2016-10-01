
package com.ssmksh.closestack.controller;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ssmksh.closestack.dto.Image;
import com.ssmksh.closestack.dto.User;

@Service
@Controller
@RequestMapping("/image")
public class ImageController {
	private final Logger log = LoggerFactory.getLogger(ImageController.class);
	
	@RequestMapping(value = "/imageMain", method = RequestMethod.GET)
	public String imageMain(Model model){
		log.info("imageMain()");
		
		Image image = new Image("kafka","Image","Active", false,false,"RAW");
		Image image2 = new Image("kafka2","Image","Active", false,false,"RAW");
		
		List<Image> images = new ArrayList<Image>();
		images.add(image);
		images.add(image2);
		
		model.addAttribute("imageList", images);
		
		
		return "image/imageMain";
	}
	
	
	@RequestMapping(value = "/imageDetail")
	public String imageDetail(Model model){
		log.info("imageDetail()");
		
		return "image/imageDetail";
	}
	
	@RequestMapping(value = "/createImage", method = RequestMethod.POST)
	private String createImage(HttpSession session,HttpServletRequest request, Model model) {
		
		log.info("createImage()");
		User user = (User)session.getAttribute("userLoginInfo");
		
	
		return "redirect:/Image/imageMain";
	}
}
