package com.example.teamcity.api.config;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static Config config;
    private Properties properties;

    private final static String CONFIG_PROPERTIES = "config.properties";

    private Config() {
        properties = new Properties();
        loadProperties(CONFIG_PROPERTIES);
    }



    public static Config getConfig() {
        if (config == null) {
            config = new Config();

        }
        return config;
    }

    public void loadProperties(String fileName) {
        try (InputStream stream = Config.class.getClassLoader().getResourceAsStream(fileName)) {
            if (stream == null) {
                System.out.println("File not found");
            }
            properties.load(stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static String getProperty(String name){
        return getConfig().properties.getProperty(name);
    }

}
