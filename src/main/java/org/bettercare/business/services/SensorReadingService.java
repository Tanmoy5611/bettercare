package org.bettercare.business.services;

import org.bettercare.business.entities.Observation;
import org.bettercare.data.repository.SensorReadingRepository;
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
