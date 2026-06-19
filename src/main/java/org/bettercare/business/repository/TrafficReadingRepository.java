package org.bettercare.business.repository;

import org.bettercare.domain.model.TrafficReading;

import java.util.List;

public interface TrafficReadingRepository {
    List<TrafficReading> getTrafficReadings();

    int getCongestionLevel();

    int getTrafficJams();
}