package com.mycontacts.group.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.mycontacts.common.exception.ValidationException;
import com.mycontacts.contact.model.Contact;

public class ContactGroup {
	private final UUID id;
	private final String name;
	private final List<Contact> contacts;

	public ContactGroup(UUID id, String name) {
		this.id = Objects.requireNonNull(id, "id cannot be null");
		this.name = normalizeRequired(name, "Group name");
		this.contacts = new ArrayList<>();
	}

	public UUID getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void addContact(Contact contact) {
		Objects.requireNonNull(contact, "contact cannot be null");
		for (Contact existing : contacts) {
			if (existing.getId().equals(contact.getId())) {
				return;
			}
		}
		contacts.add(contact);
	}

	public void removeContact(Contact contact) {
		if (contact == null) {
			return;
		}
		contacts.removeIf(c -> c.getId().equals(contact.getId()));
	}

	public List<Contact> getContacts() {
		return new ArrayList<>(contacts);
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
