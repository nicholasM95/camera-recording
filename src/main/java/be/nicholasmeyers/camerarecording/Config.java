package be.nicholasmeyers.camerarecording;

import java.util.List;

public class Config {

    private List<Camera> cameras;
    private String outputFolder;

    private String bucketEndpoint;
    private String bucketName;
    private String accessKey;
    private String secretKey;

    public Config() {
    }

    public Config(List<Camera> cameras, String outputFolder) {
        this.cameras = cameras;
        this.outputFolder = outputFolder;
    }

    public List<Camera> getCameras() {
        return cameras;
    }

    public void setCameras(List<Camera> cameras) {
        this.cameras = cameras;
    }

    public String getOutputFolder() {
        return outputFolder;
    }

    public void setOutputFolder(String outputFolder) {
        this.outputFolder = outputFolder;
    }

    public String getBucketEndpoint() {
        return bucketEndpoint;
    }

    public void setBucketEndpoint(String bucketEndpoint) {
        this.bucketEndpoint = bucketEndpoint;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
