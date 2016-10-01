package com.ssmksh.closestack.service;

import java.util.List;

import org.springframework.ui.Model;

import com.ssmksh.closestack.dto.Instance;
import com.ssmksh.closestack.dto.User;

public interface InstanceService {
	  public void insertInstance(Model model, String userName);

	  public List<Instance> getInstances(String userName);

	  public Instance getInstance(String name);

	  public boolean deleteInstance(Instance instance);

	  public User updateInstancer(Instance instance);
}
