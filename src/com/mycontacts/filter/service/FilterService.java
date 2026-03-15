package com.mycontacts.filter.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.mycontacts.contact.model.Contact;

public interface FilterService {
	List<Contact> filterByGroup(UUID groupId);

	List<Contact> filterByDate(LocalDate date);

	List<Contact> sortByName();

	List<Contact> sortByCreationDate();
}
