package be.nicholasmeyers.camerarecording;

import java.io.IOException;
import java.util.List;

public class CameraRecordingApplication {

    static void main(String[] args) throws IOException {
        System.out.println("Starting camera recording...");

        ConfigReader configReader = new ConfigReader();
        Config config = configReader.readConfig();
        List<Camera> cameras = config.getCameras();
        CameraRecorder recorder = new CameraRecorder();

        cameras.forEach(camera -> recorder.startRecording(camera.getName(), camera.getUrl(), config.getOutputFolder()));

        // Graceful shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(recorder::shutdown));
    }

}
