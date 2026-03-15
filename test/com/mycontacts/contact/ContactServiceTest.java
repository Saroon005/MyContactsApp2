package com.mycontacts.contact;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.mycontacts.contact.model.OrganizationContact;
import com.mycontacts.contact.model.PersonContact;
import com.mycontacts.contact.repository.ContactRepository;
import com.mycontacts.contact.repository.InMemoryContactRepository;
import com.mycontacts.contact.service.ContactService;
import com.mycontacts.contact.service.ContactServiceImpl;

public class ContactServiceTest {
	@Test
	void shouldCreatePersonContact() {
		ContactRepository repo = new InMemoryContactRepository();
		ContactService service = new ContactServiceImpl(repo);

		var contact = service.createPersonContact("John Doe");
		assertTrue(contact instanceof PersonContact);
		assertNotNull(contact.getId());
		assertEquals("John Doe", contact.getName());
	}

	@Test
	void shouldCreateOrganizationContact() {
		ContactRepository repo = new InMemoryContactRepository();
		ContactService service = new ContactServiceImpl(repo);

		var contact = service.createOrganizationContact("Acme Inc");
		assertTrue(contact instanceof OrganizationContact);
		assertNotNull(contact.getId());
		assertEquals("Acme Inc", contact.getName());
	}

	@Test
	void shouldStoreContactInRepository() {
		ContactRepository repo = new InMemoryContactRepository();
		ContactService service = new ContactServiceImpl(repo);

		var contact = service.createPersonContact("Jane Doe");
		assertTrue(repo.findById(contact.getId()).isPresent());
	}

	@Test
	void shouldReturnAllContacts() {
		ContactRepository repo = new InMemoryContactRepository();
		ContactService service = new ContactServiceImpl(repo);

		service.createPersonContact("A");
		service.createOrganizationContact("B");
		assertEquals(2, service.getAllContacts().size());
	}
}
