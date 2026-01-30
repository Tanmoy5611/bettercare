package org.bettercare.data.repository;

import org.bettercare.business.entities.Observation;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.List;

@Repository
public class SensorReadingRepository {

    private final JdbcClient jdbcClient;
    private final int BASELINE = 60;

    public SensorReadingRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<Observation> getObservations() {

        String sql = "SELECT * FROM sensor_data";

        return jdbcClient.sql(sql)
                .query((rs, rowNum) -> {
                    OffsetDateTime odt = rs.getObject("observed_at", OffsetDateTime.class);
                    LocalTime time = odt.toLocalTime();

                    return new Observation(
                            rs.getInt("id"),
                            odt.toLocalDate(),
                            time,
                            rs.getInt("pollution_level")-BASELINE,
                            rs.getInt("uv_index")
                    );
                })
                .list();
    }

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
