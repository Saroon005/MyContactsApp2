package com.mycontacts.contact.display;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mycontacts.contact.model.Contact;

public class MaskedEmailDecorator extends ContactViewDecorator {
	private static final Pattern EMAIL_PATTERN = Pattern.compile("[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+");

	public MaskedEmailDecorator(ContactView inner) {
		super(inner);
	}

	@Override
	public String display(Contact contact) {
		Objects.requireNonNull(contact, "contact cannot be null");
		String base = inner.display(contact);
		Matcher matcher = EMAIL_PATTERN.matcher(base);
		StringBuffer out = new StringBuffer();
		while (matcher.find()) {
			String email = matcher.group();
			matcher.appendReplacement(out, Matcher.quoteReplacement(mask(email)));
		}
		matcher.appendTail(out);
		return out.toString();
	}

	private static String mask(String email) {
		int at = email.indexOf('@');
		if (at <= 0 || at == email.length() - 1) {
			return email;
		}
		String local = email.substring(0, at);
		String domain = email.substring(at + 1);
		char first = local.charAt(0);
		return first + "***@" + domain;
	}
}
