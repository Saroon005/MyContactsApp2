package com.mycontacts.group.service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.mycontacts.common.exception.ValidationException;
import com.mycontacts.common.util.IdGenerator;
import com.mycontacts.contact.model.Contact;
import com.mycontacts.contact.service.ContactService;
import com.mycontacts.group.model.ContactGroup;
import com.mycontacts.group.repository.GroupRepository;

public class GroupServiceImpl implements GroupService {
	private final GroupRepository groupRepository;
	private final ContactService contactService;

	public GroupServiceImpl(GroupRepository groupRepository, ContactService contactService) {
		this.groupRepository = Objects.requireNonNull(groupRepository, "groupRepository cannot be null");
		this.contactService = Objects.requireNonNull(contactService, "contactService cannot be null");
	}

	@Override
	public ContactGroup createGroup(String name) {
		UUID id = IdGenerator.generateId();
		ContactGroup group = new ContactGroup(id, name);
		groupRepository.save(group);
		return group;
	}

	@Override
	public void addContactToGroup(UUID groupId, Contact contact) {
		ContactGroup group = groupRepository.findById(groupId)
				.orElseThrow(() -> new ValidationException("Group not found"));
		group.addContact(contact);
		groupRepository.save(group);
	}

	@Override
	public List<Contact> getGroupContacts(UUID groupId) {
		ContactGroup group = groupRepository.findById(groupId)
				.orElseThrow(() -> new ValidationException("Group not found"));
		return group.getContacts();
	}

	@Override
	public void deleteAllContactsInGroup(UUID groupId) {
		ContactGroup group = groupRepository.findById(groupId)
				.orElseThrow(() -> new ValidationException("Group not found"));

		for (Contact contact : group.getContacts()) {
			if (contact == null) {
				continue;
			}
			if (contactService.getContactById(contact.getId()).isPresent()) {
				contactService.deleteContact(contact.getId());
			}
		}
	}
}
