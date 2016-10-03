package com.ssmksh.closestack.dao;

import java.util.List;

import com.ssmksh.closestack.dto.Flavor;
import com.ssmksh.closestack.dto.Instance;

public interface InstanceDAO {
	
	public void insertInstance(Instance instance);
	
	public List<Instance> getInstances(String userName);
	
	
}
