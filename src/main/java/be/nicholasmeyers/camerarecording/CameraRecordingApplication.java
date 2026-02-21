package be.nicholasmeyers.camerarecording;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.IOException;
import java.net.URI;
import java.util.List;

public class CameraRecordingApplication {

    static void main(String[] args) throws IOException {
        System.out.println("Starting camera recording...");

        ConfigReader configReader = new ConfigReader();
        Config config = configReader.readConfig();
        List<Camera> cameras = config.getCameras();
        CameraRecorder recorder = new CameraRecorder();

        cameras.forEach(camera -> recorder.startRecording(camera.getName(), camera.getUrl(), config.getOutputFolder()));

        S3Client s3Client = S3Client.builder()
                .endpointOverride(URI.create(config.getBucketEndpoint()))
                .region(Region.US_EAST_1)
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(config.getAccessKey(), config.getSecretKey())
                ))
                .build();

        S3Uploader uploader = new S3Uploader(s3Client, config.getBucketName(), config.getOutputFolder());
        uploader.start();

        // Graceful shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(recorder::shutdown));
    }

}
