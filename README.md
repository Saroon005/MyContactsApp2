# MyContacts — UC-10 Filter and Sort Contacts

This repository contains **Use Case 10 of the MyContacts system**.
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

## Description

- **Use Case:** UC-10 Filter and Sort Contacts
- **Actor:** Authenticated user
- **Goal:** Filter contacts by group/date and sort contacts by name/creation date

## Filtering Flow

1. User logs in
2. User selects **Filter Contacts**
3. User chooses filter type:
  - Filter by Group
  - Filter by Creation Date
4. User enters the group ID or date
5. System prints matching contacts (or `No contacts found`)

## Sorting Flow

1. User logs in
2. User selects **Sort Contacts**
3. User chooses sort type:
  - Sort by Name
  - Sort by Creation Date
4. System prints the sorted contact list (or `No contacts found`)

## Java Concepts Used

- `UUID` parsing for group IDs
- `LocalDate` parsing for date filters
- `List` + loops for filtering
- Sorting via `Comparator` and `List.sort()`

## Testing Summary (JUnit 5)

- `test/com/mycontacts/filter/FilterServiceTest.java`
  - `shouldFilterContactsByGroup()`
  - `shouldFilterContactsByDate()`
  - `shouldSortContactsByName()`
  - `shouldSortContactsByCreationDate()`