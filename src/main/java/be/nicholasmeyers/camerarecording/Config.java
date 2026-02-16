package be.nicholasmeyers.camerarecording;

import java.util.List;

public class Config {

    private List<Camera> cameras;
    private String outputFolder;

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
}
