package com.mycontacts.tag.repository;

import java.util.List;
import java.util.Optional;

import com.mycontacts.tag.model.Tag;

public interface TagRepository {
	void save(Tag tag);

	Optional<Tag> findByName(String name);

	List<Tag> findAll();
}
