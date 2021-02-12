package main.java.utils;

import main.java.exception.InvalidTestConfigException;
import org.apache.commons.collections4.MapUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyUtil {

    private final static Properties PROPERTIES = new Properties();
    private final static String APP_PROP_FILE_NAME = "application.properties";

    public static void loadProperties() throws IOException {
        InputStream inputStream;
        inputStream = PropertyUtil.class.getClassLoader().getResourceAsStream(APP_PROP_FILE_NAME);
        if (inputStream != null) {
            try {
                PROPERTIES.load(inputStream);
                if (MapUtils.isEmpty(PROPERTIES))
                    throw new InvalidTestConfigException("Required application properties are not loaded correctly.");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                inputStream.close();
            }
        } else {
            throw new FileNotFoundException("No such file found: " + APP_PROP_FILE_NAME);
        }
    }

    public static String getPropertyValue(String key) {
        return PROPERTIES.getProperty(key);
    }
}
