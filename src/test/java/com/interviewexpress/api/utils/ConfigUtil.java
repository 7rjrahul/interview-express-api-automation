package com.interviewexpress.api.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class ConfigUtil {

    private static final Properties properties = new Properties();

    static {
        try {
            String env = System.getProperty("env", "dev");

            File localFile = new File("src/test/resources/config-local.properties");
            String fileName = localFile.exists()
                    ? localFile.getPath()
                    : "src/test/resources/config-" + env + ".properties";

            try (InputStream input = new FileInputStream(fileName)) {
                properties.load(input);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config file", e);
        }
    }

    public static String get(String key) {
        // 1. Check system properties passed via Maven (-Dapi.key=...)
        String systemProp = System.getProperty(key);
        if (systemProp != null && !systemProp.isBlank()) {
            return systemProp;
        }

        // 2. Check environment variables (maps "api.key" -> "API_KEY")
        String envKey = key.toUpperCase().replace('.', '_');
        String envVar = System.getenv(envKey);
        if (envVar != null && !envVar.isBlank()) {
            return envVar;
        }

        // 3. Fallback to properties file
        return properties.getProperty(key);
    }
}