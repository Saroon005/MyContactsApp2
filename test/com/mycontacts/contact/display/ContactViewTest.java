package com.mycontacts.contact.display;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.mycontacts.contact.model.Email;
import com.mycontacts.contact.model.PersonContact;
import com.mycontacts.contact.model.PhoneNumber;

public class ContactViewTest {
	private static PersonContact sampleContact() {
		UUID id = UUID.fromString("11111111-1111-1111-1111-111111111111");
		LocalDateTime createdAt = LocalDateTime.of(2026, 3, 16, 10, 30);
		return new PersonContact(id, "John Doe", List.of(new PhoneNumber("9876543210", "Mobile")),
				List.of(new Email("john.doe@email.com", "Primary")), createdAt, "John", "Doe");
	}

	@Test
	void shouldDisplayContactNormally() {
		var contact = sampleContact();
		ContactView view = new BaseContactView();
		String out = view.display(contact);

		assertTrue(out.contains("ID: 11111111-1111-1111-1111-111111111111"));
		assertTrue(out.contains("Name: John Doe"));
		assertTrue(out.contains("Phones: Mobile: 9876543210"));
		assertTrue(out.contains("Emails: Primary: john.doe@email.com"));
		assertTrue(out.contains("Created: 2026-03-16"));
	}

	@Test
	void shouldDisplayUppercaseName() {
		var contact = sampleContact();
		ContactView view = new UpperCaseNameDecorator(new BaseContactView());
		String out = view.display(contact);

		assertTrue(out.contains("Name: JOHN DOE"));
	}

	@Test
	void shouldMaskEmailAddress() {
		var contact = sampleContact();
		ContactView view = new MaskedEmailDecorator(new BaseContactView());
		String out = view.display(contact);

		assertTrue(out.contains("j***@email.com"));
		assertFalse(out.contains("john.doe@email.com"));
	}

	@Test
	void shouldAllowMultipleDecorators() {
		var contact = sampleContact();
		ContactView view = new MaskedEmailDecorator(new UpperCaseNameDecorator(new BaseContactView()));
		String out = view.display(contact);

		assertTrue(out.contains("Name: JOHN DOE"));
		assertTrue(out.contains("j***@email.com"));
	}
}
