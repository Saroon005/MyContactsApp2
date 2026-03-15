package com.mycontacts.contact.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public final class OrganizationContact extends Contact {
	private final String organizationName;

	public OrganizationContact(UUID id, String name, List<PhoneNumber> phoneNumbers, List<Email> emails, LocalDateTime createdAt,
			String organizationName) {
		super(id, name, phoneNumbers, emails, createdAt);
		this.organizationName = organizationName;
	}

	public String getOrganizationName() {
		return organizationName;
	}
}
