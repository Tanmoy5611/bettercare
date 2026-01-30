package org.bettercare.business.services;

import org.springframework.stereotype.Service;

@Service
public class UserSettingsService {

    // Repository used to update user settings in the database
    private final UserAccountService userAccountService;

    // Constructor injection for Spring
    public UserSettingsService(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    // Updates the email alert preference for a specific user
    public void updateReceiveEmailAlerts(int userId, boolean receiveAlerts) {
        userAccountService.updateReceiveEmailAlerts(userId, receiveAlerts);
    }
}