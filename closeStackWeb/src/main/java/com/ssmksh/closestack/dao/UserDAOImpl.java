package com.ssmksh.closestack.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.ssmksh.closestack.dto.User;

public class UserDAOImpl implements UserDAO {

	@Autowired
	MongoTemplate mongoTemplate;
	
	private static String COLLECTION_NAME = "ssmksh";
	
	@Override
	public User insert(User user) {
		// TODO Auto-generated method stub
		mongoTemplate.insert(user, COLLECTION_NAME);
		return user;
	}

	@Override
	public List<User> getUsers() {
		// TODO Auto-generated method stub
		return (List<User>) mongoTemplate.findAll(User.class, COLLECTION_NAME);
	}

	@Override
	public User getUser(User user) {
		// TODO Auto-generated method stub
		return mongoTemplate.findById(user.getId(), User.class, COLLECTION_NAME);
	}

	@Override
	public void deleteUser(User user) {
		// TODO Auto-generated method stub
		Query query = new Query(new Criteria("id").is(user.getId()));
		mongoTemplate.remove(query, COLLECTION_NAME);
	}

	@Override
	public User updateUser(User user) {
		// TODO Auto-generated method stub
		Query query = new Query(new Criteria("id").is(user.getId()));
		Update update = new Update();
		update.set("userName", user.getUserName());
		update.set("password", user.getPassword());
		mongoTemplate.updateFirst(query, update, COLLECTION_NAME);
		return user;
	}

}
