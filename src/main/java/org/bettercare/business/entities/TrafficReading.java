package org.bettercare.business.entities;

import java.time.LocalDate;
import java.time.LocalTime;

public class TrafficReading {
    private int reading_id;
    private LocalDate observation_date;
    private LocalTime observation_time;
    private int congestionLevel;
    private int trafficJams;

    public TrafficReading(int reading_id, LocalDate observation_date, LocalTime observation_time, int congestionLevel, int trafficJams) {
        this.reading_id = reading_id;
        this.observation_date = observation_date;
        this.observation_time = observation_time;
        this.congestionLevel = congestionLevel;
        this.trafficJams = trafficJams;
    }

    public LocalDate getObservation_date() {
        return observation_date;
    }

    public int getReading_id() {
        return reading_id;
    }

    public void setObservation_date(LocalDate observation_date) {
        this.observation_date = observation_date;
    }

    public LocalTime getObservation_time() {
        return observation_time;
    }

    public void setObservation_time(LocalTime observation_time) {
        this.observation_time = observation_time;
    }

    public int getCongestionLevel() {
        return congestionLevel;
    }

    public void setCongestionLevel(int congestionLevel) {
        this.congestionLevel = congestionLevel;
    }

    public int getTrafficJams() {
        return trafficJams;
    }

    public void setTrafficJams(int trafficJams) {
        this.trafficJams = trafficJams;
    }
}
