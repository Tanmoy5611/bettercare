package org.bettercare.business.entities;

import java.time.LocalDate;
import java.time.LocalTime;

public class Observation {
    private int observation_id;
    private LocalDate observation_date;
    private LocalTime observation_time;
    private int pollutionLevel;
    private int uvIndex;

    public Observation(int observation_id,
                       LocalDate observation_date,
                       LocalTime observation_time,
                       int pollutionLevel,
                       int uvIndex) {
        this.observation_id = observation_id;
        this.observation_date = observation_date;
        this.observation_time = observation_time;
        this.pollutionLevel = pollutionLevel;
        this.uvIndex = uvIndex;
    }

    public int getObservation_id() {
        return observation_id;
    }

    public LocalDate getObservation_date() {
        return observation_date;
    }

    public int getPollutionLevel() {
        return pollutionLevel;
    }

    public int getUvIndex() {
        return uvIndex;
    }

    public LocalTime getObservation_time() {
        return observation_time;
    }
}
