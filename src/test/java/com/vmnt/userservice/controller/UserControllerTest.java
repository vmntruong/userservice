package com.vmnt.userservice.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
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
	private ObjectMapper objectMapper;
	
	@MockBean
	private UserService service;
	
	@MockBean
	private UserRepo repo;
	
	@BeforeEach
	void setUp() {
		repo.deleteAll();
		
	}

	@Test
	void testRegister_whenValidInput_thenReturns200() throws Exception {
		// given
		User user = new User(
				"AAAA",
				LocalDate.of(LocalDate.now().getYear() - 20, 07, 12),
				"France",
				"02343434",
				Gender.FEMALE
		);
		
		// when, then
		mockMvc.perform(post("/api/v1/user/register")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(user)))
				.andExpect(status().isOk());
		
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
	void testRegister_whenInvalidInput_thenReturns400() throws Exception {
		// given
		final User invalidUser = new User(
				null,
				LocalDate.of(LocalDate.now().getYear() - 20, 07, 12),
				"France",
				"02343434",
				Gender.MALE
		);
		
		// when, then
		mockMvc.perform(post("/api/v1/user/register")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(invalidUser)))
				.andExpect(status().isBadRequest());
		
	}
	
	@Test
	void testRegister_whenNotAllowedInput_thenReturns400() throws Exception {
		// given
		final User notFrenchUser = new User(
				"CCCCC",
				LocalDate.of(LocalDate.now().getYear() - 20, 07, 12),
				"Germany",
				"023465654",
				Gender.MALE
		);
		final User notAdultUser = new User(
				"DDDDDD",
				LocalDate.of(LocalDate.now().getYear() - 17, 07, 12),
				"France",
				"23242342",
				Gender.MALE
		);
		
		// when, then
		mockMvc.perform(post("/api/v1/user/register")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(notFrenchUser)))
				.andExpect(status().isBadRequest());
		mockMvc.perform(post("/api/v1/user/register")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(notAdultUser)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void testGetUser_whenNotFound_thenReturns404() throws Exception {
		
		// given
		final long userId = 1L;
		
		// when, then
		mockMvc.perform(get("/api/v1/user/{id}", userId))
				.andExpect(status().isNotFound());
		
	}
	
	@Test
	void testGetUser_whenFound_thenReturns200() throws Exception {
		
		// given
		final User user = new User(
				"EEEEE",
				LocalDate.of(LocalDate.now().getYear() - 20, 07, 12),
				"France",
				"2121313",
				Gender.FEMALE
		);
		final User savedUser = user;
		savedUser.setId(1L);
		
		// when
		when(service.getUserById(savedUser.getId())).thenReturn(savedUser);
		
		// when, then
		mockMvc.perform(get("/api/v1/user/{id}", savedUser.getId()))
				.andExpect(status().isOk());
		
	}

}
