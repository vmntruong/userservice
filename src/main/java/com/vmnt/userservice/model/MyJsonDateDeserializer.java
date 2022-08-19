package com.vmnt.userservice.model;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.http.converter.HttpMessageNotReadableException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * Handle json parse exception for LocalDate
 * 
 * @author vmntruong
 *
 */
public class MyJsonDateDeserializer extends JsonDeserializer<LocalDate> {
	
    @SuppressWarnings("deprecation")
	@Override
    public LocalDate deserialize(JsonParser jsonParser,
            DeserializationContext deserializationContext) throws IOException, HttpMessageNotReadableException {

    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/y");
        String date = jsonParser.getText();
        try {
            return LocalDate.parse(date, formatter);
        } catch (Exception exception) {
        	throw new HttpMessageNotReadableException("Day of birth must be in this format d/M/y");
        }

    }

}