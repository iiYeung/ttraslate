package util;

import domain.YouDaoQuery;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class YouDaoUtil {

    public static final String PROPERTY_RESOURCE_LOCATION = "configure.properties";

    public static String query(YouDaoQuery query) {

        return null;
    }

    public static Map<String, String> loadProperty() throws IOException {
        Map<String, String> map = new HashMap<>();

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        URL url = classLoader.getResource(PROPERTY_RESOURCE_LOCATION);

        if (Objects.isNull(url)) {
            return map;
        }

        FileInputStream fis = new FileInputStream(PROPERTY_RESOURCE_LOCATION);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fis));

        String result = null;

        while ((result = bufferedReader.readLine()) != null) {
            String[] strArr = result.split("=");

            if (strArr.length > 1) {
                map.put(strArr[0], strArr[1]);
            }
        }

        return map;
    }

}
