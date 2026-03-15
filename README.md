# MyContacts — UC-03 User Profile Management

This repository contains **Use Case 3 of the MyContacts system**.
It builds on:

- **UC-01:** User Registration
- **UC-02:** Authentication + Session
- **UC-03:** Profile Management (this version)

## Description

- **Use Case:** UC-03 User Profile Management
- **Actor:** Authenticated user
- **Goal:** Update email and change password securely

## Profile Management Flow

1. User registers and/or logs in
2. User chooses **Manage Profile** from the main menu
3. System checks session (must be logged in)
4. User selects:
	 - **Update Email** (validated by regex)
	 - **Change Password** (verifies old password, hashes new password)
5. Operations are executed via command objects

## OOP Concepts Used

- **Encapsulation:** profile updates happen through service + commands
- **Abstraction:** `ProfileService` hides command orchestration
- **Polymorphism:** commands share a common `ProfileCommand` interface

## Design Patterns Used

- **Command Pattern:** `UpdateEmailCommand`, `ChangePasswordCommand`
- **Strategy Pattern (from UC-02):** authentication via `AuthenticationStrategy`
- **Singleton Pattern (from UC-02):** `SessionManager`

## Java Concepts Used

- `Optional` for session/user lookups
- `MessageDigest` (SHA-256) for password hashing/verification
- `UUID` + `LocalDateTime` on the user model

## Testing Summary (JUnit 5)

- Profile: `test/com/mycontacts/profile/ProfileServiceTest.java`
	- `shouldUpdateEmailSuccessfully()`
	- `shouldRejectInvalidEmail()`
	- `shouldChangePasswordSuccessfully()`
	- `shouldFailIfOldPasswordIncorrect()`

Run tests using VS Code Testing (Java Test Runner) or Eclipse JUnit.