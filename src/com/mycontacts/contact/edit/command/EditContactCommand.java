package com.mycontacts.contact.edit.command;

public interface EditContactCommand {
	void execute();

	void undo();
}
