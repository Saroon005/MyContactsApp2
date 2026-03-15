package com.mycontacts.user.service;

import java.util.Objects;
import java.util.Optional;

import com.mycontacts.common.exception.ValidationException;
import com.mycontacts.user.factory.UserFactory;
import com.mycontacts.user.model.User;
import com.mycontacts.user.repository.UserRepository;

public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;
	private final UserFactory userFactory;

	public UserServiceImpl(UserRepository userRepository, UserFactory userFactory) {
		this.userRepository = Objects.requireNonNull(userRepository, "userRepository cannot be null");
		this.userFactory = Objects.requireNonNull(userFactory, "userFactory cannot be null");
	}

	@Override
	public User register(String type, String email, String password) {
		String normalizedType = normalizeRequired(type, "User type").toUpperCase();
		String normalizedEmail = normalizeRequired(email, "Email");
		String rawPassword = normalizeRequired(password, "Password");

		if (rawPassword.length() < 6) {
			throw new ValidationException("Password must be at least 6 characters");
		}

		if (userRepository.findByEmail(normalizedEmail).isPresent()) {
			throw new ValidationException("Email already exists");
		}

		User user = userFactory.createUser(normalizedType, normalizedEmail, rawPassword);
		userRepository.save(user);
		return user;
	}

	@Override
	public Optional<User> findUserByEmail(String email) {
		if (email == null) {
			return Optional.empty();
		}
		return userRepository.findByEmail(email.trim());
	}

	private static String normalizeRequired(String value, String fieldName) {
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
