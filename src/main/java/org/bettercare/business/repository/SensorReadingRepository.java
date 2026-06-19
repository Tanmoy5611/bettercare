package org.bettercare.business.repository;

import org.bettercare.domain.model.Observation;

import java.util.List;

public interface SensorReadingRepository {
    List<Observation> getObservations();

    int getUVIndex();
}