package com.mycontacts.contact.display;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mycontacts.contact.model.Contact;

public class UpperCaseNameDecorator extends ContactViewDecorator {
	private static final Pattern NAME_LINE = Pattern.compile("(?m)^Name: (.*)$");

	public UpperCaseNameDecorator(ContactView inner) {
		super(inner);
	}

	@Override
	public String display(Contact contact) {
		Objects.requireNonNull(contact, "contact cannot be null");
		String base = inner.display(contact);
		Matcher m = NAME_LINE.matcher(base);
		if (!m.find()) {
			return base;
		}
		String originalName = m.group(1);
		return m.replaceFirst("Name: " + originalName.toUpperCase());
	}
}
