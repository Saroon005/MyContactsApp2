package com.mycontacts.app;

import java.util.Scanner;

import com.mycontacts.auth.strategy.AuthenticationStrategy;
import com.mycontacts.auth.strategy.BasicAuthenticationStrategy;
import com.mycontacts.common.exception.AuthenticationException;
import com.mycontacts.common.exception.ValidationException;
import com.mycontacts.contact.model.Email;
import com.mycontacts.contact.model.PhoneNumber;
import com.mycontacts.contact.repository.ContactRepository;
import com.mycontacts.contact.repository.InMemoryContactRepository;
import com.mycontacts.contact.service.ContactService;
import com.mycontacts.contact.service.ContactServiceImpl;
import com.mycontacts.profile.service.ProfileService;
import com.mycontacts.profile.service.ProfileServiceImpl;
import com.mycontacts.session.SessionManager;
import com.mycontacts.user.factory.UserFactory;
import com.mycontacts.user.repository.InMemoryUserRepository;
import com.mycontacts.user.repository.UserRepository;
import com.mycontacts.user.service.UserService;
import com.mycontacts.user.service.UserServiceImpl;

/**
 * ======================================================
 * MAIN CLASS - MyContactsApplication
 * ======================================================
 *
 * Use Case 4: Create Contact
 *
 * Description:
 * This version extends the MyContacts system by allowing
 * authenticated users to create and store contacts.
 *
 * At this stage, the application:
 * - Supports creating person and organization contacts
 * - Uses composition for phone numbers and emails
 * - Uses a builder to construct contacts
 * - Stores contacts in a repository
 *
 * This introduces the core contact management domain.
 *
 * @author Developer
 * @version 4.0
 */
public class MyContactsApplication {
	public static void main(String[] args) {
		UserRepository userRepository = new InMemoryUserRepository();
		ContactRepository contactRepository = new InMemoryContactRepository();
		UserFactory userFactory = new UserFactory.Default();
		UserService userService = new UserServiceImpl(userRepository, userFactory);
		AuthenticationStrategy authenticationStrategy = new BasicAuthenticationStrategy(userRepository);
		SessionManager sessionManager = SessionManager.getInstance();
		ProfileService profileService = new ProfileServiceImpl(userRepository);
		ContactService contactService = new ContactServiceImpl(contactRepository);

		try (Scanner scanner = new Scanner(System.in)) {
			System.out.println("=== MyContacts (UC-04: Create Contact) ===");
			while (true) {
				System.out.println();
				System.out.println("1 Register");
				System.out.println("2 Login");
				System.out.println("3 Manage Profile");
				System.out.println("4 Create Contact");
				System.out.println("5 View Contacts");
				System.out.println("6 Logout");
				System.out.println("7 Exit");
				System.out.print("Choose an option: ");

				String choice = scanner.nextLine().trim();
				switch (choice) {
				case "1":
					try {
						System.out.print("Enter user type (FREE or PREMIUM): ");
						String type = scanner.nextLine();

						System.out.print("Enter email: ");
						String email = scanner.nextLine();

						System.out.print("Enter password (min 6 chars): ");
						String password = scanner.nextLine();

						var user = userService.register(type, email, password);
						System.out.println("Registration successful! User ID: " + user.getId());
					} catch (ValidationException ex) {
						System.out.println("Registration failed: " + ex.getMessage());
					}
					break;
				case "2":
					try {
						System.out.print("Enter email: ");
						String email = scanner.nextLine();

						System.out.print("Enter password: ");
						String password = scanner.nextLine();

						var user = authenticationStrategy.authenticate(email, password)
								.orElseThrow(() -> new AuthenticationException("Invalid credentials"));
						sessionManager.login(user);
						System.out.println("Login successful");
					} catch (AuthenticationException ex) {
						System.out.println("Login failed: " + ex.getMessage());
					}
					break;
				case "3":
					var currentUserOpt = sessionManager.getCurrentUser();
					if (currentUserOpt.isEmpty()) {
						System.out.println("No user is logged in. Please login first.");
						break;
					}

					System.out.println();
					System.out.println("=== Manage Profile ===");
					System.out.println("1 Update Email");
					System.out.println("2 Change Password");
					System.out.print("Choose an option: ");
					String profileChoice = scanner.nextLine().trim();
					try {
						var currentUser = currentUserOpt.orElseThrow();
						switch (profileChoice) {
						case "1":
							System.out.print("Enter new email: ");
							String newEmail = scanner.nextLine();
							profileService.updateEmail(currentUser, newEmail);
							System.out.println("Email updated successfully");
							break;
						case "2":
							System.out.print("Enter old password: ");
							String oldPassword = scanner.nextLine();
							System.out.print("Enter new password (min 6 chars): ");
							String newPassword = scanner.nextLine();
							profileService.changePassword(currentUser, oldPassword, newPassword);
							System.out.println("Password changed successfully");
							break;
						default:
							System.out.println("Invalid option. Please choose 1 or 2.");
							break;
						}
					} catch (ValidationException ex) {
						System.out.println("Profile update failed: " + ex.getMessage());
					}
					break;
				case "4":
					if (sessionManager.getCurrentUser().isEmpty()) {
						System.out.println("No user is logged in. Please login first.");
						break;
					}

					try {
						System.out.print("Enter contact type (PERSON or ORGANIZATION): ");
						String contactType = scanner.nextLine().trim().toUpperCase();
						System.out.print("Enter contact name: ");
						String name = scanner.nextLine();
						System.out.print("Enter phone number (optional): ");
						String phoneRaw = scanner.nextLine();
						System.out.print("Enter email (optional): ");
						String emailRaw = scanner.nextLine();

						PhoneNumber phone = (phoneRaw == null || phoneRaw.trim().isEmpty()) ? null
								: new PhoneNumber(phoneRaw, "Mobile");
						Email email = (emailRaw == null || emailRaw.trim().isEmpty()) ? null
								: new Email(emailRaw, "Primary");

						var contact = switch (contactType) {
						case "PERSON" -> contactService.createPersonContact(name, phone, email);
						case "ORGANIZATION" -> contactService.createOrganizationContact(name, phone, email);
						default -> throw new ValidationException("Unsupported contact type: " + contactType);
						};

						System.out.println("Contact created successfully! Contact ID: " + contact.getId());
					} catch (ValidationException ex) {
						System.out.println("Contact creation failed: " + ex.getMessage());
					}
					break;
				case "5":
					if (sessionManager.getCurrentUser().isEmpty()) {
						System.out.println("No user is logged in. Please login first.");
						break;
					}

					var contacts = contactService.getAllContacts();
					if (contacts.isEmpty()) {
						System.out.println("No contacts found.");
						break;
					}
					System.out.println("=== Contacts ===");
					for (var c : contacts) {
						System.out.println("ID: " + c.getId());
						System.out.println("Name: " + c.getName());
						if (!c.getPhoneNumbers().isEmpty()) {
							System.out.println("Phones:");
							for (var p : c.getPhoneNumbers()) {
								System.out.println("- " + p.getLabel() + ": " + p.getNumber());
							}
						}
						if (!c.getEmails().isEmpty()) {
							System.out.println("Emails:");
							for (var e : c.getEmails()) {
								System.out.println("- " + e.getLabel() + ": " + e.getAddress());
							}
						}
						System.out.println();
					}
					break;
				case "6":
					sessionManager.logout();
					System.out.println("Logged out");
					break;
				case "7":
					System.out.println("Goodbye!");
					return;
				default:
					System.out.println("Invalid option. Please choose 1, 2, 3, 4, 5, 6, or 7.");
					break;
				}
			}
		}
	}
}



