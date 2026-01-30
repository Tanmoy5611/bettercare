package org.bettercare.business.services;

import org.bettercare.business.entities.Observation;
import org.bettercare.business.entities.UserAccount;
import org.bettercare.business.entities.enums.NOTIFICATION_LEVEL;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WeatherAlertService {

    private final NotificationService notificationService;
    private final EmailService emailService;
    private final UserAccountService userAccountService;
    private final SensorReadingService sensorReadingService;
    private final PollutionService pollutionService;

    // Constructor injection of required services and repositories
    public WeatherAlertService(NotificationService notificationService,
                               EmailService emailService,
                               UserAccountService userAccountService,
                               SensorReadingService sensorReadingService,
                               PollutionService pollutionService) {
        this.notificationService = notificationService;
        this.emailService = emailService;
        this.userAccountService = userAccountService;
        this.sensorReadingService = sensorReadingService;
        this.pollutionService = pollutionService;
    }

    // Scheduled task that runs every 5 minutes.
    @Scheduled(fixedRate = 300000)
    public void autoCheckSensors() {
        System.out.println("Running scheduled weather alert check...");

        List<Observation> observations = sensorReadingService.getObservations();
        if (observations == null || observations.isEmpty()) {
            System.out.println("No sensor data found. Skipping.");
            return;
        }

        evaluateLatest(observations);
    }

    // Evaluates only the latest observation and applies the rules for all subscribed users.
    public void evaluateLatest(List<Observation> observations) {
        if (observations == null || observations.isEmpty()) return;

        // Only the most recent observation is relevant
        Observation latest = observations.get(observations.size() - 1);

        // All users with receive_email_alerts = true
        List<UserAccount> users = userAccountService.getUsersWithEmailAlertsEnabled();

        for (UserAccount user : users) {
            evaluateObservationForUser(latest, user);
        }
    }

    // Applies UV and air quality rules for one user. Creates notifications and sends an email if needed.
    private void evaluateObservationForUser(Observation obs, UserAccount user) {

        int uv = obs.getUvIndex();
        int pollution = pollutionService.getPollutionAVG();

        StringBuilder emailSummary = new StringBuilder();
        NOTIFICATION_LEVEL highestLevel = NOTIFICATION_LEVEL.INFO;

        // Calculate and store daily health score
        int healthScore = calculateHealthScore(uv, pollution);

        // INFO notification (always)
        String infoMsg = "Health Score Today: " + healthScore + "/100";
        notificationService.createNotification(user, infoMsg, NOTIFICATION_LEVEL.INFO);
        emailSummary.append(infoMsg).append("<br><br>");

        // UV evaluation rules
        if (uv >= 8) {
            String msg = "EXTREME UV (" + uv + ") – Avoid direct sunlight.";
            notificationService.createNotification(user, msg, NOTIFICATION_LEVEL.DANGER);
            emailSummary.append(msg).append("<br><br>");
            highestLevel = NOTIFICATION_LEVEL.DANGER;

        } else if (uv >= 6) {
            String msg = "HIGH UV (" + uv + ") – Use SPF 30+.";
            notificationService.createNotification(user, msg, NOTIFICATION_LEVEL.WARNING);
            emailSummary.append(msg).append("<br><br>");
            highestLevel = maxLevel(highestLevel, NOTIFICATION_LEVEL.WARNING);
        }

        // Air quality evaluation rules
        if (pollution > 150) {
            String msg = "Unhealthy air quality (" + pollution + "). Ventilate immediately!";
            notificationService.createNotification(user, msg, NOTIFICATION_LEVEL.DANGER);
            emailSummary.append(msg).append("<br><br>");
            highestLevel = NOTIFICATION_LEVEL.DANGER;

        } else if (pollution > 100) {
            String msg = "Moderate air quality (" + pollution + ").";
            notificationService.createNotification(user, msg, NOTIFICATION_LEVEL.WARNING);
            emailSummary.append(msg).append("<br><br>");
            highestLevel = maxLevel(highestLevel, NOTIFICATION_LEVEL.WARNING);
        }

        // Send email for INFO, WARNING, and DANGER if user enabled alerts
        if (user.getReceiveEmailAlerts() && user.getEmail() != null) {

            emailService.sendDetailedAlertEmail(
                    user.getEmail(),
                    user.getName(),
                    emailSummary.toString(),
                    healthScore,
                    uv,
                    pollution,
                    highestLevel
            );
        }
    }

    // Helper to pick stronger notification level
    private NOTIFICATION_LEVEL maxLevel(NOTIFICATION_LEVEL a, NOTIFICATION_LEVEL b) {
        return (a.ordinal() < b.ordinal()) ? b : a;
    }

    // Calculates a combined health score based on UV and air quality.
    // Air quality has a higher weight than UV.
    private int calculateHealthScore(int uv, int pollution) {
        int uvScore = switch (uv) {
            case 0, 1, 2 -> 100;
            case 3, 4, 5 -> 70;
            case 6, 7 -> 40;
            default -> 20;
        };

        int airScore = pollution < 50 ? 100 :
                pollution < 100 ? 70 :
                        pollution < 150 ? 45 :
                                pollution < 200 ? 20 : 5;

        return (int) ((uvScore * 0.4) + (airScore * 0.6));
    }
}