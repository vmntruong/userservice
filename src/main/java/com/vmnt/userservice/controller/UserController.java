package com.vmnt.userservice.controller;

import static com.vmnt.userservice.controller.UserServiceConstants.*;

import java.time.LocalDate;
import java.time.Period;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vmnt.userservice.constants.ApiUserResponse;
import com.vmnt.userservice.exception.UserNotAllowedException;
import com.vmnt.userservice.exception.UserNotFoundException;
import com.vmnt.userservice.model.User;
import com.vmnt.userservice.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(tags= {"API User"})
@RequestMapping(path = "/api")
@Validated
public class UserController {
	
	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	/**
	 * Register a user
	 * @param input User
	 * @return the user registered
	 * @throws UserException if failed
	 */
	@PostMapping(value="/v1/user/register")
	@ApiOperation(value = "Register a new user" )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = ApiUserResponse.SUCCESS),
            @ApiResponse(code = 400, message = ApiUserResponse.BAD_REQUEST),
            @ApiResponse(code = 500, message = ApiUserResponse.SERVER_ON_ERROR)
            })   
	public ResponseEntity<Object> register(final @Valid @RequestBody User input,
						 				 final HttpServletRequest request) throws Exception {
		// Check input
		checkUserInput(input);
			
		final User registedUser = userService.addNewUser(input);
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(registedUser);
	}
	
	/**
	 * Get a user with id
	 * @param id userId
	 * @return the user to be returned or NULL if not exist
	 */
	@ApiOperation(value = "Get a user by Id" )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = ApiUserResponse.SUCCESS),
            @ApiResponse(code = 404, message = ApiUserResponse.NOT_FOUND),
            @ApiResponse(code = 500, message = ApiUserResponse.SERVER_ON_ERROR)
            })  
	@GetMapping(value="/v1/user/{id}")
	public ResponseEntity<User> getUser(@PathVariable long id) {
		final User user = userService.getUserById(id);
		if (null == user) {
			throw new UserNotFoundException(String.format("User with id %d not found", id));
		}
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(user);
	}
	
	// Private methods =================================================================
	private void checkUserInput(User user) {
		if (user != null) {
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
