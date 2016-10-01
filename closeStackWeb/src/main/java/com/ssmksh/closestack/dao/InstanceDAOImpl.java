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
	public Instance insert(Instance instance) {
		// TODO Auto-generated method stub
		log.info("insert()");
		repository.save(instance);
		return instance;
	}


	@Override
	public Instance getInstance(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteInstance(Instance instance) {
		// TODO Auto-generated method stub
		this.repository.delete(instance);
		
	}

	@Override
	public Instance updatedeleteInstancer(Instance instance) {
		// TODO Auto-generated method stub
		return null;
	}
	


	@Override
	public List<Instance> getInstancesbyUserName(String userName) {
		log.info("Instance findByUserName: "+userName);
		
		
		log.info("Instance findByUserName: "+repository.count());
		/*
		Iterable<Instance> iterable = this.repository.findAll();
		
		repository.findAll();
		
		List<Instance> intanceList = new ArrayList<Instance>();
		
		for(Instance item:iterable){
			if(item.getuserName().equals(userName))		
				intanceList.add(item);
		}
		
		*/
		
		List<Instance> intanceList = new ArrayList<Instance>();
		
		return intanceList;
	}

}
