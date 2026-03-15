package com.mycontacts.tag;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.mycontacts.contact.repository.InMemoryContactRepository;
import com.mycontacts.contact.service.ContactService;
import com.mycontacts.contact.service.ContactServiceImpl;
import com.mycontacts.tag.repository.InMemoryTagRepository;
import com.mycontacts.tag.service.TagService;
import com.mycontacts.tag.service.TagServiceImpl;

public class TagServiceTest {
	@Test
	void shouldCreateTag() {
		var contactRepo = new InMemoryContactRepository();
		var tagRepo = new InMemoryTagRepository();
		TagService tagService = new TagServiceImpl(tagRepo, contactRepo);

		var t1 = tagService.createTag("Work");
		var t2 = tagService.createTag("work");

		assertEquals(t1.getId(), t2.getId());
		assertEquals("work", t1.getName());
	}

	@Test
	void shouldAddTagToContact() {
		var contactRepo = new InMemoryContactRepository();
		var tagRepo = new InMemoryTagRepository();
		ContactService contactService = new ContactServiceImpl(contactRepo);
		TagService tagService = new TagServiceImpl(tagRepo, contactRepo);

		var contact = contactService.createPersonContact("Alice");
		tagService.addTagToContact(contact.getId(), "Family");

		var stored = contactRepo.findById(contact.getId()).orElseThrow();
		assertTrue(stored.getTags().stream().anyMatch(t -> t.getName().equals("family")));
	}

	@Test
	void shouldRemoveTagFromContact() {
		var contactRepo = new InMemoryContactRepository();
		var tagRepo = new InMemoryTagRepository();
		ContactService contactService = new ContactServiceImpl(contactRepo);
		TagService tagService = new TagServiceImpl(tagRepo, contactRepo);

		var contact = contactService.createPersonContact("Bob");
		tagService.addTagToContact(contact.getId(), "Friends");
		tagService.removeTagFromContact(contact.getId(), "friends");

		var stored = contactRepo.findById(contact.getId()).orElseThrow();
		assertTrue(stored.getTags().isEmpty());
	}

	@Test
	void shouldReturnContactsByTag() {
		var contactRepo = new InMemoryContactRepository();
		var tagRepo = new InMemoryTagRepository();
		ContactService contactService = new ContactServiceImpl(contactRepo);
		TagService tagService = new TagServiceImpl(tagRepo, contactRepo);

		var c1 = contactService.createPersonContact("Alice");
		var c2 = contactService.createPersonContact("Charlie");
		tagService.addTagToContact(c1.getId(), "Emergency");

		var results = tagService.getContactsByTag("EMERGENCY");
		assertEquals(1, results.size());
		assertEquals(c1.getId(), results.get(0).getId());
		assertTrue(results.stream().noneMatch(c -> c.getId().equals(c2.getId())));
	}
}
