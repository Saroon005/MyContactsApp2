package com.mycontacts.app;

import java.util.Scanner;

import com.mycontacts.auth.strategy.AuthenticationStrategy;
import com.mycontacts.auth.strategy.BasicAuthenticationStrategy;
import com.mycontacts.common.exception.AuthenticationException;
import com.mycontacts.common.exception.ValidationException;
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
 * Use Case 3: User Profile Management
 *
 * Description:
 * This version extends the MyContacts system by allowing
 * authenticated users to manage their profile.
 *
 * At this stage, the application:
 * - Allows logged-in users to update their email
 * - Allows users to change passwords securely
 * - Applies profile updates through command objects
 * - Maintains proper validation and session awareness
 *
 * This introduces structured profile operations.
 *
 * @author Developer
 * @version 3.0
 */
public class MyContactsApplication {
	public static void main(String[] args) {
		UserRepository userRepository = new InMemoryUserRepository();
		UserFactory userFactory = new UserFactory.Default();
		UserService userService = new UserServiceImpl(userRepository, userFactory);
		AuthenticationStrategy authenticationStrategy = new BasicAuthenticationStrategy(userRepository);
		SessionManager sessionManager = SessionManager.getInstance();
		ProfileService profileService = new ProfileServiceImpl(userRepository);

		try (Scanner scanner = new Scanner(System.in)) {
			System.out.println("=== MyContacts (UC-03: User Profile Management) ===");
			while (true) {
				System.out.println();
				System.out.println("1 Register");
				System.out.println("2 Login");
				System.out.println("3 Manage Profile");
				System.out.println("4 Logout");
				System.out.println("5 Exit");
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
					sessionManager.logout();
					System.out.println("Logged out");
					break;
				case "5":
					System.out.println("Goodbye!");
					return;
				default:
					System.out.println("Invalid option. Please choose 1, 2, 3, 4, or 5.");
					break;
				}
			}
		}
	}
}


