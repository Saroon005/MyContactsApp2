package com.mycontacts.app;

import java.time.LocalDate;
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
import com.mycontacts.filter.service.FilterService;
import com.mycontacts.filter.service.FilterServiceImpl;
import com.mycontacts.group.repository.GroupRepository;
import com.mycontacts.group.repository.InMemoryGroupRepository;
import com.mycontacts.group.service.GroupService;
import com.mycontacts.group.service.GroupServiceImpl;
import com.mycontacts.profile.service.ProfileService;
import com.mycontacts.profile.service.ProfileServiceImpl;
import com.mycontacts.search.service.SearchService;
import com.mycontacts.search.service.SearchServiceImpl;
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
 * Use Case 10: Filter and Sort Contacts
 *
 * Description:
 * This version extends the MyContacts system by allowing
 * users to filter and sort their contacts.
 *
 * At this stage, the application:
 * - Allows filtering contacts by group
 * - Allows filtering contacts by creation date
 * - Allows sorting contacts by name
 * - Allows sorting contacts by creation date
 *
 * The implementation uses simple filtering
 * and sorting logic.
 *
 * @author Developer
 * @version 10.0
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
		GroupRepository groupRepository = new InMemoryGroupRepository();
		GroupService groupService = new GroupServiceImpl(groupRepository, contactService);
		SearchService searchService = new SearchServiceImpl(contactRepository);
		FilterService filterService = new FilterServiceImpl(contactRepository, groupRepository);

		try (Scanner scanner = new Scanner(System.in)) {
			System.out.println("=== MyContacts (UC-10: Filter and Sort Contacts) ===");
			while (true) {
				System.out.println();
				System.out.println("1 Register");
				System.out.println("2 Login");
				System.out.println("3 Manage Profile");
				System.out.println("4 Create Contact");
				System.out.println("5 View Contact");
				System.out.println("6 Edit Contact");
				System.out.println("7 Delete Contact");
				System.out.println("8 Create Group");
				System.out.println("9 Add Contact To Group");
				System.out.println("10 View Group Contacts");
				System.out.println("11 Bulk Delete Group Contacts");
				System.out.println("12 Search Contacts");
				System.out.println("13 Filter Contacts");
				System.out.println("14 Sort Contacts");
				System.out.println("15 Logout");
				System.out.println("16 Exit");
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
				case "6": {
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
					System.out.print("Enter group name: ");
					String groupName = scanner.nextLine();
					try {
						var group = groupService.createGroup(groupName);
						System.out.println("Group created successfully! Group ID: " + group.getId());
					} catch (ValidationException ex) {
						System.out.println("Group creation failed: " + ex.getMessage());
					}
					break;
				}
				case "9": {
					if (sessionManager.getCurrentUser().isEmpty()) {
						System.out.println("No user is logged in. Please login first.");
						break;
					}

					System.out.print("Enter group ID: ");
					String groupIdRaw = scanner.nextLine();
					UUID groupId;
					try {
						groupId = UUID.fromString(groupIdRaw.trim());
					} catch (IllegalArgumentException ex) {
						System.out.println("Invalid group ID format.");
						break;
					}

					System.out.print("Enter contact ID: ");
					String contactIdRaw = scanner.nextLine();
					UUID contactId;
					try {
						contactId = UUID.fromString(contactIdRaw.trim());
					} catch (IllegalArgumentException ex) {
						System.out.println("Invalid contact ID format.");
						break;
					}

					var contactOpt = contactService.getContactById(contactId);
					if (contactOpt.isEmpty()) {
						System.out.println("Contact not found.");
						break;
					}

					try {
						groupService.addContactToGroup(groupId, contactOpt.orElseThrow());
						System.out.println("Contact added to group");
					} catch (ValidationException ex) {
						System.out.println("Add to group failed: " + ex.getMessage());
					}
					break;
				}
				case "10": {
					if (sessionManager.getCurrentUser().isEmpty()) {
						System.out.println("No user is logged in. Please login first.");
						break;
					}

					System.out.print("Enter group ID: ");
					String groupIdRaw = scanner.nextLine();
					UUID groupId;
					try {
						groupId = UUID.fromString(groupIdRaw.trim());
					} catch (IllegalArgumentException ex) {
						System.out.println("Invalid group ID format.");
						break;
					}

					try {
						var members = groupService.getGroupContacts(groupId);
						System.out.println("Group members:");
						if (members.isEmpty()) {
							System.out.println("(none)");
							break;
						}
						for (var c : members) {
							System.out.println(
									"- " + c.getId() + " | " + c.getName() + (c.isDeleted() ? " (deleted)" : ""));
						}
					} catch (ValidationException ex) {
						System.out.println("View group failed: " + ex.getMessage());
					}
					break;
				}
				case "11": {
					if (sessionManager.getCurrentUser().isEmpty()) {
						System.out.println("No user is logged in. Please login first.");
						break;
					}

					System.out.print("Enter group ID: ");
					String groupIdRaw = scanner.nextLine();
					UUID groupId;
					try {
						groupId = UUID.fromString(groupIdRaw.trim());
					} catch (IllegalArgumentException ex) {
						System.out.println("Invalid group ID format.");
						break;
					}

					try {
						groupService.deleteAllContactsInGroup(groupId);
						System.out.println("Bulk delete executed (soft delete)");
					} catch (ValidationException ex) {
						System.out.println("Bulk delete failed: " + ex.getMessage());
					}
					break;
				}
				case "12": {
					if (sessionManager.getCurrentUser().isEmpty()) {
						System.out.println("No user is logged in. Please login first.");
						break;
					}

					System.out.println();
					System.out.println("Search type:");
					System.out.println("1 Search by Name");
					System.out.println("2 Search by Phone");
					System.out.println("3 Search by Email");
					System.out.print("Choose an option: ");
					String searchType = scanner.nextLine().trim();
					System.out.print("Enter search text: ");
					String query = scanner.nextLine();

					var results = switch (searchType) {
					case "1" -> searchService.searchByName(query);
					case "2" -> searchService.searchByPhone(query);
					case "3" -> searchService.searchByEmail(query);
					default -> null;
					};

					if (results == null) {
						System.out.println("Invalid option. Please choose 1, 2, or 3.");
						break;
					}
					if (results.isEmpty()) {
						System.out.println("No contacts found");
						break;
					}

					System.out.println("Matches:");
					for (var c : results) {
						System.out.println("- " + c.getId() + " | " + c.getName());
					}
					break;
				}
				case "13": {
					if (sessionManager.getCurrentUser().isEmpty()) {
						System.out.println("No user is logged in. Please login first.");
						break;
					}

					System.out.println();
					System.out.println("Filter Contacts:");
					System.out.println("1 Filter by Group");
					System.out.println("2 Filter by Creation Date");
					System.out.print("Choose an option: ");
					String filterType = scanner.nextLine().trim();

					var results = switch (filterType) {
					case "1" -> {
						System.out.print("Enter group ID: ");
						String groupIdRaw = scanner.nextLine();
						try {
							UUID groupId = UUID.fromString(groupIdRaw.trim());
							yield filterService.filterByGroup(groupId);
						} catch (IllegalArgumentException ex) {
							yield null;
						}
					}
					case "2" -> {
						System.out.print("Enter creation date (YYYY-MM-DD): ");
						String dateRaw = scanner.nextLine();
						try {
							LocalDate date = LocalDate.parse(dateRaw.trim());
							yield filterService.filterByDate(date);
						} catch (Exception ex) {
							yield null;
						}
					}
					default -> null;
					};

					if (results == null) {
						System.out.println("Invalid option or invalid input.");
						break;
					}
					if (results.isEmpty()) {
						System.out.println("No contacts found");
						break;
					}

					System.out.println("Results:");
					for (var c : results) {
						System.out.println("- " + c.getId() + " | " + c.getName() + " | " + c.getCreatedAt());
					}
					break;
				}
				case "14": {
					if (sessionManager.getCurrentUser().isEmpty()) {
						System.out.println("No user is logged in. Please login first.");
						break;
					}

					System.out.println();
					System.out.println("Sort Contacts:");
					System.out.println("1 Sort by Name");
					System.out.println("2 Sort by Creation Date");
					System.out.print("Choose an option: ");
					String sortType = scanner.nextLine().trim();

					var results = switch (sortType) {
					case "1" -> filterService.sortByName();
					case "2" -> filterService.sortByCreationDate();
					default -> null;
					};

					if (results == null) {
						System.out.println("Invalid option. Please choose 1 or 2.");
						break;
					}
					if (results.isEmpty()) {
						System.out.println("No contacts found");
						break;
					}

					System.out.println("Results:");
					for (var c : results) {
						System.out.println("- " + c.getId() + " | " + c.getName() + " | " + c.getCreatedAt());
					}
					break;
				}
				case "15": {
					sessionManager.logout();
					System.out.println("Logged out");
					break;
				}
				case "16":
					System.out.println("Goodbye!");
					return;
				default:
					System.out.println("Invalid option. Please choose 1-16.");
					break;
				}
			}
		}
	}
}
