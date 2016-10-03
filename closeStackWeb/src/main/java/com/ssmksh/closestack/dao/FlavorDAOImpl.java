package com.ssmksh.closestack.dao;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.ssmksh.closestack.dto.Flavor;

@Repository
@Service
public class FlavorDAOImpl implements FlavorDAO {
	private Logger log = LoggerFactory.getLogger(FlavorDAOImpl.class);
	
	@Autowired
	private FlavorRepository repository;

	@Override
	public void insertFlavor(Flavor flavor) {
		// TODO Auto-generated method stub
		repository.save(flavor);
		
	}
	
	
	@Override
	public List<Flavor> getFlavors() {
		
		Iterable<Flavor> iterable= repository.findAll();
		List<Flavor> flavorList = new ArrayList<Flavor>();
		
		for(Flavor item:iterable){
			flavorList.add(item);
		}
		return flavorList;
	}

	@Override
	public Flavor getFlavor(String name) {
		// TODO Auto-generated method stub
		return repository.findByname(name);
	}




}
