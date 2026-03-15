package com.mycontacts.app;

import java.util.Scanner;

import com.mycontacts.auth.strategy.AuthenticationStrategy;
import com.mycontacts.auth.strategy.BasicAuthenticationStrategy;
import com.mycontacts.common.exception.AuthenticationException;
import com.mycontacts.common.exception.ValidationException;
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
 * Use Case 2: User Authentication
 *
 * Description:
 * This version extends the MyContacts application by
 * introducing user authentication.
 *
 * At this stage, the application:
 * - Allows registered users to log in
 * - Verifies credentials securely
 * - Uses authentication strategies
 * - Maintains a logged-in user session
 *
 * This establishes secure access to the system.
 *
 * @author Developer
 * @version 2.0
 */
public class MyContactsApplication {
	public static void main(String[] args) {
		UserRepository userRepository = new InMemoryUserRepository();
		UserFactory userFactory = new UserFactory.Default();
		UserService userService = new UserServiceImpl(userRepository, userFactory);
		AuthenticationStrategy authenticationStrategy = new BasicAuthenticationStrategy(userRepository);
		SessionManager sessionManager = SessionManager.getInstance();

		try (Scanner scanner = new Scanner(System.in)) {
			System.out.println("=== MyContacts (UC-02: User Authentication) ===");
			while (true) {
				System.out.println();
				System.out.println("1 Register");
				System.out.println("2 Login");
				System.out.println("3 Exit");
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
					System.out.println("Goodbye!");
					return;
				default:
					System.out.println("Invalid option. Please choose 1, 2, or 3.");
					break;
				}
			}
		}
	}
}

