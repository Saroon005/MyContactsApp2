package com.mycontacts.common.util;

import java.util.UUID;

public final class IdGenerator {
	private IdGenerator() {
		// utility
	}

	public static UUID generateId() {
		return UUID.randomUUID();
	}
}
