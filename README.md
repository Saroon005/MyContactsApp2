# MyContacts — UC-06 Edit Contact with Undo/Redo

This repository contains **Use Case 6 of the MyContacts system**.
It builds on:

- **UC-01:** User Registration
- **UC-02:** Authentication
- **UC-03:** Profile Management
- **UC-04:** Contact Creation
- **UC-05:** View Contact Details
- **UC-06:** Edit Contact (this version)

## Description

- **Use Case:** UC-06 Edit Contact with Undo/Redo
- **Actor:** Authenticated user
- **Goal:** Edit a contact’s name/phone/email and undo/redo edits

## Edit Contact Flow

1. User logs in
2. User chooses **Edit Contact**
3. User enters contact ID
4. User chooses a field to edit: name / phone / email
5. The system executes an edit command via an undo/redo manager

## Undo/Redo Flow

- **Undo Last Edit** reverts the last command
- **Redo Last Edit** re-applies the most recently undone command

## OOP Concepts Used

- **Encapsulation:** edit operations are isolated in command objects
- **Abstraction:** commands share a common interface
- **Composition:** contacts contain `PhoneNumber` and `Email`

## Design Patterns Used

- **Command Pattern:** edit operations are represented as commands
- **Memento Pattern:** `ContactMemento` stores previous contact state

## Java Concepts Used

- `Deque` stacks for undo/redo
- `UUID` parsing for contact lookup
- Defensive copies for lists

## Testing Summary (JUnit 5)

- Edits: `test/com/mycontacts/contact/edit/EditContactCommandTest.java`
  - `shouldUpdateContactName()`
  - `shouldUndoNameChange()`
  - `shouldRedoNameChange()`
  - `shouldUpdatePhone()`
  - `shouldRestorePreviousState()`

Run tests using VS Code Testing (Java Test Runner) or Eclipse JUnit.