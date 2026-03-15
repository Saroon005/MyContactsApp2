package com.mycontacts.user.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

import com.mycontacts.common.exception.ValidationException;

public abstract class User {
	private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

	private final UUID id;
	private String email;
	private String passwordHash;
	private final LocalDateTime createdAt;

	protected User(UUID id, String email, String passwordHash, LocalDateTime createdAt) {
		this.id = Objects.requireNonNull(id, "id cannot be null");
		this.email = normalizeAndValidateEmail(email);
		this.passwordHash = validatePasswordHash(passwordHash);
		this.createdAt = Objects.requireNonNull(createdAt, "createdAt cannot be null");
	}

	public UUID getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void updateEmail(String newEmail) {
		this.email = normalizeAndValidateEmail(newEmail);
	}

	public void updatePasswordHash(String newPasswordHash) {
		this.passwordHash = validatePasswordHash(newPasswordHash);
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	private static String normalizeAndValidateEmail(String email) {
		if (email == null) {
			throw new ValidationException("Email cannot be null");
		}
		String normalized = email.trim();
		if (normalized.isEmpty()) {
			throw new ValidationException("Email cannot be empty");
		}
		if (!EMAIL_PATTERN.matcher(normalized).matches()) {
			throw new ValidationException("Invalid email format");
		}
		return normalized;
	}

	private static String validatePasswordHash(String passwordHash) {
		if (passwordHash == null || passwordHash.trim().isEmpty()) {
			throw new ValidationException("Password hash cannot be empty");
		}
		return passwordHash;
	}
}
