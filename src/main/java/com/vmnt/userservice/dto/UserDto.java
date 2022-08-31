package com.vmnt.userservice.dto;

import static com.vmnt.userservice.controller.UserServiceConstants.MSG_MANDATORY_DOB;
import static com.vmnt.userservice.controller.UserServiceConstants.MSG_MANDATORY_NAME;
import static com.vmnt.userservice.controller.UserServiceConstants.MSG_MANDATORY_RESIDENCE_COUNTRY;
import static com.vmnt.userservice.controller.UserServiceConstants.MSG_VALID_DOB;
import static com.vmnt.userservice.controller.UserServiceConstants.MSG_VALID_PHONE_NUMBER;

import java.time.LocalDate;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.vmnt.userservice.model.Gender;
import com.vmnt.userservice.model.MyJsonDateDeserializer;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
	
	@ApiModelProperty(notes = "User id", example = "1", required = false)
	private Long id;
	
	@NotBlank(message=MSG_MANDATORY_NAME)
	@ApiModelProperty(notes = "User name", example = "Alex", required = true)
	private String name;
	
	@NotNull(message=MSG_MANDATORY_DOB)
	@JsonProperty("dayOfBirth")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	@ApiModelProperty(notes = "Only adults can be registered", example = "22/12/1992", required = true)
	@JsonDeserialize(using=MyJsonDateDeserializer.class)
	@PastOrPresent(message=MSG_VALID_DOB)
	private LocalDate dayOfBirth;
	
	@NotBlank(message=MSG_MANDATORY_RESIDENCE_COUNTRY)
	@ApiModelProperty(notes = "Only French users can be registered", example = "France", required = true)
	private String residenceCountry;
	
	@Digits(message=MSG_VALID_PHONE_NUMBER, fraction=0, integer=10)
	@ApiModelProperty(notes = "Only digits are allowed, max = 10 digits", example = "061234567", required = false)
	private String phoneNumber;
	
	@Enumerated(EnumType.STRING)
	private Gender gender;
	
	/**
	 * Constructor without id
	 */
	public UserDto(String name, LocalDate dayOfBirth, String residenceCountry, String phoneNumber, Gender gender) {
		super();
		this.name = name;
		this.dayOfBirth = dayOfBirth;
		this.residenceCountry = residenceCountry;
		this.phoneNumber = phoneNumber;
		this.gender = gender;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", dayOfBirth=" + dayOfBirth + ", residenceCountry="
				+ residenceCountry + ", phoneNumber=" + phoneNumber + ", gender=" + gender + "]";
	}
}
