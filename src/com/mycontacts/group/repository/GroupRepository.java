package com.mycontacts.group.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.mycontacts.group.model.ContactGroup;

public interface GroupRepository {
	void save(ContactGroup group);

	Optional<ContactGroup> findById(UUID id);

	List<ContactGroup> findAll();
}
