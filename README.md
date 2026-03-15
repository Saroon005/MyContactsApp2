# MyContacts — UC-08 Contact Groups and Bulk Operations

This repository contains **Use Case 8 of the MyContacts system**.
It builds on:

- UC-01 User Registration
- UC-02 Authentication
- UC-03 Profile Management
- UC-04 Create Contact
- UC-05 View Contact
- UC-06 Edit Contact
- UC-07 Delete Contact
- UC-08 Contact Groups and Bulk Operations

## Description

- **Use Case:** UC-08 Contact Groups and Bulk Operations
- **Actor:** Authenticated user
- **Goal:** Organize contacts into groups and support bulk delete actions

## Features Added (UC-08)

- Create a contact group
- Add contacts to a group
- View group contacts
- Bulk delete all contacts in a group (soft delete)

## Console Menu (UC-08)

1 Register
2 Login
3 Manage Profile
4 Create Contact
5 View Contact
6 Edit Contact
7 Delete Contact
8 Create Group
9 Add Contact To Group
10 View Group Contacts
11 Bulk Delete Group Contacts
12 Logout
13 Exit

## Implementation Notes

- Group module: `src/com/mycontacts/group/`
   - `group.model.ContactGroup` stores `UUID id`, `String name`, and a `List<Contact>`.
   - `group.repository.InMemoryGroupRepository` stores groups in a `Map<UUID, ContactGroup>`.
   - `group.service.GroupServiceImpl` provides create/add/list/bulk-delete operations.
- Bulk delete uses existing UC-07 behavior by calling `ContactService.deleteContact(id)` for each contact in the group.

## Testing Summary (JUnit 5)

- `test/com/mycontacts/group/GroupServiceTest.java`
   - `shouldCreateGroup()`
   - `shouldAddContactToGroup()`
   - `shouldReturnGroupContacts()`
   - `shouldDeleteAllContactsInGroup()`