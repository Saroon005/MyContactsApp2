package com.mycontacts.contact.repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import com.mycontacts.contact.model.Contact;

public class InMemoryContactRepository implements ContactRepository {
	private final Map<UUID, Contact> contactsById = new LinkedHashMap<>();

	@Override
	public void save(Contact contact) {
		Objects.requireNonNull(contact, "contact cannot be null");
		contactsById.put(contact.getId(), contact);
	}

	@Override
	public List<Contact> findAll() {
		return new ArrayList<>(contactsById.values());
	}

	@Override
	public Optional<Contact> findById(UUID id) {
		if (id == null) {
			return Optional.empty();
		}
		return Optional.ofNullable(contactsById.get(id));
	}

	@Override
	public List<Contact> findAllActive() {
		List<Contact> active = new ArrayList<>();
		for (Contact contact : contactsById.values()) {
			if (!contact.isDeleted()) {
				active.add(contact);
			}
		}
		return active;
	}

	@Override
	public void delete(UUID id) {
		if (id == null) {
			return;
		}
		Contact contact = contactsById.get(id);
		if (contact != null) {
			contact.markDeleted();
		}
	}

	@Override
	public void hardDelete(UUID id) {
		if (id == null) {
			return;
		}
		contactsById.remove(id);
	}
}
