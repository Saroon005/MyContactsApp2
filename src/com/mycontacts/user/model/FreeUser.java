package com.mycontacts.user.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class FreeUser extends User {
	public FreeUser(UUID id, String email, String passwordHash, LocalDateTime createdAt) {
		super(id, email, passwordHash, createdAt);
	}
}
