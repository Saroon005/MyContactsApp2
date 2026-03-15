package com.mycontacts.profile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.mycontacts.common.exception.ValidationException;
import com.mycontacts.common.util.PasswordHasher;
import com.mycontacts.profile.service.ProfileService;
import com.mycontacts.profile.service.ProfileServiceImpl;
import com.mycontacts.user.factory.UserFactory;
import com.mycontacts.user.repository.InMemoryUserRepository;
import com.mycontacts.user.repository.UserRepository;
import com.mycontacts.user.service.UserService;
import com.mycontacts.user.service.UserServiceImpl;

public class ProfileServiceTest {
	@Test
	void shouldUpdateEmailSuccessfully() {
		UserRepository repo = new InMemoryUserRepository();
		UserService userService = new UserServiceImpl(repo, new UserFactory.Default());
		ProfileService profileService = new ProfileServiceImpl(repo);

		var user = userService.register("FREE", "old@example.com", "secret1");
		profileService.updateEmail(user, "new@example.com");

		assertEquals("new@example.com", user.getEmail());
		assertTrue(repo.findByEmail("new@example.com").isPresent());
		assertSame(user, repo.findByEmail("new@example.com").orElseThrow());
		assertFalse(repo.findByEmail("old@example.com").isPresent());
	}

	@Test
	void shouldRejectInvalidEmail() {
		UserRepository repo = new InMemoryUserRepository();
		UserService userService = new UserServiceImpl(repo, new UserFactory.Default());
		ProfileService profileService = new ProfileServiceImpl(repo);

		var user = userService.register("FREE", "valid@example.com", "secret1");
		assertThrows(ValidationException.class, () -> profileService.updateEmail(user, "not-an-email"));
	}

	@Test
	void shouldChangePasswordSuccessfully() {
		UserRepository repo = new InMemoryUserRepository();
		UserService userService = new UserServiceImpl(repo, new UserFactory.Default());
		ProfileService profileService = new ProfileServiceImpl(repo);

		var user = userService.register("PREMIUM", "p@example.com", "secret1");
		profileService.changePassword(user, "secret1", "secret2");

		assertTrue(PasswordHasher.verifyPassword("secret2", user.getPasswordHash()));
		assertFalse(PasswordHasher.verifyPassword("secret1", user.getPasswordHash()));
	}

	@Test
	void shouldFailIfOldPasswordIncorrect() {
		UserRepository repo = new InMemoryUserRepository();
		UserService userService = new UserServiceImpl(repo, new UserFactory.Default());
		ProfileService profileService = new ProfileServiceImpl(repo);

		var user = userService.register("FREE", "x@example.com", "secret1");
		assertThrows(ValidationException.class, () -> profileService.changePassword(user, "wrong", "secret2"));
	}
}
