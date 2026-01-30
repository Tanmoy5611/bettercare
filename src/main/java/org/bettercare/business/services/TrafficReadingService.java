package org.bettercare.business.services;

import org.bettercare.business.entities.TrafficReading;
import org.bettercare.data.repository.TrafficReadingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrafficReadingService {

    private final TrafficReadingRepository trafficReadingRepository;

    public TrafficReadingService(TrafficReadingRepository trafficReadingRepository) {
        this.trafficReadingRepository = trafficReadingRepository;
    }

    public List<TrafficReading> getAllTrafficReadings(){
        return trafficReadingRepository.getTrafficReadings();
    }

    public int getCongestionLevel(){
        return  trafficReadingRepository.getCongestionLevel();
    }

    public int getTrafficJams(){
        return  trafficReadingRepository.getTrafficJams();
    }

}
