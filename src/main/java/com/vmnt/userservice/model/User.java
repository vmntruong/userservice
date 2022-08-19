package com.vmnt.userservice.model;

import static com.vmnt.userservice.controller.UserServiceConstants.*;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;


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
	
	@NotBlank(message=MSG_MANDATORY_NAME)
	private String name;
	
	@NotNull(message=MSG_MANDATORY_DOB)
	@JsonProperty("dayOfBirth")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	@JsonDeserialize(using=MyJsonDateDeserializer.class)
	@PastOrPresent(message=MSG_VALID_DOB)
	private LocalDate dayOfBirth;
	
	@NotBlank(message=MSG_MANDATORY_RESIDENCE_COUNTRY)
	private String residenceCountry;
	
	@Digits(message=MSG_VALID_PHONE_NUMBER, fraction=0, integer=10)
	private String phoneNumber;
	
	@Enumerated(EnumType.STRING)
	private Gender gender;
	
	public User() {	}
	
	public User(Long id, String name, LocalDate dayOfBirth, String residenceCountry, String phoneNumber,
			Gender gender) {
		super();
		this.id = id;
		this.name = name;
		this.dayOfBirth = dayOfBirth;
		this.residenceCountry = residenceCountry;
		this.phoneNumber = phoneNumber;
		this.gender = gender;
	}

	public User(String name, LocalDate dayOfBirth, String residenceCountry, String phoneNumber, Gender gender) {
		super();
		this.name = name;
		this.dayOfBirth = dayOfBirth;
		this.residenceCountry = residenceCountry;
		this.phoneNumber = phoneNumber;
		this.gender = gender;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getDayOfBirth() {
		return dayOfBirth;
	}

	public void setDayOfBirth(LocalDate dayOfBirth) {
		this.dayOfBirth = dayOfBirth;
	}

	public String getResidenceCountry() {
		return residenceCountry;
	}

	public void setResidenceCountry(String residenceCountry) {
		this.residenceCountry = residenceCountry;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", dayOfBirth=" + dayOfBirth + ", residenceCountry="
				+ residenceCountry + ", phoneNumber=" + phoneNumber + ", gender=" + gender + "]";
	}


}
