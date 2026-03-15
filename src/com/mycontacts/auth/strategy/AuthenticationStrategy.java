package com.mycontacts.auth.strategy;

import java.util.Optional;

import com.mycontacts.user.model.User;

public interface AuthenticationStrategy {
	Optional<User> authenticate(String email, String password);
}
