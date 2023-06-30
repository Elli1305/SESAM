package com.gpse.sesam.domain.location.door.config;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Predicate {
	EQUAL("="),
	GREATER(">"),
	GREATER_THEN(">="),
	SMALLER("<"),
	SMALLER_THEN("<=");

	private final String value;

	private Predicate(final String description) {
		value = description;
	}


	@JsonValue
	public String getValue() {
		return value;
	}


	public static Predicate fromString(final String s) {
		for (final Predicate predicate : Predicate.values()) {
			if (predicate.getValue().equals(s)) {
				return predicate;
			}
		}
		return null;
	}

}
