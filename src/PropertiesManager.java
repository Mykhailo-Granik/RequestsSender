import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * This class reads a Properties object on creation.
 * It provides methods for getting the total number of requests and url to send requests to.
 */
public class PropertiesManager {

    private static final String PROPERTIES_FILE_PATH = "config.properties";
    private static final String REQUESTS_NUMBER_PROPERTY_KEY = "requests.number";
    private static final String URL_PROPERTY_KEY = "url";

    private Properties properties;

    public PropertiesManager() throws IOException {
        InputStream propertiesStream = new FileInputStream(PROPERTIES_FILE_PATH);
        properties = new Properties();
        properties.load(propertiesStream);
    }

    public int getRequestsNumber() {
        return Integer.parseInt(properties.getProperty(REQUESTS_NUMBER_PROPERTY_KEY));
    }

    public String getUrl() {
        return properties.getProperty(URL_PROPERTY_KEY);
    }

}
