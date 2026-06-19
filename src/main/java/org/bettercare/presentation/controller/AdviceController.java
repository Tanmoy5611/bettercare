package org.bettercare.presentation.controller;

import jakarta.servlet.http.HttpSession;
import org.bettercare.domain.model.Advice;
import org.bettercare.domain.model.UserAccount;
import org.bettercare.business.service.AdviceService;
import org.bettercare.business.service.PollutionService;
import org.bettercare.business.service.SensorReadingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdviceController {

    private SensorReadingService sensorReadingService;
    private PollutionService pollutionService;
    private AdviceService adviceService;

    public AdviceController(SensorReadingService sensorReadingService,
                            PollutionService pollutionService,
                            AdviceService adviceService) {
        this.sensorReadingService = sensorReadingService;
        this.pollutionService = pollutionService;
        this.adviceService = adviceService;
    }

    @GetMapping("/advice")
    public String showAdvice(Model model, HttpSession session) {
        // The service keeps the advice logic outside the controller
        UserAccount user = (UserAccount) session.getAttribute("user");
        Advice advice = adviceService.generateAdvice();

        model.addAttribute("account", user);
        model.addAttribute("advice", advice);
        model.addAttribute("adviceInfo", advice.getAdviceInfo());

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