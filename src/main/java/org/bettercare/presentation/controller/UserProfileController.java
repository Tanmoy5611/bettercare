package org.bettercare.presentation.controller;

import jakarta.servlet.http.HttpSession;
import org.bettercare.business.entities.UserAccount;
import org.bettercare.business.entities.UserProfile;
import org.bettercare.business.entities.enums.SKIN_COLOR;
import org.bettercare.business.services.UserAccountService;
import org.bettercare.business.services.UserProfileService;
import org.bettercare.business.services.intelligence.SunExAi;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class UserProfileController {

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
        model.addAttribute("skinColors", SKIN_COLOR.values());
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
        System.out.println(profile);
        return "redirect:/userProfile";
    }

    @GetMapping("/edit/{id}")
    public String editProfile(@PathVariable int id, Model model) {
        UserProfile profile = profileService.findById(id);
        model.addAttribute("profile", profile);
        model.addAttribute("accounts", accountService.findAll());
        model.addAttribute("skinColors", SKIN_COLOR.values());
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
