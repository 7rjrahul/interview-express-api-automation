package com.interviewexpress.api.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class ConfigUtil {

    /*
     Properties is a built-in Java class that works like a dictionary (key-value pairs).
     It is perfect for loading .properties files because those files store data as key=value.
     Example: base.uri=https://client-api.interview.express
              api.key=aih_dummy_key
    */
    private static final Properties properties = new Properties();

    /*
     This static block runs automatically when the class is first loaded.
     It will load the correct config file and store values in the Properties object.
    */
    static {
        try {
            /* Step 1: Decide which environment to use.
               If no env is passed (-Denv=qa), default is "dev". */
            String env = System.getProperty("env", "dev");

            /* Step 2: Pick the right file.
               If config-local.properties exists → use it (your real secrets).
               Otherwise → use config-dev.properties or config-qa.properties. */
            File localFile = new File("src/test/resources/config-local.properties");
            String fileName = localFile.exists()
                    ? localFile.getPath()
                    : "src/test/resources/config-" + env + ".properties";

            /* Step 3: Load the chosen file into Properties object. */
            try (InputStream input = new FileInputStream(fileName)) {
                properties.load(input);
            }

            /* Step 4: Environment variable override.
               If API_KEY is set in CI/CD pipeline, it will replace whatever is in the file. */
            String apiKey = System.getenv("API_KEY");
            if (apiKey != null && !apiKey.isBlank()) {
                properties.setProperty("api.key", apiKey);
            }

        } catch (Exception e) {
            /* If something goes wrong (file missing, etc.), throw an error. */
            throw new RuntimeException("Failed to load config file", e);
        }
    }

    /*
     Step 5: Simple getter method.
     This lets you fetch values by key, e.g. ConfigUtil.get("base.uri").
    */
    public static String get(String key) {
        return properties.getProperty(key);
    }
}
