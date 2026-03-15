package com.mycontacts.contact.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import com.mycontacts.common.util.IdGenerator;
import com.mycontacts.contact.builder.ContactBuilder;
import com.mycontacts.contact.model.Contact;
import com.mycontacts.contact.model.Email;
import com.mycontacts.contact.model.PhoneNumber;
import com.mycontacts.contact.repository.ContactRepository;

public class ContactServiceImpl implements ContactService {
	private final ContactRepository contactRepository;

	public ContactServiceImpl(ContactRepository contactRepository) {
		this.contactRepository = Objects.requireNonNull(contactRepository, "contactRepository cannot be null");
	}

	@Override
	public Contact createPersonContact(String name, PhoneNumber phone, Email email) {
		var id = IdGenerator.generateId();
		var createdAt = LocalDateTime.now();
		ContactBuilder builder = new ContactBuilder(id, createdAt).setName(name).addPhoneNumber(phone).addEmail(email);
		Contact contact = builder.buildPersonContact();
		contactRepository.save(contact);
		return contact;
	}

	@Override
	public Contact createOrganizationContact(String name, PhoneNumber phone, Email email) {
		var id = IdGenerator.generateId();
		var createdAt = LocalDateTime.now();
		ContactBuilder builder = new ContactBuilder(id, createdAt).setName(name).addPhoneNumber(phone).addEmail(email);
		Contact contact = builder.buildOrganizationContact();
		contactRepository.save(contact);
		return contact;
	}

	@Override
	public Optional<Contact> getContactById(UUID id) {
		return contactRepository.findById(id);
	}

	@Override
	public List<Contact> getAllContacts() {
		return contactRepository.findAll();
	}
}

