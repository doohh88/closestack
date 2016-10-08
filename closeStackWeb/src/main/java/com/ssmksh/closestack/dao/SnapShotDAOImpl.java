package com.ssmksh.closestack.dao;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.ssmksh.closestack.dto.Flavor;
import com.ssmksh.closestack.dto.SnapShot;

@Repository
@Service
public class SnapShotDAOImpl implements SnapShotDAO {
	private Logger log = LoggerFactory.getLogger(FlavorDAOImpl.class);
	
	@Autowired
	private SnapShotRepository repository;

	@Override
	public void insertSnapShot(SnapShot snapShot) {
		// TODO Auto-generated method stub
		repository.save(snapShot);
	}

	@Override
	public List<SnapShot> getSnapShots() {
		// TODO Auto-generated method stub
		

		Iterable<SnapShot> iterable= repository.findAll();
		List<SnapShot> snapShopList = new ArrayList<SnapShot>();
		
		for(SnapShot item:iterable){
			snapShopList.add(item);
		}
		return snapShopList;
	}

	@Override
	public SnapShot findByName(String name) {
		// TODO Auto-generated method stub
		return repository.findByname(name);
	}

	@Override
	public void delete(String name) {
		// TODO Auto-generated method stub
		repository.delete(repository.findByname(name));
	}



}
