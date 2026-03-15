# MyContacts App

A **Java-based console application** that demonstrates **Object-Oriented Programming (OOP), design patterns, and core Java concepts** through a modular **contact management system** implemented using structured **use-case scenarios (UC1–UC12)**.

The project focuses on applying real-world software engineering principles such as **encapsulation, inheritance, polymorphism, abstraction, design patterns, and modern Java features** while building a functional contacts manager.

---

# Features

### User Management
- **User Registration (UC1)** – Create accounts with validated email and secure password hashing.
- **Authentication (UC2)** – Login using pluggable authentication strategies.
- **Profile Management (UC3)** – Update personal details, preferences, and passwords.

### Contact Management
- **Create Contacts (UC4)** – Add **Person** or **Organization** contacts with phones, emails, and metadata.
- **View Contacts (UC5)** – Display formatted contact information using decorators.
- **Edit Contacts (UC6)** – Modify contact data with **undo/redo support**.
- **Delete Contacts (UC7)** – Supports **soft delete and hard delete**.

### Organization & Bulk Operations
- **Bulk Operations (UC8)** – Perform batch actions like tagging or deletion on multiple contacts.
- **Tags Management (UC11)** – Create and manage tags such as *Family, Work, Friends*.
- **Apply Tags (UC12)** – Assign multiple tags to contacts.

### Search and Filtering
- **Search Contacts (UC9)** – Search by **name, phone, email, or tags**.
- **Advanced Filtering (UC10)** – Apply multiple filters and sorting strategies.

### Admin Features
- User oversight
- Global contact search

---

# Object-Oriented Design

### Core Entities
- **User**
  - `FreeUser`
  - `PremiumUser`
- **Contact**
  - `Person`
  - `Organization`
- **Tag**
- **PhoneNumber**
- **Email`

### Relationships
- **Composition:** Contact → PhoneNumber, Email  
- **Association:** Contact ↔ Tag (many-to-many)  
- **Aggregation:** User → Contacts  
- **Inheritance:** Contact and User hierarchies

---

# Design Patterns Used

### Creational
- **Factory** – Create User and Contact types
- **Builder** – Construct complex objects
- **Singleton** – SessionManager

### Structural
- **Decorator** – Display formatting (uppercase names, masked email)
- **Composite** – Bulk operations and filter combinations
- **Flyweight** – Shared tag instances

### Behavioral
- **Strategy** – Authentication and filtering algorithms
- **Observer** – Notify system components on updates
- **Command** – Edit operations with undo/redo
- **Memento** – State preservation for edits
- **Specification** – Flexible search queries
- **Chain of Responsibility** – Filter pipeline

---

# Java Concepts Demonstrated

### Core Java
- Encapsulation with getters/setters
- Inheritance and polymorphism
- Interfaces and abstraction
- Exception handling

### Collections & Functional Programming
- `List`, `Set`, `EnumSet`
- `Stream API`
- `Predicate`
- `Comparator`
- Lambda expressions

### Modern Java Features
- `Optional` for null safety
- `LocalDateTime` for timestamps
- `UUID` for unique identifiers
- `MessageDigest` for password hashing

### Advanced Concepts
- Deep vs shallow copying
- Defensive copying
- Immutable view objects
- `equals()` and `hashCode()` implementations
- Soft vs hard delete strategies
- Batch processing

---

# Use Case Coverage

| Use Case | Description |
|--------|-------------|
| UC1 | User Registration |
| UC2 | User Authentication |
| UC3 | Profile Management |
| UC4 | Create Contact |
| UC5 | View Contact |
| UC6 | Edit Contact (Undo/Redo) |
| UC7 | Delete Contact |
| UC8 | Bulk Operations |
| UC9 | Search Contacts |
| UC10 | Advanced Filtering |
| UC11 | Manage Tags |
| UC12 | Apply Tags |

---

# Key Learning Outcomes

- Applying **OOP principles in a real-world application**
- Implementing **multiple design patterns in Java**
- Using **Java Collections, Streams, and functional programming**
- Structuring applications using **modular use-case driven design**
- Managing complex relationships between domain entities

---

# Tech Stack

- **Language:** Java  
- **Interface:** Console-based CLI  
- **Paradigm:** Object-Oriented Programming  
- **Architecture:** Use-case modular design  

---

# Purpose

This project serves as a **learning-oriented implementation of advanced Java and software design principles**, showcasing how **object-oriented architecture and design patterns** can be applied to build a scalable contact management system.