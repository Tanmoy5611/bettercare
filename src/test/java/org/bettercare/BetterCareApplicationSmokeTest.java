package org.bettercare;

import org.bettercare.domain.model.UserAccount;
import org.bettercare.business.service.NotificationService;
import org.bettercare.business.service.SensorReadingService;
import org.bettercare.business.service.TrafficReadingService;
import org.bettercare.business.service.UserAccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BetterCareApplicationSmokeTest {

    @Autowired
    private SensorReadingService sensorReadingService;

    @Autowired
    private TrafficReadingService trafficReadingService;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private NotificationService notificationService;

    @Test
    void contextLoadsWithDemoData() {
        // This checks that the main demo data is ready when the app starts
        UserAccount demoUser = userAccountService.loginVerification("demo", "password123");

        assertFalse(sensorReadingService.getObservations().isEmpty());
        assertFalse(trafficReadingService.getAllTrafficReadings().isEmpty());
        assertNotNull(demoUser);
        assertFalse(notificationService.getNotificationsFor(demoUser).isEmpty());
    }
}