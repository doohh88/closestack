package com.ssmksh.closestack.service;

import java.util.List;

import org.springframework.ui.Model;

import com.ssmksh.closestack.dto.User;

public interface UserService {
	  public void insertUser(Model model);

	  public List<User> getUsers();

	  public User getUser(String name);

	  public boolean deleteUser(User user);

	  public User updateUser(User user);
}
