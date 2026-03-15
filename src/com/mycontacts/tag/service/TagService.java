package com.mycontacts.tag.service;

import java.util.List;
import java.util.UUID;

import com.mycontacts.contact.model.Contact;
import com.mycontacts.tag.model.Tag;

public interface TagService {
	Tag createTag(String name);

	void addTagToContact(UUID contactId, String tagName);

	void removeTagFromContact(UUID contactId, String tagName);

	List<Contact> getContactsByTag(String tagName);
}
