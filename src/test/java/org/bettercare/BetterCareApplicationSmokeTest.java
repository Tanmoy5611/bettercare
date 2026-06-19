package org.bettercare;

import org.bettercare.business.entities.UserAccount;
import org.bettercare.business.services.NotificationService;
import org.bettercare.business.services.SensorReadingService;
import org.bettercare.business.services.TrafficReadingService;
import org.bettercare.business.services.UserAccountService;
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
        UserAccount demoUser = userAccountService.loginVerification("demo", "password123");

        assertFalse(sensorReadingService.getObservations().isEmpty());
        assertFalse(trafficReadingService.getAllTrafficReadings().isEmpty());
        assertNotNull(demoUser);
        assertFalse(notificationService.getNotificationsFor(demoUser).isEmpty());
    }
}
