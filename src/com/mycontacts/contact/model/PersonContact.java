package com.mycontacts.contact.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public final class PersonContact extends Contact {
	private final String firstName;
	private final String lastName;

	public PersonContact(UUID id, String name, List<PhoneNumber> phoneNumbers, List<Email> emails, LocalDateTime createdAt,
			String firstName, String lastName) {
		super(id, name, phoneNumbers, emails, createdAt);
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}
}
