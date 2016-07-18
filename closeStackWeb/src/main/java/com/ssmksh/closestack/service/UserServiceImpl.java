package com.ssmksh.closestack.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ssmksh.closestack.dao.UserDAO;
import com.ssmksh.closestack.dto.User;

public class UserServiceImpl implements UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
	private UserDAO userDAO;
	
	@Override
	public void insertUser(User user) {
		// TODO Auto-generated method stub
		logger.info("userServiceImple.insertUser: {}" + user);
		userDAO.insert(user);
	}

	@Override
	public List<User> getUsers() {
		// TODO Auto-generated method stub
		return userDAO.getUsers();
	}

	@Override
	public User getUser(User user) {
		// TODO Auto-generated method stub
		logger.info("userServiceImple.getUser: {}", user);
		return (User)userDAO.getUser(user);
	}

	@Override
	public boolean deleteUser(User user) {
		// TODO Auto-generated method stub
		logger.info("userServiceImple.delUser: {}" + user);
	    try {
	      userDAO.deleteUser(user);
	      return true;
	    } catch (Exception e) {
	      return false;
	    }
	}

	@Override
	public User updateUser(User user) {
		// TODO Auto-generated method stub
		logger.info("userServiceImple.updateUser: {}" + user);

	    return userDAO.updateUser(user);
	}

}
