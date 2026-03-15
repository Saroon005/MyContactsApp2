package com.mycontacts.contact.model;

import java.util.Objects;

import com.mycontacts.common.exception.ValidationException;

public final class PhoneNumber {
	private final String number;
	private final String label;

	public PhoneNumber(String number, String label) {
		this.number = normalizeRequired(number, "Phone number");
		this.label = normalizeRequired(label, "Phone label");
	}

	public String getNumber() {
		return number;
	}

	public String getLabel() {
		return label;
	}

	private static String normalizeRequired(String value, String fieldName) {
		Objects.requireNonNull(fieldName, "fieldName cannot be null");
		if (value == null) {
			throw new ValidationException(fieldName + " cannot be null");
		}
		String trimmed = value.trim();
		if (trimmed.isEmpty()) {
			throw new ValidationException(fieldName + " cannot be empty");
		}
		return trimmed;
	}
}
