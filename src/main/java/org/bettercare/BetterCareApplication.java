package org.bettercare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.sql.SQLException;

@SpringBootApplication
@EnableScheduling   // for email notifications
public class BetterCareApplication {
    public static void main(String[] args) throws SQLException {
        SpringApplication.run(BetterCareApplication.class, args);
    }
}
