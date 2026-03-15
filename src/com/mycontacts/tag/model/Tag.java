package com.mycontacts.tag.model;

import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import com.mycontacts.common.exception.ValidationException;

public final class Tag {
	private final UUID id;
	private final String name;

	public Tag(UUID id, String name) {
		this.id = Objects.requireNonNull(id, "id cannot be null");
		this.name = normalizeName(name);
	}

	public UUID getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		Tag other = (Tag) obj;
		return name.equals(other.name);
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	private static String normalizeName(String value) {
		if (value == null) {
			throw new ValidationException("Tag name cannot be null");
		}
		String trimmed = value.trim();
		if (trimmed.isEmpty()) {
			throw new ValidationException("Tag name cannot be empty");
		}
		return trimmed.toLowerCase(Locale.ROOT);
	}
}
