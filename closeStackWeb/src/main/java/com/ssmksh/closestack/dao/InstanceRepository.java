package com.ssmksh.closestack.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.ssmksh.closestack.dto.Instance;

public interface InstanceRepository extends PagingAndSortingRepository<Instance, String> {
	Instance findByname(String name);
	Instance deleteByname(String name);
}
