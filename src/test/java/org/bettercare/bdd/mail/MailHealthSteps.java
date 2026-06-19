package org.bettercare.bdd.mail;

// These steps check that mail health monitoring is switched on

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.HealthContributorRegistry;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:bettercare-cucumber-mail-health")
public class MailHealthSteps {

    @Autowired
    private HealthContributorRegistry healthContributorRegistry;

    @Value("${management.health.mail.enabled}")
    private boolean mailHealthEnabled;

    @Given("mail health monitoring is enabled")
    public void mailHealthMonitoringIsEnabled() {
        Assertions.assertTrue(mailHealthEnabled);
    }

    @When("Actuator initializes its health contributors")
    public void actuatorInitializesItsHealthContributors() {
        // The Spring Boot test context initializes Actuator before this step.
    }

    @Then("a mail health contributor is available")
    public void aMailHealthContributorIsAvailable() {
        Assertions.assertNotNull(healthContributorRegistry.getContributor("mail"));
    }
}