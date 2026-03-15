package com.mycontacts.contact.display;

import java.util.Objects;
import java.util.stream.Collectors;

import com.mycontacts.contact.model.Contact;

public class BaseContactView implements ContactView {
	@Override
	public String display(Contact contact) {
		Objects.requireNonNull(contact, "contact cannot be null");

		String phones = contact.getPhoneNumbers().isEmpty() ? ""
				: contact.getPhoneNumbers().stream().map(p -> p.getLabel() + ": " + p.getNumber())
						.collect(Collectors.joining(", "));
		String emails = contact.getEmails().isEmpty() ? ""
				: contact.getEmails().stream().map(e -> e.getLabel() + ": " + e.getAddress())
						.collect(Collectors.joining(", "));

		StringBuilder sb = new StringBuilder();
		sb.append("ID: ").append(contact.getId()).append(System.lineSeparator());
		sb.append("Name: ").append(contact.getName()).append(System.lineSeparator());
		sb.append("Phones: ").append(phones).append(System.lineSeparator());
		sb.append("Emails: ").append(emails).append(System.lineSeparator());
		sb.append("Created: ").append(contact.getCreatedAt().toLocalDate());
		return sb.toString();
	}
}
