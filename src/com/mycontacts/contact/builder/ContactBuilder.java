package com.mycontacts.contact.builder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.mycontacts.common.exception.ValidationException;
import com.mycontacts.contact.model.Contact;
import com.mycontacts.contact.model.Email;
import com.mycontacts.contact.model.OrganizationContact;
import com.mycontacts.contact.model.PersonContact;
import com.mycontacts.contact.model.PhoneNumber;

public class ContactBuilder {
	private final UUID id;
	private final LocalDateTime createdAt;

	private String name;
	private final List<PhoneNumber> phoneNumbers = new ArrayList<>();
	private final List<Email> emails = new ArrayList<>();

	public ContactBuilder(UUID id, LocalDateTime createdAt) {
		this.id = Objects.requireNonNull(id, "id cannot be null");
		this.createdAt = Objects.requireNonNull(createdAt, "createdAt cannot be null");
	}

	public ContactBuilder setName(String name) {
		this.name = name;
		return this;
	}

	public ContactBuilder addPhoneNumber(PhoneNumber phone) {
		if (phone != null) {
			phoneNumbers.add(phone);
		}
		return this;
	}

	public ContactBuilder addEmail(Email email) {
		if (email != null) {
			emails.add(email);
		}
		return this;
	}

	public Contact buildPersonContact() {
		String normalizedName = normalizeRequired(name, "Contact name");
		String[] parts = normalizedName.split("\\s+", 2);
		String firstName = parts.length > 0 ? parts[0] : normalizedName;
		String lastName = parts.length == 2 ? parts[1] : "";

		return new PersonContact(id, normalizedName, List.copyOf(phoneNumbers), List.copyOf(emails), createdAt, firstName,
				lastName);
	}

	public Contact buildOrganizationContact() {
		String normalizedName = normalizeRequired(name, "Contact name");
		return new OrganizationContact(id, normalizedName, List.copyOf(phoneNumbers), List.copyOf(emails), createdAt,
				normalizedName);
	}

	private static String normalizeRequired(String value, String fieldName) {
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
