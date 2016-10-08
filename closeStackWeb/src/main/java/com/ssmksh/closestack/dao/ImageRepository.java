package com.ssmksh.closestack.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.ssmksh.closestack.dto.Image;

public interface ImageRepository extends PagingAndSortingRepository<Image, String> {
	Image findByname(String name);
}
