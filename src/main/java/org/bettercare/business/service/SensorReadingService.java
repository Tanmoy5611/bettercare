package org.bettercare.business.service;

import org.bettercare.domain.model.Observation;
import org.bettercare.business.repository.SensorReadingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SensorReadingService {

    private final SensorReadingRepository sensorReadingRepository;

    public SensorReadingService(SensorReadingRepository sensorReadingRepository) {
        this.sensorReadingRepository = sensorReadingRepository;
    }

    public List<Observation> getObservations(){
        return sensorReadingRepository.getObservations();
    }

    public int getUVIndex(){
        return sensorReadingRepository.getUVIndex();
    }

}