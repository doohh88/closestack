package com.ssmksh.closestack.dao;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.ssmksh.closestack.dto.Instance;

@Repository
@Service
public class InstanceDAOImpl implements InstanceDAO {
	private Logger log = LoggerFactory.getLogger(InstanceDAOImpl.class);
	
	@Autowired
	private InstanceRepository repository;

	@Override
	public void insertInstance(Instance instance) {
		// TODO Auto-generated method stub
		this.repository.save(instance);
		
	}

	@Override
	public List<Instance> getInstances(String userName) {
		// TODO Auto-generated method stub
		
		
		//Instance instance = this.repository.findByname("Aa");
		
		//System.out.println(instance.getName());
		
		List<Instance> instanceList = new ArrayList<Instance>();
		
		
		
		return instanceList;
	}


}
