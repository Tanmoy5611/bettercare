package org.bettercare.presentation.controller;

import jakarta.servlet.http.HttpSession;
import org.bettercare.business.entities.Advice;
import org.bettercare.business.entities.UserAccount;
import org.bettercare.business.services.PollutionService;
import org.bettercare.business.services.SensorReadingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdviceController {

    private SensorReadingService sensorReadingService;
    private PollutionService pollutionService;

    public AdviceController(SensorReadingService sensorReadingService, PollutionService pollutionService) {
        this.sensorReadingService = sensorReadingService;
        this.pollutionService = pollutionService;
    }

    @GetMapping("/advice")
    public String showAdvice(Model model, HttpSession session) {
        UserAccount user = (UserAccount) session.getAttribute("user");
        Advice advice = new Advice(sensorReadingService,pollutionService);
        String adviceInfo = advice.generateAdvice();

        model.addAttribute("account", user);
        model.addAttribute("advice", advice);
        model.addAttribute("adviceInfo", adviceInfo);

        if (!sensorReadingService.getObservations().isEmpty()) {
            var observation = sensorReadingService.getObservations().getLast();
            model.addAttribute("uvIndex", observation.getUvIndex());
            model.addAttribute("pollutionLevel", pollutionService.getPollutionAVG());
            model.addAttribute("observationDate", observation.getObservation_date());
            model.addAttribute("observationTime", observation.getObservation_time());
        }

        return "advice";
    }
}
