package org.bettercare.infrastructure.persistence.repository;

// This repository reads sensor values from the database

import org.bettercare.domain.model.Observation;
import org.bettercare.business.repository.SensorReadingRepository;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
public class JdbcSensorReadingRepository implements SensorReadingRepository {

    private final JdbcClient jdbcClient;
    private final int BASELINE = 60;

    public JdbcSensorReadingRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public List<Observation> getObservations() {

        String sql = "SELECT * FROM sensor_data";

        return jdbcClient.sql(sql)
                .query((rs, rowNum) -> {
                    LocalDateTime observedAt = rs.getTimestamp("observed_at").toLocalDateTime();
                    LocalTime time = observedAt.toLocalTime();

                    return new Observation(
                            rs.getInt("id"),
                            observedAt.toLocalDate(),
                            time,
                            rs.getInt("pollution_level")-BASELINE,
                            rs.getInt("uv_index")
                    );
                })
                .list();
    }

    @Override
    public int getUVIndex() {
        String sql = """
        SELECT uv_index
        FROM sensor_data
        ORDER BY observed_at DESC
        LIMIT 1
        """;

        return jdbcClient.sql(sql)
                .query(Integer.class)
                .optional()
                .orElse(0);
    }

}