package com.ssmksh.closestack.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.ssmksh.closestack.dao.FlavorDAO;
import com.ssmksh.closestack.dao.InstanceDAO;
import com.ssmksh.closestack.dto.Flavor;
import com.ssmksh.closestack.dto.Instance;
import com.ssmksh.closestack.dto.User;

@Transactional
@Service
public class InstanceServiceImpl implements InstanceService {
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private InstanceDAO instanceDAO;
	
	@Autowired
	private FlavorDAO flavorDAO;

	@Override
	public void insertInstance(Model model, String userName) {
		logger.info("instanceServiceImple.insertInstance");
		Map<String, Object> map = model.asMap();
		HttpServletRequest request = (HttpServletRequest) map.get("request");
		
		String instanceName = request.getParameter("instanceId");
		System.out.println(instanceName);
		String intancePw = request.getParameter("instancePw");
		System.out.println(intancePw);
		String flavorName = request.getParameter("dropFlavorName");
		System.out.println(flavorName);
		String osName = request.getParameter("osName");
		System.out.println(osName);
		
		
		Flavor flavor = flavorDAO.getFlavor(flavorName);
		
		Instance instance = new Instance(userName, instanceName, intancePw,osName,flavor);
		instanceDAO.insert(instance);
		
	}

	@Override
	public List<Instance> getInstances(String userName) {
		logger.info("instanceServiceImple.getInstances");
		return instanceDAO.getInstancesbyUserName(userName);
	}

	@Override
	public Instance getInstance(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteInstance(Instance instance) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public User updateInstancer(Instance instance) {
		// TODO Auto-generated method stub
		return null;
	}


	
}
