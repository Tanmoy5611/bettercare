package org.bettercare.business.entities;

import org.bettercare.business.services.PollutionService;
import org.bettercare.business.services.SensorReadingService;
import java.time.LocalDate;
import java.time.LocalTime;

public class Advice {
    private int riskLevel;
    private String adviceInfo;

    private final SensorReadingService sensorReadingService;
    private final PollutionService pollutionService;

    public Advice(SensorReadingService sensorReadingService, PollutionService pollutionService) {
        this.sensorReadingService = sensorReadingService;
        this.pollutionService = pollutionService;
    }

    public String generateAdvice() {
        if (sensorReadingService == null || sensorReadingService.getObservations().isEmpty()) {
            adviceInfo = "No sensor data available to generate advice.";
            return adviceInfo;
        }

        Observation observation = sensorReadingService.getObservations().getLast();

        int uvIndex = observation.getUvIndex();
        int pollutionLevel = pollutionService.getPollutionAVG();
        LocalTime time = observation.getObservation_time();
        LocalDate date = observation.getObservation_date();

        riskLevel = calculateRiskLevel(time);
        String timeAdvice = generateTimeAdvice(time);
        String uvAdvice = generateUvAdvice(uvIndex);
        String pollutionAdvice = generatePollutionAdvice(pollutionLevel);

        adviceInfo = buildAdviceMessage(date, timeAdvice, uvAdvice, pollutionAdvice, riskLevel);
        return adviceInfo;
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
        if (uv <= 2)
            return "Low UV level. Minimal sun protection is needed.";
        if (uv <= 5)
            return "Moderate UV level. Sunglasses and light sunscreen are recommended.";
        if (uv <= 7)
            return "High UV level. Use sunscreen SPF 30+ and seek shade when possible.";
        if (uv <= 10)
            return "Very high UV level. Avoid direct sunlight and wear protective clothing.";
        return "Extreme UV level. Stay indoors if possible.";
    }

    private String generatePollutionAdvice(int pollution) {
        if (pollution <= 50)
            return "Air quality is good. Outdoor activities are safe.";
        if (pollution <= 100)
            return "Air quality is moderate. Sensitive individuals should be cautious.";
        if (pollution <= 200)
            return "Air quality is unhealthy. Limit prolonged outdoor activities.";
        if (pollution <= 500)
            return "Air quality is very unhealthy. Stay indoors if possible.";
        return "Air pollution level is extremely high. Avoid outdoor exposure.";
    }

    private String buildAdviceMessage(
            LocalDate date,
            String timeAdvice,
            String uvAdvice,
            String pollutionAdvice,
            int riskLevel
    ) {
        return
                "Date: " + date + "\n\n" +

                        timeAdvice + "\n\n" +

                        "UV Advice:\n" +
                        "- " + uvAdvice + "\n\n" +

                        "Air Quality Advice:\n" +
                        "- " + pollutionAdvice + "\n\n" +

                        "Overall Risk Level: " + riskLevel + " / 5";
    }

    private String formatTime(LocalTime time) {
        return String.format("%02d:%02d", time.getHour(), time.getMinute());
    }

    public int getRiskLevel() {
        return riskLevel;
    }

    public String getAdviceInfo() {
        return adviceInfo;
    }

    @Override
    public String toString() {
        return "Advice{" +
                ", riskLevel=" + riskLevel +
                ", adviceInfo='" + adviceInfo + '\'' +
                '}';
    }
}
