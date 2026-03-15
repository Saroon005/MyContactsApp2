package com.mycontacts.contact.edit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.mycontacts.contact.model.Email;
import com.mycontacts.contact.model.PhoneNumber;
import com.mycontacts.contact.repository.InMemoryContactRepository;
import com.mycontacts.contact.service.ContactService;
import com.mycontacts.contact.service.ContactServiceImpl;

public class EditContactCommandTest {
	@Test
	void shouldUpdateContactName() {
		var repo = new InMemoryContactRepository();
		ContactService service = new ContactServiceImpl(repo);

		var contact = service.createPersonContact("John Doe");
		service.updateContactName(contact.getId(), "Jane Doe");
		assertEquals("Jane Doe", service.getContactById(contact.getId()).orElseThrow().getName());
	}

	@Test
	void shouldUndoNameChange() {
		var repo = new InMemoryContactRepository();
		ContactService service = new ContactServiceImpl(repo);

		var contact = service.createPersonContact("John Doe");
		service.updateContactName(contact.getId(), "Jane Doe");
		service.undoLastEdit();
		assertEquals("John Doe", service.getContactById(contact.getId()).orElseThrow().getName());
	}

	@Test
	void shouldRedoNameChange() {
		var repo = new InMemoryContactRepository();
		ContactService service = new ContactServiceImpl(repo);

		var contact = service.createPersonContact("John Doe");
		service.updateContactName(contact.getId(), "Jane Doe");
		service.undoLastEdit();
		service.redoLastEdit();
		assertEquals("Jane Doe", service.getContactById(contact.getId()).orElseThrow().getName());
	}

	@Test
	void shouldUpdatePhone() {
		var repo = new InMemoryContactRepository();
		ContactService service = new ContactServiceImpl(repo);

		var contact = service.createPersonContact("John Doe", new PhoneNumber("111", "Mobile"), null);
		service.updatePhone(contact.getId(), new PhoneNumber("222", "Mobile"));

		var updated = service.getContactById(contact.getId()).orElseThrow();
		assertEquals(1, updated.getPhoneNumbers().size());
		assertEquals("222", updated.getPhoneNumbers().get(0).getNumber());
	}

	@Test
	void shouldRestorePreviousState() {
		var repo = new InMemoryContactRepository();
		ContactService service = new ContactServiceImpl(repo);

		var contact = service.createPersonContact("John Doe", new PhoneNumber("111", "Mobile"),
				new Email("john@email.com", "Primary"));
		service.updatePhone(contact.getId(), new PhoneNumber("222", "Mobile"));
		service.updateEmail(contact.getId(), new Email("john2@email.com", "Primary"));

		var updated = service.getContactById(contact.getId()).orElseThrow();
		assertEquals("222", updated.getPhoneNumbers().get(0).getNumber());
		assertTrue(updated.getEmails().get(0).getAddress().contains("john2"));

		service.undoLastEdit(); // undo email update
		var afterUndoEmail = service.getContactById(contact.getId()).orElseThrow();
		assertFalse(afterUndoEmail.getEmails().get(0).getAddress().contains("john2"));
		assertEquals("222", afterUndoEmail.getPhoneNumbers().get(0).getNumber());

		service.undoLastEdit(); // undo phone update
		var afterUndoPhone = service.getContactById(contact.getId()).orElseThrow();
		assertEquals("111", afterUndoPhone.getPhoneNumbers().get(0).getNumber());
	}
}
