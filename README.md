# MyContacts — UC-09 Search Contacts

This repository contains **Use Case 9 of the MyContacts system**.
It builds on:

- UC-01 User Registration
- UC-02 Authentication
- UC-03 Profile Management
- UC-04 Create Contact
- UC-05 View Contact
- UC-06 Edit Contact
- UC-07 Delete Contact
- UC-08 Contact Groups
- UC-09 Search Contacts

## Description

- **Use Case:** UC-09 Search Contacts
- **Actor:** Authenticated user
- **Goal:** Search for contacts by name, phone number, or email

## Search Flow

1. User logs in
2. User selects **Search Contacts**
3. User chooses a search type (Name / Phone / Email)
4. User enters a query
5. System prints matching contacts (or `No contacts found`)

## Search Types

- **Name:** case-insensitive substring match on contact name
- **Phone:** case-insensitive substring match across a contact's phone numbers
- **Email:** case-insensitive substring match across a contact's email addresses

## Java Concepts Used

- `List` traversal with loops
- Case-insensitive search via `String.toLowerCase(Locale.ROOT)`
- Filtering active contacts via repository (`deleted == false`)

## Testing Summary (JUnit 5)

- `test/com/mycontacts/search/SearchServiceTest.java`
  - `shouldFindContactByName()`
  - `shouldFindContactByPhone()`
  - `shouldFindContactByEmail()`
  - `shouldReturnEmptyListIfNotFound()`