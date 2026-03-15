package com.mycontacts.app;

import java.util.Scanner;

import com.mycontacts.common.exception.ValidationException;
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
 * Use Case 1: User Registration
 *
 * Description:
 * This application demonstrates the first use case
 * of the MyContacts system where a new user can
 * register using email and password.
 *
 * At this stage, the application:
 * - Validates user input
 * - Hashes passwords securely
 * - Creates user objects via Factory Pattern
 * - Stores users in an in-memory repository
 *
 * This establishes the foundation of the system.
 *
 * @author Developer
 * @version 1.0
 */
public class MyContactsApplication {
	public static void main(String[] args) {
		UserRepository userRepository = new InMemoryUserRepository();
		UserFactory userFactory = new UserFactory.Default();
		UserService userService = new UserServiceImpl(userRepository, userFactory);

		try (Scanner scanner = new Scanner(System.in)) {
			System.out.println("=== MyContacts (UC-01: User Registration) ===");
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
	}
}
