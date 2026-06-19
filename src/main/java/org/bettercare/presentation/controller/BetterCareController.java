package org.bettercare.presentation.controller;

import jakarta.servlet.http.HttpSession;
import org.bettercare.business.entities.*;
import org.bettercare.business.services.AdviceService;
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
    private final AdviceService adviceService;

    public BetterCareController(SensorReadingService sensorReadingService,
                                NotificationService notificationService,
                                TrafficReadingService trafficReadingService,
                                PollutionService pollutionService,
                                FutureAirQualityAi futureAirQualityAi,
                                AdviceService adviceService) {
        this.sensorReadingService = sensorReadingService;
        this.notificationService = notificationService;
        this.trafficReadingService = trafficReadingService;
        this.pollutionService = pollutionService;
        this.futureAirQualityAi = futureAirQualityAi;
        this.adviceService = adviceService;
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
            Advice advice = adviceService.generateAdvice();

            model.addAttribute("advice", advice);
            model.addAttribute("adviceInfo", advice.getAdviceInfo());
            model.addAttribute("riskLevel", advice.getRiskLevel());

        }

        return "home";
    }

    private String calculateHighestDangerLevel(int pollution, int uv) {
        return adviceService.calculateHighestDangerLevel(pollution, uv);
    }

}
