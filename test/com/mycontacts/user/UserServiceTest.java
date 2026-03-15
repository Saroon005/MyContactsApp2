package com.mycontacts.user;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.mycontacts.common.exception.ValidationException;
import com.mycontacts.common.util.PasswordHasher;
import com.mycontacts.user.factory.UserFactory;
import com.mycontacts.user.model.FreeUser;
import com.mycontacts.user.model.PremiumUser;
import com.mycontacts.user.repository.InMemoryUserRepository;
import com.mycontacts.user.service.UserService;
import com.mycontacts.user.service.UserServiceImpl;

public class UserServiceTest {
	@Test
	void shouldRegisterFreeUser() {
		UserService service = new UserServiceImpl(new InMemoryUserRepository(), new UserFactory.Default());

		var user = service.register("FREE", "free@example.com", "secret1");
		assertTrue(user instanceof FreeUser);
		assertNotNull(user.getId());
		assertNotNull(user.getCreatedAt());
		assertTrue(PasswordHasher.verifyPassword("secret1", user.getPasswordHash()));
	}

	@Test
	void shouldRegisterPremiumUser() {
		UserService service = new UserServiceImpl(new InMemoryUserRepository(), new UserFactory.Default());

		var user = service.register("PREMIUM", "premium@example.com", "secret1");
		assertTrue(user instanceof PremiumUser);
		assertNotNull(user.getId());
		assertNotNull(user.getCreatedAt());
		assertTrue(PasswordHasher.verifyPassword("secret1", user.getPasswordHash()));
	}

	@Test
	void shouldNotAllowDuplicateEmail() {
		UserService service = new UserServiceImpl(new InMemoryUserRepository(), new UserFactory.Default());

		service.register("FREE", "dup@example.com", "secret1");
		assertThrows(ValidationException.class, () -> service.register("PREMIUM", "dup@example.com", "secret1"));
	}
}
