package com.vmnt.userservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vmnt.userservice.model.User;
import com.vmnt.userservice.repo.UserRepo;

@Service
public class UserService {
	
	private UserRepo userRepo;
	
	@Autowired
	public UserService(UserRepo userRepo) {
		this.userRepo = userRepo;
	}
	
	public User getUserById(Long id) {
		return userRepo.findById(id).orElse(null);
	}
	
	public User addNewUser(User user) {
		return userRepo.save(user);
	}
	
}
