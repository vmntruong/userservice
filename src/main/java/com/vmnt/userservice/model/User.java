package com.vmnt.userservice.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="users")
public class User {
	
	@Id
	@SequenceGenerator(
			name="users_sequence",
			sequenceName="users_sequence",
			allocationSize=1
	)
	@GeneratedValue(
			strategy=GenerationType.SEQUENCE,
			generator="users_sequence"
	)
	private Long id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="dayOfBirth")
	private LocalDate dayOfBirth;
	
	@Column(name="residenceCountry")
	private String residenceCountry;
	
	@Column(name="phoneNumber")
	private String phoneNumber;
	
	@Column(name="gender")
	@Enumerated(EnumType.STRING)
	private Gender gender;

	/**
	 * Constructor without id
	 */
	public User(String name, LocalDate dayOfBirth, String residenceCountry, String phoneNumber, Gender gender) {
		super();
		this.name = name;
		this.dayOfBirth = dayOfBirth;
		this.residenceCountry = residenceCountry;
		this.phoneNumber = phoneNumber;
		this.gender = gender;
	}

}
