package com.mycontacts.user.service;

import java.util.Optional;

import com.mycontacts.user.model.User;

public interface UserService {
	User register(String type, String email, String password);

	Optional<User> findUserByEmail(String email);
}
