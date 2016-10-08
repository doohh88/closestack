package com.ssmksh.closestack.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.ssmksh.closestack.dto.Instance;

@Repository
@Service
public class InstanceDAOImpl implements InstanceDAO {
	private Logger log = LoggerFactory.getLogger(InstanceDAOImpl.class);

	@Autowired InstanceRepository instanceRepository;
	
	@Autowired MongoTemplate mongoTemplate;
	
	@Override
	public void insertInstance(Instance instance) {
		// TODO Auto-generated method stub
	
		instanceRepository.save(instance);
		
	}

	@Override
	public List<Instance> getInstances(String userName) {
		// TODO Auto-generated method stub
	

		List<Instance> instanceList = instanceRepository.findByUserName(userName);
		
		
		return instanceList;
	}

	@Override
	public Instance getInstance(String name, String userName) {
		// TODO Auto-generated method stub
		return instanceRepository.findBynameAndUserName(name, userName);
	}

	@Override
	public void deleteInstance(String name, String userName) {
		// TODO Auto-generated method stub
		
		instanceRepository.delete(instanceRepository.findBynameAndUserName(name, userName));		
	}

	@Override
	public void updateIp(String userName, String name, String ip) {
		Query query = new Query();
		query.addCriteria(new Criteria("name").is(name));
		query.addCriteria(new Criteria("userName").is(userName));
		
		Update update = new Update();
		update.set("ip",ip);
		mongoTemplate.updateFirst(query, update, Instance.class);
		
	}

	@Override
	public void updateStatus(String userName, String name, String status) {
		Query query = new Query();
		query.addCriteria(new Criteria("name").is(name));
		query.addCriteria(new Criteria("userName").is(userName));
		
		Update update = new Update();
		update.set("status",status);
		mongoTemplate.updateFirst(query, update, Instance.class);
		
	}


}
