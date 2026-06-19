package org.bettercare.presentation.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.bettercare.business.service.UserAccountService;
import org.bettercare.business.service.NotificationService;
import org.bettercare.presentation.viewmodel.CreationViewModel;
import org.bettercare.presentation.viewmodel.LoginViewModel;
import org.bettercare.domain.model.UserAccount;
import org.bettercare.domain.model.enums.NotificationLevel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;

@Controller
public class UserAccountCreationController {
    private final UserAccountService userAccountService;
    private final NotificationService notificationService;
    private final Logger log = LoggerFactory.getLogger(UserAccountCreationController.class);

    public UserAccountCreationController(UserAccountService userAccountService,
                                         NotificationService notificationService) {
        this.userAccountService = userAccountService;
        this.notificationService = notificationService;
    }

    // Show empty form
    @GetMapping("/newaccount")
    public String showUserAccountCreation(Model model, CreationViewModel creationVM) {
        model.addAttribute("creationVM", creationVM);
        return "userAccountCreation";
    }

    @GetMapping("/login")
    public String Login(Model model, LoginViewModel loginViewModel) {
        model.addAttribute("loginVM", loginViewModel);
        return "userAccountLogin";
    }

    // Handle submitted form
    @PostMapping("/userAccounts/create")
    public String handleUserAccountCreation(
            Model model,HttpSession session,@Valid @ModelAttribute("creationVM") CreationViewModel creationVM
            ,BindingResult result)
    {
        model.addAttribute("submitted", true);
        if (result.hasErrors()) {
            result.getAllErrors()
                    .forEach(error -> log.error(error.toString()
                    ));

            return "userAccountCreation";
        }
        if (userAccountService.findByName(creationVM.getName()) != null) {
            result.rejectValue("name", "name.taken", "That username is already in use.");
            return "userAccountCreation";
        }

        UserAccount account = new UserAccount();
        account.setName(creationVM.getName());
        account.setEmail(creationVM.getEmail());
        account.setPassword(creationVM.getPassword());
        try {
            userAccountService.insertUserAccount(account);
            account = userAccountService.findByName(creationVM.getName());
            session.setAttribute("user", account);
            notificationService.createNotification(
                    account,
                    "Welcome to BetterCare. Your account is ready to use.",
                    NotificationLevel.INFO
            );
            return "redirect:/";
        } catch (DataIntegrityViolationException exception) {
            log.warn("Account creation failed because username '{}' was already registered", creationVM.getName());
            result.rejectValue("name", "name.taken", "That username is already in use.");
            return "userAccountCreation";
        }
    }


    @PostMapping("/userAccounts/login")
    public String handleUserLogin(Model model, @Valid @ModelAttribute("loginVM") LoginViewModel loginViewModel
            , BindingResult result,HttpSession session) {
        // The service checks the password so the controller only handles the page flow
        model.addAttribute("submitted", true);
        if (result.hasErrors()) {
            result.getAllErrors()
                    .forEach(error -> log.error(error.toString()
                    ));
            return "userAccountLogin";
        }
        UserAccount account = userAccountService.loginVerification(loginViewModel.getName(),
                loginViewModel.getPassword());

        if (account != null) {
            model.addAttribute("account", account);
            session.setAttribute("user", account);
            session.setAttribute("loggedUserId", account.getUserId());
            log.info("User logged in with id {}", account.getUserId());
            return "redirect:/userAccount";
        }
        else {
            model.addAttribute("errorMessage", "Name and/or Password are wrong");
            return "userAccountLogin";
        }
    }
}