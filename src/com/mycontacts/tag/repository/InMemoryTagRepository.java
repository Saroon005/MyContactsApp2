package com.mycontacts.tag.repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import com.mycontacts.tag.model.Tag;

public class InMemoryTagRepository implements TagRepository {
	private final Map<String, Tag> tagsByName = new LinkedHashMap<>();

	@Override
	public void save(Tag tag) {
		Objects.requireNonNull(tag, "tag cannot be null");
		tagsByName.put(normalizeKey(tag.getName()), tag);
	}

	@Override
	public Optional<Tag> findByName(String name) {
		String key = normalizeKey(name);
		if (key.isEmpty()) {
			return Optional.empty();
		}
		return Optional.ofNullable(tagsByName.get(key));
	}

	@Override
	public List<Tag> findAll() {
		return new ArrayList<>(tagsByName.values());
	}

	private static String normalizeKey(String value) {
		if (value == null) {
			return "";
		}
		return value.trim().toLowerCase(Locale.ROOT);
	}
}
