package com.mycontacts.user.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import com.mycontacts.user.model.User;

public class InMemoryUserRepository implements UserRepository {
	private final Map<String, User> usersByEmail = new HashMap<>();

	@Override
	public void save(User user) {
		Objects.requireNonNull(user, "user cannot be null");
		usersByEmail.put(user.getEmail(), user);
	}

	@Override
	public Optional<User> findByEmail(String email) {
		if (email == null) {
			return Optional.empty();
		}
		return Optional.ofNullable(usersByEmail.get(email.trim()));
	}
}
