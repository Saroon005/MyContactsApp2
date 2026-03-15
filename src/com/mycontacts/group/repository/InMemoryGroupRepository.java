package com.mycontacts.group.repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import com.mycontacts.group.model.ContactGroup;

public class InMemoryGroupRepository implements GroupRepository {
	private final Map<UUID, ContactGroup> groupsById = new LinkedHashMap<>();

	@Override
	public void save(ContactGroup group) {
		Objects.requireNonNull(group, "group cannot be null");
		groupsById.put(group.getId(), group);
	}

	@Override
	public Optional<ContactGroup> findById(UUID id) {
		if (id == null) {
			return Optional.empty();
		}
		return Optional.ofNullable(groupsById.get(id));
	}

	@Override
	public List<ContactGroup> findAll() {
		return new ArrayList<>(groupsById.values());
	}
}
