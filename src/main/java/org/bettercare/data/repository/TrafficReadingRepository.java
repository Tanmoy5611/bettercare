package org.bettercare.data.repository;

import org.bettercare.business.entities.TrafficReading;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.List;

@Repository
public class TrafficReadingRepository {

    private final JdbcClient jdbcClient;
    private final int BASELINE = 18;

    public TrafficReadingRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<TrafficReading> getTrafficReadings() {

        String sql = "SELECT * FROM traffic_data";

        return jdbcClient.sql(sql)
                .query((rs, rowNum) -> {
                    OffsetDateTime odt = rs.getObject("observed_at", OffsetDateTime.class);
                    LocalTime time = odt.toLocalTime();

                    return new TrafficReading(
                            rs.getInt("id"),
                            odt.toLocalDate(),
                            time,
                            rs.getInt("congestion_level"),
                            (rs.getInt("traffic_jams") - BASELINE)
                    );
                })
                .list();
    }

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
