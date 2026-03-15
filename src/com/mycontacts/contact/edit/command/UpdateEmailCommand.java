package com.mycontacts.contact.edit.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.mycontacts.common.exception.ValidationException;
import com.mycontacts.contact.edit.memento.ContactMemento;
import com.mycontacts.contact.model.Contact;
import com.mycontacts.contact.model.Email;

public class UpdateEmailCommand implements EditContactCommand {
	private final Contact contact;
	private final Email email;
	private ContactMemento previousState;

	public UpdateEmailCommand(Contact contact, Email email) {
		this.contact = Objects.requireNonNull(contact, "contact cannot be null");
		this.email = email;
	}

	@Override
	public void execute() {
		if (email == null) {
			throw new ValidationException("Email cannot be null");
		}
		previousState = new ContactMemento(contact.getName(), contact.getPhoneNumbers(), contact.getEmails());

		List<Email> updated = new ArrayList<>(contact.getEmails());
		int idx = indexByLabel(updated, email.getLabel());
		if (idx >= 0) {
			updated.set(idx, email);
		} else {
			updated.add(email);
		}
		contact.replaceEmails(updated);
	}

	@Override
	public void undo() {
		if (previousState == null) {
			return;
		}
		contact.updateName(previousState.getName());
		contact.replacePhoneNumbers(previousState.getPhoneNumbers());
		contact.replaceEmails(previousState.getEmails());
	}

	private static int indexByLabel(List<Email> emails, String label) {
		for (int i = 0; i < emails.size(); i++) {
			if (emails.get(i).getLabel().equalsIgnoreCase(label)) {
				return i;
			}
		}
		return -1;
	}
}
