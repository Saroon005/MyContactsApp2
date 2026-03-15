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

	void updateContactName(UUID contactId, String newName);

	void updatePhone(UUID contactId, PhoneNumber phone);

	void updateEmail(UUID contactId, Email email);

	void undoLastEdit();

	void redoLastEdit();

	void deleteContact(UUID id);

	void hardDeleteContact(UUID id);

	List<Contact> getAllContacts();
}
