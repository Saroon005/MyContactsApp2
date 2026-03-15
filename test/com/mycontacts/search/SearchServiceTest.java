package com.mycontacts.search;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.mycontacts.contact.model.Email;
import com.mycontacts.contact.model.PhoneNumber;
import com.mycontacts.contact.repository.InMemoryContactRepository;
import com.mycontacts.contact.service.ContactService;
import com.mycontacts.contact.service.ContactServiceImpl;
import com.mycontacts.search.service.SearchService;
import com.mycontacts.search.service.SearchServiceImpl;

public class SearchServiceTest {
	@Test
	void shouldFindContactByName() {
		var repo = new InMemoryContactRepository();
		ContactService contactService = new ContactServiceImpl(repo);
		SearchService searchService = new SearchServiceImpl(repo);

		var c1 = contactService.createPersonContact("John Doe", null, null);
		contactService.createPersonContact("Jane Smith", null, null);

		var results = searchService.searchByName("john");
		assertEquals(1, results.size());
		assertEquals(c1.getId(), results.get(0).getId());
	}

	@Test
	void shouldFindContactByPhone() {
		var repo = new InMemoryContactRepository();
		ContactService contactService = new ContactServiceImpl(repo);
		SearchService searchService = new SearchServiceImpl(repo);

		var c1 = contactService.createPersonContact("Alice", new PhoneNumber("555-1234", "Mobile"), null);
		contactService.createPersonContact("Bob", new PhoneNumber("555-9999", "Mobile"), null);

		var results = searchService.searchByPhone("1234");
		assertEquals(1, results.size());
		assertEquals(c1.getId(), results.get(0).getId());
	}

	@Test
	void shouldFindContactByEmail() {
		var repo = new InMemoryContactRepository();
		ContactService contactService = new ContactServiceImpl(repo);
		SearchService searchService = new SearchServiceImpl(repo);

		var c1 = contactService.createPersonContact("Alice", null, new Email("alice@example.com", "Primary"));
		contactService.createPersonContact("Bob", null, new Email("bob@company.com", "Primary"));

		var results = searchService.searchByEmail("EXAMPLE");
		assertEquals(1, results.size());
		assertEquals(c1.getId(), results.get(0).getId());
	}

	@Test
	void shouldReturnEmptyListIfNotFound() {
		var repo = new InMemoryContactRepository();
		ContactService contactService = new ContactServiceImpl(repo);
		SearchService searchService = new SearchServiceImpl(repo);

		contactService.createPersonContact("John Doe", new PhoneNumber("555-1234", "Mobile"),
				new Email("john@example.com", "Primary"));

		assertTrue(searchService.searchByName("nope").isEmpty());
		assertTrue(searchService.searchByPhone("0000").isEmpty());
		assertTrue(searchService.searchByEmail("missing").isEmpty());
	}
}
