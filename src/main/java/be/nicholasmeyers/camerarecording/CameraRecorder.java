package be.nicholasmeyers.camerarecording;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CameraRecorder {

    private final ExecutorService executor = Executors.newFixedThreadPool(8);
    private final int segmentDuration = 300; // 5 minutes in seconds

    public void startRecording(String streamName, String rtspUrl, String outputDir) {
        executor.submit(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    String timestamp = LocalDateTime.now()
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
                    String outputFile = String.format("%s/%s_%s.mp4",
                            outputDir, streamName, timestamp);

                    ProcessBuilder pb = new ProcessBuilder(
                            "ffmpeg",
                            "-rtsp_transport", "tcp",
                            "-i", rtspUrl,
                            "-c", "copy",           // no re-encoding, fast
                            "-t", String.valueOf(segmentDuration),
                            "-movflags", "+faststart",
                            outputFile
                    );

                    pb.redirectErrorStream(true);
                    Process process = pb.start();

                    // Log output (optional)
                    try (BufferedReader reader = new BufferedReader(
                            new InputStreamReader(process.getInputStream()))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            System.out.println("[" + streamName + "] " + line);
                        }
                    }

                    process.waitFor();

                } catch (Exception e) {
                    System.err.println("Error recording " + streamName + ": " + e.getMessage());
                    try {
                        Thread.sleep(5000); // wait 5 sec on error, then retry
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        });
    }

    public void shutdown() {
        executor.shutdownNow();
    }

}
