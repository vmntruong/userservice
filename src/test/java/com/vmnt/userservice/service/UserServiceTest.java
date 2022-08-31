package com.vmnt.userservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.vmnt.userservice.controller.UserServiceConstants.*;
import com.vmnt.userservice.exception.InvalidInputException;
import com.vmnt.userservice.exception.UserNotAllowedException;
import com.vmnt.userservice.exception.UserNotFoundException;
import com.vmnt.userservice.model.Gender;
import com.vmnt.userservice.model.User;
import com.vmnt.userservice.repo.UserRepo;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
	
	@Mock
	private UserRepo repo;
	
	private UserService service;
	
	@BeforeEach
	void setUp() {
		service = new UserService(repo);
	}

	@Test
	void testGetUserById_Success() {
		// given
		final Long id = 1L;
		User user = new User(
				id,
				"AAAA",
				LocalDate.of(LocalDate.now().getYear() - 21, 1, 1),
				"France",
				"02343434",
				Gender.MALE
				);

		// when
		when(repo.findById(id)).thenReturn(Optional.of(user));
		User result = service.getUserById(id);
		
		// then
		assertThat(result).isNotNull();
		assertThat(result.getName().equals("AAAA"));
	}

	@Test
	void testGetUserById_whenNotFound_thenThrowException() {
		// given
		final Long id = 1L;

		// when
		when(repo.findById(id)).thenReturn(Optional.empty());
		
		assertThatExceptionOfType(UserNotFoundException.class)
		.isThrownBy(() -> service.getUserById(id))
		.withMessage(String.format(MSG_USER_WITH_ID_NOT_FOUND, id));
		
	}

	@Test
	void testAddNewUser_verifyCapturedUser() {
		// given
		User user = new User(
				"AAAA",
				LocalDate.of(LocalDate.now().getYear() - 21, 1, 1),
				"France",
				"02343434",
				Gender.MALE
		);
		
		// when
		service.addNewUser(user);
		
		// then
		ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
		verify(repo).save(userArgumentCaptor.capture());
		
		User capturedUser = userArgumentCaptor.getValue();
		assertThat(capturedUser).isEqualTo(user);
	}
	
	@Test
	void testAddNewUser_Success() {
		// given
		User user = new User(
				"AAAA",
				LocalDate.of(LocalDate.now().getYear() - 21, 1, 1),
				"France",
				"02343434",
				Gender.MALE
		);
		
		// when
		when(repo.save(Mockito.any(User.class))).thenReturn(user);
		User savedUser = service.addNewUser(user);
		
		// then
		assertThat(savedUser).isNotNull();
		assertThat(savedUser.getName()).isEqualTo("AAAA");
	}
	
	@Test
	void testAddNewUser_whenNotFrenchResident_thenThrowException() {
		// given
		User user = new User(
				"AAAA",
				LocalDate.of(LocalDate.now().getYear() - 21, 1, 1),
				"USA",
				"02343434",
				Gender.MALE
		);
		
		// when, then
		assertThatExceptionOfType(UserNotAllowedException.class)
		.isThrownBy(() -> service.addNewUser(user))
		.withMessage(MSG_ONLY_FRENCH_ADULTS);
	}
	
	@Test
	void testAddNewUser_whenNotAdultUser_thenThrowException() {
		// given
		User user = new User(
				"AAAA",
				LocalDate.of(LocalDate.now().getYear() - 17, 1, 1),
				"France",
				"02343434",
				Gender.MALE
		);
		
		// when, then
		assertThatExceptionOfType(UserNotAllowedException.class)
		.isThrownBy(() -> service.addNewUser(user))
		.withMessage(MSG_ONLY_FRENCH_ADULTS);
	}

	@Test
	void testAddNewUser_whenUserExisted_thenThrowException() {
		// given
		User user1 = new User(
				"AAAA",
				LocalDate.of(LocalDate.now().getYear() - 22, 1, 1),
				"France",
				"02343434",
				Gender.MALE
		);
		
		User user2 = new User(
				"AAAA",
				LocalDate.of(LocalDate.now().getYear() - 32, 1, 1),
				"France",
				"02343232",
				Gender.FEMALE
		);
		
		when(service.addNewUser(Mockito.any(User.class))).thenReturn(user1);
		service.addNewUser(user1);
		
		when(repo.findByName(Mockito.anyString())).thenReturn(Optional.of(user1));
		
		// when, then
		assertThatExceptionOfType(InvalidInputException.class)
		.isThrownBy(() -> service.addNewUser(user2))
		.withMessage(String.format(MSG_USER_WITH_NAME_ALREADY_EXISTED, "AAAA"));
	}
}
