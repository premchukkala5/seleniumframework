package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

   public static Properties prop;

    static {
        try {
            String env = System.getProperty("env", "test");
           // FileInputStream fis = new FileInputStream("src/test/resources/config-"+env+".properties");
            String fileName = "config-" + env + ".properties";
           InputStream fis = ConfigReader.class.getClassLoader().getResourceAsStream(fileName);
            if (fis == null) {
                throw new RuntimeException(
                        "❌ Config file not found: " + fileName +
                                " (env=" + env + ")"
                );
            }

            prop = new Properties();
            prop.load(fis);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getProperty(String key){
        return prop.getProperty(key);
    }

}

