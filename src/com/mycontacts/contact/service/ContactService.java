package com.mycontacts.contact.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.mycontacts.contact.model.Contact;
import com.mycontacts.contact.model.Email;
import com.mycontacts.contact.model.PhoneNumber;

public interface ContactService {
	Contact createPersonContact(String name, PhoneNumber phone, Email email);

	Contact createOrganizationContact(String name, PhoneNumber phone, Email email);

	default Contact createPersonContact(String name) {
		return createPersonContact(name, null, null);
	}

	default Contact createOrganizationContact(String name) {
		return createOrganizationContact(name, null, null);
	}

	Optional<Contact> getContactById(UUID id);

	List<Contact> getAllContacts();
}
