package com.booklion.util;

import java.time.LocalDateTime;
import java.util.Locale;

import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

@Component 
public class DateTimeFormatter implements Formatter<LocalDateTime> {

	@Override
	public LocalDateTime parse(String text, Locale locale) {
		return LocalDateTime.parse(text, java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
	}

	@Override
	public String print(LocalDateTime dateTime, Locale locale) {
		return dateTime.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
	}
}
