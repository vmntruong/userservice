package com.vmnt.userservice.service;

import static com.vmnt.userservice.controller.UserServiceConstants.*;

import java.time.LocalDate;
import java.time.Period;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vmnt.userservice.exception.InvalidInputException;
import com.vmnt.userservice.exception.UserNotAllowedException;
import com.vmnt.userservice.exception.UserNotFoundException;
import com.vmnt.userservice.model.User;
import com.vmnt.userservice.repo.UserRepo;

@Service
public class UserService implements UserServiceInterface {
	
	private UserRepo userRepo;
	
	@Autowired
	public UserService(UserRepo userRepo) {
		this.userRepo = userRepo;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public User getUserById(Long id) {
		User user = userRepo.findById(id).orElse(null);
		if (null == user) {
			throw new UserNotFoundException(String.format(MSG_USER_WITH_ID_NOT_FOUND, id));
		}
		return user;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public User addNewUser(User user) {
		// Check user input
		checkUserInput(user);
		
		return userRepo.save(user);
	}
	
	// Private methods =================================================================
	private void checkUserInput(User user) {
		if (user != null) {
			// Check user existed
			final String userName = user.getName();
			if (userName != null && userRepo.findByName(userName).isPresent()) {
				throw new InvalidInputException(String.format(MSG_USER_WITH_NAME_ALREADY_EXISTED, userName));
			}
			
			// Only adult French residents can register
			final LocalDate dob = user.getDayOfBirth();
			final int age = Period.between(dob, LocalDate.now()).getYears();
			final String country = user.getResidenceCountry();
			if (!country.equalsIgnoreCase("France") || age < 18) {
				throw new UserNotAllowedException(MSG_ONLY_FRENCH_ADULTS);
			}
		}
	}
	
	// ================================================================= Private methods
	
}
