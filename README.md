# MyContacts — UC-02 User Authentication

This repository contains **Use Case 2 of the MyContacts system**.
It **builds on UC-01 (User Registration)** and introduces secure login using authentication strategies and a logged-in session.

## Description

- **Use Case:** UC-02 User Authentication
- **Actor:** Registered user
- **Goal:** Log in using email + password

## Authentication Flow

1. User chooses **Login** from the console menu
2. User enters email
3. User enters password
4. System finds the user by email (repository)
5. System verifies password hash (SHA-256)
6. If valid: user is stored in a singleton session and **"Login successful"** is printed
7. If invalid: an `AuthenticationException` is raised and an error message is shown

## OOP Concepts Used

- **Abstraction:** authentication via `AuthenticationStrategy`
- **Encapsulation:** `SessionManager` controls current user state
- **Polymorphism:** application interacts with auth via interface type

## Design Patterns Used

- **Strategy Pattern:** `AuthenticationStrategy` (currently `BasicAuthenticationStrategy`)
- **Singleton Pattern:** `SessionManager`
- **Factory Pattern (from UC-01):** `UserFactory` creates user subtypes

## Java Concepts Used

- `Optional` to represent a successful authentication result
- `MessageDigest` (SHA-256) for password verification
- `UUID` + `LocalDateTime` on the user model (from UC-01)

## Testing Summary (JUnit 5)

- Authentication: `test/com/mycontacts/auth/AuthenticationStrategyTest.java`
	- `shouldLoginWithValidCredentials()`
	- `shouldFailWithWrongPassword()`
	- `shouldFailWithUnknownEmail()`
- Session: `test/com/mycontacts/session/SessionManagerTest.java`
	- `shouldStoreLoggedInUser()`
	- `shouldLogoutSuccessfully()`

Run tests using VS Code Testing (Java Test Runner) or Eclipse JUnit.