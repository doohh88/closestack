package com.ssmksh.closestack.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.ssmksh.closestack.dto.SnapShot;

public interface SnapShotRepository extends PagingAndSortingRepository<SnapShot, String> {
	SnapShot findByname(String name);
}
