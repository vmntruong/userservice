package com.vmnt.userservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
	void testGetUserById() {
		// given
		final Long id = 1L;
		
		// when
		service.getUserById(id);
		
		// then
		verify(repo).findById(id);
	}

	@Test
	void testAddNewUser() {
		// given
		User user = new User(
				"AAAA",
				LocalDate.of(LocalDate.now().getYear() - 21, 01, 01),
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

}
