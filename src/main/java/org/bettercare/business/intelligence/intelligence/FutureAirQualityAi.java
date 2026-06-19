package org.bettercare.business.intelligence;

// This class predicts how the air quality may look in two hours

import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;
import ai.onnxruntime.OnnxTensor;

import java.time.LocalTime;
import java.util.Collections;

import org.bettercare.business.service.PollutionService;
import org.bettercare.business.service.TrafficReadingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FutureAirQualityAi {
    private static final Logger log = LoggerFactory.getLogger(FutureAirQualityAi.class);
    private OrtEnvironment env;
    private OrtSession session;
    private final TrafficReadingService trafficReadingService;
    private final PollutionService pollutionService;

    public FutureAirQualityAi(TrafficReadingService trafficReadingService, PollutionService pollutionService) throws Exception {
        // Load the future prediction model when the service starts
        this.env = OrtEnvironment.getEnvironment();
        byte[] modelBytes = getClass().getResourceAsStream("/models/air_quality_2h_forest.onnx").readAllBytes();
        this.session = env.createSession(modelBytes);
        this.trafficReadingService = trafficReadingService;
        this.pollutionService = pollutionService;
    }

    //Predict Air Quality in 2h
    public int futureAirQuality() {
        try {
            // Current pollution is included because it affects the later value
            double hour = LocalTime.now().getHour();
            double minute = LocalTime.now().getMinute();
            double congestion = trafficReadingService.getCongestionLevel();
            double jams = trafficReadingService.getTrafficJams();
            double currentAir = pollutionService.getPollutionAVG();

            float[][] inputData = {{(float) congestion, (float) jams, (float) hour, (float) minute, (float) currentAir}};

            try (OnnxTensor tensor = OnnxTensor.createTensor(env, inputData);
                 OrtSession.Result results = session.run(Collections.singletonMap("float_input", tensor))) {

                float[][] output = (float[][]) results.get(0).getValue();
                return Math.round(output[0][0]);
            }
        } catch (OrtException e) {
            log.warn("AI model error", e);
            return -1;
        } catch (Exception e) {
            log.warn("Future air quality prediction failed", e);
            return -1;
        }
    }
}