package serverConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ServerConfiguration {
    private static Properties properties;

    static {
        try {
            InputStream inputStream = ServerConfiguration.class.getResourceAsStream("/cfg/settings.properties");
            properties = new Properties();
            properties.load(inputStream);
            inputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ServerConfiguration() {
    }

    public static Object getProperty(String propertyKey) {
        Object propertiesValue = properties.get(propertyKey);
        if (propertiesValue == null)
            throw new RuntimeException("No such property: " + propertyKey);
        Object propertiesValueWithAppropriateType =
                recreateWithAppropriateType(propertyKey, (String) propertiesValue);
        return propertiesValueWithAppropriateType;
    }

    private static Object recreateWithAppropriateType(String propertyKey, String propertyValue) {
        switch (propertyKey) {
            case "port":
                return Integer.parseInt(propertyValue);
            case "entry":
                return propertyValue;
            case "jdbcDriverName":
                return propertyValue;
            case "connectionToDB":
                return propertyValue;
            case "dbUser":
                return propertyValue;
            case "dbPassword":
                return propertyValue;
            default:
                throw new RuntimeException("Unknown property key: " + propertyKey + " with value " + propertyValue);
        }
    }
}