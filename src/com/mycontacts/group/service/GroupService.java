package com.mycontacts.group.service;

import java.util.List;
import java.util.UUID;

import com.mycontacts.contact.model.Contact;
import com.mycontacts.group.model.ContactGroup;

public interface GroupService {
	ContactGroup createGroup(String name);

	void addContactToGroup(UUID groupId, Contact contact);

	List<Contact> getGroupContacts(UUID groupId);

	void deleteAllContactsInGroup(UUID groupId);
}
