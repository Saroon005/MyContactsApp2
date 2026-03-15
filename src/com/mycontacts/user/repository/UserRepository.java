package com.mycontacts.user.repository;

import java.util.Optional;

import com.mycontacts.user.model.User;

public interface UserRepository {
	void save(User user);

	Optional<User> findByEmail(String email);
}
