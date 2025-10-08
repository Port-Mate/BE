package com.portmate.domain.schedule.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class DateParser {
	private static final List<String> PATTERNS = List.of(
		"yyyy.M.d HH:mm",
		"yyyy.MM.dd HH:mm",
		"yyyy-MM-dd HH:mm",
		"yyyy/MM/dd HH:mm",
		"yyyy.MM.dd HH:mm:ss",
		"yyyy-MM-dd'T'HH:mm",
		"yyyy-MM-dd HH:mm:ss"
	);

	public static LocalDateTime parse(String value) {
		if (value == null || value.isBlank()) return null;

		String trimmed = value.trim();

		for (String pattern : PATTERNS) {
			try {
				return LocalDateTime.parse(trimmed, DateTimeFormatter.ofPattern(pattern));
			} catch (DateTimeParseException ignored) {}
		}

		return null;
	}
}
