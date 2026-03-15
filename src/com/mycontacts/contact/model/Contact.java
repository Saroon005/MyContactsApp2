package com.mycontacts.contact.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import com.mycontacts.common.exception.ValidationException;
import com.mycontacts.tag.model.Tag;

public abstract class Contact {
	private final UUID id;
	private String name;
	private List<PhoneNumber> phoneNumbers;
	private List<Email> emails;
	private final Set<Tag> tags = new HashSet<>();
	private final LocalDateTime createdAt;
	private boolean deleted = false;

	protected Contact(UUID id, String name, List<PhoneNumber> phoneNumbers, List<Email> emails, LocalDateTime createdAt) {
		this.id = Objects.requireNonNull(id, "id cannot be null");
		this.name = normalizeRequired(name, "Contact name");
		this.phoneNumbers = List.copyOf(Objects.requireNonNull(phoneNumbers, "phoneNumbers cannot be null"));
		this.emails = List.copyOf(Objects.requireNonNull(emails, "emails cannot be null"));
		this.createdAt = Objects.requireNonNull(createdAt, "createdAt cannot be null");
	}

	public UUID getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void updateName(String newName) {
		this.name = normalizeRequired(newName, "Contact name");
	}

	public List<PhoneNumber> getPhoneNumbers() {
		return new ArrayList<>(phoneNumbers);
	}

	public void replacePhoneNumbers(List<PhoneNumber> newPhoneNumbers) {
		this.phoneNumbers = List.copyOf(Objects.requireNonNull(newPhoneNumbers, "phoneNumbers cannot be null"));
	}

	public List<Email> getEmails() {
		return new ArrayList<>(emails);
	}

	public void replaceEmails(List<Email> newEmails) {
		this.emails = List.copyOf(Objects.requireNonNull(newEmails, "emails cannot be null"));
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void addTag(Tag tag) {
		Objects.requireNonNull(tag, "tag cannot be null");
		tags.add(tag);
	}

	public void removeTag(Tag tag) {
		if (tag == null) {
			return;
		}
		tags.remove(tag);
	}

	public Set<Tag> getTags() {
		return new HashSet<>(tags);
	}

	public void markDeleted() {
		this.deleted = true;
	}

	public boolean isDeleted() {
		return deleted;
	}

	protected static String normalizeRequired(String value, String fieldName) {
		Objects.requireNonNull(fieldName, "fieldName cannot be null");
		if (value == null) {
			throw new ValidationException(fieldName + " cannot be null");
		}
		String trimmed = value.trim();
		if (trimmed.isEmpty()) {
			throw new ValidationException(fieldName + " cannot be empty");
		}
		return trimmed;
	}
}
