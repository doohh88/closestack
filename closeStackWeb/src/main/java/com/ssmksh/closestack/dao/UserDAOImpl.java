package com.ssmksh.closestack.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.ssmksh.closestack.dto.User;

@Repository
@Service
public class UserDAOImpl implements UserDAO {
	private Logger log = LoggerFactory.getLogger(UserDAOImpl.class);
	
	@Autowired
	private UserRepository repository;
	
	@Override
	public User insert(User user) {
		// TODO Auto-generated method stub
		log.info("insert()");
		this.repository.save(user);
		return user;
	}

	@Override
	public List<User> getUsers() {
		// TODO Auto-generated method stub
		log.info("getUsers()");
	
		
		return null;
	}

	@Override
	public User getUser(String username) {
		// TODO Auto-generated method stub
		return repository.findByUsername(username);
	}

	@Override
	public void deleteUser(User user) {
		// TODO Auto-generated method stub
		this.repository.delete(user);
	}

	@Override
	public User updateUser(User user) {
		// TODO Auto-generated method stub
		return null;
	}

}
