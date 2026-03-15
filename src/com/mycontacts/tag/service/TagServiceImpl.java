package com.mycontacts.tag.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import com.mycontacts.common.exception.ValidationException;
import com.mycontacts.common.util.IdGenerator;
import com.mycontacts.contact.model.Contact;
import com.mycontacts.contact.repository.ContactRepository;
import com.mycontacts.tag.model.Tag;
import com.mycontacts.tag.repository.TagRepository;

public class TagServiceImpl implements TagService {
	private final TagRepository tagRepository;
	private final ContactRepository contactRepository;

	public TagServiceImpl(TagRepository tagRepository, ContactRepository contactRepository) {
		this.tagRepository = Objects.requireNonNull(tagRepository, "tagRepository cannot be null");
		this.contactRepository = Objects.requireNonNull(contactRepository, "contactRepository cannot be null");
	}

	@Override
	public Tag createTag(String name) {
		String normalized = normalizeKey(name);
		if (normalized.isEmpty()) {
			throw new ValidationException("Tag name cannot be empty");
		}
		var existing = tagRepository.findByName(normalized);
		if (existing.isPresent()) {
			return existing.orElseThrow();
		}
		Tag tag = new Tag(IdGenerator.generateId(), normalized);
		tagRepository.save(tag);
		return tag;
	}

	@Override
	public void addTagToContact(UUID contactId, String tagName) {
		Contact contact = requireActiveContact(contactId);
		Tag tag = tagRepository.findByName(tagName).orElseGet(() -> createTag(tagName));
		contact.addTag(tag);
		contactRepository.save(contact);
	}

	@Override
	public void removeTagFromContact(UUID contactId, String tagName) {
		Contact contact = requireActiveContact(contactId);
		var tagOpt = tagRepository.findByName(tagName);
		if (tagOpt.isEmpty()) {
			return;
		}
		contact.removeTag(tagOpt.orElseThrow());
		contactRepository.save(contact);
	}

	@Override
	public List<Contact> getContactsByTag(String tagName) {
		var tagOpt = tagRepository.findByName(tagName);
		if (tagOpt.isEmpty()) {
			return List.of();
		}
		Tag tag = tagOpt.orElseThrow();
		List<Contact> results = new ArrayList<>();
		for (Contact contact : contactRepository.findAllActive()) {
			if (contact.getTags().contains(tag)) {
				results.add(contact);
			}
		}
		return results;
	}

	private Contact requireActiveContact(UUID contactId) {
		Contact contact = contactRepository.findById(contactId)
				.orElseThrow(() -> new ValidationException("Contact not found"));
		if (contact.isDeleted()) {
			throw new ValidationException("Contact not found");
		}
		return contact;
	}

	private static String normalizeKey(String value) {
		if (value == null) {
			return "";
		}
		return value.trim().toLowerCase(Locale.ROOT);
	}
}
