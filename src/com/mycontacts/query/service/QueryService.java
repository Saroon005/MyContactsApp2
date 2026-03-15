package com.mycontacts.query.service;

import java.time.LocalDate;
import java.util.List;

import com.mycontacts.contact.model.Contact;

public interface QueryService {
	List<Contact> searchContactsByTagAndName(String tagName, String name);

	List<Contact> filterContactsByTagAndDate(String tagName, LocalDate date);

	List<Contact> sortContactsWithinTag(String tagName);
}
