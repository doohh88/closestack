package com.ssmksh.closestack.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.ssmksh.closestack.dao.UserDAO;
import com.ssmksh.closestack.dto.User;

@Service
public class UserServiceImpl implements UserService {
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserDAO userDAO;

	@Override
	public void insertUser(Model model) {
		logger.info("userServiceImple.insertUser");
		Map<String, Object> map = model.asMap();
		HttpServletRequest request = (HttpServletRequest) map.get("request");
		String username = request.getParameter("username");
		System.out.println(username);
		String password = request.getParameter("password");
		System.out.println(password);
		String email = request.getParameter("email");
		System.out.println(email);
		User user = new User(username, password, email);
		userDAO.insert(user);
	}

	@Override
	public List<User> getUsers() {
		return userDAO.getUsers();
	}

	@Override
	public User getUser(String user) {
		logger.info("userServiceImple.getUser: {}", user);
		return (User) userDAO.getUser(user);
	}

	@Override
	public boolean deleteUser(User user) {
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
		logger.info("userServiceImple.updateUser: {}" + user);

		return userDAO.updateUser(user);

	}

}
