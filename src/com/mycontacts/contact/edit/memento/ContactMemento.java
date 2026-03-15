package com.mycontacts.contact.edit.memento;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.mycontacts.contact.model.Email;
import com.mycontacts.contact.model.PhoneNumber;

public final class ContactMemento {
	private final String name;
	private final List<PhoneNumber> phoneNumbers;
	private final List<Email> emails;

	public ContactMemento(String name, List<PhoneNumber> phoneNumbers, List<Email> emails) {
		this.name = Objects.requireNonNull(name, "name cannot be null");
		this.phoneNumbers = List.copyOf(Objects.requireNonNull(phoneNumbers, "phoneNumbers cannot be null"));
		this.emails = List.copyOf(Objects.requireNonNull(emails, "emails cannot be null"));
	}

	public String getName() {
		return name;
	}

	public List<PhoneNumber> getPhoneNumbers() {
		return new ArrayList<>(phoneNumbers);
	}

	public List<Email> getEmails() {
		return new ArrayList<>(emails);
	}
}
