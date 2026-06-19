package org.bettercare.bdd.advice;

// These steps test the UV advice feature one step at a time

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.bettercare.domain.model.Advice;
import org.bettercare.domain.model.Observation;
import org.bettercare.business.service.AdviceService;
import org.bettercare.business.service.PollutionService;
import org.bettercare.business.service.SensorReadingService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UvAdviceSteps {

    private final SensorReadingService sensorReadingService = mock(SensorReadingService.class);
    private final PollutionService pollutionService = mock(PollutionService.class);
    private final AdviceService adviceService = new AdviceService(sensorReadingService, pollutionService);
    private Advice generatedAdvice;

    @Given("the UV index is above {int}")
    public void theUvIndexIsAbove(int threshold) {
        Observation highUvObservation = new Observation(
                1, LocalDate.of(2026, 6, 19), LocalTime.of(12, 0), 40, threshold + 1
        );
        when(sensorReadingService.getObservations()).thenReturn(List.of(highUvObservation));
        when(pollutionService.getPollutionAVG()).thenReturn(40);
    }

    @When("the system generates UV advice")
    public void theSystemGeneratesUvAdvice() {
        generatedAdvice = adviceService.generateAdvice();
    }

    @Then("the user should receive the advice {string}")
    public void theUserShouldReceiveTheAdvice(String expectedAdvice) {
        assertTrue(generatedAdvice.getAdviceInfo().contains(expectedAdvice));
    }
}