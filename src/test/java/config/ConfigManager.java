package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigManager {

	private static Properties prop;

    static {

        String env = System.getProperty("env");

        if (env == null) {
            env = "uat";  // default environment
        }

        try {
            String path = "src/test/java/resources/config-" + env + ".properties";
            FileInputStream fis = new FileInputStream(path);

            prop = new Properties();
            prop.load(fis);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load config for environment: " + env);
        }
    }

    public static String get(String key) {
        return prop.getProperty(key);
    }
}