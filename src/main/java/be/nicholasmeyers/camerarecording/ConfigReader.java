package be.nicholasmeyers.camerarecording;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;

public class ConfigReader {

    public Config readConfig() throws IOException {
        Yaml yaml = new Yaml();
        Config config = yaml.loadAs(new FileInputStream("config.yml"), Config.class);

        for (Camera camera : config.getCameras()) {
            System.out.println(camera.getName() + " -> " + camera.getUrl());
        }

        System.out.println("Output folder: " + config.getOutputFolder());

        return config;
    }

}
