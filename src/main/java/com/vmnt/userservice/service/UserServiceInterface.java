package com.vmnt.userservice.service;

import com.vmnt.userservice.model.User;

public interface UserServiceInterface {
	
	/**
	 * Get a user by Id
	 * @param id Long
	 * @return A user as per the id
	 */
	public User getUserById(Long id);
	
	/**
	 * Add a new user
	 * @param user User
	 * @return the registered user
	 */
	public User addNewUser(User user);
	
}
