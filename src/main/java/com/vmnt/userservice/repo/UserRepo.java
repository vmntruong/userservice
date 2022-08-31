package com.vmnt.userservice.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vmnt.userservice.model.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
	/**
	 * Find a user by name
	 * @param name a name to find
	 * @return the optional user with the name provided
	 */
	public Optional<User> findByName(String name);
	
}
