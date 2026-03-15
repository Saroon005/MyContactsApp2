package com.mycontacts.auth.strategy;

import java.util.Objects;
import java.util.Optional;

import com.mycontacts.common.exception.AuthenticationException;
import com.mycontacts.common.util.PasswordHasher;
import com.mycontacts.user.model.User;
import com.mycontacts.user.repository.UserRepository;

public class BasicAuthenticationStrategy implements AuthenticationStrategy {
	private final UserRepository userRepository;

	public BasicAuthenticationStrategy(UserRepository userRepository) {
		this.userRepository = Objects.requireNonNull(userRepository, "userRepository cannot be null");
	}

	@Override
	public Optional<User> authenticate(String email, String password) {
		String normalizedEmail = normalize(email);
		String rawPassword = normalize(password);

		User user = userRepository.findByEmail(normalizedEmail)
				.orElseThrow(() -> new AuthenticationException("Invalid credentials"));

		if (!PasswordHasher.verifyPassword(rawPassword, user.getPasswordHash())) {
			throw new AuthenticationException("Invalid credentials");
		}

		return Optional.of(user);
	}

	private static String normalize(String value) {
		if (value == null) {
			throw new AuthenticationException("Invalid credentials");
		}
		String trimmed = value.trim();
		if (trimmed.isEmpty()) {
			throw new AuthenticationException("Invalid credentials");
		}
		return trimmed;
	}
}
