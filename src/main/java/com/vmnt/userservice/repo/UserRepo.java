package com.vmnt.userservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vmnt.userservice.model.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
	
}
