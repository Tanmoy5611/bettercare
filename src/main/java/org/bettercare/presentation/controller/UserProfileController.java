package org.bettercare.presentation.controller;

// This controller handles creating and showing a user profile

import jakarta.servlet.http.HttpSession;
import org.bettercare.domain.model.UserAccount;
import org.bettercare.domain.model.UserProfile;
import org.bettercare.domain.model.enums.SkinColor;
import org.bettercare.business.service.UserAccountService;
import org.bettercare.business.service.UserProfileService;
import org.bettercare.business.intelligence.SunExAi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class UserProfileController {
    private static final Logger log = LoggerFactory.getLogger(UserProfileController.class);

    private final UserProfileService profileService;
    private final UserAccountService accountService;
    private final SunExAi sunExAi;

    public UserProfileController(UserProfileService profileService,
                                 UserAccountService accountService,
                                 SunExAi sunExAi) {
        this.profileService = profileService;
        this.accountService = accountService;
        this.sunExAi = sunExAi;
    }

    @GetMapping("/userProfile")
    public String listProfiles(Model model, HttpSession session) {
        UserAccount user = (UserAccount) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        List<UserProfile> profiles = profileService.getProfileByUserId(user.getUserId());

        profiles.forEach(p -> {
            double prediction = sunExAi.predict(p.getSkinColor().ordinal(), p.getAge());
            p.setSunExposure(prediction);
        });

        model.addAttribute("profiles", profiles);
        model.addAttribute("account", user);
        model.addAttribute("profile", new UserProfile());
        model.addAttribute("skinColors", SkinColor.values());
        return "userProfile";
    }

    @PostMapping("/userProfile")
    public String createProfile(@ModelAttribute UserProfile profile
            , HttpSession Session, Model model, RedirectAttributes redirectAttributes
    ) {
        UserAccount user = (UserAccount) Session.getAttribute("user");
        profile.setUserAccountId(user.getUserId());
        redirectAttributes.addFlashAttribute("successMessage", "Profile created successfully.");
        profileService.save(profile);
        model.addAttribute("account", user);
        log.info("Created profile for user {}", user.getUserId());
        return "redirect:/userProfile";
    }

    @GetMapping("/edit/{id}")
    public String editProfile(@PathVariable int id, Model model) {
        UserProfile profile = profileService.findById(id);
        model.addAttribute("profile", profile);
        model.addAttribute("accounts", accountService.findAll());
        model.addAttribute("skinColors", SkinColor.values());
        return "userProfile-edit";
    }

    @PostMapping("/update/{id}")
    public String updateProfile(
            @PathVariable int id,
            @ModelAttribute UserProfile profile,
            HttpSession Session
    ) {
        profile.setProfileId(id);
        UserAccount user = (UserAccount) Session.getAttribute("user");
        profileService.update(profile, user);
        return "redirect:/userProfile";
    }

    @PostMapping("/delete/{id}")
    public String deleteProfile(@PathVariable Long id) {
        profileService.delete(id);
        return "redirect:/userProfile";
    }
}