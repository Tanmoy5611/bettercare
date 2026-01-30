package org.bettercare.presentation.controller;

import jakarta.servlet.http.HttpSession;
import org.bettercare.business.entities.UserAccount;
import org.bettercare.business.services.UserSettingsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class EmailAlertController {

    private final UserSettingsService userSettingsService;

    public EmailAlertController(UserSettingsService userSettingsService) {
        this.userSettingsService = userSettingsService;
    }

    @PostMapping("/update-alert-settings")
    public String updateAlerts(@RequestParam(defaultValue = "false") boolean receiveAlerts,
                               HttpSession session) {

        UserAccount user = (UserAccount) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        try {
            // update DB
            userSettingsService.updateReceiveEmailAlerts(user.getUserId(), receiveAlerts);

            // Update session object
                user.setReceiveEmailAlerts(receiveAlerts);
                session.setAttribute("user", user);

        } catch (Exception e) {
            System.out.println("Email settings update failed: " + e.getMessage());
        }

        return "redirect:/notifications";
    }
}