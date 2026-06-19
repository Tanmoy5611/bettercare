package org.bettercare.business.intelligence;

// This class uses the saved AI model to predict the current air quality

import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;
import ai.onnxruntime.OnnxTensor;

import java.time.LocalTime;
import java.util.Collections;

import org.bettercare.business.service.TrafficReadingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AirQualityAi {
    private static final Logger log = LoggerFactory.getLogger(AirQualityAi.class);
    private OrtEnvironment env;
    private OrtSession session;
    private final TrafficReadingService trafficReadingService;

    public AirQualityAi(TrafficReadingService trafficReadingService) throws Exception {
        // Load the model once when Spring creates this service
        this.env = OrtEnvironment.getEnvironment();
        byte[] modelBytes = getClass().getResourceAsStream("/models/air_quality_tree.onnx").readAllBytes();
        this.session = env.createSession(modelBytes);
        this.trafficReadingService = trafficReadingService;
    }

    public int predictAirQuality() {
        try {
            // These are the values the model needs for its prediction
            double hour = LocalTime.now().getHour();
            double minute = LocalTime.now().getMinute();
            double congestion = trafficReadingService.getCongestionLevel();
            double jams = trafficReadingService.getTrafficJams();

            // The order must stay the same as the order used to train the model
            float[][] inputData = {{(float) congestion, (float) jams, (float) hour, (float) minute}};

            try (OnnxTensor tensor = OnnxTensor.createTensor(env, inputData);
                 OrtSession.Result results = session.run(Collections.singletonMap("float_input", tensor))) {

                float[][] output = (float[][]) results.get(0).getValue();
                return Math.round(output[0][0]);
            }
        } catch (OrtException e) {
            log.warn("AI model error", e);
            return -1;
        } catch (Exception e) {
            log.warn("Air quality prediction failed", e);
            return -1;
        }
    }
}