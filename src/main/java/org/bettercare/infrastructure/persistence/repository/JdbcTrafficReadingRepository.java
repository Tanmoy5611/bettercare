package org.bettercare.infrastructure.persistence.repository;

// This repository reads traffic values from the database

import org.bettercare.domain.model.TrafficReading;
import org.bettercare.business.repository.TrafficReadingRepository;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
public class JdbcTrafficReadingRepository implements TrafficReadingRepository {

    private final JdbcClient jdbcClient;
    private final int BASELINE = 18;

    public JdbcTrafficReadingRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public List<TrafficReading> getTrafficReadings() {

        String sql = "SELECT * FROM traffic_data";

        return jdbcClient.sql(sql)
                .query((rs, rowNum) -> {
                    LocalDateTime observedAt = rs.getTimestamp("observed_at").toLocalDateTime();
                    LocalTime time = observedAt.toLocalTime();

                    return new TrafficReading(
                            rs.getInt("id"),
                            observedAt.toLocalDate(),
                            time,
                            rs.getInt("congestion_level"),
                            (rs.getInt("traffic_jams") - BASELINE)
                    );
                })
                .list();
    }

    @Override
    public int getCongestionLevel() {
        String sql = """
        SELECT congestion_level
        FROM traffic_data
        ORDER BY observed_at DESC
        LIMIT 1
        """;

        return jdbcClient.sql(sql)
                .query(Integer.class)
                .optional()
                .orElse(0);
    }

    @Override
    public int getTrafficJams() {
        String sql = """
        SELECT traffic_jams
        FROM traffic_data
        ORDER BY observed_at DESC
        LIMIT 1
        """;

        return jdbcClient.sql(sql)
                .query(Integer.class)
                .optional()
                .orElse(0);
    }
}