package org.bettercare.presentation.controller;

import jakarta.servlet.http.HttpSession;
import org.bettercare.business.entities.*;
import org.bettercare.business.services.PollutionService;
import org.bettercare.business.services.SensorReadingService;
import org.bettercare.business.services.TrafficReadingService;
import org.bettercare.business.services.intelligence.FutureAirQualityAi;
import org.bettercare.business.services.NotificationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class BetterCareController {

    private final SensorReadingService sensorReadingService;
    private final NotificationService notificationService;
    private final TrafficReadingService trafficReadingService;
    private final PollutionService pollutionService;
    private final FutureAirQualityAi futureAirQualityAi;

    public BetterCareController(SensorReadingService sensorReadingService,
                                NotificationService notificationService,
                                TrafficReadingService trafficReadingService,
                                PollutionService pollutionService,
                                FutureAirQualityAi futureAirQualityAi) {
        this.sensorReadingService = sensorReadingService;
        this.notificationService = notificationService;
        this.trafficReadingService = trafficReadingService;
        this.pollutionService = pollutionService;
        this.futureAirQualityAi = futureAirQualityAi;
    }

    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        UserAccount loggedUser = (UserAccount) session.getAttribute("user");

        List<Observation> observations = sensorReadingService.getObservations();
        model.addAttribute("observations", observations);

        // Get latest observation for display on home page
        Observation latestObservation = null;
        if (!observations.isEmpty()) {
            latestObservation = observations.get(observations.size() - 1);
            model.addAttribute("latestObservation", latestObservation);
            model.addAttribute("pollAVG", pollutionService.getPollutionAVG());

            // Calculate highest danger level based on observation
            String highestDangerLevel = calculateHighestDangerLevel(
                    pollutionService.getPollutionAVG(),
                    latestObservation.getUvIndex()
            );
            model.addAttribute("highestDangerLevel", highestDangerLevel);
            
            // Graph data for sensor observations
            List<String> timestamps = observations.stream()
                    .map(o -> o.getObservation_date() + " " + o.getObservation_time())
                    .toList();
            List<Integer> pollutionLevels = observations.stream()
                    .map(Observation::getPollutionLevel)
                    .toList();
            List<Integer> uvIndexes = observations.stream()
                    .map(Observation::getUvIndex)
                    .toList();

            model.addAttribute("timestamps", timestamps);
            model.addAttribute("pollutionLevels", pollutionLevels);
            model.addAttribute("uvIndexes", uvIndexes);
        }

        model.addAttribute("futureAQ", futureAirQualityAi.futureAirQuality());
        
        // Load traffic data for graph
        var trafficReads = trafficReadingService.getAllTrafficReadings();
        model.addAttribute("trafficReads", trafficReads);
        
        List<String> trafficTimestamps = trafficReads.stream()
                .map(o -> o.getObservation_date() + " " + o.getObservation_time())
                .toList();
        List<Integer> trafficJams = trafficReads.stream()
                .map(TrafficReading::getTrafficJams)
                .toList();
        List<Integer> congestionLevels = trafficReads.stream()
                .map(TrafficReading::getCongestionLevel)
                .toList();

        model.addAttribute("trafficTimestamps", trafficTimestamps);
        model.addAttribute("congestionLevels", congestionLevels);
        model.addAttribute("trafficJams", trafficJams);

        if (loggedUser != null) {
            model.addAttribute("notifications",
                    notificationService.getNotificationsFor(loggedUser));
            model.addAttribute("latestNotifications",
                    notificationService.getLatestForUser(loggedUser, 3));

            model.addAttribute("account", loggedUser);
        }

        Notification latest = notificationService.getLatestNotification();
        if (latest == null) {
            latest = new Notification();
            latest.setMessage("No notifications yet");
        }
        model.addAttribute("latest", latest);

        // Generate advice for home
        if (!observations.isEmpty()) {
            Advice advice = new Advice(sensorReadingService, pollutionService);
            String adviceInfo = advice.generateAdvice();

            model.addAttribute("advice", advice);
            model.addAttribute("adviceInfo", adviceInfo);
            model.addAttribute("riskLevel", advice.getRiskLevel());

        }

        return "home";
    }
    
    /**
     * Calculate the highest danger level based on pollution and UV index
     * Returns: "hazardous", "very-unhealthy", "unhealthy", "unhealthy-sensitive", "moderate", or "good"
     */
    private String calculateHighestDangerLevel(int pollution, int uv) {
        String pollutionLevel;
        if (pollution <= 50) {
            pollutionLevel = "good";
        } else if (pollution <= 100) {
            pollutionLevel = "moderate";
        } else if (pollution <= 150) {
            pollutionLevel = "unhealthy-sensitive";
        } else if (pollution <= 200) {
            pollutionLevel = "unhealthy";
        } else if (pollution <= 300) {
            pollutionLevel = "very-unhealthy";
        } else {
            pollutionLevel = "hazardous";
        }
        
        String uvLevel;
        if (uv >= 1 && uv <= 2) {
            uvLevel = "good";
        } else if (uv >= 3 && uv <= 5) {
            uvLevel = "moderate";
        } else if (uv >= 6 && uv <= 7) {
            uvLevel = "unhealthy-sensitive";
        } else if (uv >= 8) {
            uvLevel = "unhealthy";
        } else {
            uvLevel = "good";
        }

        int pollutionRank = getDangerRank(pollutionLevel);
        int uvRank = getDangerRank(uvLevel);
        
        return pollutionRank >= uvRank ? pollutionLevel : uvLevel;
    }
    
    private int getDangerRank(String level) {
        return switch (level) {
            case "good" -> 0;
            case "moderate" -> 1;
            case "unhealthy-sensitive" -> 2;
            case "unhealthy" -> 3;
            case "very-unhealthy" -> 4;
            case "hazardous" -> 5;
            default -> 0;
        };
    }

}