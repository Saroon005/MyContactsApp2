package com.mycontacts.filter.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import com.mycontacts.contact.model.Contact;
import com.mycontacts.contact.repository.ContactRepository;
import com.mycontacts.group.model.ContactGroup;
import com.mycontacts.group.repository.GroupRepository;

public class FilterServiceImpl implements FilterService {
	private final ContactRepository contactRepository;
	private final GroupRepository groupRepository;

	public FilterServiceImpl(ContactRepository contactRepository, GroupRepository groupRepository) {
		this.contactRepository = Objects.requireNonNull(contactRepository, "contactRepository cannot be null");
		this.groupRepository = Objects.requireNonNull(groupRepository, "groupRepository cannot be null");
	}

	@Override
	public List<Contact> filterByGroup(UUID groupId) {
		if (groupId == null) {
			return List.of();
		}

		ContactGroup group = groupRepository.findById(groupId).orElse(null);
		if (group == null) {
			return List.of();
		}

		List<Contact> results = new ArrayList<>();
		for (Contact groupContact : group.getContacts()) {
			if (groupContact == null || groupContact.getId() == null) {
				continue;
			}
			var storedOpt = contactRepository.findById(groupContact.getId());
			if (storedOpt.isEmpty()) {
				continue;
			}
			Contact stored = storedOpt.orElseThrow();
			if (!stored.isDeleted()) {
				results.add(stored);
			}
		}
		return results;
	}

	@Override
	public List<Contact> filterByDate(LocalDate date) {
		if (date == null) {
			return List.of();
		}
		List<Contact> results = new ArrayList<>();
		for (Contact contact : contactRepository.findAllActive()) {
			if (contact.getCreatedAt().toLocalDate().equals(date)) {
				results.add(contact);
			}
		}
		return results;
	}

	@Override
	public List<Contact> sortByName() {
		List<Contact> contacts = new ArrayList<>(contactRepository.findAllActive());
		contacts.sort(Comparator.comparing(c -> safeLower(c.getName())));
		return contacts;
	}

	@Override
	public List<Contact> sortByCreationDate() {
		List<Contact> contacts = new ArrayList<>(contactRepository.findAllActive());
		contacts.sort(Comparator.comparing(Contact::getCreatedAt));
		return contacts;
	}

	private static String safeLower(String value) {
		if (value == null) {
			return "";
		}
		return value.toLowerCase(Locale.ROOT);
	}
}
