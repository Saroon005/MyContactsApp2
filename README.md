# MyContacts — UC-01 User Registration

This repository contains **Use Case 1 of the MyContacts system**: registering a new user in a modular Java console application.

## Use Case Description

- **Use Case:** UC-01 User Registration
- **Actor:** End user
- **Goal:** Register a `FREE` or `PREMIUM` user using email + password

## Flow

1. User selects account type (`FREE` or `PREMIUM`)
2. User enters email
3. User enters password (minimum 6 characters)
4. System validates input and blocks duplicate emails
5. System hashes the password (SHA-256)
6. System creates a `User` via Factory Pattern
7. System stores the user in an in-memory repository
8. System prints success message with generated user ID

## OOP Concepts Used

- **Abstraction:** `User` is an abstract base class
- **Inheritance:** `FreeUser` and `PremiumUser` extend `User`
- **Encapsulation:** private fields + getters
- **Polymorphism:** service/repository interact via interfaces

## Design Patterns Used

- **Factory Pattern:** `UserFactory` creates `FREE`/`PREMIUM` users
- **Dependency Injection (constructor):** `UserServiceImpl` receives `UserRepository` + `UserFactory`

## Java Concepts Used

- `UUID` for identifiers
- `LocalDateTime` timestamps
- `Optional` to avoid nulls in repository/service lookups
- `MessageDigest` (SHA-256) for password hashing

## How to Run the Application

### Option A — VS Code / Eclipse

Run the main class: `com.mycontacts.app.MyContactsApplication`.

### Option B — PowerShell (Java 21)

From the repository root:

```powershell
Remove-Item -Recurse -Force bin -ErrorAction SilentlyContinue
New-Item -ItemType Directory -Force bin | Out-Null
javac -d bin (Get-ChildItem -Recurse src -Filter *.java).FullName
java -cp bin com.mycontacts.app.MyContactsApplication
```

## Test Coverage (JUnit 5)

JUnit 5 tests are in `test/com/mycontacts/user/UserServiceTest.java`:

- `shouldRegisterFreeUser()`
- `shouldRegisterPremiumUser()`
- `shouldNotAllowDuplicateEmail()`

Run tests via the VS Code Testing panel (Java Test Runner) or Eclipse JUnit.