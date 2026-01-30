package org.bettercare.presentation.controller;

import jakarta.servlet.http.HttpSession;
import org.bettercare.business.entities.UserAccount;
import org.bettercare.business.entities.enums.NOTIFICATION_LEVEL;
import org.bettercare.business.services.EmailService;
import org.bettercare.business.services.NotificationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class NotificationController {

    private final NotificationService notificationService;
    private final EmailService emailService;

    public NotificationController(NotificationService notificationService,
                                  EmailService emailService) {
        this.notificationService = notificationService;
        this.emailService = emailService;
    }

    // Show notification overview page for logged-in user
    @GetMapping("/notifications")
    public String notifications(Model model, HttpSession session) {

        // Get logged-in user ID from session
        UserAccount user = (UserAccount) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        // Load user notifications
        model.addAttribute(
                "alerts",
                notificationService.getNotificationsFor(user)
        );
        model.addAttribute("user", user);
        model.addAttribute("account",user);
        return "notifications";
    }

    // Mark a single notification as read
    @PostMapping("/notifications/seen/{id}")
    public String markSeen(@PathVariable int id) {
        notificationService.markSeen(id);
        return "redirect:/notifications";
    }


    // Mark all notifications as read for current user
    @PostMapping("/notifications/mark-all")
    public String markALLasRead(HttpSession session) {
        UserAccount user = (UserAccount) session.getAttribute("user");
        if (user != null) {
            notificationService.markAllAsSeenByUserId(user.getUserId());
            // notificationService.markAllAsSeen(user);
        }
        return "redirect:/notifications";
    }

    // Test endpoint: send email only (no notification saved)
    @GetMapping("/email-test")
    public String testEmail() {
        emailService.sendAlertEmail(
                "tanmoydas7349@gmail.com",
                "Test alert from BetterCare",
                NOTIFICATION_LEVEL.INFO
        );
        return "redirect:/notifications";
    }

    // create notification for logged-in user
    @GetMapping("/send-user-alert")
    public String sendAlertToLoggedUser(HttpSession session) {

        UserAccount user = (UserAccount) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        // Do not send if user disabled email alerts
        if (!user.isReceiveEmailAlerts()) {
            System.out.println("User " + user + " has disabled email alerts.");
            return "redirect:/notifications";
        }

        // Create test notification
        notificationService.createNotification(
                user,
                "Test alert from BetterCare",
                NOTIFICATION_LEVEL.INFO
        );

        System.out.println("Notification + email sent to: " + user.getEmail());
        return "redirect:/notifications";
    }
}