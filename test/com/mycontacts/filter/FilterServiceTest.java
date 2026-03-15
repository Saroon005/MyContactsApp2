package com.mycontacts.filter;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
import com.mycontacts.group.model.ContactGroup;
import com.mycontacts.group.repository.InMemoryGroupRepository;

public class FilterServiceTest {
	@Test
	void shouldFilterContactsByGroup() {
		var contactRepo = new InMemoryContactRepository();
		var groupRepo = new InMemoryGroupRepository();
		ContactService contactService = new ContactServiceImpl(contactRepo);
		FilterService filterService = new FilterServiceImpl(contactRepo, groupRepo);

		var c1 = contactService.createPersonContact("Alice");
		var c2 = contactService.createPersonContact("Bob");

		UUID groupId = UUID.randomUUID();
		var group = new ContactGroup(groupId, "Friends");
		group.addContact(c1);
		group.addContact(c2);
		groupRepo.save(group);

		contactService.deleteContact(c2.getId());

		var results = filterService.filterByGroup(groupId);
		assertEquals(1, results.size());
		assertEquals(c1.getId(), results.get(0).getId());
	}

	@Test
	void shouldFilterContactsByDate() {
		var contactRepo = new InMemoryContactRepository();
		var groupRepo = new InMemoryGroupRepository();
		FilterService filterService = new FilterServiceImpl(contactRepo, groupRepo);

		var d1 = LocalDate.of(2020, 1, 1);
		var d2 = LocalDate.of(2020, 1, 2);

		var c1 = new PersonContact(UUID.randomUUID(), "A", List.of(), List.of(), LocalDateTime.of(2020, 1, 1, 10, 0),
				"A", "A");
		var c2 = new PersonContact(UUID.randomUUID(), "B", List.of(), List.of(), LocalDateTime.of(2020, 1, 2, 10, 0),
				"B", "B");
		contactRepo.save(c1);
		contactRepo.save(c2);

		var results = filterService.filterByDate(d1);
		assertEquals(1, results.size());
		assertEquals(c1.getId(), results.get(0).getId());

		assertEquals(0, filterService.filterByDate(LocalDate.of(1999, 1, 1)).size());
		assertEquals(1, filterService.filterByDate(d2).size());
	}

	@Test
	void shouldSortContactsByName() {
		var contactRepo = new InMemoryContactRepository();
		var groupRepo = new InMemoryGroupRepository();
		FilterService filterService = new FilterServiceImpl(contactRepo, groupRepo);

		var c1 = new PersonContact(UUID.randomUUID(), "Charlie", List.of(), List.of(), LocalDateTime.of(2020, 1, 1, 10, 0),
				"C", "C");
		var c2 = new PersonContact(UUID.randomUUID(), "alice", List.of(), List.of(), LocalDateTime.of(2020, 1, 1, 10, 1),
				"A", "A");
		var c3 = new PersonContact(UUID.randomUUID(), "Bob", List.of(), List.of(), LocalDateTime.of(2020, 1, 1, 10, 2),
				"B", "B");

		contactRepo.save(c1);
		contactRepo.save(c2);
		contactRepo.save(c3);

		var results = filterService.sortByName();
		assertEquals(List.of("alice", "Bob", "Charlie"), results.stream().map(c -> c.getName()).toList());
	}

	@Test
	void shouldSortContactsByCreationDate() {
		var contactRepo = new InMemoryContactRepository();
		var groupRepo = new InMemoryGroupRepository();
		FilterService filterService = new FilterServiceImpl(contactRepo, groupRepo);

		var c1 = new PersonContact(UUID.randomUUID(), "A", List.of(), List.of(), LocalDateTime.of(2020, 1, 3, 10, 0),
				"A", "A");
		var c2 = new PersonContact(UUID.randomUUID(), "B", List.of(), List.of(), LocalDateTime.of(2020, 1, 1, 10, 0),
				"B", "B");
		var c3 = new PersonContact(UUID.randomUUID(), "C", List.of(), List.of(), LocalDateTime.of(2020, 1, 2, 10, 0),
				"C", "C");

		contactRepo.save(c1);
		contactRepo.save(c2);
		contactRepo.save(c3);

		var results = filterService.sortByCreationDate();
		assertEquals(List.of(c2.getId(), c3.getId(), c1.getId()), results.stream().map(c -> c.getId()).toList());
	}
}
