package com.mycontacts.profile.service;

import com.mycontacts.user.model.User;

public interface ProfileService {
	void updateEmail(User user, String newEmail);

	void changePassword(User user, String oldPassword, String newPassword);
}
