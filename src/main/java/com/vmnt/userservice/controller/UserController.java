package com.vmnt.userservice.controller;


import java.net.URI;
import java.net.URISyntaxException;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.vmnt.userservice.constants.ApiUserResponse;
import com.vmnt.userservice.dto.UserDto;
import com.vmnt.userservice.model.User;
import com.vmnt.userservice.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(tags= {"API User"})
@Validated
public class UserController {
	
	@Autowired
	private ModelMapper modelMapper;
	
	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	/**
	 * Register a user
	 * @param input : the user to be registered
	 * @return the registered user
	 * @throws URISyntaxException 
	 */
	@PostMapping(value="/users")
	@ApiOperation(value = "Register a new user", notes = "The id of the registered user will be"
			+ " provided in the headers Location of the response in case of success")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = ApiUserResponse.CREATED),
			@ApiResponse(code = 400, message = ApiUserResponse.BAD_REQUEST),
			@ApiResponse(code = 500, message = ApiUserResponse.SERVER_ON_ERROR)
	})
	@ResponseStatus(value = HttpStatus.CREATED)
	public ResponseEntity<Void> register(final @Valid @RequestBody UserDto input) throws URISyntaxException {
		
		// convert DTO to entity
		User user = modelMapper.map(input, User.class);
		
		final User registeredUser = userService.addNewUser(user);
		URI locationUri = new URI("/users/" + registeredUser.getId());
		
		return ResponseEntity
				.created(locationUri)
				.build();
	}
	
	/**
	 * Get a user with id
	 * @param id : user's id
	 * @return the user with the id provided or 404 if not found
	 */
	@GetMapping(value="/users/{id}")
	@ApiOperation(value = "Get a user by Id", notes = "Returns a user as per the id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = ApiUserResponse.SUCCESS),
            @ApiResponse(code = 404, message = ApiUserResponse.NOT_FOUND),
            @ApiResponse(code = 500, message = ApiUserResponse.SERVER_ON_ERROR)
            })  
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseEntity<UserDto> getUser(@PathVariable("id") @ApiParam(name = "id", value = "user id", example = "1") 
	Long id) {
		
		final User user = userService.getUserById(id);
		
		// Convert entity to DTO
		UserDto userDto = modelMapper.map(user, UserDto.class);
		
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(userDto);
	}
	
}
