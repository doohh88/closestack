package com.ssmksh.closestack.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ssmksh.closestack.dto.Instance;
import java.lang.String;

public interface InstanceRepository extends PagingAndSortingRepository<Instance, String> {
	Instance findByname(String name);
	List<Instance> findByUserName(String username);
	Instance findBynameAndUserName(String name, String userName);
	
	
		
}
