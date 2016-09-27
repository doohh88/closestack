package com.ssmksh.closestack.dao;

import java.util.List;

import com.ssmksh.closestack.dto.User;

public interface UserDAO {
	public User insert(User user);
	
	public List<User> getUsers();
	
	public User getUser(String name);
	
	public void deleteUser(User user);
	
	public User updateUser(User user);
}
