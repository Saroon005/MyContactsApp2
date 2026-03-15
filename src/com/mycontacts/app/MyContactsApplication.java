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
 * Use Case 7: Delete Contact
 *
 * Description:
 * This version extends the MyContacts system by allowing
 * users to remove contacts from their contact list.
 *
 * At this stage, the application:
 * - Allows deletion of contacts
 * - Supports soft delete and hard delete options
 * - Updates the contact repository accordingly
 *
 * The implementation is intentionally simple.
 *
 * @author Developer
 * @version 7.0
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
			System.out.println("=== MyContacts (UC-07: Delete Contact) ===");
			while (true) {
				System.out.println();
				System.out.println("1 Register");
				System.out.println("2 Login");
				System.out.println("3 Manage Profile");
				System.out.println("4 Create Contact");
				System.out.println("5 View Contact");
				System.out.println("6 Edit Contact");
				System.out.println("7 Delete Contact");
				System.out.println("8 Undo Last Edit");
				System.out.println("9 Redo Last Edit");
				System.out.println("10 Logout");
				System.out.println("11 Exit");
				System.out.print("Choose an option: ");

				if (!scanner.hasNextLine()) {
					return;
				}
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
				{
					if (sessionManager.getCurrentUser().isEmpty()) {
						System.out.println("No user is logged in. Please login first.");
						break;
					}

					System.out.print("Enter contact ID: ");
					String editIdRaw = scanner.nextLine();
					UUID editId;
					try {
						editId = UUID.fromString(editIdRaw.trim());
					} catch (IllegalArgumentException ex) {
						System.out.println("Invalid contact ID format.");
						break;
					}

					System.out.println("Choose field to edit:");
					System.out.println("1 Name");
					System.out.println("2 Phone");
					System.out.println("3 Email");
					System.out.print("Option: ");
					String fieldChoice = scanner.nextLine().trim();
					try {
						switch (fieldChoice) {
						case "1":
							System.out.print("Enter new name: ");
							String newName = scanner.nextLine();
							contactService.updateContactName(editId, newName);
							System.out.println("Contact name updated");
							break;
						case "2":
							System.out.print("Enter phone label (e.g., Mobile): ");
							String phoneLabel = scanner.nextLine();
							System.out.print("Enter phone number: ");
							String phoneNumber = scanner.nextLine();
							contactService.updatePhone(editId, new PhoneNumber(phoneNumber, phoneLabel));
							System.out.println("Contact phone updated");
							break;
						case "3":
							System.out.print("Enter email label (e.g., Primary): ");
							String emailLabel = scanner.nextLine();
							System.out.print("Enter email address: ");
							String emailAddress = scanner.nextLine();
							contactService.updateEmail(editId, new Email(emailAddress, emailLabel));
							System.out.println("Contact email updated");
							break;
						default:
							System.out.println("Invalid option. Please choose 1, 2, or 3.");
							break;
						}
					} catch (ValidationException ex) {
						System.out.println("Edit failed: " + ex.getMessage());
					}
					break;
				}
				case "7": {
					if (sessionManager.getCurrentUser().isEmpty()) {
						System.out.println("No user is logged in. Please login first.");
						break;
					}

					System.out.print("Enter contact ID: ");
					String deleteIdRaw = scanner.nextLine();
					UUID deleteId;
					try {
						deleteId = UUID.fromString(deleteIdRaw.trim());
					} catch (IllegalArgumentException ex) {
						System.out.println("Invalid contact ID format.");
						break;
					}

					System.out.println("Choose delete type:");
					System.out.println("1 Soft Delete");
					System.out.println("2 Hard Delete");
					System.out.print("Option: ");
					String deleteType = scanner.nextLine().trim();
					try {
						switch (deleteType) {
						case "1":
							contactService.deleteContact(deleteId);
							System.out.println("Contact soft-deleted");
							break;
						case "2":
							contactService.hardDeleteContact(deleteId);
							System.out.println("Contact hard-deleted");
							break;
						default:
							System.out.println("Invalid option. Please choose 1 or 2.");
							break;
						}
					} catch (ValidationException ex) {
						System.out.println("Delete failed: " + ex.getMessage());
					}
					break;
				}
				case "8": {
					if (sessionManager.getCurrentUser().isEmpty()) {
						System.out.println("No user is logged in. Please login first.");
						break;
					}
					contactService.undoLastEdit();
					System.out.println("Undo executed");
					break;
				}
				case "9": {
					if (sessionManager.getCurrentUser().isEmpty()) {
						System.out.println("No user is logged in. Please login first.");
						break;
					}
					contactService.redoLastEdit();
					System.out.println("Redo executed");
					break;
				}
				case "10": {
					sessionManager.logout();
					System.out.println("Logged out");
					break;
				}
				case "11":
					System.out.println("Goodbye!");
					return;
				default:
					System.out.println("Invalid option. Please choose 1-11.");
					break;
				}
			}
		}
	}
}



