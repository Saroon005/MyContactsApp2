package com.mycontacts.contact.display;

import java.util.Objects;

public abstract class ContactViewDecorator implements ContactView {
	protected final ContactView inner;

	protected ContactViewDecorator(ContactView inner) {
		this.inner = Objects.requireNonNull(inner, "inner ContactView cannot be null");
	}
}
