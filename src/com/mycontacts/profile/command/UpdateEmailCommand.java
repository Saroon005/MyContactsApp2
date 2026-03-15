package com.mycontacts.profile.command;

import java.util.Objects;
import java.util.regex.Pattern;

import com.mycontacts.common.exception.ValidationException;
import com.mycontacts.user.model.User;

public class UpdateEmailCommand implements ProfileCommand {
	private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

	private final User user;
	private final String newEmail;

	public UpdateEmailCommand(User user, String newEmail) {
		this.user = Objects.requireNonNull(user, "user cannot be null");
		this.newEmail = newEmail;
	}

	@Override
	public void execute() {
		String normalized = normalizeAndValidateEmail(newEmail);
		user.updateEmail(normalized);
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
}
