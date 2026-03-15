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
		// If the user's email changes, remove any stale keys that still point to this user.
		usersByEmail.entrySet().removeIf(e -> e.getValue() == user && !e.getKey().equals(user.getEmail()));
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
