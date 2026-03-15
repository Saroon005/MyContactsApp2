package com.mycontacts.contact.model;

import java.util.Objects;
import java.util.regex.Pattern;

import com.mycontacts.common.exception.ValidationException;

public final class Email {
	private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

	private final String address;
	private final String label;

	public Email(String address, String label) {
		this.address = normalizeAndValidateAddress(address);
		this.label = normalizeRequired(label, "Email label");
	}

	public String getAddress() {
		return address;
	}

	public String getLabel() {
		return label;
	}

	private static String normalizeAndValidateAddress(String address) {
		if (address == null) {
			throw new ValidationException("Email address cannot be null");
		}
		String normalized = address.trim();
		if (normalized.isEmpty()) {
			throw new ValidationException("Email address cannot be empty");
		}
		if (!EMAIL_PATTERN.matcher(normalized).matches()) {
			throw new ValidationException("Invalid email format");
		}
		return normalized;
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
