package com.ssmksh.closestack.dao;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.ssmksh.closestack.dto.Instance;
import com.ssmksh.closestack.dto.User;
import java.lang.String;

public interface InstanceRepository extends PagingAndSortingRepository<Instance, String> {
	   
	
}
