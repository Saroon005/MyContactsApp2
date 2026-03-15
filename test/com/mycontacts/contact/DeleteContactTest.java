package com.mycontacts.contact;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.mycontacts.contact.repository.InMemoryContactRepository;
import com.mycontacts.contact.service.ContactService;
import com.mycontacts.contact.service.ContactServiceImpl;

public class DeleteContactTest {
	@Test
	void shouldSoftDeleteContact() {
		var repo = new InMemoryContactRepository();
		ContactService service = new ContactServiceImpl(repo);

		var contact = service.createPersonContact("John Doe");
		service.deleteContact(contact.getId());

		var stored = repo.findById(contact.getId()).orElseThrow();
		assertTrue(stored.isDeleted());
	}

	@Test
	void shouldHardDeleteContact() {
		var repo = new InMemoryContactRepository();
		ContactService service = new ContactServiceImpl(repo);

		var contact = service.createPersonContact("John Doe");
		service.hardDeleteContact(contact.getId());

		assertTrue(repo.findById(contact.getId()).isEmpty());
	}

	@Test
	void shouldNotReturnSoftDeletedContacts() {
		var repo = new InMemoryContactRepository();
		ContactService service = new ContactServiceImpl(repo);

		var c1 = service.createPersonContact("John Doe");
		var c2 = service.createPersonContact("Jane Doe");

		service.deleteContact(c1.getId());

		var active = repo.findAllActive();
		assertEquals(1, active.size());
		assertFalse(active.get(0).isDeleted());
		assertEquals(c2.getId(), active.get(0).getId());
	}
}
