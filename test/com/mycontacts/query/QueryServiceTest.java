package com.mycontacts.query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.mycontacts.contact.model.PersonContact;
import com.mycontacts.contact.repository.InMemoryContactRepository;
import com.mycontacts.contact.service.ContactService;
import com.mycontacts.contact.service.ContactServiceImpl;
import com.mycontacts.filter.service.FilterService;
import com.mycontacts.filter.service.FilterServiceImpl;
import com.mycontacts.group.repository.InMemoryGroupRepository;
import com.mycontacts.query.service.QueryService;
import com.mycontacts.query.service.QueryServiceImpl;
import com.mycontacts.search.service.SearchService;
import com.mycontacts.search.service.SearchServiceImpl;
import com.mycontacts.tag.repository.InMemoryTagRepository;
import com.mycontacts.tag.repository.TagRepository;
import com.mycontacts.tag.service.TagService;
import com.mycontacts.tag.service.TagServiceImpl;

public class QueryServiceTest {
	@Test
	void shouldSearchContactsByTagAndName() {
		var contactRepo = new InMemoryContactRepository();
		var groupRepo = new InMemoryGroupRepository();
		TagRepository tagRepo = new InMemoryTagRepository();
		ContactService contactService = new ContactServiceImpl(contactRepo);
		SearchService searchService = new SearchServiceImpl(contactRepo);
		FilterService filterService = new FilterServiceImpl(contactRepo, groupRepo);
		TagService tagService = new TagServiceImpl(tagRepo, contactRepo);
		QueryService queryService = new QueryServiceImpl(contactRepo, tagRepo, filterService, searchService);

		var c1 = contactService.createPersonContact("Alice Wonderland");
		var c2 = contactService.createPersonContact("Bob Builder");
		var c3 = contactService.createPersonContact("Alice Deleted");
		tagService.addTagToContact(c1.getId(), "Work");
		tagService.addTagToContact(c2.getId(), "Home");
		tagService.addTagToContact(c3.getId(), "WORK");
		contactService.deleteContact(c3.getId());

		var results = queryService.searchContactsByTagAndName("work", "alice");
		assertEquals(1, results.size());
		assertEquals(c1.getId(), results.get(0).getId());
		assertTrue(queryService.searchContactsByTagAndName("home", "alice").isEmpty());
	}

	@Test
	void shouldFilterContactsByTagAndDate() {
		var contactRepo = new InMemoryContactRepository();
		var groupRepo = new InMemoryGroupRepository();
		TagRepository tagRepo = new InMemoryTagRepository();
		SearchService searchService = new SearchServiceImpl(contactRepo);
		FilterService filterService = new FilterServiceImpl(contactRepo, groupRepo);
		TagService tagService = new TagServiceImpl(tagRepo, contactRepo);
		QueryService queryService = new QueryServiceImpl(contactRepo, tagRepo, filterService, searchService);

		var c1 = new PersonContact(UUID.randomUUID(), "A", List.of(), List.of(), LocalDateTime.of(2020, 1, 1, 10, 0),
				"A", "A");
		var c2 = new PersonContact(UUID.randomUUID(), "B", List.of(), List.of(), LocalDateTime.of(2020, 1, 2, 10, 0),
				"B", "B");
		contactRepo.save(c1);
		contactRepo.save(c2);

		tagService.addTagToContact(c1.getId(), "Work");
		tagService.addTagToContact(c2.getId(), "work");

		var results = queryService.filterContactsByTagAndDate("WORK", LocalDate.of(2020, 1, 1));
		assertEquals(1, results.size());
		assertEquals(c1.getId(), results.get(0).getId());
	}

	@Test
	void shouldSortContactsWithinTag() {
		var contactRepo = new InMemoryContactRepository();
		var groupRepo = new InMemoryGroupRepository();
		TagRepository tagRepo = new InMemoryTagRepository();
		ContactService contactService = new ContactServiceImpl(contactRepo);
		SearchService searchService = new SearchServiceImpl(contactRepo);
		FilterService filterService = new FilterServiceImpl(contactRepo, groupRepo);
		TagService tagService = new TagServiceImpl(tagRepo, contactRepo);
		QueryService queryService = new QueryServiceImpl(contactRepo, tagRepo, filterService, searchService);

		var c1 = contactService.createPersonContact("Charlie");
		var c2 = contactService.createPersonContact("alice");
		var c3 = contactService.createPersonContact("Bob");
		var c4 = contactService.createPersonContact("Zed");

		tagService.addTagToContact(c1.getId(), "Work");
		tagService.addTagToContact(c2.getId(), "work");
		tagService.addTagToContact(c3.getId(), "WORK");
		// c4 is intentionally not tagged
		contactService.deleteContact(c3.getId());

		var results = queryService.sortContactsWithinTag("work");
		assertEquals(List.of("alice", "Charlie"), results.stream().map(c -> c.getName()).toList());
		assertTrue(results.stream().noneMatch(c -> c.getId().equals(c4.getId())));
	}
}
