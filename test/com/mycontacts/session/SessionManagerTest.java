package com.mycontacts.session;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mycontacts.user.factory.UserFactory;

public class SessionManagerTest {
	private SessionManager sessionManager;

	@BeforeEach
	void setUp() {
		sessionManager = SessionManager.getInstance();
		sessionManager.logout();
	}

	@Test
	void shouldStoreLoggedInUser() {
		var user = new UserFactory.Default().createUser("FREE", "session@example.com", "secret1");
		sessionManager.login(user);

		assertTrue(sessionManager.getCurrentUser().isPresent());
		assertSame(user, sessionManager.getCurrentUser().orElseThrow());
	}

	@Test
	void shouldLogoutSuccessfully() {
		var user = new UserFactory.Default().createUser("PREMIUM", "session2@example.com", "secret1");
		sessionManager.login(user);
		sessionManager.logout();

		assertFalse(sessionManager.getCurrentUser().isPresent());
	}
}
