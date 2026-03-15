# MyContacts — UC-12 Advanced Contact Queries

This repository contains **Use Case 12 of the MyContacts system**.
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
- UC-12 Advanced Contact Queries

## Description

- **Use Case:** UC-12 Advanced Contact Queries
- **Actor:** Authenticated user
- **Goal:** Run combined queries within a specific tag

## Advanced Queries Flow

1. User logs in
2. User can:
   - Search contacts by **tag + name**
   - Filter contacts by **tag + creation date**
   - Sort contacts **within a tag** (by name)
3. System prints matching contacts (or `No contacts found`)

## Java Concepts Used

- Service reuse via composition (query service reuses search/filter/tag repositories)
- Simple list intersection using IDs
- Case-insensitive tag lookup via normalization in the repository
- Active-only results (deleted contacts excluded)

## Testing Summary (JUnit 5)

- `test/com/mycontacts/query/QueryServiceTest.java`
   - `shouldSearchContactsByTagAndName()`
   - `shouldFilterContactsByTagAndDate()`
   - `shouldSortContactsWithinTag()`