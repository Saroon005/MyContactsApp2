# MyContacts — UC-05 View Contact Details

This repository contains **Use Case 5 of the MyContacts system**.
It builds on:

- **UC-01:** User Registration
- **UC-02:** Authentication
- **UC-03:** Profile Management
- **UC-04:** Contact Creation
- **UC-05:** Contact Viewing (this version)

## Description

- **Use Case:** UC-05 View Contact Details
- **Actor:** Authenticated user
- **Goal:** View stored contact details with optional formatting

## Contact Viewing Flow

1. User logs in
2. User chooses **View Contact Details**
3. User enters a contact ID
4. System retrieves the contact from the repository via `ContactService`
5. User chooses a display mode:
	 - Normal View
	 - Uppercase Name
	 - Mask Email
6. The application applies decorators dynamically and prints the formatted output

## OOP Concepts Used

- **Abstraction:** `ContactView` interface defines display behavior
- **Encapsulation:** display logic is separated from the `Contact` model
- **Polymorphism:** decorators wrap a `ContactView` without changing callers

## Design Patterns Used

- **Decorator Pattern:** `UpperCaseNameDecorator`, `MaskedEmailDecorator`
- **Builder Pattern (from UC-04):** contact construction
- **Command Pattern (from UC-03):** profile operations

## Java Concepts Used

- `Optional` for `getContactById` and session checks
- `UUID` parsing (`UUID.fromString`) for contact lookup
- `Pattern` / `Matcher` for formatting transforms

## Testing Summary (JUnit 5)

- Views: `test/com/mycontacts/contact/display/ContactViewTest.java`
	- `shouldDisplayContactNormally()`
	- `shouldDisplayUppercaseName()`
	- `shouldMaskEmailAddress()`
	- `shouldAllowMultipleDecorators()`

Run tests using VS Code Testing (Java Test Runner) or Eclipse JUnit.