# MyContacts — UC-11 Contact Tags

This repository contains **Use Case 11 of the MyContacts system**.
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
- UC-10 Filter & Sort Contacts
- UC-11 Contact Tags

## Description

- **Use Case:** UC-11 Contact Tags
- **Actor:** Authenticated user
- **Goal:** Categorize contacts using tags (e.g., Family, Work, Friends)

## Tag Management Flow

1. User logs in
2. User can:
   - Create a tag
   - Add a tag to a contact (creates the tag automatically if missing)
   - Remove a tag from a contact
   - View contacts by tag
3. System prints matching contacts (or `No contacts found`)

## Java Concepts Used

- `Set<Tag>` with `HashSet` to store unique tags per contact
- Case-insensitive tag names via normalization (`toLowerCase(Locale.ROOT)`)
- `Map` storage for tags in an in-memory repository
- Simple loops for filtering contacts by tag

## Testing Summary (JUnit 5)

- `test/com/mycontacts/tag/TagServiceTest.java`
  - `shouldCreateTag()`
  - `shouldAddTagToContact()`
  - `shouldRemoveTagFromContact()`
  - `shouldReturnContactsByTag()`