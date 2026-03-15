package com.mycontacts.session;

import java.util.Objects;
import java.util.Optional;

import com.mycontacts.user.model.User;

public final class SessionManager {
	private static final SessionManager INSTANCE = new SessionManager();

	private User currentUser;

	private SessionManager() {
		// singleton
	}

	public static SessionManager getInstance() {
		return INSTANCE;
	}

	public void login(User user) {
		this.currentUser = Objects.requireNonNull(user, "user cannot be null");
	}

	public void logout() {
		this.currentUser = null;
	}

	public Optional<User> getCurrentUser() {
		return Optional.ofNullable(currentUser);
	}
}
