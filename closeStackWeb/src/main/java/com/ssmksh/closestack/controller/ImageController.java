
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

import com.ssmksh.closestack.dao.SnapShotDAO;
import com.ssmksh.closestack.dto.SnapShot;
import com.ssmksh.closestack.dto.TellCommand;
import com.ssmksh.closestack.dto.User;
import com.ssmksh.closestack.network.Net;

import akka.actor.ActorRef;

@Service
@Controller
@RequestMapping("/image")
public class ImageController {
	private final Logger log = LoggerFactory.getLogger(ImageController.class);
	
	@Autowired
	SnapShotDAO snapShotDAO;
	
	@RequestMapping(value = "/imageMain", method = RequestMethod.GET)
	public String imageMain(Model model){
		log.info("imageMain()");

		List<SnapShot> snap = snapShotDAO.getSnapShots();
		
		model.addAttribute("snapShopList", snap);
		
		
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
		
	
		return "redirect:/image/imageMain";
	}
	
	
	@RequestMapping(value = "/deleteSnapShotProc", method = RequestMethod.POST)
	private String deleteSnapShotProc(HttpSession session,HttpServletRequest request) {
		
		log.info("deleteSnapShotProc()");
		User user = (User)session.getAttribute("userLoginInfo");
		String snapShotName = request.getParameter("snapShotName");
		
		Net net = Net.getInstance();
		ActorRef netActor = net.getNetActor();
		netActor.tell(new TellCommand<SnapShot>("web", "tell", "deleteSnapshot", snapShotDAO.findByName(snapShotName)), ActorRef.noSender());
	
		
		snapShotDAO.delete(snapShotName);
	
		return "redirect:/image/imageMain";
	}
}
