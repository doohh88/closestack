package com.ssmksh.closestack.dao;

import java.util.List;

import com.ssmksh.closestack.dto.Instance;

public interface InstanceDAO {
	public Instance insert(Instance instance);
	
	public List<Instance> getInstancesbyUserName(String userName);
	
	public Instance getInstance(String name);

	public void deleteInstance(Instance instance);
	
	public Instance updatedeleteInstancer(Instance instance);
}
