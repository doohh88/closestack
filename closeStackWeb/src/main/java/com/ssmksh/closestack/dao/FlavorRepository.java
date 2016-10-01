package com.ssmksh.closestack.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.ssmksh.closestack.dto.Flavor;

public interface FlavorRepository extends PagingAndSortingRepository<Flavor, String> {
	Flavor findByname(String name);
}
