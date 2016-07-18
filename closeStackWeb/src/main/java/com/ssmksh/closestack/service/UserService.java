package com.ssmksh.closestack.service;

import java.util.List;

import com.ssmksh.closestack.dto.User;

public interface UserService {
	public void insertUser(User user);
	public List<User> getUsers();
	public User getUser(User user);
	public boolean deleteUser(User user);
	public User updateUser(User user);
}
