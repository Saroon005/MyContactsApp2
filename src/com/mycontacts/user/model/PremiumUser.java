package com.mycontacts.user.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class PremiumUser extends User {
	public PremiumUser(UUID id, String email, String passwordHash, LocalDateTime createdAt) {
		super(id, email, passwordHash, createdAt);
	}
}
