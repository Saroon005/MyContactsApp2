package com.mycontacts.query.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import com.mycontacts.contact.model.Contact;
import com.mycontacts.contact.repository.ContactRepository;
import com.mycontacts.filter.service.FilterService;
import com.mycontacts.search.service.SearchService;
import com.mycontacts.tag.model.Tag;
import com.mycontacts.tag.repository.TagRepository;

public class QueryServiceImpl implements QueryService {
	private final ContactRepository contactRepository;
	private final TagRepository tagRepository;
	private final FilterService filterService;
	private final SearchService searchService;

	public QueryServiceImpl(ContactRepository contactRepository, TagRepository tagRepository, FilterService filterService,
			SearchService searchService) {
		this.contactRepository = Objects.requireNonNull(contactRepository, "contactRepository cannot be null");
		this.tagRepository = Objects.requireNonNull(tagRepository, "tagRepository cannot be null");
		this.filterService = Objects.requireNonNull(filterService, "filterService cannot be null");
		this.searchService = Objects.requireNonNull(searchService, "searchService cannot be null");
	}

	@Override
	public List<Contact> searchContactsByTagAndName(String tagName, String name) {
		Set<UUID> tagIds = getActiveContactIdsWithTag(tagName);
		if (tagIds.isEmpty()) {
			return List.of();
		}

		List<Contact> nameMatches = searchService.searchByName(name);
		if (nameMatches.isEmpty()) {
			return List.of();
		}

		List<Contact> results = new ArrayList<>();
		for (Contact c : nameMatches) {
			if (tagIds.contains(c.getId())) {
				results.add(c);
			}
		}
		return results;
	}

	@Override
	public List<Contact> filterContactsByTagAndDate(String tagName, LocalDate date) {
		Set<UUID> tagIds = getActiveContactIdsWithTag(tagName);
		if (tagIds.isEmpty()) {
			return List.of();
		}

		List<Contact> dateMatches = filterService.filterByDate(date);
		if (dateMatches.isEmpty()) {
			return List.of();
		}

		List<Contact> results = new ArrayList<>();
		for (Contact c : dateMatches) {
			if (tagIds.contains(c.getId())) {
				results.add(c);
			}
		}
		return results;
	}

	@Override
	public List<Contact> sortContactsWithinTag(String tagName) {
		Set<UUID> tagIds = getActiveContactIdsWithTag(tagName);
		if (tagIds.isEmpty()) {
			return List.of();
		}

		List<Contact> sortedAll = filterService.sortByName();
		List<Contact> results = new ArrayList<>();
		for (Contact c : sortedAll) {
			if (tagIds.contains(c.getId())) {
				results.add(c);
			}
		}
		return results;
	}

	private Set<UUID> getActiveContactIdsWithTag(String tagName) {
		if (tagName == null || tagName.trim().isEmpty()) {
			return Set.of();
		}

		var tagOpt = tagRepository.findByName(tagName);
		if (tagOpt.isEmpty()) {
			return Set.of();
		}
		Tag tag = tagOpt.orElseThrow();

		Set<UUID> ids = new HashSet<>();
		for (Contact c : contactRepository.findAllActive()) {
			if (c.getTags().contains(tag)) {
				ids.add(c.getId());
			}
		}
		return ids;
	}
}
