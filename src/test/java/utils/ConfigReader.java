package utils;

import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static final Properties prop = new Properties();

    static {
        try {
            String env = System.getProperty("env", "qa");
            String fileName = "config-" + env + ".properties";

            InputStream is = ConfigReader.class
                    .getClassLoader()
                    .getResourceAsStream(fileName);

            if (is == null) {
                throw new RuntimeException(
                        "❌ Config file not found: " + fileName +
                                " (env=" + env + ")"
                );
            }

            prop.load(is);

        } catch (Exception e) {
            // Print root cause clearly
            e.printStackTrace();
            throw new ExceptionInInitializerError(e);
        }
    }

    public static String getProperty(String key) {
        return prop.getProperty(key);
    }
}
