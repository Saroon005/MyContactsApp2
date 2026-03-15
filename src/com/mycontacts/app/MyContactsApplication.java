package com.mycontacts.app;

import java.util.Scanner;
import java.util.UUID;

import com.mycontacts.auth.strategy.AuthenticationStrategy;
import com.mycontacts.auth.strategy.BasicAuthenticationStrategy;
import com.mycontacts.common.exception.AuthenticationException;
import com.mycontacts.common.exception.ValidationException;
import com.mycontacts.contact.display.BaseContactView;
import com.mycontacts.contact.display.ContactView;
import com.mycontacts.contact.display.MaskedEmailDecorator;
import com.mycontacts.contact.display.UpperCaseNameDecorator;
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
 * Use Case 5: View Contact Details
 *
 * Description:
 * This version extends the MyContacts system by allowing
 * users to view stored contacts in a formatted manner.
 *
 * At this stage, the application:
 * - Displays full contact information
 * - Supports formatted views using decorators
 * - Allows masking of sensitive information
 * - Provides flexible display extensions
 *
 * This introduces the Decorator Pattern.
 *
 * @author Developer
 * @version 5.0
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
			System.out.println("=== MyContacts (UC-05: View Contact Details) ===");
			while (true) {
				System.out.println();
				System.out.println("1 Register");
				System.out.println("2 Login");
				System.out.println("3 Manage Profile");
				System.out.println("4 Create Contact");
				System.out.println("5 View Contact Details");
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

					System.out.print("Enter contact ID: ");
					String idRaw = scanner.nextLine();
					try {
						UUID id = UUID.fromString(idRaw.trim());
						var contactOpt = contactService.getContactById(id);
						if (contactOpt.isEmpty()) {
							System.out.println("Contact not found.");
							break;
						}

						System.out.println("Choose display option:");
						System.out.println("1 Normal View");
						System.out.println("2 Uppercase Name");
						System.out.println("3 Mask Email");
						System.out.print("Option: ");
						String viewChoice = scanner.nextLine().trim();

						ContactView view = new BaseContactView();
						switch (viewChoice) {
						case "1":
							break;
						case "2":
							view = new UpperCaseNameDecorator(view);
							break;
						case "3":
							view = new MaskedEmailDecorator(view);
							break;
						default:
							System.out.println("Invalid option. Showing normal view.");
							break;
						}

						System.out.println();
						System.out.println(view.display(contactOpt.orElseThrow()));
					} catch (IllegalArgumentException ex) {
						System.out.println("Invalid contact ID format.");
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



