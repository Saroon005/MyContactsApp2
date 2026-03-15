package com.mycontacts.profile.command;

import java.util.Objects;

import com.mycontacts.common.exception.ValidationException;
import com.mycontacts.common.util.PasswordHasher;
import com.mycontacts.user.model.User;

public class ChangePasswordCommand implements ProfileCommand {
	private final User user;
	private final String oldPassword;
	private final String newPassword;

	public ChangePasswordCommand(User user, String oldPassword, String newPassword) {
		this.user = Objects.requireNonNull(user, "user cannot be null");
		this.oldPassword = oldPassword;
		this.newPassword = newPassword;
	}

	@Override
	public void execute() {
		if (oldPassword == null || oldPassword.trim().isEmpty()) {
			throw new ValidationException("Old password cannot be empty");
		}
		if (newPassword == null) {
			throw new ValidationException("New password cannot be null");
		}
		String normalizedNewPassword = newPassword.trim();
		if (normalizedNewPassword.length() < 6) {
			throw new ValidationException("New password must be at least 6 characters");
		}

		if (!PasswordHasher.verifyPassword(oldPassword, user.getPasswordHash())) {
			throw new ValidationException("Old password is incorrect");
		}

		String newHash = PasswordHasher.hashPassword(normalizedNewPassword);
		user.updatePasswordHash(newHash);
	}
}
