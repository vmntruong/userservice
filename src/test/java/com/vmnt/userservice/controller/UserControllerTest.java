package com.vmnt.userservice.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vmnt.userservice.dto.UserDto;
import com.vmnt.userservice.exception.UserNotAllowedException;
import com.vmnt.userservice.exception.UserNotFoundException;
import com.vmnt.userservice.model.Gender;
import com.vmnt.userservice.model.User;
import com.vmnt.userservice.repo.UserRepo;
import com.vmnt.userservice.service.UserService;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = UserController.class)
class UserControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private UserService service;
	
	@MockBean
	private UserRepo repo;
	
	@TestConfiguration
	static class TestConfig {
		@Bean
		public ModelMapper modelMapper() {
			return new ModelMapper();
		}
	}
	
	@BeforeEach
	void setUp() {
		repo.deleteAll();
	}

	@Test
	void testRegister_verifyCapturedUser() throws Exception {
		// given
		UserDto user = new UserDto(
				"AAAA",
				LocalDate.of(LocalDate.now().getYear() - 20, 7, 12),
				"France",
				"02343434",
				Gender.FEMALE
		);
		
		// when
		when(service.addNewUser(any())).thenReturn(new User());
		
		mockMvc.perform(post("/users")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(user)));
		
		// then
		// verify the captured user
		ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
		verify(service).addNewUser(userArgumentCaptor.capture());
		User capturedUser = userArgumentCaptor.getValue();
		assertThat(capturedUser.getName()).isEqualTo("AAAA");
		assertThat(capturedUser.getDayOfBirth()).isEqualTo(LocalDate.of(LocalDate.now().getYear() - 20, 07, 12));
		assertThat(capturedUser.getResidenceCountry()).isEqualTo("France");
		assertThat(capturedUser.getPhoneNumber()).isEqualTo("02343434");
		assertThat(capturedUser.getGender()).isEqualTo(Gender.FEMALE);
	}
	
	@Test
	void testRegister_whenValidInput_thenReturns201_andIdReturnedInLocatiton() throws Exception {
		// given
		final Long id = 1L;
		UserDto userDto = new UserDto(
				1L,
				"AAAA",
				LocalDate.of(LocalDate.now().getYear() - 20, 7, 12),
				"France",
				"02343434",
				Gender.FEMALE
		);
		User user = modelMapper.map(userDto, User.class);
		
		// when
		when(service.addNewUser(any())).thenReturn(user);
		
		// then
		mockMvc.perform(post("/users")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(userDto)))
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", "/users/" + id));
	}
	
	@Test
	void testRegister_whenInvalidInput_thenReturns400() throws Exception {
		// given
		final UserDto invalidUser = new UserDto(
				null,
				LocalDate.of(LocalDate.now().getYear() - 20, 7, 12),
				"France",
				"02343434",
				Gender.MALE
		);
		
		// when, then
		mockMvc.perform(post("/users")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(invalidUser)))
				.andExpect(status().isBadRequest());
		
	}
	
	@Test
	void testRegister_whenNotFrenchUser_thenReturns400() throws Exception {
		// given
		final UserDto notFrenchUser = new UserDto(
				"CCCCC",
				LocalDate.of(LocalDate.now().getYear() - 20, 7, 12),
				"Germany",
				"023465654",
				Gender.MALE
		);
		
		when(service.addNewUser(any())).thenThrow(UserNotAllowedException.class);
		
		// when, then
		mockMvc.perform(post("/users")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(notFrenchUser)))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	void testRegister_whenNotAdultUser_thenReturns400() throws Exception {
		final UserDto notAdultUser = new UserDto(
				"DDDDDD",
				LocalDate.of(LocalDate.now().getYear() - 17, 7, 12),
				"France",
				"23242342",
				Gender.MALE
		);
		
		when(service.addNewUser(any())).thenThrow(UserNotAllowedException.class);
		
		// when, then
		mockMvc.perform(post("/users")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(notAdultUser)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void testGetUser_whenNotFound_thenReturns404() throws Exception {
		
		// given
		final long userId = 1L;
		
		// when, then
		when(service.getUserById(any())).thenThrow(UserNotFoundException.class);
		
		mockMvc.perform(get("/users/{id}", userId))
				.andExpect(status().isNotFound());
		
	}
	
	@Test
	void testGetUser_whenFound_thenReturns200() throws Exception {
		
		// given
		final User user = new User(
				1L,
				"EEEEE",
				LocalDate.of(LocalDate.now().getYear() - 20, 7, 12),
				"France",
				"2121313",
				Gender.FEMALE
		);
		
		// when, then
		when(service.getUserById(user.getId())).thenReturn(user);
		
		mockMvc.perform(get("/users/{id}", user.getId()))
				.andExpect(status().isOk());
		
	}

}
