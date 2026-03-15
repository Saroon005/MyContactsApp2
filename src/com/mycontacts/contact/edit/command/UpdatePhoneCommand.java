package com.mycontacts.contact.edit.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.mycontacts.common.exception.ValidationException;
import com.mycontacts.contact.edit.memento.ContactMemento;
import com.mycontacts.contact.model.Contact;
import com.mycontacts.contact.model.PhoneNumber;

public class UpdatePhoneCommand implements EditContactCommand {
	private final Contact contact;
	private final PhoneNumber phone;
	private ContactMemento previousState;

	public UpdatePhoneCommand(Contact contact, PhoneNumber phone) {
		this.contact = Objects.requireNonNull(contact, "contact cannot be null");
		this.phone = phone;
	}

	@Override
	public void execute() {
		if (phone == null) {
			throw new ValidationException("Phone cannot be null");
		}
		previousState = new ContactMemento(contact.getName(), contact.getPhoneNumbers(), contact.getEmails());

		List<PhoneNumber> updated = new ArrayList<>(contact.getPhoneNumbers());
		int idx = indexByLabel(updated, phone.getLabel());
		if (idx >= 0) {
			updated.set(idx, phone);
		} else {
			updated.add(phone);
		}
		contact.replacePhoneNumbers(updated);
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

	private static int indexByLabel(List<PhoneNumber> phones, String label) {
		for (int i = 0; i < phones.size(); i++) {
			if (phones.get(i).getLabel().equalsIgnoreCase(label)) {
				return i;
			}
		}
		return -1;
	}
}
