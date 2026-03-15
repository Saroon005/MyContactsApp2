package com.mycontacts.contact.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import com.mycontacts.common.exception.ValidationException;
import com.mycontacts.common.util.IdGenerator;
import com.mycontacts.contact.builder.ContactBuilder;
import com.mycontacts.contact.edit.command.UpdateContactNameCommand;
import com.mycontacts.contact.edit.command.UpdateEmailCommand;
import com.mycontacts.contact.edit.command.UpdatePhoneCommand;
import com.mycontacts.contact.edit.manager.UndoRedoManager;
import com.mycontacts.contact.model.Contact;
import com.mycontacts.contact.model.Email;
import com.mycontacts.contact.model.PhoneNumber;
import com.mycontacts.contact.repository.ContactRepository;

public class ContactServiceImpl implements ContactService {
	private final ContactRepository contactRepository;
	private final UndoRedoManager undoRedoManager;

	public ContactServiceImpl(ContactRepository contactRepository) {
		this(contactRepository, new UndoRedoManager());
	}

	public ContactServiceImpl(ContactRepository contactRepository, UndoRedoManager undoRedoManager) {
		this.contactRepository = Objects.requireNonNull(contactRepository, "contactRepository cannot be null");
		this.undoRedoManager = Objects.requireNonNull(undoRedoManager, "undoRedoManager cannot be null");
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
		return contactRepository.findById(id).filter(c -> !c.isDeleted());
	}

	@Override
	public void updateContactName(UUID contactId, String newName) {
		Contact contact = requireActiveContact(contactId);
		undoRedoManager.executeCommand(new UpdateContactNameCommand(contact, newName));
		contactRepository.save(contact);
	}

	@Override
	public void updatePhone(UUID contactId, PhoneNumber phone) {
		Contact contact = requireActiveContact(contactId);
		undoRedoManager.executeCommand(new UpdatePhoneCommand(contact, phone));
		contactRepository.save(contact);
	}

	@Override
	public void updateEmail(UUID contactId, Email email) {
		Contact contact = requireActiveContact(contactId);
		undoRedoManager.executeCommand(new UpdateEmailCommand(contact, email));
		contactRepository.save(contact);
	}

	@Override
	public void undoLastEdit() {
		undoRedoManager.undo();
	}

	@Override
	public void redoLastEdit() {
		undoRedoManager.redo();
	}

	@Override
	public void deleteContact(UUID id) {
		Contact contact = contactRepository.findById(id).orElseThrow(() -> new ValidationException("Contact not found"));
		if (contact.isDeleted()) {
			throw new ValidationException("Contact not found");
		}
		contactRepository.delete(id);
		contactRepository.save(contact);
	}

	@Override
	public void hardDeleteContact(UUID id) {
		Contact contact = contactRepository.findById(id).orElseThrow(() -> new ValidationException("Contact not found"));
		contactRepository.hardDelete(contact.getId());
	}

	@Override
	public List<Contact> getAllContacts() {
		return contactRepository.findAllActive();
	}

	private Contact requireActiveContact(UUID contactId) {
		Contact contact = contactRepository.findById(contactId)
				.orElseThrow(() -> new ValidationException("Contact not found"));
		if (contact.isDeleted()) {
			throw new ValidationException("Contact not found");
		}
		return contact;
	}
}

