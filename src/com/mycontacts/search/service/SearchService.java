package com.mycontacts.search.service;

import java.util.List;

import com.mycontacts.contact.model.Contact;

public interface SearchService {
	List<Contact> searchByName(String name);

	List<Contact> searchByPhone(String phone);

	List<Contact> searchByEmail(String email);
}
