package org.bettercare.presentation.controller;

import jakarta.servlet.http.HttpSession;
import org.bettercare.business.entities.UserAccount;
import org.bettercare.business.entities.UserProfile;
import org.bettercare.business.services.UserAccountService;
import org.bettercare.business.services.UserProfileService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserAccountController {

    private final UserProfileService userProfileService;
    private final UserAccountService userAccountService;

    public UserAccountController(UserProfileService userProfileService, UserAccountService userAccountService) {
        this.userProfileService = userProfileService;
        this.userAccountService = userAccountService;
    }

    // Show creation form
    @GetMapping("/userAccount")
    public String showUserProfile(Model model, HttpSession session) {

        UserAccount user = (UserAccount) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }


        model.addAttribute("account", user);

        List<UserProfile> profiles =
                userProfileService.getProfileByUserId(user.getUserId());

        model.addAttribute("profiles", profiles);

        return "userAccount";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";

    }

    // Update email alert preference from the user account page
    @PostMapping("/userAccount/update-alert-settings")
    public String updateEmailAlertsFromAccount(
            @RequestParam(defaultValue = "false") boolean receiveAlerts,
            HttpSession session
    ) {
        // Get logged-in user from session
        UserAccount user = (UserAccount) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        // Update preference in session object
        user.setReceiveEmailAlerts(receiveAlerts);

        // Persist preference in database
        userAccountService.updateEmailAlertPreference(
                user.getUserId(),
                receiveAlerts
        );

        return "redirect:/userAccount";
    }
}
