package org.bettercare.business.services;

import org.bettercare.business.entities.Advice;
import org.bettercare.business.entities.Observation;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class AdviceService {

    private final SensorReadingService sensorReadingService;
    private final PollutionService pollutionService;

    public AdviceService(SensorReadingService sensorReadingService,
                         PollutionService pollutionService) {
        this.sensorReadingService = sensorReadingService;
        this.pollutionService = pollutionService;
    }

    public Advice generateAdvice() {
        List<Observation> observations = sensorReadingService.getObservations();
        if (observations == null || observations.isEmpty()) {
            return new Advice(0, "No sensor data available to generate advice.");
        }

        Observation observation = observations.get(observations.size() - 1);
        int uvIndex = observation.getUvIndex();
        int pollutionLevel = pollutionService.getPollutionAVG();
        LocalTime time = observation.getObservation_time();
        LocalDate date = observation.getObservation_date();

        int riskLevel = calculateRiskLevel(time);
        String adviceInfo = buildAdviceMessage(
                date,
                generateTimeAdvice(time),
                generateUvAdvice(uvIndex),
                generatePollutionAdvice(pollutionLevel),
                riskLevel
        );

        return new Advice(riskLevel, adviceInfo);
    }

    public String calculateHighestDangerLevel(int pollution, int uv) {
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

        return getDangerRank(pollutionLevel) >= getDangerRank(uvLevel) ? pollutionLevel : uvLevel;
    }

    private int calculateRiskLevel(LocalTime time) {
        int risk = 0;
        if (time.getHour() > 10) risk++;
        if (time.getHour() > 11) risk++;
        if (time.getHour() > 12) risk++;
        if (time.getHour() > 13) risk++;
        return risk;
    }

    private String generateTimeAdvice(LocalTime time) {
        int hour = time.getHour();
        String formatted = formatTime(time);

        if (hour >= 11 && hour <= 15) {
            return "Time: " + formatted +
                    " - Sun activity is at its strongest. UV exposure is highest around midday.";
        } else if (hour < 11) {
            return "Time: " + formatted +
                    " - Sun activity is increasing. UV exposure will rise toward midday.";
        } else {
            return "Time: " + formatted +
                    " - Sun activity is decreasing. UV risk is gradually lowering.";
        }
    }

    private String generateUvAdvice(int uv) {
        if (uv <= 2) return "Low UV level. Minimal sun protection is needed.";
        if (uv <= 5) return "Moderate UV level. Sunglasses and light sunscreen are recommended.";
        if (uv <= 7) return "High UV level. Use sunscreen SPF 30+ and seek shade when possible.";
        if (uv <= 10) return "Very high UV level. Avoid direct sunlight and wear protective clothing.";
        return "Extreme UV level. Stay indoors if possible.";
    }

    private String generatePollutionAdvice(int pollution) {
        if (pollution <= 50) return "Air quality is good. Outdoor activities are safe.";
        if (pollution <= 100) return "Air quality is moderate. Sensitive individuals should be cautious.";
        if (pollution <= 200) return "Air quality is unhealthy. Limit prolonged outdoor activities.";
        if (pollution <= 500) return "Air quality is very unhealthy. Stay indoors if possible.";
        return "Air pollution level is extremely high. Avoid outdoor exposure.";
    }

    private String buildAdviceMessage(LocalDate date,
                                      String timeAdvice,
                                      String uvAdvice,
                                      String pollutionAdvice,
                                      int riskLevel) {
        return "Date: " + date + "\n\n" +
                timeAdvice + "\n\n" +
                "UV Advice:\n" +
                "- " + uvAdvice + "\n\n" +
                "Air Quality Advice:\n" +
                "- " + pollutionAdvice + "\n\n" +
                "Overall Risk Level: " + riskLevel + " / 5";
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

    private String formatTime(LocalTime time) {
        return String.format("%02d:%02d", time.getHour(), time.getMinute());
    }
}
