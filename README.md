# MyContacts — UC-07 Delete Contact

This repository contains **Use Case 7 of the MyContacts system**.
It builds on:

- UC-01 User Registration
- UC-02 Authentication
- UC-03 Profile Management
- UC-04 Create Contact
- UC-05 View Contact
- UC-06 Edit Contact
- UC-07 Delete Contact

## Description

- **Use Case:** UC-07 Delete Contact
- **Actor:** Authenticated user
- **Goal:** Remove contacts from the contact list via soft delete or hard delete

## Delete Contact Flow

1. User logs in
2. User selects **Delete Contact**
3. User enters the contact ID
4. User selects delete type:
   - Soft Delete (marks contact as deleted)
   - Hard Delete (removes contact from repository)
5. System confirms deletion

## Soft Delete vs Hard Delete

- **Soft Delete:** sets `Contact.deleted = true` and keeps the contact stored
- **Hard Delete:** removes the contact from the repository map

## Java Concepts Used

- `UUID` parsing with `UUID.fromString`
- `Optional` for repository lookups
- `Map` storage and simple filtering for active contacts

## Testing Summary (JUnit 5)

- `test/com/mycontacts/contact/DeleteContactTest.java`
  - `shouldSoftDeleteContact()`
  - `shouldHardDeleteContact()`
  - `shouldNotReturnSoftDeletedContacts()`