package com.mycontacts.profile.service;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

import com.mycontacts.common.exception.ValidationException;
import com.mycontacts.profile.command.ChangePasswordCommand;
import com.mycontacts.profile.command.UpdateEmailCommand;
import com.mycontacts.user.model.User;
import com.mycontacts.user.repository.UserRepository;

public class ProfileServiceImpl implements ProfileService {
	private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

	private final UserRepository userRepository;

	public ProfileServiceImpl(UserRepository userRepository) {
		this.userRepository = Objects.requireNonNull(userRepository, "userRepository cannot be null");
	}

	@Override
	public void updateEmail(User user, String newEmail) {
		Objects.requireNonNull(user, "user cannot be null");
		String normalized = normalizeAndValidateEmail(newEmail);

		Optional<User> existing = userRepository.findByEmail(normalized);
		if (existing.isPresent() && existing.get() != user) {
			throw new ValidationException("Email already exists");
		}

		new UpdateEmailCommand(user, normalized).execute();
		userRepository.save(user);
	}

	@Override
	public void changePassword(User user, String oldPassword, String newPassword) {
		Objects.requireNonNull(user, "user cannot be null");
		new ChangePasswordCommand(user, oldPassword, newPassword).execute();
		userRepository.save(user);
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
