package com.mycontacts.auth;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.mycontacts.auth.strategy.AuthenticationStrategy;
import com.mycontacts.auth.strategy.BasicAuthenticationStrategy;
import com.mycontacts.common.exception.AuthenticationException;
import com.mycontacts.user.factory.UserFactory;
import com.mycontacts.user.repository.InMemoryUserRepository;
import com.mycontacts.user.repository.UserRepository;
import com.mycontacts.user.service.UserService;
import com.mycontacts.user.service.UserServiceImpl;

public class AuthenticationStrategyTest {
	@Test
	void shouldLoginWithValidCredentials() {
		UserRepository repo = new InMemoryUserRepository();
		UserService userService = new UserServiceImpl(repo, new UserFactory.Default());
		AuthenticationStrategy strategy = new BasicAuthenticationStrategy(repo);

		userService.register("FREE", "login@example.com", "secret1");
		var user = strategy.authenticate("login@example.com", "secret1").orElseThrow();
		assertNotNull(user.getId());
		assertTrue(user.getEmail().equals("login@example.com"));
	}

	@Test
	void shouldFailWithWrongPassword() {
		UserRepository repo = new InMemoryUserRepository();
		UserService userService = new UserServiceImpl(repo, new UserFactory.Default());
		AuthenticationStrategy strategy = new BasicAuthenticationStrategy(repo);

		userService.register("PREMIUM", "wrongpass@example.com", "secret1");
		assertThrows(AuthenticationException.class,
				() -> strategy.authenticate("wrongpass@example.com", "nottherightpassword"));
	}

	@Test
	void shouldFailWithUnknownEmail() {
		UserRepository repo = new InMemoryUserRepository();
		AuthenticationStrategy strategy = new BasicAuthenticationStrategy(repo);

		assertThrows(AuthenticationException.class, () -> strategy.authenticate("unknown@example.com", "secret1"));
	}
}
