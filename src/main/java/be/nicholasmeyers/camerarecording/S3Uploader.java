package be.nicholasmeyers.camerarecording;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.nio.file.Files;
import java.time.Instant;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class S3Uploader {

    private final S3Client s3Client;
    private final String bucketName;
    private final String recordingsDir;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public S3Uploader(S3Client s3Client, String bucketName, String recordingsDir) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
        this.recordingsDir = recordingsDir;
    }

    public void start() {
        scheduler.scheduleAtFixedRate(this::uploadPendingFiles, 0, 1, TimeUnit.MINUTES);
    }

    private void uploadPendingFiles() {
        File dir = new File(recordingsDir);
        File[] files = dir.listFiles((d, name) -> name.endsWith(".mp4"));
        if (files == null) return;

        Arrays.sort(files, Comparator.comparingLong(File::lastModified));

        Instant oneHourAgo = Instant.now().minusSeconds(3600);

        for (File file : files) {
            if (file.lastModified() > oneHourAgo.toEpochMilli()) continue;

            try {
                String s3Key = buildS3Key(file);
                System.out.println("Uploading: " + file.getName() + " -> " + s3Key);

                s3Client.putObject(
                        PutObjectRequest.builder()
                                .bucket(bucketName)
                                .key(s3Key)
                                .build(),
                        file.toPath()
                );

                Files.delete(file.toPath());
                System.out.println("Uploaded and deleted: " + file.getName());

            } catch (Exception e) {
                System.err.println("Failed to upload " + file.getName() + ": " + e.getMessage());
            }
        }
    }

    private String buildS3Key(File file) {
        String name = file.getName();
        String[] parts = name.replace(".mp4", "").split("_");

        String date = parts[parts.length - 2];
        String time = parts[parts.length - 1];

        String[] dateParts = date.split("-");
        String hour = time.split("-")[0];

        String cameraName = String.join("_", Arrays.copyOf(parts, parts.length - 2));

        return String.format("%s/%s/%s/%s/%s/%s",
                cameraName,
                dateParts[0], dateParts[1], dateParts[2],
                hour,
                name);
    }

    public void shutdown() {
        scheduler.shutdownNow();
    }
}