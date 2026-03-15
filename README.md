# MyContacts — UC-04 Create Contact

This repository contains **Use Case 4 of the MyContacts system**.
It builds on:

- **UC-01:** User Registration
- **UC-02:** Authentication + Session
- **UC-03:** Profile Management
- **UC-04:** Contact Creation (this version)

## Description

- **Use Case:** UC-04 Create Contact
- **Actor:** Authenticated user
- **Goal:** Create and store `PERSON` or `ORGANIZATION` contacts

## Contact Creation Flow

1. User logs in
2. User chooses **Create Contact** from the main menu
3. System checks session (must be logged in)
4. User enters contact type (`PERSON` / `ORGANIZATION`)
5. User enters name
6. User optionally enters a phone number and an email
7. System constructs the contact using a builder
8. System stores the contact in an in-memory repository
9. System prints confirmation with the generated contact ID

## OOP Concepts Used

- **Abstraction:** `Contact` base class + repository/service interfaces
- **Inheritance:** `PersonContact` / `OrganizationContact`
- **Composition:** `Contact` contains `PhoneNumber` and `Email`
- **Encapsulation:** private fields + getters + defensive copying

## Design Patterns Used

- **Builder Pattern:** `ContactBuilder`
- **Command Pattern (from UC-03):** profile operations
- **Strategy Pattern (from UC-02):** authentication
- **Singleton Pattern (from UC-02):** session management

## Java Concepts Used

- `UUID` (IDs), `LocalDateTime` (timestamps)
- `List.copyOf(...)` + defensive copies for immutability
- `Optional` for session checks and repository lookups

## Testing Summary (JUnit 5)

- Contacts: `test/com/mycontacts/contact/ContactServiceTest.java`
	- `shouldCreatePersonContact()`
	- `shouldCreateOrganizationContact()`
	- `shouldStoreContactInRepository()`
	- `shouldReturnAllContacts()`

Run tests using VS Code Testing (Java Test Runner) or Eclipse JUnit.