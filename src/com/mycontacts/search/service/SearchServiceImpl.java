package com.mycontacts.search.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import com.mycontacts.contact.model.Contact;
import com.mycontacts.contact.model.Email;
import com.mycontacts.contact.model.PhoneNumber;
import com.mycontacts.contact.repository.ContactRepository;

public class SearchServiceImpl implements SearchService {
	private final ContactRepository contactRepository;

	public SearchServiceImpl(ContactRepository contactRepository) {
		this.contactRepository = Objects.requireNonNull(contactRepository, "contactRepository cannot be null");
	}

	@Override
	public List<Contact> searchByName(String name) {
		String query = normalizeQuery(name);
		if (query.isEmpty()) {
			return List.of();
		}

		List<Contact> matches = new ArrayList<>();
		for (Contact contact : contactRepository.findAllActive()) {
			if (containsIgnoreCase(contact.getName(), query)) {
				matches.add(contact);
			}
		}
		return matches;
	}

	@Override
	public List<Contact> searchByPhone(String phone) {
		String query = normalizeQuery(phone);
		if (query.isEmpty()) {
			return List.of();
		}

		List<Contact> matches = new ArrayList<>();
		for (Contact contact : contactRepository.findAllActive()) {
			for (PhoneNumber phoneNumber : contact.getPhoneNumbers()) {
				if (phoneNumber == null) {
					continue;
				}
				String value = phoneNumber.getNumber();
				if (containsIgnoreCase(value, query)) {
					matches.add(contact);
					break;
				}
			}
		}
		return matches;
	}

	@Override
	public List<Contact> searchByEmail(String email) {
		String query = normalizeQuery(email);
		if (query.isEmpty()) {
			return List.of();
		}

		List<Contact> matches = new ArrayList<>();
		for (Contact contact : contactRepository.findAllActive()) {
			for (Email e : contact.getEmails()) {
				if (e == null) {
					continue;
				}
				String value = e.getAddress();
				if (containsIgnoreCase(value, query)) {
					matches.add(contact);
					break;
				}
			}
		}
		return matches;
	}

	private static String normalizeQuery(String value) {
		if (value == null) {
			return "";
		}
		return value.trim();
	}

	private static boolean containsIgnoreCase(String value, String query) {
		if (value == null || query == null) {
			return false;
		}
		String v = value.toLowerCase(Locale.ROOT);
		String q = query.toLowerCase(Locale.ROOT);
		return v.contains(q);
	}
}
