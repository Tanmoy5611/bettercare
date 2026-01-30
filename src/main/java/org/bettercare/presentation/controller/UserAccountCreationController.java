package org.bettercare.presentation.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.bettercare.business.services.UserAccountService;
import org.bettercare.business.viewmodel.CreationViewModel;
import org.bettercare.business.viewmodel.LoginViewModel;
import org.bettercare.business.entities.UserAccount;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class UserAccountCreationController {
private final UserAccountService userAccountService;
    private final Logger log = LoggerFactory.getLogger(UserAccountCreationController.class);

    public UserAccountCreationController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
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
        System.out.println("successful login page");
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
        UserAccount account = new UserAccount();
        account.setName(creationVM.getName());
        account.setEmail(creationVM.getEmail());
        account.setPassword(creationVM.getPassword());
        userAccountService.insertUserAccount(account);
        account = userAccountService.findByName(creationVM.getName());
        model.addAttribute("account", account);
        session.setAttribute("user", account);
        return "home";
    }


    @PostMapping("/userAccounts/login")
    public String handleUserLogin(Model model, @Valid @ModelAttribute("loginVM") LoginViewModel loginViewModel
            , BindingResult result,HttpSession session) {
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
            System.out.println("User logged in: ID = " + account.getUserId());
            return "redirect:/userAccount";
        }
        else {
            model.addAttribute("errorMessage", "Name and/or Password are wrong");
            return "userAccountLogin";
        }
    }
}