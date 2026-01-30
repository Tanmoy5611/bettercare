package org.bettercare.business.services.intelligence;

import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;
import ai.onnxruntime.OnnxTensor;

import java.time.LocalTime;
import java.util.Collections;

import org.bettercare.business.services.TrafficReadingService;
import org.springframework.stereotype.Service;

@Service
public class AirQualityAi {
    private OrtEnvironment env;
    private OrtSession session;
    private final TrafficReadingService trafficReadingService;

    public AirQualityAi(TrafficReadingService trafficReadingService) throws Exception {
        this.env = OrtEnvironment.getEnvironment();
        byte[] modelBytes = getClass().getResourceAsStream("/models/air_quality_tree.onnx").readAllBytes();
        this.session = env.createSession(modelBytes);
        this.trafficReadingService = trafficReadingService;
    }

    public int predictAirQuality() {
        try {
            double hour = LocalTime.now().getHour();
            double minute = LocalTime.now().getMinute();
            double congestion = trafficReadingService.getCongestionLevel();
            double jams = trafficReadingService.getTrafficJams();

            float[][] inputData = {{(float) congestion, (float) jams, (float) hour, (float) minute}};

            try (OnnxTensor tensor = OnnxTensor.createTensor(env, inputData);
                 OrtSession.Result results = session.run(Collections.singletonMap("float_input", tensor))) {

                float[][] output = (float[][]) results.get(0).getValue();
                return Math.round(output[0][0]);
            }
        } catch (OrtException e) {
            System.err.println("AI Model Error: " + e.getMessage());
            return -1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
