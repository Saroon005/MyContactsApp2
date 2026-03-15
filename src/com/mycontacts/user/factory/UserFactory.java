package com.mycontacts.user.factory;

import java.time.LocalDateTime;
import java.util.Objects;

import com.mycontacts.common.exception.ValidationException;
import com.mycontacts.common.util.IdGenerator;
import com.mycontacts.common.util.PasswordHasher;
import com.mycontacts.user.model.FreeUser;
import com.mycontacts.user.model.PremiumUser;
import com.mycontacts.user.model.User;

public interface UserFactory {
	User createUser(String type, String email, String password);

	class Default implements UserFactory {
		@Override
		public User createUser(String type, String email, String password) {
			String normalizedType = Objects.requireNonNull(type, "type cannot be null").trim().toUpperCase();
			String normalizedEmail = Objects.requireNonNull(email, "email cannot be null").trim();
			String rawPassword = Objects.requireNonNull(password, "password cannot be null");

			if (rawPassword.trim().length() < 6) {
				throw new ValidationException("Password must be at least 6 characters");
			}

			var id = IdGenerator.generateId();
			var createdAt = LocalDateTime.now();
			var passwordHash = PasswordHasher.hashPassword(rawPassword);

			switch (normalizedType) {
			case "FREE":
				return new FreeUser(id, normalizedEmail, passwordHash, createdAt);
			case "PREMIUM":
				return new PremiumUser(id, normalizedEmail, passwordHash, createdAt);
			default:
				throw new ValidationException("Unsupported user type: " + normalizedType);
			}
		}
	}
}
