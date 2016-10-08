package com.ssmksh.closestack.dao;

import java.util.List;

import com.ssmksh.closestack.dto.Instance;

public interface InstanceDAO {
	
	public void insertInstance(Instance instance);
	
	public List<Instance> getInstances(String userName);
	
	public Instance getInstance(String name, String userName);
	
	public void deleteInstance(String name, String userName);
	
	public void updateIp(String userName, String name, String ip);
	
	public void updateStatus(String userName, String name, String status);
	
	
}
