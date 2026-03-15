package com.mycontacts.contact.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.mycontacts.contact.model.Contact;

public interface ContactRepository {
	void save(Contact contact);

	List<Contact> findAll();

	Optional<Contact> findById(UUID id);

	List<Contact> findAllActive();

	void delete(UUID id);

	void hardDelete(UUID id);
}
