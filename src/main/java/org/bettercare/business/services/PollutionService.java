package org.bettercare.business.services;

import org.bettercare.business.entities.Observation;
import org.bettercare.business.services.intelligence.AirQualityAi;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PollutionService {

    private final SensorReadingService sensorReadingService;
    private final AirQualityAi airQualityAi;

    public PollutionService(SensorReadingService sensorReadingService,
                            AirQualityAi airQualityAi) {
        this.sensorReadingService = sensorReadingService;
        this.airQualityAi = airQualityAi;
    }

    public int getPollutionAVG(){
        List<Observation> observations = sensorReadingService.getObservations();
        Observation latestObservation = null;
        latestObservation = observations.get(observations.size() - 1);

        int sensorAQ = latestObservation.getPollutionLevel();
        int predictedAQ = airQualityAi.predictAirQuality();
        int pollutionAVG = Math.round((sensorAQ+predictedAQ)/2);

        return pollutionAVG;
    }

}
