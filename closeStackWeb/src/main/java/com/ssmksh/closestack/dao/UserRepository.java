package com.ssmksh.closestack.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.ssmksh.closestack.dto.User;

public interface UserRepository extends PagingAndSortingRepository<User, String> {
	User findByUsername(String username);
	User findByEmail(String email);
}
