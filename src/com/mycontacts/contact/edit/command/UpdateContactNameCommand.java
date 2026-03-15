package com.mycontacts.contact.edit.command;

import java.util.Objects;

import com.mycontacts.contact.edit.memento.ContactMemento;
import com.mycontacts.contact.model.Contact;

public class UpdateContactNameCommand implements EditContactCommand {
	private final Contact contact;
	private final String newName;
	private ContactMemento previousState;

	public UpdateContactNameCommand(Contact contact, String newName) {
		this.contact = Objects.requireNonNull(contact, "contact cannot be null");
		this.newName = newName;
	}

	@Override
	public void execute() {
		previousState = new ContactMemento(contact.getName(), contact.getPhoneNumbers(), contact.getEmails());
		contact.updateName(newName);
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
}
