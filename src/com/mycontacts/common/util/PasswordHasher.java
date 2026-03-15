package com.mycontacts.common.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class PasswordHasher {
	private PasswordHasher() {
		// utility
	}

	public static String hashPassword(String password) {
		if (password == null) {
			throw new IllegalArgumentException("Password cannot be null");
		}
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
			return toHex(hash);
		} catch (NoSuchAlgorithmException e) {
			// SHA-256 is guaranteed in the JDK
			throw new IllegalStateException("SHA-256 algorithm not available", e);
		}
	}

	public static boolean verifyPassword(String rawPassword, String storedHash) {
		if (rawPassword == null || storedHash == null) {
			return false;
		}
		String computed = hashPassword(rawPassword);
		return constantTimeEquals(computed, storedHash);
	}

	private static String toHex(byte[] bytes) {
		StringBuilder sb = new StringBuilder(bytes.length * 2);
		for (byte b : bytes) {
			sb.append(Character.forDigit((b >>> 4) & 0x0F, 16));
			sb.append(Character.forDigit(b & 0x0F, 16));
		}
		return sb.toString();
	}

	private static boolean constantTimeEquals(String a, String b) {
		if (a.length() != b.length()) {
			return false;
		}
		int result = 0;
		for (int i = 0; i < a.length(); i++) {
			result |= a.charAt(i) ^ b.charAt(i);
		}
		return result == 0;
	}
}
