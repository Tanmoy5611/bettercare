package org.bettercare.business.services.intelligence;

import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;
import org.bettercare.business.services.SensorReadingService;
import org.springframework.stereotype.Service;
import java.time.LocalTime;
import java.util.Collections;
import java.time.LocalDate;

@Service
public class SunExAi {

        private OrtEnvironment env;
        private OrtSession session;
        private final SensorReadingService sensorReadingService;


    public SunExAi(SensorReadingService sensorReadingService) throws Exception {
            this.env = OrtEnvironment.getEnvironment();
            byte[] modelBytes = getClass().getResourceAsStream("/models/sunExAi.onnx").readAllBytes();
            this.session = env.createSession(modelBytes);
            this.sensorReadingService = sensorReadingService;
        }

        /*
    SkinType:
    0 = Pale White
    1 = Fair
    2 = Olive
    3 = Moderate Brown
    4 = Dark Brown
    5 = Black
     */

        public int predict(double SkinType, double Age) {
            try {
                double hour = LocalTime.now().getHour();
                double month = LocalDate.now().getMonthValue();
                double UVIndex = sensorReadingService.getUVIndex();

                float[][] inputData = {{(float) hour, (float) month, (float) UVIndex, (float) SkinType, (float) Age}};

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