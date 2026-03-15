package com.mycontacts.group;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.mycontacts.contact.repository.InMemoryContactRepository;
import com.mycontacts.contact.service.ContactService;
import com.mycontacts.contact.service.ContactServiceImpl;
import com.mycontacts.group.repository.InMemoryGroupRepository;
import com.mycontacts.group.service.GroupService;
import com.mycontacts.group.service.GroupServiceImpl;

public class GroupServiceTest {
	@Test
	void shouldCreateGroup() {
		var groupRepo = new InMemoryGroupRepository();
		var contactRepo = new InMemoryContactRepository();
		ContactService contactService = new ContactServiceImpl(contactRepo);
		GroupService groupService = new GroupServiceImpl(groupRepo, contactService);

		var group = groupService.createGroup("Friends");
		assertNotNull(group.getId());
		assertEquals("Friends", group.getName());
	}

	@Test
	void shouldAddContactToGroup() {
		var groupRepo = new InMemoryGroupRepository();
		var contactRepo = new InMemoryContactRepository();
		ContactService contactService = new ContactServiceImpl(contactRepo);
		GroupService groupService = new GroupServiceImpl(groupRepo, contactService);

		var group = groupService.createGroup("Work");
		var contact = contactService.createPersonContact("John Doe");

		groupService.addContactToGroup(group.getId(), contact);

		var members = groupService.getGroupContacts(group.getId());
		assertEquals(1, members.size());
		assertEquals(contact.getId(), members.get(0).getId());
	}

	@Test
	void shouldReturnGroupContacts() {
		var groupRepo = new InMemoryGroupRepository();
		var contactRepo = new InMemoryContactRepository();
		ContactService contactService = new ContactServiceImpl(contactRepo);
		GroupService groupService = new GroupServiceImpl(groupRepo, contactService);

		var group = groupService.createGroup("Family");
		var c1 = contactService.createPersonContact("Alice");
		var c2 = contactService.createPersonContact("Bob");

		groupService.addContactToGroup(group.getId(), c1);
		groupService.addContactToGroup(group.getId(), c2);

		var members = groupService.getGroupContacts(group.getId());
		assertEquals(2, members.size());
	}

	@Test
	void shouldDeleteAllContactsInGroup() {
		var groupRepo = new InMemoryGroupRepository();
		var contactRepo = new InMemoryContactRepository();
		ContactService contactService = new ContactServiceImpl(contactRepo);
		GroupService groupService = new GroupServiceImpl(groupRepo, contactService);

		var group = groupService.createGroup("ToDelete");
		var c1 = contactService.createPersonContact("A");
		var c2 = contactService.createPersonContact("B");

		groupService.addContactToGroup(group.getId(), c1);
		groupService.addContactToGroup(group.getId(), c2);

		groupService.deleteAllContactsInGroup(group.getId());

		assertTrue(contactService.getContactById(c1.getId()).isEmpty());
		assertTrue(contactService.getContactById(c2.getId()).isEmpty());
		assertTrue(contactRepo.findById(c1.getId()).orElseThrow().isDeleted());
		assertTrue(contactRepo.findById(c2.getId()).orElseThrow().isDeleted());
	}
}
